package is.hi.afk6.hbv2.services.implementation;

import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;

import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.enums.Request;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.serializers.LocalDateSerializer;
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
    public void saveNewQuestionnaire(Questionnaire questionnaire, APICallback<Questionnaire> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                Gson gson = new GsonBuilder().create();

                // Convert SignUp info to String.
                String requestJson = gson.toJson(questionnaire);

                // Send info to API and get a return object.
                JSONObject returnJson = apiService.makeNetworkRequest(
                        API_QUESTIONNAIRE_LOCATION + "create",
                        Request.POST,
                        null,
                        requestJson
                );

                if (returnJson != null)
                {
                    // If the return object is not empty, then convert JSON data to ResponseWrapper<Questionnaire>
                    Type responseType = new TypeToken<ResponseWrapper<Questionnaire>>() {}.getType();
                    callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                }
            }
        });
    }

    @Override
    public void getAllQuestionnaires(APICallback<List<Questionnaire>> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                // Fetch all Questionnaires from API.
                JSONObject returnJson = apiService.makeNetworkRequest(
                        API_QUESTIONNAIRE_LOCATION + "getAll",
                        Request.GET,
                        null,
                        ""
                );

                if (returnJson != null)
                {
                    // Convert response from JSON to Questionnaire class if response is not null.
                    Gson gson = new GsonBuilder().create();

                    Type responseType = new TypeToken<ResponseWrapper<List<Questionnaire>>>() {}.getType();
                    callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                }
            }
        });
    }

    @Override
    public void getQuestionnaireByID(Long questionnaireID, APICallback<Questionnaire> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                // Fetch User with corresponding ID from API.
                JSONObject returnJson = apiService.makeNetworkRequest(
                        API_QUESTIONNAIRE_LOCATION + "get/" + questionnaireID,
                        Request.GET,
                        null,
                        ""
                );

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
                JSONObject returnJson = apiService.makeNetworkRequest(
                        API_QUESTIONNAIRE_LOCATION + "getAllToDisplay",
                        Request.GET,
                        null,
                        ""
                );

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
    public void updateQuestionnaire(Questionnaire updatedQuestionnaire, APICallback<Questionnaire> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                try {
                    Gson gson = new GsonBuilder().create();

                    // Convert User class to String.
                    String requestJson = gson.toJson(updatedQuestionnaire);

                    // Send JSON data to API, wait for a return.
                    JSONObject returnJson = apiService.makeNetworkRequest(
                            API_QUESTIONNAIRE_LOCATION + "update",
                            Request.PUT,
                            null,
                            requestJson
                    );

                    if (returnJson != null && returnJson.length() > 0)
                    {
                        // If return is not empty, convert from JSON to ErrorResponse.
                        Type responseType = new TypeToken<ResponseWrapper<Questionnaire>>() {}.getType();

                        callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                    }
                    else
                    {
                        callback.onComplete(new ResponseWrapper<>(updatedQuestionnaire));
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
    public void updateDisplayQuestionnaireOnForm(Long questionnaireID, boolean display, APICallback<Questionnaire> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                String[] requestParam = new String[] { "updatedDisplay=" + display };

                apiService.makeNetworkRequest(
                        API_QUESTIONNAIRE_LOCATION + "setDisplay/" + questionnaireID,
                        Request.PUT,
                        requestParam,
                        ""
                );

                callback.onComplete(new ResponseWrapper<>(new Questionnaire()));
            }
        });
    }

    @Override
    public void deleteQuestionnaireByID(Long questionnaireID, APICallback<Questionnaire> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                apiService.makeNetworkRequest(
                        API_QUESTIONNAIRE_LOCATION + "delete/" + questionnaireID,
                        Request.DELETE,
                        null,
                        ""
                );

                callback.onComplete(new ResponseWrapper<>(new Questionnaire()));
            }
        });
    }

}
