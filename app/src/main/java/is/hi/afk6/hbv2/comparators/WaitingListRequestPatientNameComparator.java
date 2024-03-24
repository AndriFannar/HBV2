package is.hi.afk6.hbv2.comparators;

import java.util.Comparator;

import is.hi.afk6.hbv2.entities.WaitingListRequest;

/**
 * Comparator to compare WaitingListRequests by the name of the patient.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 24/03/2024
 * @version 1.0
 */
public class WaitingListRequestPatientNameComparator implements Comparator<WaitingListRequest>
{
    @Override
    public int compare(WaitingListRequest o1, WaitingListRequest o2) {
        return o1.getPatient().getName().compareTo(o2.getPatient().getName());
    }
}
