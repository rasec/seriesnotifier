package com.projects.seriesnotifier;

import java.util.ArrayList;
import java.util.List;

import com.projects.series.Serie;
import com.projects.utils.SeriesUtils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NewEpisodes extends Activity{
	
	final int NOTIFICATION_ID = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.new_episodes);
		
		ListView list = (ListView)findViewById(R.id.new_episode_list);		
		final ArrayList<String> array = new ArrayList<String>();
        
        final ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
	    
        list.setAdapter(aa);
			
		String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager notMan = (NotificationManager) getSystemService(ns);
	    notMan.cancel(NOTIFICATION_ID);
	    
	    List<Serie> series = SeriesUtils.getDBSeriesUpdates(getApplicationContext());
	    
		array.add(0, "Prueba");
		aa.notifyDataSetChanged();
	    
	    /*for (Serie serie : series) {
	    	array.add(0, serie.getName());
			aa.notifyDataSetChanged();
		}*/
	    
	    //SeriesUtils.updateDBSeriesUpdates(getApplicationContext());

	}

}
