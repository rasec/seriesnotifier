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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.series.Serie;
import com.projects.utils.*;

public class OwnSeries extends ListActivity {
	static int i = 0;
	final int NOTIFICATION_ID = 1;
			
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
	
    public void startActivity(int id){
    	Intent intent = new Intent().setClass(getApplicationContext(), SerieInfo.class);
    	Bundle b = new Bundle();
		b.putInt("id", id);
		b.putInt("type", 0);
		intent.putExtras(b);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	getApplicationContext().startActivity(intent);    
    }
    public void getSeries() {
    	//String[] series = SeriesUtils.getOwnSeries(getApplicationContext());
    	List<Serie> series = SeriesUtils.getDBSeries(getApplicationContext());
    	List<String> seriesString = new ArrayList<String>();
		for (Serie serie : series) {
			seriesString.add(serie.getName());			
		}
        if(series != null){
        	setListAdapter(new IconListViewAdapterDelete(this, R.layout.list_item_icon,seriesString, series, R.drawable.remove));
        	
        
	        ListView lv = getListView();
	        lv.setTextFilterEnabled(true);
	
	        lv.setOnItemClickListener(new OnItemClickListener() {
	          public void onItemClick(AdapterView<?> parent, View view,
	              int position, long id) {
		            // Navegar a la página de info de la serie
		            //showConfirmDialog(((TextView)((RelativeLayout) view).getChildAt(0)).getText());
	        	  
	        	  startActivity( Integer.parseInt(((String)(((TextView)((RelativeLayout) view).getChildAt(0))).getTag()) ));
	        	 
	            }
	        });
	        
	        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
	        	public boolean onItemLongClick(AdapterView<?> parent, View view,
		  	              int position, long id) {
	        			showOptionsDialog((((TextView)((RelativeLayout) view).getChildAt(0)).getText()));
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
	    CharSequence text = getString(R.string.newEpisode) + serie;
	    long when = System.currentTimeMillis();
	    Notification note = new Notification(icon, text, when);
	    //note.defaults |= Notification.DEFAULT_VIBRATE;
	    
	    // Create the expanded message and the Intent
	    Context context = getApplicationContext();
	    CharSequence title = getString(R.string.newEpisode) + serie;
	    CharSequence contText = getString(R.string.newEpisodeAdvise) + serie;
	    Intent notificationIntent = new Intent(this, NewEpisodes.class);
	    PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
	    
	    note.setLatestEventInfo(context, title, contText, contentIntent);
	    
	    note.defaults |= Notification.DEFAULT_LIGHTS;
	   
	    notMan.notify(NOTIFICATION_ID, note);
	}
	
	private void deleteElement(CharSequence text) {
		String message = "";
		String serie = text.toString();
		//int ret = SeriesUtils.deleteSerie(SeriesUtils.OWNSERIES, text.toString(), this);
		int ret = (int)SeriesUtils.deleteDBSerie(serie, getApplicationContext());
		//String[] series = SeriesUtils.getOwnSeries(getApplicationContext());
		List<Serie> series = SeriesUtils.getDBSeries(getApplicationContext());
		List<String> seriesString = new ArrayList<String>();
		for (Serie s : series) {
			seriesString.add(s.getName());			
		}

		if(series != null){
			setListAdapter(new IconListViewAdapterDelete(this, R.layout.list_item_icon, seriesString, series, R.drawable.remove));
		}else{
			List<String> empty = new ArrayList<String>();
			setListAdapter(new IconListViewAdapterDelete(this, R.layout.list_item_icon, empty, series, R.drawable.remove));
		}
		if(ret >= 0)
	 		message = getString(R.string.delSerie) + serie;
	 	else if(ret == -1)
	 		message = getString(R.string.delSerieNotExists) + serie;
	 	showToast(message);
	}
	
	
	public void showDialog(String message){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(message)
		.setCancelable(false)
		.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int id) {
		       dialog.cancel();
		  }
		});
		dialog.show();
	}
	
	public void showConfirmDialog(CharSequence serie){
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    	dialog.setMessage(getString(R.string.askToDelete) + serie)
    	.setPositiveButton(getString(R.string.Ok), new CommandDeleteSerie(serie))
    	.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.dismiss();
	           }
	       });
    	AlertDialog alert = dialog.create();
    	alert.show();
    }
	
	public void showOptionsDialog(CharSequence serie){
		CharSequence[] options = {getString(R.string.notify), getString(R.string.delete)};
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
	
	public void showToast(String message){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}
	
public class IconListViewAdapterDelete extends ArrayAdapter<String>{
		
	private List<Serie> items;
	    private int icon;
	    private Context context;
	    
	    public IconListViewAdapterDelete(Context context, int textViewResourceId, List<String> itemsString, List<Serie> items, int icon) {
	            super(context, textViewResourceId, itemsString);
	            this.items = items;
	            this.icon = icon;
	            this.context = context;
	    }
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View v = convertView;
	        if (v == null) {
	            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.list_item_icon, null);
	        }
	        String text = items.get(position).getName();
	        String id = items.get(position).getId();
	        	
	        //poblamos la lista de elementos
	        	
	        TextView tt = (TextView) v.findViewById(R.id.listText);
	        ImageView im = (ImageView) v.findViewById(R.id.listIcon);
	        
	        im.setOnClickListener(deleteSerie);
	        
	        if (im!= null) {
	        	im.setImageResource(this.icon);
	        }                        
	        if (tt != null) {             
	            tt.setText(text);        
	            tt.setTag(id);
	        }    	                    	                        
	        
	        return v;
	   }
		
					
		public OnClickListener deleteSerie = new OnClickListener() {
			public void onClick(View v) {
				// do something when the button is clicked
				String serie = ((TextView)((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();
				showConfirmDialog(serie);
			}
		};
	
	}
}
