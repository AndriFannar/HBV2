package is.hi.afk6.hbv2.services.implementation;

import java.util.List;

import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.services.QuestionService;

/**
 * Service for Question class.
 * Performs asynchronous calls to APIService.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/02/2024
 * @version 1.0
 */
public class QuestionServiceImplementation implements QuestionService
{

    @Override
    public void saveNewQuestion(Question question, APICallback<Question> callback) {

    }

    @Override
    public void getAllQuestions(APICallback<List<Question>> callback) {

    }

    @Override
    public void getAllQuestionsFromList(List<Long> questionIDs, APICallback<List<Question>> callback) {

    }

    @Override
    public void getQuestionByID(Long questionID, APICallback<Question> callback) {

    }

    @Override
    public void updateQuestionByID(Long questionID, Question updatedQuestion, APICallback<Question> callback) {

    }

    @Override
    public void deleteQuestionByID(Long questionID, APICallback<Question> callback) {

    }
}
