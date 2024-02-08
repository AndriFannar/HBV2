package is.hi.afk6.hbv2.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import is.hi.afk6.hbv2.databinding.FragmentHomeBinding;
import is.hi.afk6.hbv2.entities.LoginDTO;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.loginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginDTO loginInfo = new LoginDTO(binding.loginEmail.getText().toString(),
                                                  binding.loginPassword.getText().toString());

                UserServiceImplementation userService = new UserServiceImplementation(new APIServiceImplementation());
                User returnUser = userService.logInUser(loginInfo);

                binding.textLogin.setTextColor(Color.BLACK);
                String text = "Velkomin/nn " + returnUser.getName();
                binding.textLogin.setText(text);

            }
        });

        final TextView textView = binding.textLogin;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}