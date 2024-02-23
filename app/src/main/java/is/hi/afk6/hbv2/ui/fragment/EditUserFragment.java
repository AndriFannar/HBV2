package is.hi.afk6.hbv2.ui.fragment;

import static is.hi.afk6.hbv2.ui.UserHomepageActivity.EDITED_USER;
import static is.hi.afk6.hbv2.ui.UserHomepageActivity.LOGGED_IN_USER;

import android.app.Activity;
import android.app.AlertDialog;
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
import is.hi.afk6.hbv2.ui.UserHomepageActivity;
import is.hi.afk6.hbv2.ui.UsersOverviewActivity;

public class EditUserFragment extends Fragment {
    private FragmentEditUserBinding binding;
    private UserService userService;
    private User loggedInUser;
    private User editedUser;

    @Override
    public  void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(LOGGED_IN_USER);
            editedUser = getArguments().getParcelable(EDITED_USER);
        }

        userService = new UserServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        edit_setup();

        if(editedUser.getName().equals(loggedInUser.getName())){
            inputUserInEdit(loggedInUser);
            binding.buttonEditSumbit.setOnClickListener(v -> validateUpdate());
            binding.editDeleteButton.setOnClickListener(v -> deleteUserAlert(loggedInUser));
        } else {
            inputUserInEdit(editedUser);
            onlyVisibleEditText();
            binding.buttonEditSumbit.setOnClickListener(v -> changeStaffRole());
            binding.editDeleteButton.setOnClickListener(v -> deleteUserAlert(editedUser));
        }

        return view;
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

            requireActivity().runOnUiThread(() -> {
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
            });
        });
    }

    private void changeStaffRole(){
        Log.d("TAG", editedUser.getRole().toString());

        userService.updateUser(editedUser.getId(), editedUser, result -> {
            ErrorResponse errorResponse = result.getErrorResponse();

            requireActivity().runOnUiThread(() -> {
                if(errorResponse != null){
                    edit_setup();
                    errorResponse_input(errorResponse);
                } else {
                    Intent intent = UsersOverviewActivity.newIntent(getActivity(), loggedInUser);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    requireActivity().startActivity(intent);
                    requireActivity().finish();
                }
            });
        });
    }

    /**
     * Input the appropriate errors when updating information's
     * @param errorResponse Includes errorResponses for the edit info
     */
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
     * Pops a Alert to check if user has the intent to delete the account
     */
    public void deleteUserAlert(User deletedUser){
        Activity activity = getActivity();
        if (activity != null) {
            AlertDialog.Builder build = new AlertDialog.Builder(activity);
            build.setMessage("Ertu viss að þú viljir eyða aðganginum?");
            build.setCancelable(false);
            build.setPositiveButton("Já", (dialog, which) -> {
                deleteAccount(deletedUser);
            });

            build.setNegativeButton("Nei", (dialog, which) -> {
                dialog.cancel();
            });

            AlertDialog alert = build.create();
            alert.show();
        }
    }

    /**
     * Deletes accounts
     */
    public void deleteAccount(User deletedUser){
        userService.deleteUserByID(deletedUser.getId(), result -> {
            requireActivity().runOnUiThread(() -> {
                if(result != null){
                    Log.d("TAG", "could not delete");
                } else {
                    Intent intent;
                    if(editedUser.getName().equals(loggedInUser.getName())){
                        intent = new Intent(getActivity(), LoginActivity.class);
                    } else {
                        intent = new Intent(getActivity(), UsersOverviewActivity.class);
                    }
                    // Clear back stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    requireActivity().startActivity(intent);
                    requireActivity().finish();
                }
            });
        });
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

    private void inputUserInEdit(User user){
        binding.editName.setText(user.getName());
        binding.editPhone.setText(user.getPhoneNumber());
        binding.editAddress.setText(user.getAddress());
        binding.editEmail.setText(user.getEmail());
    }

    private void onlyVisibleEditText(){
        binding.editText.setFocusable(false);
        binding.editText.setFocusableInTouchMode(false);
        binding.editPhone.setFocusable(false);
        binding.editPhone.setFocusableInTouchMode(false);
        binding.editAddress.setFocusable(false);
        binding.editAddress.setFocusableInTouchMode(false);
        binding.editEmail.setFocusable(false);
        binding.editEmail.setFocusableInTouchMode(false);
    }

}
