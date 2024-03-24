package is.hi.afk6.hbv2.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.Arrays;
import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentEditUserBinding;
import is.hi.afk6.hbv2.entities.api.ErrorResponse;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;
import is.hi.afk6.hbv2.ui.LoginActivity;

public class EditUserFragment extends Fragment {
    private UserService userService;
    private User loggedInUser;
    private FragmentEditUserBinding binding;
    private User editedUser;
    private final List<String> role = Arrays.asList("Notandi", "Starfsfólk", "Sjúkraþjálfari", "Kerfisstjóri");
    private Callbacks callbacks;

    // Callback for when the User has been updated.
    public interface Callbacks
    {
        void onUserUpdated(User user);
    }

    @Override
    public void onAttach(@NonNull Context context)
    {
        super.onAttach(context);

        // Make sure the container activity has implemented the callback interface.
        if (!(context instanceof Callbacks))
        {
            throw new ClassCastException(context.toString() + " must implement EditUserFragment.Callbacks");
        }

        callbacks = (Callbacks) context;
    }

    @Override
    public void onDetach()
    {
        super.onDetach();

        callbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        // Get arguments.
        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
            editedUser = getArguments().getParcelable(getString(R.string.edited_user));
        }

        userService = new UserServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        edit_setup();
        if(editedUser.getName().equals(loggedInUser.getName()))
        {
            inputUserInEdit(loggedInUser);
            removeRoleSpinner();
            binding.buttonEditSumbit.setOnClickListener(v -> validateUpdate());
            binding.editDeleteButton.setOnClickListener(v -> deleteUserAlert(loggedInUser));
        } else
        {
            inputUserInEdit(editedUser);
            onlyVisibleEditText();
            setUpRole();
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
        loggedInUser.setSpecialization(binding.editStaffSpecialization.getText().toString());

        userService.updateUser(loggedInUser.getId(), loggedInUser, result -> {
            ErrorResponse errorResponse = result.getErrorResponse();

            requireActivity().runOnUiThread(() ->
            {
                if(errorResponse != null){
                    edit_setup();
                    errorResponse_input(errorResponse);
                } else
                {
                    edit_setup();
                    callbacks.onUserUpdated(loggedInUser);
                }
            });
        });
    }   

    private void changeStaffRole(){
        String desiredDisplayString = role.get(binding.staffRoleSpinner.getSelectedItemPosition());

        UserRole newRole = UserRole.fromDisplayString(desiredDisplayString);
        if (newRole != null) {
            editedUser.setRole(newRole);
        }

        userService.updateUser(loggedInUser.getId(), editedUser, result -> {
            ErrorResponse errorResponse = result.getErrorResponse();

            requireActivity().runOnUiThread(() -> {
                if(errorResponse != null){
                    edit_setup();
                    errorResponse_input(errorResponse);
                }
                else
                {
                    Log.d("Updated user", editedUser.toString());
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
                    navController.navigate(R.id.nav_users_overview, bundle);
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
                    if(editedUser.getName().equals(loggedInUser.getName()))
                    {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        requireActivity().startActivity(intent);
                        requireActivity().finish();
                    } else
                    {
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);

                        Bundle bundle = new Bundle();
                        bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);

                        navController.navigate(R.id.nav_users_overview, bundle);
                    }
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
        if(user.getRole().isElevatedUser()){
            binding.editStaffSpecialization.setText(user.getSpecialization());
        } else {
            binding.editStaffSpecialization.setVisibility(View.GONE);
        }
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
        binding.editStaffSpecialization.setFocusable(false);
        binding.editStaffSpecialization.setFocusableInTouchMode(false);
    }

    private void setUpRole(){
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, role);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.staffRoleSpinner.setAdapter(roleAdapter);
        int userRoleIndex = role.indexOf(editedUser.getRole().getDisplayString());
        binding.staffRoleSpinner.setSelection(userRoleIndex);
    }

    private void removeRoleSpinner(){
        binding.staffRoleSpinner.setVisibility(View.GONE);
    }

}
