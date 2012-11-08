import java.io.*;
import java.net.*;

public class HangmanClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		Socket clientSocket = null;
		PrintWriter out = null;
		String host;
		int port;
		
		if(args.length > 0)
			host = args[0];
		else host = "localhost";
		if(args.length > 1)
			port = Integer.parseInt(args[1]);
		else port = 1234;
		
		try {
			clientSocket = new Socket(host, port);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: " + host + ".");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for " +
					"the connection to: " + host + "");
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
