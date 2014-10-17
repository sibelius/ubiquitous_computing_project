package com.ubhave.profilemanager.ui.home;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileEntry
{
	private final static String DISPLAY_KEY = "display";
	private final static String PROFILE_VAR = "variable";
	private final static String DESCRIPTION = "description";
	
	private final String displayText, descriptionText;
	private final String profileVariable;
	
	public ProfileEntry(String profileVariable, String displayText, String descriptionText)
	{
		this.displayText = displayText;
		this.profileVariable = profileVariable;
		this.descriptionText = descriptionText;
	}
	
	public ProfileEntry(final JSONObject json) throws JSONException
	{
		this.displayText = getValue(json, DISPLAY_KEY);
		this.profileVariable = getValue(json, PROFILE_VAR);
		this.descriptionText = getValue(json, DESCRIPTION);
	}
	
	private static String getValue(final JSONObject json, final String key) throws JSONException
	{
		if (json.has(key))
		{
			return json.getString(key);
		}
		else
		{
			return null;
		}
	}
	
	public String getDisplayText()
	{
		return displayText;
	}
	
	public String getDescription()
	{
		return descriptionText;
	}
	
	public String getProfileVariable()
	{
		return profileVariable;
	}
}
