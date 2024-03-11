package is.hi.afk6.hbv2.networking.implementation;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.util.Log;

import org.json.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;

import is.hi.afk6.hbv2.networking.APIService;

/**
 * Connect to an API and make Get, Post, Put & Delete requests.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/02/2024
 * @version 2.0
 */
public class APIServiceImplementation implements APIService
{
    // Base API URL.
    private final String API_URL = "https://hbv1-api.onrender.com/api/v1/";
    /** @noinspection SpellCheckingInspection*/
    private final String API_PASS = "Kclj6G!2$CRpnOog";

    // Long timeout since base level Render host goes offline when not in use
    // and takes quite some time to come back online.
    private final int API_TIMEOUT = 240000;

    /**
     * Create a new API Service.
     */
    public APIServiceImplementation()
    {
    }

    private JSONObject parseNetworkResponse(InputStream inputStream) throws JSONException
    {
        try (Scanner scanner = new Scanner(inputStream, UTF_8.name()))
        {
            StringBuilder inline = new StringBuilder();

            while (scanner.hasNext())
            {
                inline.append(scanner.nextLine());
            }

            return inline.length() > 0 ? new JSONObject(inline.toString()): new JSONObject();
        }
    }

    private JSONObject makeNetworkRequest(String urlExtension, String requestMethod, String object) throws Exception
    {
        if (Objects.equals(requestMethod, "GET") && !object.isEmpty())
        {
            urlExtension += "?" + object;
        }

        Log.d("API", "Sending object to API: " + object);

        URL url = new URL(API_URL + urlExtension);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-API-KEY", API_PASS);
        connection.setConnectTimeout(API_TIMEOUT);
        connection.setReadTimeout(API_TIMEOUT);
        connection.setRequestMethod(requestMethod);

        if (!object.isEmpty() && !Objects.equals(requestMethod, "GET"))
        {
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream())
            {
                outputStream.write(object.getBytes(UTF_8));
            }
        }

        int responseCode = connection.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED && responseCode != HttpURLConnection.HTTP_BAD_REQUEST)
        {
            throw new RuntimeException("Error connecting to API. Http Response Code: " + responseCode);
        }
        else
        {
            if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST)
                return parseNetworkResponse(connection.getErrorStream());

            return parseNetworkResponse(connection.getInputStream());
        }
    }

    @Override
    public JSONObject getRequest(String urlExtension, String requestParam)
    {
        try {
            return makeNetworkRequest(urlExtension, "GET", requestParam);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject postRequest(String urlExtension, String object)
    {
        try {
            return makeNetworkRequest(urlExtension, "POST", object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject putRequest(String urlExtension, String object)
    {
        try {
            return makeNetworkRequest(urlExtension, "PUT", object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JSONObject deleteRequest(String urlExtension)
    {
        try {
            return makeNetworkRequest(urlExtension, "DELETE", "");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
