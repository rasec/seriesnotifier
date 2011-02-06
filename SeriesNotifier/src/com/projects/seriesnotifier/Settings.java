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
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.*;

public class Settings extends PreferenceActivity {
	
	static final int NUM_DIALOG_ID = 0;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                
        Preference emailNotifications = (Preference) findPreference("emailNotifications");
        Preference checkForUpdatesFrecuence = (Preference) findPreference("checkForUpdatesFrecuence");
        Preference checkForUpdatesHour = (Preference) findPreference("checkForUpdatesHour");       
        
        checkForUpdatesFrecuence.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				int value = preference.getSharedPreferences().getInt("checkForUpdatesFrecuence", -1);
				Toast.makeText(getBaseContext(), "Valor actual: " + value, Toast.LENGTH_LONG).show();
				return false;
			}
		});
        
    }
	
	@Override 
	public void onResume()
	{
		super.onResume();
	}
}
