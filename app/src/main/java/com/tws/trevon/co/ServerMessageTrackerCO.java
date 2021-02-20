package com.tws.trevon.co;

import android.content.ContentValues;
import android.database.Cursor;

import com.tws.trevon.common.AppDbUtilities;

public class ServerMessageTrackerCO
{
    public String messageId;
    public String createdOn;

    public ServerMessageTrackerCO()
    {
        super();
    }

    public static ServerMessageTrackerCO getServerMessageTrackerCOByMessageId(final String messageId)
    {
        return AppDbUtilities.getServerMessageTrackerCOByMessageId(messageId);
    }

    public ContentValues getContentValue()
    {
        ContentValues cv = new ContentValues();
        cv.put("message_id", messageId);
        cv.put("created_on", createdOn);

        return cv;
    }

    public ServerMessageTrackerCO(final Cursor cursor)
    {
        messageId = cursor.getString(cursor.getColumnIndex("message_id"));
        createdOn = cursor.getString(cursor.getColumnIndex("created_on"));
    }

    //database interactions
    public void saveInDB()
    {
        AppDbUtilities.saveServerMessageTrackerCO(getContentValue());
    }
}