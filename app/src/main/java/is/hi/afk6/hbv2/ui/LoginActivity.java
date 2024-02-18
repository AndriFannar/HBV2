package is.hi.afk6.hbv2.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

import is.hi.afk6.hbv2.databinding.ActivityLoginBinding;
import is.hi.afk6.hbv2.entities.ErrorResponse;
import is.hi.afk6.hbv2.entities.LoginDTO;
import is.hi.afk6.hbv2.entities.ResponseWrapper;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

public class LoginActivity extends AppCompatActivity
{
    private ActivityLoginBinding binding;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Create a UserService.
        userService = new UserServiceImplementation(new APIServiceImplementation());

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.contentLogin.loginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Hide errors.
                binding.contentLogin.loginError.setVisibility(View.INVISIBLE);

                // Create a LogInDTO object from user input.
                LoginDTO loginInfo = new LoginDTO(binding.contentLogin.loginEmail.getText().toString(),
                                                  binding.contentLogin.loginPassword.getText().toString());

                // Try logging User in, wait for a response from API.
                ResponseWrapper<User> returnUser = null;
                try {
                    returnUser = userService.logInUser(loginInfo).get();
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // Get the ErrorResponse from the Wrapper.
                ErrorResponse errorResponse = returnUser.getErrorResponse();

                // Check if the ErrorResponse contains an error.
                if (errorResponse != null)
                {
                    // Show error if it exists.
                    String error = errorResponse.getErrorDetails().get("login");
                    binding.contentLogin.loginError.setText(error);
                    binding.contentLogin.loginError.setVisibility(View.VISIBLE);
                }
                else
                {
                    // If no error, get the User from the Wrapper.
                    User loggedInUser = returnUser.getData();

                    // Show a welcome message.
                    binding.contentLogin.textLogin.setTextColor(Color.BLACK);
                    String text = "Velkomin/nn " + loggedInUser.getName();
                    binding.contentLogin.textLogin.setText(text);

                    //Switch to UserHomepage.
                    Intent intent = UserHomepageActivity.newIntent(LoginActivity.this, loggedInUser);
                    startActivity(intent);
                }
            }
        });

        binding.contentLogin.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Switch to SignUp page.
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);

                startActivity(intent);
            }
        });

    }
}
