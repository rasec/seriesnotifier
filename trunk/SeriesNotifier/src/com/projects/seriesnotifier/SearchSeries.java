package com.projects.seriesnotifier;

import com.projects.utils.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;

public class SearchSeries extends Activity {
	
	static int i = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_series);
        populateAutocompleteSerie();
               
//        Button button = (Button)findViewById(R.id.newserie);
//        button.setOnClickListener(setNewSerie);
        
        Button buttonSearch = (Button)findViewById(R.id.ok);
        AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.autocomplete_serie);
        buttonSearch.setOnClickListener(searchSerie);
        textView.setOnKeyListener(searchSerieEnter);
    }
	
	@Override
    public void onResume() {
		super.onResume();
		populateAutocompleteSerie();
	}
	
	/*
	 * Realiza el 'poblado' de los elementos del autocompletado
	 * obteniendolo de la lista de series disponibles 
	 */
	public void populateAutocompleteSerie()
	{
		AutoCompleteTextView textView = (AutoCompleteTextView)findViewById(R.id.autocomplete_serie);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, SeriesUtils.getListSeries(getApplicationContext()));
        textView.setAdapter(adapter);
	}
	
	/* 
	 * Listener que recibe la pulsaci�n en el bot�n buscar
	 * y guarda el texto a buscar, lanzando una nueva actividad
	 * (NewSearch) de busqueda de series pasandole el par�metro
	 */
	public OnClickListener searchSerie = new OnClickListener() {
		public void onClick(View v) {
			// Se crean los elementos necesarios: texto a pasar y intent a ejecutar
			EditText edittext = (EditText)findViewById(R.id.autocomplete_serie);
			//ProgressDialog dialog = ProgressDialog.show(getApplicationContext(), "", "Buscando...");
	    	String serie = edittext.getText().toString();
			Intent intent = new Intent().setClass(getApplicationContext(), NewSearch.class);
	    	
			// Se crea el par�metro a pasar y se a�ade al intent
			Bundle b = new Bundle();
			b.putCharSequence("q", serie);
			intent.putExtras(b);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// Se inicia la nueva actividad con el intent
	    	getApplicationContext().startActivity(intent);
	    	//dialog.dismiss();
		}
	};
	
	public OnKeyListener searchSerieEnter = new OnKeyListener() {
		public boolean onKey(View v, int keyCode, KeyEvent event) {
    		if (event.getAction() == KeyEvent.ACTION_DOWN)
    		{
    			if (keyCode == KeyEvent.KEYCODE_ENTER)
    			{
    				EditText edittext = (EditText)findViewById(R.id.autocomplete_serie);
    				//ProgressDialog dialog = ProgressDialog.show(getApplicationContext(), "", "Buscando...");
    		    	String serie = edittext.getText().toString();
    				Intent intent = new Intent().setClass(getApplicationContext(), NewSearch.class);
    				// Se crea el par�metro a pasar y se a�ade al intent
    				Bundle b = new Bundle();
    				b.putCharSequence("q", serie);
    				intent.putExtras(b);
    				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    				// Se inicia la nueva actividad con el intent
    		    	getApplicationContext().startActivity(intent);
    		    	return true;
    			}
    		}
    		return false;
		}
	};
	
	
	
//	public OnClickListener setNewSerie = new OnClickListener() {
//	    public void onClick(View v) {
//	    	Intent intent = new Intent().setClass(getApplicationContext(), NewSerie.class);
//	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//	    	getApplicationContext().startActivity(intent);
//	    }
//	};
	
//	public OnClickListener setOwnNewSerie = new OnClickListener() {
//	    public void onClick(View v) {
//	        // do something when the button is clicked
//	    	String message = "";
//	    	EditText edittext = (EditText)findViewById(R.id.autocomplete_serie);
//	    	String serie = edittext.getText().toString();
//	    	//SeriesUtils.addSerie(SeriesUtils.SERIES, serie, getApplicationContext());
//	    	int ret = SeriesUtils.addSerie(SeriesUtils.OWNSERIES, serie, getApplicationContext());
//	    	if(ret >= 0)
//	    		message = "Serie " + serie + " a�adida correctamente";
//	    	else if(ret == -1)
//	    		message = "No se ha a�adido la serie " + serie +", ya existia";
//	    	else if(ret == -2)
//	    		message = "No se ha a�adido la serie " + serie +", No existe ninguna serie con ese nombre";
//			showDialog(message);
//	    }
//	};
	
	
//	public void showDialog(String message){
//		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//	    dialog.setMessage(message)
//	    .setCancelable(false)
//	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//	           public void onClick(DialogInterface dialog, int id) {
//	                dialog.cancel();
//	           }
//	       });
//	    dialog.show();
//	}
}
