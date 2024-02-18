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
import is.hi.afk6.hbv2.databinding.FragmentEditUserBinding;
import is.hi.afk6.hbv2.entities.ErrorResponse;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

public class EditUserFragment extends Fragment {
    private FragmentEditUserBinding binding;
    private UserService userService;
    private User user;

    @Override
    public  void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        userService = new UserServiceImplementation(new APIServiceImplementation());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        user = userService.getUserByID(8L);
        edit_setup();

        if(user != null){
            binding.editName.setText(user.getName());
            binding.editPhone.setText(user.getPhoneNumber());
            binding.editAddress.setText(user.getAddress());
            binding.editEmail.setText(user.getEmail());
        }


        binding.buttonEditSumbit.setOnClickListener(v -> validateUpdate());

        return view;
    }

    /**
     * Sets the error texts as invisible, so they don't show up
     * when opening the edit screen.
     */
    private void edit_setup(){
        binding.editErrorName.setVisibility(View.INVISIBLE);
        binding.editErrorPhoneNr.setVisibility(View.INVISIBLE);
        binding.editErrorAddress.setVisibility(View.INVISIBLE);
        binding.editErrorEmail.setVisibility(View.INVISIBLE);
    }


    /**
     * Checks if the changes that are made are validated and allowed.
     */
    private void validateUpdate(){
        if(user == null){
            return;
        }
        user.setName(binding.editName.getText().toString());
        user.setAddress(binding.editAddress.getText().toString());
        user.setPhoneNumber(binding.editPhone.getText().toString());
        user.setEmail(binding.editEmail.getText().toString());

        ErrorResponse errorResponse = userService.updateUser(user.getId(), user);

        if(errorResponse != null){
            errorResponse_input(errorResponse);
        } else {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            UserFragment userFragment = new UserFragment();
            fragmentTransaction.replace(R.id.edit_fragment_container_view, userFragment);
            fragmentTransaction.commit();
        }
    }

    private void errorResponse_input(ErrorResponse errorResponse){
        String nameError = errorResponse.getErrorDetails().get("name");
        if (nameError != null)
        {
            binding.editErrorName.setText(nameError);
            binding.editErrorName.setVisibility(View.VISIBLE);
        }
        String phoneError = errorResponse.getErrorDetails().get("phoneNumber");

        if (phoneError != null)
        {
            binding.editErrorPhoneNr.setText(phoneError);
            binding.editErrorPhoneNr.setVisibility(View.VISIBLE);
        }
        String addressError = errorResponse.getErrorDetails().get("address");

        if (addressError != null)
        {
            binding.editErrorAddress.setText(addressError);
            binding.editErrorAddress.setVisibility(View.VISIBLE);
        }
        String emailError = errorResponse.getErrorDetails().get("email");

        if (emailError != null)
        {
            binding.editErrorEmail.setText(emailError);
            binding.editErrorEmail.setVisibility(View.VISIBLE);
        }

    }
}
