package is.hi.afk6.hbv2.services.networking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import is.hi.afk6.hbv2.entities.LoginDTO;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.services.implementation.ApiService;

@RunWith(AndroidJUnit4.class)
public class APIServiceTest {
    @Test
    public void testGetRequest()
    {
        ApiService apiService = new ApiService();
        JSONObject object;

        try
        {
            object = apiService.getRequestAsync("user/viewUser/1").get();

            assertEquals(object.getString("id"), "1");
        }
        catch (ExecutionException | InterruptedException | JSONException e)
        {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void testPostRequest()
    {
        try
        {
            String email = "afk6@hi.is";
            LoginDTO logIn = new LoginDTO(email, "Lykilord123");
            JSONObject logInJson = new JSONObject(new Gson().toJson(logIn));

            ApiService apiService = new ApiService();
            JSONObject object = apiService.postRequestAsync("user/login", logInJson).get();

            assertEquals(object.getString("email"), email);
        }
        catch (JSONException | ExecutionException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }

    }
}