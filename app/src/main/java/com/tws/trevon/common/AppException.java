/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tws.trevon.common;


/**
 *
 * @author chandra.kalwar
 */
public class AppException extends Exception
{
    private String detailedUserMesssage;

    /**
     * Constructs an instance of
     * <code>ApplicationException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public AppException(String msg)
    {
        super(msg);
        populateDetailedMsg(msg, null);
    }
    
    public AppException(String msg, Object[] arguments)
    {
        super(msg);
        populateDetailedMsg(msg, arguments);
    }

    private void populateDetailedMsg(String msgCode, Object[] arguments)
    {
        Object[] newArguments = {msgCode};
        //detailedUserMesssage = AppUtilities.getMessage(R.string.general_app_exception, newArguments);
    }



    public String getDetailedUserMessage() {
        return detailedUserMesssage;
    }
}
