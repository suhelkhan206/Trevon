/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tws.trevon.common;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.ISysCodes;
import com.tws.trevon.constants.ISysConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author chandra.kalwar
 *  This class will not have DB interactions... so no instance variables as well
 */

/**
 *
 * @author chandra.kalwar
  This class will not have DB interactions... so no instance variables as well
 */
public final class Utilities
{
    private static final String TAG = Utilities.class.getSimpleName();

    public static Integer getRandomInteger(int maxValue, int minValue)
    {
        Random generator = new Random();
        return generator.nextInt((maxValue - minValue) + 1) + minValue;       
    }
    
    public static String parseMessage(String message, Map<String, String> paramsMap)
    {
        if(paramsMap == null)
        {
            return message;
        }
        
        StringBuffer sb = new StringBuffer(message);
        while (sb.indexOf(IConstants.OPENING_BRACES) >= 0)
        {
                int startPos = sb.indexOf(IConstants.OPENING_BRACES) + 1;
                int endPos = sb.indexOf(IConstants.CLOSING_BRACES);
                String strRepValue;
                String strKeyName = sb.substring(startPos, endPos);
                strRepValue = paramsMap.get(strKeyName);
                
                if(strRepValue == null)
                {
                   strRepValue = strKeyName;
                }

                sb=sb.replace(startPos - 1, endPos + 1, strRepValue);
        } 
        return sb.toString();
    }

    public static void checkForDirectory(File file)
    {
        if(!file.exists())
        {
            file.mkdir();
        }
    }
    
    public static void checkForDirectory(String dirPath)
    {
        File file = new File(dirPath);        
        if(!file.exists())
        {
           file.mkdir();
        }         
    }

    public static String getNotNullValue(Object v)
    {
        if(v == null)
        {
            return "";
        }
        else
        {
            return v.toString();
        }
    }

    public static float[] getFloatArray(final String value)
    {
        return new float[] {getNotEmptyFloatValue(value)};
    }

    public static float getNotEmptyFloatValue(final String value)
    {
        if(AppValidate.isEmpty(value))
        {
            return Float.MIN_VALUE;
        }
        else
        {
            return Float.valueOf(value);
        }
    }

    public static String getOnOffByBoolean(boolean bool)
    {
        if(bool)
        {
            return IConstants.ON;
        }
        else
        {
            return IConstants.OFF;
        }
    }

    public static String getLangCodeByBoolean(boolean bool)
    {
        if(bool)
        {
            return ISysCodes.ENGLISH_LANGUAGE_CODE;
        }
        else
        {
            return ISysCodes.HINDI_LANGUAGE_CODE;
        }
    }

    public static String getLanguageStringByCode(String langCode)
    {
        if(ISysCodes.HINDI_LANGUAGE_CODE.equals(langCode))
        {
            return "Hindi";
        }
        else
        {
            return "English";
        }
    }

    public static String getGender(boolean bool)
    {
        if(bool)
        {
            return ISysCodes.FEMALE_CODE;
        }
        else
        {
            return ISysCodes.MALE_CODE;
        }
    }

    public static String getMaritalStatus(boolean bool)
    {
        if(bool)
        {
            return ISysCodes.MARITAL_STATUS_SINGLE;
        }
        else
        {
            return ISysCodes.MARITAL_STATUS_MARRIED;
        }
    }


    public static int getAge(Date dob)
    {
        Calendar dobC = Calendar.getInstance();
        dobC.setTime(dob);
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dobC.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dobC.get(Calendar.DAY_OF_YEAR))
        {
            age--;
        }

