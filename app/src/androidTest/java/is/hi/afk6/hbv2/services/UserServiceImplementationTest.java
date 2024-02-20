package is.hi.afk6.hbv2.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import is.hi.afk6.hbv2.HBV2Application;
import is.hi.afk6.hbv2.entities.dtos.LoginDTO;
import is.hi.afk6.hbv2.entities.api.ResponseWrapper;
import is.hi.afk6.hbv2.entities.User;
import is.hi.afk6.hbv2.entities.api.APICallback;
import is.hi.afk6.hbv2.networking.implementation.APIServiceImplementation;
import is.hi.afk6.hbv2.services.implementation.UserServiceImplementation;

@RunWith(AndroidJUnit4.class)
public class UserServiceImplementationTest
{
    private UserService userService;

    @Before
    public void createUserServiceClass()
    {
        userService = new UserServiceImplementation(new APIServiceImplementation(), HBV2Application.getInstance().getExecutor());
    }


    @Test
    public void testLogInExists() {
        String email = "afk6@hi.is";
        LoginDTO logIn = new LoginDTO(email, "Lykilord123");
        userService.logInUser(logIn, new APICallback<User>() {
            @Override
            public void onComplete(ResponseWrapper<User> result)
            {
                User user = result.getData();

                assertEquals(user.getEmail(), email);
            }
        });
    }


    @Test
    public void testLogInDoesNotExist() {
        String email = "MMoss@RI.co.uk";
        LoginDTO logIn = new LoginDTO(email, "0118999");
        System.out.println("Test");
        userService.logInUser(logIn, new APICallback<User>() {
            @Override
            public void onComplete(ResponseWrapper<User> result)
            {
                assertNull(result.getData());

                assertNotNull(result.getErrorResponse().getErrorDetails());
            }
        });
    }

    @Test
    public void testUpdateUserSuccessful()
    {
        userService.getUserByID(8L, new APICallback<User>() {
            @Override
            public void onComplete(ResponseWrapper<User> result)
            {
                String newName = "Andri F";
                User user = result.getData();
                user.setName(newName);

                userService.updateUser(user.getId(), user, new APICallback<User>() {
                    @Override
                    public void onComplete(ResponseWrapper<User> result)
                    {
                        userService.getUserByID(8L, new APICallback<User>() {
                            @Override
                            public void onComplete(ResponseWrapper<User> result)
                            {
                                assertEquals(user.getName(), result.getData().getName());
                            }
                        });
                    }
                });
            }
        });
    }

    @Test
    public void testUpdateUserUnsuccessful()
    {
        userService.getUserByID(8L, new APICallback<User>() {
            @Override
            public void onComplete(ResponseWrapper<User> result)
            {
                User user = result.getData();
                String newName = "";
                user.setName(newName);

                userService.updateUser(user.getId(), user, new APICallback<User>() {
                    @Override
                    public void onComplete(ResponseWrapper<User> result)
                    {
                        userService.getUserByID(8L, new APICallback<User>() {
                            @Override
                            public void onComplete(ResponseWrapper<User> result)
                            {

                                assertNotEquals(user.getName(), result.getData().getName());
                                assertNotNull(result.getErrorResponse().getErrorDetails());
                            }
                        });
                    }
                });
            }
        });
    }
}