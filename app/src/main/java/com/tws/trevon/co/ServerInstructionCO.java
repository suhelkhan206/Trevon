package com.tws.trevon.co;

public class ServerInstructionCO 
{
	public static final String FORCE_LOGOUT_TYPE = "FORCE_LOGOUT";
	public static final String RESET_CLIENT_DB_TYPE = "RESET_CLIENT_DB";
	
	public String type;
	public String data;
	public DialogCO dialogCO;
}
