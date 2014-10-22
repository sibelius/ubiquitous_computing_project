package ubiquitous.computing.behaviorcollection.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ubiquitous.computing.behaviorcollection.R;

/**
 * Created by sibelius on 10/21/14.
 */
public class OtherPlaceDialogFragment extends DialogFragment {

    private EditText mOtherPlace;

    public interface OtherPlaceDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String otherPlace);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    OtherPlaceDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (OtherPlaceDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement OtherPlaceDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_other_place, null);
        mOtherPlace = (EditText) view.findViewById(R.id.otherplacename);

        builder.setView(view)
            .setTitle(R.string.title_other_place)
            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mListener.onDialogPositiveClick(OtherPlaceDialogFragment.this,
                            mOtherPlace.getText().toString());
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mListener.onDialogNegativeClick(OtherPlaceDialogFragment.this);
                }
            });

        return builder.create();
    }
}
