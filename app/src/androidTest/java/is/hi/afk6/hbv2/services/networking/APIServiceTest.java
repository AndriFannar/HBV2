package is.hi.afk6.hbv2.services.networking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class APIServiceTest {
    @Test
    public void testGetRequest() throws JSONException {
        ApiService apiService = new ApiService();
        JSONObject object;

        try
        {
            object = apiService.getRequest("user/viewUser/1");
        } catch (IOException | JSONException e)
        {
            throw new RuntimeException(e);
        }

        System.out.println(object);

        assertEquals(object.getString("id"), "1");
    }
}