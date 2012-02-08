package com.projects.seriesnotifier;

import java.io.InputStream;
import java.net.URL;

import com.projects.series.Serie;
import com.projects.utils.SeriesUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SerieInfo extends Activity {

	private int tipo;
	private Serie serie;
	private ImageView img;
	private int id;
	private ProgressDialog progressDialog;
		
	private Context contexto;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		Bundle b = getIntent().getExtras();
		id = b.getInt("id");
		contexto = this;
		
		tipo = b.getInt("type");
		
		AsyncTask<String, Integer, Boolean> task = new NewSearchTask().execute();
				
		
	}
	
	private Drawable LoadImageFromWebOperations(String url)
	{
	       try
	       {
	           InputStream is = (InputStream) new URL(url).getContent();
	           Drawable d = Drawable.createFromStream(is, "src name");
	           return d;
	        }catch (Exception e) {
	            System.out.println("Exc="+e);
	            return null;
	        }
	}
	
	public OnClickListener serieAction = new OnClickListener() {
		public void onClick(View v) {
			// do something when the button is clicked
			if(serie.isFav()){
				showConfirmDialogDelete(serie);
	        } else {
	        	showConfirmDialog(serie);
	        }
		}
	};
	
	public void showConfirmDialog(Serie serie) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		
		dialog.setMessage(
				getString(R.string.askToAddSerie) + serie.getName())
				.setPositiveButton(getString(R.string.Ok), new CommandAddSerie(serie))
				.setNegativeButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		AlertDialog alert = dialog.create();
		alert.show();
	}
	
	/* Inner class para el control de acciones en el dialog */
	public class CommandAddSerie implements DialogInterface.OnClickListener {
		private Serie serie;

		public CommandAddSerie(Serie serie) {
			this.serie = serie;
		}

		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			addSerie(serie);
			ImageView icon = (ImageView)findViewById(R.id.icon);
			icon.setImageResource(R.drawable.favorite);
			serie.setFav(true);
		}
	}
	
	public void addSerie(Serie serie) {
		String toRet = "";
		//int ret = SeriesUtils.addSerie(SeriesUtils.OWNSERIES, serie.toString(), getApplicationContext());
		int ret = (int) SeriesUtils.addDBSerie(serie.getName(), Integer.parseInt(serie.getId()), getApplicationContext());
		if (ret >= 0)
			toRet = getString(R.string.addSuccess) + serie.getName();
		else if (ret == -1)
			toRet = getString(R.string.addAlreadyExists) + serie.getName();
		else if (ret == -2)
			toRet = getString(R.string.addNotExists) + serie.getName();
		showToast(toRet);
	}
	
	public void showDialog(String message) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(message).setCancelable(false)
				.setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		dialog.show();
	}	
	
	
	public void showConfirmDialogDelete(Serie serie){
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    	dialog.setMessage(getString(R.string.askToDelete) + serie.getName())
    	.setPositiveButton(getString(R.string.Ok), new CommandDeleteSerie(serie))
    	.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.dismiss();
	           }
	       });
    	AlertDialog alert = dialog.create();
    	alert.show();
    }
	
	public class CommandDeleteSerie implements DialogInterface.OnClickListener {
		
		  private Serie serie;
		
		  public CommandDeleteSerie(Serie serie) {
		
		    this.serie = serie;
		
		  }
		  
		  public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			deleteElement(serie);
			ImageView icon = (ImageView)findViewById(R.id.icon);
			icon.setImageResource(R.drawable.star_grey);
			serie.setFav(false);
		  }		
	}
	
	private void deleteElement(Serie serie) {
		String message = "";
		//int ret = SeriesUtils.deleteSerie(SeriesUtils.OWNSERIES, text.toString(), this);
		int ret = (int)SeriesUtils.deleteDBSerie(Integer.parseInt(serie.getId()), getApplicationContext());
				
		if(ret >= 0)
	 		message = getString(R.string.delSerie) + serie.getName();
	 	else if(ret == -1)
	 		message = getString(R.string.delSerieNotExists) + serie.getName();
		showToast(message);
	}
	
	public void showDialogDelete(String message){
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
	
	public void showToast(String message){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}
	
	public void updateView() {
		setContentView(R.layout.serie_info);
		img = (ImageView)findViewById(R.id.banner);
		String bannerUrl = getString(R.string.bannerUrl);
		
		Drawable drawable = LoadImageFromWebOperations(bannerUrl + serie.getImgUrl());
		
        img.setImageDrawable(drawable);
        
        TextView title = (TextView)findViewById(R.id.title);
        TextView rating = (TextView)findViewById(R.id.ratingValue);
        TextView desc = (TextView)findViewById(R.id.desc);
        ImageView icon = (ImageView)findViewById(R.id.icon);
        TextView stat = (TextView)findViewById(R.id.stat);
        
        
        if(serie.isFav()){
        	icon.setImageResource(R.drawable.favorite);
        } else {
        	icon.setImageResource(R.drawable.star_grey);
        }
        
        
        title.setText(serie.getName());
        rating.setText(serie.getRate());
        desc.setText(serie.getDesc());
        stat.setText(serie.getEstado());
        
        icon.setOnClickListener(serieAction);
		
	}
	
	public void showProgressDialog() {
		progressDialog = ProgressDialog.show(contexto, "Progreso", "Obteniendo información de la serie...", true);
	}
	
	public void removeProgressDialog() {
		progressDialog.dismiss();
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updateView();
		}
	};
	
	class NewSearchTask extends AsyncTask<String, Integer, Boolean> {
		  @Override
		  protected Boolean doInBackground(String... params) {
		    try {
		    	serie = SeriesUtils.getSeriesInfo(id, contexto);
		    	handler.sendEmptyMessage(0);
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		    return Boolean.TRUE;   // Return your real result here
		  }
		  @Override
		  protected void onPreExecute() {
		    showProgressDialog();
		  }
		  @Override
		  protected void onPostExecute(Boolean result) {
		    // result is the value returned from doInBackground
		    removeProgressDialog();
		  }
	}

}
