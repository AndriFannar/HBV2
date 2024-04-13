package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentUserBinding;
import is.hi.afk6.hbv2.entities.User;

/**
 * Fragment that enables a User to see information.
 *
 * @author Ástríður Haraldsdóttir Passauer, ahp9@hi.is
 * @since 05/04/2024
 * @version 1.0
 */
public class UserFragment extends Fragment
{
    private User loggedInUser;
    private FragmentUserBinding binding;

    @Override
    public  void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        if(loggedInUser != null){
            binding.userName.setText(loggedInUser.getName());
            binding.userPhoneNr.setText(loggedInUser.getPhoneNumber());
            binding.userAddress.setText(loggedInUser.getAddress());
            binding.userEmail.setText(loggedInUser.getEmail());
        }

        binding.updateUser.setOnClickListener(v -> changeFragment());
        return view;
    }

    private void changeFragment(){
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
        bundle.putParcelable(getString(R.string.edited_user), loggedInUser);

        NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);
        navController.navigate(R.id.nav_edit_user, bundle);
    }

}
