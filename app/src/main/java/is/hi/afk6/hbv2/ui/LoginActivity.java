package is.hi.afk6.hbv2.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.afk6.hbv2.databinding.ActivityLoginBinding;
import is.hi.afk6.hbv2.entities.LoginDTO;
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

        userService = new UserServiceImplementation(new APIServiceImplementation());

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.contentLogin.loginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                LoginDTO loginInfo = new LoginDTO(binding.contentLogin.loginEmail.getText().toString(),
                        binding.contentLogin.loginPassword.getText().toString());

                User returnUser = userService.logInUser(loginInfo);

                binding.contentLogin.textLogin.setTextColor(Color.BLACK);
                String text = "Velkomin/nn " + returnUser.getName();
                binding.contentLogin.textLogin.setText(text);

                Intent intent = new Intent(LoginActivity.this, UserHomepageActivity.class);

                startActivity(intent);
            }
        });

        binding.contentLogin.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);

                startActivity(intent);
            }
        });

    }
}
