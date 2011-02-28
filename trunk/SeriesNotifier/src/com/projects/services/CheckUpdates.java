package com.projects.services;

import java.util.List;

import com.projects.series.Serie;
import com.projects.seriesnotifier.NewEpisodes;
import com.projects.seriesnotifier.R;
import com.projects.utils.SeriesUtils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class CheckUpdates extends IntentService {

	final int NOTIFICATION_ID = 1;
	
	public CheckUpdates() {
		super("Nombre sin sentido");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onHandleIntent(Intent intent) {
		handleCommand(intent);
	}
	
	public void handleCommand(Intent intent){
		//showToast("Servicio Iniciado");
		List<Serie> seriesNuevas = checkUpdates();
		if(seriesNuevas != null) {
			createNotification("(" + seriesNuevas.size() + ")");
		}
	}
	
	public void showToast(String message){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}
	
	public List<Serie> checkUpdates(){
		List<Serie> seriesNuevas = SeriesUtils.getUpdatesTvDBList(getApplicationContext());
		return seriesNuevas;
	}
	
	public void createNotification(CharSequence desc){
		// Create the Notification Manager in the Notification Context
	    String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager notMan = (NotificationManager) getSystemService(ns);
	    
	    // Create the Notification with an icon, a text and a time
	    int icon = R.drawable.logo3;
	    CharSequence text =  desc + " " + getString(R.string.newEpisode);
	    long when = System.currentTimeMillis();
	    Notification note = new Notification(icon, text, when);
	    //note.defaults |= Notification.DEFAULT_VIBRATE;
	    
	    // Create the expanded message and the Intent
	    Context context = getApplicationContext();
	    CharSequence title = desc + " " + getString(R.string.newEpisode);
	    CharSequence contText = getString(R.string.newEpisodeAdvise);
	    Intent notificationIntent = new Intent(this, NewEpisodes.class);
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	    
	    note.setLatestEventInfo(context, title, contText, contentIntent);
	    
	    notMan.notify(NOTIFICATION_ID, note);
	}
}
