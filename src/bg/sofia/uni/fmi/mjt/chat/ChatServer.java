package bg.sofia.uni.fmi.mjt.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

	private static final int PORT = 4444;
	private static final int MAX_EXECUTOR_THREADS = 10;

	private static Map<String, Socket> users = new HashMap<>();
	private static Map<String,String> usersConnectionDate = new HashMap();

	public static Socket getUser(String username) {
		return users.get(username);
	}
	
	
	public static Map<String, Socket> getUsers() {
		return Collections.unmodifiableMap(users);
	}
	
	public static String formatUserData(Map.Entry<String,Socket> userEntry) {
		StringBuilder sb = new StringBuilder();
		String user = userEntry.getKey();
		sb.append(user).append(", connected at ").append(usersConnectionDate.get(user));
		return sb.toString();
	}
	
	public static void disconnectUser(String user) {
			users.remove(user);
			System.out.println(user + " disconnected");
	}

	public static void main(String[] args) {
		ExecutorService executor = Executors.newFixedThreadPool(MAX_EXECUTOR_THREADS);
		
		try (ServerSocket serverSocket = new ServerSocket(PORT)) {
			System.out.printf("server is running on localhost:%d%n", PORT);

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("A client connected to server " + socket.getInetAddress());

				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				String username = reader.readLine();
				connectUser(username,socket);
				ClientConnectionRunnable clientConnectionHandler = new ClientConnectionRunnable(username, socket);
				executor.execute(clientConnectionHandler);
			}
		} catch (IOException e) {
			System.out.println("maybe another server is running or port 8080");
		}
	}
	
	private static void connectUser(String username, Socket socket) {
		LocalDateTime connectionTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
		usersConnectionDate.put(username, connectionTime.format(formatter));	
		users.put(username, socket);
		System.out.println(username + " connected");
	}

}
