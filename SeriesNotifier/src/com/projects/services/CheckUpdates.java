package com.projects.services;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.projects.series.Episode;
import com.projects.seriesnotifier.Notifier;
import com.projects.seriesnotifier.R;
import com.projects.utils.SeriesUtils;

public class CheckUpdates extends Service {

	final int NOTIFICATION_ID = 1;
	final long DAY_MILI = 1000*86400;
	Context context;
	Timer timer = new Timer();
	boolean serviceActive = false;
	OnSharedPreferenceChangeListener listener;
	int days;
	int hour;
	long diference;
	SharedPreferences checkForUpdatesFrecuence; 
	SharedPreferences checkForUpdatesHour;
	
	MyTimerTask checkUpdates;
	
	
	// This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new MyBinder();
    
    List<Episode> episodiosNuevos;
	
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
		Toast.makeText(this, "Paramos el servicio", Toast.LENGTH_SHORT).show();

	}
	
	private class MyBinder extends Binder {
		
	}
	
	public void handleCommand(){
		// Comprobamos cada día
		
		context = getApplicationContext();
		checkForUpdatesFrecuence = context.getSharedPreferences("checkForUpdatesFrecuence", Activity.MODE_PRIVATE);
		checkForUpdatesHour = context.getSharedPreferences("checkForUpdatesHour", Activity.MODE_PRIVATE);
		boolean changed = preferencesChanged();
		
		if(!serviceActive || changed) {
			getPreferencesData();
			int horas = (int) ((diference/1000)/60/60);
			System.out.println("Horas hasta notificacion: " + horas );
			if(this.checkUpdates != null) {
				this.checkUpdates.cancel();
			}
			this.checkUpdates = new MyTimerTask();
			timer.scheduleAtFixedRate(checkUpdates, diference, DAY_MILI*days);
			//timer.scheduleAtFixedRate(checkUpdates, 1000*60, 1000*60*10);
			serviceActive = true;
		} else {
			System.out.println("servicio activo");
		}
	}
	
	private boolean preferencesChanged()
	{
		boolean changed = false;
		int days, hour;
		
		days = new Integer( checkForUpdatesFrecuence.getInt("checkForUpdatesFrecuence", 1) );
		hour = new Integer( checkForUpdatesHour.getInt("checkForUpdatesHour", 20) );
		
		changed = (this.days != days || this.hour != hour ) ?	true : false;
		
		return changed;
	}
	
	private void getPreferencesData() {
		days = new Integer( checkForUpdatesFrecuence.getInt("checkForUpdatesFrecuence", 1) );
		hour = new Integer( checkForUpdatesHour.getInt("checkForUpdatesHour", 20) );
		Date now = new Date();
		DiferentTimes dT = new DiferentTimes(now, hour);
		diference = dT.getDiference();
	}
	
	private class MyTimerTask extends TimerTask
    { 
        public void run()  
        { 
        	episodiosNuevos = SeriesUtils.getUpdatesService(getApplicationContext());
            if(episodiosNuevos != null && episodiosNuevos.size() > 0) {
    			createNotification("(" + episodiosNuevos.size() + ")");
    		} else {
    			createNotification("No hay capítulos nuevos");
    		}
        } 
    }
    	
	private void createNotification(CharSequence desc){
		System.out.println("Creamos la notificacion: " + desc);
		// Create the Notification Manager in the Notification Context
	    String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager notMan = (NotificationManager) getSystemService(ns);
	    
	    // Create the Notification with an icon, a text and a time
	    int icon = R.drawable.logo3;
	    CharSequence text =  desc + " " + getString(R.string.newEpisode);
	    long when = System.currentTimeMillis();
	    Notification note = new Notification(icon, text, when);
	    
	    note.ledARGB = 0xff00ff00;
	    note.ledOnMS = 300;
	    note.ledOffMS = 1000;
	    note.flags |= Notification.FLAG_SHOW_LIGHTS;
	    
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
	
	public class DiferentTimes {
		Date time1;
		Date time2;
		
		DiferentTimes(Date time, int hour) {
			time1 = time;
			time2 = new Date();
			if(time1.getHours() >= hour) {
				time2.setDate(time1.getDate()+1);
			} else {
				time2.setDate(time1.getDate());
			}
			time2.setYear(time1.getYear());
			time2.setMonth(time1.getMonth());
			time2.setHours(hour);
			time2.setMinutes(0);
			time2.setSeconds(0);
		}
		
		public long getDiference() {
			long ret = 0;
			if(time1.getTime() > time2.getTime()) {
				ret = time1.getTime() - time2.getTime();
			} else {
				ret = time2.getTime() - time1.getTime();
			}
			return ret;
		}
		
	}
}

