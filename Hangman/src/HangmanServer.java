import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class HangmanServer {

	//HAX - v2
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		boolean listening = true;
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

	}

}
