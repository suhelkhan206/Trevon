package com.tws.trevon.common;

import androidx.multidex.MultiDexApplication;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;

public class AppController extends MultiDexApplication
{

	public static final String TAG = AppController.class.getSimpleName();

    public static final Gson gson = new Gson();

	private Thread.UncaughtExceptionHandler defaultHandler;

	private static AppController mInstance;

	static {
		AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
		{
			@Override
			public void uncaughtException (Thread thread, Throwable exception)
			{
				AppUtilities.processException(
						exception,
						"uncaughtException",
						null,
						null);

				defaultHandler.uncaughtException(thread, exception);
			}
		});

	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}
}
