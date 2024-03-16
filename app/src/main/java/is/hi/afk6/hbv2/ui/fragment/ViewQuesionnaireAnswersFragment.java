package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentViewQuesionnaireAnswersBinding;
import java.util.List;
import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionService;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.services.QuestionnaireService;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.QuestionServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;

public class ViewQuesionnaireAnswersFragment extends Fragment {
    private FragmentViewQuesionnaireAnswersBinding binding;
    private User selectedUser;
    private Questionnaire questionnaire;
    private WaitingListRequest waitingListRequest;
    private List<Question> questions;
    private List<Integer> answers;
    private UserService userService;
    private QuestionnaireService questionnaireService;
    private WaitingListService waitingListService;
    private QuestionService questionService;
    private static final String TAG = "ViewQuesionnaireAnswersFragment";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedUser = getArguments().getParcelable(getString(R.string.view_questionnaire_answers));
        }

        APIService apiService = new APIServiceImplementation();

        userService = new UserServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        questionnaireService = new QuestionnaireServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        questionService = new QuestionServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        waitingListService = new WaitingListServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());

        answers = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentViewQuesionnaireAnswersBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        if(selectedUser != null && selectedUser.getWaitingListRequestID() != null) {
            getRequest(selectedUser.getWaitingListRequestID());
        }
        return view;
    }

    private void getRequest(Long requestId) {
        controlView(true, "");
        waitingListService.getWaitingListRequestByID(requestId, new APICallback<WaitingListRequest>() {
            @Override
            public void onComplete(ResponseWrapper<WaitingListRequest> result) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.getData() != null) {
                            waitingListRequest = result.getData();
                            updateUIWithWaitingListRequest(waitingListRequest);
                            answers = waitingListRequest.getQuestionnaireAnswers();
                            int questionNr = 1;
                            for (Integer answer : answers) {
                                LinearLayout answerContainer = createAnswerContainer();
                                TextView question = createQuestionTextView(questionNr);
                                questionNr++;
                                TextView questionAnswer = createAnswerTextView(answer);
                                answerContainer.addView(question);
                                answerContainer.addView(questionAnswer);
                                binding.answersContainer.addView(answerContainer);
                            }
                            controlView(false, "");
                        }
                     else {
                        String error = result.getErrorResponse().getErrorDetails().get("answer");
                        controlView(false, error);
                        }
                    }
                });
            }
        });
        }

    private TextView createQuestionTextView(int questionNr) {
        TextView questionAnswer = new TextView(requireContext());
        questionAnswer.setText(String.format("Spurning %s: ", questionNr));

        // Set margins (in pixels) for top and bottom
        int marginInPixels = getResources().getDimensionPixelSize(R.dimen.display_questionnaire_answers);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, marginInPixels, 0, marginInPixels);

        // Apply weight to the TextView
        params.weight = 1.0f;

        // Set the params to your TextView
        questionAnswer.setLayoutParams(params);
        return questionAnswer;
    }

    private TextView createAnswerTextView(int answer){
        TextView questionAnswer = new TextView(requireContext());
        questionAnswer.setText(String.valueOf(answer));

        // Set margins (in pixels) for top and bottom
        int marginInPixels = getResources().getDimensionPixelSize(R.dimen.display_questionnaire_answers);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, marginInPixels, 0, marginInPixels);

        // Apply weight to the TextView
        params.weight = 1.0f;
        questionAnswer.setLayoutParams(params);
        return questionAnswer;
    }


    private void updateUIWithWaitingListRequest(WaitingListRequest waitingListRequest) {
        // Update UI elements with waitingListRequest data
        if (binding != null) {
            float grade = (float) waitingListRequest.getGrade();
            binding.selectedUserName.setText(selectedUser.getName());
            binding.selectedUserGrade.setText(String.valueOf(grade));
        }
    }

    /**
     * Controls many parts of the view of the login Activity.
     *
     * @param loading Is there data being actively fetched?
     * @param error   Error to display on UI, if any.
     */
    private void controlView(boolean loading, String error)
    {
        if (loading)
        {
            binding.usersError.setVisibility(View.INVISIBLE);
            binding.usersProgressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.usersProgressBar.setVisibility(View.GONE);

            if (!error.isEmpty())
            {
                binding.usersError.setText(error);
                binding.usersError.setVisibility(View.VISIBLE);
            }
        }
    }


    private LinearLayout createAnswerContainer(){
        LinearLayout answerContainer = new LinearLayout(requireContext());
        answerContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        answerContainer.setLayoutParams(layoutParams);
        return answerContainer;
    }
}

