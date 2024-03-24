package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.time.format.DateTimeFormatter;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentWaitingListRequestBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;

public class WaitingListRequestFragment extends Fragment
{
    private User loggedInUser;
    private WaitingListRequest waitingListRequest;
    private WaitingListService waitingListService;
    private FragmentWaitingListRequestBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments.
        if (getArguments() != null) {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
            waitingListRequest = getArguments().getParcelable(getString(R.string.waiting_list_request));
        }

        APIService apiService = new APIServiceImplementation();

        waitingListService   = new WaitingListServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentWaitingListRequestBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if(waitingListRequest != null && loggedInUser.getWaitingListRequestID() != 0)
            fetchRequest();

        if (waitingListRequest == null)
        {
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);

            if (loggedInUser.getRole() == UserRole.USER)
                navigate(R.id.nav_create_waiting_list_request, bundle);
            else
                navigate(R.id.nav_create_waiting_list_request, bundle);

            return view;
        }

        setUpView();


        binding.buttonDeleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRequest();
            }

        });
        binding.buttonAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitingListService.updateWaitingListRequestStatus(waitingListRequest.getId(),true, new APICallback<WaitingListRequest>(){
                    @Override
                    public void onComplete(ResponseWrapper<WaitingListRequest> result) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                waitingListRequest.setStatus(true);
                                setUpView();
                            }
                        });
                    }
                });
            }
        });


        // Navigate to AnswerQuestionnaireFragment.
        binding.buttonAnswerQuestionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
                    bundle.putParcelable(getString(R.string.waiting_list_request), waitingListRequest);

                    navigate(R.id.nav_answer_questionnaire, bundle);
            }
        });

        binding.buttonViewQuestionnaireAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
                bundle.putParcelable(getString(R.string.waiting_list_request), waitingListRequest);

                navigate(R.id.nav_view_questionnaire_answers, bundle);
            }
        });

        return view;
    }

    /**
     * Fetches the User's WaitingListRequest from the API.
     */
    private void fetchRequest()
    {
        waitingListService.getWaitingListRequestByID(loggedInUser.getWaitingListRequestID(), new APICallback<WaitingListRequest>()
        {
            @Override
            public void onComplete(ResponseWrapper<WaitingListRequest> result)
            {
                if (result.getData() != null)
                {
                    waitingListRequest = result.getData();
                }
            }
        });
    }

    /**
     * Insert all info about the WaitingListRequest into the view.
     */
    private void setUpView()
    {
        if(loggedInUser.getRole().isElevatedUser()){

            binding.waitingListUserLinearLayout.setVisibility(View.VISIBLE);
            binding.fragmentUser.updateUser.setVisibility(View.GONE);
            binding.buttonAnswerQuestionnaire.setVisibility(View.GONE);

            binding.fragmentUser.userName.setText(waitingListRequest.getPatient().getName());
            binding.fragmentUser.userEmail.setText(waitingListRequest.getPatient().getEmail());
            binding.fragmentUser.userPhoneNr.setText(waitingListRequest.getPatient().getPhoneNumber());
            binding.fragmentUser.userAddress.setText(waitingListRequest.getPatient().getAddress());

            if (waitingListRequest.isStatus())
                binding.buttonAcceptRequest.setVisibility(View.GONE);

            if (!waitingListRequest.getQuestionnaireAnswers().isEmpty())
                binding.buttonViewQuestionnaireAnswers.setVisibility(View.VISIBLE);
        }
        else
        {
            if (waitingListRequest.getQuestionnaire() == null || waitingListRequest.getQuestionnaire().getQuestions().isEmpty())
                binding.buttonAnswerQuestionnaire.setVisibility(View.GONE);
            else if (!waitingListRequest.getQuestionnaireAnswers().isEmpty())
            {
                binding.buttonAnswerQuestionnaire.setClickable(false);
                binding.buttonAnswerQuestionnaire.setText(getString(R.string.questionnaire_answered));
                binding.buttonAnswerQuestionnaire.setAlpha(0.5f);
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        binding.waitingListDate.setText(waitingListRequest.getDateOfRequest().format(formatter));
        binding.waitingListDescription.setText(waitingListRequest.getDescription());
        binding.waitingListBodyPart.setText(waitingListRequest.getQuestionnaire().getName());
        binding.waitingListPhysiotherapist.setText(waitingListRequest.getStaff().getName());

        binding.waitingListStatus.setText(waitingListRequest.isStatus() ? getString(R.string.waiting_list_request_accepted) : getString(R.string.waiting_list_request_pending));
    }

    /**
     * Delete the WaitingListRequest.
     */
    private void deleteRequest()
    {
        waitingListService.deleteWaitingListRequestByID(waitingListRequest.getId(), new APICallback<WaitingListRequest>() {
            @Override
            public void onComplete(ResponseWrapper<WaitingListRequest> result)
            {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);

                        // Set the User's WaitingListRequestID to 0 and navigate to create fragment.
                        if(loggedInUser.getRole().isElevatedUser())
                        {
                            navigate(R.id.nav_waiting_list_overview, bundle);
                        }
                        else
                        {
                            loggedInUser.setWaitingListRequestID(0L);

                            navigate(R.id.nav_create_waiting_list_request, bundle);
                        }
                    }
                });

            }
        });
    }

    /**
     * Navigates to another fragment.
     */
    private void navigate(int navLocation, Bundle bundle)
    {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);
        navController.navigate(navLocation, bundle);
    }
}