package com.ubhave.profilemanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ubhave.profilemanager.db.tables.AbstractTable;

public abstract class AbstractProfileDatabase extends SQLiteOpenHelper
{
	private final static int dbVersion = 2;
	protected AbstractTable table;
	
	public AbstractProfileDatabase(final Context context, final String databaseId, final AbstractTable table)
	{
		super(context, databaseId, null, dbVersion);
		this.table = table;
	}
	
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		table.createTable(database);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
	{
		table.upgradeTable(database);
	}
	
	public void deleteAll()
	{
		SQLiteDatabase database = getWritableDatabase();
		table.removeContents(database);
		table.dropTable(database);
		table.createTable(database);
		database.close();
	}
	
}
