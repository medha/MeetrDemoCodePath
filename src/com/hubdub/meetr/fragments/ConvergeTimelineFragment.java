package com.hubdub.meetr.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hubdub.meetr.R;
import com.hubdub.meetr.activities.EventDetailActivity;
import com.hubdub.meetr.adapters.ConvergeTimelineAdapter;

public class ConvergeTimelineFragment extends Fragment{
	ListView lvConvergeItems;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_converge_timeline, container, false);
		return view;
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		lvConvergeItems = (ListView) getView().findViewById(R.id.lvConvergeItems);
		
		// Dummy items. Delete things below this line:
		
		ArrayList<String> dummyStrings = new ArrayList<String>();
		dummyStrings.add("Judy suggests Starbucks as the new location.");
		dummyStrings.add("Mark prefers to meet at 8:00pm.");
		dummyStrings.add("Anita suggests meeting on 10th Nov.");
		
		ConvergeTimelineAdapter adapter = new ConvergeTimelineAdapter(getActivity(), dummyStrings);
		
		lvConvergeItems.setAdapter(adapter);
	}

}
