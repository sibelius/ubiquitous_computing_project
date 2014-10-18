package ubiquitous.computing.behaviorcollection.sensing;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.datahandler.loggertypes.AbstractDataLogger;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.ESSensorManager;

import ubiquitous.computing.behaviorcollection.R;
import ubiquitous.computing.behaviorcollection.ui.BehaviorCollectionActivity;

/**
 *  This service collect data from sensors and save locally and remotely
 *
 *  @author sibelius
 */
public class SensingService extends Service {
    private static final int ONGOING_NOTIFICATION_ID = 192;
    private boolean mAllowRebind = false;
    private ESSensorManager mSensorManager;
    private BehaviorSensingManager mBehaviorSensing;

    @Override
    public void onCreate() {
        // The service is being created
        try {
            System.out.println("ESSensor Service");
            mSensorManager = ESSensorManager.getSensorManager(this);
        } catch (ESException e) {
            e.printStackTrace();
        }

        AbstractDataLogger mDataLogger = null;
        try {
            mDataLogger = new BehaviorStoreLogger(this);
        } catch (DataHandlerException e) {
            e.printStackTrace();
        } catch (ESException e) {
            e.printStackTrace();
        }
        mBehaviorSensing = new BehaviorSensingManager(this, mDataLogger);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // The service is starting, due to a call to startService()
        mBehaviorSensing.startSensing();

        // Put service in foreground
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.treatment)
                .setContentTitle(getResources().getString(R.string.service_notification));

        Intent notificationIntent = new Intent(this, BehaviorCollectionActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        mBuilder.setContentIntent(pendingIntent);
        startForeground(ONGOING_NOTIFICATION_ID, mBuilder.build());

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // A client is binding to the service with bindService()
        mBehaviorSensing.startSensing();
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        mBehaviorSensing.stopSensing();
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // The service is no longer used and is being destroyed
        mBehaviorSensing.stopSensing();
        stopForeground(true);
    }
}
