package is.hi.afk6.hbv2.services;

import java.util.List;

import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.api.APICallback;

/**
 * Service for Question class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/02/2024
 * @version 1.0
 */
public interface QuestionService
{
    /**
     * Save a new Question.
     *
     * @param question Question to save.
     * @param callback Callback for when the method has saved a new Question.
     *                 The callback will have the object returned from the action as a ResponseWrapper.
     */
    void saveNewQuestion(Question question, APICallback<Question> callback);

    /**
     * Get all saved Questions.
     *
     * @param callback Callback for when the method has fetched all Questions.
     *                 The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getAllQuestions(APICallback<List<Question>> callback);

    /**
     * Get all saved Questions from a list of unique IDs.
     *
     * @param questionIDs List of unique IDs of Questions to fetch.
     * @param callback    Callback for when the method has fetched all Questions with matching IDs, if any.
     *                    The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getAllQuestionsFromList(List<Long> questionIDs, APICallback<List<Question>> callback);

    /**
     * Get a Question by unique ID.
     *
     * @param questionID Unique ID of question to fetch.
     * @param callback   Callback for when the method has fetched a Question with matching ID, if any.
     *                   The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getQuestionByID(Long questionID, APICallback<Question> callback);

    /**
     * Update a Question with corresponding unique ID.
     *
     * @param questionID      Unique ID of Question to update.
     * @param updatedQuestion Question with updated information.
     * @param callback        Callback for when the method has updated the Question.
     *                        The callback will have the object returned from the action as a ResponseWrapper.
     */
    void updateQuestionByID(Long questionID, Question updatedQuestion, APICallback<Question> callback);

    /**
     * Delete Question by unique ID.
     *
     * @param questionID Unique ID of Question to delete.
     * @param callback   Callback for when the method has deleted the Question.
     *                   The callback will have the object returned from the action as a ResponseWrapper.
     */
    void deleteQuestionByID(Long questionID, APICallback<Question> callback);
}
