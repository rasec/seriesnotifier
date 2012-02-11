package com.projects.seriesnotifier;

import java.util.ArrayList;
import java.util.List;

import com.projects.series.Episode;
import com.projects.seriesnotifier.Recommendations.GetRecommendationsTask;
import com.projects.utils.SeriesUtils;
import com.projects.widgets.NumberPicker;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NewEpisodes extends ListActivity{
	
	final int NOTIFICATION_ID = 1;
	List<Episode> episodes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager notMan = (NotificationManager) getSystemService(ns);
	    notMan.cancel(NOTIFICATION_ID);
	    
	    episodes = SeriesUtils.getDBSeriesUpdates(getApplicationContext());
	    
	    setListAdapter(new NewEpisodesAdapter(this, episodes, R.drawable.tick36));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		episodes = SeriesUtils.getDBSeriesUpdates(getApplicationContext());
		AsyncTask<String, Integer, Boolean> task = new UpdateRateTask().execute();
	}
	
	public void showConfirmDialog(CharSequence episodeName, int id){
		LayoutInflater inflater = getLayoutInflater();
    	View dialoglayout = inflater.inflate(R.layout.value_picker, (ViewGroup) findViewById(R.id.valueId));
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    	dialog
    	//.setMessage(getString(R.string.askToDeleteEpisode) + episodeName)
    	.setPositiveButton(getString(R.string.Ok), new CommandUpdateEpisode(dialoglayout, episodeName, id))
    	.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.dismiss();
	           }
	       });
    	   	
    	dialog.setView(dialoglayout);
    	AlertDialog alert = dialog.create();
    	alert.show();
    }
	
	private void deleteElement(int value, CharSequence name, int id, Context context) {
		String message = "";
		SeriesUtils.rateSerie(id, value, context);
		int ret = (int)SeriesUtils.updateDBSeriesUpdates(getApplicationContext(), id, value);

		List<Episode> episodes = SeriesUtils.getDBSeriesUpdates(getApplicationContext());
		List<String> episodeString = new ArrayList<String>();
		for (Episode e : episodes) {
			episodeString.add(e.getSerieName() + " " + e.getSeason() +"x"+ e.getEpisode());			
		}
		setListAdapter(new NewEpisodesAdapter(this, episodes, R.drawable.tick36));
		
		if(ret >= 0)
	 		message = getString(R.string.delEpisode) + name;
	 	else if(ret == -1)
	 		message = getString(R.string.delSerieNotExists) + name;
	 	showToast(message);
	}
	
	public void showToast(String message){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}
	
	/**
	 * Clase interna que se encarga de hacer de adaptador especial para 
	 * el formato de los listados de series, agregando el layout necesario
	 * y poblando la lista
	 * 
	 * @author César de la Cruz Rueda (cesarcruz85 [at] gmail.com)
	 *
	 */
	private class NewEpisodesAdapter extends BaseAdapter {
		private Context mContext;
		int count;
		int icon;
		private List<Episode> items;

		public NewEpisodesAdapter(Context mContext, List<Episode> items, int icon) {
			this.mContext = mContext;
			this.items = items;
			this.icon = icon;
			this.count = items.size();
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.new_episodes, null);
			}
			String serieName = items.get(position).getSerieName(); 
			String episodeNum = items.get(position).getSeason() + "x" + items.get(position).getEpisode();
			String episodeRate = "Nota: " + (items.get(position).getRate() != 0 ? items.get(position).getRate() : "-");
			String date = items.get(position).getDate();
			String id = items.get(position).getId();

			// poblamos la lista de elementos

			TextView tt = (TextView) v.findViewById(R.id.SerieName);
			TextView tEpisodeNum = (TextView) v.findViewById(R.id.EpisodeNum);
			TextView tEpisodeRate = (TextView) v.findViewById(R.id.EpisodeRate);
			TextView tdate = (TextView) v.findViewById(R.id.Date);
			ImageView im = (ImageView) v.findViewById(R.id.listIcon);

			//TODO: updateDBSeriesUpdates
			//im.setOnClickListener(addSerie);

			if (im != null) {
				im.setImageResource(this.icon);
			}
			
			im.setOnClickListener(updateEpisode);
			
			if (tt != null) {
				tt.setText(serieName);
				tt.setTag(id);
			}
			if(tEpisodeNum!=null) {
				tEpisodeNum.setText(episodeNum);
			}
			if(tEpisodeRate!=null) {
				tEpisodeRate.setText(episodeRate);
			}
			if(tdate!=null) {
				tdate.setText(getString(R.string.date) + " " +  date);
			}

			return v;
		}

		public int getCount() {
			return count;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		public OnClickListener updateEpisode = new OnClickListener() {
			public void onClick(View v) {
				// do something when the button is clicked
				String serieName = ((TextView)((RelativeLayout)((LinearLayout) v.getParent()).getChildAt(1)).getChildAt(0)).getText().toString();
				String episode = ((TextView)((RelativeLayout)((LinearLayout) v.getParent()).getChildAt(1)).getChildAt(1)).getText().toString();
				int id = Integer.parseInt(((String)((TextView)((RelativeLayout)((LinearLayout) v.getParent()).getChildAt(1)).getChildAt(0)).getTag()));
				showConfirmDialog(serieName + " " + episode, id);
			}
		};
	}
	
	public class CommandUpdateEpisode implements DialogInterface.OnClickListener {
		
		  private CharSequence episodeName;
		  private int id;
		  private View v;
		  public CommandUpdateEpisode(View v, CharSequence episodeName, int id) {
			  this.id = id;
			  this.episodeName = episodeName;
			  this.v = v;
		
		  }
		  
		  public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			NumberPicker nP = (NumberPicker)((LinearLayout)v).getChildAt(1);
			int value = nP.getCurrent();
			deleteElement(value, this.episodeName, this.id, getApplicationContext());		
		  }		
	}
	
	class UpdateRateTask extends AsyncTask<String, Integer, Boolean> {
		  @Override
		  protected Boolean doInBackground(String... params) {
		    try {
		    	episodes = SeriesUtils.updateRateSeriesUpdates(episodes, getApplicationContext());
		    	handler.sendEmptyMessage(0);
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		    return Boolean.TRUE;   // Return your real result here
		  }
		  @Override
		  protected void onPreExecute() {
		    //showProgressDialog();
		  }
		  @Override
		  protected void onPostExecute(Boolean result) {
		    // result is the value returned from doInBackground
		    //removeProgressDialog();
		  }
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			setListAdapter(new NewEpisodesAdapter(getApplicationContext(), episodes, R.drawable.tick36));
		}
	};

}
