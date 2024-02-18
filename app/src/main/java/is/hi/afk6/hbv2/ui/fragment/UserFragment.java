package is.hi.afk6.hbv2.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import is.hi.afk6.hbv2.R;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.UserService;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

public class UserFragment extends Fragment {
    private UserService userService;

    @Override
    public  void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        userService = new UserServiceImplementation(new APIServiceImplementation());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState ){
        View view = inflater.inflate(R.layout.temp_user, container, false);

        TextView users_name = view.findViewById(R.id.temp_name);
        //Temp User Id
        User user = userService.getUserByID(8L);
        Log.i("Tag", user.getName());

        if(user != null){
            users_name.setText(user.getName());
        }

        return view;
    }
}
