package com.hubdub.meetr.fragments;

import java.util.ArrayList;
import java.util.Collection;

import com.hubdub.meetr.R;
import com.hubdub.meetr.adapters.EventTimelineAdapter;
import com.hubdub.meetr.models.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class EventTimelineFragment extends Fragment {
	private ListView lvTimeline;
	private EventTimelineAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(com.hubdub.meetr.R.layout.fragment_event_timeline, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ArrayList<Activities> activities = new ArrayList<Activities>();
		activities.addAll(generateDummyActivities());
		adapter = new EventTimelineAdapter(getActivity(), activities);
		lvTimeline = (ListView) getActivity().findViewById(R.id.lvTimeline);
		
		lvTimeline.setAdapter(adapter);
		
		// load the activities
	}

	private Collection<? extends Activities> generateDummyActivities() {
		ArrayList<Activities> dummyActivities = new ArrayList<Activities>();
		
		for(int i = 0; i < 20; i++) {
			dummyActivities.add(new Activities("List item " + i));
		}

		return dummyActivities;
	}
	
}
