package is.hi.afk6.hbv2.comparators;

import android.location.Location;
import android.util.Log;

import java.util.Comparator;

import is.hi.afk6.hbv2.entities.User;

/**
 * Comparator for sorting Users by distance from a given location.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 03/03/2024
 * @version 1.0
 */
public class UserDistanceComparator implements Comparator<User>
{
    private final Location currentLocation;

    /**
     * Create a new UserDistanceComparator.
     *
     * @param currentLocation Location to compare distance to.
     */
    public UserDistanceComparator(Location currentLocation)
    {
        this.currentLocation = currentLocation;
    }

    @Override
    public int compare(User o1, User o2)
    {
        float distanceO1 = currentLocation.distanceTo(o1.getLocation());
        o1.setDistance(distanceO1 / 1000);
        float distanceO2 = currentLocation.distanceTo(o2.getLocation());
        o2.setDistance(distanceO2 / 1000);
        return Float.compare(distanceO1, distanceO2);
    }
}
