package is.hi.afk6.hbv2.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import is.hi.afk6.hbv2.services.networking.ApiService;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel()
    {
        ApiService apiService = new ApiService();
        JSONObject object = new JSONObject();

        try
        {
            object = apiService.getRequest("user/viewUser/1");
        } catch (IOException | JSONException e)
        {
            throw new RuntimeException(e);
        }

        mText = new MutableLiveData<>();
        mText.setValue("sus");
    }

    public LiveData<String> getText() {
        return mText;
    }
}