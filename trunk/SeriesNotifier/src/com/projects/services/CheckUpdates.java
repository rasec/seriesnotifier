package com.projects.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.projects.series.Serie;
import com.projects.seriesnotifier.Notifier;
import com.projects.seriesnotifier.R;
import com.projects.utils.SeriesUtils;

public class CheckUpdates extends Service {

	final int NOTIFICATION_ID = 1;
	
	// This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new MyBinder();
    
    List<Serie> seriesNuevas;
	
	@Override
	public void onCreate() {
		
	}
	
	@Override
	public void onStart(Intent intent, int startId){
		handleCommand();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
		
	}
	
	@Override
	public void onDestroy() {
		
	}
	
	private class MyBinder extends Binder {
		
	}
	
	public void handleCommand(){
		new Timer().scheduleAtFixedRate(checkUpdates, 0, 1000*10);
		/*List<Serie> seriesNuevas = checkUpdates();
		if(seriesNuevas != null && seriesNuevas.size() > 0) {
			createNotification("(" + seriesNuevas.size() + ")");
		}*/
	}
	
	private TimerTask checkUpdates = new TimerTask() 
    { 
        public void run()  
        { 
        	showToast("Servicio Iniciado");
            seriesNuevas = SeriesUtils.getUpdatesService(getApplicationContext());
            if(seriesNuevas != null && seriesNuevas.size() > 0) {
    			createNotification("(" + seriesNuevas.size() + ")");
    		}
        } 
    }; 
	
	
	private void showToast(String message){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}
	
	private List<Serie> checkUpdates(){
		List<Serie> seriesNuevas = SeriesUtils.getUpdatesService(getApplicationContext());
		return seriesNuevas;
	}
	
	private void createNotification(CharSequence desc){
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
	    Intent notificationIntent = new Intent(this, Notifier.class);
	    Bundle b = new Bundle();
		b.putBoolean("notify", true);
		notificationIntent.putExtras(b);
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	    
	    note.setLatestEventInfo(context, title, contText, contentIntent);
	    
	    notMan.notify(NOTIFICATION_ID, note);
	}
}
