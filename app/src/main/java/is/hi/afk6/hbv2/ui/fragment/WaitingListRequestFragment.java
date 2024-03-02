package is.hi.afk6.hbv2.ui.fragment;

import static is.hi.afk6.hbv2.ui.UserHomepageActivity.LOGGED_IN_USER;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentWaitingListRequestBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;
import is.hi.afk6.hbv2.ui.fragment.DualHomepageFragment.*;

public class WaitingListRequestFragment extends Fragment
{
    private User loggedInUser;
    private WaitingListRequest waitingListRequest;
    private WaitingListService waitingListService;
    private FragmentWaitingListRequestBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            loggedInUser = getArguments().getParcelable(LOGGED_IN_USER);
            Log.d("RequestID in onCreate", "" + loggedInUser);
        }

        waitingListService = new WaitingListServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentWaitingListRequestBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Log.d("RequestID in onCreateView", "" + loggedInUser);

        waitingListService.getWaitingListRequestByID(loggedInUser.getWaitingListRequestID(), new APICallback<WaitingListRequest>()
        {
            @Override
            public void onComplete(ResponseWrapper<WaitingListRequest> result)
            {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        Log.d("Success", "Waiting List Request Fetched!");
                        Log.d("Success", result.getData().toString());
                        binding.waitingListDate.setText("Sus time");

                        binding.waitingListDescription.setText(waitingListRequest.getDescription());

                        binding.waitingListStatus.setText(waitingListRequest.isStatus() ? "Accepted" : "Pending");
                        if (waitingListRequest.getQuestionnaireID() == null)
                            binding.buttonAnswerQuestionnaire.setVisibility(View.GONE);
                    }
                });
            }
        });

        binding.buttonDeleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitingListService.deleteWaitingListRequestByID(waitingListRequest.getId(), new APICallback<WaitingListRequest>() {
                    @Override
                    public void onComplete(ResponseWrapper<WaitingListRequest> result)
                    {
                        CreateWaitingListRequestFragment fragment = new CreateWaitingListRequestFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(LOGGED_IN_USER, loggedInUser);
                        fragment.setArguments(bundle);

                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.replace(R.id.user_fragment_detail, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
            }
        });

        return view;
    }
}