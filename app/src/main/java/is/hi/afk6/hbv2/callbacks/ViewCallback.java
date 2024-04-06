package is.hi.afk6.hbv2.callbacks;

/**
 * Callback for when an Object is requested to be viewed.
 *
 * @param <T> The type of Object to view.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 14/03/2024
 * @version 2.0
 */
public interface ViewCallback<T>
{
    /**
     * Called when an Object is requested to be viewed.
     *
     * @param toView The Object to view.
     */
    void onViewClicked(T toView);
}
