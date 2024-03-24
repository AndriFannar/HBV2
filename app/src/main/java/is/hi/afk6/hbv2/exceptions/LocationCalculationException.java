package is.hi.afk6.hbv2.exceptions;

/**
 * Exception thrown when an error occurs during location calculation.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 24/01/2024
 * @version 1.0
 */
public class LocationCalculationException extends RuntimeException
{
    public LocationCalculationException(String message)
    {
        super(message);
    }
}
