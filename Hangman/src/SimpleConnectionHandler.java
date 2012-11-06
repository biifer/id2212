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
	private static final int IN_GAME = 1;
	int state, round = 0;
	boolean rightGuess;
	int totalScore;
	BufferedReader in = null;
	PrintWriter out;

	public SimpleConnectionHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@SuppressWarnings("resource")
	public static void fileRead() {
		try {

			FileInputStream fStream = new FileInputStream("src\\words.txt");
			DataInputStream fIn = new DataInputStream(fStream);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fIn));
			String line = reader.readLine();

			while (line != null) {
				wordList.add(line);
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
			in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);

			while (true) {

				try {
					clientMessage = in.readLine();
				} catch (IOException e2) {
					System.out.println("Client disconnected");
					break;
				}

				rightGuess = false;
				if (state == 0) {
					if (clientMessage.equals("start game")) {
						numberOfAttempts = 10;
						round = 1;
						System.out.println("start game message recieved");
						fileRead();
						word = chooseWord();
						buffer = new StringBuffer();
						for (int i = 0; i < word.length(); i++) {
							buffer.append("-");
							secretWord = buffer.toString();
						}
						out.println(secretWord);
						System.out.println("The secret word is: " + word);
					}

				}
				if (state == IN_GAME) {

					if (clientMessage.length() == 1) {
						// This is a normal letter guess.
						char letter = clientMessage.charAt(0);
						if (!guessList.contains(letter)) {
							guessList.add(letter);
							System.out.println("Player guessed: " + letter);
							for (int i = 0; i < word.length(); i++) {
								if (word.charAt(i) == letter) {
									buffer.setCharAt(i, letter);
									rightGuess = true;
								}

							}

							if (!rightGuess) {
								numberOfAttempts--;
								out.println("Wrong. You have "
										+ numberOfAttempts + " "
										+ "attempts left.");

							}

							secretWord = buffer.toString();
							out.println(secretWord);

							if (secretWord.indexOf("-") == -1) {
								out.println("You Guessed right! The secret word was: "
										+ word);
								round = 0;
								state = 0;
								guessList.clear();
								totalScore++;
								out.println("Total score: " + totalScore);
								out.println("Send 'start game' to start a new game.");
							}
						} else if (guessList.contains(letter)) {
							out.println("You have already guessed: " + letter);
							out.println("Try again with another letter or a whole word: ");
							out.println(secretWord);
						}

					} else if (clientMessage.equals(word)) {
						out.println("You guessed right! The secret word was: "
								+ word);
						round = 0;
						state = 0;
						guessList.clear();
						totalScore++;
						out.println("Total score: " + totalScore);
						out.println("Send 'start game' to start a new game.");
					} else {
						numberOfAttempts--;
						out.println("Wrong. You have " + numberOfAttempts + " "
								+ "attempts left.");
						out.println(secretWord);
					}

					if (numberOfAttempts == 0) {
						out.println("Game Over. Send 'start game' to start a new game.");
						guessList.clear();
						round = 0;
						state = 0;
						if (totalScore > 0)
							totalScore--;
						out.println("Total score: " + totalScore);
					}
				}
				if (round == 1 && state == 0)
					state = IN_GAME;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			in.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}