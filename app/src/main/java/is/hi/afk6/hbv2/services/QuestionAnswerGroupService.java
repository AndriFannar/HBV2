package is.hi.afk6.hbv2.services;


import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.entities.QuestionAnswerGroup;
import java.util.List;

/**
 * Service class for QuestionAnswerGroup objects.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is.
 * @since   2024-04-06
 * @version 1.0
 */
public interface QuestionAnswerGroupService
{
    /**
     * Saves a new QuestionAnswerGroup.
     *
     * @param questionAnswerGroup QuestionAnswerGroup to save.
     * @param callback            Callback to execute after saving has been completed.
     */
    void saveNewQuestionAnswerGroup(QuestionAnswerGroup questionAnswerGroup, APICallback<QuestionAnswerGroup> callback);


    /**
     * Gets all QuestionAnswerGroup objects.
     *
     * @param callback Callback to execute after fetching has been completed.
     */
    void getAllQuestionAnswerGroup(APICallback<List<QuestionAnswerGroup>> callback);


    /**
     * Gets the QuestionAnswerGroup with the corresponding ID.
     *
     * @param questionAnswerGroupID The ID of the QuestionAnswerGroup to fetch.
     * @param callback              Callback to execute after fetching has been completed.
     */
    void getQuestionAnswerGroupById(Long questionAnswerGroupID, APICallback<QuestionAnswerGroup> callback);


    /**
     * Update a matching QuestionAnswerGroup.
     *
     * @param updatedQuestionAnswerGroup Updated QuestionAnswerGroup.
     * @param callback                   Callback to execute after updating has been completed.
     */
    void updateQuestionAnswerGroup(QuestionAnswerGroup updatedQuestionAnswerGroup, APICallback<QuestionAnswerGroup> callback);


    /**
     * Deletes a QuestionAnswerGroup with a corresponding ID.
     *
     * @param questionAnswerGroupID ID of the QuestionAnswerGroup to delete.
     * @param callback              Callback to execute after deletion has been completed.
     */
    void deleteQuestionAnswerGroup(Long questionAnswerGroupID, APICallback<QuestionAnswerGroup> callback);
}
