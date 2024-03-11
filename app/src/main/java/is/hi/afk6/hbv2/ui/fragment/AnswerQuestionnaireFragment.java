package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.databinding.FragmentAnswerQuestionnaireBinding;
import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionService;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.QuestionServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;

public class AnswerQuestionnaireFragment extends Fragment
{
    private User loggedInUser;
    private Questionnaire questionnaire;
    private WaitingListRequest waitingListRequest;
    private List<Question> questions;
    private int currentQuestionIndex;
    Question currentQuestion;
    private List<Integer> answers;
    private FragmentAnswerQuestionnaireBinding binding;
    private QuestionService questionService;
    private WaitingListService waitingListService;
    private RadioGroup answerGroup;

    @Override
    public  void onCreate(@Nullable Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);

        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
            waitingListRequest = getArguments().getParcelable(getString(R.string.waiting_list_request));
            questionnaire = getArguments().getParcelable(getString(R.string.questionnaire));
        }

        APIService apiService = new APIServiceImplementation();

        answers = new ArrayList<>();
        currentQuestionIndex = 0;

        questionService = new QuestionServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        waitingListService = new WaitingListServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
        binding = FragmentAnswerQuestionnaireBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        answerGroup = binding.contentAnswerQuestionnaire.questionAnswersContainer;

        questionService.getAllQuestionsFromList(questionnaire.getQuestionIDs(), new APICallback<List<Question>>() {
            @Override
            public void onComplete(ResponseWrapper<List<Question>> result)
            {
                if (result.getData() != null)
                {
                    questions = result.getData();

                    requireActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            setUpQuestion();
                        }
                    });
                }
            }
        });

        binding.contentAnswerQuestionnaire.buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                questionAnswered();
            }
        });

        return view;
    }
    private void setUpQuestion()
    {

        answerGroup.removeAllViews();

        currentQuestion = questions.get(currentQuestionIndex);
        Log.d("Question", "Setting up question " + currentQuestionIndex + ", " + currentQuestion.getQuestionString() + " with " + currentQuestion.getNumberOfAnswers() + " answers");

        binding.contentAnswerQuestionnaire.questionText.setText(currentQuestion.getQuestionString());

        RadioButton answer;

        for (int i = 0; i < currentQuestion.getNumberOfAnswers(); i++)
        {
            Log.d("Answer", "Setting up answer " + i);
            answer = new RadioButton(requireActivity());
            answer.setId(i);
            answer.setText(String.valueOf(i));
            answerGroup.addView(answer);
            Log.d("Answer", "Answer " + i + " set up");
        }
    }

    private void questionAnswered()
    {
        RadioButton answer;

        for (int i = 0; i < currentQuestion.getNumberOfAnswers(); i++)
        {
            answer = answerGroup.findViewById(i);

            if (answer.isChecked())
            {
                Log.d("Answer", "Answer " + i + " is checked");
                answers.add(i);
                currentQuestionIndex++;

                Log.d("Answer", "Answer " + i + " added to answers, which now has answers " + answers.toString());

                break;
            }
        }

        Log.d("Question", "Answered number " + currentQuestionIndex + " of " + questions.size() + " questions");
        if (currentQuestionIndex >= questions.size())
        {
            allQuestionsAnswered();
        }
        else
        {
            setUpQuestion();
        }
    }

    private void allQuestionsAnswered()
    {
        waitingListRequest.setQuestionnaireAnswers(answers);

        calculateGrade();

        waitingListService.updateWaitingListRequestByID(waitingListRequest, new APICallback<WaitingListRequest>() {
            @Override
            public void onComplete(ResponseWrapper<WaitingListRequest> result)
            {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);

                Bundle bundle = new Bundle();
                bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
                bundle.putParcelable(getString(R.string.waiting_list_request), waitingListRequest);
                bundle.putParcelable(getString(R.string.questionnaire), questionnaire);

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

    private void calculateGrade() {
        double score = 0;

        for (int i = 0; i < answers.size(); i++)
        {
            score += answers.get(i) * questions.get(i).getWeight();
        }

        waitingListRequest.setGrade(score);
    }
}