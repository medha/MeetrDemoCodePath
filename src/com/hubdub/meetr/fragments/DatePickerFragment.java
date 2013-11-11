package com.hubdub.meetr.fragments;

import java.util.Calendar;

import com.hubdub.meetr.fragments.TimePickerFragment.TimePickedListener;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements OnDateSetListener{
	
	private DatePickedListener mListener;
	
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Log.w("DatePicker", "Date = " + year);
	        mListener.onDateSet(view, year, monthOfYear, dayOfMonth);
	}
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		mListener = (DatePickedListener) activity;
	}
	
	public static interface DatePickedListener
    {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth);
    }
}
