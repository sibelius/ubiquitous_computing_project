package org.ubhave.intelligenttrigger.triggers.config;

import com.ubhave.triggermanager.config.TriggerConfig;
import com.ubhave.triggermanager.config.TriggerManagerConstants;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by sibelius on 10/18/14.
 */
public class BasicTriggerConfig {
    /*
	 * Config Keys
	 */
    public static final String IGNORE_USER_PREFERENCES = "ignoreCap";
    public static final String MAX_DAILY_NOTIFICATION_CAP = "limitDailyCap";
    public static final String TRIGGER_ENABLED = "isEnabled";

    // Clock based Triggers
    public final static String CLOCK_TRIGGER_DATE_MILLIS = "clockTriggerDate";
    public final static String INTERVAL_TIME_MILLIS = "intervalTriggerTime";
    public final static String INTERVAL_TRIGGER_START_DELAY = "intervalTriggerStartDelay";
    public final static String NUMBER_OF_NOTIFICATIONS = "numberOfNotifications";

    public final static String DAILY_HOUR = "dailyHour";
    public final static String DAILY_MINUTE = "dailyMinute";

    // Sensor Based Triggers
    public final static String SENSOR_TYPE = "sensorType";
    public final static String NOTIFICATION_PROBABILITY = "notificationProb";
    public final static String POST_SENSE_WAIT_INTERVAL_MILLIS = "postSenseWait";

    // Time Boundaries
    public static final String DO_NOT_DISTURB_BEFORE_MINUTES = "limitBeforeHour";
    public static final String DO_NOT_DISTURB_AFTER_MINUTES = "limitAfterHour";
    public static final String MIN_TRIGGER_INTERVAL_MINUTES = "notificationMinInterval";

    private final HashMap<String, Object> parameters;

    public BasicTriggerConfig()
    {
        parameters = new HashMap<String, Object>();
    }

    public void addParam(final String key, final Object value)
    {
        parameters.put(key, value);
    }

    public Object getParam(final String key)
    {
        if (parameters.containsKey(key))
        {
            return parameters.get(key);
        }
        else
        {
            return defaultValue(key);
        }
    }

    public Set<String> getAllParams() {
        return parameters.keySet();
    }

    private Object defaultValue(final String key)
    {
        if (key.equals(DO_NOT_DISTURB_BEFORE_MINUTES))
        {
            return TriggerManagerConstants.DEFAULT_DO_NOT_DISTURB_BEFORE_MINUTES;
        }
        else if (key.equals(DO_NOT_DISTURB_AFTER_MINUTES))
        {
            return TriggerManagerConstants.DEFAULT_DO_NOT_DISTURB_AFTER_MINUTES;
        }
        else if (key.equals(MIN_TRIGGER_INTERVAL_MINUTES))
        {
            return TriggerManagerConstants.DEFAULT_MIN_TRIGGER_INTERVAL_MINUTES;
        }
        else if (key.equals(MAX_DAILY_NOTIFICATION_CAP))
        {
            return TriggerManagerConstants.DEFAULT_DAILY_NOTIFICATION_CAP;
        }
        else if (key.equals(TRIGGER_ENABLED))
        {
            return TriggerManagerConstants.DEFAULT_TRIGGER_ENABLED;
        }
        else if (key.equals(DO_NOT_DISTURB_BEFORE_MINUTES))
        {
            return TriggerManagerConstants.DEFAULT_DO_NOT_DISTURB_BEFORE_MINUTES;
        }
        else if (key.equals(DO_NOT_DISTURB_AFTER_MINUTES))
        {
            return TriggerManagerConstants.DEFAULT_DO_NOT_DISTURB_AFTER_MINUTES;
        }
        else if (key.equals(MIN_TRIGGER_INTERVAL_MINUTES))
        {
            return TriggerManagerConstants.DEFAULT_MIN_TRIGGER_INTERVAL_MINUTES;
        }
        else if (key.equals(IGNORE_USER_PREFERENCES))
        {
            return TriggerManagerConstants.DEFAULT_IS_SYSTEM_TRIGGER;
        }
        else
        {
            System.err.println("Key not found: "+key);
            return null;
        }
    }

    public boolean containsParam(String key)
    {
        return parameters.containsKey(key);
    }

    public boolean isSystemTrigger()
    {
        return (Boolean) getParam(TriggerConfig.IGNORE_USER_PREFERENCES);
    }

    public int numberOfNotifications()
    {
        try
        {
            return (Integer) getParam(TriggerConfig.NUMBER_OF_NOTIFICATIONS);
        }
        catch (java.lang.ClassCastException e)
        {
            return ((Long) getParam(TriggerConfig.NUMBER_OF_NOTIFICATIONS)).intValue();
        }
    }

    public int getValueInMinutes(String key)
    {
        return (Integer) getParam(key);
    }
}
