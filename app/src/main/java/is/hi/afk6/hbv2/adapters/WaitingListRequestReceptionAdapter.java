package is.hi.afk6.hbv2.adapters;

import android.graphics.Color;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.callbacks.WaitingListDeleteCallback;
import is.hi.afk6.hbv2.databinding.RecyclerviewReceptionWaitingListRequestBinding;
import is.hi.afk6.hbv2.entities.WaitingListRequest;

/**
 * Adapter to display WaitingListRequests for Receptionists.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 22/03/2024
 * @version 1.0
 */
public class WaitingListRequestReceptionAdapter extends RecyclerView.Adapter<WaitingListRequestReceptionAdapter.ViewHolder>
{
    // WaitingListRequests to display
    private List<WaitingListRequest> waitingListRequests;
    private RecyclerviewReceptionWaitingListRequestBinding binding;
    private WaitingListDeleteCallback callback;

    private int expandedPos = -1;

    /**
     * ViewHolder for the WaitingListRequestAdapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // Binding for each item in the RecyclerView
        private RecyclerviewReceptionWaitingListRequestBinding binding;

        public ViewHolder(View view)
        {
            super(view);
            binding = RecyclerviewReceptionWaitingListRequestBinding.bind(view);
        }

        public RecyclerviewReceptionWaitingListRequestBinding getBinding()
        {
            return binding;
        }
    }

    /**
     * Constructor for the WaitingListRequestAdapter.
     *
     * @param waitingListRequests WaitingListRequests to display.
     */
    public WaitingListRequestReceptionAdapter(List<WaitingListRequest> waitingListRequests, WaitingListDeleteCallback callback)
    {
        this.waitingListRequests = waitingListRequests;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_reception_waiting_list_request, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        binding = holder.getBinding();

        // Extend or collapse the item when clicked.
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

        setView(waitingListRequests.get(holder.getAdapterPosition()));

        binding.deleteWaitingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                WaitingListRequest clicked = waitingListRequests.get(holder.getAdapterPosition());
                callback.onDeleteWaitingListRequestClicked(clicked);

                waitingListRequests.remove(clicked);

                notifyDataSetChanged();
            }
        });
    }

    /**
     * Set the view for a WaitingListRequest.
     *
     * @param current WaitingListRequest to display.
     */
    private void setView(WaitingListRequest current)
    {
        if (current.isStatus())
            binding.waitingListBackground.setBackgroundColor(Color.parseColor("#E4FEDE"));


        binding.waitingListPatient.setText(current.getPatient().getName().split(" ")[0]);
        binding.waitingListGrade.setText(String.valueOf(current.getGrade()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        binding.waitingListDate.setText(current.getDateOfRequest().format(formatter));
    }

    @Override
    public int getItemCount() {
        return waitingListRequests.size();
    }
}
