package ubiquitous.computing.behaviorcollection.ui;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ubiquitous.computing.behaviorcollection.R;
import ubiquitous.computing.behaviorcollection.Util;

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
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);

        Button btnHome = (Button) rootView.findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHomelocation();
            }
        });
        Button btnSchool = (Button) rootView.findViewById(R.id.btn_school);
        btnSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSchoolLocation();
            }
        });
        Button btnOther = (Button) rootView.findViewById(R.id.btn_other);
        btnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickOtherLocation();
            }
        });

        return rootView;
    }

    public void clickHomelocation() {
        new SenseLocationTask(getActivity()).execute();
        Util.logLocation(getActivity(), Util.TAG_HOME);
    }

    public void clickSchoolLocation() {
        new SenseLocationTask(getActivity()).execute();
        Util.logLocation(getActivity(), Util.TAG_SCHOOL);
    }

    public void clickOtherLocation() {
        showOtherPlaceDialog();
    }

    public void showOtherPlaceDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new OtherPlaceDialogFragment();
        dialog.show(getFragmentManager(), "OtherPlaceDialogFragment");
    }
}
