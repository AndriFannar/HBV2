package is.hi.afk6.hbv2.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.callbacks.AcceptCallback;
import is.hi.afk6.hbv2.callbacks.DeleteCallback;
import is.hi.afk6.hbv2.callbacks.DisplayCallback;
import is.hi.afk6.hbv2.callbacks.ViewCallback;
import is.hi.afk6.hbv2.comparators.QuestionnaireDisplayComparator;
import is.hi.afk6.hbv2.comparators.QuestionnaireNameComparator;
import is.hi.afk6.hbv2.comparators.QuestionnaireSizeComparator;
import is.hi.afk6.hbv2.comparators.WaitingListRequestBodyPartComparator;
import is.hi.afk6.hbv2.comparators.WaitingListRequestDateComparator;
import is.hi.afk6.hbv2.comparators.WaitingListRequestGradeComparator;
import is.hi.afk6.hbv2.comparators.WaitingListRequestPatientNameComparator;
import is.hi.afk6.hbv2.databinding.RecyclerviewPhysioWaitingListRequestBinding;
import is.hi.afk6.hbv2.databinding.RecyclerviewQuestionnaireBinding;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.WaitingListRequest;

/**
 * Adapter to display Questionnaires.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 05/04/2024
 * @version 1.0
 */
public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder> implements AdapterView.OnItemSelectedListener
{
    // Questionnaires to display
    private List<Questionnaire> questionnaires;
    private RecyclerviewQuestionnaireBinding binding;
    private ViewCallback<Questionnaire> callbackView;
    private DeleteCallback<Questionnaire> callbackDelete;
    private DisplayCallback<Questionnaire> callbackDisplay;

    private int expandedPos = -1;
    private int sortPos = 0;

    /**
     * ViewHolder for the QuestionnaireAdapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // Binding for each item in the RecyclerView
        private RecyclerviewQuestionnaireBinding binding;

        public ViewHolder(View view)
        {
            super(view);
            binding = RecyclerviewQuestionnaireBinding.bind(view);
        }

        public RecyclerviewQuestionnaireBinding getBinding()
        {
            return binding;
        }
    }

    /**
     * Constructor for the QuestionnaireAdapter.
     *
     * @param questionnaires  Questionnaires to display.
     * @param callbackView    Callback for when a Questionnaire is requested to be viewed.
     * @param callbackDelete  Callback for when a Questionnaire is requested to be deleted.
     * @param callbackDisplay Callback for when a Questionnaire is requested to be displayed.
     */
    public QuestionnaireAdapter(List<Questionnaire> questionnaires, ViewCallback<Questionnaire> callbackView, DeleteCallback<Questionnaire> callbackDelete, DisplayCallback<Questionnaire> callbackDisplay)
    {
        this.questionnaires = questionnaires;
        this.callbackView = callbackView;
        this.callbackDelete = callbackDelete;
        this.callbackDisplay = callbackDisplay;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_questionnaire, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position)
    {
        binding = holder.getBinding();

        // Extend or collapse the item when clicked.
        boolean isExpanded = position == expandedPos;
        binding.questionnaireDetail.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedPos = isExpanded ? -1 : position;
                TransitionManager.beginDelayedTransition(binding.questionnaireDetail);
                notifyDataSetChanged();
            }
        });

        setView(questionnaires.get(holder.getAdapterPosition()));

        binding.showHideQuestionnaireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Questionnaire clicked = questionnaires.get(holder.getAdapterPosition());
                callbackDisplay.onDisplayClicked(clicked);
                clicked.setDisplayOnForm(!clicked.isDisplayOnForm());

                expandedPos = -1;
                notifyItemChanged(position);
                sort();
            }
        });

        binding.viewQuestionnaireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                callbackView.onViewClicked(questionnaires.get(holder.getAdapterPosition()));
            }
        });

        binding.deleteQuestionnaireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Questionnaire clicked = questionnaires.get(holder.getAdapterPosition());
                expandedPos = -1;
                callbackDelete.onDeleteClicked(clicked);

                questionnaires.remove(clicked);

                notifyDataSetChanged();
            }
        });
    }

    /**
     * Set the view for a WaitingListRequest.
     *
     * @param current WaitingListRequest to make view for.
     */
    private void setView(Questionnaire current)
    {
        if (current.isDisplayOnForm())
        {
            binding.questionnaireBackground.setBackgroundResource(R.color.pale_green);
            binding.showHideQuestionnaireButton.setText(R.string.questionnaire_hide_button_text);
        }
        else
        {
            binding.questionnaireBackground.setBackgroundResource(R.color.pale_yellow);
            binding.showHideQuestionnaireButton.setText(R.string.questionnaire_show_button_text);
        }

        binding.questionnaireName.setText(current.getName());
        binding.questionnaireNoOfQuestions.setText(String.valueOf(current.getQuestions().size()));
    }

    @Override
    public int getItemCount() {
        return questionnaires.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        sortPos = position;
        sort();
    }

    private void sort()
    {
        switch (sortPos)
        {
            case 0:
                questionnaires.sort(new QuestionnaireNameComparator());
                break;
            case 1:
                questionnaires.sort(new QuestionnaireSizeComparator());
                break;
            case 2:
                questionnaires.sort(new QuestionnaireDisplayComparator());
                break;
        }

        notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public void addQuestionnaire(Questionnaire questionnaire)
    {
        questionnaires.add(questionnaire);
        notifyDataSetChanged();
        sort();
    }
}
