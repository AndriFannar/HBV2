package is.hi.afk6.hbv2.networking;

import org.json.JSONObject;

/**
 * Connect to an API and make Get, Post, Put & Delete requests.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/02/2024
 * @version 2.0
 */
public interface APIService
{
    /**
     * Performs a get request with the specified URL.
     *
     * @param urlExtension Extension on top of the base URL specified in the class, if defined.
     * @return             JSONObject returned from API.
     */
    JSONObject getRequest(String urlExtension, String requestParam);

    /**
     * Performs a post request with the specified URL.
     *
     * @param urlExtension Extension on top of the base URL specified in the class, if defined.
     * @param object       JSONObject to post to API.
     * @return             JSONObject returned from API.
     */
    JSONObject postRequest(String urlExtension, String object);

    /**
     * Performs a put request with the specified URL.
     *
     * @param urlExtension Extension on top of the base URL specified in the class, if defined.
     * @param object       JSONObject to put to API.
     * @return             JSONObject returned from API.
     */
    JSONObject putRequest(String urlExtension, String object);

    /**
     * Performs a delete request with the specified URL.
     *
     * @param urlExtension Extension on top of the base URL specified in the class, if defined.
     * @return             JSONObject returned from API.
     */
    JSONObject deleteRequest(String urlExtension);
}