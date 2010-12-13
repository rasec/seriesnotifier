package com.projects.seriesnotifier;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.projects.database.DBAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.database.*;

public class Settings extends Activity {
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        String text = "Aquí irán las propiedades u opciones de la aplicación";
                
        TextView textview = new TextView(this);
        textview.setText(text);
        setContentView(textview);
    }
	
	@Override 
	public void onResume()
	{
		super.onResume();
	}
}
