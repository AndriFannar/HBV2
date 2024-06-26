package is.hi.afk6.hbv2.services.implementation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import is.hi.afk6.hbv2.comparators.userComparators.UserDistanceComparator;
import is.hi.afk6.hbv2.entities.api.ErrorResponse;
import is.hi.afk6.hbv2.entities.dtos.LoginDTO;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.dtos.SignUpDTO;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.entities.enums.Request;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.exceptions.LocationCalculationException;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.services.UserService;

/**
 * Service for User class.
 * Performs asynchronous calls to APIService.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/02/2024
 * @version 1.5
 */
public class UserServiceImplementation implements UserService
{
    private final APIService apiService;
    private final Executor executor;
    private final static String API_USER_LOCATION = "user/";

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

                // Send info to API and get a return object.
                JSONObject returnJson = apiService.makeNetworkRequest(
                        API_USER_LOCATION + "signUp",
                        Request.POST,
                        null,
                        signUpJSON
                );

                if (returnJson != null)
                {
                    // If the return object is not empty, then convert JSON data to ResponseWrapper<User>
                    Gson gson = new Gson();
                    Type responseType = new TypeToken<ResponseWrapper<User>>() {}.getType();
                    callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                }
            }
        });
    }


    @Override
    public void getAllUsers(APICallback<List<User>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                // Fetch User with corresponding ID from API.
                JSONObject returnJson = apiService.makeNetworkRequest(
                        API_USER_LOCATION + "getAll",
                        Request.GET,
                        null,
                        ""
                );

                if (returnJson != null)
                {
                    // Convert response from JSON to User class if response is not null.
                    Gson gson = new Gson();
                    Type responseType = new TypeToken<ResponseWrapper<List<User>>>() {}.getType();
                    callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                }
            }
        });
    }

    @Override
    public void getUserByID(Long userID, APICallback<User> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                // Fetch User with corresponding ID from API.
                JSONObject returnJson = apiService.makeNetworkRequest(
                        API_USER_LOCATION + "view/" + userID,
                        Request.GET,
                        null,
                        ""
                );

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
    public void getUserByEmail(String email, APICallback<User> callback)
    {

    }

    @Override
    public void getUserBySSN(String ssn, APICallback<User> callback) {

    }

    @Override
    public void getUsersByRole(final UserRole role, boolean includeElevated, APICallback<List<User>> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                String urlExtension = API_USER_LOCATION + "getByRole";

                String[] requestParams = new String[2];

                requestParams[0] = ("userRole=" + role.toString());
                requestParams[1] = ("elevated=" + includeElevated);

                JSONObject returnJson = apiService.makeNetworkRequest(
                        urlExtension,
                        Request.GET,
                        requestParams,
                        ""
                );

                if (returnJson != null && returnJson.length() > 0)
                {
                    // If return is not empty, convert from JSON to User.
                    Gson gson = new Gson();
                    Type responseType = new TypeToken<ResponseWrapper<List<User>>>() {}.getType();

                    ResponseWrapper<List<User>> responseWrapper = gson.fromJson(returnJson.toString(), responseType);

                    callback.onComplete(responseWrapper);
                }
                else
                {
                    callback.onComplete(new ResponseWrapper<>(new ArrayList<>()));
                }
            }

        });
    }

    @Override
    public void getUsersByRole(final UserRole role, boolean includeElevated, Location currentLocation, Context context, APICallback<List<User>> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getUsersByRole(role, includeElevated, new APICallback<List<User>>() {
                    @Override
                    public void onComplete(ResponseWrapper<List<User>> result)
                    {
                        if (result.getData() != null && !result.getData().isEmpty())
                        {
                            List<User> users = result.getData();
                            getUsersLocationFromAddress(users, context);
                            users.sort(new UserDistanceComparator(currentLocation));
                            callback.onComplete(new ResponseWrapper<>(users));
                        }
                        else
                        {
                            callback.onComplete(new ResponseWrapper<>(new ArrayList<>()));
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean verifyAddress(String address, Context context)
    {
        try
        {
            Address location = getLocationFromAddress(address, context);
            return location != null;
        }
        catch (LocationCalculationException e)
        {
            return false;
        }
    }

    /**
     * Gets the location of all User in the given list from their address.
     *
     * @param users   List of Users to get location for.
     * @param context Context to enable getting geolocation from User's addresses.
     */
    private void getUsersLocationFromAddress(List<User> users, Context context)
    {
        // Create an empty Location object.
        Location location;

        for (User user : users)
        {
            // Reset the location for each User.
            location = new Location("");

            try
            {
                // Get the address of the User.
                Address address = getLocationFromAddress(user.getAddress(), context);

                // Set the location of the address.
                location.setLatitude(address.getLatitude());
                location.setLongitude(address.getLongitude());
            }
            catch (LocationCalculationException e)
            {
                // If an error occurs, do nothing.
                // The users address will be set as the default location.
            }

            // Update the User's location.
            user.setLocation(location);
        }
    }

    /**
     * Finds an Adress from a String.
     *
     * @param address String of an address to find.
     * @param context Context.
     * @return        Address object from the String.
     */
    private Address getLocationFromAddress(String address, Context context)
    {
        // Create the Geocoder.
        Geocoder geocoder = new Geocoder(context);

        try
        {
            // Get the address from the String.
            List<Address> addresses = geocoder.getFromLocationName(address, 1);

            // If an address is found, return it.
            if (addresses != null && !addresses.isEmpty())
            {
                return addresses.get(0);
            }
            else
            {
                throw new LocationCalculationException("Could not find location for address: " + address);
            }
        }
        catch (IOException e)
        {
            throw new LocationCalculationException("Error getting location from address: " + e);
        }
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

                    // Send JSON data to API, wait for a return.
                    JSONObject returnJson = apiService.makeNetworkRequest(
                            API_USER_LOCATION + "update/" + requestingUserID,
                            Request.PUT,
                            null,
                            userJson
                    );

                    if (returnJson != null && returnJson.length() > 0)
                    {
                        // If return is not empty, convert from JSON to ErrorResponse.
                        Gson gson = new Gson();
                        Type responseType = new TypeToken<ResponseWrapper<User>>() {}.getType();

                        callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                    }
                    else
                    {
                        callback.onComplete(new ResponseWrapper<>(updatedUser));
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
    public void deleteUserByID(Long userID, APICallback<User> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                apiService.makeNetworkRequest(
                        API_USER_LOCATION + "delete/" + userID,
                        Request.DELETE,
                        null,
                        ""
                );

                callback.onComplete(new ResponseWrapper<>(new User()));
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
                    // Convert logIn data to String.
                    String loginJson = new Gson().toJson(login);

                    // Send logIn data as JSON to API, wait for a return.
                    JSONObject returnJson = apiService.makeNetworkRequest(
                            API_USER_LOCATION + "login",
                            Request.POST,
                            null,
                            loginJson
                    );

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
