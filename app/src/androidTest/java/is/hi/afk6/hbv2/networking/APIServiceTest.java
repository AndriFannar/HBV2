package is.hi.afk6.hbv2.networking;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import is.hi.afk6.hbv2.entities.dtos.LoginDTO;
import is.hi.afk6.hbv2.entities.enums.Request;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;

@RunWith(AndroidJUnit4.class)
public class APIServiceTest {
    @Test
    public void testGetRequest()
    {
        APIServiceImplementation apiService = new APIServiceImplementation();
        JSONObject object;

        try
        {
            object = apiService.makeNetworkRequest(
                    "user/view/8",
                    Request.GET,
            null,
                    ""
            );

            JSONObject user = object.getJSONObject("data");

            assertEquals(user.getString("id"), "8");
        }
        catch (JSONException e)
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
            String logInJson = new Gson().toJson(logIn);

            APIServiceImplementation apiService = new APIServiceImplementation();
            JSONObject object = apiService.makeNetworkRequest(
                    "user/login",
                    Request.POST,
                    null,
                    logInJson
            );

            JSONObject user = object.getJSONObject("data");

            assertEquals(user.getString("email"), email);
        }
        catch (JSONException e)
        {
            throw new RuntimeException(e);
        }

    }
}