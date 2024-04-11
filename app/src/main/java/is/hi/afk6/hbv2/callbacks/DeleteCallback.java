package is.hi.afk6.hbv2.callbacks;

/**
 * Callback for when an Object should be deleted.
 *
 * @param <T> The type of Object to delete.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 14/03/2024
 * @version 2.0
 */
public interface DeleteCallback<T>
{
    /**
     * Called when an Object should be deleted.
     *
     * @param toDelete The Object to delete.
     */
    void onDeleteClicked(T toDelete);
}
