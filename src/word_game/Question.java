package word_game;

/**
 * Represents an immutable data container for a single trivia question and its correct answer.
 * <p>
 * This class holds the formatted prompt that is displayed to the user, the exact
 * correct answer string required for validation, and an integer identifier for the
 * question type (0, 1, or 2). Its private final fields ensure the data cannot
 * be altered after instantiation.
 *
 * @author Rodrick Vyizigiro
 * @version 1.0
 * @since 2025-10-31
 */
public final class Question
{
    private static final int MIN_QUESTION_TYPE = 0;
    private static final int MAX_QUESTION_TYPE = 2;
    private final String prompt;
    private final String answer;
    private final int type;

    /**
     * Constructs an immutable Question object.
     *
     * @param prompt The question text to display to the user.
     * @param answer The correct answer string.
     * @param type   The type of question (0, 1, or 2).
     */
    public Question(final String prompt,
                    final String answer,
                    final int type)
    {
        validatePrompt(prompt);
        validateAnswer(answer);
        validateType(type);

        this.prompt = prompt;
        this.answer = answer;
        this.type   = type;
    }

    /**
     * Compares user input with a answer string, ignoring case.
     *
     * @param input The raw string entered by the user.
     * @return True if the input matches the answer, false otherwise.
     */
    public boolean checkAnswer(final String input)
    {
        return input.trim().equalsIgnoreCase(this.answer);
    }

    /**
     * Returns the formatted question text to display to the user.
     *
     * @return the question prompt string.
     */
    public String getPrompt()
    {
        return prompt;
    }

    /**
     * Returns the correct answer string.
     *
     * @return the correct answer.
     */
    public String getAnswer()
    {
        return answer;
    }

    /**
     * Returns the type identifier of the question (0, 1, or 2).
     *
     * @return the question type index.
     */
    public int getType()
    {
        return type;
    }

    /**
     * Validates that the question prompt string is not null or blank.
     *
     * @param prompt The question text to validate.
     * @throws IllegalArgumentException if the prompt is null or contains only whitespace.
     */
    private static void validatePrompt(final String prompt)
    {
        if (prompt == null || prompt.isBlank())
        {
            throw new IllegalArgumentException("Question prompt cannot be null or blank.");
        }
    }

    /**
     * Validates that the answer string is not null or blank and contains only a single word (no spaces).
     *
     * @param answer The expected answer string to validate.
     * @throws IllegalArgumentException if the answer is null/blank or contains one or more spaces.
     */
    private static void validateAnswer(final String answer)
    {
        if (answer == null || answer.isBlank())
        {
            throw new IllegalArgumentException("Answer cannot be null or blank.");
        }
    
    }

    /**
     * Validates that the question type integer is within the expected range [0, 2].
     *
     * @param questionType The type index to validate.
     * @throws IllegalArgumentException if the type index is outside the range 0 to 2 (inclusive).
     */
    private static void validateType(final int questionType)
    {
        if (questionType < MIN_QUESTION_TYPE || questionType > MAX_QUESTION_TYPE)
        {
            final String message;
            message = String.format("Question type must be between %d and %d (inclusive). Value was: %d",
                                    MIN_QUESTION_TYPE, MAX_QUESTION_TYPE, questionType);
            throw new IllegalArgumentException(message);
        }
    }
}