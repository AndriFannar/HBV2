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
     * Save a new Questionnaire.
     *
     * @param questionnaire Questionnaire to save.
     * @return              Saved Questionnaire.
     */
    public Questionnaire saveNewQuestionnaire(Questionnaire questionnaire);

    /**
     * Get all saved Questionnaires.
     *
     * @return List of all saved Questionnaires, if any.
     */
    public List<Questionnaire> getAllQuestionnaires();

    /**
     * Get Questionnaire by unique ID.
     *
     * @param questionnaireID Unique ID of questionnaire to fetch.
     * @return                Questionnaire with corresponding ID, if any.
     */
    public Questionnaire getQuestionnaireByID(Long questionnaireID);

    /**
     * Get all Questionnaires to be displayed on a form.
     *
     * @return List of Questionnaires to be displayed on a form.
     */
    public List<Questionnaire> getQuestionnairesOnForm();

    /**
     * Add a Question to Questionnaire.
     *
     * @param questionID      Unique ID of Question to add to Questionnaire.
     * @param questionnaireID Unique ID of Questionnaire to add Question to.
     */
    public void addQuestionToQuestionnaire(Long questionID, Long questionnaireID);

    /**
     * Remove a Question from Questionnaire.
     *
     * @param questionID      Unique ID of Question to remove from Questionnaire.
     * @param questionnaireID Unique ID of Questionnaire to remove question from.
     */
    public void removeQuestionFromQuestionnaire(Long questionID, Long questionnaireID);

    /**
     * Toggle displaying Questionnaire on form.
     *
     * @param questionnaireID Unique ID of Questionnaire to toggle.
     */
    public void toggleDisplayQuestionnaireOnForm(Long questionnaireID);

    /**
     * Delete a Questionnaire by unique ID.
     *
     * @param questionnaireID Unique ID of Questionnaire to delete.
     */
    public void deleteQuestionnaireByID(Long questionnaireID);
}
