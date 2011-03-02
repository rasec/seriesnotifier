package com.projects.seriesnotifier;

import com.projects.database.DBAdapter;
import com.projects.seriesnotifier.R;
import com.projects.services.CheckUpdates;
import com.projects.utils.SeriesUtils;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class Notifier extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	boolean notify = false;
    	if (getIntent().getExtras() != null) {
    		Bundle b = getIntent().getExtras();
    		notify = b.getBoolean("notify");
    	}   	
    	
        setContentView(R.layout.main);
        if(!notify) {
        	Intent service = new Intent().setClass(this, CheckUpdates.class);
        	startService(service);
        }
        
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, SearchSeries.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("SearchSeries").setIndicator(getString(R.string.search),
                          res.getDrawable(R.drawable.tab_search))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, OwnSeries.class);
        spec = tabHost.newTabSpec("OwnSeries").setIndicator(getString(R.string.my_series),
                          res.getDrawable(R.drawable.tab_own_series))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, NewEpisodes.class);
        spec = tabHost.newTabSpec("NewEpisodes").setIndicator(getString(R.string.new_episode_title),
                          res.getDrawable(R.drawable.tab_new_episodes))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Settings.class);
        spec = tabHost.newTabSpec("Settings").setIndicator(getString(R.string.settings),
                          res.getDrawable(R.drawable.tab_settings))
                      .setContent(intent);
        tabHost.addTab(spec);
        
       
        if(notify){
        	tabHost.setCurrentTab(2);
        } else {
        	tabHost.setCurrentTab(0);
        }
    }
}