package com.tws.trevon.common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tws.trevon.constants.ISysConfig;

public class DbHelper extends SQLiteOpenHelper
{
    private static final String TAG = DbHelper.class.getSimpleName();

    public static final String DB_NAME = ISysConfig.DB_NAME;
    public static final int DB_VERSION = 1;

    private static DbHelper dbHelperInstance;
    private SQLiteDatabase db;

    private DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
//
    }

    public static DbHelper getInstance() {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (dbHelperInstance == null && AppController.getInstance() != null)
        {
            dbHelperInstance = new DbHelper(AppController.getInstance().getApplicationContext());
            dbHelperInstance.db = dbHelperInstance.getWritableDatabase();

            if(dbHelperInstance.db == null)
            {
                AppUtilities.showUserMessage("Database error");
            }
        }

/*        while(dbHelperInstance.db.isDbLockedByCurrentThread() || dbHelperInstance.db.isDbLockedByOtherThreads()) {
            //db is locked, keep looping
        }*/

        return dbHelperInstance;
    }

    // Called only once first time we create the database
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        updateDatabase(db, 0);
    }

    // Gets called whenever existing version != new version, i.e. schema changed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        updateDatabase(db, oldVersion);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion)
    {
        try {
            updateDatabaseSequentially(db, oldVersion);
        }
        catch(Exception ex)
        {
            dropAllTables(db);
            updateDatabaseSequentially(db, 0);
        }
    }

    private void updateDatabaseSequentially(SQLiteDatabase db, int oldVersion)
    {
        final int startUpgradeFrom = oldVersion + 1;
        switch (startUpgradeFrom) {
            case 1:
                db.execSQL("CREATE TABLE error_report (" +
                        "code integer primary key, " +
                        "error_sub_type text, " +
                        "error_source text, " +
                        "error_source_info text, " +
                        "stack_trace text, " +
                        "error_info text, " +
                        "created_on integer)");

                db.execSQL("CREATE TABLE blocked_users (" +
                        "user_code text, " +
                        "first_name text, " +
                        "last_name text, " +
                        "profile_picture text, " +
                        "status text, " +
                        "follower_status text)");

                db.execSQL("CREATE TABLE server_message_tracker (" +
                        "message_id text, " +
                        "created_on integer)");

                break;
        }
    }

    private void dropAllTables(SQLiteDatabase db)
    {
        db.execSQL("drop table if exists error_report");
        db.execSQL("drop table if exists blocked_users");
        db.execSQL("drop table if exists server_message_tracker");
    }

    public void finalize() throws Throwable
    {
        closeDatabase();
        super.finalize();
    }

    public void closeDatabase() throws Throwable
    {
        if(null != dbHelperInstance)
            dbHelperInstance.close();
        if(null != db)
            db.close();

        dbHelperInstance = null;
    }

    public SQLiteDatabase getDatabase() {
        return db;
    }
}