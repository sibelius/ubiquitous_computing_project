package com.ubhave.profilemanager;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.ubhave.profilemanager.data.FrequencyDistribution;

public interface ProfileInterface
{
	/*
	 * Storing distributions
	 * Group Name | Variable Variable Name | Variable Value
	 */
	public String[] getDistributions();
	public boolean containsDistribution(final String groupName);
	
	public FrequencyDistribution getDistribution(final String groupName);
	public void removeDistributionTable(final String groupName);
	
	public void addToDistribution(final String groupName, final String variableValue, final int variableFrequency);
	
	/*
	 * Storing Events
	 */
	
	public String[] getEventGroups();
	public boolean containsEventGroup(final String groupName);

	public List<HashMap<String, String>> getEvents(final String groupName, final int daysInPast);
	public void removeEventGroup(final String groupName);
	
	public void addEvent(final String groupName, final long entryTimeInMillis, final HashMap<String, String> event);
	public void addEvent(final String groupName, final long entryTimeInMillis, final JSONObject event);
	
	public int countEvents(final String groupName);
	
	/*
	 * Storing a mapping
	 */
	
	public void setMapVariableValue(final String group, final String name, final String value);
	public String getMapVariableValue(final String group, final String name);
	public HashMap<String, String> getMap(final String group);
}
