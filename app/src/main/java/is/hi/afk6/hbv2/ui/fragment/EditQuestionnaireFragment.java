package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.adapters.QuestionAdapter;
import is.hi.afk6.hbv2.adapters.QuestionnaireAdapter;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.callbacks.DisplayCallback;
import is.hi.afk6.hbv2.callbacks.ViewCallback;
import is.hi.afk6.hbv2.databinding.FragmentEditQuestionnaireBinding;
import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionService;
import is.hi.afk6.hbv2.services.QuestionnaireService;
import is.hi.afk6.hbv2.services.implementation.QuestionServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;

/**
 * Fragment that enables a User to edit a Questionnaire.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 05/04/2024
 * @version 1.0
 */
public class EditQuestionnaireFragment extends Fragment implements ViewCallback<Question>
{
    private FragmentEditQuestionnaireBinding binding;
    private User loggedInUser;
    private Questionnaire questionnaire;
    private QuestionnaireService questionnaireService;
    private QuestionService questionService;
    private boolean newQuestionnaire;
    private QuestionAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
            questionnaire = getArguments().getParcelable(getString(R.string.questionnaire));
        }

        newQuestionnaire = questionnaire == null;

        if (newQuestionnaire) questionnaire = new Questionnaire();

        APIService apiService = new APIServiceImplementation();
        questionnaireService = new QuestionnaireServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        questionService = new QuestionServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentEditQuestionnaireBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.questionOverviewRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.buttonSaveQuestionnaire.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                saveQuestionnaire();
            }
        });

        binding.buttonAddQuestion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);
                navController.navigate(R.id.nav_edit_question);
            }
        });

        if (!newQuestionnaire) binding.questionnaireName.setText(questionnaire.getName());

        populateList();

        return view;
    }

    /**
     * Inserts data into the RecyclerView.
     */
    private void populateList()
    {
        viewControl(true, false);

        EditQuestionnaireFragment that = this;
        questionService.getAllQuestions(new APICallback<List<Question>>()
        {
            @Override
            public void onComplete(ResponseWrapper<List<Question>> result)
            {
                if (result.getData() != null)
                {
                    requireActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            viewControl(false, result.getData().isEmpty());
                            adapter = new QuestionAdapter(result.getData(), questionnaire, that);
                            binding.questionOverviewRecyclerView.setAdapter(adapter);
                        }
                    });
                }
            }
        });
    }

    private void viewControl(boolean loading, boolean empty)
    {
        if (loading)
        {
            binding.questionOverviewRecyclerView.setVisibility(View.GONE);
            binding.questionOverviewProgressBar.setVisibility(View.VISIBLE);
        }
        else if (empty)
        {
            binding.questionOverviewRecyclerView.setVisibility(View.GONE);
            binding.questionOverviewProgressBar.setVisibility(View.GONE);
            binding.questionsError.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.questionOverviewRecyclerView.setVisibility(View.VISIBLE);
            binding.questionOverviewProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewClicked(Question question)
    {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
        bundle.putParcelable(getString(R.string.question), question);
        navController.navigate(R.id.nav_edit_question, bundle);
    }

    private void saveQuestionnaire()
    {
        if (binding.questionnaireName.getText().toString().isEmpty())
        {
            createSnackbar(R.string.questionnaire_missing_name_error, Snackbar.LENGTH_LONG).show();
            return;
        }

        questionnaire.setName(binding.questionnaireName.getText().toString());

        if (newQuestionnaire)
        {
            questionnaireService.saveNewQuestionnaire(questionnaire, new APICallback<Questionnaire>()
            {
                @Override
                public void onComplete(ResponseWrapper<Questionnaire> result)
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
            questionnaireService.updateQuestionnaire(questionnaire, new APICallback<Questionnaire>()
            {
                @Override
                public void onComplete(ResponseWrapper<Questionnaire> result)
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