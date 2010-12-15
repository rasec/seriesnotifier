package com.projects.seriesnotifier;

import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SerieInfo extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serie_info);
		
		ImageView img = (ImageView)findViewById(R.id.banner);
		Drawable drawable = LoadImageFromWebOperations("http://www.androidpeople.com/wp-content/uploads/2010/03/android.png");

        img.setImageDrawable(drawable);

		
		// Se obtiene el par�metro pasado
		//Bundle b = getIntent().getExtras();
		//String q = b.getCharSequence("q").toString();
		

		// En caso de no haber series disponibles se puede
		// a�adir la serie indicada, para lo que se crea un
		// listener cuando se pulsa el bot�n a�adir
		
	}
	
	private Drawable LoadImageFromWebOperations(String url)
	{
	       try
	       {
	           InputStream is = (InputStream) new URL(url).getContent();
	           Drawable d = Drawable.createFromStream(is, "src name");
	           return d;
	        }catch (Exception e) {
	            System.out.println("Exc="+e);
	            return null;
	        }
	}

}
