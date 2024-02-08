package is.hi.afk6.hbv2.services;

import java.util.List;

import is.hi.afk6.hbv2.entities.Question;

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
     * @return         Saved Question.
     */
    public Question saveNewQuestion(Question question);

    /**
     * Get all saved Questions.
     *
     * @return List of all saved Questions.
     */
    public List<Question> getAllQuestions();

    /**
     * Get a Question by unique ID.
     *
     * @param questionID Unique ID of question to fetch.
     * @return           Question with corresponding ID, if any.
     */
    public Question getQuestionByID(Long questionID);

    /**
     * Update a Question with corresponding unique ID.
     *
     * @param questionID      Unique ID of Question to update.
     * @param updatedQuestion Question with updated information.
     */
    public void updateQuestionByID(Long questionID, Question updatedQuestion);

    /**
     * Delete Question by unique ID.
     *
     * @param questionID Unique ID of Question to delete.
     */
    public void deleteQuestionByID(Long questionID);
}
