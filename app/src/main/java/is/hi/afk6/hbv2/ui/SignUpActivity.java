package is.hi.afk6.hbv2.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.afk6.hbv2.databinding.ActivitySignUpBinding;
import is.hi.afk6.hbv2.entities.ErrorResponse;
import is.hi.afk6.hbv2.entities.ResponseWrapper;
import is.hi.afk6.hbv2.entities.SignUpDTO;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

public class SignUpActivity extends AppCompatActivity
{
    private ActivitySignUpBinding binding;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        userService = new UserServiceImplementation(new APIServiceImplementation());

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
        binding.contentSignUp.signupNameError.setVisibility(View.INVISIBLE);
        binding.contentSignUp.signupSsnError.setVisibility(View.INVISIBLE);
        binding.contentSignUp.signupPhoneNumberError.setVisibility(View.INVISIBLE);
        binding.contentSignUp.signupAddressError.setVisibility(View.INVISIBLE);
        binding.contentSignUp.signupEmailError.setVisibility(View.INVISIBLE);
        binding.contentSignUp.signupPasswordError.setVisibility(View.INVISIBLE);

        SignUpDTO signUpInfo = new SignUpDTO(binding.contentSignUp.signupName.getText().toString(),
                                             binding.contentSignUp.signupSsn.getText().toString(),
                                             binding.contentSignUp.signupPhoneNumber.getText().toString(),
                                             binding.contentSignUp.signupAddress.getText().toString(),
                                             binding.contentSignUp.signupEmail.getText().toString(),
                                             binding.contentSignUp.signupPassword.getText().toString());

        ResponseWrapper<User> returnUser = userService.saveNewUser(signUpInfo);

        ErrorResponse errorResponse = returnUser.getErrorResponse();

        if (errorResponse != null)
        {
            String nameError = errorResponse.getErrorDetails().get("name");
            if (nameError != null)
            {
                binding.contentSignUp.signupNameError.setText(nameError);
                binding.contentSignUp.signupNameError.setVisibility(View.VISIBLE);
            }

            String ssnError = errorResponse.getErrorDetails().get("ssn");
            if (ssnError != null)
            {
                binding.contentSignUp.signupSsnError.setText(ssnError);
                binding.contentSignUp.signupSsnError.setVisibility(View.VISIBLE);
            }

            String phoneError = errorResponse.getErrorDetails().get("phoneNumber");
            if (phoneError != null)
            {
                binding.contentSignUp.signupPhoneNumberError.setText(phoneError);
                binding.contentSignUp.signupPhoneNumberError.setVisibility(View.VISIBLE);
            }

            String addressError = errorResponse.getErrorDetails().get("address");
            if (addressError != null)
            {
                binding.contentSignUp.signupAddressError.setText(addressError);
                binding.contentSignUp.signupAddressError.setVisibility(View.VISIBLE);
            }

            String emailError = errorResponse.getErrorDetails().get("email");
            if (emailError != null)
            {
                binding.contentSignUp.signupEmailError.setText(emailError);
                binding.contentSignUp.signupEmailError.setVisibility(View.VISIBLE);
            }

            String passwordError = errorResponse.getErrorDetails().get("password");
            if (passwordError != null)
            {
                binding.contentSignUp.signupPasswordError.setText(passwordError);
                binding.contentSignUp.signupPasswordError.setVisibility(View.VISIBLE);
            }
            else
            {
                User loggedInUser = returnUser.getData();

                binding.contentSignUp.signupText.setTextColor(Color.BLACK);
                String text = "Velkomin/nn " + loggedInUser.getName();
                binding.contentSignUp.signupText.setText(text);

                Intent intent = new Intent(SignUpActivity.this, UserHomepageActivity.class);

                startActivity(intent);
            }
        }
    }
}
