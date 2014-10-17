package com.ubhave.profilemanager.ui.home;

import java.util.ArrayList;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.ubhave.profilemanager.ui.AbstractProfileActivity;

public abstract class AbstractProfileListActivity extends AbstractProfileActivity
{
	public final static String CONFIG_FILE_NAME = "profile-list.json";
	public final static String PROFILE_LIST_KEY = "profile";

	public String getJSONConfigFileName()
	{
		// Override this to set your own config file name
		return CONFIG_FILE_NAME;
	}

	public String getProfileListKey()
	{
		return PROFILE_LIST_KEY;
	}

	@Override
	protected void loadData()
	{
		new LoadJSONThread(this).start();
	}

	protected abstract AbstractProfileListAdapter getAdapter(final ArrayList<ProfileEntry> data);
	
	protected abstract void onItemClicked(final ProfileEntry entry);
	
	protected abstract void onItemLongClicked(final ProfileEntry entry);
	
	public OnItemClickListener getOnItemClickListener(final ArrayList<ProfileEntry> data, final boolean hasHeader)
	{
		return new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (hasHeader)
				{
					position --;
				}
				onItemClicked(data.get(position));
			}
		};
	}
	
	public OnItemLongClickListener getOnItemLongClickListener(final ArrayList<ProfileEntry> data, final boolean hasHeader)
	{
		return new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				if (hasHeader)
				{
					position --;
				}
				onItemLongClicked(data.get(position));
				return true;
			}
		};
	}
}
