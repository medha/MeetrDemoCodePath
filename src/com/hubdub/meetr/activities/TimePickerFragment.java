package com.hubdub.meetr.activities;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements
					TimePickerDialog.OnTimeSetListener{
	private TimePickedListener mListener;
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		mListener = (TimePickedListener) activity;
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		Log.w("TimePicker","Time (insidefrag) = " + hourOfDay + ":" + minute);
		 Calendar c = Calendar.getInstance();
	     c.set(Calendar.HOUR_OF_DAY, hourOfDay);
	     c.set(Calendar.MINUTE, minute);
	     mListener.onTimePicked(c);
	}
	
	public static interface TimePickedListener
    {
        public void onTimePicked(Calendar time);
    }
}
