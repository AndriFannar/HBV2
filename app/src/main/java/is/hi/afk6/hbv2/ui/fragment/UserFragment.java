package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentUserBinding;
import is.hi.afk6.hbv2.entities.User;

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


        binding.buttonGoEdit.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            EditUserFragment editUserFragment = new EditUserFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
            editUserFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.super_fragment, editUserFragment);
            fragmentTransaction.addToBackStack(null); // Add transaction to back stack
            fragmentTransaction.commit();
        });
        return view;
    }
}
