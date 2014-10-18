package ubiquitous.computing.behaviorcollection.sensing;

import android.content.Context;
import android.util.Log;

import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.SensorDataListener;
import com.ubhave.sensormanager.classifier.SensorClassifiers;
import com.ubhave.sensormanager.classifier.SensorDataClassifier;
import com.ubhave.sensormanager.config.pull.PullSensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ubiquitous.computing.behaviorcollection.Util;

/**
 * This class handle the configuration and sensing of the sensor
 *
 * @author sibelius
 * Created by sibelius on 6/1/14.
 */
public class BehaviorSensingManager implements SensorDataListener {

    private ESSensorManager mSensorManager;
    private AbstractDataLogger mDataLogger;
    private Context mContext;
    private ArrayList<Integer> mSubscriptions;

    private int[] MONITORED_SENSORS = new int[] {
            // Physical Activities
            SensorUtils.SENSOR_TYPE_ACCELEROMETER,
            SensorUtils.SENSOR_TYPE_GYROSCOPE,

            // Sleep Behavior
            SensorUtils.SENSOR_TYPE_LIGHT,
            SensorUtils.SENSOR_TYPE_PROXIMITY,
            SensorUtils.SENSOR_TYPE_SCREEN,
            SensorUtils.SENSOR_TYPE_MICROPHONE,
            SensorUtils.SENSOR_TYPE_BATTERY,

            // Location
            SensorUtils.SENSOR_TYPE_LOCATION
    };

    public BehaviorSensingManager(final Context context, final AbstractDataLogger logger) {
        this.mContext = context;
        this.mDataLogger = logger;
        try {
            mSensorManager = ESSensorManager.getSensorManager(context);
            setSensorConfig();
        } catch(ESException e) {
            mSensorManager = null;
            e.printStackTrace();
        }
    }

    private void setSensorConfig() {
        if (mSensorManager != null) {
            try {
                // 50 HZ
                // sleep only 1 second, necessary to save the data
                long sleepMotion = 1 * 1000L;
                // Sample 25 seconds each time
                long sampleWindow = 25 * 1000L;

                /*
                 * Set Motion sensing params
                 */
                mSensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER,
                        PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, sleepMotion);

                mSensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_GYROSCOPE,
                        PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, sleepMotion);


                // Sense window
                mSensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_ACCELEROMETER,
                        PullSensorConfig.SENSE_WINDOW_LENGTH_MILLIS, sampleWindow);
                mSensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_GYROSCOPE,
                        PullSensorConfig.SENSE_WINDOW_LENGTH_MILLIS, sampleWindow);

                // Sleep for 10 second and gather more data
                long locationSleep = 10 * 1000L;
                /*
                 * Set location sensing params

                mSensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_LOCATION,
                        LocationConfig.ACCURACY_TYPE, LocationConfig.LOCATION_ACCURACY_FINE);
                mSensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_LOCATION,
                        PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, locationSleep);
*/
                /*
                 * Set microphone sensing params
                */
                //sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_MICROPHONE, PullSensorConfig.SENSE_WINDOW_LENGTH_MILLIS, 2000L);
                //sensorManager.setSensorConfig(SensorUtils.SENSOR_TYPE_MICROPHONE, PullSensorConfig.POST_SENSE_SLEEP_LENGTH_MILLIS, 5000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Subscribe to get the sensor data for all monitored sensors
     *
     * @return true if the subscribe worked well
     */
    public boolean startSensing()
    {
        int subscriptionId;
        mSubscriptions = new ArrayList<Integer>();

        try
        {
            for (int sensorType : MONITORED_SENSORS) {
                subscriptionId = mSensorManager.subscribeToSensorData(sensorType, this);
                mSubscriptions.add(subscriptionId);
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Unsubscribe to get the sensor data for all monitored sensors
     *
     * @return true if the unsubscribe worked well
     */
    public boolean stopSensing()
    {
        try
        {
            for (Integer subscriptionId : mSubscriptions) {
                Log.d("Sensor", "Unsubscribing id = " + subscriptionId);
                mSensorManager.unsubscribeFromSensorData(subscriptionId);
            }
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // SensorDataListener
    @Override
    public void onDataSensed(SensorData data) {
        System.out.println("onDataSensed");
        mDataLogger.logSensorData(data);

        String name = "";
        try {
            name = SensorUtils.getSensorName(data.getSensorType());
            logClassification(data);
        } catch (ESException e) {
            Log.d("NOCLASSIFIER", name);
        }
    }

    @Override
    public void onCrossingLowBatteryThreshold(boolean isBelowThreshold) {
        // Nothing for this behavior collection app
    }

    /**
     * Log the classification result
     * @param data
     * @throws ESException
     */
    protected void logClassification(SensorData data) throws ESException {
        SensorDataClassifier classifier = SensorClassifiers.getSensorClassifier(data.getSensorType());

        JSONObject json = new JSONObject();
        try {
            json.put(Util.TAG_SENSOR_NAME, SensorUtils.getSensorName(data.getSensorType()));
            json.put(Util.TAG_INTERESTING, classifier.isInteresting(data));
            json.put(Util.TAG_TIMESTAMP, System.currentTimeMillis());
            json.put(Util.TAG_LOCAL_TIME, Util.localTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mDataLogger.logExtra(Util.TAG_CLASSIFIER, json);
    }
}
