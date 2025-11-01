package word_game;

/**
 * Represents a single country object used in the WordGame trivia challenge.
 * <p>
 * This class serves as the immutable data container for all geography trivia
 * components related to a country, including its name, capital city, and three
 * associated facts. The class ensures data integrity upon creation via validation.
 *
 * @author Rodrick Vyizigiro
 * @version 1.0
 * @see World
 * @since 2025-10-31
 */
public class Country
{

    /**
     * The required number of facts for every country, as per project instructions.
     */
    private static final int NUM_FACTS = 3;

    /**
     * A default string used for any fact that might be unexpectedly missing.
     */
    private static final String DEFAULT_FACT = "Fact not available.";

    private final String countryName;
    private final String capitalCityName;
    private final String[] facts;


    /**
     * Constructs a Country object with its name, capital city, and an array of facts.
     *
     * @param countryName     The name of the country (e.g., "Canada").
     * @param capitalCityName The name of the capital city (e.g., "Ottawa").
     * @param facts           An array of Strings containing exactly three unique facts about the country.
     * @throws IllegalArgumentException if any of the parameters are null, blank, or the facts array is not exactly
     *                                  NUM_FACTS long.
     */
    public Country(final String countryName,
                   final String capitalCityName,
                   final String[] facts)
    {

        validateCountryName(countryName);
        validateCountryCapital(capitalCityName);
        validateFacts(facts);

        this.countryName     = countryName;
        this.capitalCityName = capitalCityName;
        this.facts           = facts;
    }


    /**
     * Returns the official name of the country.
     *
     * @return the country's name.
     */
    public String getCountryName()
    {
        return countryName;
    }

    /**
     * Returns the name of the country's capital city.
     *
     * @return the capital city's name.
     */
    public String getCapitalCityName()
    {
        return capitalCityName;
    }

    /**
     * Returns the array of facts about the country.
     *
     * @return the array of facts.
     */
    public String[] getFacts()
    {
        return facts;
    }


    /**
     * Validates that the country name strings are not null or blank.
     *
     * @param name The country's name.
     * @throws IllegalArgumentException if either string is null or blank.
     */
    private static void validateCountryName(final String name)
    {
        if (name == null || name.isBlank())
        {
            throw new IllegalArgumentException("Country name cannot be null or blank.");
        }
    }


    /**
     * Validates that the country and capital city name strings are not null or blank.
     *
     * @param capital The capital city's name.
     * @throws IllegalArgumentException if either string is null or blank.
     */
    private static void validateCountryCapital(final String capital)
    {
        if (capital == null || capital.isBlank())
        {
            throw new IllegalArgumentException("Capital city name cannot be null or blank.");
        }
    }


    /**
     * Validates that the facts array is not null, has the required length,
     * and contains no null or blank fact strings.
     *
     * @param facts The array of facts.
     * @throws IllegalArgumentException if the array is null, the wrong size, or contains blank entries.
     */
    private static void validateFacts(final String[] facts)
    {
        if (facts == null)
        {
            throw new IllegalArgumentException("Facts array cannot be null.");
        }
        if (facts.length != NUM_FACTS)
        {
            throw new IllegalArgumentException("Facts array must contain exactly" + NUM_FACTS + " facts.");
        }

        for (final String fact : facts)
        {

            if (fact == null || fact.isBlank())
            {

                throw new IllegalArgumentException("Fact entry cannot be null or blank.");
            }
        }
    }
}