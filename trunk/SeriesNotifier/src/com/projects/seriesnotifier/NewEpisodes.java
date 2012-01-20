package com.projects.seriesnotifier;

import java.util.ArrayList;
import java.util.List;

import com.projects.series.Episode;
import com.projects.series.Serie;
import com.projects.utils.SeriesUtils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NewEpisodes extends Activity{
	
	final int NOTIFICATION_ID = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		setContentView(R.layout.new_episodes);
		
		ListView list = (ListView)findViewById(R.id.new_episode_list);		
		final ArrayList<String> array = new ArrayList<String>();
        
        final ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.list_item, array);
	    
        list.setAdapter(aa);
			
		String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager notMan = (NotificationManager) getSystemService(ns);
	    notMan.cancel(NOTIFICATION_ID);
	    
	    List<Episode> episodes = SeriesUtils.getDBSeriesUpdates(getApplicationContext());
	    
		for (Episode episode : episodes) {
			System.out.println("Id episodio" + episode.getId());
	    	array.add(0, episode.getSerieName() + " " + episode.getSeason() + "x" + episode.getEpisode() + "\n " + episode.getDate());
			aa.notifyDataSetChanged();
		}
	    
	    //SeriesUtils.updateDBSeriesUpdates(getApplicationContext());

	}

}
