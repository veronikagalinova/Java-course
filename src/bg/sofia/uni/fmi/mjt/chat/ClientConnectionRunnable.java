package bg.sofia.uni.fmi.mjt.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientConnectionRunnable implements Runnable {

	private String username;
	private Socket socket;

	public ClientConnectionRunnable(String username, Socket socket) {
		this.username = username;
		this.socket = socket;
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

			while (true) {
				String commandInput = reader.readLine();

				if (commandInput != null) {
					String[] tokens = commandInput.split("\\s+");
					String command = tokens[0];
					
					if ("send".equals(command)) {
						String to = tokens[1];
						String message = commandInput.substring(commandInput.indexOf(to) + to.length());
						sendMessage(message, to, writer);
					} else if ("list-users".equals(command)) {
						listUsers(writer);
					} else if ("send-all".equals(command)) {
						sendMessageToAll(commandInput, writer);
					} else if ("disconnect".equals(command)) {
						writer.println("disconnected");
						ChatServer.disconnectUser(username);
						break;
					} else {
						writer.println("Command " + command + " unknown!");
					}
				}
			}
		} catch (IOException e) {
			System.out.println("ClientConnectionRunnable::" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Exception occured while closing client socket");
			}
		}
	}

	private void listUsers(PrintWriter writer) {
		Map<String, Socket> users = getOtherUsers();
		if (users.isEmpty()) {
			writer.println("nobody is online");
		} else {
			users.entrySet().stream().map(ChatServer::formatUserData).forEach(writer::println);
		}
	}

	private void sendMessageToAll(String commandInput, PrintWriter writer) throws IOException {
		Map<String, Socket> users = getOtherUsers();
		if (users.isEmpty()) {
			writer.println("nobody is online");
			return;
		}
		
		String message = commandInput.substring(commandInput.indexOf("send-all") + "send-all".length());
		users.entrySet().forEach(userEntry -> {
			try {
				sendMessage(message, userEntry.getKey(), writer);
			} catch (IOException e) {
				System.out.println("Error while sending message to all!");
				e.printStackTrace();
			}
		});
	}

	private void sendMessage(String message, String receiver, PrintWriter writer) throws IOException {
		Socket toSocket = ChatServer.getUser(receiver);
		if (toSocket == null) {
			writer.println(String.format("=> %s seems to be offline", receiver));
			return;
		}
		PrintWriter toWriter = new PrintWriter(toSocket.getOutputStream(), true);
		String formattedOutput = formattedMessageToSend(receiver, message);
		toWriter.println(formattedOutput);
		System.out.println("message is " + message);
		System.out.println("successfully send message");
	}
	
	private String formattedMessageToSend(String receiver, String message) {
		LocalDateTime connectionTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(connectionTime.format(formatter)).append("]")
			.append(" ").append(username).append(":").append(" ")
			.append(message.trim());
		return sb.toString();
	}
	
	private Map<String, Socket> getOtherUsers() {
		Map<String, Socket> users = ChatServer.getUsers();
		return users.entrySet()
				.stream()
				.filter(userEntry -> !userEntry.getKey().equals(username))
				.collect(Collectors.toMap(Map.Entry<String, Socket>::getKey,
						Map.Entry<String, Socket>::getValue));
	}

}