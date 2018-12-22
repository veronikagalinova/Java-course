package bg.sofia.uni.fmi.java.network.server.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;


/**
 * This is a command execution server. It is used to listen for
 * incoming commands, to execute them and to return a response.
 * We have implemented only two commands:
 *  - echo:test - echo all the data after the echo: command
 *  - gethostname - returns the hostname of the remote machine
 */
public class CommandExecutionServer implements AutoCloseable {
	
	public static final int SERVER_PORT = 4444;
	private static final int BUFFER_SIZE = 1024;
	
	private Selector selector;
	private ByteBuffer commandBuffer;
	private boolean runServer = true;
	private ServerSocketChannel serverSocketChannel;

	public CommandExecutionServer(int port) throws IOException {
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(port));
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		commandBuffer = ByteBuffer.allocate(BUFFER_SIZE);
		System.out.println("Server started on port: " + InetAddress.getLocalHost().getHostAddress());
	}

	/**
	 * Start the server
	 * @throws IOException
	 */
	public void start() throws IOException {
		while (runServer) {
			int readyChannels = selector.select();
			if (readyChannels == 0) {
				continue;
			}
			
			Set<SelectionKey> selectedKeys = selector.selectedKeys();
			Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
			
			while (keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				if (key.isReadable()) {
					read(key);
				} else if (key.isAcceptable()) {
					accept(key);
				}
				
				keyIterator.remove();
			}
		}
	}
	
	/**
	 * Accept a new connection
	 * 
	 * @param key The key for which an accept was received
	 * @throws IOException In case of problems with the accept
	 */
	private void accept(SelectionKey key) throws IOException {
		SocketChannel channel = serverSocketChannel.accept();
		if (channel != null) {
			channel.configureBlocking(false);
			channel.register(selector, SelectionKey.OP_READ, channel.socket().getPort());
			System.out.println("Connection accepted " + channel.getLocalAddress());
		}
	}

	/**
	 * Read data from a connection
	 * 
	 * @param key The key for which data was received
	 * @throws IOException 
	 */
	private void read(SelectionKey key) throws IOException {
		SocketChannel channel = (SocketChannel) key.channel();
		
		while (true) {
			commandBuffer.clear();
			int c = channel.read(commandBuffer);
			if (c <= 0) {
				break;
			} 
			
			commandBuffer.flip();
			String command = java.nio.charset.StandardCharsets.UTF_8.decode(commandBuffer).toString();
			System.out.println("command: " + command);
			String result = executeCommand(command);

			System.out.println("result: " + result);
			
			ByteBuffer helper = ByteBuffer.allocate(BUFFER_SIZE);
			helper.put(result.getBytes(java.nio.charset.StandardCharsets.UTF_8));
			helper.flip();
			channel.write(helper);
		}
	}

	/**
	 * Stop the server
	 * @throws IOException
	 */
	public void stop() {
		runServer = false;
	}

	/**
	 * Validate and execute the received commands from the clients
	 * 
	 * @param recvMsg
	 * @return The result of the execution of the command
	 */
	private String executeCommand(String recvMsg) {
		String[] cmdParts = recvMsg.split(":");
		
		if (cmdParts.length > 2) {
			return "Incorrect command syntax" + System.lineSeparator();
		}
		
		String command = cmdParts[0].trim();
		
		if (command.equalsIgnoreCase("echo")) {
			if (cmdParts.length <= 1) {
				return "Missing Argument" + System.lineSeparator();
			}
			return cmdParts[1].trim() + System.lineSeparator();
		} else if (command.equalsIgnoreCase("gethostname")) {
			try {
				return InetAddress.getLocalHost().getHostName() + System.lineSeparator();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return "Could not get hostname" + System.lineSeparator();
			}
		} else {
			return "Unknown command" + System.lineSeparator();
		}
	}
	
	@Override
	public void close() throws Exception {
		serverSocketChannel.close();
		selector.close();
	}

	public static void main(String[] args) throws Exception {
		try (CommandExecutionServer es = new CommandExecutionServer(SERVER_PORT)) {
			es.start();
		} catch (Exception e) {
			System.out.println("An error has occured");
			e.printStackTrace();
		}
	}
}