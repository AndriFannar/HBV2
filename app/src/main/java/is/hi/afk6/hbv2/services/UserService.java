package is.hi.afk6.hbv2.services;

import java.util.List;
import java.util.concurrent.Future;

import is.hi.afk6.hbv2.entities.ErrorResponse;
import is.hi.afk6.hbv2.entities.LoginDTO;
import is.hi.afk6.hbv2.entities.ResponseWrapper;
import is.hi.afk6.hbv2.entities.SignUpDTO;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.enums.UserRole;

/**
 * Service for User class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/02/2024
 * @version 1.0
 */
public interface UserService
{
    /**
     * Saves a new User.
     *
     * @return ResponseWrapper containing saved User, or in case of errors, an ErrorResponse.
     */
    public ResponseWrapper<User> saveNewUser(SignUpDTO signUpInfo);

    /**
     * Gets all saved Users.
     *
     * @return List of all Users.
     */
    public List<User> getAllUsers();

    /**
     * Gets a User by unique ID.
     *
     * @param userID Unique ID of User to fetch.
     * @return       User with corresponding ID, if any.
     */
    public User getUserByID(Long userID);

    /**
     * Gets a User by E-mail.
     *
     * @param email E-mail of User to fetch.
     * @return      User with corresponding E-mail, if any.
     */
    public User getUserByEmail(String email);

    /**
     * Gets a User by Social Security Number.
     *
     * @param ssn Social Security Number of User to fetch.
     * @return    User with corresponding Social Security Number, if any.
     */
    public User getUserBySSN(String ssn);

    /**
     * Gets all Users with specified UserRole.
     *
     * @param role            UserRole of users to fetch.
     * @param includeElevated Include Users with higher UserRoles.
     * @return                List of Users with corresponding role.
     */
    public List<User> getUsersByRole(UserRole role, boolean includeElevated);

    /**
     * Updates User.
     *
     * @param requestingUserID Unique ID of User that is performing the update.
     * @param updatedUser      User with updated information.
     */
    public ErrorResponse updateUser(Long requestingUserID, User updatedUser);

    /**
     * Deletes a User by unique ID.
     *
     * @param userID Unique ID of User to delete.
     */
    public void deleteUserByID(Long userID);

    /**
     * Checks if a User with corresponding User info exists, and if so, returns that User.
     *
     * @param login Login info of User to find.
     * @return      ResponseWrapper containing User that corresponds with the log in info,
     *              or in case of errors, an ErrorResponse.
     */
    public Future<ResponseWrapper<User>> logInUser(LoginDTO login);
}
