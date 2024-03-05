package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentWaitingListRequestBinding;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionnaireService;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;

public class WaitingListRequestFragment extends Fragment
{
    private User loggedInUser;
    private User staff;
    private Questionnaire questionnaire;
    private WaitingListRequest waitingListRequest;
    private WaitingListService waitingListService;
    private QuestionnaireService questionnaireService;
    private UserService userService;
    private FragmentWaitingListRequestBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
            waitingListRequest = getArguments().getParcelable(getString(R.string.waiting_list_request));
            questionnaire = getArguments().getParcelable(getString(R.string.questionnaire));
        }

        APIService apiService = new APIServiceImplementation();

        waitingListService   = new WaitingListServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        userService          = new UserServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        questionnaireService = new QuestionnaireServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentWaitingListRequestBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (loggedInUser.getWaitingListRequestID() == null || loggedInUser.getWaitingListRequestID() == 0)
        {
            goToCreate();
            return null;
        }

        if (waitingListRequest == null)
        {
            waitingListService.getWaitingListRequestByID(loggedInUser.getWaitingListRequestID(), new APICallback<WaitingListRequest>()
            {
                @Override
                public void onComplete(ResponseWrapper<WaitingListRequest> result)
                {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            waitingListRequest = result.getData();
                            fetchData();
                        }
                    });
                }
            });
        }
        else
        {
            fetchData();
        }


        binding.buttonDeleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRequest();
            }

        });

        binding.buttonAnswerQuestionnaire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
                bundle.putParcelable(getString(R.string.waiting_list_request), waitingListRequest);
                bundle.putParcelable(getString(R.string.questionnaire), questionnaire);

                NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);
                navController.navigate(R.id.nav_answer_questionnaire, bundle);
            }
        });

        return view;
    }

    private void fetchData()
    {
        userService.getUserByID(waitingListRequest.getStaffID(), new APICallback<User>()
        {
            @Override
            public void onComplete(ResponseWrapper<User> result)
            {
                staff = result.getData();
                questionnaireService.getQuestionnaireByID(waitingListRequest.getQuestionnaireID(), new APICallback<Questionnaire>()
                {
                    @Override
                    public void onComplete(ResponseWrapper<Questionnaire> result)
                    {
                        questionnaire = result.getData();
                        requireActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run() {
                                setUpView();
                            }
                        });
                    }
                });
            }
        });
    }

    private void setUpView()
    {


        binding.waitingListDate.setText(waitingListRequest.getDateOfRequest().toString());
        binding.waitingListDescription.setText(waitingListRequest.getDescription());
        binding.waitingListBodyPart.setText(questionnaire.getName());
        binding.waitingListPhysiotherapist.setText(staff.getName());

        binding.waitingListStatus.setText(waitingListRequest.isStatus() ? getString(R.string.waiting_list_request_accepted) : getString(R.string.waiting_list_request_pending));
        if (waitingListRequest.getQuestionnaireID() == null || waitingListRequest.getQuestionnaireID() == 0 || !waitingListRequest.getQuestionnaireAnswers().isEmpty())
            binding.buttonAnswerQuestionnaire.setVisibility(View.GONE);
    }

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
                        loggedInUser.setWaitingListRequestID(0L);
                        goToCreate();
                    }
                });

            }
        });
    }

    private void goToCreate()
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);
        navController.navigate(R.id.nav_create_waiting_list_request, bundle);
    }
}