package com.hubdub.meetr.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import com.hubdub.meetr.R;
import com.hubdub.meetr.fragments.EventDetailFragment;
import com.hubdub.meetr.fragments.EventTimelineFragment;
import com.hubdub.meetr.fragments.PhotoGridFragment;

public class EventDetailActivity extends FragmentActivity implements ActionBar.TabListener {
	
	/* Remeber if this becomes too memory intensive switch to statePagerAdapter */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail_frame);
		
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        PagerTabStrip strip = (PagerTabStrip)findViewById(R.id.pager_title_strip);
        strip.setTabIndicatorColor(0x999999);
        strip.setDrawFullUnderline(true);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            }
        });
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();  //This should be compatible with API 5+
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	Fragment fragment = null;
            switch (i) {
                case 0:
                	fragment = new EventDetailFragment();
                    return fragment;
                case 1:
                	fragment = new EventTimelineFragment();
                    return fragment;
                    
                case 2:
                	fragment = new PhotoGridFragment();
                	return fragment;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position){
            	case 0:
            		return "Details";
            	case 1:
            		return "Timeline";
            	case 2: 
            		return "Photos";
            }
			return "Event";
        }
    }
}
