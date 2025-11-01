package word_game;

import java.util.*;

/**
 * Manages the entire Word Game, which is a terminal-based geography trivia challenge.
 * <p>
 * This class handles game initialization, including loading all country and city data
 * from the external a.txt through z.txt files into a World data structure.
 * It runs ten random trivia questions per round, tracks detailed player scores
 * (first attempt, second attempt, incorrect), and manages the continuous "Play Again"
 * loop. Upon quitting, it calculates the player's final score and updates the
 * external "score.txt" file to check for a new high score.
 *
 * @author Rodrick Vyizigiro
 * @version 1.0.0
 * @see World
 * @see Country
 * @see Score
 * @since 2025-10-31
 */
public class WordGame
{
    /**
     * Strictly representing the numerical value of one.
     */
    private static final int ONE = 1;

    /**
     * The fixed number of trivia questions asked in every game round.
     */
    private static final int MAX_QUESTIONS_PER_ROUND = 10;

    /**
     * The path to the directory containing the country data files (a.txt to z.txt).
     */
    private static final String COUNTRIES_DATA_DIRECTORY = "data/countries";

    /**
     * Symbolic constant representing the fixed value to increment game counts by.
     */
    private static final int GAME_COUNT_INCREMENT = 1;

    /**
     * The file name used for saving and loading the historical score records.
     */
    private static final String SCORES_FILE_NAME = "score.txt";

    /**
     * The lowest valid index for a question type (inclusive).
     */
    private static final int MIN_QUESTION_TYPE = 0;

    /**
     * The exclusive upper bound for random question type generation.
     */
    private static final int MAX_QUESTION_TYPE = 3;

    /**
     * The minimum index or count used for accessing or iterating lists/arrays .
     */
    private static final int MIN_COUNTRY_INDEX = 0;

    /**
     * The precise format string used to print the high score congratulatory message.
     */
    private static final String CONGRATS_MESSAGE_FORMAT =
        "CONGRATULATIONS! You are the new high score with an " +
            "average of %.2f points per game; the previous record was" +
            " %.2f points per game on %s.";

    /**
     * The lowest valid index in the facts array (inclusive).
     */
    private static final int MIN_FACT_INDEX = 0;

    /**
     * The exclusive upper bound for generating a random fact index (0, 1, or 2).
     */
    private static final int MAX_FACT_INDEX = 3;


    private final World gameWorld;
    private final ScoreManager scoreManager;

    final Runnable onGameEnd;

    public WordGame(final Runnable onGameEnd)
    {

        final CountryDataLoader dataLoader;
        final HashMap<String, Country> countriesMap;

        validateOnGameEnd(onGameEnd);
        dataLoader   = new CountryDataLoader(COUNTRIES_DATA_DIRECTORY);
        countriesMap = dataLoader.loadData();
        gameWorld    = new World(countriesMap);
        scoreManager = new ScoreManager(SCORES_FILE_NAME);

        this.onGameEnd = onGameEnd;
    }


    /**
     * Public entry point for playing the WordGame.
     * This method runs the main game loop, asks questions, handles user input,
     * manages scoring across multiple rounds, and handles the "Play Again" option
     * until the user quits.
     */
    public void startGame()
    {
        playGame(ScoreManager.startNewSessionScore());

    }

