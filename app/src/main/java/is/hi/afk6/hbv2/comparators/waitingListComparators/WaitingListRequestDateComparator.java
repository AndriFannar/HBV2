package is.hi.afk6.hbv2.comparators.waitingListComparators;

import java.util.Comparator;

import is.hi.afk6.hbv2.entities.WaitingListRequest;

/**
 * Comparator to compare WaitingListRequests by the date of the request.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 24/03/2024
 * @version 1.0
 */
public class WaitingListRequestDateComparator implements Comparator<WaitingListRequest>
{
    @Override
    public int compare(WaitingListRequest o1, WaitingListRequest o2) {
        return o2.getDateOfRequest().compareTo(o1.getDateOfRequest());
    }
}
