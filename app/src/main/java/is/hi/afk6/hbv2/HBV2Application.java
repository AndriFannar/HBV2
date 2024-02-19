package is.hi.afk6.hbv2;

import android.app.Application;
import android.content.Intent;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import is.hi.afk6.hbv2.ui.LoginActivity;

public class HBV2Application extends Application
{
    private static final int POOL_SIZE = 4;
    private static HBV2Application instance;
    private ExecutorService executorService;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = this;
        executorService = Executors.newFixedThreadPool(POOL_SIZE);
    }

    public static HBV2Application getInstance()
    {
        return instance;
    }

    public Executor getExecutor() {
        return this.executorService;
    }
}
