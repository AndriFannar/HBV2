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

        UserFragment userFragment = new UserFragment();
        fragmentTransition(R.id.edit_fragment_container_view, userFragment);

        if (loggedInUser.getRole() == UserRole.USER)
        {
            if (loggedInUser.getWaitingListRequestID() == 0)
            {
                CreateWaitingListRequestFragment createFragment = new CreateWaitingListRequestFragment();
                fragmentTransition(R.id.user_fragment_detail, createFragment);
            }
            else
            {
                Log.d("RequestID", "" + loggedInUser.getId());
                WaitingListRequestFragment waitingListRequestFragment = new WaitingListRequestFragment();
                fragmentTransition(R.id.user_fragment_detail, waitingListRequestFragment);
            }
        }

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
