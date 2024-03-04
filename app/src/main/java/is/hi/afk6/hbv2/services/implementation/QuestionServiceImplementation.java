package is.hi.afk6.hbv2.services.implementation;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

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
            public void run() {

                String urlExtension = "question/getAllInList";

                String questions = "questionIDs=" + questionIDs.stream()
                                                            .map(Object::toString).collect(Collectors.joining(","));

                JSONObject returnJson = apiService.getRequest(urlExtension, questions);

                if (returnJson != null && returnJson.length() > 0)
                {
                    // If return is not empty, convert from JSON to ErrorResponse.
                    Gson gson = new Gson();
                    Type responseType = new TypeToken<ResponseWrapper<List<Question>>>() {}.getType();

                    callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                }
                else
                {
                    callback.onComplete(new ResponseWrapper<>(new ArrayList<>()));
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
