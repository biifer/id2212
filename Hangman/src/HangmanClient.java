import java.io.*;
import java.net.*;

public class HangmanClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		Socket clientSocket = null;
		PrintWriter out = null;

		try {
			clientSocket = new Socket(args[0], 1234);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + args[0] + ".");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " +
					"the connection to: " + args[0] + "");
			System.exit(1);
		}


		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		(new ClientCommunication(clientSocket)).start();

		String msg;
		while ((msg = in.readLine()) != null) {
			out.println(msg);

		}
		in.close();
		out.close();
		clientSocket.close();

	}

}
