package is.hi.afk6.hbv2.entities.api;

/**
 * Response Wrapper that contains data returned from API, or an ErrorResponse with a detailed error message.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is.
 * @since   2024-02-13
 * @version 1.0
 *
 * @param <T> Object returned from API if request was successful.
 */
public class ResponseWrapper<T>
{
    private T data;
    private ErrorResponse errorResponse;

    /**
     * Create a response with data.
     *
     * @param data Data to return.
     */
    public ResponseWrapper(T data)
    {
        this.data = data;
    }

    /**
     * Create a response with an error.
     *
     * @param errorResponse Error to return.
     */
    public ResponseWrapper(ErrorResponse errorResponse)
    {
        this.errorResponse = errorResponse;
    }

    public T getData() {
        return data;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
