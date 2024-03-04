package is.hi.afk6.hbv2.services.implementation;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.time.LocalDate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;

import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.serializers.LocalDateSerializer;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.services.WaitingListService;

/**
 * Service for WaitingListRequest class.
 * Performs asynchronous calls to APIService.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/02/2024
 * @version 1.1
 */
public class WaitingListServiceImplementation implements WaitingListService
{
    private final APIService apiService;
    private final Executor executor;
    private final static String API_WAITING_LIST_LOCATION = "waitingList/";

    public WaitingListServiceImplementation(APIService apiService, Executor executor)
    {
        this.apiService = apiService;
        this.executor = executor;
    }

    @Override
    public void saveNewWaitingListRequest(WaitingListRequest request, APICallback<WaitingListRequest> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                        .create();

                // Convert SignUp info to String.
                String requestJson = gson.toJson(request);

                Log.d("Request", requestJson);

                // Send info to API and get a return object.
                JSONObject returnJson = apiService.postRequest(API_WAITING_LIST_LOCATION + "create", requestJson);

                if (returnJson != null)
                {
                    // If the return object is not empty, then convert JSON data to ResponseWrapper<WaitingListRequest>
                    Type responseType = new TypeToken<ResponseWrapper<WaitingListRequest>>() {}.getType();
                    callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                }
            }
        });
    }

    @Override
    public void getAllWaitingListRequests(APICallback<List<WaitingListRequest>> callback) {

    }

    @Override
    public void getWaitingListRequestByID(Long requestID, APICallback<WaitingListRequest> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                // Fetch User with corresponding ID from API.
                JSONObject returnJson = apiService.getRequest(API_WAITING_LIST_LOCATION + "view/" + requestID, "");

                if (returnJson != null)
                {
                    // Convert response from JSON to User class if response is not null.
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                            .create();

                    Type responseType = new TypeToken<ResponseWrapper<WaitingListRequest>>() {}.getType();
                    callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                }
            }
        });
    }

    @Override
    public void getWaitingListRequestByPatient(User patient, APICallback<WaitingListRequest> callback) {

    }

    @Override
    public void getWaitingListRequestByStaff(User staff, APICallback<List<WaitingListRequest>> callback) {

    }

    @Override
    public void updateWaitingListRequestByID(WaitingListRequest updatedRequest, APICallback<WaitingListRequest> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                try {
                    // Convert User class to String.
                    String requestJson = new Gson().toJson(updatedRequest);

                    // Send JSON data to API, wait for a return.
                    JSONObject returnJson = apiService.putRequest(API_WAITING_LIST_LOCATION + "update", requestJson);

                    if (returnJson != null && returnJson.length() > 0)
                    {
                        // If return is not empty, convert from JSON to ErrorResponse.
                        Gson gson = new Gson();
                        Type responseType = new TypeToken<ResponseWrapper<WaitingListRequest>>() {}.getType();

                        callback.onComplete(gson.fromJson(returnJson.toString(), responseType));
                    }
                    else
                    {
                        callback.onComplete(new ResponseWrapper<WaitingListRequest>(updatedRequest));
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
    public void updateWaitingListRequestStatus(Long requestID, boolean newStatus, APICallback<WaitingListRequest> callback) {

    }

    @Override
    public void updateQuestionnaireAnswers(Long requestID, Questionnaire questionnaire, APICallback<WaitingListRequest> callback) {

    }

    @Override
    public void deleteWaitingListRequestByID(Long requestID, APICallback<WaitingListRequest> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                apiService.deleteRequest(API_WAITING_LIST_LOCATION + "delete/" + requestID);
                callback.onComplete(null);
            }
        });
    }
}
