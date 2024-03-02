package is.hi.afk6.hbv2.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ui.AppBarConfiguration;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.ActivityUserHomepageBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.ui.fragment.CreateWaitingListRequestFragment;
import is.hi.afk6.hbv2.ui.fragment.DualHomepageFragment;
import is.hi.afk6.hbv2.ui.fragment.UserFragment;

public class UserHomepageActivity extends FragmentActivity
{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityUserHomepageBinding binding;
    public static final String LOGGED_IN_USER = "loggedInUser";
    public static final String WAITING_LIST_REQUEST = "wlrequest";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityUserHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        User loggedInUser = (User) getIntent().getParcelableExtra(LOGGED_IN_USER);

        DualHomepageFragment dualHomepageFragment = new DualHomepageFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(LOGGED_IN_USER, loggedInUser);
        dualHomepageFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.super_fragment, dualHomepageFragment).commit();
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
}
