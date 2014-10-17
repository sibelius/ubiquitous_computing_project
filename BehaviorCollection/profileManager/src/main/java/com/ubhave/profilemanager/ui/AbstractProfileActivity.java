package com.ubhave.profilemanager.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class AbstractProfileActivity extends Activity
{
	public static final int LOADING = 1;
	public static final int LOADED_SUCCESS = 2;
	public static final int LOADED_FAIL = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		setContentView(getLayoutId());
		setTitle();
		
		set(LOADING);
		loadData();
	}
	
	protected void setTitle()
	{
		String title = getScreenTitleValue();
		if (title != null)
		{
			TextView textView = getScreenTitleView();
			if (textView != null)
			{
				textView.setText(title);
			}
		}
	}

	public void set(final int status)
	{
		runOnUiThread(new Runnable()
		{
			private void setVisibility(final View view, final int value)
			{
				if (view != null)
				{
					view.setVisibility(value);
				}
			}

			@Override
			public void run()
			{
				setVisibility(getLoadingProgressBar(), status == LOADING ? View.VISIBLE : View.GONE);
				setVisibility(getListView(), status == LOADED_SUCCESS ? View.VISIBLE : View.GONE);
				setVisibility(getNoDataView(), status == LOADED_FAIL ? View.VISIBLE : View.GONE);
				if (status == LOADED_FAIL)
				{
					onNoDataAvailable();
				}
			}
		});
	}
	
	protected abstract TextView getScreenTitleView();

	protected abstract String getScreenTitleValue();

	protected abstract int getLayoutId();

	protected abstract void loadData();

	protected abstract void onNoDataAvailable();

	protected abstract View getNoDataView();

	public abstract ListView getListView();
	
	public abstract View getListViewHeader();

	protected abstract ProgressBar getLoadingProgressBar();

}
