package is.hi.afk6.hbv2.ui.fragment.questionnaire;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.adapters.QuestionnaireAdapter;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.callbacks.DeleteCallback;
import is.hi.afk6.hbv2.callbacks.DisplayCallback;
import is.hi.afk6.hbv2.callbacks.ViewCallback;
import is.hi.afk6.hbv2.databinding.FragmentQuestionnaireOverviewBinding;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionnaireService;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;

/**
 * Fragment that displays an overview of a physiotherapist clinic's Questionnaires.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 05/04/2024
 * @version 1.0
 */
public class QuestionnaireOverviewFragment extends Fragment implements ViewCallback<Questionnaire>, DeleteCallback<Questionnaire>, DisplayCallback<Questionnaire>
{
    private FragmentQuestionnaireOverviewBinding binding;
    private User loggedInUser;
    private QuestionnaireService questionnaireService;
    private QuestionnaireAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
        }

        APIService apiService = new APIServiceImplementation();
        questionnaireService = new QuestionnaireServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuestionnaireOverviewBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.questionnaireOverviewRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.questionnaire_overview_sort_options,
                android.R.layout.simple_spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.questionnaireOverviewSortSpinner.setAdapter(adapter);

        binding.buttonNewQuestionnaire.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);
                Bundle bundle = new Bundle();
                bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
                navController.navigate(R.id.nav_edit_questionnaire, bundle);
            }
        });

        populateList();

        return view;
    }

    /**
     * Inserts data into the RecyclerView.
     */
    private void populateList()
    {
        viewControl(true, false);

        QuestionnaireOverviewFragment that = this;
        if (loggedInUser.getRole() == UserRole.ADMIN)
        {
            questionnaireService.getAllQuestionnaires(new APICallback<List<Questionnaire>>()
            {
                @Override
                public void onComplete(ResponseWrapper<List<Questionnaire>> result)
                {
                    if (result.getData() != null)
                    {
                        requireActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                viewControl(false, result.getData().isEmpty());
                                adapter = new QuestionnaireAdapter(result.getData(), that, that, that);
                                binding.questionnaireOverviewSortSpinner.setOnItemSelectedListener(adapter);
                                binding.questionnaireOverviewRecyclerView.setAdapter(adapter);
                            }
                        });
                    }
                }
            });
        }
    }

    /**
     * Controls the visibility of the RecyclerView, ProgressBar and Error TextView.
     *
     * @param loading If there is data being loaded.
     * @param empty   If the resulting data is empty.
     */
    private void viewControl(boolean loading, boolean empty)
    {
        if (loading)
        {
            binding.questionnaireOverviewRecyclerView.setVisibility(View.GONE);
            binding.questionnaireOverviewProgressBar.setVisibility(View.VISIBLE);
        }
        else if (empty)
        {
            binding.questionnaireOverviewRecyclerView.setVisibility(View.GONE);
            binding.questionnaireOverviewProgressBar.setVisibility(View.GONE);
            binding.questionnaireOverviewError.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.questionnaireOverviewRecyclerView.setVisibility(View.VISIBLE);
            binding.questionnaireOverviewProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDisplayClicked(Questionnaire questionnaire)
    {
        questionnaireService.updateDisplayQuestionnaireOnForm(questionnaire.getId(), !questionnaire.isDisplayOnForm(), new APICallback<Questionnaire>()
        {
            @Override
            public void onComplete(ResponseWrapper<Questionnaire> result)
            {
                createSnackbar(!questionnaire.isDisplayOnForm() ? R.string.questionnaire_not_displayed_snackbar_text : R.string.questionnaire_displayed_snackbar_text,
                                Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewClicked(Questionnaire questionnaire)
    {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
        bundle.putParcelable(getString(R.string.questionnaire), questionnaire);
        navController.navigate(R.id.nav_edit_questionnaire, bundle);
    }

    /**
     * Deletes a Questionnaire.
     * If the Questionnaire has associated WaitingListRequests, it will not be deleted.
     * The Questionnaire will not be deleted unless the delete snackbar is dismissed.
     *
     * @param questionnaireToDelete The Object to delete.
     */
    @Override
    public void onDeleteClicked(Questionnaire questionnaireToDelete)
    {
        if (!questionnaireToDelete.getWaitingListRequestIDs().isEmpty())
        {
            adapter.addQuestionnaire(questionnaireToDelete);
            createSnackbar(R.string.questionnaire_delete_error, Snackbar.LENGTH_LONG).show();
        }
        else
        {
            Snackbar deleteSnackbar = createActionSnackbar(R.string.questionnaire_deleted_snackbar_text, Snackbar.LENGTH_LONG, R.string.snackbar_undo, new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    adapter.addQuestionnaire(questionnaireToDelete);
                    createSnackbar(R.string.question_restored_snackbar_text, Snackbar.LENGTH_SHORT).show();
                }
            });

            deleteSnackbar.addCallback(new Snackbar.Callback()
            {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    questionnaireService.deleteQuestionnaireByID(questionnaireToDelete.getId(), new APICallback<Questionnaire>() {
                            @Override
                            public void onComplete(ResponseWrapper<Questionnaire> result) {
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewControl(false, false);
                                    }
                                });
                            }
                    });
                }
            });
            deleteSnackbar.show();
        }
    }

    /**
     * Creates a Snackbar with a message and length.
     *
     * @param stringResID The resource ID of the message.
     * @param length      The length of the Snackbar.
     * @return            The Snackbar.
     */
    private Snackbar createSnackbar(int stringResID, int length)
    {
        return Snackbar.make(binding.getRoot(), stringResID, length);
    }

    /**
     * Creates a Snackbar with a message, length and an action.
     *
     * @param stringResID The resource ID of the message.
     * @param length      The length of the Snackbar.
     * @param actionResID The resource ID of the action.
     * @param listener    The listener for the action.
     * @return            The Snackbar.
     */
    private Snackbar createActionSnackbar(int stringResID, int length, int actionResID, View.OnClickListener listener)
    {
        Snackbar snackbar = createSnackbar(stringResID, length);
        snackbar.setAction(actionResID, listener);

        return snackbar;
    }
}