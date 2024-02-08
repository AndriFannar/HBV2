package is.hi.afk6.hbv2.services;

import java.util.List;

import is.hi.afk6.hbv2.entities.Questionnaire;

/**
 * Service for Questionnaire class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/02/2024
 * @version 1.0
 */
public interface QuestionnaireService
{
    /**
     *
     * @param questionnaire
     * @return
     */
    public Questionnaire saveNewQuestionnaire(Questionnaire questionnaire);
    public List<Questionnaire> getAllQuestionnaires();
    public Questionnaire getQuestionnaireByID(Long questionnaireID);
    public List<Questionnaire> getQuestionnairesOnForm();
    public void addQuestionToQuestionnaire(Long questionID, Long questionnaireID);
    public void removeQuestionFromQuestionnaire(Long questionID, Long questionnaireID);
    public void toggleDisplayQuestionnaireOnForm(Long questionnaireID);
    public void deleteQuestionnaireByID(Long questionnaireID);
}
