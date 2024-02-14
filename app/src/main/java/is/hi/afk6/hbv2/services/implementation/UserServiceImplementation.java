package is.hi.afk6.hbv2.services.implementation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

import is.hi.afk6.hbv2.entities.ErrorResponse;
import is.hi.afk6.hbv2.entities.LoginDTO;
import is.hi.afk6.hbv2.entities.ResponseWrapper;
import is.hi.afk6.hbv2.entities.SignUpDTO;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;

/**
 * Service for User class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/02/2024
 * @version 1.0
 */
public class UserServiceImplementation implements UserService
{
    private final APIService apiService;

    public UserServiceImplementation(APIService apiService)
    {
        this.apiService = apiService;
    }


    @Override
    public ResponseWrapper<User> saveNewUser(SignUpDTO signUpInfo)
    {
        // Convert SignUp info to String.
        String signUpJSON = new Gson().toJson(signUpInfo);

        try
        {
            // Convert String to JSON.
            JSONObject signUpJsonObject = new JSONObject(signUpJSON);

            // Send info to API and get a return object.
            JSONObject returnJson = apiService.postRequestAsync("user/signUp", signUpJsonObject).get();

            if (returnJson != null)
            {
                // If the return object is not empty, then convert JSON data to ResponseWrapper<User>
                Gson gson = new Gson();
                Type responseType = new TypeToken<ResponseWrapper<User>>() {}.getType();
                return gson.fromJson(returnJson.toString(), responseType);
            }

        } catch (JSONException | ExecutionException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        return null;
    }


    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserByID(Long userID)
    {
        try
        {
            // Fetch User with corresponding ID from API.
            JSONObject returnJson = apiService.getRequestAsync("user/viewUser/" + userID).get();

            if (returnJson != null)
            {
                // Convert response from JSON to User class if response is not null.
                Gson gson = new Gson();
                Type responseType = new TypeToken<User>() {}.getType();
                return gson.fromJson(returnJson.toString(), responseType);
            }

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public User getUserBySSN(String ssn) {
        return null;
    }

    @Override
    public List<User> getUsersByRole(UserRole role, boolean includeElevated) {
        return null;
    }

    @Override
    public ErrorResponse updateUser(Long requestingUserID, User updatedUser)
    {
        // Convert User class to String.
        String userJson = new Gson().toJson(updatedUser);

        try
        {
            // Convert String of User class to JSONObject.
            JSONObject updatedUserJson = new JSONObject(userJson);

            // Send JSON data to API, wait for a return.
            JSONObject returnJson = apiService.putRequestAsync("user/updateUser/" + requestingUserID, updatedUserJson).get();

            if (returnJson != null)
            {
                // If return is not empty, convert from JSON to ErrorResponse.
                Gson gson = new Gson();
                Type responseType = new TypeToken<ErrorResponse>() {}.getType();
                return gson.fromJson(returnJson.toString(), responseType);
            }
        } catch (JSONException | ExecutionException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void deleteUserByID(Long userID)
    {
        // Request deletion of a User.
        apiService.deleteRequestAsync("user/deleteUser/" + userID);
    }

    @Override
    public ResponseWrapper<User> logInUser(LoginDTO login)
    {
        // Convert LogIn data to String.
        String loginJson = new Gson().toJson(login);

        try
        {
            // Convert String to JSONObject.
            JSONObject loginJsonObject = new JSONObject(loginJson);

            // Send LogIn data as JSON to API, wait for a return.
            JSONObject returnJson = apiService.postRequestAsync("user/login", loginJsonObject).get();

            if (returnJson != null)
            {
                // If return is not empty, convert to ResponseWrapper<User>.
                Gson gson = new Gson();
                Type responseType = new TypeToken<ResponseWrapper<User>>() {}.getType();
                return gson.fromJson(returnJson.toString(), responseType);
            }

        } catch (JSONException | ExecutionException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }

        return null;
    }
}
