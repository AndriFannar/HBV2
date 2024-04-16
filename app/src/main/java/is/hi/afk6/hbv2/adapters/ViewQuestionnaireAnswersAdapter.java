package is.hi.afk6.hbv2.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.RecyclerviewViewQuestionnaireAnswersBinding;
import is.hi.afk6.hbv2.entities.Question;

/**
 *  Adapter to display Users answers for the Questionnaire.
 *
 *   @author Ástríður Haraldsdóttir Passauer, ahp9@hi.is
 *   @since 13/04/2024
 *   @version 2.0
 */
public class ViewQuestionnaireAnswersAdapter extends RecyclerView.Adapter<ViewQuestionnaireAnswersAdapter.ViewHolder> {
    private HashMap<Long, Integer> answers;
    private List<Question> questions;
    private RecyclerviewViewQuestionnaireAnswersBinding binding;

    /**
     * ViewHolder for the ViewQuestionnaireAnswersAdapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewViewQuestionnaireAnswersBinding binding;
        public ViewHolder(View view){
            super(view);
            binding = RecyclerviewViewQuestionnaireAnswersBinding.bind(view);
        }

        public RecyclerviewViewQuestionnaireAnswersBinding getBinding(){ return binding; }
    }

    /**
     * Constructor for the ViewQuestionnaireAnswersAdapter
     * @param questions Questions to display
     * @param answers   Answers to the questions
     */
    public ViewQuestionnaireAnswersAdapter(List<Question> questions, HashMap<Long, Integer> answers)
    {
        this.questions = questions;
        this.answers = answers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_view_questionnaire_answers, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        binding = holder.getBinding();

        Question currentQuestion = questions.get(holder.getAdapterPosition());

        setView(currentQuestion, answers.getOrDefault(currentQuestion.getId(), -1));
    }

    /**
     * Set the view for the Answers from the Questionnaire
     * @param question Question to make view for
     * @param answer   Patient's answer to the question
     */
    private  void setView(Question question, Integer answer){
        binding.questionnaireAnswersBackground.setBackgroundResource(R.color.pastel_purple);
        binding.questionText.setText(question.getQuestionString());
        if(answer >= 0)
        {
            try
            {
                binding.questionAnswer.setText(question.getQuestionAnswerGroup().getQuestionAnswers().get(answer));
            }
            catch (IndexOutOfBoundsException ignored)
            {
            }
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
