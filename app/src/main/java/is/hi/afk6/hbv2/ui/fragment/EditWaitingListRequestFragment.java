package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.databinding.FragmentEditWaitingListRequestBinding;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
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
 * A Fragment subclass for editing a WaitingListRequest.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 24/03/2024
 * @version 1.0
 */
public class EditWaitingListRequestFragment extends Fragment
{
    private User loggedInUser;
    private WaitingListRequest waitingListRequest;
    private UserService userService;
    private WaitingListService waitingListService;
    private QuestionnaireService questionnaireService;
    private FragmentEditWaitingListRequestBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get arguments.
        if (getArguments() != null) {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
            waitingListRequest = getArguments().getParcelable(getString(R.string.waiting_list_request));
        }

        APIService apiService = new APIServiceImplementation();

        userService = new UserServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        waitingListService = new WaitingListServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        questionnaireService = new QuestionnaireServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = is.hi.afk6.hbv2.databinding.FragmentEditWaitingListRequestBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if (loggedInUser.getRole().isElevatedUser())
            binding.waitingListInfo.setVisibility(View.GONE);
        else
        {
            binding.waitingListGrade.setVisibility(View.GONE);
            binding.staffSpinner.setVisibility(View.GONE);
        }

        getListsFromAPI();

        return view;
    }

    /**
     * Fetch the lists of Questionnaires and Physiotherapists from the API.
     */
    private void getListsFromAPI()
    {
        // Fetch Questionnaires to show on form.
        controlView(true, false, false);
        questionnaireService.getQuestionnairesOnForm(new APICallback<List<Questionnaire>>() {
            @Override
            public void onComplete(ResponseWrapper<List<Questionnaire>> result)
            {
                List<Questionnaire> questionnaires = result.getData();

                if (loggedInUser.getRole().isElevatedUser())
                {
                    userService.getUsersByRole(UserRole.PHYSIOTHERAPIST, true, new APICallback<List<User>>() {
                        @Override
                        public void onComplete(ResponseWrapper<List<User>> result) {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setUpView(result.getData(), questionnaires);
                                }
                            });
                        }
                    });
                }
                else
                {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUpView(null, questionnaires);
                        }
                    });
                }
            }
        });
    }


    /**
     * Set up the registration view and populates spinners with staff and questionnaires.
     *
     * @param staff                 List of physiotherapists to display in spinner.
     * @param displayQuestionnaires List of questionnaires to display in spinner.
     */
    private void setUpView(List<User> staff, List<Questionnaire> displayQuestionnaires)
    {
        List<String> physiotherapists = new ArrayList<>();
        List<String> questionnaires = new ArrayList<>();

        if (staff != null)
        {
            User requestStaff = waitingListRequest.getStaff();

            for (User display : staff)
            {
                // Add to the list.
                physiotherapists.add(display.getName() + " - " + display.getSpecialization());
                if (Objects.equals(display.getId(), waitingListRequest.getStaff().getId()))
                    requestStaff = display;
            }

            ArrayAdapter<String> physioAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, physiotherapists);
            physioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.staffSpinner.setAdapter(physioAdapter);

            binding.staffSpinner.setSelection(staff.indexOf(requestStaff));
        }

        Questionnaire requestQuestionnaire = waitingListRequest.getQuestionnaire();

        // For each Questionnaire, add its name to the list.
        for (Questionnaire questionnaire : displayQuestionnaires) {
            questionnaires.add(questionnaire.getName());

            if (Objects.equals(questionnaire.getId(), waitingListRequest.getQuestionnaire().getId()))
                requestQuestionnaire = questionnaire;
        }

        // Create an ArrayAdapter from the list to insert into the spinner.
        ArrayAdapter<String> questionnaireAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, questionnaires);
        questionnaireAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.questionnaireSpinner.setAdapter(questionnaireAdapter);

        binding.waitingListGrade.setText(String.valueOf(waitingListRequest.getGrade()));
        binding.waitingListInfo.setText(waitingListRequest.getDescription());
        binding.questionnaireSpinner.setSelection(displayQuestionnaires.indexOf(requestQuestionnaire));

        binding.buttonEditConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit(staff, displayQuestionnaires);
            }
        });

        controlView(false, false, false);
    }

    /**
     * Register User to the Waiting List
     *
     * @param staff                 List of physiotherapists available.
     * @param displayQuestionnaires List of Questionnaires to display.
     */
    private void saveEdit(List<User> staff, List<Questionnaire> displayQuestionnaires)
    {
        if (loggedInUser.getRole().isElevatedUser())
        {
            waitingListRequest.setStaff(staff.get(binding.staffSpinner.getSelectedItemPosition()));
            waitingListRequest.setGrade(Double.parseDouble(String.valueOf(binding.waitingListGrade.getText())));

            Questionnaire selectedQuestionnaire = displayQuestionnaires.get(binding.questionnaireSpinner.getSelectedItemPosition());

            if (!Objects.equals(waitingListRequest.getQuestionnaire().getId(), selectedQuestionnaire.getId()))
            {
                waitingListRequest.setQuestionnaire(selectedQuestionnaire);
                waitingListRequest.setQuestionnaireAnswers(new ArrayList<>());
            }
        }
        else
        {
            String desc = binding.waitingListInfo.getText().toString();
            if (!desc.isEmpty())
                waitingListRequest.setDescription(desc);
            else
            {
                controlView(false, false, true);
                return;
            }

            waitingListRequest.setQuestionnaire(displayQuestionnaires.get(binding.questionnaireSpinner.getSelectedItemPosition()));
            waitingListRequest.setStatus(false);
        }

        controlView(false, true, false);

        // Save the new WaitingListRequest to the API.
        waitingListService.updateWaitingListRequestByID(waitingListRequest, new APICallback<WaitingListRequest>() {
            @Override
            public void onComplete(ResponseWrapper<WaitingListRequest> result) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controlView(false, false, result.getData() == null);
                    }
                });
            }
        });
    }

    /**
     * Displays loading when sending in an updated request.
     *
     * @param fetching Is there data being actively fetched?
     * @param saving   Is there a request being saved?
     * @param error    Display error on UI.
     */
    private void controlView(boolean fetching, boolean saving, boolean error)
    {
        boolean loading = fetching || saving;

        if (loading)
        {
            binding.requestError.setVisibility(View.INVISIBLE);
            binding.buttonEditConfirm.setAlpha(0.7f);

            if (saving)
                binding.sendRequestProgressBar.setVisibility(View.VISIBLE);
            else
            {
                binding.staffSpinner.setAlpha(0.7f);
                binding.questionnaireSpinner.setAlpha(0.7f);

                binding.staffSpinnerProgressBar.setVisibility(View.VISIBLE);
                binding.questionnaireSpinnerProgressBar.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            binding.sendRequestProgressBar.setVisibility(View.GONE);
            binding.buttonEditConfirm.setAlpha(1f);

            binding.staffSpinner.setAlpha(1f);
            binding.questionnaireSpinner.setAlpha(1f);
            binding.staffSpinnerProgressBar.setVisibility(View.GONE);
            binding.questionnaireSpinnerProgressBar.setVisibility(View.GONE);

            if (error)
                binding.requestError.setVisibility(View.VISIBLE);
        }

        binding.buttonEditConfirm.setClickable(!loading);
        binding.staffSpinner.setClickable(!loading);
        binding.questionnaireSpinner.setClickable(!loading);
        binding.waitingListInfo.setFocusableInTouchMode(!loading);
    }
}