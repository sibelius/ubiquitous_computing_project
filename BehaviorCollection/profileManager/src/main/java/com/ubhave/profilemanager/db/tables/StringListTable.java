package com.ubhave.profilemanager.db.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StringListTable extends AbstractTable
{
	protected final static String variableName = "variableName";
	protected final static int NOT_FOUND = -1;
	
	public StringListTable(final String tableName)
	{
		super(tableName);
	}
	
	@Override
	public void createTable(final SQLiteDatabase database)
	{
		database.execSQL("CREATE TABLE IF NOT EXISTS " + tableName
				+ " ("
				+ variableName + " TEXT NOT NULL"
				+ ");");
	}
	
	public void removeVariable(final SQLiteDatabase database, final String variable)
	{
		if (variableExists(database, variableName))
		{
			database.delete(tableName, variableName + "=" + variable, null);
		}
	}
	
	public void addVariable(final SQLiteDatabase database, final String variable)
	{
		if (!variableExists(database, variable))
		{
			ContentValues values = new ContentValues();
			values.put(variableName, variable);
			database.insert(tableName, null, values);
		}
	}
	
	public boolean variableExists(final SQLiteDatabase database, final String variable)
	{
		try
		{
			Cursor cursor = database.query(tableName, null, variableName+" = ? ", new String[]{variable}, null, null, null);
			if (cursor != null)
			{
				boolean entryExists;
				if (cursor.getCount() > 0)
				{
					entryExists = true;
				}
				else
				{
					entryExists = false;
				}
				cursor.close();
				return entryExists;
			}
		}
		catch (Exception e)
		{}
		return false;
	}
	
	public String[] getVariables(final SQLiteDatabase database)
	{
		try
		{
			Cursor cursor = database.query(tableName, null, null, null, null, null, null);
			if (cursor != null)
			{
				int columnIndex = cursor.getColumnIndex(variableName);
				int currentVariable = 0;
				
				String[] variables = new String[cursor.getCount()];
				cursor.moveToFirst();
				while (!cursor.isAfterLast())
				{
					variables[currentVariable] = cursor.getString(columnIndex);
					cursor.moveToNext();
				}
				cursor.close();
				return variables;
			}
		}
		catch (Exception e)
		{}
		return null;
	}
}
