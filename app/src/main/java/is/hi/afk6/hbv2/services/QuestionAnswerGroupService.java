package is.hi.afk6.hbv2.services;


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
     */
    void saveNewQuestionAnswerGroup(QuestionAnswerGroup questionAnswerGroup);


    /**
     * Gets all QuestionAnswerGroup objects.
     *
     * @return List of all QuestionAnswerGroup objects.
     */
    List<QuestionAnswerGroup> getAllQuestionAnswerGroup();


    /**
     * Gets the QuestionAnswerGroup with the corresponding ID.
     *
     * @param questionAnswerGroupID The ID of the QuestionAnswerGroup to fetch.
     * @return                      QuestionAnswerGroup with corresponding question ID.
     */
    QuestionAnswerGroup getQuestionAnswerGroupById(Long questionAnswerGroupID);


    /**
     * Update a matching QuestionAnswerGroup.
     *
     * @param updatedQuestionAnswerGroup Updated QuestionAnswerGroup.
     */
    void updateQuestionAnswerGroup(QuestionAnswerGroup updatedQuestionAnswerGroup);


    /**
     * Deletes a QuestionAnswerGroup with a corresponding ID.
     *
     * @param questionAnswerGroupID ID of the QuestionAnswerGroup to delete.
     */
    void deleteQuestionAnswerGroupByID(Long questionAnswerGroupID);
}
