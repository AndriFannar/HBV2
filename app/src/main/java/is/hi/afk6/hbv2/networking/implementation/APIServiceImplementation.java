package is.hi.afk6.hbv2.networking.implementation;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.util.Log;

import com.google.gson.JsonIOException;

import org.jetbrains.annotations.NotNull;
import org.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import is.hi.afk6.hbv2.entities.enums.Request;
import is.hi.afk6.hbv2.exceptions.APIConnectionException;
import is.hi.afk6.hbv2.exceptions.APIOutputStreamException;
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
    private final String API_URL = "https://hbv1-api-development.onrender.com/api/v1/";
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

    @Override
    public JSONObject makeNetworkRequest(String urlExtension, Request requestType, String[] requestParam, String requestBody)
    {
        if (!(requestParam == null) && !(requestParam.length == 0))
        {
            urlExtension += "?";
            StringBuilder urlExtensionBuilder = new StringBuilder(urlExtension);

            for (int i = 0; i < requestParam.length; i++)
            {
                urlExtensionBuilder.append(requestParam[i]);

                if ((i + 1) < requestParam.length)
                    urlExtensionBuilder.append("&");
            }

            urlExtension = urlExtensionBuilder.toString();
        }

        try
        {
            // Create and set up URL connection.
            URL url = new URL(API_URL + urlExtension);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-API-KEY", API_PASS);
            connection.setConnectTimeout(API_TIMEOUT);
            connection.setReadTimeout(API_TIMEOUT);
            connection.setRequestMethod(requestType.toString());

            // Create return object.
            JSONObject returnJSON;

            if (!requestBody.isEmpty())
            {
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                try (OutputStream outputStream = connection.getOutputStream())
                {
                    outputStream.write(requestBody.getBytes(UTF_8));
                }
                catch (IOException exception)
                {
                    throw new APIOutputStreamException(exception.getMessage());
                }
            }

            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_CREATED && responseCode != HttpURLConnection.HTTP_BAD_REQUEST)
            {
                throw new APIConnectionException("Error connecting to API. Http Response Code: " + responseCode);
            }
            else
            {
                if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST)
                    returnJSON = parseNetworkResponse(connection.getErrorStream());
                else
                    returnJSON = parseNetworkResponse(connection.getInputStream());
            }

            connection.disconnect();

            return returnJSON;
        }
        catch (IOException | JSONException exception)
        {
            throw new RuntimeException(exception);
        }
    }
}
