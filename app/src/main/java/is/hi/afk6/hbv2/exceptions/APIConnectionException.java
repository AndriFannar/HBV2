package is.hi.afk6.hbv2.exceptions;

/**
 * Exception thrown when an error occurs during API connection operations.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 20/01/2024
 * @version 1.0
 */
public class APIConnectionException extends RuntimeException
{
    public APIConnectionException(String message)
    {
        super(message);
    }
}
