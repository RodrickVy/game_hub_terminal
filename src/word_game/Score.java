package word_game;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the cumulative score and game statistics for a player's session in the WordGame.
 * * A session is defined as when they start playing till they quit the game.
 *
 * @author Rodrick Vyizigiro
 * @version 1.0.0
 */
public class Score
{
    /**
     * Points awarded for a correct answer on the first guess.
     */
    private static final int POINTS_ON_FIRST_ATTEMPT = 2;

    /**
     * Points awarded for a correct answer on the second guess.
     */
    private static final int POINTS_ON_SECOND_ATTEMPT = 1;

    /**
     * Points awarded for an incorrect answer after both attempts.
     */
    private static final int POINTS_ON_INCORRECT_ATTEMPT = 0;


    /**
     * The required pattern for formatting the game date and time.
     */
    private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final String formattedDateTime;
    private final int numGamesPlayed;
    private final int numCorrectFirstAttempt;
    private final int numCorrectSecondAttempt;
    private final int numIncorrectTwoAttempts;


    /**
     * Constructs a final Score object, summarizing a player's performance.
     *
     * @param numGamesPlayed          The total number of WordGames played in the session.
     * @param numCorrectFirstAttempt  The total number of answers correct on the first guess.
     * @param numCorrectSecondAttempt The total number of answers correct on the second guess.
     * @param numIncorrectTwoAttempts The total number of answers missed after two guesses.
     * @throws IllegalArgumentException if any numerical parameter is negative.
     */
    public Score(final int numGamesPlayed,
                 final int numCorrectFirstAttempt,
                 final int numCorrectSecondAttempt,
                 final int numIncorrectTwoAttempts)
    {


        validateNumGamesPlayed(numGamesPlayed);
        validateNumCorrectFirstAttempt(numCorrectFirstAttempt);
        validateNumCorrectSecondAttempt(numCorrectSecondAttempt);
        validateNumIncorrectTwoAttempts(numIncorrectTwoAttempts);


        this.numGamesPlayed          = numGamesPlayed;
        this.numCorrectFirstAttempt  = numCorrectFirstAttempt;
        this.numCorrectSecondAttempt = numCorrectSecondAttempt;
        this.numIncorrectTwoAttempts = numIncorrectTwoAttempts;

        this.formattedDateTime = createFormattedDateTimeString();
    }


    /**
     * Returns the formatted date and time when this score object was created.
     *
     * @return The date/time string in "yyyy-MM-dd HH:mm:ss" format.
     */
    public String getFormattedDateTime()
    {
        return formattedDateTime;
    }

    /**
     * Returns the total number of WordGames played in this session.
     *
     * @return the number of games played.
     */
    public int getNumGamesPlayed()
    {
        return numGamesPlayed;
    }

    /**
     * Returns the total number of answers guessed correctly on the first attempt.
     *
     * @return the count of first-attempt correct answers.
     */
    public int getNumCorrectFirstAttempt()
    {
        return numCorrectFirstAttempt;
    }

    /**
     * Returns the total number of answers guessed correctly on the second attempt.
     *
     * @return the count of second-attempt correct answers.
     */
    public int getNumCorrectSecondAttempt()
    {
        return numCorrectSecondAttempt;
    }

    /**
     * Returns the total number of questions answered incorrectly after both attempts.
     *
     * @return the count of incorrect answers.
     */
    public int getNumIncorrectTwoAttempts()
    {
        return numIncorrectTwoAttempts;
    }

    /**
     * Calculates the total points awarded for this score session.
     * Points are awarded as: 2 points for first attempt, 1 point for second. [cite: 59, 60]
     *
     * @return the total score in points.
     */
    public int calculateTotalPoints()
    {
        return (numCorrectFirstAttempt * POINTS_ON_FIRST_ATTEMPT) +
            (numCorrectSecondAttempt * POINTS_ON_SECOND_ATTEMPT) +
            (numIncorrectTwoAttempts * POINTS_ON_INCORRECT_ATTEMPT);
    }

    /**
     * Calculates the average score per game across the session.
     * Returns 0.0 if no games were played.
     *
     * @return the average score per game as a double.
     */
    public double calculateAverageScorePerGame()
    {
        if (numGamesPlayed == 0)
        {
            return 0.0;
        }
        return (double) calculateTotalPoints() / numGamesPlayed;
    }


    /**
     * Validates that the number of games played is not negative.
     *
     * @param gamesPlayed The number of games played.
     * @throws IllegalArgumentException if the value is less than zero.
     */
    private static void validateNumGamesPlayed(final int gamesPlayed)
    {
        if (gamesPlayed < 0)
        {
            throw new IllegalArgumentException("Number of games played cannot be negative: " + gamesPlayed);
        }
    }

    /**
     * Validates that the number of correct first attempts is not negative.
     *
     * @param firstAttempts The count of first-attempt correct answers.
     * @throws IllegalArgumentException if the value is less than zero.
     */
    private static void validateNumCorrectFirstAttempt(final int firstAttempts)
    {
        if (firstAttempts < 0)
        {
            throw new IllegalArgumentException("Correct first attempts count cannot be negative: " + firstAttempts);
        }
    }

    /**
     * Validates that the number of correct second attempts is not negative.
     *
     * @param secondAttempts The count of second-attempt correct answers.
     * @throws IllegalArgumentException if the value is less than zero.
     */
    private static void validateNumCorrectSecondAttempt(final int secondAttempts)
    {
        if (secondAttempts < 0)
        {
            throw new IllegalArgumentException("Correct second attempts count cannot be negative: " + secondAttempts);
        }
    }

    /**
     * Validates that the number of incorrect attempts is not negative.
     *
     * @param incorrectAttempts The count of incorrect answers.
     * @throws IllegalArgumentException if the value is less than zero.
     */
    private static void validateNumIncorrectTwoAttempts(final int incorrectAttempts)
    {
        if (incorrectAttempts < 0)
        {
            throw new IllegalArgumentException("Incorrect attempts count cannot be negative: " + incorrectAttempts);
        }
    }

    /**
     * Creates a string of the current date and time in the required format.
     *
     * @return The formatted date/time string.
     */
    private static String createFormattedDateTimeString()
    {
        final LocalDateTime currentTime;
        final DateTimeFormatter formatter;

        currentTime = LocalDateTime.now();
        formatter   = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);

        return currentTime.format(formatter);
    }


}
