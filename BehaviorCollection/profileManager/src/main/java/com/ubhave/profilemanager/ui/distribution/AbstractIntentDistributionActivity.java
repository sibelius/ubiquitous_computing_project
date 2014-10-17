package com.ubhave.profilemanager.ui.distribution;

import java.util.HashMap;

import android.content.Intent;
import android.util.Log;

import com.ubhave.profilemanager.data.FrequencyDistribution;

public abstract class AbstractIntentDistributionActivity extends AbstractDistributionActivity
{
	public final static String DISTRIBUTION_KEY = "distribution";

	protected String getIntentKeyForDistributionData()
	{
		// Override this to set your own intent key
		return DISTRIBUTION_KEY;
	}

	@Override
	protected void loadData()
	{
		new LoadDistributionThread(this)
		{
			@SuppressWarnings("unchecked")
			protected FrequencyDistribution loadDistribution()
			{
				FrequencyDistribution distribution = null;
				String intentKey = getIntentKeyForDistributionData();
				if (intentKey != null)
				{
					Intent intent = getIntent();
					if (intent.hasExtra(intentKey))
					{
						HashMap<String, Integer> values = (HashMap<String, Integer>) intent.getSerializableExtra(intentKey);
						distribution = new FrequencyDistribution(values);
						Log.d("Profile", "loaded is null: "+(distribution==null));
					}
					else
					{
						Log.d("Profile", "Intent is missing: "+intentKey);
					}
				}
				else
				{
					Log.d("Profile", intentKey+" is null");
				}
				return distribution;
			}
		}.start();
	}
}
