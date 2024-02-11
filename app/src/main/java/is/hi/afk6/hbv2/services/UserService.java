package is.hi.afk6.hbv2.services;

import java.util.List;

import is.hi.afk6.hbv2.entities.LoginDTO;
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
     * @return Saved User.
     */
    public User saveNewUser(SignUpDTO signUpInfo);

    /**
     * Gets all saved Users.
     *
     * @return List of all Users.
     */
    public List<User> getAllUsers();

    /**
     * Gets a User by unique ID.
     *
     * @param id Unique ID of User to fetch.
     * @return   User with corresponding ID, if any.
     */
    public User getUserByID(Long id);

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
     * Updates User by unique ID.
     *
     * @param userID      Unique ID of User to update.
     * @param updatedUser User with updated information.
     */
    public void updateUserByID(Long userID, User updatedUser);

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
     * @return      User that matches the Login info, if any.
     */
    public User logInUser(LoginDTO login);
}
