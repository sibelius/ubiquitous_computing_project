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
public class LocationFragment extends Fragment {
    public LocationFragment() {

    }

    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }
}
