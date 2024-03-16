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
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;

public class ViewQuesionnaireAnswersFragment extends Fragment {
    private FragmentViewQuesionnaireAnswersBinding binding;
    private User selectedUser;
    private WaitingListRequest waitingListRequest;
    private List<Integer> answers;
    private WaitingListService waitingListService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedUser = getArguments().getParcelable(getString(R.string.view_questionnaire_answers));
        }
        APIService apiService = new APIServiceImplementation();

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

    /**
     * Retrieves a waiting list request by its ID from the waitingListService and updates the UI
     * with the details of the request, including questionnaire answers.
     *
     * @param requestId The ID of the waiting list request to retrieve.
     */
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

    /**
     * Creates a TextView for displaying a questionnaire question.
     *
     * @param questionNr The number of the question.
     * @return A TextView configured to display the question number.
     */
    private TextView createQuestionTextView(int questionNr) {
        TextView question = new TextView(requireContext());
        question.setText(String.format("Spurning %s: ", questionNr));

        int marginInPixels = getResources().getDimensionPixelSize(R.dimen.display_questionnaire_answers);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, marginInPixels, 0, marginInPixels);
        params.weight = 1.0f;
        question.setLayoutParams(params);

        return question;
    }

    /**
     * Creates a TextView for displaying a questionnaire answer.
     *
     * @param answer The answer value to display.
     * @return A TextView configured to display the provided answer.
     */
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
        params.weight = 1.0f;
        questionAnswer.setLayoutParams(params);
        return questionAnswer;
    }

    /**
     * Updates the UI with details from the provided WaitingListRequest.
     *
     * @param waitingListRequest The WaitingListRequest containing the data to update the UI.
     */
    private void updateUIWithWaitingListRequest(WaitingListRequest waitingListRequest) {
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

    /**
     * Creates a LinearLayout container for displaying a single answer.
     *
     * @return A LinearLayout configured to contain a single answer.
     */
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

