package is.hi.afk6.hbv2.services;

import java.util.List;

import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;

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
     * @param request WaitingListRequest to save.
     * @return        Saved WaitingListRequest.
     */
    public WaitingListRequest saveNewWaitingListRequest(WaitingListRequest request);

    /**
     * Gets all saved WaitingListRequests.
     *
     * @return List of saved WaitingListRequests.
     */
    public List<WaitingListRequest> getAllWaitingListRequests();

    /**
     * Gets WaitingListRequest by unique ID.
     *
     * @param requestID Unique ID of WaitingListRequest to fetch.
     * @return          WaitingListRequest with corresponding ID, if any.
     */
    public WaitingListRequest getWaitingListRequestByID(Long requestID);

    /**
     * Gets WaitingListRequest by User registered as patient.
     *
     * @param patient User registered as patient for WaitingListRequest.
     * @return        WaitingListRequest with matching patient, if any.
     */
    public WaitingListRequest getWaitingListRequestByPatient(User patient);

    /**
     * Gets WaitingListRequests by User registered as staff.
     *
     * @param staff User registered as staff for WaitingListRequests to fetch.
     * @return      List of WaitingListRequests with inputted User registered as Staff.
     */
    public List<WaitingListRequest> getWaitingListRequestByStaff(User staff);

    /**
     * Update WaitingListRequest by unique ID.
     *
     * @param requestID      Unique ID of WaitingListRequest to update.
     * @param updatedRequest WaitingListRequest with updated info.
     */
    public void updateWaitingListRequestByID(Long requestID, WaitingListRequest updatedRequest);

    /**
     * Update status of WaitingListRequest.
     *
     * @param requestID Unique ID of WaitingListRequest to update.
     * @param newStatus New status of WaitingListRequest.
     */
    public void updateWaitingListRequestStatus(Long requestID, boolean newStatus);

    /**
     * Update Questionnaire answers for a WaitingListRequest.
     *
     * @param requestID     Unique ID of WaitingListRequest to update.
     * @param questionnaire Questionnaire with updated answers.
     */
    public void updateQuestionnaireAnswers(Long requestID, Questionnaire questionnaire);

    /**
     * Delete a WaitingListRequest by unique ID.
     *
     * @param requestID Unique ID of WaitingListRequest to delete.
     */
    public void deleteWaitingListRequestByID(Long requestID);
}
