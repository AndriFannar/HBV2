package is.hi.afk6.hbv2.ui.fragment;

import static is.hi.afk6.hbv2.ui.UserHomepageActivity.EDITED_USER;
import static is.hi.afk6.hbv2.ui.UserHomepageActivity.LOGGED_IN_USER;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentEditUserBinding;
import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.ErrorResponse;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionService;
import is.hi.afk6.hbv2.services.QuestionnaireService;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnswerQuestionnaireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerQuestionnaireFragment extends Fragment {

    private FragmentEditUserBinding binding;
    private QuestionnaireService questionnaireService;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AnswerQuestionnaireFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnswerQuestionnaireFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnswerQuestionnaireFragment newInstance(String param1, String param2) {
        AnswerQuestionnaireFragment fragment = new AnswerQuestionnaireFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public  void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        /*if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(LOGGED_IN_USER);
            editedUser = getArguments().getParcelable(EDITED_USER);
        }*/

        questionnaireService = new QuestionnaireServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_answer_questionnaire, container, false);

        questionnaireService.getQuestionnaireByID(Long.valueOf(1), result -> {
            // Handle the result here, for example:
            Questionnaire questionnaire = result.getData();
            if (questionnaire != null) {
                Question question = new QuestionService();
                // getQuestionIDs, senda þetta á QuestionService. Fer inní fall sem ég var að setja inn
                // QuestionService, getAllQuestionsfromList, það er fall inní QuestionService sem ég þarf að búa til.

                // getAllInList,
                // Now you can use the questionnaire object to update your UI or perform other actions.
                // Example: updateUIWithQuestionnaire(questionnaire);
            } else {
                // Handle the error, for example:
                ErrorResponse errorResponse = result.getErrorResponse();
                // Example: showErrorMessage(errorResponse);
            }
        });

        return view;
    }
}