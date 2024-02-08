package is.hi.afk6.hbv2.services;

import java.util.List;

import is.hi.afk6.hbv2.entities.LoginDTO;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.enums.UserRole;

public interface UserService
{
    public User saveNewUser();
    public List<User> getAllUsers();
    public User getUserByID(Long id);
    public User getUserByEmail(String email);
    public User getUserBySSN(String ssn);
    public List<User> getUsersByRole(UserRole role, boolean includeElevated);
    public void updateUserByID(Long userID, User updatedUser);
    public void deleteUserByID(Long userID);
    public User logInUser(LoginDTO login);
}
