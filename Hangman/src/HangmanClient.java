import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;





public class HangmanClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		Socket clientSocket = null;

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
		
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		
		in = new BufferedInputStream(clientSocket.getInputStream());
		out = new BufferedOutputStream(clientSocket.getOutputStream());
		
		byte[] toServer = args[1].getBytes();
		out.write(toServer);
		out.flush();
		
		byte[] fromServer = new byte[1024];
		in.read(fromServer, 0, fromServer.length);
		
		System.out.println(new String(fromServer));
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = br.readLine();
		toServer  = input.getBytes();
		out.write(toServer);
		in.close();
		out.close();
		
	}

}
