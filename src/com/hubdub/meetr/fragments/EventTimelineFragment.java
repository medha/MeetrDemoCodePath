package com.hubdub.meetr.fragments;

import java.util.ArrayList;

import com.hubdub.meetr.R;
import com.hubdub.meetr.adapters.EventTimeLineAdapter;
import com.hubdub.meetr.models.EventActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class EventTimelineFragment extends Fragment {
	private ListView lvTimeline;
	private EventTimeLineAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_event_timeline, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ArrayList<EventActivity> activities = new ArrayList<EventActivity>();
		adapter = new EventTimeLineAdapter(getActivity(), activities);
		lvTimeline = (ListView) getActivity().findViewById(R.id.lvTimeline);
		
		lvTimeline.setAdapter(adapter);
		
		// load the activities
	}

}
