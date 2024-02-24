package is.hi.afk6.hbv2.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.ActivityUserHomepageBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.ui.fragment.EditUserFragment;
import is.hi.afk6.hbv2.ui.fragment.UserFragment;

public class UserHomepageActivity extends AppCompatActivity
{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityUserHomepageBinding binding;
    public static final String LOGGED_IN_USER = "loggedInUser";
    public static final String EDITED_USER = "editedUser";
    private User loggedInUser;
    private User editedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityUserHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loggedInUser = getIntent().getParcelableExtra(LOGGED_IN_USER);
        editedUser = getIntent().getParcelableExtra(EDITED_USER);

        assert loggedInUser != null;
        checkIfAdmin();

        if(savedInstanceState == null)
        {
            if(editedUser != null){
                loadFragment(new EditUserFragment(), editedUser);
                binding.adminSeeAllUsers.setVisibility(View.GONE);
            } else {
                loadFragment(new UserFragment(), loggedInUser);
                binding.adminSeeAllUsers.setOnClickListener(v -> seeAll());
            }
        }
    }

    /**
     * Creates a new Intent to this Activity.
     *
     * @param packageContext Activity coming from.
     * @param loggedInUser   User to be displayed on homepage.
     * @return               Intent to this Activity.
     */
    public static Intent newIntent(Context packageContext, User loggedInUser, User editedUser)
    {
        Intent intent = new Intent(packageContext, UserHomepageActivity.class);
        intent.putExtra(LOGGED_IN_USER, loggedInUser);
        intent.putExtra(EDITED_USER, editedUser);
        return intent;
    }

    /**
     * Create the approriate fragment depending on what Parcelable user it will be
     * @param fragment Fragment that will be shown
     * @param user User that will be used for EDITED_USER
     */
    private void loadFragment(Fragment fragment, Parcelable user){
        Bundle bundle = new Bundle();
        bundle.putParcelable(LOGGED_IN_USER, loggedInUser);
        bundle.putParcelable(EDITED_USER, user);
        fragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.edit_fragment_container_view, fragment, null)
                .commit();
    }

    /**
     * Checks if the logged in user is an admin or not
     */
    private void checkIfAdmin( ){
        if(loggedInUser.getRole() == UserRole.ADMIN){
            binding.adminSeeAllUsers.setVisibility(View.VISIBLE);
        } else {
            binding.adminSeeAllUsers.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Creates a new intent to an page with overview over all users
     */
    private void seeAll(){
        Intent intent = UsersOverviewActivity.newIntent(UserHomepageActivity.this, loggedInUser);
        startActivity(intent);
    }
}