package is.hi.afk6.hbv2.comparators.questionnaireComparators;

import java.util.Comparator;

import is.hi.afk6.hbv2.entities.Questionnaire;

/**
 * Comparator to compare Questionnaires by their name.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 05/04/2024
 * @version 1.0
 */
public class QuestionnaireNameComparator implements Comparator<Questionnaire>
{
    @Override
    public int compare(Questionnaire o1, Questionnaire o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
