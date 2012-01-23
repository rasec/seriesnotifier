package com.projects.seriesnotifier;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.projects.series.Serie;
import com.projects.utils.SeriesUtils;

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
		
		// Se obtiene el parametro pasado
		Bundle b = getIntent().getExtras();
		String q = b.getCharSequence("q").toString();
		query = q;
		// Se obtiene el listado de series en base al parametro
		contexto = this;
		AsyncTask<String, Integer, Boolean> task = new NewSearchTask().execute();
		
	}

	/**
	 * Método que se encarga de obtener el listado de series
	 * coincidente con la busqueda realizada
	 * 
	 * @param query, nombre a buscar para los resultados
	 */

	public void getSeries(String query) {
		series = SeriesUtils.getSeriesByQuery(getApplicationContext(), query);
		seriesString = new ArrayList<String>();
		for (Serie serie : series) {
			seriesString.add(serie.getName());
		}
	}

	/**
	 * Método que se encarga de añadir la serie elegida a la
	 * lista de series del usuario, guardandola en la BD
	 * 
	 * @param id, identificador de la serie
	 * @param serie, nombre de la serie
	 */
	public void addSerie(int id, CharSequence serie) {
		String toRet = "";
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
	
	/**
	 * Método que se encarga de abrir la información detallada
	 * de la serie elegida por el usuario
	 * 
	 * @param id, identificador de la serie a mostrar su info
	 */

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

	/**
	 * Método que muetra el dialog de confirmación para las
	 * acciones lanzadas del el propio listado
	 * 
	 * @param id, identificador para realizar las acciones
	 * @param serie, nombre de la serie para mostrar la info
	 * 
	 */
	public void showConfirmDialog(int id, String serie) {
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

	/*
	 * Método que se encarga de mostrar el mensaje de confirmación
	 * al realizar las acciones sobre las series.
	 */
	public void showToast(String message){
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}
	
	/*
	 * Método que se encarga de mostrar el dialogo de progreso
	 */
	public void showProgressDialog() {
		progressDialog = ProgressDialog.show(contexto,  getString(R.string.progress), getString(R.string.searching), true);
	}
	
	/*
	 * Método que se encarga de cerrar el dialogo de progreso
	 */
	public void removeProgressDialog() {
		progressDialog.dismiss();
	}
	
	/*
	 * Método que actualiza la vista, poblando el listado
	 * con las series que se encuentran
	 */
	public void updateView(){
		setContentView(R.layout.list_serie_search);
		if (!series.isEmpty()) {
			setListAdapter(new IconListAdapter(getApplicationContext(),
					series,	R.drawable.star_grey));
			lv = getListView();
			lv.setTextFilterEnabled(true);
			registerForContextMenu(lv);
	
			lv.setOnItemClickListener(serieClick);
			
		} else {
			Button bt = (Button)findViewById(R.id.ok_new);
			bt.setOnClickListener(setNewSerie);
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
		menu.add(0, MENU_ADD, 1, getString(R.string.add));

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
		
		return true;
	}
	
	
	/* METODOS PARA CUANDO NO HAY SERIES */
	private OnClickListener setNewSerie = new OnClickListener() {
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
	
	private void showDialog(String message) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(message).setCancelable(false).setPositiveButton(
				getString(R.string.Ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		dialog.show();
	}
	/* FIN METODOS PARA CUANDO NO HAY SERIES */
	
	
	/*
	 * Manejador del click sobre un elemento del listado que se encarga
	 * de llamar a la función que abre la información sobre la serie.
	 */
	private OnItemClickListener serieClick = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// Navegamos
			openSerieInfo(Integer
					.parseInt((String) (((TextView) ((RelativeLayout) view)
							.getChildAt(0)).getTag())));
		}
	};
	
	private OnClickListener addSerie = new OnClickListener() {
		public void onClick(View v) {
			
			String serie = ((TextView) ((RelativeLayout) v.getParent())
					.getChildAt(0)).getText().toString();
			int id = Integer
					.parseInt((String) ((TextView) ((RelativeLayout) v
							.getParent()).getChildAt(0)).getTag());

			showConfirmDialog(id, serie);

		}
	};
	
	/*
	 * Manejador para los cambios realizados en el listado
	 * que se encarga de llamar al actualizador de la vista
	 */	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			updateView();
		}
	};	
	
	
	/* Inner class para el control de acciones en el dialog */
	private class CommandAddSerie implements DialogInterface.OnClickListener {
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
	
	/**
	 * Clase interna que se encarga de hacer de adaptador especial para 
	 * el formato de los listados de series, agregando el layout necesario
	 * y poblando la lista
	 * 
	 * @author César de la Cruz Rueda (cesarcruz85 [at] gmail.com)
	 *
	 */
	private class IconListAdapter extends BaseAdapter {
		private Context mContext;
		int count;
		int icon;
		private List<Serie> items;

		public IconListAdapter(Context mContext, List<Serie> items, int icon) {
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
	}
	
	/*
	 * Tarea asíncrona para la obtención de las series en segundo plano mientras se
	 * lanza el progreso.
	 */
	private class NewSearchTask extends AsyncTask<String, Integer, Boolean> {
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