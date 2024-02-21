package is.hi.afk6.hbv2.ui.fragment;

import static is.hi.afk6.hbv2.ui.UserHomepageActivity.LOGGED_IN_USER;

import android.content.Intent;
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


import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentEditUserBinding;
import is.hi.afk6.hbv2.entities.api.ErrorResponse;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;
import is.hi.afk6.hbv2.ui.LoginActivity;

public class EditUserFragment extends Fragment {
    private FragmentEditUserBinding binding;
    private UserService userService;
    private User loggedInUser;

    @Override
    public  void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(LOGGED_IN_USER);
        }

        userService = new UserServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        edit_setup();

        if(loggedInUser != null){
            binding.editName.setText(loggedInUser.getName());
            binding.editPhone.setText(loggedInUser.getPhoneNumber());
            binding.editAddress.setText(loggedInUser.getAddress());
            binding.editEmail.setText(loggedInUser.getEmail());
        }


        binding.buttonEditSumbit.setOnClickListener(v -> validateUpdate());
        binding.editDeleteButton.setOnClickListener(v -> deleteAccount());

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
        if(loggedInUser == null){
            return;
        }
        loggedInUser.setName(binding.editName.getText().toString());
        loggedInUser.setAddress(binding.editAddress.getText().toString());
        loggedInUser.setPhoneNumber(binding.editPhone.getText().toString());
        loggedInUser.setEmail(binding.editEmail.getText().toString());

        userService.updateUser(loggedInUser.getId(), loggedInUser, result -> {
            ErrorResponse errorResponse = result.getErrorResponse();

            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    Log.d("ErrorRespnse", "Error:" + errorResponse);
                    if(errorResponse != null){
                        edit_setup();
                        errorResponse_input(errorResponse);
                    } else {
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        UserFragment userFragment = new UserFragment();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(LOGGED_IN_USER, loggedInUser);
                        userFragment.setArguments(bundle);

                        fragmentTransaction.replace(R.id.edit_fragment_container_view, userFragment);
                        fragmentTransaction.commit();
                    }
                }
            });
        });


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

    /**
     * Deletes acounts
     */
    public void deleteAccount(){
        //Eyða aðgangi
        userService.deleteUserByID(loggedInUser.getId(), result -> {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    if(result != null){
                        Log.d("TAG", "could not delete");
                    } else {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        requireActivity().startActivity(intent);
                    }
                }
            });
        });
    }
}
