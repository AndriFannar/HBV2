package is.hi.afk6.hbv2.ui.fragment.questionnaire;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.databinding.FragmentAnswerQuestionnaireBinding;
import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.QuestionAnswerGroup;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;

/**
 * Fragment to answer a Questionnaire assigned to a User's WaitingListRequest.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @author Friðrik Þór Ólafsson, fto2@hi.is
 * @since 02/03/2024
 * @version 2.0
 */
public class AnswerQuestionnaireFragment extends Fragment
{
    private User loggedInUser;
    private WaitingListRequest waitingListRequest;
    private List<Question> questions;
    private int currentQuestionIndex;
    Question currentQuestion;
    private HashMap<Long, Integer> answers;
    private FragmentAnswerQuestionnaireBinding binding;
    private WaitingListService waitingListService;
    private RadioGroup answerGroup;
    private double score = 0;

    @Override
    public  void onCreate(@Nullable Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);

        // Get the arguments from the bundle
        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
            waitingListRequest = getArguments().getParcelable(getString(R.string.waiting_list_request));
        }

        APIService apiService = new APIServiceImplementation();

        answers = new HashMap<>();
        currentQuestionIndex = 0;

        waitingListService = new WaitingListServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
        binding = FragmentAnswerQuestionnaireBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Get the container for the answers.
        answerGroup = binding.contentAnswerQuestionnaire.questionAnswersContainer;
        //answerGroup.setOnCheckedChangeListener(this);

        questions = waitingListRequest.getQuestionnaire().getQuestions();
        setUpQuestion();

        binding.contentAnswerQuestionnaire.buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                questionAnswered();
            }
        });

        return view;
    }

    /**
     * Sets up the next question.
     */
    private void setUpQuestion()
    {
        // Remove all previous options from the screen.
        answerGroup.removeAllViews();

        currentQuestion = questions.get(currentQuestionIndex);

        binding.contentAnswerQuestionnaire.questionText.setText(currentQuestion.getQuestionString());

        RadioButton answer;

        List<String> questionAnswers = currentQuestion.getQuestionAnswerGroup().getQuestionAnswers();

        // Generate a RadioButton for each answer and add to the view.
        for (String answerString : questionAnswers)
        {
            answer = new RadioButton(requireActivity());
            answer.setId(questionAnswers.indexOf(answerString));
            answer.setText(answerString);
            answerGroup.addView(answer);
        }
    }

    /**
     * When a question is answered, add the answer to the list of answers.
     * Move to the next question if there is one, or navigate back to the WaitingListRequestFragment.
     */
    private void questionAnswered()
    {
        int selectedAnswer = answerGroup.getCheckedRadioButtonId();

        if (selectedAnswer == -1)
            return;

        // Check what answer is correct, and add it to the list of answers.
        answers.put(currentQuestion.getId(), selectedAnswer);
        score += selectedAnswer * currentQuestion.getWeight();
        currentQuestionIndex++;

        answerGroup.clearCheck();

        // Check if all questions have been answered
        if (currentQuestionIndex >= questions.size())
        {
            allQuestionsAnswered();
        }
        else
        {
            setUpQuestion();
        }
    }

    /**
     * When all questions have been answered, update the WaitingListRequest and navigate back.
     */
    private void allQuestionsAnswered()
    {
        waitingListRequest.setQuestionnaireAnswers(answers);

        waitingListRequest.setGrade(score);

        // Send updated WaitingListRequest to API, and navigate back.
        waitingListService.updateWaitingListRequestByID(waitingListRequest, new APICallback<WaitingListRequest>() {
            @Override
            public void onComplete(ResponseWrapper<WaitingListRequest> result)
            {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);

                Bundle bundle = new Bundle();
                bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
                bundle.putParcelable(getString(R.string.waiting_list_request), waitingListRequest);

                requireActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        navController.navigate(R.id.nav_waiting_list_request, bundle);
                    }
                });
            }
        });
    }
}