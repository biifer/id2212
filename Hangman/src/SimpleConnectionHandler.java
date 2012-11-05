import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;



public class SimpleConnectionHandler extends Thread {
	private Socket clientSocket;

	public SimpleConnectionHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	static String word = null;
	static int port = 1234;
	static ArrayList<String> wordList = new ArrayList<String>();

	@SuppressWarnings("resource")
	public static void fileRead() {
		try {

			FileInputStream fStream = new FileInputStream("src\\words.txt");
			DataInputStream in = new DataInputStream(fStream);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line = reader.readLine();

			while (line != null) {
				String[] wordsLine = line.split(" ");
				for (String word : wordsLine) {
					wordList.add(word);
				}
				line = reader.readLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String chooseWord() {
		Random rand = new Random();
		return wordList.get(rand.nextInt(wordList.size()));
	}
	
	
	public void run() {
		BufferedInputStream in;
		BufferedOutputStream out;

		try {
			in = new BufferedInputStream(clientSocket.getInputStream());
			out = new BufferedOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			System.out.println(e.toString());
			return;
		}
		String startMsg = "startgame";
		byte[] startGameMsg = new byte [1024];
		startGameMsg = startMsg.getBytes();
		
			byte[] msg = new byte[startGameMsg.length];
			try {
				in.read(msg);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			int numberOfAttempts = 10;
			if (new String(msg).equals(startMsg)) {

				try {
				
					fileRead();
					word = chooseWord();
					String hyphen = "ivangay";
					byte[] sendWord = null; 
					for(int i=0; i<word.length(); i++) {
						sendWord = hyphen.getBytes();
						out.write(sendWord);
						out.flush();
						msg = null;
					}
					while ( numberOfAttempts > 0 ){
					in.read(msg);
					System.out.println(new String(msg));
					//if()
					numberOfAttempts--;
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				
			}

		
		try {
			out.close();
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}