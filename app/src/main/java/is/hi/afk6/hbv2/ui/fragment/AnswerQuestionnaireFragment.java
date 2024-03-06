package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
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
import is.hi.afk6.hbv2.services.QuestionnaireService;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.QuestionServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;
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

        answerGroup = binding.questionAnswersContainer;

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

        binding.buttonNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                questionAnswered();
            }
        });

        /*radioButtonOption1 = binding.radioButtonOption1;
        radioButtonOption2 = binding.radioButtonOption2;
        radioButtonOption3 = binding.radioButtonOption3;
        radioButtonOption4 = binding.radioButtonOption4;
        radioButtonOption5 = binding.radioButtonOption5;

        radioButtons = new ArrayList<>();
        radioButtons.add(radioButtonOption1);
        radioButtons.add(radioButtonOption2);
        radioButtons.add(radioButtonOption3);
        radioButtons.add(radioButtonOption4);
        radioButtons.add(radioButtonOption5);


        radioButtonOption1.setTag(1);
        radioButtonOption2.setTag(2);
        radioButtonOption3.setTag(3);
        radioButtonOption4.setTag(4);
        radioButtonOption5.setTag(5);
        // Set a listener to handle radio button selections
        radioButtonOption1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Option 1 is selected
                selectedRadioButton = radioButtonOption1;
            }
        });

        radioButtonOption2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Option 2 is selected
                selectedRadioButton = radioButtonOption2;
            }
        });
        radioButtonOption3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Option 3 is selected
                selectedRadioButton = radioButtonOption3;
            }
        });
        radioButtonOption4.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Option 4 is selected
                selectedRadioButton = radioButtonOption4;
            }
        });
        radioButtonOption5.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Option 5 is selected
                selectedRadioButton = radioButtonOption5;
            }
        });

        binding.goHomeButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
        binding.nextQuestionButton.setOnClickListener(v -> {
            if(currentQuestionIndex < questions.size()) {
                if(selectedRadioButton != null){
                int radioButtonNumber = (int) selectedRadioButton.getTag();
                listi.add(radioButtonNumber);
                if (currentQuestionIndex < questions.size()) {
                    Question nextQuestion = questions.get(currentQuestionIndex);
                    if (nextQuestion != null) {
                        currentQuestionIndex++;
                        String questionText = nextQuestion.getQuestionString();
                        binding.spurning.setText(questionText);
                        for (RadioButton radioButton : radioButtons) {
                            radioButton.setChecked(false);
                        }
                    } else {
                        // Handle the error, for example:
                        binding.spurning.setText("Spurningarnar eru búnar.");
                        // Example: showErrorMessage(errorResponse);
                    }
                } else {
                    // Handle the error, for example:
                    binding.spurning.setText("Spurningarnar eru búnar.");
                    // Example: showErrorMessage(errorResponse);
                }
                }
            }
            else {
                radioButtonOption1.setVisibility(View.INVISIBLE);
                radioButtonOption2.setVisibility(View.INVISIBLE);
                radioButtonOption3.setVisibility(View.INVISIBLE);
                radioButtonOption4.setVisibility(View.INVISIBLE);
                radioButtonOption5.setVisibility(View.INVISIBLE);
                binding.spurning.setText("Takk fyrir að svara");

                waitingListRequest.setQuestionnaireAnswers(listi);

                waitingListService.updateWaitingListRequestByID(waitingListRequest, result ->
                {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            Bundle bundle = new Bundle();

                            bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
                            bundle.putParcelable(getString(R.string.waiting_list_request), waitingListRequest);
                            bundle.putParcelable(getString(R.string.questionnaire), questionnaire);

                            NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);
                            navController.navigate(R.id.nav_waiting_list_request, bundle);
                        }
                    });
                });
            }

        });

        questionService.getAllQuestionsFromList(questionnaire.getQuestionIDs(), result1 -> {
            questions = result1.getData();
            if (questions != null && !questions.isEmpty()) {
                // Assuming you want to display the first question
                Question firstQuestion = questions.get(0);
                String questionText = firstQuestion.getQuestionString();

                // Set the text using data binding
                binding.spurning.setText(questionText);
                takkar(firstQuestion.getNumberOfAnswers());
                currentQuestionIndex++;
            }
        });*/


        return view;
    }
    private void setUpQuestion()
    {
        answerGroup.removeAllViews();

        currentQuestion = questions.get(currentQuestionIndex);
        Log.d("Question", "Setting up question " + currentQuestionIndex + " with " + currentQuestion.getNumberOfAnswers() + " answers");

        binding.questionText.setText(currentQuestion.getQuestionString());

        RadioButton answer;

        for (int i = 0; i < currentQuestion.getNumberOfAnswers(); i++)
        {
            Log.d("Answer", "Setting up answer " + i);
            answer = new RadioButton(requireActivity());
            answer.setId(i);
            answer.setText(String.valueOf(i));
            answerGroup.addView(answer);
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
                break;
            }
        }

        Log.d("Question", "Answered question " + currentQuestionIndex + " of " + questions.size() + " questions");
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
        Log.d("Question", "All questions answered");
        waitingListRequest.setQuestionnaireAnswers(answers);

        waitingListService.updateWaitingListRequestByID(waitingListRequest, new APICallback<WaitingListRequest>() {
            @Override
            public void onComplete(ResponseWrapper<WaitingListRequest> result)
            {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);

                Bundle bundle = new Bundle();
                bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
                bundle.putParcelable(getString(R.string.waiting_list_request), waitingListRequest);
                bundle.putParcelable(getString(R.string.questionnaire), questionnaire);

                navController.navigate(R.id.nav_waiting_list_request, bundle);
            }
        });
    }
}