package is.hi.afk6.hbv2.services.implementation;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.json.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ApiService
{
    private final String API_URL = "https://hbv1-api.onrender.com/api/v1/";

    private final ExecutorService executorService;

    public ApiService()
    {
        executorService = Executors.newFixedThreadPool(5);
    }

    public Future<JSONObject> getRequestAsync(String urlExtension)
    {
        return executorService.submit(() ->
        {
            URL url = new URL((API_URL + urlExtension));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("X-API-KEY", "[Test]");
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
            connection.setRequestProperty("X-API-KEY", "[Test]");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(object.toString().getBytes(UTF_8));

            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Error connecting to API. Http Response Code: " + responseCode);
            } else {
                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(connection.getInputStream());

                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }

                scanner.close();

                return new JSONObject(inline.toString());
            }
        });
    }
}
