package is.hi.afk6.hbv2.entities;

/**
 * Class to hold Sign Up information for Users.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/01/2024
 * @version 1.0
 */
public class SignUpDTO
{
    private String name;
    private String ssn;
    private String phoneNumber;
    private String address;
    private String email;
    private String password;

    /**
     * Create a new empty SignUp object.
     */
    public SignUpDTO() {
    }

    /**
     * Create a new SignUp object.
     *
     * @param name        Name of User.
     * @param ssn         SSN of User.
     * @param phoneNumber Phone number of User
     * @param address     Address of User
     * @param email       E-mail of User.
     * @param password    Password of User.
     */
    public SignUpDTO(String name, String ssn, String phoneNumber, String address, String email, String password) {
        this.name = name;
        this.ssn = ssn;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
