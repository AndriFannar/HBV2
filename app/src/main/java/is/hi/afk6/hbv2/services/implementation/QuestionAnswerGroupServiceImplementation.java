package is.hi.afk6.hbv2.services.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;

import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.entities.QuestionAnswerGroup;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.enums.Request;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.services.QuestionAnswerGroupService;

/**
 * Service for QuestionAnswerGroup class.
 * Performs asynchronous calls to APIService.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 06/04/2024
 * @version 1.0
 */
public class QuestionAnswerGroupServiceImplementation implements QuestionAnswerGroupService
{
    private final APIService apiService;
    private final Executor executor;
    private final static String API_QUESTION_ANSWER_GROUP_LOCATION = "questionAnswerGroup/";

    public QuestionAnswerGroupServiceImplementation(APIService apiService, Executor executor)
    {
        this.apiService = apiService;
        this.executor = executor;
    }

    @Override
    public void saveNewQuestionAnswerGroup(QuestionAnswerGroup questionAnswerGroup, APICallback<QuestionAnswerGroup> callback) {

    }

    @Override
    public void getAllQuestionAnswerGroup(APICallback<List<QuestionAnswerGroup>> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                // Fetch all Questionnaires from API.
                JSONObject returnJson = apiService.makeNetworkRequest(
                        API_QUESTION_ANSWER_GROUP_LOCATION + "getAll",
                        Request.GET,
                        null,
                        ""
                );

                if (returnJson != null)
                {
                    // Convert response from JSON to QuestionAnswerGroup class if response is not null.
                    Gson gson = new GsonBuilder().create();

                    Type responseType = new TypeToken<ResponseWrapper<List<QuestionAnswerGroup>>>() {}.getType();
                    callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                }
            }
        });
    }

    @Override
    public void getQuestionAnswerGroupById(Long questionAnswerGroupID, APICallback<QuestionAnswerGroup> callback) {

    }

    @Override
    public void updateQuestionAnswerGroup(QuestionAnswerGroup updatedQuestionAnswerGroup, APICallback<QuestionAnswerGroup> callback) {

    }

    @Override
    public void deleteQuestionAnswerGroup(Long questionAnswerGroupID, APICallback<QuestionAnswerGroup> callback) {

    }
}
