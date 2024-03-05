package is.hi.afk6.hbv2.services.implementation;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;

import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.services.QuestionnaireService;

/**
 * Service for Questionnaire class.
 * Performs asynchronous calls to APIService.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/02/2024
 * @version 1.0
 */
public class QuestionnaireServiceImplementation implements QuestionnaireService
{
    private final APIService apiService;
    private final Executor executor;
    private final static String API_QUESTIONNAIRE_LOCATION = "questionnaire/";

    public QuestionnaireServiceImplementation(APIService apiService, Executor executor)
    {
        this.apiService = apiService;
        this.executor = executor;
    }

    @Override
    public void saveNewQuestionnaire(Questionnaire questionnaire, APICallback<Questionnaire> callback) {

    }

    @Override
    public void getAllQuestionnaires(APICallback<List<Questionnaire>> callback) {

    }

    @Override
    public void getQuestionnaireByID(Long questionnaireID, APICallback<Questionnaire> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                // Fetch User with corresponding ID from API.
                JSONObject returnJson = apiService.getRequest(API_QUESTIONNAIRE_LOCATION + "get/" + questionnaireID, "");

                if (returnJson != null)
                {
                    // Convert response from JSON to User class if response is not null.
                    Gson gson = new Gson();
                    Type responseType = new TypeToken<ResponseWrapper<Questionnaire>>() {}.getType();
                    callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                }
            }
        });
    }

    @Override
    public void getQuestionnairesOnForm(APICallback<List<Questionnaire>> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                JSONObject returnJson = apiService.getRequest(API_QUESTIONNAIRE_LOCATION + "getAllToDisplay", "");

                if (returnJson != null && returnJson.length() > 0)
                {
                    // If return is not empty, convert from JSON to ErrorResponse.
                    Gson gson = new Gson();
                    Type responseType = new TypeToken<ResponseWrapper<List<Questionnaire>>>() {}.getType();

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
    public void addQuestionToQuestionnaire(Long questionID, Long questionnaireID, APICallback<Questionnaire> callback) {

    }

    @Override
    public void removeQuestionFromQuestionnaire(Long questionID, Long questionnaireID, APICallback<Questionnaire> callback) {

    }

    @Override
    public void toggleDisplayQuestionnaireOnForm(Long questionnaireID, APICallback<Questionnaire> callback) {

    }

    @Override
    public void deleteQuestionnaireByID(Long questionnaireID, APICallback<Questionnaire> callback) {

    }

}
