package is.hi.afk6.hbv2.entities.api;

/**
 * Interface for API Callbacks.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 19/02/2024
 * @version 1.0
 */
public interface APICallback<T>
{
    /**
     * Code to execute when result has been returned.
     *
     * @param result The result of the called method.
     */
    void onComplete(ResponseWrapper<T> result);
}
