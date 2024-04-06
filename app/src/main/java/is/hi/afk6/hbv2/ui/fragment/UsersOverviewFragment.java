package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import java.util.List;
import java.util.Objects;
import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.adapters.UsersOverviewAdapter;
import is.hi.afk6.hbv2.adapters.WaitingListRequestReceptionAdapter;
import is.hi.afk6.hbv2.callbacks.UserOverviewCallback;
import is.hi.afk6.hbv2.databinding.FragmentUsersOverviewBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

public class UsersOverviewFragment extends Fragment implements UserOverviewCallback {
    private FragmentUsersOverviewBinding binding;
    private UserService userService;
    private List<User> users;
    private User loggedInUser;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userService = new UserServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());
        if (getArguments() != null) {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentUsersOverviewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        getUsers();

        return view;
    }

    /**
     * Retrieves users from the userService and populates the UI with user data,
     * including buttons for editing user details and viewing questionnaire answers.
     * This method asynchronously fetches user data from the API and updates the UI accordingly.
     * It also handles user interactions such as button clicks for editing and viewing answers.
     */
    public void getUsers(){
        controlView(true, "");
        UsersOverviewFragment that = this;
        userService.getAllUsers(new APICallback<List<User>>() {
            @Override
            public void onComplete(ResponseWrapper<List<User>> result) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(result.getData() != null){
                            controlView(false, "");
                            users = result.getData();

                            UsersOverviewAdapter adapter = new UsersOverviewAdapter(result.getData(), that);
                            binding.usersOverviewRecyclerView.setAdapter(adapter);
                            for (User user : users){
                                if(!Objects.equals(user.getEmail(), loggedInUser.getEmail())){
                                    LinearLayout userContainer = createUserContainer();
                                    TextView userName = createTextView(user);
                                    Button button = createButton();
                                    userContainer.addView(userName);
                                    userContainer.addView(button);

                                    button.setOnClickListener(v ->
                                    {
                                        NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);

                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);
                                        bundle.putParcelable(getString(R.string.edited_user), user);
                                        navController.navigate(R.id.nav_edit_user, bundle);
                                    });
                                    binding.usersContainer.addView(userContainer);
                                }
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

    /**
     * Create a TextView with the users name
     * @param user User that is used
     * @return a TextView with that user
     */
    private TextView createTextView(User user){
        TextView usersName = new TextView(requireContext());
        usersName.setText(user.getName());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        );
        usersName.setLayoutParams(params);

        return usersName;
    }

    /**
     * Create a button that will let admin be able to update/delete
     * User
     * @return the Button
     */
    private Button createButton(){
        Button updateButton = new Button(requireContext());
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

    /**
     * Creates a user layout container, so the TextView and Button will
     * be in the same line
     * @return the LinearLayout container
     */
    private LinearLayout createUserContainer(){
        LinearLayout userContainer = new LinearLayout(requireContext());
        userContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        userContainer.setLayoutParams(layoutParams);
        return userContainer;
    }
}
