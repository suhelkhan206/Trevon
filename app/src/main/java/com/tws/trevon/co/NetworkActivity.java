package com.tws.trevon.co;

/**
 * Created by Chandra Prakash on 10-02-2015.
 */
public class NetworkActivity
{
    public final String method;
    public Boolean loginRequired = Boolean.TRUE;

    public NetworkActivity(String mobRole) {
        this.method = mobRole;
    }
}
