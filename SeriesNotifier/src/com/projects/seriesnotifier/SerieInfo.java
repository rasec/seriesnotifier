package com.projects.seriesnotifier;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SerieInfo extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serie_info);
		
		// Se obtiene el par�metro pasado
		//Bundle b = getIntent().getExtras();
		//String q = b.getCharSequence("q").toString();
		

		// En caso de no haber series disponibles se puede
		// a�adir la serie indicada, para lo que se crea un
		// listener cuando se pulsa el bot�n a�adir
		
	}
}
