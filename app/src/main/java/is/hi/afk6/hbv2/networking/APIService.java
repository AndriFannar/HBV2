package is.hi.afk6.hbv2.networking;

import org.json.JSONObject;

import java.util.concurrent.Future;

/**
 * Connect to an API and make Get, Post, Put & Delete requests.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/02/2024
 * @version 1.0
 */
public interface APIService
{
    /**
     * Performs an asynchronous get request with the specified URL.
     *
     * @param urlExtension Extension on top of the base URL specified in the class, if defined.
     * @return             JSONObject returned from API.
     */
    public Future<JSONObject> getRequestAsync(String urlExtension);

    /**
     * Performs an asynchronous post request with the specified URL.
     *
     * @param urlExtension Extension on top of the base URL specified in the class, if defined.
     * @param object       JSONObject to post to API.
     * @return             JSONObject returned from API.
     */
    public Future<JSONObject> postRequestAsync(String urlExtension, JSONObject object);

    /**
     * Performs an asynchronous put request with the specified URL.
     *
     * @param urlExtension Extension on top of the base URL specified in the class, if defined.
     * @param object       JSONObject to put to API.
     * @return             JSONObject returned from API.
     */
    public Future<JSONObject> putRequestAsync(String urlExtension, JSONObject object);

    /**
     * Performs an asynchronous delete request with the specified URL.
     *
     * @param urlExtension Extension on top of the base URL specified in the class, if defined.
     * @return             JSONObject returned from API.
     */
    public Future<JSONObject> deleteRequestAsync(String urlExtension);
}
