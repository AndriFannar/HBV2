package is.hi.afk6.hbv2.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.RecyclerviewViewQuestionnaireAnswersBinding;
import is.hi.afk6.hbv2.entities.QuestionAnswerPair;

/**
 *  Adapter to display Users answers for the Questionnaire.
 *
 *   @author Ástríður Haraldsdóttir Passauer, ahp9@hi.is
 *   @since 13/04/2024
 *   @version 1.0
 */
public class ViewQuestionnaireAnswersAdapter extends RecyclerView.Adapter<ViewQuestionnaireAnswersAdapter.ViewHolder> {
    private List<QuestionAnswerPair> questions;
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
     * @param questions QuestionAnswerPair to display
     */
    public ViewQuestionnaireAnswersAdapter(List<QuestionAnswerPair> questions){
        this.questions = questions;
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

        setView(questions.get(holder.getAdapterPosition()));
    }

    /**
     * Set the view for the Answers from the Questionnaire
     * @param current QuestionAnswerPair to make view for
     */
    private  void setView(QuestionAnswerPair current){
        binding.questionnaireAnswersBackground.setBackgroundResource(R.color.pastel_purple);
        binding.questionText.setText(current.getQuestion().getQuestionString());
        if(current.getAnswer() > 0){
            binding.questionAnswer.setText(current.getQuestion().getQuestionAnswerGroup().getQuestionAnswers().get(current.getAnswer()));
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}
