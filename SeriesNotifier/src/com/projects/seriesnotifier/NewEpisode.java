package com.projects.seriesnotifier;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NewEpisode extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		TextView text = new TextView(this);
		text.setText(R.string.newEpisdeProv);
		setContentView(text);
	}

}
