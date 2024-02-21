package is.hi.afk6.hbv2.services.implementation;

import java.util.List;

import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.services.QuestionnaireService;

/**
 * Service for Questionnaire class.
 * Performs asynchronous calls to APIService.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/02/2024
 * @version 1.0
 */
public class QuestionnaireServiceImplementation implements QuestionnaireService
{

    @Override
    public void saveNewQuestionnaire(Questionnaire questionnaire, APICallback<Questionnaire> callback) {

    }

    @Override
    public void getAllQuestionnaires(APICallback<List<Questionnaire>> callback) {

    }

    @Override
    public void getQuestionnaireByID(Long questionnaireID, APICallback<Questionnaire> callback) {

    }

    @Override
    public void getQuestionnairesOnForm(APICallback<List<Questionnaire>> callback) {

    }

    @Override
    public void addQuestionToQuestionnaire(Long questionID, Long questionnaireID, APICallback<Questionnaire> callback) {

    }

    @Override
    public void removeQuestionFromQuestionnaire(Long questionID, Long questionnaireID, APICallback<Questionnaire> callback) {

    }

    @Override
    public void toggleDisplayQuestionnaireOnForm(Long questionnaireID, APICallback<Questionnaire> callback) {

    }

    @Override
    public void deleteQuestionnaireByID(Long questionnaireID, APICallback<Questionnaire> callback) {

    }
}
