package com.ubhave.profilemanager.db;

import java.util.HashMap;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ubhave.profilemanager.db.tables.EventTable;
import com.ubhave.profilemanager.db.tables.MapTable;

public class MapDatabase extends AbstractProfileDatabase
{
	public MapDatabase(final Context context, final String databaseId)
	{
		super(context, databaseId, new MapTable(databaseId));
	}
	
	public void add(final long entryTime, final HashMap<String, String> values)
	{
		SQLiteDatabase database = getWritableDatabase();
		((EventTable) table).add(database, entryTime, values);
		database.close();
	}
	
	public void set(final String variable, final String value)
	{
		SQLiteDatabase database = getWritableDatabase();
		((MapTable) table).setField(database, variable, value);
		database.close();
	}
	
	public HashMap<String, String> getMapping()
	{
		SQLiteDatabase database = getReadableDatabase();
		HashMap<String, String> map = ((MapTable) table).getMapping(database);
		database.close();
		return map;
	}
	
	public String getValue(final String variable)
	{
		SQLiteDatabase database = getReadableDatabase();
		String value = ((MapTable) table).getValue(database, variable);
		database.close();
		return value;
	}
}
