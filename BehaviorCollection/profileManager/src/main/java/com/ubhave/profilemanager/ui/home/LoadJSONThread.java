package com.ubhave.profilemanager.ui.home;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.ubhave.profilemanager.ui.LoadingThread;

public class LoadJSONThread extends LoadingThread
{
	protected ArrayList<ProfileEntry> data;

	public LoadJSONThread(final AbstractProfileListActivity ui)
	{
		super(ui);
	}

	protected boolean loadData()
	{
		try
		{
			JSONObject config = new JSONObject(loadFileContents());

			String profileKey = ((AbstractProfileListActivity) ui).getProfileListKey();
			JSONArray profile = config.getJSONArray(profileKey);

			data = new ArrayList<ProfileEntry>();
			for (int i = 0; i < profile.length(); i++)
			{
				data.add(build((JSONObject) profile.get(i)));
			}

			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	protected ProfileEntry build(JSONObject json) throws JSONException
	{
		return new ProfileEntry(json);
	}

	public String loadFileContents() throws Exception
	{
		String fileName = ((AbstractProfileListActivity) ui).getJSONConfigFileName();
		try
		{
			return getContent(new BufferedReader(new InputStreamReader(ui.openFileInput(fileName))));
		}
		catch (Exception e)
		{
			return getContent(new BufferedReader(new InputStreamReader(ui.getAssets().open(fileName))));
		}
	}

	private String getContent(final BufferedReader in) throws Exception
	{
		StringBuffer fileContents = new StringBuffer();
		if (in != null)
		{
			String line;
			while ((line = in.readLine()) != null)
			{
				fileContents.append(line);
			}
			in.close();
		}
		return fileContents.toString();
	}

	@Override
	protected void updateListView(final ListView listView, final boolean hasHeader)
	{
		if (data != null)
		{
			AbstractProfileListActivity profileHome = (AbstractProfileListActivity) ui;
			AbstractProfileListAdapter adapter = (AbstractProfileListAdapter) listView.getAdapter();
			if (adapter != null)
			{
				adapter.clear();
				adapter.notifyDataSetChanged();
			}
			listView.setAdapter(profileHome.getAdapter(data));
			
			OnItemClickListener clickListener = profileHome.getOnItemClickListener(data, hasHeader);
			if (clickListener != null)
			{
				listView.setOnItemClickListener(clickListener);
			}
			
			OnItemLongClickListener holdListener = profileHome.getOnItemLongClickListener(data, hasHeader);
			if (holdListener != null)
			{
				listView.setOnItemLongClickListener(holdListener);
			}
		}
	}
}
