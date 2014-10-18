package ubiquitous.computing.behaviorcollection.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ubiquitous.computing.behaviorcollection.R;

/**
 * Created by sibelius on 10/17/14.
 */
public class SleepBehaviorFragment extends Fragment {
    public SleepBehaviorFragment() {

    }

    public static SleepBehaviorFragment newInstance() {
        SleepBehaviorFragment fragment = new SleepBehaviorFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sleep_behavior, container, false);
    }
}
