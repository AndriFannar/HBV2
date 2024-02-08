package is.hi.afk6.hbv2.entities;

public class SignUpDTO
{
    private String name;
    private String ssn;
    private String phoneNumber;
    private String address;
    private String email;
    private String password;

    public SignUpDTO() {
    }

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
