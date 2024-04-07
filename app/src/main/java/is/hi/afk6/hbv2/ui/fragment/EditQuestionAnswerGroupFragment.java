package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

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

    private void setUpView()
    {
        binding.questionAnswersContainer.removeAllViews();

        if (newQuestionAnswerGroup)
        {
            binding.headerEditQuestionAnswerGroup.setText(R.string.new_question_answer_group_header);
            addAnswer(null);
        }
        else
        {
            binding.headerEditQuestionAnswerGroup.setText(R.string.edit_question_answer_group_header);
            binding.questionAnswerGroupName.setText(questionAnswerGroup.getGroupName());

            for (String questionAnswerOption : questionAnswerGroup.getQuestionAnswers())
            {
                addAnswer(questionAnswerOption);
            }
        }

        controlView(false, false, null);
    }

    private void addAnswer(String answer)
    {
        EditText questionAnswerOption = new EditText(getContext());

        if (answer != null)
        {
            questionAnswerOption.setText(answer);
        }
        else
        {
            questionAnswerOption.setHint(getString(R.string.question_answer_group_hint) + " " + (binding.questionAnswersContainer.getChildCount() + 1));
        }

        binding.questionAnswersContainer.addView(questionAnswerOption);
    }

    private void save()
    {
        controlView(false, true, null);

        if (binding.questionAnswerGroupName.getText().toString().isEmpty())
        {
            controlView(false, false, R.string.empty_question_answer_group_name_error);
            return;
        }

        questionAnswerGroup.setGroupName(binding.questionAnswerGroupName.getText().toString());

        if (!newQuestionAnswerGroup)
            questionAnswerGroup.getQuestionAnswers().clear();

        for (int i = 0; i < binding.questionAnswersContainer.getChildCount(); i++)
        {
            EditText questionAnswerOptionEditText = (EditText) binding.questionAnswersContainer.getChildAt(i);

            if (questionAnswerOptionEditText.getText().toString().isEmpty())
            {
                controlView(false, false, R.string.empty_question_answer_option_error);
                return;
            }

            questionAnswerGroup.getQuestionAnswers().add(questionAnswerOptionEditText.getText().toString());
        }

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
        else
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

    private void deleteQuestionAnswerGroup() {
        if (!questionAnswerGroup.getQuestionIDs().isEmpty()) {
            createSnackbar(R.string.question_delete_error, Snackbar.LENGTH_LONG).show();
        } else {
            controlView(false, true, null);
            questionAnswerGroupService.deleteQuestionAnswerGroup(questionAnswerGroup.getId(), new APICallback<QuestionAnswerGroup>() {
                @Override
                public void onComplete(ResponseWrapper<QuestionAnswerGroup> result) {
                    QuestionAnswerGroup deletedQuestionAnswerGroup = questionAnswerGroup;
                    questionAnswerGroup = new QuestionAnswerGroup();

                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            controlView(false, false, null);
                            Snackbar deleteSnackbar = createActionSnackbar(R.string.question_answer_group_deleted_snackbar_text, Snackbar.LENGTH_SHORT, R.string.snackbar_undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    controlView(false, true, null);
                                    questionAnswerGroupService.saveNewQuestionAnswerGroup(deletedQuestionAnswerGroup, new APICallback<QuestionAnswerGroup>() {
                                        @Override
                                        public void onComplete(ResponseWrapper<QuestionAnswerGroup> result) {
                                            questionAnswerGroup = result.getData();
                                            requireActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    controlView(false, false, null);
                                                }
                                            });
                                        }
                                    });
                                }
                            });

                            deleteSnackbar.addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    super.onDismissed(transientBottomBar, event);

                                    if (questionAnswerGroup.getId() == null) {
                                        requireActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                                            }
                                        });
                                    }
                                }
                            });

                            deleteSnackbar.show();
                        }
                    });
                }
            });
        }
    }

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

        for (int i = 0; i < binding.questionAnswersContainer.getChildCount(); i++)
        {
            EditText questionAnswerOptionEditText = (EditText) binding.questionAnswersContainer.getChildAt(i);
            questionAnswerOptionEditText.setFocusableInTouchMode(!loading);
        }
    }

    private Snackbar createActionSnackbar(int stringResID, int length, int actionResID, View.OnClickListener listener)
    {
        Snackbar snackbar = createSnackbar(stringResID, length);
        snackbar.setAction(actionResID, listener);

        return snackbar;
    }

    private Snackbar createSnackbar(int stringResID, int length)
    {
        return Snackbar.make(binding.getRoot(), stringResID, length);
    }
}