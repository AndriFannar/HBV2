package is.hi.afk6.hbv2.services.implementation;

import java.util.List;

import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.services.WaitingListService;

/**
 * Service for WaitingListRequest class.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 08/02/2024
 * @version 1.0
 */
public class WaitingListServiceImplementation implements WaitingListService
{
    @Override
    public WaitingListRequest saveNewWaitingListRequest(WaitingListRequest request) {
        return null;
    }

    @Override
    public List<WaitingListRequest> getAllWaitingListRequests() {
        return null;
    }

    @Override
    public WaitingListRequest getWaitingListRequestByID(Long requestID) {
        return null;
    }

    @Override
    public WaitingListRequest getWaitingListRequestByPatient(User patient) {
        return null;
    }

    @Override
    public List<WaitingListRequest> getWaitingListRequestByStaff(User staff) {
        return null;
    }

    @Override
    public void updateWaitingListRequestByID(Long requestID, WaitingListRequest updatedRequest) {

    }

    @Override
    public void updateWaitingListRequestStatus(Long requestID, boolean newStatus) {

    }

    @Override
    public void updateQuestionnaireAnswers(Long requestID, Questionnaire questionnaire) {

    }

    @Override
    public void deleteWaitingListRequestByID(Long requestID) {

    }
}
