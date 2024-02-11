package is.hi.afk6.hbv2.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import is.hi.afk6.hbv2.databinding.ActivityUserHomepageBinding;

public class UserHomepageActivity extends AppCompatActivity
{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityUserHomepageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityUserHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
