/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tws.trevon.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tws.trevon.R;
import com.tws.trevon.activity.AbstractActivity;
import com.tws.trevon.activity.LoginOriginal;
import com.tws.trevon.activity.SplashScreen;
import com.tws.trevon.co.ErrorCO;
import com.tws.trevon.co.ResponseCO;
import com.tws.trevon.co.ServerInstructionCO;
import com.tws.trevon.co.User;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IErrorCodes;
import com.tws.trevon.constants.IPreferences;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.constants.ISysCodes;
import com.tws.trevon.constants.ISysConfig;
import com.tws.trevon.fragment.CustomAlertDialog;
import com.tws.trevon.fragment.CustomControlledDialog;
import com.tws.trevon.fragment.CustomProgressDialog;
import com.tws.trevon.receivers.AlarmReceiver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import me.leolin.shortcutbadger.ShortcutBadger;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 *
 * @author chandra.kalwar
 *  This class will not have DB interactions... so no instance variables as well
 */

public final class AppUtilities
{
    private static final String TAG = AppUtilities.class.getSimpleName();
    private static boolean isChannelCreated = false;

    public static boolean isInternetConnectionAvailable()
    {
        ConnectivityManager connMgr = (ConnectivityManager)AppController.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static String getMessage(int msgCode, final Object[] arguments)
    {
        return getMessage(AppController.getInstance().getString(msgCode), arguments);
    }

    public static String getMessage(String message, final Object[] arguments)
    {
        if(arguments != null)
        {
            MessageFormat messageMF = new MessageFormat(message);
            message = messageMF.format(arguments);
        }

        return message;
    }

    public static void showUserMessage(int userMsgKey)
    {
        showUserMessage(getMessage(userMsgKey, null), Toast.LENGTH_SHORT);
    }

    public static void showUserMessage(int userMsgKey, final int msgTime)
    {
        showUserMessage(getMessage(userMsgKey, null), msgTime);
    }

    public static void showUserMessage(String userMessage)
    {
        showUserMessage(getMessage(userMessage, null), Toast.LENGTH_SHORT);
    }

    public static void showUserMessage(int userMsgKey, final Object[] arguments)
    {
        showUserMessage(getMessage(userMsgKey, arguments), Toast.LENGTH_SHORT);
    }

    public static void showUserMessage(String userMessage, final Object[] arguments)
    {
        showUserMessage(getMessage(userMessage, arguments), Toast.LENGTH_SHORT);
    }

    public static void showUserMessage(View view, final int userMsgKey, final int msgTime)
    {
        showUserMessage(view, getMessage(userMsgKey, null), msgTime);
    }

    public static void showUserMessageInDevMode(String userMessage)
    {
        if(ISysConfig.APPLICATION_MODE.equals(ISysConfig.APPLICATION_DEVELOPMENT_MODE))
        {
            showUserMessage(userMessage);
        }
    }

    public static void showUserMessage(View view, final String userMessage, final int msgTime)
    {
        if(!AppValidate.isEmpty(userMessage))
        {
            Snackbar.make(view, userMessage, msgTime).show();
        }
    }

    public static void showUserMessage(String userMessage, final int msgTime)
    {
        if(!AppValidate.isEmpty(userMessage))
        {
            Toast.makeText(AppController.getInstance(), userMessage, msgTime).show();
        }
    }

    public static void logException(final String TAG, final String userMessage, final Throwable th)
    {
        logMsg(TAG, userMessage, ISysCodes.LOG_MODE_ERROR, th);
    }

    public static void logMessage(final String TAG, final String userMessage, final int logMode)
    {
        logMsg(TAG, userMessage, logMode, null);
    }

    private static void logMsg(final String TAG, final String userMessage, final int logMode, final Throwable th)
    {
        if(ISysConfig.APPLICATION_MODE.equals(ISysConfig.APPLICATION_DEVELOPMENT_MODE))
        {
            if(ISysCodes.LOG_MODE_INFO == logMode)
            {
                Log.i(TAG, userMessage);
            }
            else if(th == null)
            {
                Log.e(TAG, userMessage);
            }
            else
            {
                Log.e(TAG, userMessage, th);
            }
        }
    }

    public static String getAppPrivateStoragePath(final String orgId, final String fileName){
        ContextWrapper cw = new ContextWrapper(AppController.getInstance());
        // path to /data/data/yourapp/app_data/data
        File dataDirectory = cw.getDir(ISysConfig.APP_INTERNAL_DATA_DIRECTORY, Context.MODE_PRIVATE);
        String finalPath = IConstants.EMPTY_STRING;

        if(AppValidate.isNotEmpty(orgId))
        {
            finalPath = orgId;
        }
        else
        {
            finalPath = ISysConfig.APP_INTERNAL_DATA_APP_DIRECTORY;
        }

        File finalDirPath = new File(dataDirectory, finalPath);
        finalDirPath.mkdirs();
        File filePath = new File(finalDirPath, fileName);

        return filePath.getAbsolutePath();
    }

    public static String saveToInternalSorage(final Bitmap bitmapImage, final String fileName){
        ContextWrapper cw = new ContextWrapper(AppController.getInstance());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(ISysConfig.APP_INTERNAL_IMAGE_DIRECTORY, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (Exception e)
        {
            if(fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e1) {
                    Utilities.processException(TAG, e1);
                }
            }
            Utilities.processException(TAG, e);
        }
        return directory.getAbsolutePath();
    }

    public static void removeFromPref(final String key)
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void writeToPrefForOrg(final String orgId, final String key, final String value)
    {
        writeToPref(orgId + IConstants.UNDER_SCORE + key, value);
    }

    public static String readFromPrefForOrg(final String orgId, final String key)
    {
        return readFromPref(orgId + IConstants.UNDER_SCORE + key);
    }

    public static void writeToPref(final String key, final String value)
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void writeLongToPref(final String key, final Long value)
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static void writeIntToPref(final String key, final Integer value)
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String readFromPref(final String key)
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, IConstants.EMPTY_STRING);
    }

    public static Boolean readBooleanFromPref(final String key, final boolean defaultValue)
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void writeBooleanToPref(final String key, final boolean value)
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(key, value);

        editor.commit();
    }

    public static long readLongFromPref(final String key)
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, ISysCodes.DEFAULT_INT_VALUE);
    }

    public static int readIntFromPref(final String key)
    {
        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, ISysCodes.DEFAULT_INT_VALUE);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion()
    {
//        return 4;
        try
        {
            PackageInfo packageInfo = AppController.getInstance().getPackageManager().getPackageInfo(AppController.getInstance().getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (Exception ex)
        {
            // should never happen
            return ISysConfig.APP_VERSION_CODE;
        }
    }


/*    public static File getTempSubDir(final String defaultDirName)
    {
        File file = getStorageDir(defaultDirName);
        // Get the directory for the user's public pictures directory.
        File tempFile = new File(file, ISysConfig.TEMP_DIR_NAME);
        Utilities.checkForDirectory(tempFile);

        return tempFile;
    }*/

/*    public static File getStorageDir(final String defaultDirName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStorageDirectory(), ISysConfig.DEFAULT_ALBUM_NAME);
        if (!file.mkdirs())
        {
            logMsg(TAG, "Directory not Created", ISysCodes.LOG_MODE_ERROR, null);
        }
        return file;
    }*/

/*    public static File getAlbumStorageDir()
    {
        return getStorageDir(Environment.DIRECTORY_PICTURES);
    }*/

/*    public static File getImageTempSubDir()
    {
        File file = getAlbumStorageDir();
        // Get the directory for the user's public pictures directory.
        File tempFile = new File(file, ISysConfig.TEMP_DIR_NAME);
        Utilities.deleteAndReCreateDirectory(tempFile);

        return tempFile;
    }*/

    public static String writeImageToPath(final String filePath, final Bitmap bitMap)
    {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);

            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, out); // control
            // the jpeg
            // quality

            out.flush();
            out.close();

        } catch (IOException e)
        {

        }
        finally
        {
            if(out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {

                }
            }
        }

        return filePath;
    }

    public static String writeImageToDir(final File dir, final String fileName, final Bitmap bitMap)
    {
        String filePath = dir.getAbsolutePath() + "/" + fileName;

        return writeImageToPath(filePath, bitMap);
    }


    public static boolean isWhatsAppInstalled()
    {
        try {
            PackageManager pm = AppController.getInstance().getPackageManager();
            PackageInfo info = pm.getPackageInfo(IConstants.WHATSAPP_PACKAGE_NAME, PackageManager.GET_META_DATA);
        }
        catch(Exception e)
        {
            return false;
        }

        return true;
    }

    public static void processException(Throwable t,
                                        final String type,
                                        final String orgID,
                                        final Object relevantData
    )
    {
        ErrorCO errorCO = new ErrorCO();

        if(ISysConfig.APPLICATION_MODE.equals(ISysConfig.APPLICATION_DEVELOPMENT_MODE))
        {
            t.printStackTrace();
        }

        try
        {
            errorCO.errorSource = "ANDROID " + type;
            errorCO.createdOn = Long.toString((new Date()).getTime());
            errorCO.stackTrace = Utilities.getStackTrace(t);

            String stringRelevantData = IConstants.EMPTY_STRING;
            if(relevantData != null)
            {
                if(relevantData instanceof String)
                {
                    stringRelevantData = relevantData.toString();
                }
                else
                {
                    stringRelevantData = AppController.gson.toJson(relevantData);
                }
            }

            errorCO.errorInfo = stringRelevantData;
            //errorCO.errorSourceInfo = getAppCurrentStateIdentifier();
        }
        catch(Exception ex)
        {

        }

        errorCO.saveInDB();
        writeToPref(IConstants.ERROR, errorCO.toString());
    }

    public static void refreshPostRelatedPages()
    {
        AppUtilities.writeBooleanToPref(IPreferences.FEEDS_REFRESH_REQUIRED, true);
        AppUtilities.writeBooleanToPref(IPreferences.SKY_REFRESH_REQUIRED, true);
        AppUtilities.writeBooleanToPref(IPreferences.I_CARE_REFRESH_REQUIRED, true);
        AppUtilities.writeBooleanToPref(IPreferences.USER_PROFILE_POST_REFRESH_REQUIRED, true);
    }

    public static void refreshUserRelatedPages()
    {
        refreshPostRelatedPages();
        AppUtilities.writeBooleanToPref(IPreferences.NOTIFICATIONS_REFRESH_REQUIRED, true);
        AppUtilities.writeBooleanToPref(IPreferences.PEOPLE_GLOBAL_SEARCH_REFRESH_REQUIRED, true);

        AppUtilities.writeBooleanToPref(IPreferences.PEOPLE_FRIEND_REFRESH_REQUIRED, true);
        AppUtilities.writeBooleanToPref(IPreferences.PEOPLE_FOLLOWER_REFRESH_REQUIRED, true);
        AppUtilities.writeBooleanToPref(IPreferences.PEOPLE_FOLLOWING_REFRESH_REQUIRED, true);
    }

    public static void refreshFriendUserRelatedPages()
    {
        AppUtilities.writeBooleanToPref(IPreferences.NOTIFICATIONS_REFRESH_REQUIRED, true);
        AppUtilities.writeBooleanToPref(IPreferences.PEOPLE_GLOBAL_SEARCH_REFRESH_REQUIRED, true);

        AppUtilities.writeBooleanToPref(IPreferences.PEOPLE_FRIEND_REFRESH_REQUIRED, true);
        AppUtilities.writeBooleanToPref(IPreferences.PEOPLE_FOLLOWER_REFRESH_REQUIRED, true);
        AppUtilities.writeBooleanToPref(IPreferences.PEOPLE_FOLLOWING_REFRESH_REQUIRED, true);
    }

    public static String dumpIntent(Intent intent)
    {
        String stringRepresentation = IConstants.EMPTY_STRING;

        if(intent != null)
        {
            Bundle bundle = intent.getExtras();
            stringRepresentation = stringRepresentation + dumpBundle(bundle);
        }

        return stringRepresentation;
    }

    public static String dumpBundle(Bundle bundle)
    {
        String stringRepresentation = IConstants.EMPTY_STRING;

        try
        {
            if (bundle != null)
            {
                Set<String> keys = bundle.keySet();

                if(keys != null)
                {
                    for(String key : keys)
                    {
                        stringRepresentation = stringRepresentation +
                                "[" + key + "=" + bundle.get(key)+"]";
                    }
                }
            }
        }
        catch(Exception ex)
        {

        }

        return stringRepresentation;
    }

    private static String getNextMsgId()
    {
        long nextMsgId = readLongFromPref(IReqParams.CURRENT_GCM_MESSAGE_ID) + 1;
        writeLongToPref(IReqParams.CURRENT_GCM_MESSAGE_ID, nextMsgId);
        return Long.toString(nextMsgId);
    }

   /* public static void loadImageUsingVolley(final ImageView imageView, final String url)
    {
        AppController.getInstance().getImageLoader().get(Utilities.getNotNullValue(url)
                , ImageLoader.getImageListener(imageView, 0, 0));
    }

    public static void loadImageUsingVolley(final ImageView imageView, final String url, final int defaultImageResId, final int errorImageResId)
    {
        AppController.getInstance().getImageLoader().get(Utilities.getNotNullValue(url)
                , ImageLoader.getImageListener(imageView, defaultImageResId, errorImageResId));
    }

    public static void loadRVImageUsingVolleyLowThanHigh(final RvImageCO rvImageCO)
    {
        AppController.getInstance().getImageLoader().get(Utilities.getNotNullValue(rvImageCO.lowResolutionImageUrl)
                , new ImageLoader.ImageListener()
                {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                    {
                        if (rvImageCO.isCorrectIVForLowRes() && response.getBitmap() != null)
                        {
                            rvImageCO.imageView.setImageBitmap(response.getBitmap());
                            rvImageCO.imageView.setTag(rvImageCO.url);
                            loadRVImageUsingVolley(rvImageCO);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }

    public static void loadRVImageUsingVolley(final RvImageCO rvImageCO)
    {
        AppController.getInstance().getImageLoader().get(Utilities.getNotNullValue(rvImageCO.url)
                , new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                    {
                        if (rvImageCO.isCorrectIV() && response.getBitmap() != null)
                        {
                            rvImageCO.imageView.setImageBitmap(response.getBitmap());

                            if(rvImageCO.shouldAnimateLoading)
                            {
                                animateImageLoading(rvImageCO.imageView);
                            }
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (rvImageCO.isCorrectIV())
                        {
                            rvImageCO.imageView.setImageResource(rvImageCO.defaultImageRes);
                        }
                    }
                });
    }

    public static void loadResizedImageUsingVolley(final RvImageCO rvImageCO, final int defaultImageResId, final int errorImageResId)
    {
        loadResizedImageUsingVolley(rvImageCO, defaultImageResId, errorImageResId, IConstants.EMPTY_RESOURCE_ID);
    }

    public static void loadResizedImageUsingVolley(final ImageView imageView, final String url, final int defaultImageResId, final int errorImageResId, final int intendedWidth)
    {
        AppController.getInstance().getImageLoader().get(Utilities.getNotNullValue(url), new ImageLoader.ImageListener()
                {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                    {
                        if (response.getBitmap() != null)
                        {
                            *//*imageView.setImageBitmap(response.getBitmap());*//*

                            if(intendedWidth == IConstants.EMPTY_RESOURCE_ID)
                            {
                                imageView.setImageBitmap(resizeImage(response.getBitmap(), getScreenWidthInPixel()));
                            }
                            else
                            {
                                imageView.setImageBitmap(resizeImage(response.getBitmap(), intendedWidth));
                            }
                        }
                        else if (defaultImageResId != 0)
                        {
                            imageView.setImageResource(defaultImageResId);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (errorImageResId != 0) {
                            imageView.setImageResource(errorImageResId);
                        }
                    }
                });
    }

    public static void loadResizedImageUsingVolley(final RvImageCO rvImageCO, final int defaultImageResId, final int errorImageResId, final int intendedWidth)
    {
        AppController.getInstance().getImageLoader().get(Utilities.getNotNullValue(rvImageCO.url)
                , new ImageLoader.ImageListener()
                {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                    {
                        if (rvImageCO.isCorrectIV() && response.getBitmap() != null)
                        {
                            *//*imageView.setImageBitmap(response.getBitmap());*//*

                            if(intendedWidth == IConstants.EMPTY_RESOURCE_ID)
                            {
                                rvImageCO.imageView.setImageBitmap(resizeImage(response.getBitmap(), getScreenWidthInPixel()));
                            }
                            else
                            {
                                rvImageCO.imageView.setImageBitmap(resizeImage(response.getBitmap(), intendedWidth));
                            }
                        }
                        else if (defaultImageResId != 0)
                        {
                            rvImageCO.imageView.setImageResource(defaultImageResId);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (rvImageCO.isCorrectIV() && errorImageResId != 0)
                        {
                            rvImageCO.imageView.setImageResource(errorImageResId);
                        }
                    }
                });
    }

    public static void loadResizedImageUsingVolleyForLowThanHighResolution(final RvImageCO rvImageCO, final int defaultImageResId, final int errorImageResId, final int intendedWidth)
    {
        AppController.getInstance().getImageLoader().get(Utilities.getNotNullValue(rvImageCO.lowResolutionImageUrl)
                , new ImageLoader.ImageListener()
                {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate)
                    {
                        if (rvImageCO.isCorrectIVForLowRes() && response.getBitmap() != null)
                        {
                            *//*imageView.setImageBitmap(response.getBitmap());*//*

                            if(intendedWidth == IConstants.EMPTY_RESOURCE_ID)
                            {
                                rvImageCO.imageView.setImageBitmap(resizeImage(response.getBitmap(), getScreenWidthInPixel()));
                            }
                            else
                            {
                                rvImageCO.imageView.setImageBitmap(resizeImage(response.getBitmap(), intendedWidth));
                            }

                            if(rvImageCO.shouldAnimateLoading)
                            {
                                animateImageLoading(rvImageCO.imageView);
                            }

                            rvImageCO.imageView.setTag(rvImageCO.url);
                            loadResizedImageUsingVolley(rvImageCO, defaultImageResId, errorImageResId, intendedWidth);
                        }
                        else if (defaultImageResId != 0)
                        {
                            rvImageCO.imageView.setImageResource(defaultImageResId);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (rvImageCO.isCorrectIV() && errorImageResId != 0)
                        {
                            rvImageCO.imageView.setImageResource(errorImageResId);
                        }
                    }
                });
    }*/

    public static void animateImageLoading(ImageView imageView)
    {
        imageView.setAlpha(0f);
        imageView.animate().alpha(1f).setDuration(ISysConfig.VOLLEY_IMAGE_SHOW_ANIMATION_DURATION_DEFAULT);
    }


    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static boolean isWiFiConnected(Context context)
    {
        if(context == null)
        {
            context = AppController.getInstance();
        }

        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        boolean isWifiConnected = false;
        NetworkInfo info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
            if (info.isAvailable() && info.isConnected()) {
                isWifiConnected = true;
            }
        }

        return isWifiConnected;
    }


    public static ActivityManager.MemoryInfo getMemoryInfo()
    {
        ActivityManager actManager = (ActivityManager) AppController.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        actManager.getMemoryInfo(memInfo);
        return memInfo;
    }

    public static int getScreenWidthInPixel()
    {
        DisplayMetrics displayMetrics = AppController.getInstance().getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int calculateNoOfColumns(int widthOfOneItem)
    {
        if(widthOfOneItem == 0)
        {
            widthOfOneItem = 360;
        }

        DisplayMetrics displayMetrics = AppController.getInstance().getResources().getDisplayMetrics();
        //float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = displayMetrics.widthPixels / widthOfOneItem;

        if(noOfColumns <= 2)
        {
            return 2;
        }
        else if(noOfColumns == 3)
        {
            return noOfColumns;
        }
        else /*if(noOfColumns > 3)*/
        {
            widthOfOneItem = 420;
            return displayMetrics.widthPixels / widthOfOneItem;
        }
    }


    public static float getPixelFromDP(final float dpNumber)
    {
        DisplayMetrics displayMetrics = AppController.getInstance().getResources().getDisplayMetrics();
        return dpNumber * displayMetrics.density;
    }

    //large or small image using its width

    public static float[] getResizedDimensions(Bitmap btmp, int width)
    {
        float dimension[] = new float[2];
        try {
            float wretval = 0;
            float hretval = 0;

            if (width > btmp.getWidth())
            {
                // growth
                wretval = ((width - btmp.getWidth()) * 100) / btmp.getWidth();

                dimension[0] = width;
                dimension[1] = (int) (btmp.getHeight() + (btmp.getHeight() * (wretval / 100)));
            }
            else
            {
                // loss
                wretval = ((btmp.getWidth() - width) * 100) / width;

                dimension[0] = btmp.getWidth();
                dimension[1] = (int) (btmp.getHeight() - (btmp.getHeight() * (wretval / 100)));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return dimension;
    }

    public static Bitmap resizeImage(Bitmap btmp, int intendedWidth)
    {
        try {
            btmp = Bitmap.createScaledBitmap(
                    btmp,
                    intendedWidth,
                    Utilities.getResizedHeightByIntendedWidth(btmp, intendedWidth),
                    true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return btmp;
    }

    public static Bitmap getBitMapFromImageView(ImageView imageView)
    {
        Bitmap imageBitmap;
        Drawable drawable = imageView.getDrawable();
        if (drawable instanceof BitmapDrawable)
        {
            imageBitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        else
        {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            imageBitmap = bitmap;
        }

        return imageBitmap;
    }

    public static Drawable getCircleDrawableByCatType(final int colorCode)
    {
/*        int colorRes = R.drawable.circle_thank_you_bg;
        switch(catType)
        {
            case ISysCodes.THANK_YOU_CATEGORY_TYPE:
                colorRes = R.drawable.circle_thank_you_bg;
                break;
            case ISysCodes.MISS_YOU_CATEGORY_TYPE:
                colorRes = R.drawable.circle_miss_you_bg;
                break;
            case ISysCodes.ADMIRE_YOU_CATEGORY_TYPE:
                colorRes = R.drawable.circle_admire_you_bg;
                break;
            case ISysCodes.NONE_CATEGORY_TYPE:
                colorRes = R.drawable.circle_thank_you_bg;
                break;
        }*/

        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.setIntrinsicWidth (((Float)getPixelFromDP(16.0f)).intValue());
        shapeDrawable.setIntrinsicHeight (((Float)getPixelFromDP(16.0f)).intValue());
        shapeDrawable.getPaint().setColor(colorCode);
        return shapeDrawable;
    }

    public static Drawable getRectangleDrawable(final int radius, final int fillColorCode, final int borderWidth, int borderColorCode)
    {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(fillColorCode);
        gd.setCornerRadius(radius);
        gd.setStroke(borderWidth, borderColorCode);

        return gd;
    }

    public static String getMacAddress()
    {
        String str = "";
        try {
            WifiManager manager = (WifiManager) AppController.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            str = info.getMacAddress();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }

    public static Bitmap getBitmapFromAsset(String strName, int height, int width, Context givencontext)
    {
        Bitmap retval = null;
        AssetManager assetManager = givencontext.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
            retval = Bitmap.createScaledBitmap(
                    BitmapFactory.decodeStream(istr), width, height, true);
        } catch (Exception e) {
            retval = BitmapFactory.decodeStream(istr);
        }

        return retval;
    }


    public static void hideSoftKeyboard(Dialog dialog)
    {
        try
        {
            /*dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);*/

            InputMethodManager inputMethodManager = (InputMethodManager) AppController.getInstance().getSystemService(Activity.INPUT_METHOD_SERVICE);

            if(dialog.getWindow().getCurrentFocus() != null)
            {
                inputMethodManager.hideSoftInputFromWindow(dialog.getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        catch(Exception ex)
        {

        }
    }

    public static void hideSoftKeyboard(Activity activity)
    {
        try
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

            if(activity.getCurrentFocus() != null)
            {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        }
        catch(Exception ex)
        {

        }
    }

    public static void showSoftKeyboard(Activity activity, final View forView)
    {
        try
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(forView, InputMethodManager.SHOW_FORCED);
        }
        catch(Exception ex)
        {

        }
    }

    public static Bitmap getScreenShot(View view)
    {
        /*view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.buildDrawingCache(true);
        Bitmap bitmap = null;*/

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);


/*        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = null;

        if(view.getDrawingCache() != null)
        {
            bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
        }*/

        return bitmap;
    }

    public static Bitmap tintBitmap(Bitmap bitmap, int color)
    {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        Bitmap bitmapResult = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapResult);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmapResult;
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout()
            {
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine <= 0)
                {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                }
                else if (tv.getLineCount() >= maxLine)
                {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                }
            }
        });
    }



    public static String getBase64String(final ImageView imageView, final int intendedWidth)
    {
        Bitmap bitmap = null;
        try {
            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            bitmap = resizeImage(bitmap, intendedWidth);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (bitmap == null)
        {
            return "";
        }
        else
        {
            return bitmapToBase64(bitmap);
        }
    }

    public static String getBase64String(Bitmap bitmap, final int intendedWidth)
    {
        try {
            bitmap = resizeImage(bitmap, intendedWidth);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (bitmap == null)
        {
            return "";
        }
        else
        {
            return bitmapToBase64(bitmap);
        }
    }

    public static String bitmapToBase64(Bitmap bitmap)
    {
        try {
            if (bitmap != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                return Base64.encodeToString(byteArray, Base64.DEFAULT);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static Intent getNewFreshActivityIntent(Context activity, Class targetClass)
    {
        Intent intent = new Intent(activity, targetClass);
        // Closing all the Activities
        /*intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);*/
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        // Add new Flag to start new Activity
        /*intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );*/

        return intent;
    }


    public static boolean isRefreshRequired(final int timeInSeconds, final String prefNameLEO)
    {
        boolean allowRun = false;
        long lastExecutedOn = AppUtilities.readLongFromPref(prefNameLEO);

        if(lastExecutedOn == ISysCodes.DEFAULT_INT_VALUE)
        {
            allowRun = true;
        }
        else
        {
            allowRun = Utilities.isRefreshRequired(Long.toString(lastExecutedOn), timeInSeconds);
        }

        if(allowRun)
        {
            AppUtilities.writeLongToPref(prefNameLEO, (new Date()).getTime());
        }

        return allowRun;
    }

    public static void setNotificationBadge()
    {
        try
        {
            ShortcutBadger.setBadge(AppController.getInstance(), AppUtilities.readIntFromPref(IConstants.NOTIFICATION_COUNT));
        }
        catch(Exception ex)
        {

        }
    }

    public static Bitmap rotateImage(Bitmap bitmap, final float rotationAngle)
    {
        if(rotationAngle != 0.0f)
        {
            Matrix matrix = new Matrix();
            /*matrix.postRotate(rotationAngle);*/
            matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        else
        {
            return bitmap;
        }
    }

    public static void performLongClickVibrate()
    {
        Vibrator vibrator = (Vibrator) AppController.getInstance().getSystemService(Context.VIBRATOR_SERVICE) ;
        vibrator.vibrate(35);
    }

    public static void copyTextToClipboard(String data, final boolean isEncoded)
    {
/*        if(isEncoded)
        {
            data = Utilities.decodeBase64(data);
        }*/

        ClipboardManager clipboard = (ClipboardManager) AppController.getInstance().getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Text", data);
        clipboard.setPrimaryClip(clip);
    }

    public static String getVideoPathFromUri(Uri videoUri)
    {
        String filePath = IConstants.EMPTY_STRING;
        try
        {
            AssetFileDescriptor videoAsset = AppController.getInstance().getContentResolver().openAssetFileDescriptor(videoUri, "r");
            FileInputStream fis = videoAsset.createInputStream();

            File root=new File(Environment.getExternalStorageDirectory(),"/Onourem/Video/");
            if (!root.exists())
            {
                root.mkdirs();
            }

            File file;
            file=new File(root,"onourem_video_"+System.currentTimeMillis()+".mp4" );

            FileOutputStream fos = new FileOutputStream(file);

            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }

            fis.close();
            fos.close();

            filePath = file.getAbsolutePath();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return filePath;
    }

    public static String getBase6StringFromVideoUri(Uri videoUri)
    {
        String base64String = IConstants.EMPTY_STRING;
        try
        {
            /*AssetFileDescriptor videoAsset = AppController.getInstance().getContentResolver().openAssetFileDescriptor(videoUri, "r");*/
            FileInputStream fis = new FileInputStream(videoUri.toString());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int len;
            while ((len = fis.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }

            base64String = Utilities.encodeToBase64(bos.toByteArray());
            fis.close();
            bos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return base64String;
    }

    public static String getMimeType(Context context, Uri uri)
    {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    public static Bitmap compressBitmap(final Bitmap bitmap)
    {
        ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bmpStream);

        byte[] byteArray = bmpStream.toByteArray();
        Bitmap outputBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);

        outputBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(bmpStream.toByteArray()));
        try {
            bmpStream.flush();//to avoid out of memory error
            bmpStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputBitmap;
    }

    public static Bitmap compressBitmapToLimitInKB(Bitmap bitmap, final int sizeInKB)
    {
/*        File f = new File(mImageDirectory + mImageName);
        if(f.exists()){
            f.delete();
        }*/

        int MAX_IMAGE_SIZE = sizeInKB * 1024;
        int streamLength = MAX_IMAGE_SIZE;
        int compressQuality = 105;
        ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
        while (streamLength >= MAX_IMAGE_SIZE && compressQuality > 5)
        {
            try {
                bmpStream.flush();//to avoid out of memory error
                bmpStream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
            compressQuality -= 5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
            byte[] bmpPicByteArray = bmpStream.toByteArray();
            streamLength = bmpPicByteArray.length;
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
        return BitmapFactory.decodeByteArray(bmpStream.toByteArray(), 0, bmpStream.toByteArray().length);
    }

    public static String getColorCode(final int resId)
    {
        final int colorRes = AppController.getInstance().getResources().getColor(resId);
        String strColor = Integer.toHexString(colorRes);

        if(AppValidate.isNotEmpty(strColor))
        {
            if(strColor.length() > 6)
            {
                strColor = strColor.substring(2);
            }
        }

        return "#"+strColor;
    }

    public static int getColorFromServerCode(final String colorCode)
    {
        int color = IConstants.EMPTY_RESOURCE_ID;
        try
        {
            color = Color.parseColor("#" + colorCode);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            color = Color.parseColor("#b4b4b4");
        }

        return color;
    }

    public static int getResourcesColor(final int colorCode)
    {
        try
        {
            return AppController.getInstance().getResources().getColor(colorCode);
        }
        catch(Exception ex)
        {
            return colorCode;
        }
    }

    public static int darkDefault(int color) {
        return darken(color, 0.10);
    }

    public static int lightDefault(int color) {
        return lighten(color, 0.10);
    }

    public static int lighten(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = lightenColor(red, fraction);
        green = lightenColor(green, fraction);
        blue = lightenColor(blue, fraction);
        int alpha = Color.alpha(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static int darken(int color, double fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        red = darkenColor(red, fraction);
        green = darkenColor(green, fraction);
        blue = darkenColor(blue, fraction);
        int alpha = Color.alpha(color);

        return Color.argb(alpha, red, green, blue);
    }

    private static int darkenColor(int color, double fraction) {
        return (int)Math.max(color - (color * fraction), 0);
    }

    private static int lightenColor(int color, double fraction) {
        return (int) Math.min(color + (color * fraction), 255);
    }

    public static void processCallApi(final NetworkHelper networkHelper)
    {
        if(networkHelper.getCallApi().processInBackground)
        {
            processCallApiInBackground(networkHelper);
            return;
        }
        else if(networkHelper.getCallApi().isSilentCall)
        {
            processCallApiSilently(networkHelper);
            return;
        }

        if(!networkHelper.getActivity().isInternetConnectionAvailable())
        {
            networkHelper.onApiCallError(networkHelper.getCallApi(), IErrorCodes.INTERNET_NOT_AVAILABLE);
            return;
        }

        final CustomProgressDialog customProgressDialog = CustomProgressDialog.initializeCustomDialog(networkHelper.getActivity().getSupportFragmentManager());

        networkHelper.getCallApi().setListener(new CallApi.Listener() {
            @Override
            public void onSuccess(final ResponseCO responseCO)
            {
                try{
                    networkHelper.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            customProgressDialog.dismissAllowingStateLoss();
                            networkHelper.onApiCallSuccess(responseCO.response, responseCO.callApi);
                        }
                    });
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(final ResponseCO responseCO)
            {
                try{
                    networkHelper.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            customProgressDialog.dismiss();

                            if(ISysConfig.APPLICATION_MODE.equals(ISysConfig.APPLICATION_PRODUCTION_MODE))
                            {
                                networkHelper.onApiCallError(responseCO.callApi, responseCO.clientErrorCode);
                            }
                            else
                            {
                                showCustomAlertDialog(networkHelper.getActivity(),responseCO.response, responseCO.response, false, new CustomAlertDialog.OnDialogFragmentListener() {
                                    @Override
                                    public void onPositiveAction() {
                                        networkHelper.onApiCallError(responseCO.callApi, responseCO.clientErrorCode);
                                    }

                                    @Override
                                    public void onNegativeAction() {
                                        networkHelper.onApiCallError(responseCO.callApi, responseCO.clientErrorCode);
                                    }
                                });
                            }
                        }
                    });
                }

                catch(Exception e)
            {
                e.printStackTrace();
            }

            }
        });

        networkHelper.getCallApi().execute();
    }

    public static void processCallApiInBackground(final NetworkHelper networkHelper)
    {
        if(networkHelper.getActivity() == null)
        {
            if(isInternetConnectionAvailable())
            {
                AppUtilities.showUserMessage("Internet connection not available");
                networkHelper.onApiCallError(networkHelper.getCallApi(), IErrorCodes.INTERNET_NOT_AVAILABLE);
                return;
            }
        }
        else if(!networkHelper.getActivity().isInternetConnectionAvailable())
        {
            networkHelper.onApiCallError(networkHelper.getCallApi(), IErrorCodes.INTERNET_NOT_AVAILABLE);
            return;
        }

        networkHelper.getCallApi().setListener(new CallApi.Listener() {
            @Override
            public void onSuccess(final ResponseCO responseCO)
            {
                if(networkHelper.getActivity() == null)
                {
                    //AppUtilities.showUserMessage(responseCO.response);
                    networkHelper.onApiCallSuccess(responseCO.response, responseCO.callApi);
                }
                else
                {
                    networkHelper.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run()
                        {
                            // networkHelper.getActivity().showUserMessage(responseCO.getServerMessage());
                            networkHelper.onApiCallSuccess(responseCO.response, responseCO.callApi);
                        }
                    });
                }
            }

            @Override
            public void onError(final ResponseCO responseCO)
            {
                if(networkHelper.getActivity() == null)
                {
                    //   AppUtilities.showUserMessage(responseCO.getServerMessage());
                    networkHelper.onApiCallError(responseCO.callApi, responseCO.clientErrorCode);
                }
                else
                {
                    if(responseCO.isUserError)
                    {
                        if(networkHelper.getActivity() == null)
                        {
                            AppUtilities.showUserMessage(responseCO.response);
                            networkHelper.onApiCallError(responseCO.callApi, responseCO.clientErrorCode);
                        }
                        else
                        {
                            showCustomAlertDialog(networkHelper.getActivity(),responseCO.response, responseCO.response, false, new CustomAlertDialog.OnDialogFragmentListener() {
                                @Override
                                public void onPositiveAction() {
                                    networkHelper.onApiCallError(responseCO.callApi, responseCO.clientErrorCode);
                                }

                                @Override
                                public void onNegativeAction() {
                                    networkHelper.onApiCallError(responseCO.callApi, responseCO.clientErrorCode);
                                }
                            });
                        }
                    }
                    else
                    {
                        if(ISysConfig.APPLICATION_MODE.equals(ISysConfig.APPLICATION_PRODUCTION_MODE))
                        {
                            if(networkHelper.getActivity() == null)
                            {
                                AppUtilities.showUserMessage(responseCO.response);
                            }
                            else
                            {
                                networkHelper.getActivity().showUserMessage(responseCO.response);
                            }

                            networkHelper.onApiCallError(responseCO.callApi, responseCO.clientErrorCode);
                        }
                        else
                        {
                            if(networkHelper.getActivity() == null)
                            {
                                AppUtilities.showUserMessage(responseCO.response);
                                networkHelper.onApiCallError(responseCO.callApi, responseCO.clientErrorCode);
                            }
                            else
                            {
                                showCustomAlertDialog(networkHelper.getActivity(),responseCO.response, responseCO.response, false, new CustomAlertDialog.OnDialogFragmentListener() {
                                    @Override
                                    public void onPositiveAction() {
                                        networkHelper.onApiCallError(responseCO.callApi, responseCO.clientErrorCode);
                                    }

                                    @Override
                                    public void onNegativeAction() {
                                        networkHelper.onApiCallError(responseCO.callApi, responseCO.clientErrorCode);
                                    }
                                });
                            }
                        }
                    }
                }
            }

        });

        networkHelper.getCallApi().execute();
    }

    public static void processCallApiSilently(final NetworkHelper networkHelper)
    {
        if(isInternetConnectionAvailable())
        {
            networkHelper.onApiCallError(networkHelper.getCallApi(), IErrorCodes.INTERNET_NOT_AVAILABLE);
            return;
        }

        networkHelper.getCallApi().setListener(new CallApi.Listener() {
            @Override
            public void onSuccess(final ResponseCO responseCO)
            {
                networkHelper.onApiCallSuccess(responseCO.response, responseCO.callApi);
            }

            @Override
            public void onError(final ResponseCO responseCO)
            {
                networkHelper.onApiCallError(responseCO.callApi, responseCO.clientErrorCode);
            }

        });

        networkHelper.getCallApi().execute();
    }

    private static void processServerInstructionCO(final NetworkHelper networkHelper, final ServerInstructionCO serverInstructionCO)
    {
        if(serverInstructionCO != null)
        {
            switch(Utilities.getNotNullValue(serverInstructionCO.type))
            {
                case ServerInstructionCO.RESET_CLIENT_DB_TYPE:
                    AppUtilities.clearDatabase();
                    break;

                case ServerInstructionCO.FORCE_LOGOUT_TYPE:
                    networkHelper.getActivity().processLogout(new Intent(networkHelper.getActivity(), LoginOriginal.class));
                    break;

                default:
                    //do nothing
            }


            if(serverInstructionCO.dialogCO != null && networkHelper.getActivity() != null)
            {
                FragmentManager fragmentManager = networkHelper.getActivity().getSupportFragmentManager();
                CustomControlledDialog customControlledDialog = new CustomControlledDialog();
                Bundle bundle = new Bundle();
                bundle.putParcelable(IConstants.OBJECT, serverInstructionCO.dialogCO);
                customControlledDialog.setArguments(bundle);
                customControlledDialog.show(fragmentManager, "");
            }
        }
    }

    public static void showCustomAlertDialog(final AbstractActivity activity, final String title, final String description, final boolean isCancelable, final CustomAlertDialog.OnDialogFragmentListener listener)
    {
        if(AppValidate.isNotEmpty(description))
        {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            CustomAlertDialog customAlertDialog = new CustomAlertDialog();
            Bundle bundle = new Bundle();
            bundle.putString(IConstants.DIALOG_TITLE, title);
            bundle.putString(IConstants.DESCRIPTION, description);
            bundle.putBoolean(IConstants.BOOLEAN, isCancelable);
            customAlertDialog.setArguments(bundle);
            customAlertDialog.setOnDialogClickListener(listener);
            customAlertDialog.show(fragmentManager,"");
        }
    }

    public static void clearDatabase()
    {
        User user = User.getUserCOFromDB();
        boolean isUserLoggedIn = AppUtilities.readBooleanFromPref(IConstants.IS_USER_LOGGED_IN, false);

        SharedPreferences sharedPreferences = AppController.getInstance().getSharedPreferences(ISysConfig.APP_SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        user.saveInDB();
        AppUtilities.writeBooleanToPref(IConstants.IS_USER_LOGGED_IN, isUserLoggedIn);

        try
        {
            DbHelper.getInstance().closeDatabase();
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }

        AppController.getInstance().deleteDatabase(DbHelper.DB_NAME);

        AppUtilities.writeBooleanToPref(IConstants.CLEAR_DATABASE, Boolean.FALSE);
    }

    public static void addUserMessageOnActivity(final Intent intent, final String userMessage)
    {
        intent.putExtra(IConstants.USER_MESSAGE, userMessage);
    }

    public static void sendDefaultNotification(final String title, final String message, Intent resultIntent)
    {
        sendNotification(title, message, resultIntent, -1, -1);
    }

    public static void sendNotification(final String title, final String message, Intent resultIntent, int smallIcon, int largeIcon)
    {
        AppController appControllerInstance = AppController.getInstance();

        if(ISysConfig.APPLICATION_PRODUCTION_MODE.equals(ISysConfig.APPLICATION_MODE))
        {
            if(!AppUtilities.readBooleanFromPref(IConstants.IS_USER_LOGGED_IN, false))
            {
                return;
            }
        }

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.

        if(resultIntent == null)
        {
            resultIntent = new Intent(appControllerInstance, getHomeClass());
            resultIntent.putExtra(IConstants.IS_NOTIF_CLICK, true);
            resultIntent.putExtra(IConstants.FORWARD_TO, IConstants.NOTIFICATIONS);
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(appControllerInstance);
        // Adds the back stack
        stackBuilder.addParentStack(getHomeClass());
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager mNotificationManager = (NotificationManager) AppController.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

        if(smallIcon < 0)
        {
            smallIcon = R.drawable.ic_notification;
        }

        if(largeIcon < 0)
        {
            largeIcon = R.drawable.ic_notification;
        }

        initChannels();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AppController.getInstance(), ISysConfig.APP_NAME)
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(AppController.getInstance().getResources(), largeIcon))
                .setColor(appControllerInstance.getResources().getColor(R.color.app_dark_primary_color))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(new long[]{500, 500, 500, 500, 500})
                .setLights(Color.RED, 3000, 3000)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(resultPendingIntent);

/*        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AppController.getInstance())
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(AppController.getInstance().getResources(), largeIcon))
                .setColor(appControllerInstance.getResources().getColor(R.color.app_dark_primary_color))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(new long[]{500, 500, 500, 500, 500})
                .setLights(Color.RED, 3000, 3000)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(resultPendingIntent);*/

        if(AppValidate.isNotEmpty(message))
        {
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        }

        mNotificationManager.notify(Utilities.getRandomInteger(1000, 0), mBuilder.build());
    }

    public static void initChannels()
    {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }

        if(!isChannelCreated)
        {
            NotificationManager notificationManager =
                    (NotificationManager) AppController.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("framework",
                    "Default",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(ISysConfig.APP_NAME);
            notificationManager.createNotificationChannel(channel);

            isChannelCreated = true;
        }
    }

    public static Class getHomeClass()
    {
        return SplashScreen.class;
    }

    public static void checkForBackgroundProcess(Context context)
    {
        if (context == null) {
            context = AppController.getInstance();
        }


      /*  if(!AppUtilities.readBooleanFromPref(IConstants.BACKGROUND_ALARM_STARTED, false))
        {//to open the drawer the first time user uses the app
            AppUtilities.startScheduledService(null);
        }
        else
        {
            Long alarmLastOn = AppUtilities.readLongFromPref(IConstants.BACKGROUND_ALARM_LAST_TIME);
            if(alarmLastOn == ISysCodes.DEFAULT_INT_VALUE)
            {
                AppUtilities.startScheduledService(null);
            }
            else
            {
                long nextAlarmOnTimeMS = alarmLastOn + ISysConfig.CHECK_FOR_ALARM_IN_TIME;
                if(System.currentTimeMillis() >= nextAlarmOnTimeMS)
                {
                    AppUtilities.startScheduledService(null);
                }
            }
        }*/
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 99999999, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                2000, pendingIntent);

        AppUtilities.writeBooleanToPref(IConstants.BACKGROUND_ALARM_STARTED, Boolean.TRUE);
    }

    public static void startScheduledService(Context context)
    {
        if(context == null)
        {
            context = AppController.getInstance();
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 99999999, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(),
                ISysConfig.ALARM_INTERVAL, pendingIntent);

        AppUtilities.writeBooleanToPref(IConstants.BACKGROUND_ALARM_STARTED, Boolean.TRUE);
    }

    public static Date getTime(String start_time) throws ParseException {
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = format.parse(start_time);
        return date;
    }

    public static File createBitmapWithBackground(Uri uri)
    {
        File filePath=null;
        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(AppController.getInstance().getContentResolver(),uri);
            Bitmap finalBitmap= AppUtilities.customBitmapCreation(bitmap);
            String filepath = AppUtilities.writeImageToTemp(Utilities.getTempImageName("upload"), finalBitmap);
            //return filePath;
            filePath= new File(filepath);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return filePath;
    }

    public static Bitmap customBitmapCreation(Bitmap bmp1)
    {

        int mWidth=bmp1.getWidth();
        int mHeight=bmp1.getHeight();
        Bitmap bmp2 ;
       /* if(mWidth>mHeight)
        {
            bmp2 = Bitmap.createBitmap(bmp1.getWidth()/2, bmp1.getWidth(), bmp1.getConfig());
        }
        else
        {
            bmp2 = Bitmap.createBitmap(bmp1.getHeight(), bmp1.getHeight()/2, bmp1.getConfig());
        }*/
        if(mHeight>mWidth)
        {
            bmp2 = Bitmap.createBitmap(bmp1.getHeight()/2, bmp1.getHeight(), bmp1.getConfig());
        }
        else
        {
            bmp2 = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getWidth()/2, bmp1.getConfig());
        }
        int max=Math.max(mWidth, mHeight);
        int min=Math.min(mWidth, mHeight);
        double ratio=max/min;
        if(ratio>IConstants.MAX_SCREEN_RATIO)
        {
            Bitmap backgroundBitmap = bmp2;
            Bitmap bitmapToDrawInTheCenter = bmp1;

            Bitmap resultBitmap=null;

            if(mWidth>mHeight)
            {
                resultBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(),backgroundBitmap.getWidth()/2, backgroundBitmap.getConfig());
                Canvas canvas = new Canvas(resultBitmap);
                canvas.drawBitmap(backgroundBitmap, new Matrix(), null);
                canvas.drawBitmap(bitmapToDrawInTheCenter, (backgroundBitmap.getWidth() - bitmapToDrawInTheCenter.getWidth()) / 2, (backgroundBitmap.getHeight() - bitmapToDrawInTheCenter.getHeight()) / 2, new Paint());
            }
            else
            {
                resultBitmap = Bitmap.createBitmap(backgroundBitmap.getHeight()/2,backgroundBitmap.getHeight(), backgroundBitmap.getConfig());
                Canvas canvas = new Canvas(resultBitmap);
                canvas.drawBitmap(backgroundBitmap, new Matrix(), null);
                canvas.drawBitmap(bitmapToDrawInTheCenter, (backgroundBitmap.getWidth() - bitmapToDrawInTheCenter.getWidth()) / 2, (backgroundBitmap.getHeight() - bitmapToDrawInTheCenter.getHeight()) / 2, new Paint());
            }

            return  resultBitmap;
        }
        else
        {
            return  bmp1;
        }
        //return  resultBitmap;
    }

    public static String writeImageToTemp(final String fileName, final Bitmap bitMap)
    {
        String filePath = getPublicStorageDirectory(true, ISysCodes.TEMPORARY_PUBLIC_DIRECTORY, fileName);

        if(AppValidate.isNotEmpty(filePath))
        {
            return writeImageToPath(filePath, bitMap);
        }

        return null;
    }

    public static String getPublicStorageDirectory(final boolean isForWrite, final String directoryType, final String fileName)
    {
        if(isForWrite)
        {
            if(!isExternalStorageWritable())
            {
                showUserMessage(R.string.storage_not_available_to_write);
                return null;
            }
        }
        else
        {
            if(!isExternalStorageReadable())
            {
                showUserMessage(R.string.storage_not_available_to_read);
                return null;
            }
        }


        final File externalDirRootFile = Environment.getExternalStorageDirectory();

        final String targetPath = externalDirRootFile.getAbsolutePath() + File.separator +
                ISysConfig.APP_PUBLIC_DIRECTORY_NAME + File.separator +
                directoryType;

        final File targetDirectory = new File(targetPath);
        targetDirectory.mkdirs();

        return targetPath + File.separator + fileName;
    }




   /* public static String colorCodes(String letter)
    {
        String[] bgColors = AppController.getInstance().getResources().getStringArray(R.array.bgColors);

        switch (letter)
        {
            case IConstants.A:
                return bgColors[0];
            case IConstants.B:
                return bgColors[1];
            case IConstants.C:
                return bgColors[2];
            case IConstants.D:
                return bgColors[3];
            case IConstants.E:
                return bgColors[4];
            case IConstants.F:
                return bgColors[5];
            case IConstants.G:
                return bgColors[6];
            case IConstants.H:
                return bgColors[7];
            case IConstants.I:
                return bgColors[8];
            case IConstants.J:
                return bgColors[9];
            case IConstants.K:
                return bgColors[10];
            case IConstants.L:
                return bgColors[11];
            case IConstants.M:
                return bgColors[12];
            case IConstants.N:
                return bgColors[13];
            case IConstants.O:
                return bgColors[14];
            case IConstants.P:
                return bgColors[15];
            case IConstants.Q:
                return bgColors[16];
            case IConstants.R:
                return bgColors[0];
            case IConstants.S:
                return bgColors[1];
            case IConstants.T:
                return bgColors[2];
            case IConstants.U:
                return bgColors[3];
            case IConstants.V:
                return bgColors[4];
            case IConstants.W:
                return bgColors[5];
            case IConstants.X:
                return bgColors[6];
            case IConstants.Y:
                return bgColors[7];
            case IConstants.Z:
                return bgColors[8];
            default:
                return bgColors[0];
        }
    }*/

    public static File saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try
        {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return file;
    }

  /*  public static float getColumnWidth(Context context) {
        if (GRID_COLUMN_WIDTH == -1) {
            int screenWidth = getDisplayMetrics(context).widthPixels;
            float horizontalPadding = getDimensInPixel(context, R.dimen.activity_horizontal_margin);
            // NUM OF COLUMN = 2
            GRID_COLUMN_WIDTH = screenWidth / 2 - horizontalPadding * 2;
        }

        return GRID_COLUMN_WIDTH;
    }
*/

    public static String removeSpace(String str)
    {
        String noSpaceStr = str.replaceAll("\\s", "");
        return noSpaceStr;
    }

    public static void shareImage(String image, Context context, String sharedtext)
    {
        Uri bmpUri = getLocalBitmapUri(image, context);

        if (bmpUri != null)
        {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_TEXT, sharedtext);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            context.startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            // user did'n get bitmap uri, sharing failed
        }
    }

    public static Uri getLocalBitmapUri(String  imageView, Context mContext) {
        // Extract Bitmap from ImageView drawable
        Bitmap bmp = null;
        try {
            URL url = new URL(imageView);
            bmp = getImageBitmapFromURL(mContext, imageView);
           // bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(IOException e) {
            System.out.println(e);
        }

        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // This way, you don't need to request external read/write permission.
            File file =  new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);
            //bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public static Bitmap getImageBitmapFromURL(final Context context, final String imageUrl){
        Bitmap imageBitmap = null;
        try {
            imageBitmap = new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... params) {
                    try {
                        int targetHeight = 200;
                        int targetWidth = 200;

                        return Picasso.with(context).load(String.valueOf(imageUrl))
                                //.resize(targetWidth, targetHeight)
                                .placeholder(R.drawable.error_images)
                                .error(R.drawable.error_images)
                                .get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return imageBitmap;
    }

    public static void downloadFile(Uri uri,String fileName,Context context)
    {

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);  // Tell on which network you want to download file.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
        request.setTitle(fileName); // Title for notification.
        request.setVisibleInDownloadsUi(true);
        // request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getLastPathSegment());  // Storage directory path
        ((DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE)).enqueue(request); // This will start downloading


        //request.setMimeType("*/*");
    }

    public static void showPdfFromUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
        if (activities.size() > 0) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "No app installed in your phone to view pdf", Toast.LENGTH_SHORT).show();
        }
    }

    public static CharSequence trim(CharSequence s, int start, int end) {
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }

        return s.subSequence(start, end);
    }


}
