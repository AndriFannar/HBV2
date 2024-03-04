package is.hi.afk6.hbv2.entities.dtos;

/**
 * Class to hold Login information for Users.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/01/2024
 * @version 1.0
 */
public class LoginDTO
{
    private String email;
    private String password;

    /**
     * Create a new empty LoginDTO object.
     */
    public LoginDTO()
    {
    }

    /**
     * Create a new LoginDTO object.
     *
     * @param email    E-mail of User to login.
     * @param password Password of User to login.
     */
    public LoginDTO(String email, String password) {
        this.email    = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
