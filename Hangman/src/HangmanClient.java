import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;



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
		
		 BufferedReader sIn = new BufferedReader(new InputStreamReader( clientSocket.getInputStream()));
		 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		 out = new PrintWriter(clientSocket.getOutputStream(), true);
		   String msg;
		   
	        while ((msg = in.readLine()) != null) {
	            out.println(msg);
	            System.out.println(sIn.readLine());
	        }
		 
		 
				clientSocket.close();
		
	}

}
