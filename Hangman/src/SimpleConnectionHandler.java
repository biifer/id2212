import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class SimpleConnectionHandler extends Thread {
	private Socket clientSocket;
	static String word = null;
	static ArrayList<String> wordList = new ArrayList<String>();
	static ArrayList<Character> guessList = new ArrayList<Character>();
	int numberOfAttempts = 10;
	static String clientMessage, secretWord = null;
	static StringBuffer buffer = null;
	public SimpleConnectionHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

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
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);

			

			while (clientSocket.isConnected()) {
				clientMessage = in.readLine();
				if (clientMessage.length() == 1) {
					// This is a normal letter guess.
					char letter = clientMessage.charAt(0);
					if (!guessList.contains(letter)) {
						guessList.add(letter);
						System.out.println("Player guessed: " + letter);
						for (int i = 0; i < word.length(); i++) {
							if (word.charAt(i) == letter) {
								buffer.setCharAt(i, letter);
							}

						}
						secretWord = buffer.toString();
						out.println(secretWord);
						numberOfAttempts--;
						if (secretWord.indexOf("-") == -1) {
							out.println("You Guessed right! The secret word was: " + word);
						}
					} else {
						out.println("You have already guessed: " + letter);
						out.println(secretWord);
						out.print("Try again with another letter or a whole word: ");
						
					}
				} else if (clientMessage.equals("game start")) {
					numberOfAttempts = 10;
					System.out.println("game start message recieved");
					fileRead();
					word = chooseWord();
					buffer = new StringBuffer();
					for (int i = 0; i < word.length(); i++) {
						buffer.append("-");
						secretWord = buffer.toString();
					}
					out.println(secretWord);
					System.out.println("The secret word is: " + word);
				} else {
					if (clientMessage.equals(word)) {
						out.println("You guessed right! The secret word was: "
								+ word);
						guessList.clear();
					} else {
						numberOfAttempts--;
						out.println("Wrong. You have " + numberOfAttempts + " "
								+ "attempts left.");
					}

				}

				if (numberOfAttempts == 0)
					out.println("Game Over. Send 'start game' to start a new game.");
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