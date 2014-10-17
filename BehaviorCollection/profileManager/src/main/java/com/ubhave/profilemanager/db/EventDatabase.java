package com.ubhave.profilemanager.db;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ubhave.profilemanager.db.tables.EventTable;

public class EventDatabase extends AbstractProfileDatabase
{
	public EventDatabase(final Context context, final String databaseId)
	{
		super(context, databaseId, new EventTable(databaseId));
	}
	
	public void add(final long entryTime, final HashMap<String, String> values)
	{
		SQLiteDatabase database = getWritableDatabase();
		((EventTable) table).add(database, entryTime, values);
		database.close();
	}
	
	public void add(final long entryTime, final JSONObject values)
	{
		SQLiteDatabase database = getWritableDatabase();
		((EventTable) table).add(database, entryTime, values);
		database.close();
	}
	
	public List<HashMap<String, String>> getEvents(int daysInPast)
	{
		SQLiteDatabase database = getReadableDatabase();
		List<HashMap<String, String>> events = ((EventTable) table).getEvents(database, daysInPast);
		database.close();
		return events;
	}
	
	public int countEvents()
	{
		SQLiteDatabase database = getReadableDatabase();
		int numEvents = ((EventTable) table).countEvents(database);
		database.close();
		return numEvents;	
	}
}
