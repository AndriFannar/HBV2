package is.hi.afk6.hbv2.services.networking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class APIServiceTest {
    @Test
    public void testGetRequest() {
        ApiService apiService = new ApiService();
        JSONObject object;

        try
        {
            object = apiService.getRequest("user/viewUser/1");
        } catch (IOException | JSONException e)
        {
            throw new RuntimeException(e);
        }

        assertNotNull(object);
    }
}