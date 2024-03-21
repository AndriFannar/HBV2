package is.hi.afk6.hbv2.networking;

import org.json.JSONObject;

import is.hi.afk6.hbv2.entities.enums.Request;

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
     * Send a request to the API.
     *
     * @param urlExtension Extension on top of the base URL specified in the class, if defined.
     * @param requestParam String representation of object to send with the request as a request parameter.
     * @param requestBody  String representation of object to send with the request as a request body.
     * @return             JSONObject returned from API.
     */
    JSONObject makeNetworkRequest(String urlExtension, Request requestType, String[] requestParam, String requestBody);
}