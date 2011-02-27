package com.projects.seriesnotifier;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class NewEpisode extends Activity{
	
	final int NOTIFICATION_ID = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView text = new TextView(this);
		text.setText(R.string.newEpisdeProv);
		
		String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager notMan = (NotificationManager) getSystemService(ns);
	    notMan.cancel(NOTIFICATION_ID);
		
		setContentView(text);
	}

}
