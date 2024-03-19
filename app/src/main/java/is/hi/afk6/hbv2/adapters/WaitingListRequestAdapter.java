package is.hi.afk6.hbv2.adapters;

import android.graphics.Color;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.RecyclerviewWaitingListRequestBinding;
import is.hi.afk6.hbv2.entities.WaitingListRequest;

/**
 * Adapter to display Physiotherapists' WaitingListRequests.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 14/03/2024
 * @version 1.0
 */
public class WaitingListRequestAdapter extends RecyclerView.Adapter<WaitingListRequestAdapter.ViewHolder>
{
    // WaitingListRequests to display
    private List<WaitingListRequest> waitingListRequests;

    private int expandedPos = -1;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // Binding for each item in the RecyclerView
        private RecyclerviewWaitingListRequestBinding binding;

        public ViewHolder(View view)
        {
            super(view);
            binding = RecyclerviewWaitingListRequestBinding.bind(view);
        }

        public RecyclerviewWaitingListRequestBinding getBinding()
        {
            return binding;
        }
    }

    /**
     * Constructor for the WaitingListRequestAdapter.
     *
     * @param waitingListRequests WaitingListRequests to display.
     */
    public WaitingListRequestAdapter(List<WaitingListRequest> waitingListRequests)
    {
        this.waitingListRequests = waitingListRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_waiting_list_request, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        RecyclerviewWaitingListRequestBinding binding = holder.getBinding();

        boolean isExpanded = position == expandedPos;
        binding.waitingListOverviewDetail.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedPos = isExpanded ? -1 : position;
                TransitionManager.beginDelayedTransition(binding.waitingListOverviewDetail);
                notifyDataSetChanged();
            }
        });

        WaitingListRequest current = waitingListRequests.get(holder.getAdapterPosition());

        if (current.isStatus())
            binding.waitingListBackground.setBackgroundColor(Color.parseColor("#E4FEDE"));

        binding.waitingListDescription.setText(current.getPatient().getName());
        binding.waitingListQuestionnaire.setText(current.getDescription());
    }

    @Override
    public int getItemCount() {
        return waitingListRequests.size();
    }
}
