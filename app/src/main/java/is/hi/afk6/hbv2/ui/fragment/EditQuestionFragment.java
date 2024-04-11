package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionAnswerGroupService;
import is.hi.afk6.hbv2.services.QuestionService;
import is.hi.afk6.hbv2.services.implementation.QuestionAnswerGroupServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.QuestionServiceImplementation;

/**
 * A Fragment subclass for editing a Question.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 06/04/2024
 * @version 1.0
 */
public class EditQuestionFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FragmentEditQuestionBinding binding;
    private Question question;
    private QuestionService questionService;
    private QuestionAnswerGroupService questionAnswerGroupService;
    private boolean newQuestion = false;
    private List<QuestionAnswerGroup> questionAnswerGroups;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            question = getArguments().getParcelable(getString(R.string.question));
        }

        if (question == null)
        {
            question = new Question();
            newQuestion = true;
        }

        APIService apiService = new APIServiceImplementation();
        questionService = new QuestionServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        questionAnswerGroupService = new QuestionAnswerGroupServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditQuestionBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        controlView(true, false, null);

        binding.buttonEditConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveQuestion();
            }
        });
        binding.buttonDeleteQuestion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deleteQuestion();
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

    /**
     * Sets up the view elements with data from an existing Question if editing, or a new Question if creating.
     */
    private void setUpView()
    {
        List<String> questionAnswerGroupStrings = new ArrayList<>();

        if (!newQuestion)
        {
            binding.questionText.setText(question.getQuestionString());
            binding.questionWeight.setText(String.valueOf(question.getWeight()));
            binding.editQuestionHeaderText.setText(R.string.edit_question_header_text);
            binding.buttonDeleteQuestion.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.editQuestionHeaderText.setText(R.string.new_question_text);
            binding.buttonDeleteQuestion.setVisibility(View.GONE);
        }

        QuestionAnswerGroup currentQuestionAnswerGroup = question.getQuestionAnswerGroup();

        // Insert QuestionAnswerGroups into the spinner if they exist.
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

        // Add an option to create a new QuestionAnswerGroup.
        questionAnswerGroupStrings.add(getString(R.string.new_question_answer_group));

        binding.questionAnswerGroupSpinner.setOnItemSelectedListener(this);

        controlView(false, false, null);
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
            binding.buttonDeleteQuestion.setAlpha(0.7f);
            binding.buttonEditQuestionAnswerGroup.setAlpha(0.7f);

            if (saving)
                binding.sendRequestProgressBar.setVisibility(View.VISIBLE);
            else
            {
                binding.questionAnswerGroupSpinner.setAlpha(0.7f);
                binding.questionAnswerGroupSpinnerProgressBar.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            binding.sendRequestProgressBar.setVisibility(View.GONE);
            binding.buttonEditConfirm.setAlpha(1f);
            binding.buttonDeleteQuestion.setAlpha(1f);
            binding.buttonEditQuestionAnswerGroup.setAlpha(1f);

            binding.questionAnswerGroupSpinner.setAlpha(1f);
            binding.questionAnswerGroupSpinnerProgressBar.setVisibility(View.GONE);

            if (errorResID != null)
                createSnackbar(errorResID, Snackbar.LENGTH_LONG).show();
        }

        binding.buttonEditConfirm.setClickable(!loading);
        binding.buttonDeleteQuestion.setClickable(!loading);
        binding.questionAnswerGroupSpinner.setClickable(!loading);
        binding.buttonEditQuestionAnswerGroup.setClickable(!loading);
        binding.questionText.setFocusableInTouchMode(!loading);
        binding.questionWeight.setFocusableInTouchMode(!loading);
    }

    /**
     * Saves a new Question or updates an existing one.
     */
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
        else if (binding.questionAnswerGroupSpinner.getSelectedItemPosition() == questionAnswerGroups.size())
        {
            createSnackbar(R.string.question_no_answer_group_error, Snackbar.LENGTH_LONG).show();
            return;
        }

        controlView(false, true, null);

        question.setQuestionString(binding.questionText.getText().toString());
        question.setWeight(Double.parseDouble(String.valueOf(binding.questionWeight.getText())));

        QuestionAnswerGroup qAG = questionAnswerGroups.get(binding.questionAnswerGroupSpinner.getSelectedItemPosition());
        question.getQuestionAnswerGroup().removeQuestionID(question.getId());
        question.setQuestionAnswerGroup(qAG);
        qAG.addQuestionID(question.getId());

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
                                question = result.getData();
                                controlView(false, false, null);
                                createSnackbar(R.string.save_snackbar_text, Snackbar.LENGTH_SHORT).show();
                                newQuestion = false;
                                setUpView();
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
                                controlView(false, false, null);
                                createSnackbar(R.string.save_snackbar_text, Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }

    /**
     * Delete a Question with an option to restore.
     * If the Question is associated with a Questionnaire, it cannot be deleted.
     * The Question will not be deleted from the API until the Snackbar is dismissed.
     * After deletion, navigate back.
     */
    private void deleteQuestion()
    {
        if (!question.getQuestionnaireIDs().isEmpty())
        {
            createSnackbar(R.string.question_delete_error, Snackbar.LENGTH_LONG).show();
        }
        else
        {
            Question deletedQuestion = question;
            question = new Question();

            Snackbar deleteSnackbar = createActionSnackbar(R.string.question_deleted_snackbar_text, Snackbar.LENGTH_LONG, R.string.snackbar_undo, new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    question = deletedQuestion;
                    createSnackbar(R.string.question_restored_snackbar_text, Snackbar.LENGTH_SHORT).show();
                }
            });

            deleteSnackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);

                    if (question.getId() == null)
                    {
                        questionService.deleteQuestionByID(deletedQuestion.getId(), new APICallback<Question>()
                        {
                            @Override
                            public void onComplete(ResponseWrapper<Question> result)
                            {
                                requireActivity().runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        controlView(false, false, null);
                                        getActivity().getOnBackPressedDispatcher().onBackPressed();
                                    }
                                });
                            }
                        });
                    }
                }
            });

            deleteSnackbar.show();
        }
    }

    /**
     * Create a new Snackbar with a string resource ID and length.
     *
     * @param stringResID The resource ID for the String to display on the Snackbar.
     * @param length      The length of time to display the Snackbar.
     * @return            The created Snackbar.
     */
    private Snackbar createSnackbar(int stringResID, int length)
    {
        return Snackbar.make(binding.getRoot(), stringResID, length);
    }

    /**
     * Navigate to a new Fragment.
     *
     * @param bundle      The Bundle to pass to the new Fragment.
     * @param destination The destination ID of the new Fragment.
     */
    private void navigate(Bundle bundle, int destination)
    {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);
        navController.navigate(destination, bundle);
    }

    /**
     * Create a new Snackbar with an action.
     *
     * @param stringResID The resource ID for the String to display on the Snackbar.
     * @param length      The length of time to display the Snackbar.
     * @param actionResID The resource ID for the String to display on the action button.
     * @param listener    The listener for the action button.
     * @return            The created Snackbar.
     */
    private Snackbar createActionSnackbar(int stringResID, int length, int actionResID, View.OnClickListener listener)
    {
        Snackbar snackbar = createSnackbar(stringResID, length);
        snackbar.setAction(actionResID, listener);

        return snackbar;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        // Change the behavior of the button next to the spinner based on the selected item.
        if (position == questionAnswerGroups.size())
        {
            binding.buttonEditQuestionAnswerGroup.setText(R.string.new_question_answer_group);

            binding.buttonEditQuestionAnswerGroup.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    navigate(null, R.id.nav_edit_question_answer_group);
                }
            });
        }
        else
        {
            binding.buttonEditQuestionAnswerGroup.setText(R.string.edit_question_answer_group);

            binding.buttonEditQuestionAnswerGroup.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(getString(R.string.question_answer_group), questionAnswerGroups.get(position));
                    navigate(bundle, R.id.nav_edit_question_answer_group);
                }
            });
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}