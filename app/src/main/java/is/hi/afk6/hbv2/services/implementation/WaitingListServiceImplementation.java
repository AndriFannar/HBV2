package is.hi.afk6.hbv2.services.implementation;

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

    public WaitingListServiceImplementation(APIService apiService, Executor executor)
    {
        this.apiService = apiService;
        this.executor = executor;
    }

    @Override
    public void saveNewWaitingListRequest(WaitingListRequest request, APICallback<WaitingListRequest> callback) {

    }

    @Override
    public void getAllWaitingListRequests(APICallback<List<WaitingListRequest>> callback) {

    }

    @Override
    public void getWaitingListRequestByID(Long requestID, APICallback<WaitingListRequest> callback) {

    }

    @Override
    public void getWaitingListRequestByPatient(User patient, APICallback<WaitingListRequest> callback) {

    }

    @Override
    public void getWaitingListRequestByStaff(User staff, APICallback<List<WaitingListRequest>> callback) {

    }

    @Override
    public void updateWaitingListRequestByID(Long requestID, WaitingListRequest updatedRequest, APICallback<WaitingListRequest> callback)
    {
        executor.execute(new Runnable() {
            @Override
            public void run()
            {
                try {
                    // Convert User class to String.
                    String requestJson = new Gson().toJson(updatedRequest);

                    // Send JSON data to API, wait for a return.
                    JSONObject returnJson = apiService.putRequest("waitingList/update/" + requestID, requestJson);

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
    public void deleteWaitingListRequestByID(Long requestID, APICallback<WaitingListRequest> callback) {

    }
}
