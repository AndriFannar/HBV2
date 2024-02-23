package is.hi.afk6.hbv2.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.ActivityUserHomepageBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.ui.fragment.UserFragment;

public class UserHomepageActivity extends AppCompatActivity
{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityUserHomepageBinding binding;
    public static final String LOGGED_IN_USER = "loggedInUser";
    public static final String EDITED_USER = "editedUser";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityUserHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        User loggedInUser = getIntent().getParcelableExtra(LOGGED_IN_USER);

        assert loggedInUser != null;
        checkIfAdmin(loggedInUser);

        if(savedInstanceState == null)
        {
            UserFragment userFragment = new UserFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(LOGGED_IN_USER, loggedInUser);
            bundle.putParcelable(EDITED_USER, loggedInUser);
            userFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.edit_fragment_container_view, userFragment, null)
                    .commit();

            binding.adminSeeAllUsers.setOnClickListener(v -> seeAll(loggedInUser));
        }
    }

    /**
     * Creates a new Intent to this Activity.
     *
     * @param packageContext Activity coming from.
     * @param loggedInUser   User to be displayed on homepage.
     * @return               Intent to this Activity.
     */
    public static Intent newIntent(Context packageContext, User loggedInUser)
    {
        Intent intent = new Intent(packageContext, UserHomepageActivity.class);
        intent.putExtra(LOGGED_IN_USER, loggedInUser);
        return intent;
    }

    private void checkIfAdmin(User loggedInUser ){
        if(loggedInUser.getRole() == UserRole.ADMIN){
            binding.adminSeeAllUsers.setVisibility(View.VISIBLE);
        } else {
            binding.adminSeeAllUsers.setVisibility(View.INVISIBLE);
        }
    }

    private void seeAll(User loggedInUser){
        Intent intent = UsersOverviewActivity.newIntent(UserHomepageActivity.this, loggedInUser);
        startActivity(intent);
    }
}
