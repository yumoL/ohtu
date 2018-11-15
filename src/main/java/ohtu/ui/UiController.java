package ohtu.ui;

import java.util.Arrays;
import java.util.Scanner;

public class UiController {

	// I'll implement the mock UI component later (Riku).

	private static final String APP_NAME = "Bookmarks Database";

	private Scanner scanner;

	public UiController(Scanner scanner) {
		this.scanner = scanner;
	}

	public void printGreeting() {
		System.out.println("Welcome to " + APP_NAME + "!\n");
	}

	public void printGoodbye() {
		System.out.println("Thanks for using " + APP_NAME + ".");
	}

	public void addBlogpost() {
		System.out.println("Adding a new blogpost.");
		String title = askForString("Title:", false);
		String author = askForString("Author:", false);
		String url = askForString("URL:", false);
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
			System.out.print(prompt.trim() + " ");
			data = scanner.nextLine();
			if (data.length() == 0) {
				if (allowEmpty) {
					break;
				} else {
					System.out.println("Please write something.");
				}
			} else {
				break;
			}
		}
		return data;
	}

	/**
	 * Prints all the available instructions for the user.
	 */
	public void printInstructions() {
		System.out.println("L: List all blogposts");
		System.out.println("A: Add a new blogpost");
		System.out.println("E: Exit from the app");
		System.out.println();
	}

	/**
	 * Continuously ask the user for a character, until a valid one is given.
	 * @param allowedChars A list of chars that are accepted.
	 * @return A valid uppercase character.
	 */
	public char askForCharacter(char[] allowedChars) {

		while (true) {

			System.out.print("Your choice (type 'X' to list all available commands): ");

			while (scanner.hasNextLine()) {

				String next = scanner.nextLine();

				if (next.isEmpty()) {
					System.out.println("Please enter something.");
					continue;
				}

				if (next.length() > 1) {
					System.out.println("Please only enter 1 character.");
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
					System.out.println("Please enter a character from the following: " + Arrays.toString(allowedChars));
				} else {
					return input;
				}
			}
		}
	}
}
