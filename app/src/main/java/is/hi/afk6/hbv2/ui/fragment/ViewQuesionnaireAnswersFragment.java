package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.adapters.ViewQuestionnaireAnswersAdapter;
import is.hi.afk6.hbv2.callbacks.ViewCallback;
import is.hi.afk6.hbv2.databinding.FragmentViewQuesionnaireAnswersBinding;

import java.util.ArrayList;
import java.util.List;
import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.QuestionAnswerPair;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import android.widget.LinearLayout;
import android.widget.TextView;
import is.hi.afk6.hbv2.entities.WaitingListRequest;

public class ViewQuesionnaireAnswersFragment extends Fragment {
    private FragmentViewQuesionnaireAnswersBinding binding;
    private User loggedInUser;
    private WaitingListRequest waitingListRequest;

    /**
     * Called when the fragment is starting. This method is invoked during the creation of the fragment.
     * It initializes necessary services and data structures.
     *
     * @param savedInstanceState If the fragment is being re-initialized after previously being shut down then
     *                           this Bundle contains the data it most recently supplied in {@link #onSaveInstanceState(Bundle)}.
     *                           Note: Otherwise it is null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
            waitingListRequest = getArguments().getParcelable(getString(R.string.waiting_list_request));
        }
    }

    /**
     * Called to have the fragment instantiate its user interface view. This method is called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment,
     *                           typically obtained from {@link android.app.Activity#getLayoutInflater()}.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     *                           The fragment should not add the view itself, but this can be used to generate
     *                           the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     *                           as given here.
     * @return The View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentViewQuesionnaireAnswersBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        binding.questionnaireAnswersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        if (waitingListRequest != null)
        {
            updateUIWithWaitingListRequest(waitingListRequest);
            createAnswers();

        }
        else
        {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);

            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
            navController.navigate(R.id.nav_waiting_list_overview, bundle);
        }

        return view;
    }

    /**
     * Create Adapter to show Users answers from Questionnaire
     */
    private void createAnswers(){
        List<Integer> answers = waitingListRequest.getQuestionnaireAnswers();
        Questionnaire questionnaire = waitingListRequest.getQuestionnaire();
        List<Question> questions = questionnaire.getQuestions();
        List<QuestionAnswerPair> pairs = createQuestionAnswerPair(questions, answers);

        ViewQuestionnaireAnswersAdapter adapter = new ViewQuestionnaireAnswersAdapter(pairs);
        binding.questionnaireAnswersRecyclerView.setAdapter(adapter);
    }

    /**
     * Creates a QuestionAnswerPair out of the questions Users answered and
     * their answers
     * @param questions List of questions
     * @param answers List of answers
     * @return List of paired questions and answers
     */
    private List<QuestionAnswerPair> createQuestionAnswerPair(List<Question> questions, List<Integer> answers) {
        List<QuestionAnswerPair> pair = new ArrayList<QuestionAnswerPair>();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            Integer answer = answers.get(i);
            if(answer != null) {
                pair.add(new QuestionAnswerPair(question, answer));
            } else {
                pair.add(new QuestionAnswerPair(question, -1));
            }
        }

        return pair;
    }


    /**
     * Updates the UI elements with the information from the provided WaitingListRequest object.
     * If the provided Binding object is not null, this method sets the name of the selected user,
     * the user's grade, and the description text based on the information in the WaitingListRequest object.
     *
     * @param waitingListRequest The WaitingListRequest object containing the information to be displayed.
     */
    private void updateUIWithWaitingListRequest(WaitingListRequest waitingListRequest) {
        if (binding != null) {
            float grade = (float) waitingListRequest.getGrade();
            binding.selectedUserName.setText(loggedInUser.getName());
            binding.selectedUserGrade.setText(String.format(" - %s stig", (int) grade));
            binding.descriptionText.setText(waitingListRequest.getDescription());
        }
    }

}

