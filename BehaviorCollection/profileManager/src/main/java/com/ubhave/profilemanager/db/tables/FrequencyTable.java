package com.ubhave.profilemanager.db.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ubhave.profilemanager.data.FrequencyDistribution;

public class FrequencyTable extends AbstractTable
{
	protected final static String variableName = "variableName";
	protected final static String variableFrequency = "variableFrequency";
	protected final static int NOT_FOUND = -1;
	
	public FrequencyTable(final String tableName)
	{
		super(tableName);
	}
	
	@Override
	public void createTable(final SQLiteDatabase database)
	{
		database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
				+ " ("
				+ variableName + " TEXT NOT NULL, "
				+ variableFrequency + " INTEGER DEFAULT 0"
				+ ");");
	}
	
	public void incrementField(final SQLiteDatabase database, final String variable, final int amount)
	{
		ContentValues values = new ContentValues();
		int currentFrequency = getCurrentFrequency(database, variable);
		if (currentFrequency == NOT_FOUND)
		{
			values.put(variableName, variable);
			values.put(variableFrequency, amount);
			database.insert(tableName, null, values);
		}
		else
		{
			values.put(variableFrequency,  currentFrequency + amount);
			database.update(tableName, values, variableName+" = ?", new String[]{variable});
		}
	}
	
	private int getCurrentFrequency(final SQLiteDatabase database, final String variable)
	{
		try
		{
			Cursor cursor = database.query(tableName, new String[]{variableFrequency}, variableName+" = ? ", new String[]{variable}, null, null, null);
			if (cursor != null)
			{
				int frequency;
				if (cursor.getCount() > 0)
				{
					cursor.moveToFirst();
					frequency = cursor.getInt(cursor.getColumnIndex(variableFrequency));
				}
				else
				{
					frequency = NOT_FOUND;
				}
				cursor.close();
				return frequency;
			}
		}
		catch (Exception e)
		{}
		return NOT_FOUND;
	}
	
	public FrequencyDistribution getDistribution(final SQLiteDatabase database)
	{
		Cursor cursor = database.query(tableName, null, null, null, null, null, null);
		if (cursor != null)
		{
			FrequencyDistribution result = new FrequencyDistribution();
			int variableNameIndex = cursor.getColumnIndex(variableName);
			int variableValueIndex = cursor.getColumnIndex(variableFrequency);
			cursor.moveToFirst();
			while (!cursor.isAfterLast())
			{
				String key = cursor.getString(variableNameIndex);
				Integer frequency = cursor.getInt(variableValueIndex);
				if (frequency > 0)
				{
					result.put(key, frequency);
				}
				cursor.moveToNext();
			}
			cursor.close();
			if (!result.isEmpty())
			{
				return result;
			}
		}
		return null;
	}
}
