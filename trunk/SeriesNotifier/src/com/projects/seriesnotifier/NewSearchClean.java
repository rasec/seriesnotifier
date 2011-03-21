package com.projects.seriesnotifier;

import java.util.ArrayList;
import java.util.List;

import com.projects.series.Serie;
import com.projects.seriesnotifier.OwnSeriesClean.IconListAdapter;
import com.projects.utils.SeriesUtils;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.os.Handler;

public class NewSearchClean extends ListActivity {
	
	// Variables Globales
	private static final int MENU_OPEN = Menu.FIRST + 1;
	private static final int MENU_ADD = Menu.FIRST + 2;
	static final int NEW_SERIE_DIALOG = 0;
	
	private String query;
	
	private ProgressDialog progressDialog;
	
	private Context contexto;
	List<Serie> series;
	List<String> seriesString;
	ListView lv;
	private IconListAdapter la;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Se obtiene el par�metro pasado
		Bundle b = getIntent().getExtras();
		String q = b.getCharSequence("q").toString();
		query = q;
		// Se obtiene el listado de series en base al par�metro
		//getSeries(q);
		contexto = this;
		AsyncTask<String, Integer, Boolean> task = new NewSearchTask().execute();
		// dialog.dismiss();

		// En caso de no haber series disponibles se puede
		// a�adir la serie indicada, para lo que se crea un
		// listener cuando se pulsa el bot�n a�adir
		//Button button = (Button) findViewById(R.id.ok_new);
		//button.setOnClickListener(setNewSerie);

		//EditText editview = (EditText) findViewById(R.id.entry_new);
		//editview.setText(q);
	}

	public void getSeries(String query) {
		series = SeriesUtils.getSeriesByQuery(
				getApplicationContext(), query);
		seriesString = new ArrayList<String>();
		for (Serie serie : series) {
			seriesString.add(serie.getName());
		}
	}

	public void addSerie(int id, CharSequence serie) {
		String toRet = "";
		// int ret = SeriesUtils.addSerie(SeriesUtils.OWNSERIES,
		// serie.toString(), getApplicationContext());
		int ret = (int) SeriesUtils.addDBSerie(serie.toString(), id,
				getApplicationContext());
		if (ret >= 0)
			toRet = getString(R.string.addSuccess) + serie;
		else if (ret == -1)
			toRet = getString(R.string.addAlreadyExists) + serie;
		else if (ret == -2)
			toRet = getString(R.string.addNotExists) + serie;
		showToast(toRet);
	}

	public void showConfirmDialog(String serie, int id) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(getString(R.string.askToAddSerie) + serie)
				.setPositiveButton(getString(R.string.Ok),
						new CommandAddSerie(serie, id)).setNegativeButton(
						getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		AlertDialog alert = dialog.create();
		alert.show();
	}

//	private void showOptionsDialog(CharSequence serie, int id) {
//		CharSequence[] options = { getString(R.string.add),
//				getString(R.string.open) };
//		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//		dialog.setItems(options, new CommandLongClick(serie, id));
//		AlertDialog alert = dialog.create();
//		alert.show();
//	}

//	private class CommandLongClick implements DialogInterface.OnClickListener {
//
//		private CharSequence serie;
//		private int id;
//
//		public CommandLongClick(CharSequence serie, int id) {
//
//			this.serie = serie;
//			this.id = id;
//
//		}
//
//		public void onClick(DialogInterface dialog, int which) {
//			dialog.dismiss();
//			switch (which) {
//			case 0:
//				addSerie(id, serie);
//				break;
//			case 1:
//				openSerieInfo(id);
//				break;
//			default:
//				break;
//			}
//
//		}
//	}

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
			addSerie(id, serie);
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
		dialog.setMessage(message).setCancelable(false).setPositiveButton(
				getString(R.string.Ok), new DialogInterface.OnClickListener() {
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

	public void openSerieInfo(int id) {
		Intent intent = new Intent().setClass(getApplicationContext(),
				SerieInfo.class);
		Bundle b = new Bundle();
		b.putInt("id", id);
		b.putInt("type", 1);
		intent.putExtras(b);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplicationContext().startActivity(intent);
	}

	public class IconListViewAdapterAdd extends ArrayAdapter<String> {

		private List<Serie> items;
		private int icon;
		private Context context;

		public IconListViewAdapterAdd(Context context, int textViewResourceId,
				List<String> itemsString, List<Serie> items, int icon) {
			super(context, textViewResourceId, itemsString);
			this.items = items;
			this.icon = icon;
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.list_item_icon, null);
			}
			String text = items.get(position).getName();
			String id = items.get(position).getId();

			// poblamos la lista de elementos

			TextView tt = (TextView) v.findViewById(R.id.listText);
			ImageView im = (ImageView) v.findViewById(R.id.listIcon);

			im.setOnClickListener(addSerie);

			if (im != null) {
				im.setImageResource(this.icon);
			}
			if (tt != null) {
				tt.setText(text);
				tt.setTag(id);
			}

			return v;
		}

		public OnClickListener addSerie = new OnClickListener() {
			public void onClick(View v) {
				// do something when the button is clicked

				// String message = "";
				String serie = ((TextView) ((RelativeLayout) v.getParent())
						.getChildAt(0)).getText().toString();
				int id = Integer
						.parseInt((String) ((TextView) ((RelativeLayout) v
								.getParent()).getChildAt(0)).getTag());

				showConfirmDialog(serie, id);

			}
		};
	}
	
	public void showProgressDialog() {
		progressDialog = ProgressDialog.show(contexto, "Progreso", "Buscando...", true);
	}
	
	public void removeProgressDialog() {
		progressDialog.dismiss();
	}
	
	public void updateView(){
		setContentView(R.layout.list_serie_search);
		if (!series.isEmpty()) {
			setListAdapter(new IconListViewAdapterAdd(getApplicationContext(),
					R.layout.list_item_icon, seriesString, series,
					R.drawable.add));
			lv = getListView();
			lv.setTextFilterEnabled(true);
			registerForContextMenu(lv);
	
			lv.setOnItemClickListener(serieClick);
			
		}
	}
	
	/*
	 * Método que se ejecuta como menu contextual al realizar una
	 * pulsación larga sobre alguno de las series de la lista
	 * Muestra dos elementos: ver detalle y eliminar 
	 */
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, MENU_OPEN, 0, getString(R.string.open));
		menu.add(0, MENU_ADD, 0, getString(R.string.add));

	}

	/*
	 * Método que se ejecuta al pulsar sobre alguno de los elementos
	 * del menú contextual previamente creado.
	 * En base al elemento que se pulse, se realiza una acción
	 * u otra: ver info de la serie o eliminar serie.
	 */
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int pos = info.position;

		RelativeLayout rl = (RelativeLayout) info.targetView;
		int serieId = Integer.parseInt((String) rl.getChildAt(0).getTag());

		CharSequence serie = (CharSequence) (series.get(pos).getName());

		switch (item.getItemId()) {
		case MENU_OPEN:
			openSerieInfo(serieId);
			break;
		case MENU_ADD:
			addSerie(serieId, serie);
			break;
		default:
			break;
		}

		lv.setAdapter(la);

		return true;
	}
	
	private OnItemClickListener serieClick = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// Navegamos
			openSerieInfo(Integer
					.parseInt((String) (((TextView) ((RelativeLayout) view)
							.getChildAt(0)).getTag())));
		}
	};
	
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
		    	getSeries(query);
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