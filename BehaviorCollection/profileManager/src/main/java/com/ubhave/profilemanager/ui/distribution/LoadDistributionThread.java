package com.ubhave.profilemanager.ui.distribution;

import android.widget.ListView;

import com.ubhave.profilemanager.data.FrequencyDistribution;
import com.ubhave.profilemanager.ui.LoadingThread;

public abstract class LoadDistributionThread extends LoadingThread
{
	private FrequencyDistribution distribution;
	
	public LoadDistributionThread(final AbstractDistributionActivity ui)
	{
		super(ui);
	}
	
	@Override
	protected boolean loadData()
	{
		distribution = loadDistribution();
		return (distribution != null);
	}
	
	protected abstract FrequencyDistribution loadDistribution();
	
	@Override
	protected void updateListView(final ListView listView, final boolean hasHeader)
	{
		if (distribution != null)
		{
			AbstractDistributionActivity distributionUI = (AbstractDistributionActivity) ui;
			AbstractDistributionListAdapter adapter = (AbstractDistributionListAdapter) listView.getAdapter();
			if (adapter != null)
			{
				adapter.clear();
				adapter.notifyDataSetChanged();
			} 
			listView.setAdapter(distributionUI.getAdapter(distribution));
		}
	}
}
