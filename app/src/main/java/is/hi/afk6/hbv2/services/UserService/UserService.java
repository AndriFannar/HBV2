package is.hi.afk6.hbv2.services.UserService;

import org.json.JSONObject;

import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.services.networking.ApiService;

public class UserService
{
    private ApiService apiService;

    public UserService()
    {
        apiService = new ApiService();
    }

    public User getUser(String id)
    {
        try
        {
            JSONObject object = apiService.getRequest("user/viewUser/" + id);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //User user = new Gson

        return null;
    }
}
