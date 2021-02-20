package com.tws.trevon.co;

import com.tws.trevon.common.CallApi;

public class ResponseCO
{
	public String response;
	public boolean isUserError = false;

	//extra fields on client
	public String clientErrorCode;
	public CallApi callApi;
}
