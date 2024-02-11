package is.hi.afk6.hbv2.services.implementation;

import java.util.List;

import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.services.QuestionnaireService;

/**
 * Service for Questionnaire class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/02/2024
 * @version 1.0
 */
public class QuestionnaireServiceImplementation implements QuestionnaireService
{
    @Override
    public Questionnaire saveNewQuestionnaire(Questionnaire questionnaire)
    {
        return null;
    }

    @Override
    public List<Questionnaire> getAllQuestionnaires() {
        return null;
    }

    @Override
    public Questionnaire getQuestionnaireByID(Long questionnaireID) {
        return null;
    }

    @Override
    public List<Questionnaire> getQuestionnairesOnForm() {
        return null;
    }

    @Override
    public void addQuestionToQuestionnaire(Long questionID, Long questionnaireID) {

    }

    @Override
    public void removeQuestionFromQuestionnaire(Long questionID, Long questionnaireID) {

    }

    @Override
    public void toggleDisplayQuestionnaireOnForm(Long questionnaireID) {

    }

    @Override
    public void deleteQuestionnaireByID(Long questionnaireID) {

    }
}
