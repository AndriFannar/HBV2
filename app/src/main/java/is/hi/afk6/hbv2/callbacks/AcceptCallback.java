package is.hi.afk6.hbv2.callbacks;

/**
 * Callback for when an Object should be be accepted.
 *
 * @param <T> The type of Object to accept.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 05/04/2024
 * @version 1.0
 */
public interface AcceptCallback<T>
{
    /**
     * Called when an Object should be accepted.
     *
     * @param toAccept The Object to accept.
     */
    void onAcceptClicked(T toAccept);
}
