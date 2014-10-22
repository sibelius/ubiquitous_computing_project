package ubiquitous.computing.behaviorcollection.ui;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.sensormanager.ESException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ubiquitous.computing.behaviorcollection.CustomAdapter;
import ubiquitous.computing.behaviorcollection.ImageText;
import ubiquitous.computing.behaviorcollection.R;
import ubiquitous.computing.behaviorcollection.Util;
import ubiquitous.computing.behaviorcollection.sensing.BehaviorStoreLogger;

/**
 * Created by sibelius on 10/17/14.
 */
public class PhysicalActivityFragment extends Fragment {

    private TextView mCountDown;
    private Spinner mActivities;
    private Button mActivityButton;

    private AbstractDataLogger mDataLogger;

    private SharedPreferences mPrefs;

    // Is performing any physical activity
    private boolean isPerforming = false;

    private Counter counter;


    public PhysicalActivityFragment() {

    }

    public static PhysicalActivityFragment newInstance() {
        PhysicalActivityFragment fragment = new PhysicalActivityFragment();
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mDataLogger = BehaviorStoreLogger.getBehaviorStoreLogger(getActivity());
        } catch (DataHandlerException e) {
            e.printStackTrace();
        } catch (ESException e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_physical_activity, container, false);

        mPrefs = getActivity().getSharedPreferences(Util.BEHAVIOR_COLLECTION_PREFS, 0);

        mActivityButton = (Button) rootView.findViewById(R.id.activity_button);
        mActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickStartActivity();
            }
        });

        mCountDown = (TextView) rootView.findViewById(R.id.countdown);
        mActivities = (Spinner) rootView.findViewById(R.id.spnActivity);

        CustomAdapter adapter = new CustomAdapter(this.getActivity(), R.layout.spinner_rows, getPaTypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mActivities.setAdapter(adapter);
        mActivities.setSelection(0);

        return rootView;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(Util.TAG_PERFORMING, isPerforming);
        editor.putInt(Util.TAG_SELECTED_ACTIVITY, mActivities.getSelectedItemPosition());
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("FINISH", "RESUME");

        isPerforming = mPrefs.getBoolean(Util.TAG_PERFORMING, false);
        switchButtons(!isPerforming);
        mActivities.setSelection(mPrefs.getInt(Util.TAG_SELECTED_ACTIVITY, 0));

        if (isPerforming) {
            long startTime = mPrefs.getLong(Util.TAG_START, 0);
            long remainTime = Util.TOTAL_TIME - (System.currentTimeMillis() - startTime);
            Log.d("STARTTIME", "" + startTime);
            Log.d("REMAINTIME", "" + remainTime);

            if (counter != null) {
                Log.d("COUNTER", "NULL");
                counter.cancel();
            } else {
                Log.d("COUNTER", "not NULL");
            }

            if (remainTime > 0) {
                counter = new Counter(remainTime, 1000);
                counter.start();
                Log.d("FINISH", "RESTART");
            } else {
                if (isPerforming) {
                    Log.d("FINISH", "NEGATIVE REMAIN PERFORMING");
                    finishCounter();
                } else
                    Log.d("FINISH", "NEGATIVE REMAIN not PERFORMING");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("FINISH", "DESTROY");

        if (counter != null)
            counter.cancel();
    }

    /**
     * Create a list of Images and Text with the Physical Activities (PA) types
     * @return
     */
    private ArrayList<ImageText> getPaTypes() {
        ArrayList<ImageText> values = new ArrayList<ImageText>();

        values.add(new ImageText(R.string.walking, R.drawable.walking));
        values.add(new ImageText(R.string.running, R.drawable.running));
        values.add(new ImageText(R.string.sitting, R.drawable.sitting));
        values.add(new ImageText(R.string.standing, R.drawable.standing));
        values.add(new ImageText(R.string.driving, R.drawable.driving));

        return values;
    }

    /**
     * A CountDownTimer to ensure the data collection task
     */
    class Counter extends CountDownTimer {
        public Counter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
            long seconds = millisUntilFinished / 1000;
            if (seconds > 60) {
                if (seconds == 65) {
                    mCountDown.setText("Go!");
                } else
                    mCountDown.setText(String.valueOf(seconds - 61));
                System.out.println("Remaining: " + seconds);
            } else if (seconds == 60) {
                logEvent(Util.TAG_ACTIVITY_START);
                mCountDown.setText(seconds + " seconds");
            } else {
                mCountDown.setText(seconds + " seconds");
                System.out.println("Remaining: " + seconds);
            }
        }

        public void onFinish() {
            finishCounter();
        }
    }

    private void finishCounter() {
        Log.d("FINISH", "true");
        mCountDown.setText("Done!");
        logEvent(Util.TAG_ACTIVITY_STOP);

        isPerforming = false;
        switchButtons(!isPerforming);

        // Wait 30 seconds to finish the sensor data collection
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Util.manageService(getActivity(), false);
            }
        }, 30 * 1000L);

        // Required if the count down finish in the background
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putBoolean(Util.TAG_PERFORMING, isPerforming);
        editor.putLong(Util.TAG_START, 0);
        editor.apply();
    }

    private void switchButtons(boolean enabled) {
        mActivityButton.setEnabled(enabled);
        mActivities.setEnabled(enabled);
    }

    public void clickStartActivity() {
        isPerforming = true;
        switchButtons(!isPerforming);

        Util.manageService(getActivity(), true);

        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putLong(Util.TAG_START, System.currentTimeMillis());
        editor.apply();
        counter = new Counter(Util.TOTAL_TIME, 1000);
        counter.start();

    }

    private void logEvent(String event) {
        JSONObject json = new JSONObject();
        try {
            int activityId = ((ImageText) mActivities.getSelectedItem()).getDescription();
            json.put(Util.TAG_ACTIVITY, getResources().getString(activityId));
            json.put(Util.TAG_EVENT, event);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mDataLogger.logExtra(Util.TAG_LABEL, json);
    }
}
