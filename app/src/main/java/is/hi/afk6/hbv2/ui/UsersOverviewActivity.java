package is.hi.afk6.hbv2.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.ActivityUsersOverviewBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.services.UserService;

public class UsersOverviewActivity extends Activity {

    private ActivityUsersOverviewBinding binding;
    private UserService userService;
    public static final String LOGGED_IN_USER = "loggedInUser";
    private List<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getUsers();
    }

    public static Intent newIntent(Context packageContext, User loggedInUser)
    {
        Intent intent = new Intent(packageContext, UserHomepageActivity.class);
        intent.putExtra(LOGGED_IN_USER, loggedInUser);
        return intent;
    }

    public void getUsers(){
        controlView(true, "");

        userService.getAllUsers(new APICallback<List<User>>() {
            @Override
            public void onComplete(ResponseWrapper<List<User>> result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result.getData() != null){
                            controlView(false, "");
                            users = result.getData();

                            for (User user : users){
                                Log.d("TAG", user.getName());
                            }
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
            binding.usersError.setVisibility(View.INVISIBLE);
            binding.usersProgressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.usersProgressBar.setVisibility(View.GONE);

            if (!error.isEmpty())
            {
                binding.usersError.setText(error);
                binding.usersError.setVisibility(View.VISIBLE);
            }
        }
    }
}
