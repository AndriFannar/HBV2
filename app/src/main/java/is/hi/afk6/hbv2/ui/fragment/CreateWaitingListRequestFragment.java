package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import static is.hi.afk6.hbv2.ui.UserHomepageActivity.LOGGED_IN_USER;
import static is.hi.afk6.hbv2.ui.UserHomepageActivity.WAITING_LIST_REQUEST;

import java.util.ArrayList;
import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentCreateWaitingListRequestBinding;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionnaireService;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;

/**
 * Fragment to register to Waiting List.
 * Creates a new WaitingListRequest for logged in User.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is
 * @since   21/02/2024
 * @version 1.0
 */
public class CreateWaitingListRequestFragment extends Fragment
{
    private FragmentCreateWaitingListRequestBinding binding;
    private User loggedInUser;
    private UserService userService;
    private WaitingListService waitingListService;
    private QuestionnaireService questionnaireService;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(LOGGED_IN_USER);
        }

        APIService apiService = new APIServiceImplementation();

        userService = new UserServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        waitingListService = new WaitingListServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        questionnaireService = new QuestionnaireServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentCreateWaitingListRequestBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        userService.getUsersByRole(UserRole.PHYSIOTHERAPIST, true, new APICallback<List<User>>() {
            @Override
            public void onComplete(ResponseWrapper<List<User>> result)
            {
                List<User> staff = result.getData();

                questionnaireService.getQuestionnairesOnForm(new APICallback<List<Questionnaire>>() {
                    @Override
                    public void onComplete(ResponseWrapper<List<Questionnaire>> result)
                    {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setUpView(staff, result.getData());
                            }
                        });
                    }
                });
            }
        });

        return view;
    }

    private void setUpView(List<User> staff, List<Questionnaire> displayQuestionnaires)
    {
        List<String> physiotherapists = new ArrayList<>();
        List<String> questionnaires = new ArrayList<>();

        for (User display : staff)
        {
            physiotherapists.add(display.getName() + " - " + display.getSpecialization());
        }

        for(Questionnaire questionnaire : displayQuestionnaires)
        {
            questionnaires.add(questionnaire.getName());
        }

        ArrayAdapter<String> physioAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, physiotherapists);
        ArrayAdapter<String> questionnaireAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, questionnaires);
        physioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionnaireAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.staffSpinner.setAdapter(physioAdapter);
        binding.questionnaireSpinner.setAdapter(questionnaireAdapter);

        binding.buttonRegisterConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(staff, displayQuestionnaires);
            }
        });
    }

    /**
     * Register User to Waiting List
     *
     * @param staff                 List of physiotherapists available.
     * @param displayQuestionnaires List of Questionnaires to display.
     */
    private void register(List<User> staff, List<Questionnaire> displayQuestionnaires)
    {
        if (binding.waitingListInfo.getText() == null)
            return;

        controlView(true, false);

        WaitingListRequest request = new WaitingListRequest(
                loggedInUser.getId(),
                staff.get(binding.staffSpinner.getSelectedItemPosition()).getId(),
                binding.waitingListInfo.getText().toString(),
                displayQuestionnaires.get(binding.questionnaireSpinner.getSelectedItemPosition()).getId()
        );

        waitingListService.saveNewWaitingListRequest(request, new APICallback<WaitingListRequest>() {
            @Override
            public void onComplete(ResponseWrapper<WaitingListRequest> result)
            {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        if (result.getData() != null)
                        {
                            controlView(false, false);

                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            WaitingListRequestFragment waitingListRequestFragment = new WaitingListRequestFragment();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(WAITING_LIST_REQUEST, request);
                            waitingListRequestFragment.setArguments(bundle);

                            fragmentTransaction.replace(R.id.user_fragment_detail, waitingListRequestFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                        else
                        {
                            controlView(false, true);
                        }
                    }
                });
            }
        });
    }

    /**
     * Displays loading when sending in a request.
     *
     * @param loading Is there data being actively fetched?
     * @param error   Display error on UI.
     */
    private void controlView(boolean loading, boolean error)
    {
        if (loading)
        {
            binding.requestError.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonRegisterConfirm.setAlpha(0.7f);
        }
        else
        {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonRegisterConfirm.setAlpha(1f);

            if (error)
                binding.requestError.setVisibility(View.VISIBLE);
        }

        binding.buttonRegisterConfirm.setClickable(!loading);
        binding.staffSpinner.setFocusableInTouchMode(!loading);
        binding.questionnaireSpinner.setFocusableInTouchMode(!loading);
        binding.waitingListInfo.setFocusableInTouchMode(!loading);
    }
}