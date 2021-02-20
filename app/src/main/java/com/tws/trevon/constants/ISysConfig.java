package com.tws.trevon.constants;

public interface ISysConfig
{
    long COUNT_TIMER=180000;
    String APPLICATION_DEVELOPMENT_MODE = "DEVELOPMENT";
    String APPLICATION_PRODUCTION_MODE = "PRODUCTION";

    String RAZOR_PAY_API_KEY = "rzp_live_PnROAOy1e2jMQx";
    String RAZOR_PAY_API_SECRET_KEY = "4TIK5g2WJJ2TxYhZbtQzdYDg";

    public static final String APP_PUBLIC_DIRECTORY_NAME = "VYAPAAR_SAARTHI";

    String APPLICATION_MODE = APPLICATION_PRODUCTION_MODE;
    int APP_VERSION_CODE = 1;
    //for test
    String SERVER_HIT_URL ="https://developmentconsole.tech/Trevon/api/ApiController/init";

    //for Live
    //String SERVER_HIT_URL ="https://mefinez.com/api/ApiController/init";

    String SERVER_IMAGE_UPLOAD_URL = "https://developmentconsole.tech/Trevon/api/ApiController";
    String SERVER_DOCUMENT_UPLOAD_URL = "https://mefinez.com/api/TestApiController/UploadDocumentImages";


    String SHARE_DEFAULT_BODY_START = "Sent using Framework Android App. ";
    String DB_NAME = "framework.db";
    String APP_NAME = "Framework";

    //Personal app configurations end
    String APP_INTERNAL_IMAGE_DIRECTORY = "imageDir";
    String APP_INTERNAL_DATA_DIRECTORY = "data";
    String APP_INTERNAL_DATA_APP_DIRECTORY = "app";
    String APP_SHARED_PREFERENCE_FILE_NAME = "appPrefFile";
    float COLOR_TO_BLINK = 0.0f;
    int RESET_ANIMATION_TIME = 600;
    int VALIDATION_ANIMATION_TIME = 1700;
    int CUSTOM_NOTIFICATION_TASK_UPDATE_TYPE = 1;
    int SAVE_QUERY_UPDATE_TYPE = 2;
    int FORWARD_QUERY_UPDATE_TYPE = 3;
    int FULL_IMAGE_MAX_WIDTH = 800;
    int FULL_IMAGE_MAX_HEIGHT = 800;
    int ALPHABETIC_VALIDATION = 1;
    int DATE_VALIDATION = 2;
    int PINCODE_VALIDATION = 3;
    int LANDLINE_VALIDATION = 4;
    int MOBILE_NO_VALIDATION = 5;
    int EMAIL_VALIDATION = 6;
    int ALPHANUMERIC_VALIDATION = 7;
    int NUMERIC_VALIDATION = 8;
    int POSITIVE_VALIDATION = 9;

    int ALPHABETIC_WITH_SPACES_VALIDATION = 13;
    int ALPHANUMERIC_WITH_SPACES_VALIDATION = 14;
    int DOUBLE_VALIDATION = 15;
    int ALPHANUMERIC_WITH_COMMA_SEPARATED = 16;
    int ALPHANUMERIC_WITH_GENERAL_SPECIAL_CHARS = 17;
    int ALPHANUMERIC_WITH_SPECIAL_CHARS_AND_SPACE = 18;
   int INTEGER_PERCENTAGE = 19;
   int BOOLEAN_STRING = 20;
    int POSITIVE_LONG_VALIDATION = 21;
    int SIGNED_DOUBLE_VALIDATION = 22;
    int ALLOW_ALL_LANGUAGE_CHARACTERS_VALIDATION = 23;
    int STRONG_PASSWORD_VALIDATION = 24;

    String APP_DISPLAY_DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
    String APP_DISPLAY_DATE_FORMAT = "dd-MMM-yyyy";
    String APP_DISPLAY_DATE_WEEK_FORMAT = "EEEE dd-MMM-yyyy";
    String APP_DISPLAY_TIME_FORMAT = "hh:mm a";
    String APP_DISPLAY_TIME_FORMAT_SMALLER = "HH:mm";
    String APP_CHARACTER_ENCODING = "UTF-8";
    String APP_NUMERIC_DATE_FORMAT = "yyyyMMdd";

    String EMAIL_PATTERN = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    String MOBILE_NUMBER_PATTERN = "^[6-9]{1}\\d{9}$";
    String[] alphaNumericWithSpacialCharsAndSpacesArray =
    {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "!", IConstants.SYSTEM_LINE_SEPARATOR, "\n", ".", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", " ",
        "'", ",", "@", "#", "?", "$", "%", "&", "*", "(", ")", "{", "}", "[", "]", "|", ":", ";", "<", ">", "/", "~", "+", "=", "-", "_"
    };
    
    String[] alphaNumericWithGeneralSpecialCharsArray =
    {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "!", IConstants.SYSTEM_LINE_SEPARATOR, "\n", ".", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "@", "#", "$", "^", "&", "'", ",", "%", "*", "(", ")", "{", "}", "[", "]", "?", ":", ";", "/", "-", "_"
    };

    int AMOUNT_ROUND_UP_PLACES = 2;
    int DEFAULT_ROUND_UP_PLACES = 2;
    int FIVE_SECOND_TIME_IN_SECONDS = 5;
    int ONE_MINUTE_TIME_IN_SECONDS = 60;
    int ONE_HOUR_TIME_IN_SECONDS = ONE_MINUTE_TIME_IN_SECONDS * 60;
    long ALARM_INTERVAL = 2 * 1000;//2 seconds
    int VOLLEY_IMAGE_SHOW_ANIMATION_DURATION_DEFAULT = 700;
}
