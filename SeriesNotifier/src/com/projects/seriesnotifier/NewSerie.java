package com.projects.seriesnotifier;

import com.projects.utils.SeriesUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class NewSerie extends Activity{
	
	static final int NEW_SERIE_DIALOG = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newserie);
        Button button = (Button)findViewById(R.id.ok_new);
        button.setOnClickListener(setNewSerie);
    }
	
	public OnClickListener setNewSerie = new OnClickListener() {
	    public void onClick(View v) {
	        // do something when the button is clicked
	    	String message = "";
	    	EditText edittext = (EditText)findViewById(R.id.entry_new);
	    	String serie = edittext.getText().toString();
	    	int ret = SeriesUtils.addSerie(SeriesUtils.SERIES, serie, getApplicationContext());
	    	//SeriesUtils.addSerie(SeriesUtils.OWNSERIES, serie, getApplicationContext());
	    	
	    	if(ret >= 0)
	    		message = "Serie " + serie + " añadida correctamente";
	    	else
	    		message = "No se ha añadido la serie " + serie +", ya existia";
    		showDialog(message);
	    		
	    }
	};
	
	public void showDialog(String message){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
	    dialog.setMessage(message)
	    .setCancelable(false)
	    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	           }
	       });
	    dialog.show();
	}
	
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = new Dialog(this);
	    switch(id) {
		    case NEW_SERIE_DIALOG:
		        // do the work to define the pause Dialog
		    	dialog.setTitle("Serie Añadida");
		        break;
		    default:
		        dialog = null;
	    }
	    return dialog;
	}
	
}
