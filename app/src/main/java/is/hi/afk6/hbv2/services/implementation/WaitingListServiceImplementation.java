package is.hi.afk6.hbv2.services.implementation;

import java.util.List;

import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.entities.api.APICallback;
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
    public void updateWaitingListRequestByID(Long requestID, WaitingListRequest updatedRequest, APICallback<WaitingListRequest> callback) {

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
