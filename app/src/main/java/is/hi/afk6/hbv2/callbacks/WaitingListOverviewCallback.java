package is.hi.afk6.hbv2.callbacks;

import is.hi.afk6.hbv2.entities.WaitingListRequest;

public interface WaitingListOverviewCallback
{
    void onViewWaitingListRequestClicked(WaitingListRequest waitingListRequest);

    void onAcceptWaitingListRequestClicked(WaitingListRequest waitingListRequest);
}
