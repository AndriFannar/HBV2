package is.hi.afk6.hbv2.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.databinding.ActivitySignUpBinding;
import is.hi.afk6.hbv2.entities.api.ErrorResponse;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.dtos.SignUpDTO;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

/**
 * SignUp Activity for the Application.
 * Signs Up a new User given user information, and saves new User to API.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is
 * @since   08/02/2024
 * @version 1.5
 */
public class SignUpActivity extends AppCompatActivity
{
    private ActivitySignUpBinding binding;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Create a new UserService.
        userService = new UserServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.contentSignUp.signupConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                signUp();
            }
        });
    }

    private void signUp()
    {
        // Display loading & hide errors.
        controlView(true);
        displayErrorsOnUI(false, null);

        // Create a SignUpDTO from user input.
        SignUpDTO signUpInfo = new SignUpDTO(binding.contentSignUp.signupName.getText().toString(),
                binding.contentSignUp.signupSsn.getText().toString(),
                binding.contentSignUp.signupPhoneNumber.getText().toString(),
                binding.contentSignUp.signupAddress.getText().toString(),
                binding.contentSignUp.signupEmail.getText().toString(),
                binding.contentSignUp.signupPassword.getText().toString());

        // Send SignUp info asynchronously to API.
        userService.saveNewUser(signUpInfo, new APICallback<User>() {
            @Override
            public void onComplete(ResponseWrapper<User> result)
            {
                // Get ErrorResponse from ResponseWrapper.
                ErrorResponse errorResponse = result.getErrorResponse();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        controlView(false);

                        if (errorResponse != null)
                            displayErrorsOnUI(true, errorResponse);
                        else
                        {
                            // If no errors, get User from Wrapper.
                            User loggedInUser = result.getData();

                            // Show welcome text.
                            binding.contentSignUp.signupText.setTextColor(Color.BLACK);
                            String text = "Velkomin/nn " + loggedInUser.getName();
                            binding.contentSignUp.signupText.setText(text);

                            // Go to UserHomepage.
                            Intent intent = UserHomepageActivity.newIntent(SignUpActivity.this, loggedInUser);

                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    /**
     * Displays errors on SignUp UI.
     *
     * @param display       Display or hide errors.
     * @param errorResponse Errors to display.
     */
    private void displayErrorsOnUI (boolean display, ErrorResponse errorResponse)
    {
        if (display)
        {
            // If ErrorResponse is not null, display relevant errors.
            String nameError = errorResponse.getErrorDetails().get("name");
            if (nameError != null) {
                binding.contentSignUp.signupNameError.setText(nameError);
                binding.contentSignUp.signupNameError.setVisibility(View.VISIBLE);
            }

            String ssnError = errorResponse.getErrorDetails().get("ssn");
            if (ssnError != null) {
                binding.contentSignUp.signupSsnError.setText(ssnError);
                binding.contentSignUp.signupSsnError.setVisibility(View.VISIBLE);
            }

            String phoneError = errorResponse.getErrorDetails().get("phoneNumber");
            if (phoneError != null) {
                binding.contentSignUp.signupPhoneNumberError.setText(phoneError);
                binding.contentSignUp.signupPhoneNumberError.setVisibility(View.VISIBLE);
            }

            String addressError = errorResponse.getErrorDetails().get("address");
            if (addressError != null) {
                binding.contentSignUp.signupAddressError.setText(addressError);
                binding.contentSignUp.signupAddressError.setVisibility(View.VISIBLE);
            }

            String emailError = errorResponse.getErrorDetails().get("email");
            if (emailError != null) {
                binding.contentSignUp.signupEmailError.setText(emailError);
                binding.contentSignUp.signupEmailError.setVisibility(View.VISIBLE);
            }

            String passwordError = errorResponse.getErrorDetails().get("password");
            if (passwordError != null) {
                binding.contentSignUp.signupPasswordError.setText(passwordError);
                binding.contentSignUp.signupPasswordError.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            binding.contentSignUp.signupNameError.setVisibility(View.INVISIBLE);
            binding.contentSignUp.signupSsnError.setVisibility(View.INVISIBLE);
            binding.contentSignUp.signupPhoneNumberError.setVisibility(View.INVISIBLE);
            binding.contentSignUp.signupAddressError.setVisibility(View.INVISIBLE);
            binding.contentSignUp.signupEmailError.setVisibility(View.INVISIBLE);
            binding.contentSignUp.signupPasswordError.setVisibility(View.INVISIBLE);
        }
    }


    /**
     * Controls some parts of the view of the signUp Activity.
     *
     * @param loading Is there data being actively fetched?
     */
    private void controlView(boolean loading)
    {
        if (loading)
        {
            binding.contentSignUp.progressBar.setVisibility(View.VISIBLE);
            binding.contentSignUp.signupConfirm.setAlpha(0.7f);
        }
        else
        {
            binding.contentSignUp.progressBar.setVisibility(View.GONE);
            binding.contentSignUp.signupConfirm.setAlpha(1f);
        }

        binding.contentSignUp.signupConfirm.setClickable(!loading);
        binding.contentSignUp.signupName.setFocusableInTouchMode(!loading);
        binding.contentSignUp.signupSsn.setFocusableInTouchMode(!loading);
        binding.contentSignUp.signupPhoneNumber.setFocusableInTouchMode(!loading);
        binding.contentSignUp.signupAddress.setFocusableInTouchMode(!loading);
        binding.contentSignUp.signupEmail.setFocusableInTouchMode(!loading);
        binding.contentSignUp.signupPassword.setFocusableInTouchMode(!loading);
    }
}


