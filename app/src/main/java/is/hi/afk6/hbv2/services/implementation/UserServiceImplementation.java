package is.hi.afk6.hbv2.services.implementation;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import is.hi.afk6.hbv2.entities.ErrorResponse;
import is.hi.afk6.hbv2.entities.LoginDTO;
import is.hi.afk6.hbv2.entities.ResponseWrapper;
import is.hi.afk6.hbv2.entities.SignUpDTO;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.callbacks.APICallback;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.networking.APIService;
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
    private final Executor executor;

    public UserServiceImplementation(APIService apiService, Executor executor)
    {
        this.apiService = apiService;
        this.executor = executor;
    }


    @Override
    public void saveNewUser(SignUpDTO signUpInfo, APICallback<User> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                // Convert SignUp info to String.
                String signUpJSON = new Gson().toJson(signUpInfo);

                try
                {
                    // Convert String to JSON.
                    JSONObject signUpJsonObject = new JSONObject(signUpJSON);

                    // Send info to API and get a return object.
                    JSONObject returnJson = apiService.postRequest("user/signUp", signUpJsonObject);

                    if (returnJson != null)
                    {
                        // If the return object is not empty, then convert JSON data to ResponseWrapper<User>
                        Gson gson = new Gson();
                        Type responseType = new TypeToken<ResponseWrapper<User>>() {}.getType();
                        callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                    }

                } catch (JSONException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void getUserByID(Long userID, APICallback<User> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                // Fetch User with corresponding ID from API.
                JSONObject returnJson = apiService.getRequest("user/view/" + userID);

                if (returnJson != null)
                {
                    // Convert response from JSON to User class if response is not null.
                    Gson gson = new Gson();
                    Type responseType = new TypeToken<ResponseWrapper<User>>() {}.getType();
                    callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                }
            }
        });
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
    public void updateUser(final Long requestingUserID, final User updatedUser, final APICallback<User> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                try {
                    // Convert User class to String.
                    String userJson = new Gson().toJson(updatedUser);

                    // Convert String of User class to JSONObject.
                    JSONObject updatedUserJson = new JSONObject(userJson);

                    // Send JSON data to API, wait for a return.
                    JSONObject returnJson = apiService.putRequest("user/update/" + requestingUserID, updatedUserJson);

                    if (returnJson != null && returnJson.length() > 0)
                    {
                        // If return is not empty, convert from JSON to ErrorResponse.
                        Gson gson = new Gson();
                        Type responseType = new TypeToken<ResponseWrapper<User>>() {}.getType();

                        callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                    }
                    else
                    {
                        callback.onComplete(new ResponseWrapper<User>(updatedUser));
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void deleteUserByID(Long userID)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                apiService.deleteRequest("user/delete/" + userID);
            }
        });
    }

    @Override
    public void logInUser(final LoginDTO login, final APICallback<User> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                try {
                    // Convert LogIn data to String.
                    String loginJson = new Gson().toJson(login);

                    JSONObject loginJsonObject = new JSONObject(loginJson);

                    // Send LogIn data as JSON to API, wait for a return.
                    JSONObject returnJson = apiService.postRequest("user/login", loginJsonObject);

                    if (returnJson != null)
                    {
                        // If return is not empty, convert to ResponseWrapper<User>.
                        Gson gson = new Gson();
                        Type responseType = new TypeToken<ResponseWrapper<User>>() {}.getType();

                        callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                    }
                }
                catch (Exception e)
                {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setError("No response from API. Error: " + e);
                    ResponseWrapper<User> responseWrapper = new ResponseWrapper<>(errorResponse);
                    callback.onComplete(responseWrapper);
                }
            }
        });
    }
}
