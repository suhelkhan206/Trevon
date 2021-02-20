package com.tws.trevon.common;

import com.tws.trevon.co.NetworkActivity;
import com.tws.trevon.co.ResponseCO;
import com.tws.trevon.constants.IErrorCodes;
import com.tws.trevon.constants.IReqParams;
import com.tws.trevon.constants.ISysConfig;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Chandra Prakash
 */
public class CallApi
{
    public Map<String, String> params;
    public final NetworkActivity networkActivity;
    private Listener listener;
    public boolean processInBackground = false;
    public boolean isSilentCall = false;
    public String TAG;
    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    public CallApi(NetworkActivity networkActivity)
    {
        this.networkActivity = networkActivity;
        this.params = new HashMap<>();
      //  params.put("business_slug", IConstants.BUSINESS_SLUG);
    }

    public void addReqParams(final String paramName, final String paramValue)
    {
        params.put(paramName, Utilities.getNotNullValue(paramValue));
    }

    public void execute()
    {
        OkHttpClient client = new OkHttpClient();

        addReqParams(IReqParams.ACTION_REQUEST_PARAMETER, networkActivity.method);
        //        addReqParams(IReqParams.APP_VERSION_TAG_REQUEST_PARAMETER, Integer.toString(AppUtilities.getAppVersion()));
//        addReqParams(IReqParams.REQUEST_SOURCE_REQUEST_PARAMETER, ISysConfig.ANDROID);
//        addReqParams(IReqParams.APP_TYPE_REQUEST_PARAMETER, ISysConfig.CUSTOMER);
//        addReqParams(IReqParams.ACCESS_TOKEN_REQUEST_PARAMETER, AppUtilities.readFromPref(IPreferences.API_ACCESS_TOKEN));
        //addReqParams(IReqParams.ACCESS_TOKEN_REQUEST_PARAMETER, "dfsfsdfd");


        Request request = new Request.Builder()
                .url(ISysConfig.SERVER_HIT_URL)
                .post(RequestBody.create(MEDIA_TYPE, getStringToPost()))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "rrrrrrrrrrr")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException ex)
            {
                call.cancel();

                ResponseCO responseCO = new ResponseCO();
                responseCO.clientErrorCode = IErrorCodes.getIOExceptionErrorCodes(ex);
                responseCO.callApi = CallApi.this;
                listener.onError(responseCO);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String myResponse = response.body().string();

                if(AppValidate.isEmpty(myResponse))
                {
                    ResponseCO responseCO = new ResponseCO();
                    responseCO.clientErrorCode = IErrorCodes.EMPTY_RESPONSE_FROM_SERVER;
                    responseCO.callApi = CallApi.this;
                    listener.onError(responseCO);
                }
                else
                {
                    ResponseCO responseCO = new ResponseCO();
                    responseCO.response = myResponse;
                    responseCO.callApi = CallApi.this;
                    listener.onSuccess(responseCO);
                }
            }
        });
    }

    public String getStringToPost()
    {
        JSONObject json = new JSONObject();

        try
        {
            for (Map.Entry<String, String> param : params.entrySet())
            {
                /*json.put(URLEncoder.encode(param.getKey(), ISysConfig.APP_CHARACTER_ENCODING), URLEncoder.encode(param.getValue(), ISysConfig.APP_CHARACTER_ENCODING));*/
                json.put(param.getKey(), param.getValue());
            }
        }
        catch(Exception ex)
        {

        }

        return json.toString();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener
    {
        void onSuccess(ResponseCO responseCO);
        void onError(ResponseCO responseCO);
    }
}