    private void playGame(Score currentScore)
    {
        final Score currentSessionScore;
        int numGamesPlayed;
        int numCorrectFirstAttempts;
        int numCorrectSecondAttempts;
        int numIncorrectSecondAttempts;


        numGamesPlayed             = currentScore.getNumGamesPlayed() + GAME_COUNT_INCREMENT;
        numCorrectFirstAttempts    = currentScore.getNumCorrectFirstAttempt();
        numCorrectSecondAttempts   = currentScore.getNumCorrectSecondAttempt();
        numIncorrectSecondAttempts = currentScore.getNumIncorrectTwoAttempts();

        for (int questionNumber = 0; questionNumber < MAX_QUESTIONS_PER_ROUND; questionNumber++)
        {
            final Question question;
            final Scanner userInputScanner;

            question = generateRandomQuestion();
            promptUserWithQuestion(questionNumber + ONE, question);
            userInputScanner = new Scanner(System.in);

            while (userInputScanner.hasNextLine())
            {

                final boolean firstTryIsCorrectAnswer;
                firstTryIsCorrectAnswer = question.checkAnswer(userInputScanner.nextLine());


                if (firstTryIsCorrectAnswer)
                {
                    reportUserGotCorrectAnswer();
                    numCorrectFirstAttempts += ONE;
                    break;
                }
                else
                {
                    reportUserGotWrongAnswer();
                    promptUserToTryQuestionAgain();

                    final boolean secondTryIsCorrectAnswer;
                    secondTryIsCorrectAnswer = question.checkAnswer(userInputScanner.nextLine());

                    if (secondTryIsCorrectAnswer)
                    {
                        reportUserGotCorrectAnswer();
                        numCorrectSecondAttempts += ONE;
                        break;
                    }
                    else
                    {
                        reportUserGotWrongAnswer();
                        numIncorrectSecondAttempts += ONE;
                        reportTheCorrectAnswer(question.getAnswer());
                        break;

                    }

                }
            }

        }

        currentSessionScore = new Score(numGamesPlayed,
                                        numCorrectFirstAttempts,
                                        numCorrectSecondAttempts,
                                        numIncorrectSecondAttempts);

        reportSessionScore(currentSessionScore);

        final boolean wantsToKeepPlaying;
        final boolean isNewSessionHighestEver;

        wantsToKeepPlaying      = promptUserToPlayAnotherRound();
        isNewSessionHighestEver = scoreManager.isNewHighScore(currentSessionScore);

        if (wantsToKeepPlaying)
        {
            playGame(currentSessionScore);
        }
        else
        {
            if (isNewSessionHighestEver)
            {
                printNewHighScoreCongratsMessage(
                    currentSessionScore.calculateAverageScorePerGame(),
                    scoreManager.getHighestAverageScore(),
                    scoreManager.getHighestScoreDateTime());
                
                onGameEnd.run();
            }
        }

        scoreManager.saveNewScoreToFile(currentSessionScore);
    }

    private void reportTheCorrectAnswer(final String answer)
    {
        System.out.printf("The correct answer is: %s\n", answer);
    }


    /**
     * Reports the player's cumulative score statistics for the session in the
     * required multi-line format.
     *
     * @param sessionScore The Score object containing the total accumulated stats for all games played.
     */
    private void reportSessionScore(final Score sessionScore)
    {

        final int gamesPlayed;
        final int firstAttempts;
        final int secondAttempts;
        final int incorrectAttempts;

        gamesPlayed       = sessionScore.getNumGamesPlayed();
        firstAttempts     = sessionScore.getNumCorrectFirstAttempt();
        secondAttempts    = sessionScore.getNumCorrectSecondAttempt();
        incorrectAttempts = sessionScore.getNumIncorrectTwoAttempts();

        System.out.println("\n--- Session Score Report ---");

        System.out.println("- " + gamesPlayed + " word game" + (gamesPlayed != ONE ? "s" : "") + " played");
        System.out.println("- " + firstAttempts + " correct answers on the first attempt");
        System.out.println("- " + secondAttempts + " correct answers on the second attempt");
        System.out.println("- " + incorrectAttempts + " incorrect answers on two attempts each");

        System.out.println("----------------------------");
    }


    /**
     * Prints the prompt asking the user for their second guess.
     */
    private void promptUserToTryQuestionAgain()
    {
        System.out.print("\nTry again, last guess: ");
    }

    /**
     * Prints the 'INCORRECT' feedback message to the console.
     */
    private void reportUserGotWrongAnswer()
    {
        System.out.println("INCORRECT.");
    }

    private void reportUserGotCorrectAnswer()
    {
        System.out.println("CORRECT");
    }

    private void promptUserWithQuestion(final int questionNumber,
                                        final Question question)
    {
        System.out.printf("\nQuestion %d : %s \n", questionNumber, question.getPrompt());
    }


