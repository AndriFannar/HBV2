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
        String signUpJSON = new Gson().toJson(signUpInfo);

        try
        {
            JSONObject signUpJsonObject = new JSONObject(signUpJSON);

            JSONObject returnJson = apiService.postRequestAsync("user/signUp", signUpJsonObject).get();

            if (returnJson != null)
            {
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
            JSONObject returnJson = apiService.getRequestAsync("user/viewUser/" + userID).get();

            if (returnJson != null)
            {
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
        String userJson = new Gson().toJson(updatedUser);

        try
        {
            JSONObject updatedUserJson = new JSONObject(userJson);

            JSONObject returnJson = apiService.putRequestAsync("user/updateUser/" + requestingUserID, updatedUserJson).get();

            if (returnJson != null)
            {
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
        apiService.deleteRequestAsync("user/deleteUser/" + userID);
    }

    @Override
    public ResponseWrapper<User> logInUser(LoginDTO login)
    {
        String loginJson = new Gson().toJson(login);

        try
        {
            JSONObject loginJsonObject = new JSONObject(loginJson);

            JSONObject returnJson = apiService.postRequestAsync("user/login", loginJsonObject).get();

            if (returnJson != null)
            {
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
