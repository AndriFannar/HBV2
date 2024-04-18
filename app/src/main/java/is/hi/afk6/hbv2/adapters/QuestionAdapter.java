package is.hi.afk6.hbv2.adapters;

import android.annotation.SuppressLint;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.callbacks.DisplayCallback;
import is.hi.afk6.hbv2.callbacks.ViewCallback;
import is.hi.afk6.hbv2.databinding.RecyclerviewQuestionBinding;
import is.hi.afk6.hbv2.entities.Question;
import is.hi.afk6.hbv2.entities.Questionnaire;

/**
 * Adapter to display Questions.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 05/04/2024
 * @version 1.0
 */
public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder>
{
    // Questions to display
    private List<Question> questions;
    private Questionnaire questionnaire;
    private is.hi.afk6.hbv2.databinding.RecyclerviewQuestionBinding binding;
    private ViewCallback<Question> callbackView;

    private int expandedPos = -1;

    /**
     * ViewHolder for the QuestionAdapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // Binding for each item in the RecyclerView
        private RecyclerviewQuestionBinding binding;

        public ViewHolder(View view)
        {
            super(view);
            binding = RecyclerviewQuestionBinding.bind(view);
        }

        public RecyclerviewQuestionBinding getBinding()
        {
            return binding;
        }
    }

    /**
     * Constructor for the QuestionAdapter.
     *
     * @param questions       Questions to display.
     * @param questionnaire   Questionnaire being edited.
     * @param callbackView    Callback for when a Question is requested to be viewed.
     */
    public QuestionAdapter(List<Question> questions, Questionnaire questionnaire, ViewCallback<Question> callbackView)
    {
        this.questions = questions;
        this.questionnaire = questionnaire;
        this.callbackView = callbackView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_question, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        binding = holder.getBinding();

        // Extend or collapse the item when clicked.
        boolean isExpanded = position == expandedPos;
        binding.questionDetail.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedPos = isExpanded ? -1 : position;
                TransitionManager.beginDelayedTransition(binding.questionDetail);
                notifyDataSetChanged();
            }
        });

        setView(questions.get(holder.getAdapterPosition()));

        // Add/remove Question from Questionnaire.
        binding.addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Question clicked = questions.get(holder.getAdapterPosition());

                Question existing = questionnaire.getQuestions().stream().filter(question -> question.getId().equals(clicked.getId())).findFirst().orElse(null);

                if (existing != null)
                {
                    questionnaire.removeQuestion(existing);
                    clicked.removeQuestionnaireID(questionnaire.getId());
                }
                else
                {
                    questionnaire.addQuestion(clicked);
                    clicked.addQuestionnaireID(questionnaire.getId());
                }

                notifyItemChanged(position);
            }
        });

        binding.viewQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                callbackView.onViewClicked(questions.get(holder.getAdapterPosition()));
            }
        });
    }

    /**
     * Set the view for a WaitingListRequest.
     *
     * @param current WaitingListRequest to make view for.
     */
    private void setView(Question current)
    {
        if (questionnaire.getQuestions().stream().anyMatch(question -> question.getId().equals(current.getId())))
        {
            binding.questionBackground.setBackgroundResource(R.color.pale_green);
            binding.addQuestionButton.setText(R.string.remove_question_button_text);
        }
        else
        {
            binding.questionBackground.setBackgroundResource(R.color.pale_yellow);
            binding.addQuestionButton.setText(R.string.add_question_button_text);
        }

        binding.questionText.setText(current.getQuestionString());
        binding.questionNoOfAnswers.setText(String.valueOf(current.getQuestionAnswerGroup().getGroupName()));
        binding.questionWeight.setText(String.valueOf(current.getWeight()));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
