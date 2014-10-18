package ubiquitous.computing.behaviorcollection;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sibelius on 10/17/14.
 */
public class Util {
    public static final String BEHAVIOR_COLLECTION_PREFS = "BehaviorCollectionPrefs";

    public static final String TAG_USERNAME = "username";
    public static final String TAG_SENSOR_NAME = "sensorname";
    public static final String TAG_INTERESTING = "isinteresting";
    public static final String TAG_TIMESTAMP = "timestamp";
    public static final String TAG_LOCAL_TIME = "localTime";
    public static final String TAG_CLASSIFIER = "classifier";

    @SuppressLint("SimpleDateFormat")
    public static String localTime()
    {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zZ");
        return dateFormat.format(calendar.getTime());
    }

}
