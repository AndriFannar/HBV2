package is.hi.afk6.hbv2.callbacks;

/**
 * Callback for when an Object should be be displayed.
 *
 * @param <T> The type of Object to display.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 05/04/2024
 * @version 1.0
 */
public interface DisplayCallback<T>
{
    /**
     * Called when an Object should be displayed.
     *
     * @param toDisplay The Object to display.
     */
    void onDisplayClicked(T toDisplay);
}
