package is.hi.afk6.hbv2.ui.fragment;

import static is.hi.afk6.hbv2.ui.UserHomepageActivity.LOGGED_IN_USER;
import static is.hi.afk6.hbv2.ui.UserHomepageActivity.WAITING_LIST_REQUEST;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentDualHomepageBinding;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;

public class DualHomepageFragment extends Fragment
{
    private FragmentDualHomepageBinding binding;
    private User loggedInUser;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            loggedInUser = getArguments().getParcelable(LOGGED_IN_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        binding = FragmentDualHomepageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);
        navController.navigate(R.id.nav_user_fragment);

        /*if (loggedInUser != null)
        {
            test = (NavHostFragment) getChildFragmentManager().findFragmentById(R.id.user_fragment_detail);
            navController = test.getNavController();

            if (loggedInUser.getRole() == UserRole.USER)
            {
                if (loggedInUser.getWaitingListRequestID() == 0)
                {
                    navController.navigate(R.id.nav_create_waiting_list_request);
                }
                else
                {
                    navController.navigate(R.id.nav_waiting_list_request);
                }
            }
        }*/

        return view;
    }

    private void fragmentTransition(int container, Fragment fragment)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(LOGGED_IN_USER, loggedInUser);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
