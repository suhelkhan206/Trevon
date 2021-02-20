/**
 *
 * @author chandra.kalwar
 */
package com.tws.trevon.common;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.ISysConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppValidate
{
    private static final boolean defaultEmptyOK = true;
    private static final String[] validWeekDays = {"1", "2", "3", "4", "5", "6", "7"};
    private static final String decimalPointDelimiter = ".";
    private static final int digitsInZipCode = 6;
    private static final Pattern m_no_pattern = Pattern.compile(ISysConfig.MOBILE_NUMBER_PATTERN);
    private static final Pattern email_pattern = Pattern.compile(ISysConfig.EMAIL_PATTERN);
    private static final String[] alphaNumericWithSpacialCharsAndSpacesArray = ISysConfig.alphaNumericWithSpacialCharsAndSpacesArray;
    
    private static final String[] alphaNumericWithGeneralSpecialCharsArray = ISysConfig.alphaNumericWithGeneralSpecialCharsArray;    

    public static boolean areEqual(Object obj, Object obj2)
    {
        if (obj == null)
        {
            return (obj2 == null);
        }
        else
        {
            return obj.equals(obj2);
        }
    }

    public static boolean isEmpty(TextView et)
    {
        return (et.getText().toString().length() == 0);
    }

    public static boolean isNotEmpty(TextView et)
    {
        return !isEmpty(et);
    }

    public static boolean isInSizeRange(EditText et, int startRange, int endRange)
    {
        return (et.getText().toString().length() > startRange && et.getText().toString().length() < endRange);
    }

    public static boolean isEmpty(String s)
    {
//        return ((s == null) || (s.trim().length() == 0));
        return ((s == null) || (s.trim().isEmpty()));
    }

    public static boolean isEmpty(CharSequence cs)
    {
//        return ((s == null) || (s.trim().length() == 0));
        return ((cs == null) || (cs.toString().trim().isEmpty()));
    }

    public static boolean isEmpty(Collection c)
    {
        return ((c == null) || (c.isEmpty()));
    }
    
    public static boolean isEmpty(Object[] array)
    {
        return ((array == null) || (array.length == 0));
    }

    public static boolean isNotEmpty(String s)
    {
        return ((s != null) && (s.trim().length() > 0));
    }

    public static boolean isNotEmpty(Object[] a)
    {
        return ((a != null) && (!(a.length == 0)));
    }

    public static boolean isNotEmpty(Collection c)
    {
        return ((c != null) && (!c.isEmpty()));
    }

    public static boolean isString(Object obj)
    {
        return ((obj != null) && (obj instanceof String));
    }

    public static boolean isEmail(final String inputEmail)
    {
/*        Matcher matcher = email_pattern.matcher(inputEmail);
        return matcher.matches();*/

        boolean isEmail = false;
        if(isEmpty(inputEmail))
        {
            isEmail = false;
        }
        else if(inputEmail.length() < 6)
        {
            isEmail = false;
        }
        else if(inputEmail.contains("@"))
        {
            int at_index = inputEmail.indexOf("@");
            if(at_index > 0 && inputEmail.length() > (at_index + 1))
            {//@ should not be first letter
                String domain = inputEmail.substring(at_index + 1);
                if(isEmpty(domain))
                {
                    isEmail = false;
                }
                else
                {
                    int dot_index = domain.indexOf(".");
                    if(dot_index > 0 && (dot_index + 1) < domain.length())
                    {
                        isEmail = true;
                    }
                    else
                    {
                        isEmail = false;
                    }
                }
            }
            else
            {
                isEmail = false;
            }
        }
        else
        {
            isEmail = false;
        }

        return isEmail;
    }

    public static boolean isStrongPassword(final String imputPassword)
    {
        if(AppValidate.isEmpty(imputPassword))
        {
            return defaultEmptyOK;
        }
        else
        {
            String digitPat = "\\d+";
            String lowPat = "[a-z]+";
            String upperPat = "[A-Z]+";
            String symPat = "[`~!@$%^&\\-\\+*/=_,;.':|\\(\\)\\[\\]\\{\\}]+";
            Matcher matDigit = Pattern.compile(digitPat).matcher(imputPassword);
            Matcher matLowerCase = Pattern.compile(lowPat).matcher(imputPassword);
            Matcher matUpperCase = Pattern.compile(upperPat).matcher(imputPassword);
            Matcher matSymbol = Pattern.compile(symPat).matcher(imputPassword);

            return (matDigit.find() && matLowerCase.find() && matUpperCase.find() && matSymbol.find());
        }
    }

    public static boolean isLandLine(final String landLine)
    {
        return true;
    }

    public static boolean isMobileNumber(final String mobileNumber)
    {
        Matcher matcher = m_no_pattern.matcher(mobileNumber);
        return matcher.matches();
    }

    public static boolean testPattern(String input)
    {
        final String EMAIL_PATTERN = "^[7-9]{2}\\d{8}$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static void main(String[] args)
    {
//        ddd();
    }

    /**
     * Removes all characters which appear in string bag from string s.
     */
    public static String stripCharsInBag(String s, String bag)
    {
        int i;
        String returnString = "";

        for (i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            if (bag.indexOf(c) == -1)
            {
                returnString += c;
            }
        }
        return returnString;
    }

    /**
     * Removes all characters which do NOT appear in string bag from string s.
     */
    public static String stripCharsNotInBag(String s, String bag)
    {
        int i;
        String returnString = "";

        for (i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            if (bag.indexOf(c) != -1)
            {
                returnString += c;
            }
        }
        return returnString;
    }

    public static boolean isLetter(char c)
    {
        return Character.isLetter(c);
    }

    /**
     * Returns true if character c is a digit (0 .. 9).
     */
    public static boolean isDigit(char c)
    {
        return Character.isDigit(c);
    }

    /**
     * Returns true if character c is a letter or digit.
     */
    public static boolean isLetterOrDigit(char c)
    {
        return Character.isLetterOrDigit(c);
    }

    public static boolean isInteger(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        for (int i = 0; i < s.length(); i++)
        {
            // Check that current character is number.
            char c = s.charAt(i);

            if (!isDigit(c))
            {
                return false;
            }
        }

        // All characters are numbers.
        return true;
    }

    /**
     * Returns true if all characters are numbers; first character is allowed to be + or - as well.
     *
     * Does not accept floating point, exponential notation, etc.
     */
    public static boolean isSignedInteger(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        try
        {
            Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }

    }

    /**
     * Returns true if all characters are numbers; first character is allowed to be + or - as well.
     *
     * Does not accept floating point, exponential notation, etc.
     */
    public static boolean isSignedLong(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        try
        {
            Long.parseLong(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * Returns true if string s is an integer > 0. NOTE: using the Java Long object for greatest precision
     */
    public static Long isPositiveLong(String s)
    {
        try
        {
            long temp = Long.parseLong(s);
            if (temp > 0)
            {
                return temp;
            }
            else
            {
                return null;
            }
        }
        catch (NumberFormatException nfe)
        {
            return null;
        }
    }

    /**
     * True if string s is a signed or unsigned floating point (real) number. First character is allowed to be + or -.
     *
     * Also returns true for unsigned integers. If you wish to distinguish between integers and floating point numbers, first call isSignedInteger, then call isSignedFloat.
     */
    public static Double isSignedDoubleValue(String s)
    {
        Double doubleValue = null;
        try
        {
            doubleValue = Double.parseDouble(s);
        }
        catch (NumberFormatException e)
        {

        }

        return doubleValue;
    }

    public static boolean isSignedDouble(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        try
        {
            Double.parseDouble(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * Returns true if string s is an integer > 0. NOTE: using the Java Long object for greatest precision
     */
    public static Integer isPositiveInteger(String s)
    {
        try
        {
            int temp = Integer.parseInt(s);
            if (temp > 0)
            {
                return temp;
            }
            else
            {
                return null;
            }
        }
        catch (NumberFormatException nfe)
        {
            return null;
        }
    }

    /**
     * Returns true if string s is an integer >= 0.
     */
    public static boolean isNonnegativeInteger(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        try
        {
            int temp = Integer.parseInt(s);
            return temp >= 0;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * Returns true if string s is an integer < 0.
     */
    public static boolean isNegativeInteger(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        try
        {
            int temp = Integer.parseInt(s);
            return temp < 0;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * Returns true if string s is an integer <= 0.
     */
    public static boolean isNonpositiveInteger(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        try
        {
            int temp = Integer.parseInt(s);
            return temp <= 0;
        }
        catch (NumberFormatException e)
        {
            return false;
        }

        // return(isSignedInteger(s, secondArg)
        // &&((isEmpty(s) && secondArg)  ||(parseInt(s) <= 0) ) );
    }

    /**
     * True if string s is an unsigned floating point(real) number.
     *
     * Also returns true for unsigned integers. If you wish to distinguish between integers and floating point numbers, first call isInteger, then call isFloat.
     *
     * Does not accept exponential notation.
     */
    public static boolean isFloat(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        boolean seenDecimalPoint = false;

        if (s.startsWith(decimalPointDelimiter))
        {
            return false;
        }

        for (int i = 0; i < s.length(); i++)
        {
            // Check that current character is number.
            char c = s.charAt(i);

            if (c == decimalPointDelimiter.charAt(0))
            {
                if (!seenDecimalPoint)
                {
                    seenDecimalPoint = true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                if (!isDigit(c))
                {
                    return false;
                }
            }
        }
        // All characters are numbers.
        return true;
    }

    /**
     * General routine for testing whether a string is a float.
     */
    public static boolean isFloat(String s, boolean allowNegative, boolean allowPositive, int minDecimal, int maxDecimal)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        try
        {
            float temp = Float.parseFloat(s);
            if (!allowNegative && temp < 0)
            {
                return false;
            }
            if (!allowPositive && temp > 0)
            {
                return false;
            }
            String floatString = Float.toString(temp);
            int decimalPoint = floatString.indexOf('.');
            if (decimalPoint == -1)
            {
                return minDecimal <= 0;
            }
            // 1.2345; length=6; point=1; num=4
            int numDecimals = floatString.length() - decimalPoint;
            if (minDecimal >= 0 && numDecimals < minDecimal)
            {
                return false;
            }
            return maxDecimal < 0 || numDecimals <= maxDecimal;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * General routine for testing whether a string is a double.
     */
    public static boolean isDouble(String s, boolean allowNegative, boolean allowPositive, int minDecimal, int maxDecimal)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        //Min Decimal : It represents the minimum number of decimal places (including decimal point)
        //Max decimal : It represents the maximum number of decimal places (including decimal point)
        try
        {
            double temp = Double.parseDouble(s);
            if (!allowNegative && temp < 0)
            {
                return false;
            }
            if (!allowPositive && temp > 0)
            {
                return false;
            }
            String doubleString = Double.toString(temp);
            int decimalPoint = doubleString.indexOf('.');
            if (decimalPoint == -1)
            {
                return minDecimal <= 0;
            }
            // 1.2345; length=6; point=1; num=4
            int numDecimals = doubleString.length() - decimalPoint;
            if (minDecimal >= 0 && numDecimals < minDecimal)
            {
                return false;
            }
            return maxDecimal < 0 || numDecimals <= maxDecimal;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * True if string s is a signed or unsigned floating point (real) number. First character is allowed to be + or -.
     *
     * Also returns true for unsigned integers. If you wish to distinguish between integers and floating point numbers, first call isSignedInteger, then call isSignedFloat.
     */
    public static boolean isSignedFloat(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        try
        {
            Float.parseFloat(s);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }

    /**
     * Returns true if string s is letters only.
     *
     * NOTE: This should handle i18n version to support European characters, etc. since it now uses Character.isLetter()
     */
    public static boolean isAlphabetic(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        for (int i = 0; i < s.length(); i++)
        {
            // Check that current character is letter.
            char c = s.charAt(i);

            if (!isLetter(c))
            {
                return false;
            }
        }

        // All characters are letters.
        return true;
    }

    public static boolean isAlphabeticWithSpaces(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        for (int i = 0; i < s.length(); i++)
        {
            // Check that current character is letter.
            char c = s.charAt(i);

            if (isLetter(c))
            {
            }
            else if (Character.isSpaceChar(c))
            {
            }
            else
            {
                return false;
            }
        }

        // All characters are letters.
        return true;
    }

    public static Date isDate(final String dateString)
    {
        return isDate(dateString, ISysConfig.APP_DISPLAY_DATE_FORMAT);
    }

    public static boolean isDateProvided(TextView et)
    {
        if(isEmpty(et))
        {
            return false;
        }
        else return isDate(et.getText().toString()) != null;
    }
    
    public static Date isDate(final String dateString, final String dateFormat)
    {
        final SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);

        try
        {
            Date date = sdf.parse(dateString);
            
            if (!dateString.equals(sdf.format(date)))
            {
                return null;
            }
            
            return date;
        }
        catch (ParseException ex)
        {
            return null;
        }
    }    

    public static boolean isAlphaNumericWithSpace(final String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        for (int i = 0; i < s.length(); i++)
        {
            final char c = s.charAt(i);

            if (isLetter(c))
            {
            }
            else if (Character.isSpaceChar(c))
            {
            }
            else if (Character.getNumericValue(c) >= 0)
            {
            }
            else
            {
                return false;
            }
        }

        // All characters are letters.
        return true;
    }
   // ends here
    
    public static boolean isAlphaNumericWithComaSeperated(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);

            if (",".equals(String.valueOf(c)))
            {
                continue;
            }
            if (Character.isSpaceChar(c))
            {
                continue;
            }
            if (Character.getNumericValue(c) == -1)
            {
                return false;
            }
        }
        return true;
    }
    
    
    public static boolean isAlphaNumericAndSpecialCharsWithSpaces(String s)
    {
        boolean isCorrect = false;
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        for (int i = 0; i < s.length(); i++)
        {
                      // if(!isCorrect)
            // {\
            isCorrect = false;
            for (int j = 0; j < alphaNumericWithSpacialCharsAndSpacesArray.length; j++)
            {

                /*
                 * Check whether all the letters are alphabtets or not
                 */
                if (s.substring(i, i + 1).equalsIgnoreCase(alphaNumericWithSpacialCharsAndSpacesArray[j]))
                {
                    isCorrect = true;
                    break;
                }
                else
                {
                    isCorrect = false;
                }
            }
            if (!isCorrect)
            {
                return false;
            }
            //}
        }
        if (!isCorrect)
        {
            return false;
        }
        else
        {
            return isCorrect;
        }
    }
    
    
    
    public static boolean isAlphaNumericWithGeneralSpecialChars(String s)
    {
        boolean isCorrect = false;
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        for (int i = 0; i < s.length(); i++)
        {
                      // if(!isCorrect)
            // {\
            isCorrect = false;
            for (int j = 0; j < alphaNumericWithGeneralSpecialCharsArray.length; j++)
            {

                /*
                 * Check whether all the letters are alphabtets or not
                 */
                if (s.substring(i, i + 1).equalsIgnoreCase(alphaNumericWithGeneralSpecialCharsArray[j]))
                {
                    isCorrect = true;
                    break;
                }
                else
                {
                    isCorrect = false;
                }
            }
            if (!isCorrect)
            {
                return false;
            }
            //}
        }
        if (!isCorrect)
        {
            return false;
        }
        else
        {
            return isCorrect;
        }
    }

    public static boolean isAlphaNumericWithSpaceAndExceptSpecialCharacter(String s)
    {
        boolean isCorrect = false;
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        String[] checkarray =
        {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", " ", "A",
            "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "{", "}", ",", ":", "_", "."
        };

        for (int i = 0; i < s.length(); i++)
        {
            isCorrect = false;
            for (String checkarray1 : checkarray)
            {
                if (s.substring(i, i + 1).equalsIgnoreCase(checkarray1))
                {
                    isCorrect = true;
                    break;
                }
                else
                {
                    isCorrect = false;
                }
            }
            if (!isCorrect)
            {
                return false;
            }
        }
        if (!isCorrect)
        {
            return false;
        }
        else
        {
            return isCorrect;
        }
    }

    public static boolean isAlphanumeric(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        for (int i = 0; i < s.length(); i++)
        {
            // Check that current character is number or letter.
            char c = s.charAt(i);

            if (!isLetterOrDigit(c))
            {
                return false;
            }
        }

        // All characters are numbers or letters.
        return true;
    }

    public static boolean isAlphanumericNew(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        boolean containalpha = false;

        for (int i = 0; i < s.length(); i++)
        {
            // Check that current character is number or letter.
            char c = s.charAt(i);

            if (!isLetterOrDigit(c))
            {
                return false;
            }
            if (Character.isLetter(c))
            {
                containalpha = true;
            }
        }
        return containalpha;
    }
//ends here

    /**
     * isZIPCode returns true if string s is a valid U.S. ZIP code. Must be 5 or 9 digits only.
     */
    public static boolean isPinCode(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        return (isInteger(s) && (s.length() == digitsInZipCode));

    }

    public static boolean emailValidation(String email)
    {
        Pattern p = Pattern.compile("^[_A-Za-z0-9]+(\\.[_A-Za-z0-9]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher m = p.matcher(email);
        boolean b = m.matches();

        return b;
    }

    public static boolean timeFormatValidation(String strTime)
    {
        Pattern p = Pattern
                .compile("^([01]?[0-9]|2[0-3]):[0-5][0-9]-([01]?[0-9]|2[0-3]):[0-5][0-9]$");
        Matcher m = p.matcher(strTime);
        return m.matches();
    }

    /**
     * isUrl returns true if the string contains ://
     *
     * @param s String to validate
     * @return true if s contains ://
     */
    public static boolean isUrl(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        return s.indexOf("://") != -1;
    }

    /**
     * isYear returns true if string s is a valid Year number. Must be 2 or 4 digits only.
     *
     * For Year 2000 compliance, you are advised to use 4-digit year numbers everywhere.
     */
    public static boolean isYear(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        if (!isNonnegativeInteger(s))
        {
            return false;
        }
//     		Commenting this code snippet to return only true if year is mentioned in YYYY format only
//        return ((s.length() == 2) || (s.length() == 4));
        return ((s.length() == 4));
    }

    /**
     * isIntegerInRange returns true if string s is an integer within the range of integer arguments a and b, inclusive.
     */
    public static boolean isIntegerInRange(String s, int a, int b)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        // Catch non-integer strings to avoid creating a NaN below,
        // which isn't available on JavaScript 1.0 for Windows.
        if (!isSignedInteger(s))
        {
            return false;
        }
        // Now, explicitly change the type to integer via parseInt
        // so that the comparison code below will work both on
        // JavaScript 1.2(which typechecks in equality comparisons)
        // and JavaScript 1.1 and before(which doesn't).
        int num = Integer.parseInt(s);

        return ((num >= a) && (num <= b));
    }

    /**
     * isMonth returns true if string s is a valid month number between 1 and 12.
     */
    public static boolean isMonth(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        if ((s.length() != 2))
        {
            return false;
        }
        return isIntegerInRange(s, 1, 12);
    }

    /**
     * isDay returns true if string s is a valid day number between 1 and 31.
     */
    public static boolean isDay(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        if ((s.length() != 2))
        {
            return false;
        }
        return isIntegerInRange(s, 1, 31);
    }

    /**
     * Given integer argument year, returns number of days in February of that year.
     */
    public static int daysInFebruary(int year)
    {
        // February has 29 days in any year evenly divisible by four,
        // EXCEPT for centurial years which are not also divisible by 400.
        return (((year % 4 == 0) && ((!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28);
    }

    /**
     * isHour returns true if string s is a valid number between 0 and 23.
     */
    public static boolean isHour(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        return isIntegerInRange(s, 0, 23);
    }

    /**
     * isMinute returns true if string s is a valid number between 0 and 59.
     */
    public static boolean isMinute(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        return isIntegerInRange(s, 0, 59);
    }

    /**
     * isSecond returns true if string s is a valid number between 0 and 59.
     */
    public static boolean isSecond(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }
        return isIntegerInRange(s, 0, 59);
    }

    /**
     * isTime returns true if string arguments hour, minute, and second form a valid time.
     */
    public static boolean isTime(String hour, String minute, String second)
    {
        return isHour(hour) && isMinute(minute) && isSecond(second);
    }

    /**
     * isTime returns true if string argument time forms a valid time.
     */
    public static boolean isTime(String time)
    {
        if (isEmpty(time))
        {
            return defaultEmptyOK;
        }

        String hour;
        String minute;
        String second;

        int timeColon1 = time.indexOf(':');
        int timeColon2 = time.lastIndexOf(':');

        if (timeColon1 <= 0)
        {
            return false;
        }
        hour = time.substring(0, timeColon1);
        if (timeColon1 == timeColon2)
        {
            minute = time.substring(timeColon1 + 1);
            second = "0";
        }
        else
        {
            minute = time.substring(timeColon1 + 1, timeColon2);
            second = time.substring(timeColon2 + 1);
        }
        return isTime(hour, minute, second);
    }

    /**
     * This method is used to validate an IP address
     *
     * @param ipAddress
     * @return
     */
    public static boolean isValidateIpAddress(String ipAddress)
    {
        StringTokenizer st = new StringTokenizer(ipAddress, ".");
        int count = 0;
        if (st.countTokens() != 4)
        {
            return false;
        }
        while (st.hasMoreTokens())
        {
            try
            {
                int part = Integer.parseInt(st.nextToken());
                if (count == 0 && part == 0)
                {
                    return false;
                }
                if (part > 255 || part < 0)
                {
                    return false;
                }
            }
            catch (NumberFormatException nfex)
            {
                return false;
            }
            count++;
        }
        return true;
    }

    /**
     * Returns true if string s contains numbers only.
     */
    public static boolean isNumeric(String s)
    {
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

       // Search through string's characters one by one
        // until we find a non-numeric character.
        // When we do, return false; if we don't, return true.
        for (int i = 0; i < s.length(); i++)
        {
            // Check that current character is number or letter.
            char c = s.charAt(i);

            if (!isDigit(c))
            {
                return false;
            }
        }

        // All characters are numbers or letters.
        return true;
    }

    public static boolean isAlphabeticNumericWithSpaces(String s)
    {
        boolean isCorrect = false;
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        String[] checkarray =
        {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", " ",
            "'"
        };

        for (int i = 0; i < s.length(); i++)
        {
	    	 // if(!isCorrect)
            // {\
            isCorrect = false;
            for (String checkarray1 : checkarray)
            {
                /*
                 * Check whether all the letters are alphabtets or not
                 */
                if (s.substring(i, i + 1).equalsIgnoreCase(checkarray1))
                {
                    isCorrect = true;
                    break;
                }
                else
                {
                    isCorrect = false;
                }
            }
            if (!isCorrect)
            {
                return false;
            }
            //}
        }
        if (!isCorrect)
        {
            return false;
        }
        else
        {
            return isCorrect;
        }
    }

    public static boolean isAlphabeticNumericWithSpacesAndUnderscore(String s)
    {
        boolean isCorrect = false;
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        String[] checkarray =
        {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", " ", "A",
            "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "_"
        };

        for (int i = 0; i < s.length(); i++)
        {
   	 // if(!isCorrect)
            // {\
            isCorrect = false;
            for (String checkarray1 : checkarray)
            {
                /*
                 * Check whether all the letters are alphabtets or not
                 */
                if (s.substring(i, i + 1).equalsIgnoreCase(checkarray1))
                {
                    isCorrect = true;
                    break;
                }
                else
                {
                    isCorrect = false;
                }
            }
            if (!isCorrect)
            {
                return false;
            }
            //}
        }
        if (!isCorrect)
        {
            return false;
        }
        else
        {
            return isCorrect;
        }
    }

    public static boolean isAlphaNumericOnly(String s)
    {
        boolean isCorrect = false;
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        String[] checkarray =
        {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
        };

        for (int i = 0; i < s.length(); i++)
        {
            // if(!isCorrect)
            //{
            isCorrect = false;
            for (String checkarray1 : checkarray)
            {
                /*
                 * Check whether all the letters are alphabtets or not
                 */
                if (s.substring(i, i + 1).equalsIgnoreCase(checkarray1))
                {
                    isCorrect = true;
                    break;
                }
                // else
                //  isCorrect = false;
            }
            if (!isCorrect)
            {
                return false;
            }

            // }
        }
        if (!isCorrect)
        {
            return false;
        }
        else
        {
            return isCorrect;
        }
    }

    public static boolean isValidNumeric(String strGivenValue)
    {
        boolean retValue = true;
        if (strGivenValue.contains("."))
        {
            if (strGivenValue.endsWith("."))
            {
                retValue = false;
            }
            String[] value = strGivenValue.trim().split("\\.");
            if (value.length != 2)
            {
                retValue = false;
            }
        }
        return retValue;
    }

    public static boolean isAlphabeticNumeric(String s)
    {
        boolean isCorrect = false;
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        String[] checkarray =
        {
            ",", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            " ", "'", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "_"
        };

        for (int i = 0; i < s.length(); i++)
        {
            isCorrect = false;
            for (String checkarray1 : checkarray)
            {
                /*
                 * Check whether all the letters are alphabtets or not
                 */
                if (s.substring(i, i + 1).equalsIgnoreCase(checkarray1))
                {
                    isCorrect = true;
                    break;
                }
                else
                {
                    isCorrect = false;
                }
            }
            if (!isCorrect)
            {
                return false;
            }

        }
        if (!isCorrect)
        {
            return false;
        }
        else
        {
            return isCorrect;
        }
    }

    public static boolean isNumericOnly(String s)
    {
        boolean isCorrect = false;
        if (isEmpty(s))
        {
            return defaultEmptyOK;
        }

        String[] checkarray =
        {
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "."
        };

        for (int i = 0; i < s.length(); i++)
        {
            isCorrect = false;
            for (String checkarray1 : checkarray)
            {
                /*
                 * Check whether all the letters are alphabtets or not
                 */
                if (s.substring(i, i + 1).equalsIgnoreCase(checkarray1))
                {
                    isCorrect = true;
                    break;
                }
                else
                {
                    isCorrect = false;
                }
            }
            if (!isCorrect)
            {
                return false;
            }

        }
        if (!isCorrect)
        {
            return false;
        }
        else
        {
            return isCorrect;
        }
    }

    public static boolean isEmptySpaces(String s)
    {
        return ((s == null) || (s.length() == 0));
    }

    public static String removeLastSpecialChar(String s)
    {
        if ((",").equals(s.substring(s.length() - 2).trim()))
        {
            // if( s.substring(s.length()-2).trim().equals(",") ) {  
            s = s.substring(0, s.length() - 2);

        }
        return s;
    }

    public static boolean isContainWhiteSpace(String s)
    {
        return s.matches(".*\\s.*");
    }

    public static String getActionType(String operator)
    {
        if (operator.equals("<")) {
            operator = "Less than";

        } else if (operator.equals(">")) {
            operator = "Greater than";

        } else if (operator.equals("=")) {
            operator = "Equal to";

        }

        return operator;
    }

    public static boolean isPositiveString(String s)
    {

        boolean isNumber = true;

        if (s.charAt(0) == '-')
        {
            s = s.substring(1);
            isNumber = isNumericOnly(s);
        }
        else
        {
            isNumber = false;
        }

        return isNumber;
    }

    public static boolean validatePercentageAmount(String s)
    {

        boolean isValid = true;
        if (!s.isEmpty() && s != null)
        {
            if (s.contains("."))
            {
                String s2 = s;
                int index = s.indexOf('.');
                if (index != -1)
                {
                    String s3 = s.substring(0, index);
                    String s4 = s2.substring(index + 1);

                    if (s3 != null && s3.length() > 2)
                    {
                        isValid = false;
                    }
                    if (s4 != null && s4.length() > 2)
                    {
                        isValid = false;
                    }
                }
            }
            else
            {
                if (s.length() > 2)
                {
                    isValid = false;
                }
            }
        }

        return isValid;
    }


//    public static boolean isDouble(String s)
//    {
//
//        if (s.startsWith(".") || s.endsWith("."))
//        {
//            return false;
//        }
//        else
//        {
//            try
//            {
//                Double.parseDouble(s);
//                return true;
//            }
//            catch (Exception e)
//            {
//                return false;
//            }
//        }
//    }
    
    public static Double isDouble(String s)
    {
        Double value = null;
        if (s.startsWith(".") || s.endsWith("."))
        {
        }
        else
        {
            try
            {
                value = Double.parseDouble(s);
            }
            catch (NumberFormatException e)
            {
            }
        }
        
        return value;
    }

    public static Integer isIntegerPercentage(String string)
    {
        Integer value = null;
        try
        {
            value = Integer.parseInt(string);

            if(value >= 0 && value <= 100)
            {

            }
            else
            {
                value = null;
            }
        }
        catch (NumberFormatException e)
        {
        }

        return value;
    }

    public static Boolean isBoolean(String string)
    {
        if("true".equalsIgnoreCase(string) || "false".equalsIgnoreCase(string))
        {
            return Boolean.parseBoolean(string);
        }
        else
        {
            return null;
        }
    }
    
    public static boolean isValidCommaSeparatedWeekDays(String value)
    {
        if (isEmpty(value))
        {
            return true;
        }        
        
        String[] daysString = value.split(",");
        
        for (String inputDay : daysString)
        {
            boolean isValidDay = false;
            for (String validWeekDay : validWeekDays)
            {
                if(inputDay.trim().equals(validWeekDay))
                {
                    isValidDay = true;
                }                               
            }
            
            if(!isValidDay)
            {
                return false;
            }            
        }
        
        return true;
    }

    public static boolean isEmpty(Spinner spinner)
    {
        if(spinner.getSelectedItem() == null)
        {
            return true;
        }
        else if(AppValidate.isEmpty(spinner.getSelectedItem().toString()))
        {
            return true;
        }
        else return spinner.getSelectedItem().toString().equals(IConstants.NONE_ITEM);
    }

    public static boolean isNumber(String input)
    {
        //String input = "34s";
        if(input.matches("^\\d+(\\.\\d+)?"))
        {
            //okay
            return true;
        } else {
            // not okay !
            return false;
        }
    }
}
