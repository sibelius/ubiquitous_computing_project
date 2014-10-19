package ubiquitous.computing.behaviorcollection.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.ToggleButton;

import ubiquitous.computing.behaviorcollection.R;

/**
 * Created by sibelius on 10/17/14.
 */
public class SleepBehaviorFragment extends Fragment {
    private Switch mBtnSleeping;

    public SleepBehaviorFragment() {

    }

    public static SleepBehaviorFragment newInstance() {
        SleepBehaviorFragment fragment = new SleepBehaviorFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sleep_behavior, container, false);
        mBtnSleeping = (Switch) rootView.findViewById(R.id.btn_sleep);
        mBtnSleeping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickChangeSleepAwake(view);
            }
        });
        return rootView;
    }

    public void clickChangeSleepAwake(View view) {
        // Is the toggle on?
        boolean isSleeping = ((ToggleButton) view).isChecked();

        // TODO put a confirmation dialog
        // TODO log the sleeping or awakeness event
        // TODO put a trigger to monitor the sleeping sensor data
        // The user is going to sleep
        if (isSleeping) {

        } else { // The user is going to wake up

        }

    }
/*
    private void logEvent(String event) {
        JSONObject json = new JSONObject();
        try {
            int activityId = ((ImageText) mActivities.getSelectedItem()).getDescription();
            json.put(TAG_ACTIVITY, getResources().getString(activityId));
            json.put(TAG_EVENT, event);
            json.put(TAG_TIMESTAMP, System.currentTimeMillis());
            json.put(TAG_LOCAL_TIME, Util.localTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mDataLogger.logExtra(TAG_LABEL, json);
    }
    */
}
