import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ClientCommunication extends Thread  {
	private Socket clientSocket;
	BufferedReader sIn;

	public ClientCommunication(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void run() {


		try {
			sIn = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
			while (true) {
				System.out.println(sIn.readLine());
			}
		} catch (IOException e) {
			System.out.println("server disconnected");
		}

		try {
			sIn.close();
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
