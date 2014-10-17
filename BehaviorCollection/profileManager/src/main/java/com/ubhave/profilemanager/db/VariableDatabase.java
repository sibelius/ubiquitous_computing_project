package com.ubhave.profilemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ubhave.profilemanager.db.tables.StringListTable;

public class VariableDatabase extends AbstractProfileDatabase
{
	private final static String databaseId = "com_lathia_profilemanager_db_VARIABLE_DB";
	
	public VariableDatabase(final Context context, final String name)
	{
		super(context, databaseId+"_"+name, new StringListTable(databaseId));
	}
	
	public void addVariable(final String variableId)
	{
		SQLiteDatabase database = getWritableDatabase();
		((StringListTable) table).addVariable(database, variableId);
		database.close();
	}
	
	public void removeVariable(final String variableId)
	{
		SQLiteDatabase database = getWritableDatabase();
		((StringListTable) table).removeVariable(database, variableId);
		database.close();
	}
	
	public String[] getVariables()
	{
		SQLiteDatabase database = getReadableDatabase();
		String[] variables = ((StringListTable) table).getVariables(database);
		database.close();
		return variables;
	}
	
	public boolean variableExists(final String variableName)
	{
		SQLiteDatabase database = getReadableDatabase();
		boolean exists = ((StringListTable) table).variableExists(database, variableName);
		database.close();
		return exists;
	}
}
