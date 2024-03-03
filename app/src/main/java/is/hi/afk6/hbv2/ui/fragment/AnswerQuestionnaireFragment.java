package is.hi.afk6.hbv2.ui.fragment;

import static is.hi.afk6.hbv2.ui.UserHomepageActivity.EDITED_USER;
import static is.hi.afk6.hbv2.ui.UserHomepageActivity.LOGGED_IN_USER;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentAnswerQuestionnaireBinding;
import is.hi.afk6.hbv2.databinding.FragmentEditUserBinding;
import is.hi.afk6.hbv2.databinding.FragmentUserBinding;
import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.ErrorResponse;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionService;
import is.hi.afk6.hbv2.services.QuestionnaireService;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.QuestionServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnswerQuestionnaireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswerQuestionnaireFragment extends Fragment {
    private List<RadioButton> radioButtons;
    int[] listi = new int[4];
    private RadioButton selectedRadioButton;
    private RadioButton radioButtonOption1;
    private RadioButton radioButtonOption2;
    private RadioButton radioButtonOption3;
    private RadioButton radioButtonOption4;
    private RadioButton radioButtonOption5;
    private RadioGroup radioGroup;
    private Questionnaire questionnaire;

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private FragmentAnswerQuestionnaireBinding binding;
    private QuestionnaireService questionnaireService;
    private QuestionService questionService;


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

        questionnaireService = new QuestionnaireServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());
        questionService = new QuestionServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
        is.hi.afk6.hbv2.databinding.FragmentAnswerQuestionnaireBinding binding = FragmentAnswerQuestionnaireBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        View view = binding.getRoot();
        //radioGroup = binding.radioGroup;
        // Set a listener to handle radio button selections

        radioButtonOption1 = binding.radioButtonOption1;
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
                listi[currentQuestionIndex-1] = radioButtonNumber;
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
            }

        });

        questionnaireService.getQuestionnaireByID(1L, result -> {
            // Handle the result here, for example:
            questionnaire = result.getData();
            if (questionnaire != null) {
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
                });
            } else {
                // Handle the error, for example:
                ErrorResponse errorResponse = result.getErrorResponse();
                // Example: showErrorMessage(errorResponse);
            }
        });


        return view;
    }
    private void takkar(int numberOfAnswers){

    }
}