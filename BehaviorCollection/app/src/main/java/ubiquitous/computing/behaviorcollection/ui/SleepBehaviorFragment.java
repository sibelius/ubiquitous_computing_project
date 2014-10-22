package ubiquitous.computing.behaviorcollection.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.sensormanager.ESException;
import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.triggers.TriggerUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.ubhave.intelligenttrigger.IntelligentTriggerManager;
import org.ubhave.intelligenttrigger.IntelligentTriggerReceiver;
import org.ubhave.intelligenttrigger.learners.LearnerResultBundle;
import org.ubhave.intelligenttrigger.triggers.config.BasicTriggerConfig;
import org.ubhave.intelligenttrigger.triggers.definitions.TimeTriggerDefinition;
import org.ubhave.intelligenttrigger.utils.ITException;

import java.util.ArrayList;

import ubiquitous.computing.behaviorcollection.R;
import ubiquitous.computing.behaviorcollection.Util;
import ubiquitous.computing.behaviorcollection.sensing.BehaviorStoreLogger;

/**
 * Created by sibelius on 10/17/14.
 */
public class SleepBehaviorFragment extends Fragment implements IntelligentTriggerReceiver {
    private static final String SLEEP_BEHAVIOR_TRIGGER_ID = "SleepBehaviorTrigger";
    private static final String TAG_SWICTH_SLEEPING = "isSleeping";
    private static final String TAG_SLEEPING = "sleeping";

    private Switch mBtnSleeping;
    private boolean mAlreadyChanged = false;

    private SharedPreferences mPrefs;

    private AbstractDataLogger mDataLogger;

    private IntelligentTriggerManager mIntelligentTriggerManager;

    public SleepBehaviorFragment() {
    }

    public static SleepBehaviorFragment newInstance() {
        SleepBehaviorFragment fragment = new SleepBehaviorFragment();

        fragment.addTrigger();

        return fragment;
    }

    public void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(TAG_SWICTH_SLEEPING, mBtnSleeping.isChecked());
        editor.apply();
    }

    public void onResume() {
        super.onResume();
        boolean isOn = mPrefs.getBoolean(TAG_SWICTH_SLEEPING, false);

        if (isOn != mBtnSleeping.isChecked()) {
            mAlreadyChanged = true;
            mBtnSleeping.setChecked(isOn);
        } else
            mAlreadyChanged = false;
    }

    public void addTrigger() {

        BasicTriggerConfig config = new BasicTriggerConfig();
        config.addParam(TriggerConfig.INTERVAL_TRIGGER_START_DELAY, 10 * 1000L); // Wait 10 seconds
        config.addParam(TriggerConfig.INTERVAL_TIME_MILLIS, 30 * 60 * 1000L); // Each 30 minutes
        config.addParam(TriggerConfig.MAX_DAILY_NOTIFICATION_CAP, 15); // Monitor only 15 intervals of sleep

        TimeTriggerDefinition definition = new TimeTriggerDefinition(
                TriggerUtils.TYPE_CLOCK_TRIGGER_ON_INTERVAL, config);

        try {
            mIntelligentTriggerManager = IntelligentTriggerManager.getTriggerManager(getActivity());
            mIntelligentTriggerManager.addIntelligentTrigger(
                    SLEEP_BEHAVIOR_TRIGGER_ID, definition, this);
            mIntelligentTriggerManager.pauseIntelligentTrigger(SLEEP_BEHAVIOR_TRIGGER_ID);
        } catch (ITException e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sleep_behavior, container, false);

        mPrefs = getActivity().getSharedPreferences(Util.BEHAVIOR_COLLECTION_PREFS, 0);

        mBtnSleeping = (Switch) rootView.findViewById(R.id.btn_sleep);
        mBtnSleeping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                clickChangeSleepAwake(isChecked);
            }
        });
        return rootView;
    }

    public void clickChangeSleepAwake(final boolean isChecked) {
        // TODO put a trigger to monitor the sleeping sensor data
        // The user is going to sleep

        if (!mAlreadyChanged) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            try {
                                changeMonitoringState(isChecked);
                            } catch (ITException e) {
                                e.printStackTrace();
                            }

                            logSleepBehavior(isChecked);

                            //Yes button clicked
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            mAlreadyChanged = true;
                            mBtnSleeping.setChecked(!isChecked);
                            break;
                    }
                }
            };

            String message;
            if (isChecked) {
                message = "Are you going to sleep?";
            } else { // The user is going to wake up
                message = "Are you going to wake up?";
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message).setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        } else {
            mAlreadyChanged = false;
        }
    }

    private void logSleepBehavior(boolean isChecked) {
        // log sleeping or wakening up
        JSONObject json = new JSONObject();
        try {
            json.put(TAG_SLEEPING, isChecked);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            BehaviorStoreLogger.getBehaviorStoreLogger(getActivity()).logExtra(Util.TAG_LABEL, json);
        } catch (ESException e) {
            e.printStackTrace();
        } catch (DataHandlerException e) {
            e.printStackTrace();
        }
    }

    public void changeMonitoringState(boolean isSleeping) throws ITException {
        if (isSleeping) {
            IntelligentTriggerManager.getTriggerManager(getActivity()).unpauseIntelligentTrigger(SLEEP_BEHAVIOR_TRIGGER_ID);
        } else {
            IntelligentTriggerManager.getTriggerManager(getActivity()).pauseIntelligentTrigger(SLEEP_BEHAVIOR_TRIGGER_ID);
        }
    }

    // Monitor sleep behavior
    @Override
    public void onTriggerReceived(String a_triggerID, ArrayList<LearnerResultBundle> a_bundles) {
        // TODO  monitor this sensor below to infer sleep behavior
        Log.d("SLEEPBEHAVIORTRIGGER", "START");
        Util.manageService(getActivity(), true);

        // Monitor per 3 minutes
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("SLEEPBEHAVIORTRIGGER", "STOP");
                Util.manageService(getActivity(), false);
            }
        }, 3 * 60 * 1000L);
    }
}
