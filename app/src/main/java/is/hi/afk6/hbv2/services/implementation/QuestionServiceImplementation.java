package is.hi.afk6.hbv2.services.implementation;

import java.util.List;

import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.services.QuestionService;

/**
 * Service for Question class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/02/2024
 * @version 1.0
 */
public class QuestionServiceImplementation implements QuestionService
{
    @Override
    public Question saveNewQuestion(Question question) {
        return null;
    }

    @Override
    public List<Question> getAllQuestions() {
        return null;
    }

    @Override
    public Question getQuestionByID(Long questionID) {
        return null;
    }

    @Override
    public void updateQuestionByID(Long questionID, Question updatedQuestion) {

    }

    @Override
    public void deleteQuestionByID(Long questionID) {

    }
}
