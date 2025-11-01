package word_game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Provides static utility methods to handle the complex I/O operations for
 * loading all country data files (a.txt through z.txt) into the World repository.
 * <p>
 * This class uses the try-with-resources idiom for safe file management.
 *
 * @author Rodrick Vyizigiro
 * @version 1.0
 * @see World
 */
final public class CountryDataLoader
{

    /**
     * A string containing characters corresponding to data files that should be skipped during the loading loop.
     */
    private static final String SKIPPED_FILE_CHARS = "wxWX";

    /**
     * The starting character used in the file naming loop ('a').
     */
    private static final char START_FILE_CHAR = 'a';

    /**
     * The ending character used in the file naming loop ('z').
     */
    private static final char END_FILE_CHAR = 'z';

    /**
     * The file extension appended to all data file names.
     */
    private static final String FILE_EXTENSION = ".txt";

    /**
     * Regular expression pattern used to split the file content into individual country data blocks.
     */
    private static final String COUNTRY_BLOCK_DELIMITER_PATTERN = "(?m)^\\s*$\\n";

    private static final String COUNTRY_BLOCK_LINE_SEPARATOR = "\\R";
    private static final String COUNTRY_NAME_CAPITAL_SEPARATOR = ":";

    private static final int FIRST_ELEMENT_INDEX = 0;
    private static final int SECOND_ELEMENT_INDEX = 1;

    private static final int COUNTRY_BLOCK_LINES_FACTS_START_INDEX = 1;
    private static final int COUNTRY_BLOCK_LINES_FACTS_END_INDEX = 4;


    private final String filesDirectory;


    /**
     * Constructs the data loader, configuring it with the necessary extraction logic and file path.
     * * @param extractor The functional interface (e.g., Country::createFromTextBlock)
     * to parse a raw text block into a Country object.
     *
     * @param filesDirectory The base directory path where the data files are located (e.g., "data/").
     * @throws IllegalArgumentException if the extractor is null or the file directory path is null/blank.
     */
    public CountryDataLoader(final String filesDirectory)
    {


        validateDirectoryPath(filesDirectory);
        this.filesDirectory = filesDirectory;
    }

    /**
     * Loads all country data from 'a.txt' through 'z.txt' and returns a HashMap of all countries.
     *
     * @return a HashMap where the key is the country name (String) and the value is the Country object.
     */
    public HashMap<String, Country> loadData()
    {

        final HashMap<String, Country> countryMap;

        countryMap = new HashMap<>();

        for (char c = START_FILE_CHAR; c <= END_FILE_CHAR; c++)
        {
            if (SKIPPED_FILE_CHARS.contains(String.valueOf(c)))
            {
                continue;
            }

            final String fileName;
            fileName = this.filesDirectory + "/" + c + FILE_EXTENSION;

            try (final Scanner fileScanner = new Scanner(new File(fileName)))
            {

                fileScanner.useDelimiter(COUNTRY_BLOCK_DELIMITER_PATTERN);

                while (fileScanner.hasNext())
                {
                    final String countryTextBlock;
                    countryTextBlock = fileScanner.next().trim();

                    if (!countryTextBlock.isEmpty())
                    {
                        final Country country;
                        final String countryName;
                        final String countryCapital;
                        final String[] countryFacts;
                        final String[] splitCountryTextBlock;
                        final String[] firstLineParts;


                        splitCountryTextBlock = countryTextBlock.split(COUNTRY_BLOCK_LINE_SEPARATOR);
                        firstLineParts        =
                            (splitCountryTextBlock[FIRST_ELEMENT_INDEX]).split(COUNTRY_NAME_CAPITAL_SEPARATOR);


                        countryName    = firstLineParts[FIRST_ELEMENT_INDEX].trim();
                        countryCapital = firstLineParts[SECOND_ELEMENT_INDEX].trim();

                        countryFacts = Arrays.copyOfRange(splitCountryTextBlock,
                                                          COUNTRY_BLOCK_LINES_FACTS_START_INDEX,
                                                          COUNTRY_BLOCK_LINES_FACTS_END_INDEX);


                        country = new Country(countryName, countryCapital, countryFacts);

                        countryMap.put(countryName, country);
                    }
                }

            }
            catch (final FileNotFoundException e)
            {
                System.err.println("CRITICAL ERROR: Required data file not found: " + fileName);
            }
            catch (final IllegalArgumentException e)
            {
                System.err.println("Data Format Error in " + fileName + ": " + e.getMessage());
            }
        }

        validateMapNotEmpty(countryMap);

        return countryMap;
    }


    /**
     * Validates that the file directory path is not null or blank.
     * * This ensures a valid starting path is provided for I/O operations.
     *
     * @param directoryPath The path string to validate.
     * @throws IllegalArgumentException if the path is null or contains only whitespace.
     */
    private static void validateDirectoryPath(final String directoryPath)
    {
        if (directoryPath == null || directoryPath.isBlank())
        {
            throw new IllegalArgumentException("Files directory path cannot be null or blank.");
        }
    }


    /**
     * Validates that the loaded country map contains at least one country.
     *
     * @param countryMap The map to validate.
     * @throws IllegalStateException if the map is empty after all files have been processed.
     */
    private static void validateMapNotEmpty(final HashMap<String, Country> countryMap)
    {
        if (countryMap.isEmpty())
        {
            throw new IllegalStateException("The World map failed to load any countries. Check data files and paths.");
        }
    }


}
