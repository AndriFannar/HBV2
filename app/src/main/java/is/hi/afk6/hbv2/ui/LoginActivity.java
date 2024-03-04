package is.hi.afk6.hbv2.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.ActivityLoginBinding;
import is.hi.afk6.hbv2.entities.dtos.LoginDTO;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

/**
 * Login Activity for the Application.
 * Logs in a User given credentials that exist in API.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is
 * @since   08/02/2024
 * @version 1.5
 */
public class LoginActivity extends AppCompatActivity
{
    private ActivityLoginBinding binding;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Create a UserService.
        userService = new UserServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.contentLogin.loginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                logIn();
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

    /**
     * Performs actions for when a User presses the login button.
     */
    private void logIn()
    {
        // Hide errors and display loading.
        controlView(true, "");

        // Create a LogInDTO object from user input.
        LoginDTO loginInfo = new LoginDTO(binding.contentLogin.loginEmail.getText().toString(),
                binding.contentLogin.loginPassword.getText().toString());

        // Try logging User in, send asynchronous call to API.
        userService.logInUser(loginInfo, new APICallback<User>() {
            @Override
            public void onComplete(ResponseWrapper<User> result)
            {
                // Run on UI thread so view can be updated.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        // If data is not null, a User was successfully found.
                        if (result.getData() != null)
                        {
                            // Stop displaying loading
                            controlView(false, "");

                            // If no error, get the User from the Wrapper.
                            User loggedInUser = result.getData();

                            // Show a welcome message.
                            binding.contentLogin.textLogin.setTextColor(Color.BLACK);
                            String text = "Velkomin/nn " + loggedInUser.getName();
                            binding.contentLogin.textLogin.setText(text);

                            //Switch to UserHomepage.
                            Intent intent = UserHomepageActivity.newIntent(LoginActivity.this, getString(R.string.logged_in_user), loggedInUser, null, null);
                            startActivity(intent);
                        }
                        else
                        {
                            // Show error if User was not found.
                            String error = result.getErrorResponse().getErrorDetails().get("login");
                            controlView(false, error);
                        }
                    }
                });
            }
        });
    }

    /**
     * Controls many parts of the view of the login Activity.
     *
     * @param loading Is there data being actively fetched?
     * @param error   Error to display on UI, if any.
     */
    private void controlView(boolean loading, String error)
    {
        if (loading)
        {
            binding.contentLogin.loginError.setVisibility(View.INVISIBLE);
            binding.contentLogin.progressBar.setVisibility(View.VISIBLE);
            binding.contentLogin.loginConfirm.setAlpha(0.7f);
        }
        else
        {
            binding.contentLogin.progressBar.setVisibility(View.GONE);
            binding.contentLogin.loginConfirm.setAlpha(1f);

            if (!error.isEmpty())
            {
                binding.contentLogin.loginError.setText(error);
                binding.contentLogin.loginError.setVisibility(View.VISIBLE);
            }
        }

        binding.contentLogin.loginConfirm.setClickable(!loading);
        binding.contentLogin.signup.setClickable(!loading);
        binding.contentLogin.loginEmail.setFocusableInTouchMode(!loading);
        binding.contentLogin.loginPassword.setFocusableInTouchMode(!loading);
    }
}
