package is.hi.afk6.hbv2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import is.hi.afk6.hbv2.entities.enums.UserRole;

/**
 * Class to hold User information.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/01/2024
 * @version 1.0
 */
public class User implements Parcelable
{
    // Variables.
    private Long id;
    private String name;
    private String email;

    private String ssn;
    private String phoneNumber;
    private String address;
    private String specialization;
    private Long waitingListRequestID;

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
    public User(Long id, String name, String email, String ssn, String phoneNumber, String address, String specialization, Long waitingListRequestID, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.ssn = ssn;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.specialization = specialization;
        this.waitingListRequestID = waitingListRequestID;
        this.role = role;
    }

    /**
     * Create a new User from Parcel.
     *
     * @param in Parcel to create User from.
     */
    private User(Parcel in)
    {
        this.id = in.readLong();
        this.name = in.readString();
        this.email = in.readString();
        this.ssn = in.readString();
        this.phoneNumber = in.readString();
        this.address = in.readString();
        this.specialization = in.readString();
        this.waitingListRequestID = in.readLong();
        this.role = UserRole.values()[in.readInt()];
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

    public Long getWaitingListRequestID() {
        return waitingListRequestID;
    }

    public void setWaitingListRequestID(Long waitingListRequestID) {
        this.waitingListRequestID = waitingListRequestID;
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
                ", waitingListRequestID='" + waitingListRequestID + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(ssn);
        dest.writeString(phoneNumber);
        dest.writeString(address);
        dest.writeString(specialization);
        dest.writeLong(waitingListRequestID);
        dest.writeInt(role.ordinal());
    }


    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>()
    {
        public User createFromParcel(Parcel in)
        {
            return new User(in);
        }

        public User[] newArray(int size)
        {
            return new User[size];
        }
    };
}
