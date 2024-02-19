package is.hi.afk6.hbv2.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.databinding.ActivityLoginBinding;
import is.hi.afk6.hbv2.entities.ErrorResponse;
import is.hi.afk6.hbv2.entities.LoginDTO;
import is.hi.afk6.hbv2.entities.ResponseWrapper;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.callbacks.APICallback;
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
        userService = new UserServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.contentLogin.loginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Log.println(Log.INFO, "login", "Executing onClick");
                // Hide errors.
                binding.contentLogin.loginError.setVisibility(View.INVISIBLE);
                binding.contentLogin.progressBar.setVisibility(View.VISIBLE);
                binding.contentLogin.loginConfirm.setAlpha(0.7f);
                binding.contentLogin.loginConfirm.setClickable(false);
                binding.contentLogin.signup.setClickable(false);
                binding.contentLogin.loginEmail.setFocusable(false);

                // Create a LogInDTO object from user input.
                LoginDTO loginInfo = new LoginDTO(binding.contentLogin.loginEmail.getText().toString(),
                                                  binding.contentLogin.loginPassword.getText().toString());

                // Try logging User in, wait for a response from API.
                ResponseWrapper<User> returnUser = null;
                userService.logInUser(loginInfo, new APICallback<User>() {
                    @Override
                    public void onComplete(ResponseWrapper<User> result)
                    {
                        Log.println(Log.INFO, "login", "Data: " + result.getData());
                        Log.println(Log.INFO, "login", "Error: " + result.getErrorResponse());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                Log.println(Log.INFO, "login", "Running on ui...");
                                binding.contentLogin.progressBar.setVisibility(View.GONE);
                                binding.contentLogin.loginConfirm.setAlpha(1f);
                                binding.contentLogin.loginConfirm.setClickable(true);
                                binding.contentLogin.signup.setClickable(true);
                                if (result.getData() != null)
                                {
                                    // If no error, get the User from the Wrapper.
                                    User loggedInUser = result.getData();

                                    // Show a welcome message.
                                    binding.contentLogin.textLogin.setTextColor(Color.BLACK);
                                    String text = "Velkomin/nn " + loggedInUser.getName();
                                    binding.contentLogin.textLogin.setText(text);

                                    //Switch to UserHomepage.
                                    Intent intent = UserHomepageActivity.newIntent(LoginActivity.this, loggedInUser);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Log.println(Log.INFO, "login", "Displaying errors");
                                    // Show error if it exists.
                                    String error = result.getErrorResponse().getErrorDetails().get("login");
                                    Log.println(Log.INFO, "login", "Error is: " + error);
                                    binding.contentLogin.loginError.setText(error);
                                    binding.contentLogin.loginError.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                });
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
