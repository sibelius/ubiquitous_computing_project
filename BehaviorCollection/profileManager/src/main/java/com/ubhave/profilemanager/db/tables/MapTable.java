package com.ubhave.profilemanager.db.tables;

import java.util.HashMap;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MapTable extends AbstractTable
{
	protected final static String variableName = "variableName";
	protected final static String variableValue = "variableValue";
	protected final static String NOT_FOUND = null;
	
	public MapTable(final String tableName)
	{
		super(tableName);
	}
	
	@Override
	public void createTable(final SQLiteDatabase database)
	{
		database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
				+ " ("
				+ variableName + " TEXT NOT NULL, "
				+ variableValue + " INTEGER DEFAULT 0"
				+ ");");
	}
	
	public void setField(final SQLiteDatabase database, final String variable, final String value)
	{
		ContentValues values = new ContentValues();
		String currentValue = getValue(database, variable);
		if (currentValue == NOT_FOUND)
		{
			values.put(variableName, variable);
			values.put(variableValue, value);
			database.insert(tableName, null, values);
		}
		else
		{
			database.update(tableName, values, variableName+" = ?", new String[]{variable});
		}
	}
	
	public String getValue(final SQLiteDatabase database, final String variable)
	{
		try
		{
			Cursor cursor = database.query(tableName, new String[]{variableValue}, variableName+" = ? ", new String[]{variable}, null, null, null);
			if (cursor != null)
			{
				String value = NOT_FOUND;
				if (cursor.getCount() > 0)
				{
					cursor.moveToFirst();
					value = cursor.getString(cursor.getColumnIndex(variableValue));
				}
				cursor.close();
				return value;
			}
		}
		catch (Exception e)
		{}
		return NOT_FOUND;
	}
	
	public HashMap<String, String> getMapping(final SQLiteDatabase database)
	{
		Cursor cursor = database.query(tableName, null, null, null, null, null, null);
		if (cursor != null)
		{
			HashMap<String, String> map = new HashMap<String, String>();
			int variableNameIndex = cursor.getColumnIndex(variableName);
			int variableValueIndex = cursor.getColumnIndex(variableValue);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				String key = cursor.getString(variableNameIndex);
				String value = cursor.getString(variableValueIndex);
				map.put(key, value);
				cursor.moveToNext();
			}
			cursor.close();
			return map;
		}
		return null;
	}
}
