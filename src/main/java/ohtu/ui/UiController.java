package ohtu.ui;

import ohtu.io.IO;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

public class UiController {

	private IO io;

	public UiController(IO io) {
		this.io = io;
	}

	public void addSuccess(String type) {
		io.println("A new " + type.trim() + " has been created and saved to the database.");
	}

	public void addFailure() {
		io.println("Could not add your new bookmark to the database. Please try again.");
	}

	public void printGreeting() {
		io.println("Welcome to " + ohtu.main.Main.APP_NAME + "!\n");
	}

	public void printGoodbye() {
		io.println("Thanks for using " + ohtu.main.Main.APP_NAME + ".");
	}

	public Object[] askBlogpostData() {

		io.println("Adding a new blogpost.");

		String title = askForString("Title:", false);
		String author = askForString("Author:", false);
		String url = askForString("URL:", false);

		Date date = Date.from(Instant.now());

		return new Object[]{title, author, url, date};
	}

	/**
	 * Asks the user for a string. Optionally doesn't allow empty strings.
	 * @param prompt What to ask (prompt) for the user.
	 * @param allowEmpty If true, the string can be empty. Otherwise it cannot.
	 * @return The string the user typed in.
	 */
	public String askForString(String prompt, boolean allowEmpty) {

		String data = "";

		while (true) {
			data = io.readLine(prompt);
			if (data.isEmpty()) {
				if (allowEmpty) {
					break;
				} else {
					io.println("Please write something.");
				}
			} else {
				break;
			}
		}
		return data;
	}

	/**
	 * printlns all the available instructions for the user.
	 */
	public void printInstructions() {
		io.println("L: List all blogposts");
		io.println("A: Add a new blogpost");
		io.println("E: Exit from the app");
		io.println("");
	}

	/**
	 * Continuously ask the user for a character, until a valid one is given.
	 * @param allowedChars A list of chars that are accepted.
	 * @return A valid uppercase character.
	 */
	public char askForCharacter(char[] allowedChars) {

		while (true) {

			String next = io.readLine("Your choice:");

			if (next.isEmpty()) {
				io.println("Please enter something. For a list of available commands, type 'X'.");
				continue;
			}

			if (next.length() > 1) {
				io.println("Please only enter 1 character.");
				continue;
			}

			char input = next.toUpperCase().charAt(0);

			boolean found = false;

			for (char c : allowedChars) {
				if (input == c || input == 'X') {
					found = true;
					break;
				}
			}

			if (!found) {
				io.println("Please enter a character from the following: " + Arrays.toString(allowedChars));
			} else {
				return input;
			}
		}

	}
}
