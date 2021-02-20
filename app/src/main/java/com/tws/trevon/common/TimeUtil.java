/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tws.trevon.common;

import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.ISysConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class TimeUtil
{

    public static int minThreshold = 15;
    public static String threshold_unit = "minute";

    public static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(7),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1) );
    public static final List<String> timesString = Arrays.asList("year","month","week","day","hour","minute");


    public static final List<Long> times2 = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(7),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1) );
    public static final List<String> timesString2 = Arrays.asList("year","week","day","hour","minute");

    public static String getRelatedTime(final String serverTime)
    {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISysConfig.APP_DISPLAY_DATE_TIME_FORMAT, Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("MST"));

            Date serverDate = ((simpleDateFormat)).parse(serverTime);

            long duration = System.currentTimeMillis() - serverDate.getTime();
            StringBuffer res = new StringBuffer();

            long oneDayMillis = TimeUnit.DAYS.toMillis(1);
            long dayDiff = duration/oneDayMillis;

            if(dayDiff > 0)
            {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(serverDate);

                if(dayDiff <= 7)
                {
                    res.append(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US));
                }
                else
                {
                    res.append(Utilities.getDateString(serverDate));
                }
            }
            else
            {
                long oneMinuteMillis = TimeUnit.MINUTES.toMillis(1);
                long minuteDiff = duration/oneMinuteMillis;

                if(minuteDiff < minThreshold)
                {
                    res.append("Just now");
                }
                else if(minuteDiff < 60)
                {
                    res.append(minuteDiff + " minutes");
                }
                else
                {
                    long hourDiff = minuteDiff/60;
                    res.append(hourDiff + " hours");
                }
            }

            if("".equals(res.toString()))
                return "Just now";
            else
                return res.toString();

        } catch (ParseException e) {

        }

        return IConstants.EMPTY_STRING;
    }

    public static String getRelatedTime2(final String serverTime)
    {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISysConfig.APP_DISPLAY_DATE_TIME_FORMAT, Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("MST"));

            Date serverDate = ((simpleDateFormat)).parse(serverTime);

            return toDuration(System.currentTimeMillis() - serverDate.getTime());
        } catch (ParseException e) {

        }

        return IConstants.EMPTY_STRING;
    }

    public static String toDuration(long duration)
    {
        StringBuffer res = new StringBuffer();
        for(int i=0;i< TimeUtil.times.size(); i++)
        {
            Long current = TimeUtil.times.get(i);
            long temp = duration/current;

            if(temp > 0)
            {
                if(threshold_unit.equals(TimeUtil.timesString.get(i)) && temp < minThreshold)
                {
                    res.append("Just now");
                }
                else if ("day".equals(TimeUtil.timesString.get(i)) && temp == 1)
                {
                    res.append("Yesterday");
                }
                else if ("month".equals(TimeUtil.timesString.get(i)) && temp == 1)
                {
                    res.append("Last month");
                }
                else
                {
                    res.append(temp).append(" ").append( TimeUtil.timesString.get(i) ).append(temp != 1 ? "s" : "");
                }

                break;
            }
        }

        if("".equals(res.toString()))
            return "Just now";
        else
            return res.toString();
    }
}