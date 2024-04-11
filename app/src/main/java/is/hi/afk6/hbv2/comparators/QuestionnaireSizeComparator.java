package is.hi.afk6.hbv2.comparators;

import java.util.Comparator;

import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.WaitingListRequest;

/**
 * Comparator to compare Questionnaires by the size of their Question list.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 05/04/2024
 * @version 1.0
 */
public class QuestionnaireSizeComparator implements Comparator<Questionnaire>
{
    @Override
    public int compare(Questionnaire o1, Questionnaire o2) {
        return Double.compare(o2.getQuestions().size(), o1.getQuestions().size());
    }
}
