package com.tws.trevon.common;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tws.trevon.co.ErrorCO;
import com.tws.trevon.co.ServerMessageTrackerCO;

import java.util.ArrayList;

/**
 * Created by Chandra Prakash on 17-02-2015.
 */
public class AppDbUtilities
{
    public static ServerMessageTrackerCO getServerMessageTrackerCOByMessageId(final String messageId)
    {
        ServerMessageTrackerCO serverMessageTrackerCO = null;
        Cursor cursor = executeQuery("SELECT * FROM server_message_tracker where message_id = ?", messageId);

        if(cursor.moveToFirst())
        {
            do
            {
                serverMessageTrackerCO = new ServerMessageTrackerCO(cursor);
            }
            while(cursor.moveToNext());
        }

        closeCursor(cursor);
        return serverMessageTrackerCO;
    }

    public static void saveServerMessageTrackerCO(ContentValues cv)
    {
        DbHelper.getInstance().getDatabase().insertWithOnConflict("server_message_tracker", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static ArrayList<ErrorCO> getErrorCOList()
    {
        ArrayList<ErrorCO> errorCOList = new ArrayList<>();
        Cursor cursor = executeQuery("SELECT * FROM error_report", null);

        if(cursor.moveToFirst())
        {
            do
            {
                errorCOList.add(new ErrorCO(cursor));
            }
            while(cursor.moveToNext());
        }

        closeCursor(cursor);

        return errorCOList;
    }

    public static void saveErrorReport(ContentValues cv)
    {
        DbHelper.getInstance().getDatabase().insertWithOnConflict("error_report", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void deleteErrorReport(final String code)
    {
        executeDelete("DELETE FROM error_report where code = ?", code);
    }

    public static void deleteAllErrors()
    {
        executeDelete("DELETE FROM error_report", null);
    }


    private static Cursor executeQuery(final String queryString, final String... arguments)
    {
        if(arguments == null)
        {
            try
            {
                return DbHelper.getInstance().getDatabase().rawQuery(queryString, new String[]{});
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                return null;
            }
        }
        else
        {
            return DbHelper.getInstance().getDatabase().rawQuery(queryString, arguments);
        }
    }

    private static void executeDelete(final String queryString, final String... arguments)
    {
        if(arguments == null)
        {
            DbHelper.getInstance().getDatabase().execSQL(queryString);
        }
        else
        {
            DbHelper.getInstance().getDatabase().execSQL(queryString, arguments);
        }
    }

    private static void closeCursor(Cursor cursor)
    {
        if(cursor != null && !cursor.isClosed())
        {
            cursor.close();
        }
    }
}