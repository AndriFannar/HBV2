package is.hi.afk6.hbv2.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import is.hi.afk6.hbv2.entities.ErrorResponse;
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


    @Test
    public void testLogInExists()
    {
        String email = "afk6@hi.is";
        LoginDTO logIn = new LoginDTO(email, "Lykilord123");
        ResponseWrapper<User> response = userService.logInUser(logIn);

        User user = response.getData();

        assertEquals(user.getEmail(), email);
    }

    @Test
    public void testUpdateUserSuccessful()
    {
        User user = userService.getUserByID(8L);

        String newName = "Andri";
        user.setName(newName);

        userService.updateUser(user.getId(), user);

        User updatedUser = userService.getUserByID(8L);

        assertEquals(user.getName(), updatedUser.getName());
    }

    @Test
    public void testUpdateUserUnsuccessful()
    {
        User user = userService.getUserByID(8L);

        String newName = "";
        user.setName(newName);

        ErrorResponse errorResponse = userService.updateUser(user.getId(), user);

        User updatedUser = userService.getUserByID(8L);

        assertNotEquals(user.getName(), updatedUser.getName());
        assertNotNull(errorResponse.getErrorDetails());
    }
}