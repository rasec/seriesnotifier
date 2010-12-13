package com.projects.seriesnotifier;

import com.projects.database.DBAdapter;
import com.projects.seriesnotifier.R;
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
        setContentView(R.layout.main);
        //TODO: delete this sentence
        //deleteFile(SeriesUtils.OWNSERIES);        
        //deleteFile(SeriesUtils.SERIES);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, SearchSeries.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("SearchSeries").setIndicator("Buscar",
                          res.getDrawable(R.drawable.arrow_up_selected))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, OwnSeries.class);
        spec = tabHost.newTabSpec("OwnSeries").setIndicator("Mis Series",
                          res.getDrawable(R.drawable.copy_unselected))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Settings.class);
        spec = tabHost.newTabSpec("Settings").setIndicator("Opciones",
                          res.getDrawable(R.drawable.options_unselected))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
}