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
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

public class UserFragment extends Fragment {
    private UserService userService;
    private Long user_id;

    @Override
    public  void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        userService = new UserServiceImplementation(new APIServiceImplementation());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
        is.hi.afk6.hbv2.databinding.FragmentUserBinding binding = FragmentUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        user_id = 8L;

        User user = userService.getUserByID(user_id);

        if(user != null){
            binding.userName.setText(user.getName());
            binding.userPhoneNr.setText(user.getPhoneNumber());
            binding.userAddress.setText(user.getAddress());
            binding.userEmail.setText(user.getEmail());
        }


        binding.buttonGoEdit.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            EditUserFragment editUserFragment = new EditUserFragment();

            Bundle bundle = new Bundle();
            bundle.putLong("userId", user_id);
            editUserFragment.setArguments(bundle);

            fragmentTransaction.replace(R.id.edit_fragment_container_view, editUserFragment);
            fragmentTransaction.addToBackStack(null); // Add transaction to back stack
            fragmentTransaction.commit();
        });
        return view;
    }
}
