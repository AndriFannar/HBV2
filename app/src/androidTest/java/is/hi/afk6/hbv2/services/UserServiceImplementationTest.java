package is.hi.afk6.hbv2.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import is.hi.afk6.hbv2.entities.LoginDTO;
import is.hi.afk6.hbv2.entities.ResponseWrapper;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

@RunWith(AndroidJUnit4.class)
public class UserServiceImplementationTest
{
    private UserService userService;

    @Before
    public void createUserServiceClass()
    {
        userService = new UserServiceImplementation(new APIServiceImplementation());
    }

    /*@Test
    public void testGetRequest()
    {
        APIServiceImplementation apiService = new APIServiceImplementation();
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
    }*/


    @Test
    public void testPostRequest()
    {

        String email = "sus@hi.is";
        LoginDTO logIn = new LoginDTO(email, "Lykilord123");
        ResponseWrapper<User> response = userService.logInUser(logIn);

        User user = response.getData();

        System.out.println("Error: " + response.getErrorResponse());
        System.out.println("Data: " + response.getData());
        System.out.println("User: " + user);
        //assertEquals(object.getString("email"), email);
    }
}