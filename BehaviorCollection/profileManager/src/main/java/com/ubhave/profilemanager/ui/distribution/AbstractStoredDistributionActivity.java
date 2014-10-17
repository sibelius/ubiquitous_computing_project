package com.ubhave.profilemanager.ui.distribution;

import com.ubhave.profilemanager.ProfileDataStore;
import com.ubhave.profilemanager.data.FrequencyDistribution;

public abstract class AbstractStoredDistributionActivity extends AbstractDistributionActivity
{
	protected abstract String getDistributionVariableName();

	@Override
	protected void loadData()
	{
		new LoadDistributionThread(this)
		{
			protected FrequencyDistribution loadDistribution()
			{
				FrequencyDistribution distribution = null;
				ProfileDataStore profileManager = ProfileDataStore.getInstance(ui);
				String variableName = getDistributionVariableName();
				
				if (variableName != null)
				{
					if (profileManager.containsDistribution(variableName))
					{
						distribution = profileManager.getDistribution(variableName);
					}
				}
				return distribution;
			}
		}.start();
	}
}
