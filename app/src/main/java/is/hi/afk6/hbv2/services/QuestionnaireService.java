package is.hi.afk6.hbv2.services;

import java.util.List;

import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.callbacks.APICallback;

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
     * @param callback      Callback for when the method has saved the Questionnaire.
     *                      The callback will have the object returned from the method as a ResponseWrapper.
     */
    void saveNewQuestionnaire(Questionnaire questionnaire, APICallback<Questionnaire> callback);

    /**
     * Get all saved Questionnaires.
     *
     * @param callback Callback for when the method has fetched all Questionnaires.
     *                 The callback will have the object returned from the method as a ResponseWrapper.
     */
    void getAllQuestionnaires(APICallback<List<Questionnaire>> callback);

    /**
     * Get Questionnaire by unique ID.
     *
     * @param questionnaireID Unique ID of questionnaire to fetch.
     * @param callback        Callback for when the method has fetched a Questionnaire with a matching ID, if any.
     *                        The callback will have the object returned from the method as a ResponseWrapper.
     */
    void getQuestionnaireByID(Long questionnaireID, APICallback<Questionnaire> callback);

    /**
     * Get all Questionnaires to be displayed on a form.
     *
     * @param callback Callback for when the method has fetched all Questionnaires to be displayed.
     *                 The callback will have the object returned from the method as a ResponseWrapper.
     */
    void getQuestionnairesOnForm(APICallback<List<Questionnaire>> callback);

    /**
     * Add a Question to Questionnaire.
     *
     * @param questionID      Unique ID of Question to add to Questionnaire.
     * @param questionnaireID Unique ID of Questionnaire to add Question to.
     * @param callback        Callback for when the method has added a Question to a Questionnaire.
     *                        The callback will have the object returned from the method as a ResponseWrapper.
     */
    void addQuestionToQuestionnaire(Long questionID, Long questionnaireID, APICallback<Questionnaire> callback);

    /**
     * Remove a Question from Questionnaire.
     *
     * @param questionID      Unique ID of Question to remove from Questionnaire.
     * @param questionnaireID Unique ID of Questionnaire to remove question from.
     * @param callback        Callback for when the method has removed a Question from Questionnaire.
     *                        The callback will have the object returned from the method as a ResponseWrapper.
     */
    void removeQuestionFromQuestionnaire(Long questionID, Long questionnaireID, APICallback<Questionnaire> callback);

    /**
     * Toggle displaying Questionnaire on form.
     *
     * @param questionnaireID Unique ID of Questionnaire to toggle.
     * @param callback        Callback for when the method has marked the Questionnaire to be displayed on form.
     *                        The callback will have the object returned from the method as a ResponseWrapper.
     */
    void toggleDisplayQuestionnaireOnForm(Long questionnaireID, APICallback<Questionnaire> callback);

    /**
     * Delete a Questionnaire by unique ID.
     *
     * @param questionnaireID Unique ID of Questionnaire to delete.
     * @param callback        Callback for when the method has deleted the Questionnaire.
     *                        The callback will have the object returned from the method as a ResponseWrapper.
     */
    void deleteQuestionnaireByID(Long questionnaireID, APICallback<Questionnaire> callback);
}
