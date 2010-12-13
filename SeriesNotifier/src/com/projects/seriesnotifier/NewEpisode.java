package com.projects.seriesnotifier;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NewEpisode extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView text = new TextView(this);
		text.setText("Existe una nueva serie apunto de ser emitida de la lista de series elegida");
		setContentView(text);
	}

}