        return age;
    }
    
    public static int getDaysBetweenDates(Date bigger, Date smaller)
    {
        long diffInMillis = bigger.getTime() - smaller.getTime();
        Long diffInDays = diffInMillis / (1000 * 60 * 60 * 24);

        return diffInDays.intValue();
    }    
    
    public static List<String> getParamListFromString(String params, String separator)
    {
        List<String> paramsList = new ArrayList<String>();
        String[] s = params.split(separator);
        paramsList.addAll(Arrays.asList(s));
        
        return paramsList;
    } 
    
    public static Integer getTimeDifferenceInSeconds(Date biggerDate, Date smallerDate)
    {
        long diffL = biggerDate.getTime() - smallerDate.getTime();
        Long diffSeconds = diffL / 1000;
        
        return diffSeconds.intValue();
    }
    
    public static Integer getTimeDifferenceInMinutes(Date biggerDate, Date smallerDate)
    {
        long diffL = biggerDate.getTime() - smallerDate.getTime();
        Long diffMinutes = diffL / (60 * 1000);
        
        return diffMinutes.intValue();
    }
    
    public static String getUserImageAbsolutePath(Integer userId, String imgDirPath)
    {
        return imgDirPath + File.separator + userId.toString() + IConstants.DEFAULT_IMG_EXTENSION;
//      String userImageRelativePath = imgDirPath + File.separator + userId.toString() + IConstants.DEFAULT_IMG_EXTENSION;
//      return (getAppAbsolutePath() + userImageRelativePath);        
    }
    
    public static Date addMonths(Date date, int months)
    {        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months); //minus number would decrement the months
        return cal.getTime();
    }
    
    public static Date addWeek(Date date, int weeks)
    {        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_MONTH, weeks); //minus number would decrement the weeks
        return cal.getTime();
    }    
    
    public static Date addDays(Date date, int days)
    {        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
    
    public static Date addHours(Date date, int hours)
    {        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR_OF_DAY, hours); //minus number would decrement the hours
        return cal.getTime();
    }
    
    public static Date addMinutes(Date date, int minutes)
    {        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes); //minus number would decrement the minutes
        return cal.getTime();
    }

    public static Date addSeconds(Date date, int seconds)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.SECOND, seconds); //minus number would decrement the minutes
        return cal.getTime();
    }
    
    /**
     * returns true if firstdate is lesser than secondDate
     *
     * @param firstDate
     * @param secondDate
     * @return a boolean.
     */    
    public static int compareTillMonth(Date firstDate, Date secondDate)
    {
        int GREATER = 1;
        int LESSER = -1;
        int EQUAL = 0;
        int returnValue;
        
        Calendar firstDateCal = new GregorianCalendar();
        firstDateCal.setTime(firstDate);

        Calendar secondDateCal = new GregorianCalendar();
        secondDateCal.setTime(secondDate);            

        if(firstDateCal.get(Calendar.YEAR) > secondDateCal.get(Calendar.YEAR))
        {
            returnValue = GREATER;
        }
        else if(firstDateCal.get(Calendar.YEAR) < secondDateCal.get(Calendar.YEAR))
        {
            returnValue = LESSER;
        }
        else
        {
            if(firstDateCal.get(Calendar.MONTH) > secondDateCal.get(Calendar.MONTH))
            {
                returnValue = GREATER;
            }
            else if(firstDateCal.get(Calendar.MONTH) < secondDateCal.get(Calendar.MONTH))
            {
                returnValue = LESSER; 
            }
            else
            {
                returnValue = EQUAL; 
            }                
        }
        
        return returnValue;
    }
    
    public static int compareTillWeek(Date firstDate, Date secondDate)
    {        
        int returnValue = compareTillMonth(firstDate, secondDate);
        if(returnValue != 0)
        {
            return returnValue;
        }
        
        int GREATER = 1;
        int LESSER = -1;
        int EQUAL = 0;
        
        Calendar firstDateCal = new GregorianCalendar();
        firstDateCal.setTime(firstDate);

        Calendar secondDateCal = new GregorianCalendar();
        secondDateCal.setTime(secondDate);            

        if(firstDateCal.get(Calendar.WEEK_OF_MONTH) > secondDateCal.get(Calendar.WEEK_OF_MONTH))
        {
            returnValue = GREATER;
        }
        else if(firstDateCal.get(Calendar.WEEK_OF_MONTH) < secondDateCal.get(Calendar.WEEK_OF_MONTH))
        {
            returnValue = LESSER;
        }
        else
        {
            returnValue = EQUAL;                
        }
        
        return returnValue;
    }    
    
    public static int compareTillHour(Date firstDate, Date secondDate)
    {
        int returnValue = compareTillDay(firstDate, secondDate);
        if(returnValue != 0)
        {
            return returnValue;
        }
        
        int GREATER = 1;
        int LESSER = -1;
        int EQUAL = 0;
        
        Calendar firstDateCal = new GregorianCalendar();
        firstDateCal.setTime(firstDate);

        Calendar secondDateCal = new GregorianCalendar();
        secondDateCal.setTime(secondDate);            

        if(firstDateCal.get(Calendar.HOUR_OF_DAY) > secondDateCal.get(Calendar.HOUR_OF_DAY))
        {
            returnValue = GREATER;
        }
        else if(firstDateCal.get(Calendar.HOUR_OF_DAY) < secondDateCal.get(Calendar.HOUR_OF_DAY))
        {
            returnValue = LESSER;
        }
        else
        {
            returnValue = EQUAL;               
        }
        
        return returnValue;
    }
    
    public static int compareTillMinute(Date firstDate, Date secondDate)
    {
        int returnValue = compareTillHour(firstDate, secondDate);
        if(returnValue != 0)
        {
            return returnValue;
        }
        
        int GREATER = 1;
        int LESSER = -1;
        int EQUAL = 0;
        
        Calendar firstDateCal = new GregorianCalendar();
        firstDateCal.setTime(firstDate);

        Calendar secondDateCal = new GregorianCalendar();
        secondDateCal.setTime(secondDate);            

        if(firstDateCal.get(Calendar.MINUTE) > secondDateCal.get(Calendar.MINUTE))
        {
            returnValue = GREATER;
        }
        else if(firstDateCal.get(Calendar.MINUTE) < secondDateCal.get(Calendar.MINUTE))
        {
            returnValue = LESSER;
        }
        else
        {
            returnValue = EQUAL;               
        }
        
        return returnValue;
    }    
    
    /**
     * returns true if firstdate is lesser than secondDate
     *
     * @param firstDate
     * @param secondDate
     * @return a boolean.
     */    
    public static boolean compareTime(Date firstDate, Date secondDate)
    {
        boolean returnValue;
        
        Calendar firstDateCal = new GregorianCalendar();
        firstDateCal.setTime(firstDate);

        Calendar secondDateCal = new GregorianCalendar();
        secondDateCal.setTime(secondDate);            

        if(firstDateCal.get(Calendar.HOUR_OF_DAY) < secondDateCal.get(Calendar.HOUR_OF_DAY))
        {
            returnValue = true;
        }
        else if(firstDateCal.get(Calendar.HOUR_OF_DAY) == secondDateCal.get(Calendar.HOUR_OF_DAY))
        {
            if(firstDateCal.get(Calendar.MINUTE) < secondDateCal.get(Calendar.MINUTE))
            {
                returnValue = true;
            }
            else if(firstDateCal.get(Calendar.MINUTE) == secondDateCal.get(Calendar.MINUTE))
            {
                returnValue = firstDateCal.get(Calendar.SECOND) <= secondDateCal.get(Calendar.SECOND); 
            }
            else
            {
                returnValue = false; 
            }                
        }
        else
        {
            returnValue = false; 
        }
        
        return returnValue;
    }

    /**
     * returns if(firstDate<secondDate) : -1
     * * returns if(firstDate==secondDate) : 0
     * * returns if(firstDate>secondDate) : 1
     */
    public static int compareTime(long firstDate, long secondDate)
    {
        int returnValue;

        Calendar firstDateCal = new GregorianCalendar();
        firstDateCal.setTimeInMillis(firstDate);

        Calendar secondDateCal = new GregorianCalendar();
        secondDateCal.setTimeInMillis(secondDate);

        if(firstDateCal.get(Calendar.HOUR_OF_DAY) < secondDateCal.get(Calendar.HOUR_OF_DAY))
        {
            returnValue = -1;
        }
        else if(firstDateCal.get(Calendar.HOUR_OF_DAY) == secondDateCal.get(Calendar.HOUR_OF_DAY))
        {
            if(firstDateCal.get(Calendar.MINUTE) < secondDateCal.get(Calendar.MINUTE))
            {
                returnValue = -1;
            }
            else if(firstDateCal.get(Calendar.MINUTE) == secondDateCal.get(Calendar.MINUTE))
            {
                if(firstDateCal.get(Calendar.SECOND) < secondDateCal.get(Calendar.SECOND))
                {
                    returnValue = -1;
                }
                else if(firstDateCal.get(Calendar.SECOND) == secondDateCal.get(Calendar.SECOND))
                {
                    returnValue = 0;
                }
                else
                {
                    returnValue = 1;
                }
            }
            else
            {
                returnValue = 1;
            }
        }
        else
        {
            returnValue = 1;
        }

        return returnValue;
    }
    
//    public static void processException(Exception e)
//    {
//        logger.error(e.getLocalizedMessage());
//        
//        if(ApplicationData.getProperty(IProperties.APPLICATION_MODE).equals(ISysConfig.APPLICATION_DEVELOPMENT_MODE))
//        {
//            e.printStackTrace();
//        }        
//    }
    
