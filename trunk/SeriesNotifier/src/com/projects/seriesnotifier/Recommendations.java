package com.projects.seriesnotifier;

import java.util.ArrayList;
import java.util.List;

import com.projects.series.Serie;
import com.projects.seriesnotifier.NewSearch.CommandAddSerie;
import com.projects.utils.SeriesUtils;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Recommendations extends ListActivity {
	
	private ListView lv;
	private IconListViewAdapterAdd la;
	private List<Serie> series;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRecommendations();        
    }   
	
    @Override
	public void onResume() {
		super.onResume();
		getRecommendations();
    }
    
    private void getRecommendations() {
		series = SeriesUtils.getRecommendations(getApplicationContext());
		List<String> seriesString = new ArrayList<String>();
		for (Serie serie : series) {
			seriesString.add(serie.getName());
		}
		if (series != null) {
			//la = new IconListViewAdapterDelete(this, R.layout.list_item_icon, seriesString, series, R.drawable.remove);
			la = new IconListViewAdapterAdd(this, R.layout.list_item_icon,seriesString, series, R.drawable.star_grey);
			setListAdapter(la);

			lv = getListView();
			lv.setTextFilterEnabled(true);
			registerForContextMenu(lv);

			lv.setOnItemClickListener(serieClick);
		}
	}/*
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
			showConfirmDialog(serie, id);
		}
	};
	
	public void addSerie(CharSequence serie, int id) {
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
	
	/**
	 * Método que se encarga de mostrar un dialog para la confirmación
	 * de la acción de borrado de una serie
	 * @param serie, nombre de la serie para mostrarle al usuario
	 */
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
			boolean fav = items.get(position).isFav();
			
			// poblamos la lista de elementos

			TextView tt = (TextView) v.findViewById(R.id.listText);
			ImageView im = (ImageView) v.findViewById(R.id.listIcon);

			im.setOnClickListener(addSerie);

			if (im != null) {
				if(fav) {
					im.setImageResource(R.drawable.favorite);
				} else {
					im.setImageResource(this.icon);
				}
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
}
