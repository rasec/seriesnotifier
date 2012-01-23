package com.projects.seriesnotifier;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.projects.series.Serie;
import com.projects.utils.SeriesUtils;

/**
 * Clase que representa la lista de Series Elegidas por el
 * usuario. Permite realizar acciones sobre cada una de las
 * series, como ver el detalle o eliminarla de la lista
 * de series guardadas
 * 
 * @author César de la Cruz Rueda (cesarcruz85 [at] gmail.com)
 *
 */
public class OwnSeriesClean extends ListActivity {

	// Variables Globales
	private static final int MENU_OPEN = Menu.FIRST + 1;
	private static final int MENU_DELETE = Menu.FIRST + 2;
	private ListView lv;
	private IconListAdapter la;
	private List<Serie> series;

	/*
	 * Método que se ejuta al crear la actividad representada por la clase
	 * Se encarga de obtener el listado de series del usuario.
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSeries();
	}

	/*
	 * Método que se ejuta al reiniciar la actividad representada por la clase
	 * Se encarga de obtener el listado de series del usuario.
	 * 
	 */
	@Override
	public void onResume() {
		super.onResume();
		getSeries();
	}

	/**
	 * Método encargado de obtener el listado de series del
	 * usuario previamente guardados en la base de datos
	 * y ponerlos en la lista a mostrar
	 * 
	 */
	private void getSeries() {
		List<String> seriesString;

		series = SeriesUtils.getDBSeries(getApplicationContext());
		seriesString = new ArrayList<String>();

		for (Serie serie : series) {
			seriesString.add(serie.getName());
		}

		if (series != null) {
			//la = new IconListViewAdapterDelete(this, R.layout.list_item_icon, seriesString, series, R.drawable.remove);
			la = new IconListAdapter(this, series, R.drawable.favorite);
			setListAdapter(la);

			lv = getListView();
			lv.setTextFilterEnabled(true);
			registerForContextMenu(lv);

			lv.setOnItemClickListener(serieClick);
		}
	}

	/**
	 * Método que se encarga de eliminar el elemento
	 * indicado de la base de datos
	 * 
	 * @param id: el identificador de la serie
	 * @param text, el nombre de la serie a eliminar
	 */
	private void deleteElement(int id, CharSequence text) {
		String message = "";
		String serie = text.toString();
		List<String> seriesString;
		int ret = (int) SeriesUtils.deleteDBSerie(id,
				getApplicationContext());

		series = SeriesUtils.getDBSeries(getApplicationContext());
		seriesString = new ArrayList<String>();

		for (Serie s : series) {
			seriesString.add(s.getName());
		}

		if (series != null) {
			this.la = new IconListAdapter(this, series, R.drawable.favorite);
			setListAdapter(this.la);
		} else {
			this.la = new IconListAdapter(this, series, R.drawable.favorite);
			setListAdapter(this.la);
		}
		if (ret >= 0) {
			message = getString(R.string.delSerie) + serie;
		} else if (ret == -1) {
			message = getString(R.string.delSerieNotExists) + serie;
		}
		showToast(message);
	}
	
	/*
	 * Método que se ejecuta como menu contextual al realizar una
	 * pulsación larga sobre alguno de las series de la lista
	 * Muestra dos elementos: ver detalle y eliminar 
	 */
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, MENU_OPEN, 0, getString(R.string.open));
		menu.add(0, MENU_DELETE, 0, getString(R.string.delete));

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
		case MENU_DELETE:
			deleteElement(serieId, serie);
			break;
		default:
			break;
		}

		lv.setAdapter(la);

		return true;
	}
	
	/**
	 * Método que se encarga de abrir la ficha de la serie indicada
	 * para ello se encarga de crear un intent para abrir la actividad
	 * que representa la ficha, pasandole los parámetros necesarios
	 * @param id: el identificador de la serie
	 */
	private void openSerieInfo(int id) {
		Intent intent = new Intent().setClass(getApplicationContext(),
				SerieInfo.class);
		Bundle b = new Bundle();
		b.putInt("id", id);
		b.putInt("type", 0);
		intent.putExtras(b);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getApplicationContext().startActivity(intent);
	}

	/**
	 * Método que se encarga de mostrar un dialog para la confirmación
	 * de la acción de borrado de una serie
	 * @param serie, nombre de la serie para mostrarle al usuario
	 */
	private void showConfirmDialog(int id, CharSequence serie) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setMessage(getString(R.string.askToDelete) + serie)
				.setPositiveButton(getString(R.string.Ok),
						new CommandDeleteSerie(id, serie)).setNegativeButton(
						getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		AlertDialog alert = dialog.create();
		alert.show();
	}

	/**
	 * Método que se encarga de mostrar un mensaje de confirmación
	 * al realizar alguna acción en la actividad
	 * @param message
	 */
	private void showToast(String message) {
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}
	
	/*
	 * Nuevo Listener que se encarga de recoger el click sobre cualquiera
	 * de los elementos del listado de series. Se encarga de llamar
	 * al método que abre la ficha de la serie. 
	 */
	private OnItemClickListener serieClick = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			openSerieInfo(Integer
					.parseInt(((String) (((TextView) ((RelativeLayout) view)
							.getChildAt(0))).getTag())));
		}
	};
	
	/*
	 * Nuevo Listener que se encarga de recoger el click sobre el 
	 * icono de borrado de cualquiera de los elementos del listado de series.
	 * Se encarga de llamar al método que solicita la confirmación
	 * de borrado de la serie. 
	 */
	public OnClickListener deleteSerie = new OnClickListener() {
		public void onClick(View v) {
			// do something when the button is clicked
			int id = Integer.parseInt(((TextView) ((RelativeLayout) v.getParent())
					.getChildAt(0)).getTag().toString());
			String serie = ((TextView) ((RelativeLayout) v.getParent())
					.getChildAt(0)).getText().toString();
			showConfirmDialog(id, serie);
		}
	};

	/**
	 * Clase interna para la captura de la solicitud de borrado de una serie
	 * 
	 * @author César de la Cruz Rueda (cesarcruz85 [at] gmail.com)
	 *
	 */
	private class CommandDeleteSerie implements DialogInterface.OnClickListener {

		private CharSequence serie;
		private int id;

		public CommandDeleteSerie(int id, CharSequence serie) {
			this.id = id;
			this.serie = serie;

		}

		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			deleteElement(id, serie);
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
	public class IconListAdapter extends BaseAdapter {
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

			im.setOnClickListener(deleteSerie);

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
}
