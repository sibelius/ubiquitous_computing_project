package com.ubhave.profilemanager.ui.distribution;

import java.text.DecimalFormat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ubhave.profilemanager.data.FrequencyDistribution;

public abstract class AbstractDistributionListAdapter extends ArrayAdapter<String>
{
	private final FrequencyDistribution data;
	private final int frequencySum, layoutId;
	private final DecimalFormat formatter;

	public AbstractDistributionListAdapter(Context context, final FrequencyDistribution data, int layoutId)
	{
		super(context, layoutId, data.getKeys());
		this.data = data;
		this.frequencySum = data.frequencySum();
		this.layoutId = layoutId;
		this.formatter = new DecimalFormat("#.##");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		if (row == null)
		{
			row = View.inflate(getContext(), layoutId, null);
		}
		String key = getItem(position);
		getEntryText(row).setText(key);
		ProgressBar progressBar = getEntryProgressBar(row);
		progressBar.setMax(frequencySum);
		progressBar.setProgress(data.get(key));

		TextView progressLabel = getLabelTextView(row);
		if (progressLabel != null)
		{
			if (frequencySum > 0)
			{
				double percent = ((double) data.get(key) / frequencySum) * 100;
				progressLabel.setText(formatter.format(percent)+"%");
			}
			else
			{
				progressLabel.setText("");
			}
			
		}
		return row;
	}

	protected abstract TextView getEntryText(View row);

	protected abstract TextView getLabelTextView(View row);

	protected abstract ProgressBar getEntryProgressBar(View row);
}
