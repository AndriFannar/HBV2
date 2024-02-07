package is.hi.afk6.hbv2.services.implementation;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import is.hi.afk6.hbv2.entities.LoginDTO;
import is.hi.afk6.hbv2.entities.User;

public class UserService
{
    private final ApiService apiService;

    public UserService()
    {
        apiService = new ApiService();
    }

    public User getUser(String id)
    {
        try
        {
            JSONObject object = apiService.getRequestAsync("user/viewUser/" + id).get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //User user = new Gson

        return null;
    }


    public User logInUser(LoginDTO login)
    {
        String loginJson = new Gson().toJson(login);

        try
        {
            JSONObject loginJsonObject = new JSONObject(loginJson);

            ApiService apiService = new ApiService();

            JSONObject userJson = apiService.postRequestAsync("user/login", loginJsonObject).get();

            if (userJson != null)
            {
                return new Gson().fromJson(userJson.toString(), User.class);
            }

        } catch (JSONException | ExecutionException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        return null;
    }
}
