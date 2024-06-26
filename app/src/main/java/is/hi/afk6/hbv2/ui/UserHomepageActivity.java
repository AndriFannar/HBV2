package is.hi.afk6.hbv2.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.ActivityUserHomepageBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.ui.fragment.user.EditUserFragment;

public class UserHomepageActivity extends AppCompatActivity implements EditUserFragment.Callbacks, NavigationView.OnNavigationItemSelectedListener {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityUserHomepageBinding binding;
    private User loggedInUser;
    private User editedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DrawerLayout drawer = binding.mainDrawerLayout;
        NavigationView navigationView = binding.mainNav;

        loggedInUser = (User) getIntent().getParcelableExtra(getString(R.string.logged_in_user));

        if (loggedInUser.getRole() == UserRole.USER) {
            binding.mainNav.getMenu().findItem(R.id.nav_waiting_list_overview).setVisible(false);
            binding.mainNav.getMenu().findItem(R.id.nav_users_overview).setVisible(false);
            binding.mainNav.getMenu().findItem(R.id.nav_questionnaire_overview).setVisible(false);

            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_create_waiting_list_request, R.id.nav_waiting_list_request, R.id.nav_user_fragment)
                    .setOpenableLayout(drawer)
                    .build();
        }
        else if (loggedInUser.getRole().isStaffMember())
        {
            binding.mainNav.getMenu().findItem(R.id.nav_waiting_list_request).setVisible(false);

            if (loggedInUser.getRole() != UserRole.ADMIN)
            {
                binding.mainNav.getMenu().findItem(R.id.nav_users_overview).setVisible(false);
                binding.mainNav.getMenu().findItem(R.id.nav_questionnaire_overview).setVisible(false);
            }

            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_waiting_list_overview, R.id.nav_users_overview, R.id.nav_user_fragment, R.id.nav_questionnaire_overview)
                    .setOpenableLayout(drawer)
                    .build();
        }

        binding.mainNav.getHeaderView(0).findViewById(R.id.nav_logout).setOnClickListener(v ->
        {
            logout();
        });

        NavController navController = Navigation.findNavController(this, R.id.super_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        onUserUpdated(loggedInUser);

        navigate(navController);
    }

    /**
     * Creates a new Intent to this Activity.
     *
     * @param packageContext      Activity coming from.
     * @param bundleExtraLoggedIn String to associate with the logged in User.
     * @param loggedInUser        User to be displayed on homepage.
     * @param bundleExtraEdited   String to associate with the edited User.
     * @param editedUser          Edited User.
     * @return Intent to this Activity.
     */
    public static Intent newIntent(Context packageContext, String bundleExtraLoggedIn, User loggedInUser, String bundleExtraEdited, User editedUser) {
        Intent intent = new Intent(packageContext, UserHomepageActivity.class);
        intent.putExtra(bundleExtraLoggedIn, loggedInUser);
        intent.putExtra(bundleExtraEdited, editedUser);
        return intent;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.super_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        NavController navController = Navigation.findNavController(this, R.id.super_fragment);

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);

        if (loggedInUser.getRole() != UserRole.ADMIN || editedUser == null)
            bundle.putParcelable(getString(R.string.edited_user), loggedInUser);
        else
            bundle.putParcelable(getString(R.string.edited_user), editedUser);

        int itemId = menuItem.getItemId();

        navController.navigate(itemId, bundle);

        return true;
    }

    /**
     * Navigates to the correct Fragment based on the User's Role.
     *
     * @param navController NavController to navigate with.
     */
    private void navigate(NavController navController) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);

        if (loggedInUser.getRole() == UserRole.USER)
        {
            if (loggedInUser.getWaitingListRequestID() != null && loggedInUser.getWaitingListRequestID() != 0) {
                navController.navigate(R.id.nav_waiting_list_request, bundle);
            } else {
                navController.navigate(R.id.nav_create_waiting_list_request, bundle);
            }
        }
        else if (loggedInUser.getRole().isStaffMember())
        {
            navController.navigate(R.id.nav_waiting_list_overview, bundle);
        }
    }

    /**
     * Callback for when a User has been updated.
     *
     * @param user Updated User.
     */
    @Override
    public void onUserUpdated(User user) {
        // Update the text in the Navigation with the new info.
        View headerView = binding.mainNav.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.nav_username);
        TextView email = headerView.findViewById(R.id.nav_email);
        username.setText(loggedInUser.getName());
        email.setText(loggedInUser.getEmail());
    }

    private void logout(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}