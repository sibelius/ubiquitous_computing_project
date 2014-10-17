package com.ubhave.profilemanager.ui.distribution;

import com.ubhave.profilemanager.data.FrequencyDistribution;

public abstract class AbstractBuiltDistributionActivity extends AbstractDistributionActivity
{
	protected abstract FrequencyDistribution loadDistribution();

	@Override
	protected void loadData()
	{
		new LoadDistributionThread(this)
		{
			protected FrequencyDistribution loadDistribution()
			{
				return loadDistribution();
			}
		}.start();
	}
}
