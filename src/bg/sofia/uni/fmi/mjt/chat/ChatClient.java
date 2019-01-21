package bg.sofia.uni.fmi.mjt.chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient implements Runnable {
	private static final int PORT = 4444;
	private PrintWriter writer;

	public static void main(String[] args) {
		new ChatClient().run();
	}

	public void run() {
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String input = scanner.nextLine();
				String[] tokens = input.split(" ");
				String command = tokens[0];
				
				if ("disconnect".equals(command)) {
					writer.println(input);
					break;
				} else if ("connect".equals(command)) {
					String host = tokens[1];
					int port = Integer.parseInt(tokens[2]);
					String username = tokens[3];
					connect(host, port, username);
				} else {
					writer.println(input);
				}

			}
		}
	}

	private void connect(String host, int port, String username) {
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			writer = new PrintWriter(socket.getOutputStream(), true);

			System.out.printf("connected to server running on localhost:%d as %s%n",PORT,username);
			writer.println(username);

			ClientRunnable clientRunnable = new ClientRunnable(socket);
			new Thread(clientRunnable).start();
		} catch (IOException e) {
			System.out.println("=> cannot connect to server on localhost:8080, make sure that the server is started");
		} 
	}
}
