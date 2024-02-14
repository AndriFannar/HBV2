package is.hi.afk6.hbv2.networking.implementation;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.json.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
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
    private final String API_PASS = "[Test]";

    private final ExecutorService executorService;

    /**
     * Create a new API Service.
     */
    public APIServiceImplementation()
    {
        executorService = Executors.newFixedThreadPool(5);
    }

    public Future<JSONObject> getRequestAsync(String urlExtension)
    {
        return executorService.submit(() ->
        {
            URL url = new URL((API_URL + urlExtension));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-API-KEY", API_PASS);
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK)
            {
                throw new RuntimeException("Error connecting to API. Http Response Code: " + responseCode);
            }
            else
            {
                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());

                while (scanner.hasNext())
                {
                    inline.append(scanner.nextLine());
                }

                scanner.close();

                return new JSONObject(inline.toString());
            }
        });
    }


    public Future<JSONObject> postRequestAsync(String urlExtension, JSONObject object)
    {
        return executorService.submit(() ->
        {
            URL url = new URL((API_URL + urlExtension));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-API-KEY", API_PASS);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(object.toString().getBytes(UTF_8));

            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_BAD_REQUEST) {
                throw new RuntimeException("Error connecting to API. Http Response Code: " + responseCode);
            } else {
                InputStream inputStream = null;

                if (responseCode == HttpURLConnection.HTTP_OK)
                    inputStream = connection.getInputStream();
                else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST)
                    inputStream = connection.getErrorStream();

                if (inputStream != null)
                {
                    StringBuilder inline = new StringBuilder();
                    Scanner scanner = new Scanner(inputStream);

                    while (scanner.hasNext()) {
                        inline.append(scanner.nextLine());
                    }

                    scanner.close();

                    return new JSONObject(inline.toString());
                }
                else
                {
                    throw new RuntimeException("Error reading response from API.");
                }
            }
        });
    }

    @Override
    public Future<JSONObject> putRequestAsync(String urlExtension, JSONObject object)
    {
        return executorService.submit(() ->
        {
            URL url = new URL((API_URL + urlExtension));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-API-KEY", API_PASS);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(object.toString().getBytes(UTF_8));

            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK && responseCode != HttpURLConnection.HTTP_BAD_REQUEST) {
                throw new RuntimeException("Error connecting to API. Http Response Code: " + responseCode);
            } else {
                InputStream inputStream = null;

                if (responseCode == HttpURLConnection.HTTP_OK)
                    inputStream = connection.getInputStream();
                else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST)
                    inputStream = connection.getErrorStream();

                if (inputStream != null)
                {
                    StringBuilder inline = new StringBuilder();
                    Scanner scanner = new Scanner(inputStream);

                    while (scanner.hasNext()) {
                        inline.append(scanner.nextLine());
                    }

                    scanner.close();

                    return new JSONObject(inline.toString());
                }
                else
                {
                    throw new RuntimeException("Error reading response from API.");
                }
            }
        });
    }

    @Override
    public Future<JSONObject> deleteRequestAsync(String urlExtension)
    {
        return executorService.submit(() ->
        {
            URL url = new URL((API_URL + urlExtension));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-API-KEY", API_PASS);
            connection.setRequestMethod("DELETE");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK)
            {
                throw new RuntimeException("Error connecting to API. Http Response Code: " + responseCode);
            }
            else
            {
                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());

                while (scanner.hasNext())
                {
                    inline.append(scanner.nextLine());
                }

                scanner.close();

                return new JSONObject(inline.toString());
            }
        });
    }
}
