package is.hi.afk6.hbv2.exceptions;

/**
 * Exception thrown when an error occurs during API output stream operations.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 20/01/2024
 * @version 1.0
 */
public class APIOutputStreamException extends RuntimeException
{
    public APIOutputStreamException(String message)
    {
        super(message);
    }
}
