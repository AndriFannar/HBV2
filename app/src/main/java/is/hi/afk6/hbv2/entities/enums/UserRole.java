package is.hi.afk6.hbv2.entities.enums;

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
        this.staffMember = staffMember;
        this.elevatedUser = elevatedUser;
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
}
