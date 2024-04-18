package is.hi.afk6.hbv2.ui.fragment.waitingListRequest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.databinding.FragmentCreateWaitingListRequestBinding;
import is.hi.afk6.hbv2.entities.Questionnaire;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.WaitingListRequest;
import is.hi.afk6.hbv2.callbacks.APICallback;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.enums.UserRole;
import is.hi.afk6.hbv2.networking.APIService;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.QuestionnaireService;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.WaitingListService;
import is.hi.afk6.hbv2.services.implementation.QuestionnaireServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.WaitingListServiceImplementation;

/**
 * Fragment to register to Waiting List.
 * Creates a new WaitingListRequest for logged in User.
 *
 * @author Andri Fannar Kristjánsson, afk6@hi.is
 * @since 21/02/2024
 * @version 1.0
 */
public class CreateWaitingListRequestFragment extends Fragment {
    private FragmentCreateWaitingListRequestBinding binding;
    private User loggedInUser;
    private UserService userService;
    private WaitingListService waitingListService;
    private QuestionnaireService questionnaireService;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private List<User> staff;
    private List<Questionnaire> displayQuestionnaires;

    // Asks for Location Service permission.
    // If use of Location Services is allowed, then get last known location, otherwise have no special order.
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isGranted) {
                    if (isGranted)
                        getLastKnownLocation();
                    else
                    {
                        getListsFromAPI(null);
                    }
                }
            }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            loggedInUser = getArguments().getParcelable(getString(R.string.logged_in_user));
        }

        APIService apiService = new APIServiceImplementation();

        userService = new UserServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        waitingListService = new WaitingListServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());
        questionnaireService = new QuestionnaireServiceImplementation(apiService, HBV2Application.getInstance().getExecutor());

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastKnownLocation();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateWaitingListRequestBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        controlView(true, false, false);

        return view;
    }

    /**
     * Fetch the lists of Questionnaires and Physiotherapists from the API.
     *
     * @param currentLocation User's current location, if Location Services have been enabled.
     */
    private void getListsFromAPI(Location currentLocation)
    {
        // Fetch Questionnaires to show on form.
        questionnaireService.getQuestionnairesOnForm(new APICallback<List<Questionnaire>>() {
            @Override
            public void onComplete(ResponseWrapper<List<Questionnaire>> result)
            {
                displayQuestionnaires = result.getData();

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setUpView();
                    }
                });
            }
        });

        // Fetch physiotherapists from API and order by location if Location Services are enabled.
        if (currentLocation != null)
        {
            userService.getUsersByRole(UserRole.PHYSIOTHERAPIST, true, currentLocation, requireContext(), new APICallback<List<User>>() {

                @Override
                public void onComplete(ResponseWrapper<List<User>> result) {
                    staff = result.getData();
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUpView();
                        }
                    });
                }
            });
        }
        else
        {
            userService.getUsersByRole(UserRole.PHYSIOTHERAPIST, true, new APICallback<List<User>>() {
                @Override
                public void onComplete(ResponseWrapper<List<User>> result) {
                    staff = result.getData();
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUpView();
                        }
                    });
                }
            });
        }
    }


    /**
     * Set up the registration view and populates spinners with staff and questionnaires.
     */
    private void setUpView()
    {
        if (staff != null && displayQuestionnaires != null) {
            List<String> physiotherapists = new ArrayList<>();
            List<String> questionnaires = new ArrayList<>();

            // Set the format for the distance.
            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            decimalFormat.setRoundingMode(RoundingMode.CEILING);

            for (User display : staff) {
                // Insert name of Physiotherapist and their specialization.
                StringBuilder sb = new StringBuilder(display.getName() + " - " + display.getSpecialization());

                // If the distance is greater than 0 (distance was calculated), then add the distance to the end of the string.
                if (display.getDistance() > 0)
                    sb.append(" - ").append(decimalFormat.format(display.getDistance())).append(" km");

                // Add to the list.
                physiotherapists.add(sb.toString());
            }

            // For each Questionnaire, add its name to the list.
            for (Questionnaire questionnaire : displayQuestionnaires) {
                questionnaires.add(questionnaire.getName());
            }

            // Create an ArrayAdapter from the lists to insert into the spinners.
            ArrayAdapter<String> physioAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, physiotherapists);
            ArrayAdapter<String> questionnaireAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, questionnaires);

            physioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            questionnaireAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            binding.staffSpinner.setAdapter(physioAdapter);
            binding.questionnaireSpinner.setAdapter(questionnaireAdapter);

            binding.buttonRegisterConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    register(staff, displayQuestionnaires);
                }
            });

            controlView(false, false, false);
        }
    }

    /**
     * Register User to the Waiting List
     *
     * @param staff                 List of physiotherapists available.
     * @param displayQuestionnaires List of Questionnaires to display.
     */
    private void register(List<User> staff, List<Questionnaire> displayQuestionnaires) {
        // If there is no description, don't continue.
        if (binding.waitingListInfo.getText().toString().isEmpty())
        {
            controlView(false, false, true);
            return;
        }

        controlView(false, true, false);

        // Create a new WaitingListRequest from the info.
        WaitingListRequest request = new WaitingListRequest(
                loggedInUser,
                staff.get(binding.staffSpinner.getSelectedItemPosition()),
                binding.waitingListInfo.getText().toString(),
                displayQuestionnaires.get(binding.questionnaireSpinner.getSelectedItemPosition())
        );

        // Save the new WaitingListRequest to the API.
        waitingListService.saveNewWaitingListRequest(request, new APICallback<WaitingListRequest>() {
            @Override
            public void onComplete(ResponseWrapper<WaitingListRequest> result) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.getData() != null)
                        {
                            // If the request was successful, then navigate to the WaitingListRequestFragment.
                            controlView(false, false, false);

                            Bundle bundle = new Bundle();
                            bundle.putParcelable(getString(R.string.waiting_list_request), result.getData());

                            loggedInUser.setWaitingListRequestID(result.getData().getId());
                            bundle.putParcelable(getString(R.string.logged_in_user), loggedInUser);

                            NavController navController = Navigation.findNavController(requireActivity(), R.id.super_fragment);
                            navController.popBackStack(R.id.nav_create_waiting_list_request, true);
                            navController.navigate(R.id.nav_waiting_list_request, bundle);
                        } else {
                            controlView(false, false, true);
                        }
                    }
                });
            }
        });
    }

    /**
     * Displays loading when sending in a request.
     *
     * @param fetching    Is there data being actively fetched?
     * @param registering Is there a request being registered?
     * @param error       Display error on UI.
     */
    private void controlView(boolean fetching, boolean registering, boolean error)
    {
        boolean loading = fetching || registering;

        if (loading)
        {
            binding.requestError.setVisibility(View.INVISIBLE);
            binding.buttonRegisterConfirm.setAlpha(0.7f);

            if (registering)
                binding.sendRequestProgressBar.setVisibility(View.VISIBLE);
            else
            {
                binding.staffSpinner.setAlpha(0.7f);
                binding.questionnaireSpinner.setAlpha(0.7f);

                binding.staffSpinnerProgressBar.setVisibility(View.VISIBLE);
                binding.questionnaireSpinnerProgressBar.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            binding.sendRequestProgressBar.setVisibility(View.GONE);
            binding.buttonRegisterConfirm.setAlpha(1f);

            binding.staffSpinner.setAlpha(1f);
            binding.questionnaireSpinner.setAlpha(1f);
            binding.staffSpinnerProgressBar.setVisibility(View.GONE);
            binding.questionnaireSpinnerProgressBar.setVisibility(View.GONE);

            if (error)
                binding.requestError.setVisibility(View.VISIBLE);
        }

        binding.buttonRegisterConfirm.setClickable(!loading);
        binding.staffSpinner.setClickable(!loading);
        binding.questionnaireSpinner.setClickable(!loading);
        binding.waitingListInfo.setFocusableInTouchMode(!loading);
    }


    /**
     * Get last known location of User if User has enabled GeoLocation.
     */
    private void getLastKnownLocation()
    {
        // Initialize currentLocation variable and get the LocationProvider.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Build the LocationRequest.
        LocationRequest locationRequest =
                new LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        10000
                ).build();

        // Create a LocationCallback to get the last known location.
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull com.google.android.gms.location.LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult.getLastLocation() != null)
                {
                    // After getting one location update, remove the callback.
                    // The User's location is only needed once.
                    fusedLocationProviderClient.removeLocationUpdates(this);

                    // Fetch the lists from the API, and send the User's current location.
                    // List of Physiotherapists will then be ordered by distance from User.
                    getListsFromAPI(locationResult.getLastLocation());
                }
            }
        };

        // Request location updates with the callback.
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, requireActivity().getMainLooper());
        }
    }
}