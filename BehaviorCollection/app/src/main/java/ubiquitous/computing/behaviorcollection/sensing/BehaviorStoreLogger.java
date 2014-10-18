package ubiquitous.computing.behaviorcollection.sensing;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.loggertypes.AbstractStoreOnlyLogger;
import com.ubhave.sensormanager.ESException;

import ubiquitous.computing.behaviorcollection.Util;

/**
 * Created by sibelius on 10/17/14.
 */
public class BehaviorStoreLogger extends AbstractStoreOnlyLogger {
    private static final String LOCAL_STORAGE_DIRECTORY = "BehaviorCollection";

    public BehaviorStoreLogger(Context context) throws DataHandlerException, ESException {
        super(context);
    }

    @Override
    protected String getLocalStorageDirectoryName() {
        return LOCAL_STORAGE_DIRECTORY;
    }

    @Override
    protected String getUniqueUserId() {
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        SharedPreferences settings = context.getSharedPreferences(Util.BEHAVIOR_COLLECTION_PREFS, 0);
        return settings.getString(Util.TAG_USERNAME, "uniqueuserid" + mngr.getDeviceId());
    }

    @Override
    protected boolean shouldPrintLogMessages() {
        return true;
    }

    @Override
    protected String getDeviceId() {
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mngr.getDeviceId();
    }
}
