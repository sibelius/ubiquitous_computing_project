package com.ubhave.profilemanager;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.ubhave.profilemanager.data.FrequencyDistribution;
import com.ubhave.profilemanager.db.EventDatabase;
import com.ubhave.profilemanager.db.FrequencyDatabase;
import com.ubhave.profilemanager.db.MapDatabase;

public class ProfileDataStore implements ProfileInterface
{
	public static final String CONTENT_BROADCAST = "com.ubhave.profilemanager-ProfileDataStore";
	public static final String CONTENT_TYPE = "content-type";
	public static final String CONTENT_KEY = "content-key";
	public static final String TYPE_DISTRIBUTIONS_CHANGED = "changed-distributions";
	public static final String TYPE_EVENTS_CHANGED = "changed-events";
	public static final String TYPE_MAPPINGS_CHANGED = "changed-mappings";

	protected static final String DISTRIBUTIONS = "distributions";
	protected static final String EVENTS = "events";
	protected static final String MAPPINGS = "mappings";

	private static ProfileDataStore instance;

	public static ProfileDataStore getInstance(Context context)
	{
		if (instance == null)
		{
			instance = new ProfileDataStore(context);
		}
		return instance;
	}

	protected final Context context;
	protected final TableMap<FrequencyDatabase> distributionMap;
	protected final TableMap<EventDatabase> eventMap;
	protected final TableMap<MapDatabase> mapMap;

	protected ProfileDataStore(final Context context)
	{
		this.context = context;
		this.distributionMap = new TableMap<FrequencyDatabase>(context, DISTRIBUTIONS);
		this.eventMap = new TableMap<EventDatabase>(context, EVENTS);
		this.mapMap = new TableMap<MapDatabase>(context, MAPPINGS);
	}

	/*
	 * Broadcasting Content Changes
	 */

	protected void broadcast(final String message, final String details)
	{
		Intent intent = new Intent(CONTENT_BROADCAST);
		intent.putExtra(CONTENT_TYPE, message);
		if (details != null)
		{
			intent.putExtra(CONTENT_KEY, details);
		}
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}

	/*
	 * Distribution Feedback: (Group Name, Variable Name, Variable Count)
	 */

	@Override
	public String[] getDistributions()
	{
		return distributionMap.getVariables();
	}

	@Override
	public boolean containsDistribution(final String groupName)
	{
		return distributionMap.containsVariable(groupName);
	}

	private FrequencyDatabase getFrequencyDatabase(final String tableName)
	{
		FrequencyDatabase database = distributionMap.get(tableName);
		if (database == null)
		{
			database = new FrequencyDatabase(context, tableName);
			distributionMap.put(tableName, database);
		}
		return database;
	}

	@Override
	public void addToDistribution(final String variableName, final String variableValue, final int variableFrequency)
	{
		FrequencyDatabase database = getFrequencyDatabase(variableName);
		database.increment(variableValue, variableFrequency);
		broadcast(TYPE_DISTRIBUTIONS_CHANGED, variableName);
	}

	@Override
	public void removeDistributionTable(final String variableName)
	{
		try
		{
			FrequencyDatabase database = getFrequencyDatabase(variableName);
			database.deleteAll();
			distributionMap.remove(variableName);
			broadcast(TYPE_DISTRIBUTIONS_CHANGED, variableName);
		}
		catch (NullPointerException e)
		{
		}
	}

	@Override
	public FrequencyDistribution getDistribution(final String variableName)
	{
		FrequencyDatabase database = getFrequencyDatabase(variableName);
		return database.getDistribution();
	}

	/*
	 * Events: time stamped list of entries
	 */

	@Override
	public String[] getEventGroups()
	{
		return eventMap.getVariables();
	}

	@Override
	public boolean containsEventGroup(String groupName)
	{
		return eventMap.containsVariable(groupName);
	}

	private EventDatabase getEventDatabase(final String tableName)
	{
		EventDatabase database = eventMap.get(tableName);
		if (database == null)
		{
			database = new EventDatabase(context, tableName);
			eventMap.put(tableName, database);
		}
		return database;
	}

	@Override
	public void addEvent(final String groupName, final long entryTimeInMillis, final HashMap<String, String> event)
	{
		EventDatabase database = getEventDatabase(groupName);
		database.add(entryTimeInMillis, event);
		broadcast(TYPE_EVENTS_CHANGED, groupName);
	}

	@Override
	public void addEvent(final String groupName, final long entryTimeInMillis, final JSONObject event)
	{
		EventDatabase database = getEventDatabase(groupName);
		database.add(entryTimeInMillis, event);
		broadcast(TYPE_EVENTS_CHANGED, groupName);
	}

	@Override
	public void removeEventGroup(final String groupName)
	{
		EventDatabase database = getEventDatabase(groupName);
		database.deleteAll();
		eventMap.remove(groupName);
		broadcast(TYPE_EVENTS_CHANGED, groupName);
	}

	@Override
	public List<HashMap<String, String>> getEvents(final String groupName, final int daysInPast)
	{
		EventDatabase database = getEventDatabase(groupName);
		return database.getEvents(daysInPast);
	}

	@Override
	public int countEvents(final String groupName)
	{
		EventDatabase database = getEventDatabase(groupName);
		return database.countEvents();
	}

	/*
	 * Mappings (Group Name, Variable Name, Variable Value)
	 */

	private MapDatabase getMapDatabase(final String tableName)
	{
		MapDatabase database = mapMap.get(tableName);
		if (database == null)
		{
			database = new MapDatabase(context, tableName);
			mapMap.put(tableName, database);
		}
		return database;
	}

	@Override
	public void setMapVariableValue(final String group, final String name, final String value)
	{
		MapDatabase database = getMapDatabase(group);
		database.set(name, value);
		broadcast(TYPE_MAPPINGS_CHANGED, group);
	}

	@Override
	public String getMapVariableValue(final String group, final String name)
	{
		MapDatabase database = getMapDatabase(group);
		return database.getValue(name);
	}

	@Override
	public HashMap<String, String> getMap(final String group)
	{
		MapDatabase database = getMapDatabase(group);
		return database.getMapping();
	}
}
