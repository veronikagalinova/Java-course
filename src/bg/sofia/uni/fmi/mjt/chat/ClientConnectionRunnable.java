package bg.sofia.uni.fmi.mjt.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
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
						String message = commandInput.substring(commandInput.indexOf("send") + "send".length());
						sendMessage(message, to, writer);
					} else if ("list-users".equals(command)) {
						listUsers(writer);
					} else if ("send-all".equals(command)) {
						String message = commandInput.substring(commandInput.indexOf("send-all") + "send-all".length());
						sendMessageToAll(message, writer);
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
			System.out.println("ClientConnectionRunnable::client socket is closed");
			System.out.println(e.getMessage());
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Exception occured while closing client socket");
			}
		}
	}

	private void listUsers(PrintWriter writer) {
		Map<String, Socket> users = ChatServer.getUsers();
		long usersCount = users.entrySet()
				.stream()
				.filter(userEntry -> !userEntry.getKey()
						.equals(username)).count();
		if (usersCount == 0) {
			// users map cannot be empty 
			// should exclude the current user and then if it is empty ->
			// print nobody is online
			writer.println("nobody is online");
		} else {
			users.entrySet().stream().filter(userEntry -> !userEntry.getKey().equals(username))
			.map(ChatServer::formatUserData)
			.forEach(writer::println);
		}
	}

	private void sendMessageToAll(String message, PrintWriter writer) throws IOException {
		Map<String, Socket> users = ChatServer.getUsers();
		if (users.isEmpty()) {
			writer.println("nobody is online");
			return;
		}

		for (Map.Entry<String, Socket> userEntry : users.entrySet()) {
			sendMessage(message, userEntry.getKey(), writer);
		}

	}

	private void sendMessage(String message, String receiver, PrintWriter writer) throws IOException {
		Socket toSocket = ChatServer.getUser(receiver);
		if (toSocket == null) {
			writer.println(String.format("=> %s seems to be offline", receiver));
			return;
		}

		PrintWriter toWriter = new PrintWriter(toSocket.getOutputStream(), true);
		toWriter.println(String.format("[%s]: %s", username, message));
		System.out.println("message is " + message);
		System.out.println("successfully send message");
	}

}