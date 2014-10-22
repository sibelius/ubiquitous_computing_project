package ubiquitous.computing.behaviorcollection;

import android.content.Context;
import android.content.Intent;

import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.sensormanager.ESException;

import org.json.JSONException;
import org.json.JSONObject;

import ubiquitous.computing.behaviorcollection.sensing.BehaviorStoreLogger;
import ubiquitous.computing.behaviorcollection.sensing.SensingService;

/**
 * Created by sibelius on 10/17/14.
 */
public class Util {
    public static final String BEHAVIOR_COLLECTION_PREFS = "BehaviorCollectionPrefs";

    public static final String TAG_LABEL = "Label";
    public static final String TAG_ONDATASENSED = "onDataSensed";

    public static final String TAG_USERNAME = "username";
    public static final String TAG_SENSOR_NAME = "sensorname";
    public static final String TAG_INTERESTING = "isinteresting";
    public static final String TAG_CLASSIFIER = "classifier";
    public static final String TAG_ACTIVITY = "activity";
    public static final String TAG_EVENT = "event";
    public static final String TAG_ACTIVITY_START = "start";
    public static final String TAG_ACTIVITY_STOP = "stop";
    public static final String TAG_PERFORMING = "performing";
    public static final String TAG_START = "starttime";
    public static final String TAG_SELECTED_ACTIVITY = "selected_activity";
    public static final long TOTAL_TIME = 66 * 1000L;
    public static final String TAG_PLACE = "place";
    public static final String TAG_HOME = "home";
    public static final String TAG_SCHOOL = "school";

    /**
     * Start or stop the sensing service
     * @param start decide whether to start or stop the service
     */
    public static void manageService(Context context, boolean start) {
        Intent serviceIntent = new Intent(context, SensingService.class);
        if (start)
            context.startService(serviceIntent);
        else
            context.stopService(serviceIntent);
    }

    public static void logLocation(Context context, String location) {
        // log sleeping or wakening up
        JSONObject json = new JSONObject();
        try {
            json.put(TAG_PLACE, location);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            BehaviorStoreLogger.getBehaviorStoreLogger(context).logExtra(TAG_LABEL, json);
        } catch (ESException e) {
            e.printStackTrace();
        } catch (DataHandlerException e) {
            e.printStackTrace();
        }


    }
}
