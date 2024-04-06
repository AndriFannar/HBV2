package is.hi.afk6.hbv2.comparators;

import java.util.Comparator;

import is.hi.afk6.hbv2.entities.WaitingListRequest;

/**
 * Comparator to compare WaitingListRequests by if they have been accepted or not.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 05/04/2024
 * @version 1.0
 */
public class WaitingListRequestAcceptedComparator implements Comparator<WaitingListRequest>
{
    @Override
    public int compare(WaitingListRequest o1, WaitingListRequest o2) {
        return (o1.isStatus() == o2.isStatus() ? 0 : (o2.isStatus() ? 1 : -1));
    }
}
