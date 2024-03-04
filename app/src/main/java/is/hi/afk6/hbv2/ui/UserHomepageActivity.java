package is.hi.afk6.hbv2.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.ActivityUserHomepageBinding;
import is.hi.afk6.hbv2.entities.User;

public class UserHomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityUserHomepageBinding binding;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityUserHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.mainDrawerLayout;
        NavigationView navigationView = binding.mainNav;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_create_waiting_list_request, R.id.nav_edit_user)
                .setOpenableLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.super_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        loggedInUser = (User) getIntent().getParcelableExtra(getString(R.string.logged_in_user));
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);

        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.nav_username);
        TextView email = headerView.findViewById(R.id.nav_email);
        username.setText(loggedInUser.getName());
        email.setText(loggedInUser.getEmail());

        if (loggedInUser.getWaitingListRequestID() != null && loggedInUser.getWaitingListRequestID() != 0)
        {
            navController.navigate(R.id.nav_waiting_list_request, bundle);
        }
        else
        {
            navController.navigate(R.id.nav_create_waiting_list_request, bundle);
        }
    }

    /**
     * Creates a new Intent to this Activity.
     *
     * @param packageContext  Activity coming from.
     * @param bundleExtraName String to associate with the bundle extra.
     * @param loggedInUser    User to be displayed on homepage.
     * @return                Intent to this Activity.
     */
    public static Intent newIntent(Context packageContext, String bundleExtraName, User loggedInUser)
    {
        Intent intent = new Intent(packageContext, UserHomepageActivity.class);
        intent.putExtra(bundleExtraName, loggedInUser);
        return intent;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.super_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        NavController navController = Navigation.findNavController(this, R.id.super_fragment);

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
        Log.d("TAG", "onNavigationItemSelected: " + menuItem.getItemId());

        navController.navigate(menuItem.getItemId(), bundle);

        return true;
    }
}
