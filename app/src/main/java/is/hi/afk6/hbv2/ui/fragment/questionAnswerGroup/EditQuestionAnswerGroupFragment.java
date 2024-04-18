package is.hi.afk6.hbv2.ui.fragment.questionAnswerGroup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.databinding.FragmentEditQuestionAnswerGroupBinding;
import is.hi.afk6.hbv2.databinding.FragmentEditQuestionBinding;
import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.QuestionAnswerGroup;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionAnswerGroupService;
import is.hi.afk6.hbv2.services.implementation.QuestionAnswerGroupServiceImplementation;

/**
 * A Fragment subclass for editing a QuestionAnswerGroup.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 07/04/2024
 * @version 1.0
 */
public class EditQuestionAnswerGroupFragment extends Fragment
{
    private FragmentEditQuestionAnswerGroupBinding binding;
    private QuestionAnswerGroup questionAnswerGroup;
    private boolean newQuestionAnswerGroup = false;
    private QuestionAnswerGroupService questionAnswerGroupService;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            questionAnswerGroup = getArguments().getParcelable("questionAnswerGroup");
        }

        if (questionAnswerGroup == null)
        {
            questionAnswerGroup = new QuestionAnswerGroup();
            newQuestionAnswerGroup = true;
        }

        APIService apiService = new APIServiceImplementation();
        questionAnswerGroupService = new QuestionAnswerGroupServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentEditQuestionAnswerGroupBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        controlView(true, false, null);

        binding.buttonAddMoreAnswers.setOnClickListener(v ->
        {
            addAnswer(null);
        });
        binding.buttonEditQuestionAnswerGroupConfirm.setOnClickListener(v ->
        {
            save();
        });
        binding.buttonDeleteQuestionAnswerGroup.setOnClickListener(v ->
        {
            deleteQuestionAnswerGroup();
        });
        
        setUpView();

        return view;
    }

    /**
     * Sets up the view elements with data from the QuestionAnswerGroup if editing.
     * If creating a new QuestionAnswerGroup, the view is set up for creation with empty fields.
     */
    private void setUpView()
    {
        binding.questionAnswersContainer.removeAllViews();

        if (newQuestionAnswerGroup)
        {
            binding.headerEditQuestionAnswerGroup.setText(R.string.new_question_answer_group_header);
            binding.buttonDeleteQuestionAnswerGroup.setVisibility(View.GONE);
            addAnswer(null);
        }
        else
        {
            binding.buttonDeleteQuestionAnswerGroup.setVisibility(View.VISIBLE);
            binding.headerEditQuestionAnswerGroup.setText(R.string.edit_question_answer_group_header);
            binding.questionAnswerGroupName.setText(questionAnswerGroup.getGroupName());

            for (String questionAnswerOption : questionAnswerGroup.getQuestionAnswers())
            {
                addAnswer(questionAnswerOption);
            }
        }

        controlView(false, false, null);
    }

    /**
     * Adds a field for an additional answer.
     *
     * @param answer The text for the answer field, if any.
     */
    private void addAnswer(String answer)
    {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        EditText questionAnswerOption = new EditText(getContext());
        questionAnswerOption.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

        if (answer != null)
        {
            questionAnswerOption.setText(answer);
        }
        else
        {
            questionAnswerOption.setHint(getString(R.string.question_answer_group_hint) + " " + (binding.questionAnswersContainer.getChildCount() + 1));
        }

        linearLayout.addView(questionAnswerOption);

        Button button = new Button(getContext());
        button.setText(R.string.delete_button_text);
        button.setOnClickListener(v ->
        {
            binding.questionAnswersContainer.removeView(linearLayout);
        });
        button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.addView(button);

        binding.questionAnswersContainer.addView(linearLayout);
    }

    /**
     * Save a QuestionAnswerGroup.
     */
    private void save()
    {
        controlView(false, true, null);

        if (binding.questionAnswerGroupName.getText().toString().isEmpty())
        {
            controlView(false, false, R.string.empty_question_answer_group_name_error);
            return;
        }
        else if (binding.questionAnswersContainer.getChildCount() < 2)
        {
            controlView(false, false, R.string.question_answer_group_min_answers_error);
            return;
        }

        questionAnswerGroup.setGroupName(binding.questionAnswerGroupName.getText().toString());

        if (!newQuestionAnswerGroup)
            questionAnswerGroup.getQuestionAnswers().clear();

        LinearLayout layout;
        // Insert all answers into the QuestionAnswerGroup.
        for (int i = 0; i < binding.questionAnswersContainer.getChildCount(); i++)
        {
            layout = (LinearLayout) binding.questionAnswersContainer.getChildAt(i);
            EditText questionAnswerOptionEditText = (EditText) layout.getChildAt(0);

            if (questionAnswerOptionEditText.getText().toString().isEmpty())
            {
                controlView(false, false, R.string.empty_question_answer_option_error);
                return;
            }

            questionAnswerGroup.getQuestionAnswers().add(questionAnswerOptionEditText.getText().toString());
        }

        // If adding a new QuestionAnswerGroup, save it as a new QuestionAnswerGroup and reset the view to editing an existing QuestionAnswerGroup.
        if (newQuestionAnswerGroup)
        {
            questionAnswerGroupService.saveNewQuestionAnswerGroup(questionAnswerGroup, new APICallback<QuestionAnswerGroup>()
            {
                @Override
                public void onComplete(ResponseWrapper<QuestionAnswerGroup> result)
                {
                    requireActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            controlView(true, false, null);
                            questionAnswerGroup = result.getData();
                            newQuestionAnswerGroup = false;
                            createSnackbar(R.string.save_snackbar_text, Snackbar.LENGTH_SHORT).show();
                            setUpView();
                        }
                    });
                }
            });
        }
        else // Else, update an existing QuestionAnswerGroup.
        {
            questionAnswerGroupService.updateQuestionAnswerGroup(questionAnswerGroup, new APICallback<QuestionAnswerGroup>()
            {
                @Override
                public void onComplete(ResponseWrapper<QuestionAnswerGroup> result)
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
            });
        }
    }

    /**
     * Delete a QuestionAnswerGroup with an option to restore.
     * Does not delete the QuestionAnswerGroup if it is being used by other Questions.
     * The QuestionAnswerGroup is not removed from the API until the Snackbar is dismissed.
     */
    private void deleteQuestionAnswerGroup()
    {
        // A QuestionAnswerGroup can't be deleted if it is being used by other Questions.
        if (!questionAnswerGroup.getQuestionIDs().isEmpty())
        {
            createSnackbar(R.string.question_answer_group_delete_error, Snackbar.LENGTH_LONG).show();
        }
        else
        {
            QuestionAnswerGroup deletedQuestionAnswerGroup = questionAnswerGroup;
            questionAnswerGroup = new QuestionAnswerGroup();

            Snackbar deleteSnackbar = createActionSnackbar(R.string.question_answer_group_deleted_snackbar_text, Snackbar.LENGTH_SHORT, R.string.snackbar_undo, new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    questionAnswerGroup = deletedQuestionAnswerGroup;
                    createSnackbar(R.string.question_answer_group_restored_snackbar_text, Snackbar.LENGTH_SHORT).show();
                }
            });

            deleteSnackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);

                    if (questionAnswerGroup.getId() == null)
                    {
                        questionAnswerGroupService.deleteQuestionAnswerGroup(deletedQuestionAnswerGroup.getId(), new APICallback<QuestionAnswerGroup>()
                        {
                            @Override
                            public void onComplete(ResponseWrapper<QuestionAnswerGroup> result)
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
     * Controls the view elements based on the state of fetching and saving data.
     *
     * @param fetching   Adjusts the view elements based on whether data is being fetched.
     * @param saving     Adjusts the view elements based on whether data is being saved.
     * @param errorResID The resource ID of an error message to display.
     */
    private void controlView(boolean fetching, boolean saving, Integer errorResID)
    {
        boolean loading = fetching || saving;

        if (loading)
        {
            binding.buttonEditQuestionAnswerGroupConfirm.setAlpha(0.7f);
            binding.buttonDeleteQuestionAnswerGroup.setAlpha(0.7f);

            if (saving)
                binding.sendQuestionAnswerGroupProgressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.sendQuestionAnswerGroupProgressBar.setVisibility(View.GONE);
            binding.buttonEditQuestionAnswerGroupConfirm.setAlpha(1f);
            binding.buttonDeleteQuestionAnswerGroup.setAlpha(1f);

            if (errorResID != null)
                createSnackbar(errorResID, Snackbar.LENGTH_LONG).show();
        }

        binding.buttonEditQuestionAnswerGroupConfirm.setClickable(!loading);
        binding.buttonDeleteQuestionAnswerGroup.setClickable(!loading);
        binding.questionAnswerGroupName.setFocusableInTouchMode(!loading);
        binding.buttonAddMoreAnswers.setFocusableInTouchMode(!loading);

        LinearLayout layout;

        for (int i = 0; i < binding.questionAnswersContainer.getChildCount(); i++)
        {
            layout = (LinearLayout) binding.questionAnswersContainer.getChildAt(i);
            EditText questionAnswerOptionEditText = (EditText) layout.getChildAt(0);
            Button deleteButton = (Button) layout.getChildAt(1);

            questionAnswerOptionEditText.setFocusableInTouchMode(!loading);
            deleteButton.setClickable(!loading);
        }
    }

    /**
     * Creates a Snackbar with an action.
     *
     * @param stringResID Resource ID of the string to display.
     * @param length      Length of the Snackbar.
     * @param actionResID Resource ID of the action string.
     * @param listener    Listener for the action.
     * @return            The created Snackbar.
     */
    private Snackbar createActionSnackbar(int stringResID, int length, int actionResID, View.OnClickListener listener)
    {
        Snackbar snackbar = createSnackbar(stringResID, length);
        snackbar.setAction(actionResID, listener);

        return snackbar;
    }

    /**
     * Creates a Snackbar.
     *
     * @param stringResID Resource ID of the string to display.
     * @param length      Length of the Snackbar.
     * @return            The created Snackbar.
     */
    private Snackbar createSnackbar(int stringResID, int length)
    {
        return Snackbar.make(binding.getRoot(), stringResID, length);
    }
}