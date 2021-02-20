/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tws.trevon.constants;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author chandra.kalwar
 */
public abstract class IErrorCodes
{
    public final static String EMPTY_RESPONSE_FROM_SERVER = "100001";
    public final static String NO_ROUTE_EXCEPTION = "100002";
    public final static String SOCKET_TIME_OUT_EXCEPTION = "100003";
    public final static String SOCKET_EXCEPTION = "100004";
    public final static String UNKNOWN_HOST_EXCEPTION = "100005";
    public final static String UNKNOWN_IO_EXCEPTION = "100006";
    public final static String INTERNET_NOT_AVAILABLE = "100007";

    public static Map<String,String> errorDescriptionMap;

    static
    {
        errorDescriptionMap = new HashMap<>();
        errorDescriptionMap.put(EMPTY_RESPONSE_FROM_SERVER, "Empty response from server.");
        errorDescriptionMap.put(NO_ROUTE_EXCEPTION, "No route exception while calling the API");
        errorDescriptionMap.put(SOCKET_TIME_OUT_EXCEPTION, "Socket timeout exception while calling the API");
        errorDescriptionMap.put(SOCKET_EXCEPTION, "Socket exception while calling the API");
        errorDescriptionMap.put(UNKNOWN_HOST_EXCEPTION, "Unknown host exception while calling the API");
        errorDescriptionMap.put(UNKNOWN_IO_EXCEPTION, "Unknown IO exception while calling the API");
        errorDescriptionMap.put(INTERNET_NOT_AVAILABLE, "Internet connection not available");
    }

    public static String getIOExceptionErrorCodes(final IOException ex)
    {
        if(ex instanceof NoRouteToHostException)
        {
            return IErrorCodes.NO_ROUTE_EXCEPTION;
        }
        else if(ex instanceof SocketTimeoutException)
        {
            return IErrorCodes.SOCKET_TIME_OUT_EXCEPTION;
        }
        else if(ex instanceof SocketException)
        {
            return IErrorCodes.SOCKET_EXCEPTION;
        }
        else if(ex instanceof UnknownHostException)
        {
            return IErrorCodes.UNKNOWN_HOST_EXCEPTION;
        }
        else
        {
            return IErrorCodes.UNKNOWN_IO_EXCEPTION;
        }
    }
}
