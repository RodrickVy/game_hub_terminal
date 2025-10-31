import java.util.Scanner;

/**
 * COMP2522 Term Project: Terminal-Based Games Suite
 * <p>
 * This project implements a suite of three interactive, terminal-based games
 * that adhere to the best practices and principles of object-oriented design
 * learned throughout the course.
 * <p>
 * The application's main entry point is the Main class, which offers a menu
 * to access the three core games:
 * <p>
 * 1. WordGame: A geography trivia game. It requires loading data from 26
 * external text files (a.txt to z.txt) into structured data, specifically
 * Country and World classes, for efficient gameplay. The user is asked ten
 * randomized questions and their score is tracked across multiple attempts.
 * <p>
 * 2. NumberGame: A number-based game (details to be implemented).
 * <p>
 * 3. MyGame: A user-defined third game (details to be implemented).
 * <p>
 * Emphasis has been placed on clear user prompts, robust error handling in the
 * terminal menu, and a well-structured design to ensure maintainability and
 * adherence to code style standards.
 *
 * @author Rodrick Vyizigiro
 * @version 1.0.0
 * @since 2025-10-31
 */
public class Main
{

    /**
     * Title displayed at the top of the main game selection menu.
     */
    private static final String MENU_TITLE = "WELCOME TO THE GAME SELECTOR";

    /**
     * The instruction prompt shown to the user in the main menu.
     */
    private static final String MENU_PROMPT = "Please choose a game or quit:";

    /**
     * Menu option text for starting the Number Game.
     */
    private static final String OPTION_NUMBER_GAME = " [N] Number Game";

    /**
     * Menu option text for starting the Word Game.
     */
    private static final String OPTION_WORD_GAME = " [W] Word Game";

    /**
     * Menu option text for starting the custom MyGame.
     */
    private static final String OPTION_MY_GAME = " [M] My Game";

    /**
     * Menu option text for exiting the program.
     */
    private static final String OPTION_QUIT = " [Q] Quit";

    /**
     * String used to visually separate sections of the menu output.
     */
    private static final String MENU_SEPARATOR = "-----------------------------";

    /**
     * The single-character command used by the user to quit the application.
     */
    private static final String QUIT_COMMAND = "Q";

    /**
     * The single-character command used by the user to select the Number Game.
     */
    private static final String NUMBER_GAME_COMMAND = "N";

    /**
     * The single-character command used by the user to select the Word Game.
     */
    private static final String WORD_GAME_COMMAND = "W";

    /**
     * The single-character command used by the user to select the MyGame.
     */
    private static final String MY_GAME_COMMAND = "M";

    /**
     * The main entry point for the program.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args)
    {
        startApplication();
    }

    /**
     * Initializes the menu, processes user input, and launches the selected game
     * or quits the application.
     */
    private static void startApplication()
    {
        displayMenu();
        promptUser();

        final Scanner userInputScanner;

        userInputScanner = new Scanner(System.in);

        while (userInputScanner.hasNextLine())
        {

            final String userInput;
            final boolean userIsQuitting;
            final boolean userChoseNumberGame;
            final boolean userChoseWordGame;
            final boolean userChoseMyGame;

            userInput = userInputScanner.nextLine().trim();

            userIsQuitting      = checkCommand(userInput, QUIT_COMMAND);
            userChoseNumberGame = checkCommand(userInput, NUMBER_GAME_COMMAND);
            userChoseWordGame   = checkCommand(userInput, WORD_GAME_COMMAND);
            userChoseMyGame     = checkCommand(userInput, MY_GAME_COMMAND);


            if (userIsQuitting)
            {
                System.out.println("Quitting...");
                break;
            }
            else if (userChoseNumberGame)
            {
                System.out.println("Starting number game...");
                // TODO: launch number game
            }
            else if (userChoseWordGame)
            {
                System.out.println("Starting my game...");
                // TODO: launch word game
            }
            else if (userChoseMyGame)
            {
                System.out.println("Starting word game...");
                // TODO: launch my game
            }
            else
            {
                System.out.println("Unknown command. Try again.");
                promptUser();
            }
        }

        userInputScanner.close();
    }

    /**
     * Compares user input with a command string, ignoring case.
     *
     * @param input   The raw string entered by the user.
     * @param command The expected command string (e.g., "W", "Q").
     * @return True if the input matches the command, false otherwise.
     */
    private static boolean checkCommand(final String input,
                                        final String command)
    {
        return input.equalsIgnoreCase(command);
    }


    /**
     * Prints the complete game selection menu to the console.
     */
    private static void displayMenu()
    {
        System.out.println(MENU_SEPARATOR);
        System.out.println(MENU_TITLE);
        System.out.println(MENU_SEPARATOR);
        System.out.println(OPTION_NUMBER_GAME);
        System.out.println(OPTION_WORD_GAME);
        System.out.println(OPTION_MY_GAME);
        System.out.println(OPTION_QUIT);
    }

    /**
     * Prints the prompt asking the user to make a menu selection.
     */
    private static void promptUser()
    {
        System.out.println(MENU_PROMPT);
    }


}


