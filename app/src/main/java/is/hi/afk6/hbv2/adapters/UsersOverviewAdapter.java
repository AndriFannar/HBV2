package is.hi.afk6.hbv2.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import java.util.List;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.callbacks.UserOverviewCallback;
import is.hi.afk6.hbv2.databinding.RecyclerviewAdminUserOverviewBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.enums.UserRole;

public class UsersOverviewAdapter extends RecyclerView.Adapter<UsersOverviewAdapter.ViewHolder> implements AdapterView.OnItemSelectedListener {

    private List<User> users;
    private RecyclerviewAdminUserOverviewBinding binding;
    private UserOverviewCallback callback;

    private int expandedPos = -1;

    /**
     * ViewHolder for the WaitingListRequestAdapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // Binding for each item in the RecyclerView
        private RecyclerviewAdminUserOverviewBinding binding;

        public ViewHolder(View view)
        {
            super(view);
            binding = RecyclerviewAdminUserOverviewBinding.bind(view);
        }

        public RecyclerviewAdminUserOverviewBinding getBinding()
        {
            return binding;
        }
    }

    /**
     * Constructor for the UsersOverviewAdapter.
     *
     * @param users users to display.
     */
    public UsersOverviewAdapter(List<User> users, UserOverviewCallback callback)
    {
        this.users  = users;
        this.callback = callback;
    }

    @NonNull
    @Override
    public UsersOverviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_admin_user_overview, parent, false);

        return new UsersOverviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        binding = holder.getBinding();

        boolean isExpanded = position == expandedPos;
        binding.userOverviewDetail.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                expandedPos = isExpanded ? -1 : position;
                TransitionManager.beginDelayedTransition(binding.userOverviewDetail);
                notifyDataSetChanged();
            }
        });

        setView(users.get(holder.getAdapterPosition()));

        binding.updateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                callback.onViewUserClicked(users.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    /**
     * Set the view for a User.
     *
     * @param current User to make view for.
     */
    private void setView(User current)
    {
        if(current.getRole() == UserRole.USER){
            binding.userBackground.setBackgroundColor(Color.parseColor("#E6D7FF"));
        } else if(current.getRole() == UserRole.ADMIN) {
            binding.userBackground.setBackgroundColor(Color.parseColor("#D0F8FF"));
        } else if(current.getRole() == UserRole.PHYSIOTHERAPIST){
            binding.userBackground.setBackgroundColor(Color.parseColor("#E4FEDE"));
        } else {
            binding.userBackground.setBackgroundColor(Color.parseColor("#FEDFFC"));
        }

        binding.userName.setText(current.getName());
        binding.userRole.setText(current.getRole().getDisplayString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
