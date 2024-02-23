package is.hi.afk6.hbv2.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.databinding.ActivityUsersOverviewBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

public class UsersOverviewActivity extends Activity {

    private ActivityUsersOverviewBinding binding;
    private UserService userService;
    public static final String LOGGED_IN_USER = "loggedInUser";
    private List<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userService = new UserServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());

        binding = ActivityUsersOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getUsers();

    }

    public static Intent newIntent(Context packageContext, User loggedInUser)
    {
        Intent intent = new Intent(packageContext, UsersOverviewActivity.class);
        intent.putExtra(LOGGED_IN_USER, loggedInUser);
        return intent;
    }

    public void getUsers(){
        controlView(true, "");
        Log.d("TAG", "GET USER");
        userService.getAllUsers(new APICallback<List<User>>() {
            @Override
            public void onComplete(ResponseWrapper<List<User>> result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result.getData() != null){
                            Log.d("Tag", result.getData().toString());
                            controlView(false, "");
                            users = result.getData();

                            for (User user : users){
                                LinearLayout userContainer = createUserContainer();
                                TextView userName = createTextView(user);
                                Button button = createButton();
                                userContainer.addView(userName);
                                userContainer.addView(button);
                                button.setOnClickListener(v -> Log.d("TAG", "TEST button"));

                                binding.usersContainer.addView(userContainer);
                            }
                        } else {
                            String error = result.getErrorResponse().getErrorDetails().get("user");
                            controlView(false, error);
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

    private TextView createTextView(User user){
        TextView usersName = new TextView(UsersOverviewActivity.this);
        usersName.setText(user.getName());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        usersName.setLayoutParams(params);

        return usersName;
    }

    private Button createButton(){
        Button updateButton = new Button(UsersOverviewActivity.this);
        String BUTTON_TEXT = "Uppfæra";
        updateButton.setText(BUTTON_TEXT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        updateButton.setLayoutParams(params);

        return updateButton;
    }

    private LinearLayout createUserContainer(){
        LinearLayout userContainer = new LinearLayout(UsersOverviewActivity.this);
        userContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        userContainer.setLayoutParams(layoutParams);
        return userContainer;
    }
}
