package is.hi.afk6.hbv2.services.implementation;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.api.ErrorResponse;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.enums.Request;
import is.hi.afk6.hbv2.exceptions.APIConnectionException;
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
    private final static String API_QUESTION_LOCATION = "question/";

    public QuestionServiceImplementation(APIService apiService, Executor executor)
    {
        this.apiService = apiService;
        this.executor = executor;
    }

    @Override
    public void saveNewQuestion(Question question, APICallback<Question> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    Gson gson = new GsonBuilder().create();

                    // Convert Question info to String.
                    String requestJson = gson.toJson(question);

                    // Send info to API and get a return object.
                    JSONObject returnJson = apiService.makeNetworkRequest(
                            API_QUESTION_LOCATION + "create",
                            Request.POST,
                            null,
                            requestJson
                    );

                    if (returnJson != null)
                    {
                        // If the return object is not empty, then convert JSON data to ResponseWrapper<Question>
                        Type responseType = new TypeToken<ResponseWrapper<Question>>() {}.getType();
                        callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                    }
                }
                catch (APIConnectionException e)
                {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.setError(e.getMessage());
                    callback.onComplete(new ResponseWrapper<>(errorResponse));
                }
            }
        });
    }

    @Override
    public void getAllQuestions(APICallback<List<Question>> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                // Fetch all Questions from API.
                JSONObject returnJson = apiService.makeNetworkRequest(
                        API_QUESTION_LOCATION + "getAll",
                        Request.GET,
                        null,
                        ""
                );

                if (returnJson != null)
                {
                    // Convert response from JSON to Question class if response is not null.
                    Gson gson = new GsonBuilder().create();

                    Type responseType = new TypeToken<ResponseWrapper<List<Question>>>() {}.getType();
                    callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                }
            }
        });
    }

    @Override
    public void getAllQuestionsFromList(List<Long> questionIDs, APICallback<List<Question>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                String questions = "questionIDs=" + questionIDs.stream()
                                                            .map(Object::toString).collect(Collectors.joining(","));

                String[] requestParams = new String[] { questions };

                JSONObject returnJson = apiService.makeNetworkRequest(
                        API_QUESTION_LOCATION + "getAllInList",
                        Request.GET,
                        requestParams,
                        null
                );

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
    public void updateQuestion(Question updatedQuestion, APICallback<Question> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                try {
                    Gson gson = new GsonBuilder().create();

                    // Convert Question class to String.
                    String requestJson = gson.toJson(updatedQuestion);

                    // Send JSON data to API, wait for a return.
                    JSONObject returnJson = apiService.makeNetworkRequest(
                            API_QUESTION_LOCATION + "update",
                            Request.PUT,
                            null,
                            requestJson
                    );

                    if (returnJson != null && returnJson.length() > 0)
                    {
                        // If return is not empty, convert from JSON to ErrorResponse.
                        Type responseType = new TypeToken<ResponseWrapper<Question>>() {}.getType();

                        callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                    }
                    else
                    {
                        callback.onComplete(new ResponseWrapper<>(new Question()));
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void deleteQuestionByID(Long questionID, APICallback<Question> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                apiService.makeNetworkRequest(
                        API_QUESTION_LOCATION + "delete/" + questionID,
                        Request.DELETE,
                        null,
                        ""
                );

                callback.onComplete(new ResponseWrapper<>(new Question()));
            }
        });
    }
}
