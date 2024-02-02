package is.hi.afk6.hbv2.services.networking;

import org.json.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiService
{
    private final String API_URL = "https://hbv1-api.onrender.com/api/v1/";

    public JSONObject getRequest(String url_extension) throws IOException, JSONException
    {
        URL url = new URL((API_URL + url_extension));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("X-API-KEY", "Test");
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();

        if (responseCode != 200)
        {
            throw new RuntimeException("Error connecting to api. Http Response Code: " + responseCode);
        }
        else
        {
            StringBuilder inline = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext())
            {
                inline.append(scanner.nextLine());
            }

            scanner.close();

            return new JSONObject(inline.toString());
        }
    }
}
