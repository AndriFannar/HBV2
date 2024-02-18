package is.hi.afk6.hbv2.networking.implementation;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.json.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import is.hi.afk6.hbv2.networking.APIService;

/**
 * Connect to an API and make Get, Post, Put & Delete requests.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/02/2024
 * @version 1.0
 */
public class APIServiceImplementation implements APIService
{
    // Base API URL.
    private final String API_URL = "https://hbv1-api.onrender.com/api/v1/";
    private final String API_PASS = "Kclj6G!2$CRpnOog";

    // Base level Render host goes offline when not in use and takes quite some time to come back online.
    private final int API_TIMEOUT = 240000;

    private final ExecutorService executorService;

    /**
     * Create a new API Service.
     */
    public APIServiceImplementation()
    {
        executorService = Executors.newFixedThreadPool(5);
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

    private JSONObject makeNetworkRequest(String urlExtension, String requestMethod, JSONObject object) throws Exception
    {
        URL url = new URL(API_URL + urlExtension);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-API-KEY", API_PASS);
        connection.setConnectTimeout(API_TIMEOUT);
        connection.setReadTimeout(API_TIMEOUT);
        connection.setRequestMethod(requestMethod);

        if (object != null)
        {
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream outputStream = connection.getOutputStream())
            {
                outputStream.write(object.toString().getBytes(UTF_8));
            }
        }

        int responseCode = connection.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_BAD_REQUEST && responseCode != HttpURLConnection.HTTP_CREATED)
        {
            throw new RuntimeException("Error connecting to API. Http Response Code: " + responseCode);
        }
        else
        {
            InputStream inputStream = connection.getInputStream();
            return parseNetworkResponse(inputStream);
        }
    }

    @Override
    public CompletableFuture<JSONObject> getRequestAsync(String urlExtension)
    {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        executorService.submit(() -> {
            try
            {
                future.complete(makeNetworkRequest(urlExtension, "GET", null));
            }
            catch (Exception e)
            {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<JSONObject> postRequestAsync(String urlExtension, JSONObject object)
    {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        executorService.submit(() -> {
            try
            {
                future.complete(makeNetworkRequest(urlExtension, "POST", object));
            }
            catch (Exception e)
            {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<JSONObject> putRequestAsync(String urlExtension, JSONObject object)
    {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        executorService.submit(() -> {
            try
            {
                future.complete(makeNetworkRequest(urlExtension, "PUT", object));
            }
            catch (Exception e)
            {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<JSONObject> deleteRequestAsync(String urlExtension)
    {
        CompletableFuture<JSONObject> future = new CompletableFuture<>();

        executorService.submit(() -> {
            try
            {
                future.complete(makeNetworkRequest(urlExtension, "DELETE", null));
            }
            catch (Exception e)
            {
                future.completeExceptionally(e);
            }
        });

        return future;
    }
}
