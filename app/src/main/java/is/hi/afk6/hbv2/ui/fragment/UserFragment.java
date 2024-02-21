package is.hi.afk6.hbv2.ui.fragment;

import static is.hi.afk6.hbv2.ui.UserHomepageActivity.LOGGED_IN_USER;

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
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;
import is.hi.afk6.hbv2.ui.UserHomepageActivity;

<<<<<<< HEAD
public class UserFragment extends Fragment
{
    private User loggedInUser;
=======
public class UserFragment extends Fragment {
    private UserService userService;
    private Long user_id;
>>>>>>> develop

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
<<<<<<< HEAD
        //Temp User Id
        //User user = userService.getUserByID(8L);
=======
        user_id = 8L;

        User user = userService.getUserByID(user_id);
>>>>>>> develop

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
<<<<<<< HEAD
            Bundle bundle = new Bundle();
            bundle.putParcelable(LOGGED_IN_USER, loggedInUser);
=======

            Bundle bundle = new Bundle();
            bundle.putLong("userId", user_id);
>>>>>>> develop
            editUserFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.edit_fragment_container_view, editUserFragment);
            fragmentTransaction.addToBackStack(null); // Add transaction to back stack
            fragmentTransaction.commit();
        });
        return view;
    }
}
