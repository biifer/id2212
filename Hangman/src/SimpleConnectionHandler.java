import java.io.*;
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
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
			        clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			
			String msg, sWord = null;
			StringBuffer buffer = null;
		    while (clientSocket.isConnected()) {
		    	msg = in.readLine();
		        if(msg.equals("game start")){
		        	fileRead();
					word = chooseWord();
		        	buffer = new StringBuffer();
		        	for(int i=0; i<word.length(); i++) {
						buffer.append("-");
						sWord = buffer.toString();		
					}
		        	out.println(sWord);
		        }
		        else
		        	out.println("");
		        
		    }
		    
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}