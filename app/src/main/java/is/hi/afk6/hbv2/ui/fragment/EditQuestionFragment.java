package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.databinding.FragmentEditQuestionBinding;
import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.QuestionAnswerGroup;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionAnswerGroupService;
import is.hi.afk6.hbv2.services.QuestionService;
import is.hi.afk6.hbv2.services.implementation.QuestionAnswerGroupServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.QuestionServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;

/**
 * A Fragment subclass for editing a Question.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 06/04/2024
 * @version 1.0
 */
public class EditQuestionFragment extends Fragment
{
    private FragmentEditQuestionBinding binding;
    private Question question;
    private QuestionService questionService;
    private QuestionAnswerGroupService questionAnswerGroupService;
    private boolean newQuestion;
    private List<QuestionAnswerGroup> questionAnswerGroups;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            question = getArguments().getParcelable(getString(R.string.question));
        }

        newQuestion = question == null;

        if (newQuestion) question = new Question();

        APIService apiService = new APIServiceImplementation();
        questionService = new QuestionServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        questionAnswerGroupService = new QuestionAnswerGroupServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditQuestionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.buttonEditConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveQuestion();
            }
        });

        questionAnswerGroupService.getAllQuestionAnswerGroup(new APICallback<List<QuestionAnswerGroup>>() {
            @Override
            public void onComplete(ResponseWrapper<List<QuestionAnswerGroup>> result)
            {
                questionAnswerGroups = result.getData();
                requireActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setUpView();
                    }
                });
            }
        });

        return view;
    }

    private void setUpView()
    {
        List<String> questionAnswerGroupStrings = new ArrayList<>();

        if (!newQuestion)
        {
            binding.questionText.setText(question.getQuestionString());
            binding.questionWeight.setText(String.valueOf(question.getWeight()));
        }

        QuestionAnswerGroup currentQuestionAnswerGroup = question.getQuestionAnswerGroup();

        if (questionAnswerGroups != null)
        {
            for (QuestionAnswerGroup qAG : questionAnswerGroups)
            {
                // Add to the list.
                questionAnswerGroupStrings.add(qAG.getGroupName() + " - " + qAG.getQuestionAnswers().size());
                if ((currentQuestionAnswerGroup != null) && (Objects.equals(currentQuestionAnswerGroup.getId(), qAG.getId())))
                    currentQuestionAnswerGroup = qAG;
            }

            ArrayAdapter<String> questionAnswerGroupAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, questionAnswerGroupStrings);
            questionAnswerGroupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.questionAnswerGroupSpinner.setAdapter(questionAnswerGroupAdapter);

            binding.questionAnswerGroupSpinner.setSelection(questionAnswerGroups.indexOf(currentQuestionAnswerGroup));
        }
    }

    /**
     * Controls the view elements when loading.
     *
     * @param fetching   Is there data being actively fetched?
     * @param saving     Is there a question being saved?
     * @param errorResID Res ID of the error message.
     */
    private void controlView(boolean fetching, boolean saving, Integer errorResID)
    {
        boolean loading = fetching || saving;

        if (loading)
        {
            binding.buttonEditConfirm.setAlpha(0.7f);

            if (saving)
                binding.sendRequestProgressBar.setVisibility(View.VISIBLE);
            else
            {
                binding.questionAnswerGroupSpinner.setAlpha(0.7f);
                binding.questionAnswerGroupSpinner.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            binding.sendRequestProgressBar.setVisibility(View.GONE);
            binding.buttonEditConfirm.setAlpha(1f);

            binding.questionAnswerGroupSpinner.setAlpha(1f);
            binding.questionAnswerGroupSpinner.setVisibility(View.GONE);

            if (errorResID != null)
                createSnackbar(errorResID, Snackbar.LENGTH_LONG).show();
        }

        binding.buttonEditConfirm.setClickable(!loading);
        binding.questionAnswerGroupSpinner.setClickable(!loading);
        binding.questionText.setFocusableInTouchMode(!loading);
        binding.questionWeight.setFocusableInTouchMode(!loading);
    }

    private void saveQuestion()
    {
        if (binding.questionText.getText().toString().isEmpty())
        {
            createSnackbar(R.string.question_no_text_error, Snackbar.LENGTH_LONG).show();
            return;
        }
        else if (binding.questionWeight.getText().toString().isEmpty())
        {
            createSnackbar(R.string.question_no_weight_error, Snackbar.LENGTH_LONG).show();
            return;
        }

        question.setQuestionString(binding.questionText.getText().toString());
        question.setWeight(Double.parseDouble(String.valueOf(binding.questionWeight.getText())));
        question.setQuestionAnswerGroup(questionAnswerGroups.get(binding.questionAnswerGroupSpinner.getSelectedItemPosition()));

        if (newQuestion)
        {
            questionService.saveNewQuestion(question, new APICallback<Question>()
            {
                @Override
                public void onComplete(ResponseWrapper<Question> result)
                {
                    if (result.getData() != null)
                    {
                        requireActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                createSnackbar(R.string.save_snackbar_text, Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
        else
        {
            questionService.updateQuestion(question, new APICallback<Question>()
            {
                @Override
                public void onComplete(ResponseWrapper<Question> result)
                {
                    if (result.getData() != null)
                    {
                        requireActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                createSnackbar(R.string.save_snackbar_text, Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }

    private Snackbar createSnackbar(int stringResID, int length)
    {
        return Snackbar.make(binding.getRoot(), stringResID, length);
    }
}