package com.projects.seriesnotifier;

import java.util.EventObject;
import java.util.List;

import com.projects.adapters.IconListViewAdapter;
import com.projects.seriesnotifier.OwnSeries.CommandLongClick;
import com.projects.utils.SeriesUtils;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class NewSearch extends ListActivity {

	static final int NEW_SERIE_DIALOG = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listseriesearch);
		
		// Se obtiene el par�metro pasado
		Bundle b = getIntent().getExtras();
		String q = b.getCharSequence("q").toString();
		// Se obtiene el listado de series en base al par�metro
		getSeries(q);
		//dialog.dismiss();

		// En caso de no haber series disponibles se puede
		// a�adir la serie indicada, para lo que se crea un
		// listener cuando se pulsa el bot�n a�adir
		Button button = (Button) findViewById(R.id.ok_new);
		button.setOnClickListener(setNewSerie);

		EditText editview = (EditText) findViewById(R.id.entry_new);
		editview.setText(q);
	}

	public void getSeries(String query) {
		List<String> series = SeriesUtils.getSeriesByQuery(getApplicationContext(),
				query);
		if (!series.isEmpty()) {
			setListAdapter(new IconListViewAdapterAdd(this, R.layout.list_item_icon, series, R.drawable.plus));
			ListView lv = getListView();
			lv.setTextFilterEnabled(true);
			
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// Navegamos					
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

	public void addSerie(CharSequence serie) {
		String toRet = "";
		//int ret = SeriesUtils.addSerie(SeriesUtils.OWNSERIES, serie.toString(), getApplicationContext());
		int ret = (int) SeriesUtils.addDBSerie(serie.toString(), getApplicationContext());
		if (ret >= 0)
			toRet = getString(R.string.addSuccess) + serie;
		else if (ret == -1)
			toRet = getString(R.string.addAlreadyExists) + serie;
		else if (ret == -2)
			toRet = getString(R.string.addNotExists) + serie;
		showDialog(toRet);
	}

	public void showConfirmDialog(String serie) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(
				getString(R.string.askToAddSerie) + serie)
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
	
	public void showOptionsDialog(CharSequence serie){
		CharSequence[] options = {getString(R.string.add), getString(R.string.open)};
    	AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    	dialog.setItems(options, new CommandLongClick(serie));
    	AlertDialog alert = dialog.create();
    	alert.show();
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
				addSerie(serie);
				break;
			case 1:
				//showConfirmDialog(serie);
				break;
			default:
				break;
			}
					
		  }
	}
	

	/* Inner class para el control de acciones en el dialog */
	public class CommandAddSerie implements DialogInterface.OnClickListener {
		private CharSequence serie;

		public CommandAddSerie(CharSequence serie) {
			this.serie = serie;
		}

		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			addSerie(serie);
		}
	}
	
	/* METODOS PARA CUANDO NO HAY SERIES */
	public OnClickListener setNewSerie = new OnClickListener() {
		public void onClick(View v) {
			// do something when the button is clicked
			String message = "";
			EditText edittext = (EditText) findViewById(R.id.entry_new);
			String serie = edittext.getText().toString();
			int ret = SeriesUtils.addSerie(SeriesUtils.SERIES, serie,
					getApplicationContext());
			// SeriesUtils.addSerie(SeriesUtils.OWNSERIES, serie,
			// getApplicationContext());

			if (ret >= 0)
				message = getString(R.string.addSuccess) + serie;
			else
				message = getString(R.string.addAlreadyExists) + serie;
			showDialog(message);

		}
	};
	
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
	
	public class IconListViewAdapterAdd extends ArrayAdapter<String>{
		
		private List<String> items;
	    private int icon;
	    private Context context;
	    
	    public IconListViewAdapterAdd(Context context, int textViewResourceId, List<String> items, int icon) {
	            super(context, textViewResourceId, items);
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
	        String text = items.get(position);
	        	
	        //poblamos la lista de elementos
	        	
	        TextView tt = (TextView) v.findViewById(R.id.listText);
	        ImageView im = (ImageView) v.findViewById(R.id.listIcon);
	        
	        im.setOnClickListener(addSerie);
	        
	        if (im!= null) {
	        	im.setImageResource(this.icon);
	        }                        
	        if (tt != null) {             
	            tt.setText(text);                             
	        }    	                    	                        
	        
	        return v;
	   }
		
					
		public OnClickListener addSerie = new OnClickListener() {
			public void onClick(View v) {
				// do something when the button is clicked
				v.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						//String message = "";
						String serie = ((TextView)((RelativeLayout) v.getParent()).getChildAt(0)).getText().toString();

						showConfirmDialog(serie);
						
					}
				});
				//System.out.println("Salto: " +  ((TextView)((RelativeLayout)v.getParent()).getChildAt(0)).getText());

			}
		};
	
	}
}