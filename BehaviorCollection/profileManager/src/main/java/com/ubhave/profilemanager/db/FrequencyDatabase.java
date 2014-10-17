package com.ubhave.profilemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.ubhave.profilemanager.data.FrequencyDistribution;
import com.ubhave.profilemanager.db.tables.FrequencyTable;

public class FrequencyDatabase extends AbstractProfileDatabase
{	
	public FrequencyDatabase(final Context context, final String databaseId)
	{
		super(context, databaseId, new FrequencyTable(databaseId));
	}
	
	public void increment(final String variableId, final int amount)
	{
		SQLiteDatabase database = getWritableDatabase();
		((FrequencyTable) table).incrementField(database, variableId, amount);
		database.close();
	}
	
	public FrequencyDistribution getDistribution()
	{
		SQLiteDatabase database = getReadableDatabase();
		FrequencyDistribution distribution;
		try
		{
			distribution = ((FrequencyTable) table).getDistribution(database);
		}
		catch (SQLiteException e)
		{
			if (e.getMessage().toString().contains("no such table"))
			{
				distribution = null;
			}
			else throw e;
		}
		database.close();
		return distribution;
	}
}
