package word_game;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages all file I/O operations and persistence logic related to game scores.
 * <p>
 * This class is responsible for loading the history of all recorded scores from
 * the "score.txt" file, saving (appending) new scores to the file, and determining
 * the all-time highest score for comparison. It abstracts the file system details
 * away from the main WordGame controller.
 *
 * @author Rodrick Vyizigiro
 * @version 1.0
 * @see Score
 * @see WordGame
 */
public final class ScoreManager
{
    private static final int SCORE_LINES_PER_ENTRY = 6;
    private static final int SCORE_GAMES_PLAYED_LINE = 1;
    private static final int SCORE_FIRST_ATTEMPTS_LINE = 2;
    private static final int SCORE_SECOND_ATTEMPTS_LINE = 3;
    private static final int SCORE_INCORRECT_ATTEMPTS_LINE = 4;
    private static final String SCORE_ENTRY_DELIMITER_PATTERN = "(?m)^\\s*$\\n"; // Pattern for splitting entries


    /**
     * Symbolic constant for the initial count of games played.
     */
    private static final int INITIAL_GAMES_PLAYED = 0;

    /**
     * Symbolic constant for the initial count of correct first attempts.
     */
    private static final int INITIAL_CORRECT_FIRST = 0;

    /**
     * Symbolic constant for the initial count of correct second attempts.
     */
    private static final int INITIAL_CORRECT_SECOND = 0;

    /**
     * Symbolic constant for the initial count of incorrect attempts.
     */
    private static final int INITIAL_INCORRECT_TWO = 0;


    private final String scoresFileNamee;


    private final List<Score> allScores;


    /**
     * Constructs the ScoreManager and immediately loads all existing scores
     * from the score file into memory.
     */
    public ScoreManager(final String scoresFileNamee)
    {
        this.scoresFileNamee = scoresFileNamee;
        this.allScores       = loadAllScoresFromFile();
    }


    /**
     * Appends a new Score object to the "score.txt" file and updates the
     * internal list of all recorded scores.
     *
     * @param newScore The Score object to be saved.
     * @throws IllegalArgumentException if the newScore is null.
     */
    public void saveNewScoreToFile(final Score newScore)
    {
        final String scoreEntry;
        final Path filePath;

        scoreEntry = formatScoreForFile(newScore);
        filePath   = Path.of(this.scoresFileNamee);

        try (final BufferedWriter writer = Files.newBufferedWriter(
            filePath,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND))
        {

            writer.write(scoreEntry);
            writer.newLine();

            this.allScores.add(newScore);

        }
        catch (final IOException e)
        {
            System.err.println("CRITICAL I/O ERROR: Failed to save score to file: " + e.getMessage());
        }
    }


    /**
     * Compares the average score of the provided session score against the
     * best average score recorded in the score history.
     *
     * @param sessionScore The Score object from the player's most recent session.
     * @return true if the sessionScore average is strictly greater than the previous high score.
     * @throws IllegalArgumentException if the sessionScore is null.
     */
    public boolean isNewHighScore(final Score sessionScore)
    {

        final double currentSessionAverage;
        final double previousHighestAverage;

        currentSessionAverage  = sessionScore.calculateAverageScorePerGame();
        previousHighestAverage = findHighestAverageScore();
        return currentSessionAverage > previousHighestAverage;
    }


    /**
     * Reads and parses all existing score entries from the "score.txt" file.
     * * This method uses file I/O and must handle exceptions (FileNotFound, parsing errors).
     *
     * @return A List of all Score objects found in the file, or an empty list if the file is new or empty.
     */
    private List<Score> loadAllScoresFromFile()
    {
        final List<Score> scores;
        final Path filePath;

        scores   = new ArrayList<>();
        filePath = Path.of(scoresFileNamee);

        try (final Scanner fileScanner = new Scanner(filePath.toFile()))
        {

            fileScanner.useDelimiter(SCORE_ENTRY_DELIMITER_PATTERN);

            while (fileScanner.hasNext())
            {

                final String scoreBlock;
                final String[] lines;
                final int gamesPlayed;
                final int correctFirst;
                final int correctSecond;
                final int incorrectTwo;
                final Score score;

                scoreBlock = fileScanner.next().trim();

                if (!scoreBlock.isEmpty())
                {
                    lines = scoreBlock.split("\n");

                    if (lines.length >= SCORE_LINES_PER_ENTRY)
                    {

                        gamesPlayed   = parseLineValue(lines[SCORE_GAMES_PLAYED_LINE]);
                        correctFirst  = parseLineValue(lines[SCORE_FIRST_ATTEMPTS_LINE]);
                        correctSecond = parseLineValue(lines[SCORE_SECOND_ATTEMPTS_LINE]);
                        incorrectTwo  = parseLineValue(lines[SCORE_INCORRECT_ATTEMPTS_LINE]);

                        score = new Score(gamesPlayed, correctFirst, correctSecond, incorrectTwo);
                        scores.add(score);
                    }
                }
            }

        }
        catch (final IOException e)
        {
            System.err.println("I/O ERROR: Could not read score history file: " + e.getMessage());
        }
        catch (final IllegalArgumentException e)
        {
            System.err.println("DATA PARSE ERROR: Score file corrupted or malformed: " + e.getMessage());
        }
        return scores;
    }


