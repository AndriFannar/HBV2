package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.concurrent.ExecutorService;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.adapters.WaitingListRequestAdapter;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.databinding.FragmentWaitingListRequestOverviewBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
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
public class WaitingListRequestOverviewFragment extends Fragment
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
        waitingListService.getWaitingListRequestByStaff(loggedInUser, new APICallback<List<WaitingListRequest>>()
        {
            @Override
            public void onComplete(ResponseWrapper<List<WaitingListRequest>> result)
            {
                if (result.getData() != null)
                {
                    Log.d("WaitingListRequestOverviewFragment", "Populating list with " + result.getData().size() + " items.");
                    requireActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Log.d("WaitingListRequestOverviewFragment", "Populating list with " + result.getData().size() + " items.");
                            WaitingListRequestAdapter adapter = new WaitingListRequestAdapter(result.getData());

                            binding.requestOverviewRecyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });
    }
}