package is.hi.afk6.hbv2.comparators;

import java.util.Comparator;

import is.hi.afk6.hbv2.entities.WaitingListRequest;

/**
 * Comparator to compare WaitingListRequests by the grade of the request.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 24/03/2024
 * @version 1.0
 */
public class WaitingListRequestGradeComparator implements Comparator<WaitingListRequest>
{
    @Override
    public int compare(WaitingListRequest o1, WaitingListRequest o2) {
        return Double.compare(o1.getGrade(), o2.getGrade());
    }
}
