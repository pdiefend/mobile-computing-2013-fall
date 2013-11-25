package mobilecomputing.ssagui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

public class AddSatelliteDialogue extends DialogFragment {

	public final String TAG = "Add Sat Dialog";

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();

		builder.setView(inflater.inflate(R.layout.activity_add_satellite, null));

		builder.setPositiveButton(R.string.cont, new OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Dialog dialog2 = (Dialog) dialog;
				EditText edit = (EditText) dialog2
						.findViewById(R.id.addSatelliteName);
				EditText edit2 = (EditText) dialog2
						.findViewById(R.id.addSatelliteNumber);
				String satelliteName = edit.getText().toString();
				String satelliteNumber = "(" + edit2.getText().toString() + ")";
				String satellite = satelliteName + " #" + satelliteNumber;

				if (!satellite.equals(" #")) {
					((MainActivity) getActivity()).addItems(getView(),
							satelliteName, satelliteNumber, true);
				} else {
					Log.w("Add Satellite", "aborted");
				}

			}
		}).setTitle(R.string.add_satellite_title);

		return builder.create();
	}
}
