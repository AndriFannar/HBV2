package is.hi.afk6.hbv2.entities.enums;

/**
 * Enum for User Roles.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/01/2024
 * @version 1.0
 */
public enum UserRole
{
    USER("Notandi", false, false),
    STAFF("Starfsfólk", true, false),
    PHYSIOTHERAPIST("Sjúkraþjálfari", true, true),
    ADMIN("Kerfisstjóri", true, true);

    private final String displayString;
    private final boolean staffMember;
    private final boolean elevatedUser;

    UserRole(String displayString, boolean staffMember, boolean elevatedUser)
    {
        this.displayString = displayString;
        this.staffMember   = staffMember;
        this.elevatedUser  = elevatedUser;
    }

    public String getDisplayString()
    {
        return this.displayString;
    }

    public boolean isStaffMember()
    {
        return this.staffMember;
    }

    public boolean isElevatedUser()
    {
        return this.elevatedUser;
    }

    public static UserRole fromDisplayString(String displayString) {
        for (UserRole role : UserRole.values()) {
            if (role.displayString.equals(displayString)) {
                return role;
            }
        }
        return null;
    }
}
