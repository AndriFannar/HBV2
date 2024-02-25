package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import static is.hi.afk6.hbv2.ui.UserHomepageActivity.LOGGED_IN_USER;

import java.util.ArrayList;
import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.databinding.FragmentCreateWaitingListRequestBinding;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionnaireService;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateWaitingListRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
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

        userService = new UserServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());
        waitingListService = new WaitingListServiceImplementation();
        questionnaireService = new QuestionnaireServiceImplementation();

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
                        setUpView(staff, result.getData());
                    }
                });
            }
        });

        return view;
    }

    private void setUpView(List<User> staff, List<Questionnaire> displayQuestionnaires)
    {
        List<String> test = new ArrayList<>();
        test.add("test");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, test);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.staffSpinner.setAdapter(adapter);
    }
}