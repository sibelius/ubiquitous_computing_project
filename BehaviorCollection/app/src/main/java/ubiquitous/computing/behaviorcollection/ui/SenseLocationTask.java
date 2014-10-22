package ubiquitous.computing.behaviorcollection.ui;

import android.content.Context;
import android.os.AsyncTask;

import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.sensors.SensorUtils;

import ubiquitous.computing.behaviorcollection.sensing.BehaviorStoreLogger;

/**
 * Created by sibelius on 10/21/14.
 */
public class SenseLocationTask extends AsyncTask<Void, Void, Void> {
    private final Context mContext;
    protected ESSensorManager mSensorManager;
    protected BehaviorStoreLogger mBehaviorStoreLogger;

    public SenseLocationTask(final Context context) {
        mContext = context;
        try
        {
            mSensorManager = ESSensorManager.getSensorManager(context);
            mBehaviorStoreLogger = BehaviorStoreLogger.getBehaviorStoreLogger(mContext);
        }
        catch (ESException e)
        {
            e.printStackTrace();
        } catch (DataHandlerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            SensorData sensorData = mSensorManager.getDataFromSensor(SensorUtils.SENSOR_TYPE_LOCATION);
            mBehaviorStoreLogger.logSensorData(sensorData);
        } catch (ESException e) {
            e.printStackTrace();
        }

        return null;
    }
}
