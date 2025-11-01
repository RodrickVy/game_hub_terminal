package word_game;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the complete collection of geography trivia data for the WordGame.
 * <p>
 * This class serves as the central data repository, holding all country, capital city,
 * and fact data loaded from the external text files (a.txt through z.txt). It manages
 * the collection of {@link Country} objects using a HashMap, where the country name
 * acts as the unique key. The World class provides methods for adding countries and
 * retrieving an encapsulated view of the data for use by the game logic.
 *
 * @author Rodrick Vyizigiro
 * @version 1.0
 * @see Country
 * @see WordGame
 * @since 2025-10-31
 */
public class World
{
    private final HashMap<String, Country> countries;

    public World(
        final HashMap<String, Country> countries
                )
    {
        validateCountries(countries);
        this.countries = countries;
    }


    /**
     * Validates that the collection of countries is not null and contains
     * at least one entry.
     * * This method ensures the World object can function as a trivia source.
     *
     * @param countries the HashMap of countries to validate.
     * @throws IllegalArgumentException if the map reference is null or is empty.
     */
    private static void validateCountries(final HashMap<String, Country> countries)
    {
        if (countries == null)
        {
            throw new IllegalArgumentException("The country map cannot be null.");
        }
        if (countries.isEmpty())
        {
            throw new IllegalArgumentException("The country map cannot be empty; at " +
                                                   "least one country must be loaded.");
        }
    }

    public Map<String, Country> getCountries()
    {
        return countries;
    }
}