    /**
     * Generates a pseudo-random integer within a specified range.
     *
     * @param min The minimum possible random number (inclusive).
     * @param max The maximum possible random number (exclusive).
     * @return A random integer between min (inclusive) and max (exclusive).
     */
    private static int generateRandomNumber(final int min,
                                            final int max)
    {
        final Random randomGenerator;
        randomGenerator = new Random();
        return randomGenerator.nextInt(max - min) + min;
    }


    /**
     * Selects a random country and generates a random trivia question based on one of three types.
     *
     * @return A Question object containing the formatted question prompt, the correct answer,
     * and the question type (zero, one, or two).
     */
    private Question generateRandomQuestion()
    {

        final List<Country> allCountries;
        final int totalNumberOfCountries;
        final Country selectedCountry;

        final int questionType;
        final String prompt;
        final String answer;
        final int factIndex;

        allCountries           = new ArrayList<>(this.gameWorld.getCountries().values());
        totalNumberOfCountries = allCountries.size();

        selectedCountry = allCountries.get(generateRandomNumber(MIN_COUNTRY_INDEX, totalNumberOfCountries));
        questionType    = generateRandomNumber(MIN_QUESTION_TYPE, MAX_QUESTION_TYPE);
        factIndex       = generateRandomNumber(MIN_FACT_INDEX, MAX_FACT_INDEX);

        switch (questionType)
        {
            case 0:
                prompt = "What country is " + selectedCountry.getCapitalCityName() + " the capital of?";
                answer = selectedCountry.getCountryName();
                break;

            case 1:
                prompt = "What is the capital city of " + selectedCountry.getCountryName() + "?";
                answer = selectedCountry.getCapitalCityName();
                break;

            case 2:

                prompt = "Which country is being described by this fact: " + selectedCountry.getFacts()[factIndex];
                answer = selectedCountry.getCountryName();
                break;

            default:
                prompt = "Error: Question generation failed.";
                answer = "";
                break;
        }

        return new Question(prompt, answer, questionType);
    }


    /**
     * Prompts the user to continue playing until a valid "Yes" or "No" response is received.
     *
     * @return true if the user chooses to play again, false otherwise.
     */
    private boolean promptUserToPlayAnotherRound()
    {
        final Scanner inputScanner;
        inputScanner = new Scanner(System.in);

        boolean playAgain = false;

        System.out.println("\nDo you want to play another round? (Yes/No)");
        while (inputScanner.hasNextLine())
        {
            final String userInput;

            userInput = inputScanner.nextLine().trim();

            if (userInput.equalsIgnoreCase("Yes"))
            {
                playAgain = true;
                break;
            }
            else if (userInput.equalsIgnoreCase("No"))
            {
                playAgain = false;

                break;

            }
            else
            {
                System.out.println("Invalid input. Please enter 'Yes' or 'No'.");
                promptUserToPlayAnotherRound();
                break;
            }
        }
        return playAgain;
    }

    /**
     * Prints the congratulatory message when a player achieves a new all-time high score.
     *
     * @param newAverage       The player's average score that set the new record.
     * @param previousAverage  The previous highest average score.
     * @param previousDateTime The date and time of the previous high score record.
     */
    private static void printNewHighScoreCongratsMessage(final double newAverage,
                                                         final double previousAverage,
                                                         final String previousDateTime)
    {

        final String formattedMessage;

        formattedMessage = String.format(CONGRATS_MESSAGE_FORMAT,
                                         newAverage,
                                         previousAverage,
                                         previousDateTime);

        System.out.println(formattedMessage);
    }

    // Inside WordGame.java

    /**
     * Validates that the Runnable callback provided for game cleanup is not null.
     *
     * @param onGameEnd The Runnable object to validate.
     * @throws IllegalArgumentException if the onGameEnd parameter is null, as the game needs
     *                                  a defined action to perform upon completion.
     */
    private static void validateOnGameEnd(final Runnable onGameEnd)
    {
        if (onGameEnd == null)
        {
            throw new IllegalArgumentException("The onGameEnd callback cannot be null.");
        }
    }

}
