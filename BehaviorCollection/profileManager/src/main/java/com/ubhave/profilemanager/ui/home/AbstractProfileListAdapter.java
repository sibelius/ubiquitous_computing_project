package com.ubhave.profilemanager.ui.home;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public abstract class AbstractProfileListAdapter extends ArrayAdapter<ProfileEntry>
{
	private final int layoutId;

	public AbstractProfileListAdapter(Context context, final List<ProfileEntry> data, int layoutId)
	{
		super(context, layoutId, data);
		this.layoutId = layoutId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View row = convertView;
		if (row == null)
		{
			row = View.inflate(getContext(), layoutId, null);
		}
		
		ProfileEntry entry = getItem(position);
		TextView textView = getEntryText(row);
		if (textView != null)
		{
			textView.setText(entry.getDisplayText());
		}
		return row;
	}

	protected abstract TextView getEntryText(View row);
}
