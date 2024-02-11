package is.hi.afk6.hbv2.services.implementation;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import is.hi.afk6.hbv2.entities.LoginDTO;
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
    public User saveNewUser(SignUpDTO signUpInfo)
    {
        String signUpJSON = new Gson().toJson(signUpInfo);

        try
        {
            JSONObject signUpJsonObject = new JSONObject(signUpJSON);

            JSONObject userJson = apiService.postRequestAsync("user/signUp", signUpJsonObject).get();

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


    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUserByID(Long id) {
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
    public void updateUserByID(Long userID, User updatedUser) {

    }

    @Override
    public void deleteUserByID(Long userID) {

    }

    @Override
    public User logInUser(LoginDTO login)
    {
        String loginJson = new Gson().toJson(login);

        try
        {
            JSONObject loginJsonObject = new JSONObject(loginJson);

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
