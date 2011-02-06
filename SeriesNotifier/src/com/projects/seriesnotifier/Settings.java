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
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        
        EditTextPreference myEditTextPreference = (EditTextPreference) findPreference("checkForUpdatesFrecuence");
        EditText myEditText = (EditText)myEditTextPreference.getEditText();
        myEditText.setKeyListener(DigitsKeyListener.getInstance(false,true));
        
        EditTextPreference myEditTextPreference2 = (EditTextPreference) findPreference("checkForUpdatesHour");
        EditText myEditText2 = (EditText)myEditTextPreference2.getEditText();
        myEditText2.setKeyListener(DigitsKeyListener.getInstance(false,true));
        
    }
	
	@Override 
	public void onResume()
	{
		super.onResume();
	}
}
