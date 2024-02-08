package is.hi.afk6.hbv2.entities;

import is.hi.afk6.hbv2.entities.enums.UserRole;

/**
 * Class to hold User information.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/01/2024
 * @version 1.0
 */
public class User
{
    // Variables.
    private Long id;
    private String name;
    private String email;

    private String ssn;
    private String phoneNumber;
    private String address;
    private String specialization;

    private UserRole role;

    /**
     * Create a new empty user.
     */
    public User()
    {
        role = UserRole.USER;
    }

    /**
     * Create a new User.
     *
     * @param id             Unique ID of User.
     * @param name           Name of User.
     * @param email          E-mail of User.
     * @param ssn            SSN of User.
     * @param phoneNumber    Phone number of User.
     * @param address        Address of User.
     * @param specialization Specialization of User.
     * @param role           Role of User.
     */
    public User(Long id, String name, String email, String ssn, String phoneNumber, String address, String specialization, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.ssn = ssn;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.specialization = specialization;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", ssn='" + ssn + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", specialization='" + specialization + '\'' +
                ", role=" + role +
                '}';
    }
}
