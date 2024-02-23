package is.hi.afk6.hbv2.ui.fragment;

import static is.hi.afk6.hbv2.ui.UserHomepageActivity.EDITED_USER;
import static is.hi.afk6.hbv2.ui.UserHomepageActivity.LOGGED_IN_USER;

import android.os.Bundle;
import android.util.Log;
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

    @Override
    public  void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(LOGGED_IN_USER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
        is.hi.afk6.hbv2.databinding.FragmentUserBinding binding = FragmentUserBinding.inflate(inflater, container, false);
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
            bundle.putParcelable(LOGGED_IN_USER, loggedInUser);
            bundle.putParcelable(EDITED_USER, loggedInUser);
            editUserFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.edit_fragment_container_view, editUserFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }
}
