package is.hi.afk6.hbv2.services;

import android.content.Context;
import android.location.Location;

import java.util.List;

import is.hi.afk6.hbv2.entities.dtos.LoginDTO;
import is.hi.afk6.hbv2.entities.dtos.SignUpDTO;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.entities.enums.UserRole;

/**
 * Service for User class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/02/2024
 * @version 2.0
 */
public interface UserService
{
    /**
     * Saves a new User.
     *
     * @param signUpInfo Info for User to save.
     * @param callback   Callback for when User has been successfully saved.
     *                   The callback will have the object returned from the action as a ResponseWrapper.
     */
    void saveNewUser(SignUpDTO signUpInfo, APICallback<User> callback);

    /**
     * Gets all saved Users.
     *
     * @param callback Callback for when a list of Users has been fetched.
     *                 The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getAllUsers(APICallback<List<User>> callback);

    /**
     * Gets a User by unique ID.
     *
     * @param userID   Unique ID of User to fetch.
     * @param callback Callback for when the action has been completed, and will contain either:
     *                 A User with a matching ID, or an ErrorResponse.
     *                 The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getUserByID(Long userID, APICallback<User> callback);

    /**
     * Gets a User by E-mail.
     *
     * @param email    E-mail of User to fetch.
     * @param callback Callback for when the method has completed fetching,
     *                 and returns either a User with a matching E-mail, or an ErrorResponse.
     *                 The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getUserByEmail(String email, APICallback<User> callback);

    /**
     * Gets a User by Social Security Number.
     *
     * @param ssn      Social Security Number of User to fetch.
     * @param callback Callback for when the method has completed fetching,
     *                 and returns either a User with a matching SSN, or an ErrorResponse.
     *                 The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getUserBySSN(String ssn, APICallback<User> callback);

    /**
     * Gets all Users with specified UserRole.
     *
     * @param role            UserRole of users to fetch.
     * @param includeElevated Include Users with higher UserRoles.
     * @param callback        Callback for when the method has returned with a list of Users with a matching role.
     *                        The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getUsersByRole(UserRole role, boolean includeElevated, APICallback<List<User>> callback);

    /**
     * Gets all Users with specified UserRole.
     *
     * @param role            UserRole of users to fetch.
     * @param includeElevated Include Users with higher UserRoles.
     * @param currentLocation Order Users by their distance from the User.
     * @param context         Context to enable getting geolocation from User's addresses.
     * @param callback        Callback for when the method has returned with a list of Users with a matching role.
     *                        The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getUsersByRole(UserRole role, boolean includeElevated, Location currentLocation, Context context, APICallback<List<User>> callback);

    /**
     * Updates User.
     *
     * @param requestingUserID Unique ID of User that is performing the update.
     * @param updatedUser      User with updated information.
     * @param callback         Callback for when the method has updated the User, or if update was unsuccessful,
     *                         with an ErrorResponse containing reasons for unsuccessful update.
     *                         The callback will have the object returned from the action as a ResponseWrapper.
     */
    void updateUser(Long requestingUserID, User updatedUser, APICallback<User> callback);

    /**
     * Deletes a User by unique ID.
     *
     * @param userID   Unique ID of User to delete.
     * @param callback Callback for when the method has deleted the User.
     *                 The callback will have the object returned from the action as a ResponseWrapper.
     */
    void deleteUserByID(Long userID, APICallback<User> callback);

    /**
     * Checks if a User with corresponding User info exists, and if so, returns that User.
     *
     * @param login    Login info of User to find.
     * @param callback Callback for when the method has returned with a matching User, or if login was unsuccessful,
     *                 with an ErrorResponse containing reasons for unsuccessful login.
     *                 The callback will have the object returned from the action as a ResponseWrapper.
     */
    void logInUser(LoginDTO login, APICallback<User> callback);
}
