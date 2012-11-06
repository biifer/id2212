import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class HangmanServer {


	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Boolean listening = true;
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(1234);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (listening) {
			System.out.println("Waiting...");
			Socket clientSocket = serverSocket.accept();
			System.out.println("accepted, starting new thread");
			(new SimpleConnectionHandler(clientSocket)).start();
		}
		serverSocket.close();
	}

}
