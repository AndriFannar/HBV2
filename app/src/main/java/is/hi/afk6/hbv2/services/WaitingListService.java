package is.hi.afk6.hbv2.services;

import java.util.List;

import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;

public interface WaitingListService
{
    public WaitingListRequest saveNewWaitingListRequest(WaitingListRequest request);
    public List<WaitingListRequest> getAllWaitingListRequests();
    public WaitingListRequest getWaitingListRequestByID(Long requestID);
    public WaitingListRequest getWaitingListRequestByPatient(User patient);
    public List<WaitingListRequest> getWaitingListRequestByStaff(User staff);
    public void updateWaitingListRequestByID(Long requestID, WaitingListRequest updatedRequest);
    public void updateWaitingListRequestStatus(Long requestID, boolean newStatus);
    public void updateQuestionnaireAnswers(Long requestID, Questionnaire questionnaire);
    public void deleteWaitingListRequestByID(Long requestID);
}
