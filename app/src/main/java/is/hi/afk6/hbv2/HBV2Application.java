package is.hi.afk6.hbv2;

import android.app.Application;
import android.content.Intent;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import is.hi.afk6.hbv2.ui.LoginActivity;

/**
 * Main class for the Application.
 * Manages the thread pool so other threads can perform network tasks.
 *
 * @author  Andri Fannar Kristjánsson, afk6@hi.is
 * @since   19/02/2024
 * @version 1.0
 */
public class HBV2Application extends Application
{
    // Number of threads to use.
    private static final int POOL_SIZE = 4;
    private ExecutorService executorService;
    private static HBV2Application instance;

    @Override
    public void onCreate()
    {
        super.onCreate();

        // Save the class instance, and create a new thread pool.
        instance = this;
        executorService = Executors.newFixedThreadPool(POOL_SIZE);
    }

    /**
     * Get the instance of the class.
     *
     * @return Class instance.
     */
    public static HBV2Application getInstance()
    {
        return instance;
    }

    /**
     * Get an executor to run asynchronous tasks.
     *
     * @return Executor to run Runnable asynchronous tasks.
     */
    public Executor getExecutor() {
        return this.executorService;
    }
}
