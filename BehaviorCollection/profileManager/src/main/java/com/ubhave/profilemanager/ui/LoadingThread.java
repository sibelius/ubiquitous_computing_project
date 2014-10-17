package com.ubhave.profilemanager.ui;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

public abstract class LoadingThread extends Thread
{
	protected final static String LOG_TAG = "Profile-Load";
	protected final AbstractProfileActivity ui;

	public LoadingThread(final AbstractProfileActivity ui)
	{
		this.ui = ui;
	}

	@Override
	public void run()
	{
		Log.d(LOG_TAG, "Set UI to loading.");
		ui.set(AbstractProfileActivity.LOADING);
		boolean loadedData = loadData();
		if (loadedData)
		{
			ListView listView = ui.getListView();
			if (listView != null)
			{
				updateUserInterface(listView);
			}
		}

		Log.d(LOG_TAG, "Load successful = " + loadedData + ", reset UI.");
		ui.set(loadedData ? AbstractProfileActivity.LOADED_SUCCESS : AbstractProfileActivity.LOADED_FAIL);
		Log.d(LOG_TAG, "Done.");
	}

	protected abstract boolean loadData();

	private void updateUserInterface(final ListView listView)
	{
		ui.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				boolean hasHeader = addHeader(listView);
				updateListView(listView, hasHeader);
			}
		});
	}

	protected abstract void updateListView(final ListView listView, final boolean hasHeader);

	protected boolean addHeader(final ListView listView)
	{
		View header = ui.getListViewHeader();
		if (header != null)
		{
			listView.addHeaderView(header, null, false);
		}
		return (header != null);
	}
}
