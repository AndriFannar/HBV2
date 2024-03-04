package is.hi.afk6.hbv2.services;

import java.util.List;

import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.entities.api.APICallback;

/**
 * Service for WaitingListRequest class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/02/2024
 * @version 1.0
 */
public interface WaitingListService
{
    /**
     * Saves a new WaitingListRequest.
     *
     * @param request  WaitingListRequest to save.
     * @param callback Callback for when WaitingListRequest has been successfully saved and method has returned.
     *                 The callback will have the object returned from the action as a ResponseWrapper.
     */
    void saveNewWaitingListRequest(WaitingListRequest request, APICallback<WaitingListRequest> callback);

    /**
     * Gets all saved WaitingListRequests.
     *
     * @param callback Callback for when the method has returned a list of WaitingListRequests.
     *                 The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getAllWaitingListRequests(APICallback<List<WaitingListRequest>> callback);

    /**
     * Gets WaitingListRequest by unique ID.
     *
     * @param requestID Unique ID of WaitingListRequest to fetch.
     * @param callback  Callback for when the method has returned, either with a WaitingListRequest with a matching ID,
     *                  or with an ErrorResponse.
     *                  The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getWaitingListRequestByID(Long requestID, APICallback<WaitingListRequest> callback);

    /**
     * Gets WaitingListRequest by User registered as patient.
     *
     * @param patient  User registered as patient for WaitingListRequest.
     * @param callback Callback for when the method has returned, either with a WaitingListRequest with a matching Patient,
     *                 or with an ErrorResponse.
     *                 The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getWaitingListRequestByPatient(User patient, APICallback<WaitingListRequest> callback);

    /**
     * Gets WaitingListRequests by User registered as staff.
     *
     * @param staff    User registered as staff for WaitingListRequests to fetch.
     * @param callback Callback for when the method has returned, either with a list of WaitingListRequests
     *                 with a matching Staff, or with an ErrorResponse.
     *                 The callback will have the object returned from the action as a ResponseWrapper.
     */
    void getWaitingListRequestByStaff(User staff, APICallback<List<WaitingListRequest>> callback);

    /**
     * Update WaitingListRequest.
     *
     * @param updatedRequest WaitingListRequest with updated info.
     * @param callback       Callback for when the method has updated the WaitingListRequest, or if update was unsuccessful,
     *                       with an ErrorResponse containing reasons for unsuccessful update.
     *                       The callback will have the object returned from the action as a ResponseWrapper.
     */
    void updateWaitingListRequestByID(WaitingListRequest updatedRequest, APICallback<WaitingListRequest> callback);

    /**
     * Update status of WaitingListRequest.
     *
     * @param requestID Unique ID of WaitingListRequest to update.
     * @param newStatus New status of WaitingListRequest.
     * @param callback  Callback for when the method has updated the status of the WaitingListRequest,
     *                  or if update was unsuccessful, with an ErrorResponse containing reasons for unsuccessful update.
     *                  The callback will have the object returned from the action as a ResponseWrapper.
     */
    void updateWaitingListRequestStatus(Long requestID, boolean newStatus, APICallback<WaitingListRequest> callback);

    /**
     * Update Questionnaire answers for a WaitingListRequest.
     *
     * @param requestID     Unique ID of WaitingListRequest to update.
     * @param questionnaire Questionnaire with updated answers.
     * @param callback      Callback for when the method has updated the Questionnaire answers.
     *                      The callback will have the object returned from the action as a ResponseWrapper.
     */
    void updateQuestionnaireAnswers(Long requestID, Questionnaire questionnaire, APICallback<WaitingListRequest> callback);

    /**
     * Delete a WaitingListRequest by unique ID.
     *
     * @param requestID Unique ID of WaitingListRequest to delete.
     * @param callback  Callback for when the method has deleted the WaitingListRequest.
     *                  The callback will have the object returned from the action as a ResponseWrapper.
     */
    void deleteWaitingListRequestByID(Long requestID, APICallback<WaitingListRequest> callback);
}
