import java.io.*;
import java.net.*;

public class HangmanServer {

	public static void main(String[] args) throws IOException {
		boolean listening = true;
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(1234);
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (listening) {
			System.out.println("Waiting...");
			Socket clientSocket = serverSocket.accept();
			System.out.println("accepted, starting new thread");
			(new ServerCommunication(clientSocket)).start();
		}
		serverSocket.close();
	}

}
