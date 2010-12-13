package com.projects.seriesnotifier;

import java.util.EventObject;

import com.projects.utils.SeriesUtils;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

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
		String[] series = SeriesUtils.getSeriesByQuery(getApplicationContext(),
				query);
		if (series != null && series.length > 0) {

			setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
					series));
			ListView lv = getListView();
			lv.setTextFilterEnabled(true);

			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// When clicked, show a toast with the TextView text

					//String message = "";
					String serie = ((TextView) view).getText().toString();

					// SeriesUtils.addSerie(SeriesUtils.SERIES, serie,
					// getApplicationContext());
					/*
					 * int ret = SeriesUtils.addSerie(SeriesUtils.OWNSERIES,
					 * serie, getApplicationContext()); if(ret >= 0) message =
					 * "Serie " + serie + " a�adida correctamente"; else if(ret
					 * == -1) message = "No se ha a�adido la serie " + serie
					 * +", ya existia"; else if(ret == -2) message =
					 * "No se ha a�adido la serie " + serie
					 * +", No existe ninguna serie con ese nombre";
					 * showDialog(message);
					 * //Toast.makeText(getApplicationContext(), ((TextView)
					 * view).getText(),Toast.LENGTH_SHORT).show();
					 */
					showConfirmDialog(serie);
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
}