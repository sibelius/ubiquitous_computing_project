package com.ubhave.profilemanager.ui.distribution;

import com.ubhave.profilemanager.data.FrequencyDistribution;
import com.ubhave.profilemanager.ui.AbstractProfileActivity;

public abstract class AbstractDistributionActivity extends AbstractProfileActivity
{
	public abstract AbstractDistributionListAdapter getAdapter(final FrequencyDistribution distribution);	
}
