package is.hi.afk6.hbv2.adapters;

import android.annotation.SuppressLint;
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
import is.hi.afk6.hbv2.callbacks.ViewCallback;
import is.hi.afk6.hbv2.comparators.waitingListComparators.WaitingListRequestAcceptedComparator;
import is.hi.afk6.hbv2.comparators.waitingListComparators.WaitingListRequestBodyPartComparator;
import is.hi.afk6.hbv2.comparators.waitingListComparators.WaitingListRequestDateComparator;
import is.hi.afk6.hbv2.comparators.waitingListComparators.WaitingListRequestGradeComparator;
import is.hi.afk6.hbv2.comparators.waitingListComparators.WaitingListRequestPatientNameComparator;
import is.hi.afk6.hbv2.databinding.RecyclerviewPhysioWaitingListRequestBinding;
import is.hi.afk6.hbv2.entities.WaitingListRequest;

/**
 * Adapter to display Physiotherapists' WaitingListRequests.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 14/03/2024
 * @version 1.0
 */
public class WaitingListRequestPhysioAdapter extends RecyclerView.Adapter<WaitingListRequestPhysioAdapter.ViewHolder> implements AdapterView.OnItemSelectedListener
{
    // WaitingListRequests to display
    private List<WaitingListRequest> waitingListRequests;
    private RecyclerviewPhysioWaitingListRequestBinding binding;
    private ViewCallback<WaitingListRequest> callbackView;
    private AcceptCallback<WaitingListRequest> callbackAccept;

    private int expandedPos = -1;

    /**
     * ViewHolder for the WaitingListRequestAdapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // Binding for each item in the RecyclerView
        private RecyclerviewPhysioWaitingListRequestBinding binding;

        public ViewHolder(View view)
        {
            super(view);
            binding = RecyclerviewPhysioWaitingListRequestBinding.bind(view);
        }

        public RecyclerviewPhysioWaitingListRequestBinding getBinding()
        {
            return binding;
        }
    }

    /**
     * Constructor for the WaitingListRequestAdapter.
     *
     * @param waitingListRequests WaitingListRequests to display.
     * @param callbackView        Callback for when a WaitingListRequest is requested to be viewed.
     * @param acceptCallback      Callback for when a WaitingListRequest is requested to be accepted.
     */
    public WaitingListRequestPhysioAdapter(List<WaitingListRequest> waitingListRequests, ViewCallback<WaitingListRequest> callbackView, AcceptCallback<WaitingListRequest> acceptCallback)
    {
        this.waitingListRequests = waitingListRequests;
        this.callbackView = callbackView;
        this.callbackAccept = acceptCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_physio_waiting_list_request, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position)
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

        binding.acceptWaitingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                WaitingListRequest clicked = waitingListRequests.get(holder.getAdapterPosition());
                callbackAccept.onAcceptClicked(clicked);
                clicked.setStatus(true);

                notifyItemChanged(position);
            }
        });

        binding.viewWaitingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                callbackView.onViewClicked(waitingListRequests.get(holder.getAdapterPosition()));
            }
        });
    }

    /**
     * Set the view for a WaitingListRequest.
     *
     * @param current WaitingListRequest to make view for.
     */
    private void setView(WaitingListRequest current)
    {
        if (current.isStatus())
        {
            binding.waitingListBackground.setBackgroundResource(R.color.pale_green);
            binding.acceptWaitingListButton.setVisibility(View.GONE);
        }
        else
        {
            binding.waitingListBackground.setBackgroundResource(R.color.pale_yellow);
            binding.acceptWaitingListButton.setVisibility(View.VISIBLE);
        }

        binding.waitingListPatient.setText(current.getPatient().getName().split(" ")[0]);
        binding.waitingListGrade.setText(String.valueOf(current.getGrade()));

        binding.waitingListQuestionnaire.setText(current.getQuestionnaire().getName());

        binding.waitingListDescription.setText(current.getDescription());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        binding.waitingListDate.setText(current.getDateOfRequest().format(formatter));
    }

    @Override
    public int getItemCount() {
        return waitingListRequests.size();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        switch (position)
        {
            case 0:
                waitingListRequests.sort(new WaitingListRequestGradeComparator());
                break;
            case 1:
                waitingListRequests.sort(new WaitingListRequestDateComparator());
                break;
            case 2:
                waitingListRequests.sort(new WaitingListRequestPatientNameComparator());
                break;
            case 3:
                waitingListRequests.sort(new WaitingListRequestBodyPartComparator());
                break;
            case 4:
                waitingListRequests.sort(new WaitingListRequestAcceptedComparator());
                break;
        }

        notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }
}
