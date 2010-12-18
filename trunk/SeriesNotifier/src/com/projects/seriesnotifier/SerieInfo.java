package com.projects.seriesnotifier;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.projects.series.Serie;
import com.projects.seriesnotifier.NewSearch.CommandAddSerie;
import com.projects.seriesnotifier.OwnSeries.CommandDeleteSerie;
import com.projects.seriesnotifier.OwnSeries.IconListViewAdapterDelete;
import com.projects.utils.SeriesUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SerieInfo extends Activity {

	int tipo;
	Serie serie;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serie_info);
		
		Bundle b = getIntent().getExtras();
		int id = b.getInt("id");
		
		tipo = b.getInt("type");
		
		serie = SeriesUtils.getSeriesInfo(id, getApplicationContext());
		
		ImageView img = (ImageView)findViewById(R.id.banner);
		
		String bannerUrl = getString(R.string.bannerUrl);
		
		Drawable drawable = LoadImageFromWebOperations(bannerUrl + serie.getImgUrl());
		
        img.setImageDrawable(drawable);
        
        TextView title = (TextView)findViewById(R.id.title);
        TextView desc = (TextView)findViewById(R.id.desc);
        ImageView icon = (ImageView)findViewById(R.id.icon);
        
        
        if(tipo == 1)
        {
        	icon.setImageResource(R.drawable.plus);
        }
        
        
        title.setText(serie.getName());
        desc.setText(serie.getDesc());
        
        icon.setOnClickListener(serieAction);
		
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
			if(tipo==1)
			{
				showConfirmDialog(serie.getName(), Integer.parseInt(serie.getId()));
			}else{
				showConfirmDialogDelete(serie);
							
			}
		}
	};
	
	public void showConfirmDialog(String serie, int id) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(
				getString(R.string.askToAddSerie) + serie)
				.setPositiveButton(getString(R.string.Ok), new CommandAddSerie(serie, id))
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
		private CharSequence serie;
		private int id;

		public CommandAddSerie(CharSequence serie, int id) {
			this.serie = serie;
			this.id = id;
		}

		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			addSerie(serie, id);
		}
	}
	
	public void addSerie(CharSequence serie, int id) {
		String toRet = "";
		//int ret = SeriesUtils.addSerie(SeriesUtils.OWNSERIES, serie.toString(), getApplicationContext());
		int ret = (int) SeriesUtils.addDBSerie(serie.toString(), id, getApplicationContext());
		if (ret >= 0)
			toRet = getString(R.string.addSuccess) + serie;
		else if (ret == -1)
			toRet = getString(R.string.addAlreadyExists) + serie;
		else if (ret == -2)
			toRet = getString(R.string.addNotExists) + serie;
		showDialog(toRet);
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
    	.setPositiveButton(getString(R.string.Ok), new CommandDeleteSerie(serie.getName()))
    	.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.dismiss();
	           }
	       });
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
	
	private void deleteElement(CharSequence serie) {
		String message = "";
		//int ret = SeriesUtils.deleteSerie(SeriesUtils.OWNSERIES, text.toString(), this);
		int ret = (int)SeriesUtils.deleteDBSerie(serie.toString(), getApplicationContext());
				
		if(ret >= 0)
	 		message = getString(R.string.delSerie) + serie;
	 	else if(ret == -1)
	 		message = getString(R.string.delSerieNotExists) + serie;
		showDialogDelete(message);
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

}
