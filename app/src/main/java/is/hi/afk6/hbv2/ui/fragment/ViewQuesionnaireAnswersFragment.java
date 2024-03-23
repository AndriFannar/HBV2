package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentViewQuesionnaireAnswersBinding;
import java.util.List;
import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.services.QuestionService;
import is.hi.afk6.hbv2.services.QuestionnaireService;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.QuestionServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;

public class ViewQuesionnaireAnswersFragment extends Fragment {
    private FragmentViewQuesionnaireAnswersBinding binding;
    private User selectedUser;
    private WaitingListRequest waitingListRequest;
    private List<Integer> answers;
    private WaitingListService waitingListService;
    private QuestionnaireService questionnaireService;
    private QuestionService questionService;
    private Questionnaire questionnaire;
    private List<Question> questions;

    /**
     * Called when the fragment is starting. This method is invoked during the creation of the fragment.
     * It initializes necessary services and data structures.
     *
     * @param savedInstanceState If the fragment is being re-initialized after previously being shut down then
     *                           this Bundle contains the data it most recently supplied in {@link #onSaveInstanceState(Bundle)}.
     *                           Note: Otherwise it is null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedUser = getArguments().getParcelable(getString(R.string.view_questionnaire_answers));
        }
        APIService apiService = new APIServiceImplementation();

        questionnaireService = new QuestionnaireServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        questionService = new QuestionServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        waitingListService = new WaitingListServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        questions = new ArrayList<>();
        answers = new ArrayList<>();
    }

    /**
     * Called to have the fragment instantiate its user interface view. This method is called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment,
     *                           typically obtained from {@link android.app.Activity#getLayoutInflater()}.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself, but this can be used to generate
     *                           the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     *                           as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        waitingListService.getWaitingListRequestByID(requestId, result -> requireActivity().runOnUiThread(() -> {
            if (result.getData() != null) {
                waitingListRequest = result.getData();
                updateUIWithWaitingListRequest(waitingListRequest);
                answers = waitingListRequest.getQuestionnaireAnswers();
                questionnaireService.getQuestionnaireByID(waitingListRequest.getQuestionnaireID(), result1 -> {
                    questionnaire = result1.getData();

                    if (questionnaire != null) {
                        questionService.getAllQuestionsFromList(questionnaire.getQuestionIDs(), result2 -> {
                            questions = result2.getData();

                            if (questions != null && answers.size() == questions.size()) {
                                requireActivity().runOnUiThread(() -> {

                                    for (int i = 0; i < questions.size(); i++) {
                                        LinearLayout answersContainer = createAnswerContainer();
                                        TextView question = createQuestionTextView(questions.get(i));
                                        TextView answer = createAnswerTextView(answers.get(i));
                                        answersContainer.addView(question);
                                        answersContainer.addView(answer);
                                        binding.answersContainer.addView(answersContainer);
                                    }
                                });
                            }
                        });
                    }
                });
                controlView(false, "");
            }
            else {
                String error = result.getErrorResponse().getErrorDetails().get("answer");
                controlView(false, error);
            }
        }));
    }

    /**
     * Creates a TextView for displaying a questionnaire question.
     *
     * @param question The number of the question.
     * @return A TextView configured to display the question number.
     */
    private TextView createQuestionTextView(Question question) {
        TextView questionView = new TextView(requireContext());
        questionView.setText(String.format(question.getQuestionString()));

        int marginInPixels = getResources().getDimensionPixelSize(R.dimen.display_questionnaire_answers);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, marginInPixels, 0, marginInPixels);
        params.weight = 1.0f;
        questionView.setLayoutParams(params);

        return questionView;
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
     * Updates the UI elements with the information from the provided WaitingListRequest object.
     * If the provided Binding object is not null, this method sets the name of the selected user,
     * the user's grade, and the description text based on the information in the WaitingListRequest object.
     *
     * @param waitingListRequest The WaitingListRequest object containing the information to be displayed.
     */
    private void updateUIWithWaitingListRequest(WaitingListRequest waitingListRequest) {
        if (binding != null) {
            float grade = (float) waitingListRequest.getGrade();
            binding.selectedUserName.setText(selectedUser.getName());
            binding.selectedUserGrade.setText(String.format(" - %s stig", (int) grade));
            binding.descriptionText.setText(waitingListRequest.getDescription());
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