//    public static void processException(Throwable t)
//    {
//        logger.error(t.getLocalizedMessage());
//        
//        if(ApplicationData.getProperty(IProperties.APPLICATION_MODE).equals(ISysConfig.APPLICATION_DEVELOPMENT_MODE))
//        {
//            t.printStackTrace();
//        }        
//    }    
    
    public static boolean getBooleanNotif(String stringNotif)
    {
        return IConstants.YES.equals(stringNotif);               
    }
    
    public static String getBooleanString(boolean bool)
    {
        if(bool)               
        {
            return IConstants.BOOLEAN_TRUE_STRING;
        }
        else
        {
            return IConstants.BOOLEAN_FALSE_STRING;
        }
    }

    public static int[] getFirstRange(int rangeChunk)
    {        
        return new int[]{0, rangeChunk -1};
    }    
    
    public static int[] getNextRange(int[] prevRange, int rangeChunk)
    {        
        return new int[]{prevRange[1] + 1, prevRange[1] + rangeChunk};
    }
    
    public static boolean isAmongValidValues(String value, String validValues, String validValuesSeparator)
    {
        String[] validValuesArray = validValues.split(validValuesSeparator);
        
        for (String validValue : validValuesArray)
        {
            if(validValue.equals(value))
            {
                return true;
            }
        } 

        return false;
    }

    private static Throwable getLastThrowable(Exception e)
    {
        Throwable t = e.getCause();
        
        while(t != null)
        {
            t = e.getCause();
        }
        
        return t;
    }

    public static String getStackTrace(Throwable t)
    {
        String stackTrace = IConstants.EMPTY_STRING;
        StringWriter sw = null;
        PrintWriter pw = null;

        try
        {
            sw = new StringWriter();
            pw = new PrintWriter(sw);

            t.printStackTrace(pw);
            stackTrace = sw.getBuffer().toString();
        }
        catch(Exception ex)
        {

        }
        finally
        {
            if(pw != null)
            {
                pw.close();
            }

            if(sw != null)
            {
                try
                {
                    sw.close();
                }
                catch (IOException e)
                {

                }
            }
        }

        return stackTrace;
    }

    public static double getPercentage(double obtainedMarks, int totalMarks)
    {
        double percentage = obtainedMarks*100/totalMarks;
        percentage = Math.round(percentage*100);
        percentage /= 100; 
        return percentage;
    }
    
   public static Character calculateGrade(double percentage)
    {
        String grade;
        if(percentage >= 60)
        {
            grade = "A";
        }
        else if(percentage >= 50)
        {
            grade = "B";
        }
        else if(percentage >= 40)
        {
            grade = "C";
        } 
        else if(percentage >= 30)
        {
            grade = "D";
        }
        else
        {
            grade = "E";
        } 
        
        return grade.charAt(0);
    }

    public static <T> ArrayList<T> getNotNullList(ArrayList<T> list)
    {
        if(list == null)
        {
            list = new ArrayList<>();
        }

        return list;
    }

    public static <T> List<T> getNotNullList(List<T> list)
    {
        if(list == null)
        {
            list = new ArrayList<>();
        }

        return list;
    }
    
    public static String getTimeString(Date date)
    {
        SimpleDateFormat sf = new SimpleDateFormat(ISysConfig.APP_DISPLAY_DATE_TIME_FORMAT, Locale.getDefault());
        return sf.format(date);
    }

    public static String getTimeString(final long longTime)
    {
        return getTimeString(new Date(longTime));
    }

    public static String getTimeOnlyString(final long longTime)
    {
        SimpleDateFormat sf = new SimpleDateFormat(ISysConfig.APP_DISPLAY_TIME_FORMAT_SMALLER, Locale.getDefault());
        return sf.format(new Date(longTime));
    }
    
    public static String getDateString(Date date)
    {
        SimpleDateFormat sf = new SimpleDateFormat(ISysConfig.APP_DISPLAY_DATE_FORMAT, Locale.getDefault());
        return sf.format(date);
    }

    public static String getDateWeekString(long milliSeconds)
    {
        SimpleDateFormat sf = new SimpleDateFormat(ISysConfig.APP_DISPLAY_DATE_WEEK_FORMAT, Locale.getDefault());
        return sf.format(new Date(milliSeconds));
    }

    public static String getDateNumericString(final long milliSeconds)
    {
        SimpleDateFormat sf = new SimpleDateFormat(ISysConfig.APP_NUMERIC_DATE_FORMAT, Locale.getDefault());
        return sf.format(new Date(milliSeconds));
    }

    public static String getDateString(long milliSeconds)
    {
        SimpleDateFormat sf = new SimpleDateFormat(ISysConfig.APP_DISPLAY_DATE_FORMAT, Locale.getDefault());
        return sf.format(new Date(milliSeconds));
    }

    public static String getDateString(final int day, final int month, final int year)
    {
        return getDateString(getDate(day, month, year));
    }

    public static Date getDateFromString(final String dateString)
    {
        SimpleDateFormat sf = new SimpleDateFormat(ISysConfig.APP_DISPLAY_DATE_FORMAT, Locale.getDefault());
        try {
            return sf.parse(dateString);
        } catch (ParseException e) {

        }

        return null;
    }

    public static Date getDateFromNumericString(final String dateString)
    {
        SimpleDateFormat sf = new SimpleDateFormat(ISysConfig.APP_NUMERIC_DATE_FORMAT, Locale.getDefault());
        try {
            return sf.parse(dateString);
        } catch (ParseException e) {

        }

        return null;
    }

    public static Date getDate(final int day, final int month, final int year)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }
    
    public static String getValidFileName(String fileName)
    {
        return fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
    }
    
    public static void deleteFilesOlderThanNdays(long daysBack, String dirWay)  
     {  
         File directory = new File(dirWay);  
         if(directory.exists())  
         {     
             File[] listFiles = directory.listFiles();  
             long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);  
             for(File listFile : listFiles)  
             {  
                 if(listFile.lastModified() > purgeTime)  
                 {  
                     System.out.println("File or directory name: " + listFile);  
                     if (listFile.isFile())  
                     {  
                         listFile.delete();  
                     }  
                     else  
                     {    
                         deleteFilesOlderThanNdays(daysBack, listFile.getAbsolutePath());  
                     }  
                 }  
             }  
         }   
         else  
              Log.w(TAG, "Files were not deleted, directory " + dirWay + " does'nt exist!");
     }

    public static void deleteAndReCreateDirectory(File file)
    {
        if(!file.exists())
        {
            file.mkdir();
        }
        else
        {
            deleteFilesRecursive(file);
            checkForDirectory(file);
        }
    }

    public static void deleteFilesRecursive(File fileOrDirectory)
    {
        if (fileOrDirectory.isDirectory())
        {
            for (File child : fileOrDirectory.listFiles())
            {
                deleteFilesRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }
    
    public static String getUserFullName(String namePrefix, String firstName, String middleName, String lastName)
    {
        StringBuilder fullName = new StringBuilder();
        
        fullName = fullName.append(namePrefix).append(IConstants.SPACE).append(firstName).append(IConstants.SPACE);
        
        if (middleName != null)
        {
            fullName = fullName.append(middleName).append(IConstants.SPACE);
        }
        
        if (lastName != null)
        {
            fullName = fullName.append(lastName);
        }
        
        return fullName.toString();        
    }
    
    public static void deleteFileByFileName(String fileName)
    {
        File file = new File(fileName);

        if(file.exists())
            deleteFile(file);
    }

/*    public static void main(String[] args)
    {
        Random rand = new Random();
        int randomNum = getRandomInteger(9999, 1000);
        String s = "sw" + randomNum;
    }*/

    public static Object getConvertedValue(String value, String convertTo)
    {
        if(AppValidate.isEmpty(value))
        {
            throw new NullPointerException();
        }
        
        Object convertedValue = null;
/*
        switch(convertTo)
        {
            case ISysCodes.INTEGER_TYPE_INDICATOR :
                convertedValue = Integer.valueOf(value);
                break;
            case ISysCodes.STRING_TYPE_INDICATOR :
                convertedValue = value;
                break;
            case ISysCodes.DOUBLE_TYPE_INDICATOR :
                convertedValue = Double.valueOf(value);
                break; 
            case ISysCodes.CHARACTER_TYPE_INDICATOR :
                convertedValue = value.charAt(0);
                break;
            case ISysCodes.BOOLEAN_TYPE_INDICATOR :
                convertedValue = Boolean.valueOf(value);
                break;
            case ISysCodes.FLOAT_TYPE_INDICATOR :
                convertedValue = Float.valueOf(value);
                break;
            case ISysCodes.LONG_TYPE_INDICATOR :
                convertedValue = Long.valueOf(value);
                break; 
            default:
                throw new UnsupportedOperationException(convertTo + " org preference type not supported");
        }
*/

        return convertedValue;
    }

    
    public static int compareTillDay(Date firstDate, Date secondDate)
    {
        return getDateWithOutTime(firstDate).compareTo(getDateWithOutTime(secondDate));
    }    
    
    public static Date getDateWithOutTime(Date dateToBeConverted)
    {
//        Date resultDate = dateToBeConverted;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(dateToBeConverted);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date resultDate = calendar.getTime();

        return resultDate;
    }

    public static void processException(final String tag, final Throwable t)
    {
        logMsg(TAG, "", ISysCodes.LOG_MODE_ERROR, t);

        if(ISysConfig.APPLICATION_MODE.equals(ISysConfig.APPLICATION_DEVELOPMENT_MODE))
        {
            t.printStackTrace();

/*            if(context != null)
            {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }*/
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

/*    public static String getCompleteUrl(final String relativeUrl)
    {
        return ISysConfig.PRIVATE_CLOUD_URL + relativeUrl;
    }*/

    public static String getFileNameWithoutExtension(final String fileNameWithExtension)
    {
        return fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf(IConstants.FILE_EXTENSION_SEPARATOR));
    }

/*    public static String addDefaultNotifPrefix(final String fileName)
    {
        return "notif_" + getFileNameWithoutExtension(fileName).toLowerCase();
    }*/

    public static String getConvertedListToStringMsg(final List<String> msgList)
    {
        if(AppValidate.isEmpty(msgList))
        {
            return IConstants.EMPTY_STRING;
        }
        else if(msgList.size() == 1)
        {
            return msgList.get(0);
        }
        else
        {
            StringBuilder msgSB = new StringBuilder();
            for (int i = 0; i < (msgList).size(); i++)
            {
                if(i > 0)
                {
                    msgSB.append(IConstants.SYSTEM_LINE_SEPARATOR);
                }

                msgSB.append(i+1).append(" : ").append((msgList).get(i));
            }

            return msgSB.toString();
        }
    }

    public static void showObjectHierarchy(final String TAG, Class<?> objectClass)
    {
        if(objectClass.getSuperclass() == null)
        {
            Log.i(TAG, objectClass.getName());
            return;
        }
        else
        {
            Log.i(TAG, objectClass.getName());
            System.out.println(objectClass.getName());
        }
    }

    public static boolean getBooleanFromString(final String bool)
    {
        if(AppValidate.isEmpty(bool))
        {
            return false;
        }

        return bool.equals("1");
    }

    public static Object runValidationOnData(String fieldData, int validationIdentifier)
    {
        Object returnObject = null;
        switch(validationIdentifier)
        {
            case ISysConfig.ALPHABETIC_VALIDATION:
                if(AppValidate.isAlphabetic(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
            case ISysConfig.DATE_VALIDATION:
                returnObject = AppValidate.isDate(fieldData);
                break;
            case ISysConfig.PINCODE_VALIDATION:
                if(AppValidate.isPinCode(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
            case ISysConfig.LANDLINE_VALIDATION:
                if(AppValidate.isLandLine(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
            case ISysConfig.MOBILE_NO_VALIDATION:
                if(AppValidate.isMobileNumber(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
            case ISysConfig.EMAIL_VALIDATION:
                if(AppValidate.isEmail(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
            case ISysConfig.ALPHANUMERIC_VALIDATION:
                if(AppValidate.isAlphaNumericOnly(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
            case ISysConfig.NUMERIC_VALIDATION:
                if(AppValidate.isNumeric(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
            case ISysConfig.POSITIVE_VALIDATION:
                returnObject = AppValidate.isPositiveInteger(fieldData);
                break;
            case ISysConfig.ALPHABETIC_WITH_SPACES_VALIDATION:
                returnObject = Boolean.FALSE;
                if(AppValidate.isAlphabeticWithSpaces(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
            case ISysConfig.ALPHANUMERIC_WITH_SPACES_VALIDATION:
                returnObject = Boolean.FALSE;
                if(AppValidate.isAlphaNumericWithSpace(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
            case ISysConfig.ALPHANUMERIC_WITH_COMMA_SEPARATED:
                returnObject = Boolean.FALSE;
                if(AppValidate.isAlphaNumericWithComaSeperated(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
            case ISysConfig.ALPHANUMERIC_WITH_GENERAL_SPECIAL_CHARS:
                returnObject = Boolean.FALSE;
                if(AppValidate.isAlphaNumericWithGeneralSpecialChars(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
            case ISysConfig.ALPHANUMERIC_WITH_SPECIAL_CHARS_AND_SPACE:
                returnObject = Boolean.FALSE;
                if(AppValidate.isAlphaNumericAndSpecialCharsWithSpaces(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
            case ISysConfig.DOUBLE_VALIDATION:
                returnObject = AppValidate.isDouble(fieldData);
                break;
            case ISysConfig.INTEGER_PERCENTAGE:
                returnObject = AppValidate.isIntegerPercentage(fieldData);
                break;
            case ISysConfig.BOOLEAN_STRING:
                returnObject = AppValidate.isBoolean(fieldData);
                break;
            case ISysConfig.POSITIVE_LONG_VALIDATION:
                returnObject = AppValidate.isPositiveLong(fieldData);
                break;
            case ISysConfig.SIGNED_DOUBLE_VALIDATION:
                returnObject = AppValidate.isSignedDoubleValue(fieldData);
                break;
            case ISysConfig.ALLOW_ALL_LANGUAGE_CHARACTERS_VALIDATION:
                returnObject = Boolean.TRUE;
                break;
            case ISysConfig.STRONG_PASSWORD_VALIDATION:
                if(AppValidate.isStrongPassword(fieldData))
                {
                    returnObject = Boolean.TRUE;
                }
                break;
        }

        return returnObject;
    }

    public static int greatestCommonDivisor(int p, int q)
    {
        if (q == 0)
        {
            return p;
        }
        else
        {
            return greatestCommonDivisor(q, p % q);
        }
    }

    public static int getFullIntegerRatio(int a, int b)
    {
        return a/greatestCommonDivisor(a,b);
    }

    /*public static void createFile(File file, InputStream in)
    {
        try(OutputStream out = new FileOutputStream(file))
        {
            int read;
            byte[] bytes = new byte[1_024];

            while ((read = in.read(bytes)) != -1)
            {
                out.write(bytes, 0, read);
            }

            out.flush();
        }
        catch (IOException ex)
        {

        }
    }*/

    public static String getTempImageName()
    {
        return Long.toString(System.currentTimeMillis()) + IConstants.DEFAULT_IMG_EXTENSION;
    }

    public static String getTempImageName(final String prefix)
    {
        return prefix + "_" + Long.toString(System.currentTimeMillis()) + IConstants.DEFAULT_IMG_EXTENSION;
    }

    public static String addDefaultShareBodyStart(final String body)
    {
        return ISysConfig.SHARE_DEFAULT_BODY_START + body;
    }

    public static String getTimeFromMilliSeconds(final long milliSeconds)
    {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds);

        return String.format("%02d : %02d", minutes, TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(minutes));
    }

    public static String getTimeFromMilliSeconds2(final long milliSeconds)
    {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds);

        return String.format("%02d min : %02d sec", minutes, TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(minutes));
    }

    public static String getTiming(final Date fromDate, final Date toDate)
    {
        SimpleDateFormat sf = new SimpleDateFormat(ISysConfig.APP_DISPLAY_TIME_FORMAT, Locale.getDefault());
        String timing = sf.format(fromDate);
        timing = timing + " - " + sf.format(toDate);
        return timing;
    }

    public static String getTimeStringFromDate(final Date date)
    {
        Calendar calDate = getCalendar(date);
        return getTimeFromHourAndMinutes(calDate.get(Calendar.HOUR_OF_DAY), calDate.get(Calendar.MINUTE));
    }

    public static int compareLongTime(final long long1, final long long2)
    {
        if(long1 == long2)
        {
            return 0;
        }
        else if(long2 > long1)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    public static int compareImportance(String string1, String string2)
    {
        if(string1.equals(string2))
        {
            return 0;
        }
        else if(ISysCodes.IMPORTANCE_HIGH.equals(string2) || ISysCodes.IMPORTANCE_LOW.equals(string1))
        {
            return 1;
        }
        else if(ISysCodes.IMPORTANCE_LOW.equals(string2) || ISysCodes.IMPORTANCE_HIGH.equals(string1))
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }

    public static String getOrgNameString(final String orgName)
    {
        return " [" + orgName + "]";
    }

    public static String getYesOrNo(boolean bool)
    {
        if(bool)
        {
            return IConstants.YES_STRING;
        }
        else
        {
            return IConstants.NO_STRING;
        }
    }

    public static String getYOrN(boolean bool)
    {
        if(bool)
        {
            return IConstants.YES;
        }
        else
        {
            return IConstants.NO;
        }
    }

    public static String trimInternalSpacesToOne(String string)
    {
        return string.replaceAll("\\s+", " ");
    }

    public static String trimAllInternalSpaces(String string)
    {
        return string.replaceAll("\\s+", "");
    }

   /* public static List<LinearGrid2> getLinearGrid2List(final List<GridItem> gridItemList)
    {
        List<LinearGrid2> linearGrid2List = new ArrayList<>();
        LinearGrid2 linearGrid2 = new LinearGrid2();

        for(GridItem gridItem : gridItemList)
        {
            if(linearGrid2.firstGridItem == null)
            {
                linearGrid2.firstGridItem = gridItem;
                linearGrid2List.add(linearGrid2);
            }
            else if(linearGrid2.secondGridItem == null)
            {
                linearGrid2.secondGridItem = gridItem;
                linearGrid2 = new LinearGrid2();
            }
        }

        return linearGrid2List;
    }*/

    public static String getReadableName(String name)
    {
        if(AppValidate.isEmpty(name))
        {
            return IConstants.EMPTY_STRING;
        }
        else if(name.length() > 1)
        {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        else
        {
            return name.substring(0, 1).toUpperCase();
        }
    }

    public static double roundedAmount(double value)
    {
        return round(value, ISysConfig.AMOUNT_ROUND_UP_PLACES);
    }

    public static Double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String getFileNameFromFilePath(final String filepath)
    {
        if(AppValidate.isEmpty(filepath))
        {
            return IConstants.EMPTY_STRING;
        }
        else
        {
            String pathSeparator;
            if(filepath.contains("\\"))
            {
                pathSeparator = "\\";
            }
            else
            {
                pathSeparator = "/";
            }

            return filepath.substring(filepath.lastIndexOf(pathSeparator) + 1);
        }
    }

    public static String getThumbNailImageFilePath(final String filepathWithExtension)
    {
        if(AppValidate.isEmpty(filepathWithExtension))
        {
            return IConstants.EMPTY_STRING;
        }
        else
        {
            return filepathWithExtension.substring(0, filepathWithExtension.lastIndexOf(IConstants.FILE_EXTENSION_SEPARATOR))
                    + IConstants.THUMBNAIL_IMAGE_INDICATOR
                    + filepathWithExtension.substring(filepathWithExtension.lastIndexOf(IConstants.FILE_EXTENSION_SEPARATOR));
        }
    }

    public static ArrayList<String> getThumbNailUrlList(final ArrayList<String> urlList)
    {
        ArrayList<String> convertedUrlList = new ArrayList();
        for(String url : urlList)
        {
            convertedUrlList.add(getThumbNailImageFilePath(url));
        }

        return convertedUrlList;
    }

    public static String getNotNullSearchText(Object v)
    {
        if(v == null)
        {
            return "";
        }
        else
        {
            return v.toString().toLowerCase() + " ";
        }
    }

    public static boolean isRefreshRequired(final String lastRefreshTime, final int refreshIntervalInSeconds)
    {
        if(AppValidate.isEmpty(lastRefreshTime))
        {
            return true;
        }
        else
        {
            Date toBeRefreshedOnTime = Utilities.addSeconds(new Date(Long.valueOf(lastRefreshTime)), refreshIntervalInSeconds);
            return toBeRefreshedOnTime.before(new Date());
        }
    }

   /* public static Class getHomeClass()
    {
        *//*if(ISysConfig.isPersonalApp)
        {
            if(ISysConfig.isUsingExtendedHome)
            {
                if(ISysConfig.PERSONAL_APP_ORG_TYPE.equals(ISysCodes.SHOPPING_ORGANIZATION_TYPE))
                {
                    return com.pnp.android.activity.ShopHome.class;
                }
            }
        }*//*

        return com.pnp.android.activity.Home.class;
    }*/

    public static Date getDateFromTimeString(final String timeString)
    {
        String[] elements = timeString.split(IConstants.COLON_SEPARATOR);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(elements[0]));
        cal.set(Calendar.MINUTE, Integer.valueOf(elements[1]));
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    public static String getExpectedDeliveryTime(final int afterDays, final String timeFrame)
    {
        String expectedDeliveryTime = getDateString(addDays(new Date(), afterDays));
        return expectedDeliveryTime + ",  " + timeFrame;
    }

    public static String getExpectedDeliveryTime(final String minDeliveryTimeString, final String maxDeliveryTimeString, final int approximateDeliveryTime)
    {
        Date maxDeliveryTime = Utilities.getDateFromTimeString(maxDeliveryTimeString);
        Date currDate = new Date();
        Date expectedDeliveryTime = addMinutes(currDate, approximateDeliveryTime);

        if(expectedDeliveryTime.after(maxDeliveryTime))
        {
            Date minDeliveryTime = addDays(Utilities.getDateFromTimeString(minDeliveryTimeString), 1);
            expectedDeliveryTime = addMinutes(minDeliveryTime, approximateDeliveryTime);
        }

        return Utilities.getTimeString(expectedDeliveryTime);
    }

    public static int getResizedHeightByIntendedWidth(final Bitmap photo, final int intendedWidth)
    {
        if(photo == null)
        {
            return intendedWidth;
        }
        else
        {
            if (intendedWidth > photo.getWidth())
            {
                float widthScalePercentage = (float)(intendedWidth - photo.getWidth())*100/photo.getWidth();
                float increaseHeightBy = (float)photo.getHeight() * widthScalePercentage/100;
                return photo.getHeight() + (int)increaseHeightBy;
            }
            else if (intendedWidth < photo.getWidth())
            {
                float widthScalePercentage = (float)(photo.getWidth() - intendedWidth)*100/photo.getWidth();
                float decreaseHeightBy = (float)photo.getHeight() * widthScalePercentage/100;
                return photo.getHeight() - (int)decreaseHeightBy;
            }
            else
            {
                return photo.getHeight();
            }
        }
    }

    public static Map<String, Integer> getDimensionsForCrop(final Bitmap photo)
    {
        Map dimensionMap = new HashMap();
        final int actualWidth = photo.getWidth();
        final int actualHeight = photo.getHeight();
        int width;
        int height;

        double ratio = (double)actualWidth/actualHeight;

        if(actualWidth > actualHeight)
        {
            if(actualWidth > ISysConfig.FULL_IMAGE_MAX_WIDTH)
            {
                width = ISysConfig.FULL_IMAGE_MAX_WIDTH;
            }
            else
            {
                width = actualWidth;
            }

            height = (new Double(round(width/ratio, 0))).intValue();
        }
        else
        {
            if(actualHeight > ISysConfig.FULL_IMAGE_MAX_HEIGHT)
            {
                height = ISysConfig.FULL_IMAGE_MAX_HEIGHT;
            }
            else
            {
                height = actualHeight;
            }

            width = (new Double(round(height*ratio, 0))).intValue();
        }

        dimensionMap.put(IConstants.WEIGHT, width);
        dimensionMap.put(IConstants.HEIGHT, height);

        return dimensionMap;
    }

    public static boolean moveFile(final String srcFilePath, final String destFilePath)
    {
        boolean isSuccessful = false;
        InputStream inStream = null;
        OutputStream outStream = null;

        try{

            File srcFile =new File(srcFilePath);
            File destFile =new File(destFilePath);

            inStream = new FileInputStream(srcFile);
            outStream = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes
            while ((length = inStream.read(buffer)) > 0)
            {
                outStream.write(buffer, 0, length);
            }

            outStream.flush();
/*            inStream.close();
            outStream.close();*/

            //delete the original file
            srcFile.delete();
            isSuccessful = true;

        }catch(IOException e)
        {

        }
        finally
        {
            if(inStream != null)
            {
                try {
                    inStream.close();
                } catch (IOException e1)
                {

                }
            }

            if(outStream != null)
            {
                try {
                    outStream.close();
                } catch (IOException e1)
                {

                }
            }
        }

        return isSuccessful;
    }

    public static String getReadableFileSize(final long fileSizeInBytes)
    {
        if(fileSizeInBytes > 1024)
        {
            double sizeInKB = (double)fileSizeInBytes/1024;

            if(sizeInKB > 1024)
            {
                double sizeInMB = sizeInKB/1024;

                if(sizeInMB > 1024)
                {
                    return Utilities.round(sizeInMB /1024, ISysConfig.DEFAULT_ROUND_UP_PLACES) + " GB";
                }
                else
                {
                    return Utilities.round(sizeInMB, ISysConfig.DEFAULT_ROUND_UP_PLACES) + " MB";
                }
            }
            else
            {
                return Utilities.round(sizeInKB, ISysConfig.DEFAULT_ROUND_UP_PLACES) + " KB";
            }
        }
        else
        {
            return Utilities.round(fileSizeInBytes, ISysConfig.DEFAULT_ROUND_UP_PLACES) + " Bytes";
        }
    }

    public static String getBloodGroupString(final int bgCode)
    {
        switch(bgCode)
        {
            case ISysCodes.NONE_BLOOD_GROUP:
                return "None";
            case ISysCodes.A_POSITIVE_BLOOD_GROUP:
                return "A+";
            case ISysCodes.A_NEGATIVE_BLOOD_GROUP:
                return "A-";
            case ISysCodes.B_POSITIVE_BLOOD_GROUP:
                return "B+";
            case ISysCodes.B_NEGATIVE_BLOOD_GROUP:
                return "B-";
            case ISysCodes.O_POSITIVE_BLOOD_GROUP:
                return "O+";
            case ISysCodes.O_NEGATIVE_BLOOD_GROUP:
                return "O-";
            case ISysCodes.AB_POSITIVE_BLOOD_GROUP:
                return "AB+";
            case ISysCodes.AB_NEGATIVE_BLOOD_GROUP:
                return "AB-";
        }

        return "None";
    }

    public static String getGenderCharacter(final boolean bool)
    {
        if(bool)
        {
            return ISysCodes.FEMALE_GENDER_CHARACTER_CODE;
        }
        else
        {
            return ISysCodes.MALE_GENDER_CHARACTER_CODE;
        }
    }

    public static String getMaritalStatusCharacter(final boolean bool)
    {
        if(bool)
        {
            return ISysCodes.SINGLE_MARITAL_STATUS_CHARACTER_CODE;
        }
        else
        {
            return ISysCodes.MARRIED_MARITAL_STATUS_CHARACTER_CODE;
        }
    }

    public static int getDiffYears(Date first, Date last)
    {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
                (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String getNumericDate(final Date targetDate)
    {
        return (new SimpleDateFormat("ddMMM")).format(targetDate);
    }

    public static String getNumericTime(final Date targetDate)
    {
        return (new SimpleDateFormat("mmhhddMMM")).format(targetDate);
    }

    public static String getNumericTimeSmall(final Date targetDate)
    {
        return (new SimpleDateFormat("mmhh")).format(targetDate);
    }

    public static String getTimeFromHourAndMinutes(final int hour, final int minutes)
    {
        return Utilities.pad(hour) + ":" +  Utilities.pad(minutes);
    }

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static String getTimeDifferenceString(final Date smallDate, final Date bigDate)
    {
        long diff = bigDate.getTime() - smallDate.getTime();
/*        if(diff < 0)
        {
            diff = -1 * diff;
        }
        else*/ if(diff == 0)
        {
            return "0 seconds";
        }

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        boolean isStringStarted = false;
        String representation = IConstants.EMPTY_STRING;
        if(diffDays > 0)
        {
            isStringStarted = true;
            representation = diffDays + " days";
        }

        if(diffHours > 0)
        {
            if(isStringStarted)
            {
                representation = representation + ", ";
            }

            isStringStarted = true;
            representation = representation + diffHours + " hours";
        }

        if(diffMinutes > 0)
        {
            if(isStringStarted)
            {
                representation = representation + ", ";
            }

            isStringStarted = true;
            representation = representation + diffMinutes + " minutes";
        }

        if(diffSeconds > 0)
        {
            if(isStringStarted)
            {
                representation = representation + ", ";
            }

            isStringStarted = true;
            representation = representation + diffSeconds + " seconds";
        }

        return representation;
    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2)
    {
        double earthRadius = 6370.99056; //km
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }

    public static void main(String[] args)
    {
        double dist = distFrom(25.1867802d,
                75.8570223d,
                25.2017832d,
                75.8589977d);

        System.out.println(dist + " ");
    }

    public static String getUrlFromHtmlImgTag(String htmlText)
    {
        String imgPath = "";
        if(AppValidate.isNotEmpty(htmlText))
        {
            if(htmlText.contains("<img"))
            {
                int startIndex = htmlText.indexOf("http", htmlText.indexOf("<img"));
                String stringFromUrl = htmlText.substring(startIndex);

                int lastIndex = stringFromUrl.indexOf("\"");
                if(lastIndex == -1)
                {
                    lastIndex = stringFromUrl.indexOf("/>");

                    if(lastIndex == -1)
                    {
                        lastIndex = stringFromUrl.indexOf(">");
                    }
                }

                imgPath = stringFromUrl.substring(0, lastIndex);

            }
        }

        return imgPath;
    }



    public static String removeAllHtmlTag(String htmlString)
    {
        if(htmlString.contains("<"))
        {
            int startIndex = htmlString.indexOf("<");
            int endIndex = htmlString.indexOf(">");

            if(endIndex > startIndex)
            {
                String stringBeforeTag = htmlString.substring(0, startIndex);
                String stringAfterTag = htmlString.substring(endIndex + 1);
                String finalString = stringBeforeTag + stringAfterTag;

                return removeAllHtmlTag(finalString);
            }
        }

        return htmlString;
    }

    public static String removeHtmlTag(final String tagName, String htmlString)
    {
        int startIndex = -1;
        if(AppValidate.isNotEmpty(htmlString) && htmlString.contains("<" + tagName))
        {
            startIndex = htmlString.indexOf("<" + tagName);
        }
        else if(htmlString.contains("</" + tagName))
        {
            startIndex = htmlString.indexOf("</" + tagName);
        }

        if(startIndex != -1)
        {
            int endIndex = htmlString.indexOf(">", startIndex);

            if(endIndex > startIndex)
            {
                String stringBeforeTag = htmlString.substring(0, startIndex);
                String stringAfterTag = htmlString.substring(endIndex + 1);
                String finalString = stringBeforeTag + stringAfterTag;

                return removeHtmlTag(tagName, finalString);
            }
        }

        return htmlString;
    }

    public static boolean isToday(Date dateToCheck)
    {
        Calendar calToCheck = getCalendar(dateToCheck);
        Calendar calToday = getCalendar(new Date());

        return calToday.get(Calendar.DAY_OF_YEAR) == calToCheck.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isTodayOrFuture(Date dateToCheck)
    {
        Calendar calToCheck = getCalendar(dateToCheck);
        Calendar calToday = getCalendar(new Date());

        return calToday.get(Calendar.DAY_OF_YEAR) <= calToCheck.get(Calendar.DAY_OF_YEAR);
    }

    public static String getAppLinkPageCode(final String appLink)
    {
        return (appLink.split("-"))[1];
    }

    public static String getCommaSeparatedString(final ArrayList<String> inputList)
    {
        return getTokenSeparatedString(inputList, ",");
    }

    public static String getTokenSeparatedString(final ArrayList<String> inputList, final String separator)
    {
        String commaSeparatedString = IConstants.EMPTY_STRING;
        for(int i = 0; i < inputList.size(); i++)
        {
            String data = inputList.get(i);
            commaSeparatedString = commaSeparatedString + data;

            if(i != (inputList.size() - 1))
            {
                commaSeparatedString = commaSeparatedString + separator;
            }
        }

        return commaSeparatedString;
    }

    public static ArrayList<String> getNextNItems(final ArrayList<String> itemList, int startIndex, final int chunkSize)
    {
        final ArrayList<String> nextList = new ArrayList<>();
        if(AppValidate.isEmpty(itemList))
        {
            return nextList;
        }

        if(startIndex < 0)
        {
            startIndex = 0;
        }

        int endIndex = startIndex + chunkSize - 1;
        if(endIndex >= itemList.size())
        {
            endIndex = itemList.size() - 1;
        }

        for(int i = startIndex; i <= endIndex; i++)
        {
            nextList.add(itemList.get(i));
        }

        return nextList;
    }

    public static boolean isTrue(final String booleanString)
    {
        if(AppValidate.isEmpty(booleanString))
        {
            return false;
        }
        else return IConstants.TRUE_1_STRING.equals(booleanString)
                || IConstants.TRUE_T_STRING.equals(booleanString)
                || IConstants.YES.equals(booleanString)
                || Boolean.TRUE.toString().equals(booleanString);
    }

    public static int[] addArray(int[] firstArray, final int[] secondArray)
    {
        int[] finalArray = new int[firstArray.length + secondArray.length];

        for(int i = 0; i < firstArray.length ; i++)
        {
            finalArray[i] = firstArray[i];
        }

        for(int j = 0; j < secondArray.length ; j++)
        {
            finalArray[j + firstArray.length] = secondArray[j];
        }

        return finalArray;
    }

    public static long decreaseCountInLimit(long currentCount, int limit)
    {
        currentCount = currentCount - 1;
        if(currentCount < limit)
        {
            return limit;
        }
        else
        {
            return currentCount;
        }
    }

    //http://stackoverflow.com/questions/26893796/how-set-emoji-by-unicode-in-android-textview
    //http://apps.timwhitlock.info/emoji/tables/unicode
    public static String getEmojiByUnicode(int unicode)
    {
        return new String(Character.toChars(unicode));
    }

    public static String encodeToBase64(final String simpleString)
    {
        String finalString;
        try
        {
            finalString = Base64.encodeToString(simpleString.getBytes(ISysConfig.APP_CHARACTER_ENCODING), Base64.DEFAULT);
        }
        catch(Exception ex)
        {
            finalString = simpleString;
        }

        return finalString;
    }

    public static String encodeToBase64(final byte[] byteArray)
    {
        String finalString;
        try
        {
            finalString = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        catch(Exception ex)
        {
            finalString = IConstants.EMPTY_STRING;
        }

        return finalString;
    }

    public static int ordinalIndexOf(String str, String substr, int n)
    {
        int pos = str.indexOf(substr);
        while (--n > 0 && pos != -1)
            pos = str.indexOf(substr, pos + 1);
        return pos;
    }

    public static <T> List<T> removeDuplicateFromList(final List<T> inputList)
    {
        LinkedHashSet<T> hashSet = new LinkedHashSet<T>();

        /* Adding ArrayList elements to the HashSet
         * in order to remove the duplicate elements and
         * to preserve the insertion order.
         */
        hashSet.addAll(inputList);

        // Removing ArrayList elements
        inputList.clear();

        // Adding LinkedHashSet elements to the ArrayList
        inputList.addAll(hashSet);

        return inputList;
    }

/*    public static boolean isStringBase64Encoded(String data)
    {
        boolean flag= false;
        try
        {
            //byte isBase64 [] = Base64Utils.decodeFromString(data);
            byte b1[]= android.util.Base64.getMimeDecoder().decode(data.getBytes());
            flag = true;

        } catch (Exception e)
        {
            logger.error("Error occured while checking base64 encoding ");
            e.printStackTrace();
            flag =  false;
        }
        return flag;
    }*/

    public static String getUserReadableSmallName(String name)
    {
        if(AppValidate.isNotEmpty(name))
        {
            name = name.trim();
            if(name.length() > 9)
            {
                name = name.substring(0, 6) + "...";
            }
        }

        return name;
    }

    public static String getReadableSmallString(String name, int maxLetterLimit)
    {
        if(AppValidate.isNotEmpty(name))
        {
            name = name.trim();
            if(name.length() > maxLetterLimit)
            {
                name = name.substring(0, (maxLetterLimit - 3)) + "...";
            }
        }

        return name;
    }

    public static void deleteFile(File file)
    {
        try
        {
            file.delete();
        }
        catch(Exception ex)
        {
            AppUtilities.showUserMessageInDevMode("deleteFile for " + file + ", Exception : " + ex.getMessage());
        }
    }

    public static boolean isURL(final String path)
    {
        try
        {
            URL url = new URL(path);
            return true;
        }
        catch(MalformedURLException mfUrlEx)
        {
            return false;
        }
    }

    public static void introduceDelay(final long milliSeconds)
    {
        try
        {
            /*Thread.sleep(milliSeconds);*/
        }
        catch(Exception ex)
        {
            //ignore
        }
    }
/* public static ArrayList<FilterResultCO<String>> performFiltering(ArrayList<FilterResultCO<String>> filteredResultCOList, final ArrayList<String> actualStringList, String searchText)
    {
        if(filteredResultCOList == null)
        {
            filteredResultCOList = new ArrayList<>();
        }
        else
        {
            filteredResultCOList.clear();
        }

        if(!AppValidate.isEmpty(searchText))
        {
            searchText = searchText.toLowerCase();
            String[] searchTextArray = searchText.split(" ");
            for (int i = 0; i < actualStringList.size(); i++)
            {
                FilterResultCO<String> filterResultCO = new FilterResultCO<>();
                filterResultCO.item = actualStringList.get(i);
                final String stringSearchText = filterResultCO.item.toLowerCase();

                for(String currentSearchText : searchTextArray)
                {
                    if (stringSearchText.contains(currentSearchText))
                    {
                        filterResultCO.matchCount = filterResultCO.matchCount + 1;
                    }
                }

                if(filterResultCO.matchCount > 0)
                {
                    filteredResultCOList.add(filterResultCO);
                }
            }

            Collections.sort(filteredResultCOList, FilterResultCO.matchCountComparator);
        }
        else
        {
            for (int i = 0; i < actualStringList.size(); i++)
            {
                FilterResultCO<String> filterResultCO = new FilterResultCO<>();
                filterResultCO.item = actualStringList.get(i);
                filteredResultCOList.add(filterResultCO);
            }
        }

        return filteredResultCOList;
    }*/


    public static String getQueryParameter(final String url, final String paramName)
    {
        try
        {
            String query = new URL(url).getQuery();
            String[] params = query.split("&");
            for (String param : params)
            {
                String name = param.split("=")[0];
                if(paramName.equals(name))
                {
                    return param.split("=")[1];
                }
            }
        }
        catch(Exception ex)
        {

        }
        return null;
    }

    public static Map<String, String> getQueryMap(String query)
    {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }
}
