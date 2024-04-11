package is.hi.afk6.hbv2.comparators;

import java.util.Comparator;

import is.hi.afk6.hbv2.entities.Questionnaire;

/**
 * Comparator to compare Questionnaires by if they will be displayed on the registration form.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 05/04/2024
 * @version 1.0
 */
public class QuestionnaireDisplayComparator implements Comparator<Questionnaire>
{
    @Override
    public int compare(Questionnaire o1, Questionnaire o2) {
        return (o1.isDisplayOnForm() == o2.isDisplayOnForm() ? 0 : (o2.isDisplayOnForm() ? 1 : -1));
    }
}
