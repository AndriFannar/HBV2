package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.adapters.WaitingListRequestPhysioAdapter;
import is.hi.afk6.hbv2.adapters.WaitingListRequestReceptionAdapter;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.callbacks.WaitingListDeleteCallback;
import is.hi.afk6.hbv2.callbacks.WaitingListViewCallback;
import is.hi.afk6.hbv2.databinding.FragmentWaitingListRequestOverviewBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;

/**
 * Fragment that displays an overview of a Physiotherapists' WaitingListRequests.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 14/03/2024
 * @version 1.0
 */
public class WaitingListRequestOverviewFragment extends Fragment implements WaitingListViewCallback, WaitingListDeleteCallback
{
    private FragmentWaitingListRequestOverviewBinding binding;
    private User loggedInUser;
    private WaitingListService waitingListService;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
        }

        APIService apiService = new APIServiceImplementation();
        waitingListService = new WaitingListServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWaitingListRequestOverviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.requestOverviewRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        populateList();

        return view;
    }

    /**
     * Inserts data into the RecyclerView.
     */
    private void populateList()
    {
        viewControl(true, false);

        WaitingListRequestOverviewFragment that = this;
        if (loggedInUser.getRole().isElevatedUser())
        {
            waitingListService.getWaitingListRequestByStaff(loggedInUser, new APICallback<List<WaitingListRequest>>()
            {
                @Override
                public void onComplete(ResponseWrapper<List<WaitingListRequest>> result)
                {
                    if (result.getData() != null)
                    {
                        requireActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                viewControl(false, result.getData().isEmpty());
                                WaitingListRequestPhysioAdapter adapter = new WaitingListRequestPhysioAdapter(result.getData(), that);

                                binding.requestOverviewRecyclerView.setAdapter(adapter);
                            }
                        });
                    }
                }
            });
        }
        else if (loggedInUser.getRole() == UserRole.STAFF)
        {
            waitingListService.getAllWaitingListRequests(new APICallback<List<WaitingListRequest>>()
            {
                @Override
                public void onComplete(ResponseWrapper<List<WaitingListRequest>> result)
                {
                    if (result.getData() != null)
                    {
                        requireActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                viewControl(false, result.getData().isEmpty());
                                WaitingListRequestReceptionAdapter adapter = new WaitingListRequestReceptionAdapter(result.getData(), that);

                                binding.requestOverviewRecyclerView.setAdapter(adapter);
                            }
                        });
                    }
                }
            });
        }
    }

    private void viewControl(boolean loading, boolean empty)
    {
        if (loading)
        {
            binding.requestOverviewRecyclerView.setVisibility(View.GONE);
            binding.requestOverviewProgressBar.setVisibility(View.VISIBLE);
        }
        else if (empty)
        {
            binding.requestOverviewRecyclerView.setVisibility(View.GONE);
            binding.requestOverviewProgressBar.setVisibility(View.GONE);
            binding.requestOverviewError.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.requestOverviewRecyclerView.setVisibility(View.VISIBLE);
            binding.requestOverviewProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAcceptWaitingListRequestClicked(WaitingListRequest request)
    {
        waitingListService.updateWaitingListRequestStatus(request.getId(), true, new APICallback<WaitingListRequest>()
        {
            @Override
            public void onComplete(ResponseWrapper<WaitingListRequest> result)
            {
            }
        });
    }

    @Override
    public void onViewWaitingListRequestClicked(WaitingListRequest request)
    {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
        bundle.putParcelable(getString(R.string.waiting_list_request), request);
        navController.navigate(R.id.nav_waiting_list_request, bundle);
    }

    @Override
    public void onDeleteWaitingListRequestClicked(WaitingListRequest waitingListRequest)
    {
        waitingListService.deleteWaitingListRequestByID(waitingListRequest.getId(), new APICallback<WaitingListRequest>()
        {
            @Override
            public void onComplete(ResponseWrapper<WaitingListRequest> result)
            {
            }
        });
    }
}