    /**
     * Formats a Score object into the multi-line string required for score.txt.
     * This format matches the input format required by loadAllScoresFromFile().
     *
     * @param score The score object to format.
     * @return A multi-line string formatted for the score.txt file.
     */
    private String formatScoreForFile(final Score score)
    {
        final StringBuilder builder;

        builder = new StringBuilder();

        builder.append("Date and Time: ").append(score.getFormattedDateTime()).append("\n");

        builder.append("Games Played: ").append(score.getNumGamesPlayed()).append("\n");

        builder.append("Correct First Attempts: ").append(score.getNumCorrectFirstAttempt()).append("\n");

        builder.append("Correct Second Attempts: ").append(score.getNumCorrectSecondAttempt()).append("\n");

        builder.append("Incorrect Attempts: ").append(score.getNumIncorrectTwoAttempts()).append("\n");

        builder.append("Total Score: ").append(score.calculateTotalPoints()).append(" points").append("\n");

        return builder.toString();
    }

    /**
     * Parses the integer value from a structured score line (e.g., "Games Played: 1" returns 1).
     *
     * @param line The string line to parse.
     * @return The integer value found at the end of the line.
     * @throws NumberFormatException if the value cannot be parsed as an integer.
     */
    private int parseLineValue(final String line)
    {
        final String[] parts;
        final String valueString;


        parts = line.split(":\\s*");

        valueString = parts[1].split("\\s")[0];

        return Integer.parseInt(valueString.trim());
    }

    /**
     * Iterates through all historical scores and finds the highest average score per game.
     *
     * @return The highest average score per game found in the history, or 0.0 if the history is empty.
     */
    private double findHighestAverageScore()
    {
        return findHighestScore().calculateAverageScorePerGame();
    }


    /**
     * Creates and returns a new Score object initialized with zero values,
     * representing the start of a new WordGame session.
     *
     * @return A new Score instance with all game statistics set to zero.
     */
    public static Score startNewSessionScore()
    {
        return new Score(INITIAL_GAMES_PLAYED,
                         INITIAL_CORRECT_FIRST,
                         INITIAL_CORRECT_SECOND,
                         INITIAL_INCORRECT_TWO);
    }


    // Inside ScoreManager.java

    /**
     * Returns the average score of the current all-time highest recorded score.
     *
     * @return The highest average score per game found in the history, or 0.0 if the history is empty.
     */
    public double getHighestAverageScore()
    {
        final Score highestScore;

        highestScore = findHighestScore();

        if (highestScore == null)
        {
            return 0.0;
        }
        return highestScore.calculateAverageScorePerGame();
    }

    /**
     * Returns the formatted date and time of the current all-time highest recorded score.
     *
     * @return The date/time string of the highest score, or an empty string if the history is empty.
     */
    public String getHighestScoreDateTime()
    {
        final Score highestScore;

        highestScore = findHighestScore();

        if (highestScore == null)
        {
            return "";
        }
        return highestScore.getFormattedDateTime();
    }


    /**
     * Finds and returns the single Score object that represents the all-time high score
     * (highest average score per game) from the history.
     *
     * @return The highest-scoring Score object, or null if the history list is empty.
     */
    private Score findHighestScore()
    {
        if (this.allScores.isEmpty())
        {
            return startNewSessionScore();
        }
        Score highestScore;
        double highestAverage;
        highestAverage = 0.0;
        highestScore   = this.allScores.getFirst();

        for (final Score score : this.allScores)
        {
            final double currentAverage;
            currentAverage = score.calculateAverageScorePerGame();

            if (currentAverage > highestAverage)
            {
                highestAverage = currentAverage;
                highestScore   = score;
            }
        }

        return highestScore;
    }

}