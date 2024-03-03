package is.hi.afk6.hbv2.services.implementation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;

import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.APIService;
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
    private final APIService apiService;
    private final Executor executor;

    public QuestionServiceImplementation(APIService apiService, Executor executor)
    {
        this.apiService = apiService;
        this.executor = executor;
    }

    @Override
    public void saveNewQuestion(Question question, APICallback<Question> callback) {

    }

    @Override
    public void getAllQuestions(APICallback<List<Question>> callback) {

    }

    @Override
    public void getAllQuestionsFromList(List<Long> questionIDs, APICallback<List<Question>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                // Fetch User with corresponding ID from API.
                JSONObject returnJson = apiService.getRequest("question/getAllInList/" + questionIDs);

                if (returnJson != null)
                {
                    // Convert response from JSON to User class if response is not null.
                    Gson gson = new Gson();
                    Type responseType = new TypeToken<ResponseWrapper<Question>>() {}.getType();
                    callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                }
            }
        });
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
