package is.hi.afk6.hbv2.entities.api;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Error Response class that contains errors from the API.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is.
 * @since   2024-02-13
 * @version 1.0
 */
public class ErrorResponse
{
    private String error;
    private Map<String, String> errorDetails;

    /**
     * Create a new Error Response.
     *
     * @param error        Error heading.
     * @param errorDetails Details of the error.
     */
    public ErrorResponse(String error, Map<String, String> errorDetails) {
        this.error = error;
        this.errorDetails = errorDetails;
    }

    /**
     * Create an empty Error Response.
     */
    public ErrorResponse()
    {
        this.errorDetails = new HashMap<>();
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, String> getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(Map<String, String> errorDetails) {
        this.errorDetails = errorDetails;
    }

    public void addErrorDetail(String error, String detail)
    {
        this.errorDetails.put(error, detail);
    }

    @NonNull
    @Override
    public String toString() {
        return "ErrorResponse{" +
                "error='" + error + '\'' +
                ", errorDetails=" + errorDetails +
                '}';
    }
}