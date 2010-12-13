package com.projects.seriesnotifier;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.projects.utils.*;

public class OwnSeries extends ListActivity {
	static int i = 0;
			
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSeries();        
    }   
	
    @Override
	public void onResume() {
		super.onResume();
		getSeries();
    }
	
    
    public void getSeries() {
    	//String[] series = SeriesUtils.getOwnSeries(getApplicationContext());
    	String[] series = SeriesUtils.getDBSeries(getApplicationContext());
        if(series != null){
        	setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, series));
        
	        ListView lv = getListView();
	        lv.setTextFilterEnabled(true);
	
	        lv.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view,
	              int position, long id) {
	            // When clicked, show a toast with the TextView text
	        	  //createNotification(((TextView) view).getText());
	            //deleteElement(((TextView) view).getText());
	            showConfirmDialog(((TextView) view).getText());
	            }
	        });
	        
	        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
	        	public boolean onItemLongClick(AdapterView<?> parent, View view,
		  	              int position, long id) {
	        			showOptionsDialog(((TextView) view).getText());
		        		//createNotification(((TextView) view).getText());
		        		return true;
		        	}
	        });
        }
    }
    
	public void createNotification(CharSequence serie){
		// Create the Notification Manager in the Notification Context
	    String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager notMan = (NotificationManager) getSystemService(ns);
	    
	    // Create the Notification with an icon, a text and a time
	    int icon = R.drawable.icon;
	    CharSequence text = "Nuevo episodio de " + serie;
	    long when = System.currentTimeMillis();
	    Notification note = new Notification(icon, text, when);
	    //note.defaults |= Notification.DEFAULT_VIBRATE;
	    
	    // Create the expanded message and the Intent
	    Context context = getApplicationContext();
	    CharSequence title = "Nuevo episodio de " + serie;
	    CharSequence contText = "Existe un nuevo capítulo de la serie " + serie + " a punto de emitirse";
	    Intent notificationIntent = new Intent(this, NewEpisode.class);
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	    
	    note.setLatestEventInfo(context, title, contText, contentIntent);
	    
	    final int HELLO_ID = 1;
	    notMan.notify(HELLO_ID, note);
	}
	
	private void deleteElement(CharSequence text) {
		String message = "";
		String serie = text.toString();
		//int ret = SeriesUtils.deleteSerie(SeriesUtils.OWNSERIES, text.toString(), this);
		int ret = (int)SeriesUtils.deleteDBSerie(serie, getApplicationContext());
		//String[] series = SeriesUtils.getOwnSeries(getApplicationContext());
		String[] series = SeriesUtils.getDBSeries(getApplicationContext());
		if(series != null){
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, series));
		}else{
			List<String> empty = new ArrayList<String>();
			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, empty));
		}
		if(ret >= 0)
	 		message = "Serie " + serie + " Eliminada correctamente";
	 	else if(ret == -1)
	 		message = "No se ha elimindo la serie " + serie +", ya que no existia";
	 	showDialog(message);
	}
	
	
	public void showDialog(String message){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(message)
		.setCancelable(false)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int id) {
		       dialog.cancel();
		  }
		});
		dialog.show();
	}
	
	public void showConfirmDialog(CharSequence serie){
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    	dialog.setMessage("¿Desea eliminar la serie '" + serie + "' de su lista de series?")
    	.setPositiveButton("OK", new CommandDeleteSerie(serie))
    	.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.dismiss();
	           }
	       });
    	AlertDialog alert = dialog.create();
    	alert.show();
    }
	
	public void showOptionsDialog(CharSequence serie){
		CharSequence[] options = {"Notificar", "Eliminar"};
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    	dialog.setItems(options, new CommandLongClick(serie));
    	AlertDialog alert = dialog.create();
    	alert.show();
    }
	
	public class CommandDeleteSerie implements DialogInterface.OnClickListener {
		
		  private CharSequence serie;
		
		  public CommandDeleteSerie(CharSequence serie) {
		
		    this.serie = serie;
		
		  }
		  
		  public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			deleteElement(serie);		
		  }		
	}
	
	public class CommandLongClick implements DialogInterface.OnClickListener {
		
		  private CharSequence serie;
		
		  public CommandLongClick(CharSequence serie) {
		
		    this.serie = serie;
		
		  }
		  
		  public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			switch (which) {
			case 0:
				createNotification(serie);
				break;
			case 1:
				deleteElement(serie);
				//showConfirmDialog(serie);
				break;
			default:
				break;
			}
					
		  }
	}
	
}
