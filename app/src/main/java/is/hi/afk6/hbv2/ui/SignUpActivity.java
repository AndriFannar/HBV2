package is.hi.afk6.hbv2.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.afk6.hbv2.databinding.ActivitySignUpBinding;
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
                SignUpDTO signUpInfo = new SignUpDTO(binding.contentSignUp.signupName.getText().toString(), binding.contentSignUp.signupSsn.getText().toString(), binding.contentSignUp.signupPhoneNumber.getText().toString(), binding.contentSignUp.signupAddress.getText().toString(), binding.contentSignUp.signupEmail.getText().toString(), binding.contentSignUp.signupPassword.getText().toString());

                User returnUser = userService.saveNewUser(signUpInfo);

                binding.contentSignUp.signupText.setTextColor(Color.BLACK);
                String text = "Velkomin/nn " + returnUser.getName();
                binding.contentSignUp.signupText.setText(text);

                Intent intent = new Intent(SignUpActivity.this, UserHomepageActivity.class);

                startActivity(intent);
            }
        });
    }
}
