package bg.sofia.uni.fmi.mjt.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientRunnable implements Runnable {

	private Socket socket;

	public ClientRunnable(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(socket.getInputStream()));
			while (true) {
				String serverResponse = reader.readLine();
				if ("disconnected".equals(serverResponse)) {
					closeConnection();
					return;
				}
				System.out.println(serverResponse.trim());
			}
		} catch (IOException e) {
			System.out.println(e.getMessage() + " exception in ClientRunnable +++++++++");
		}
	}
	
	private void closeConnection() {
		System.out.println("disconnected from server on localhost:4444");
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println("Error occured while closing client socket!");
		}
	}

}
