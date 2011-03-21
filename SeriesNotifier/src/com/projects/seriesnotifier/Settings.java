package com.projects.seriesnotifier;

import android.os.Bundle;

import android.preference.EditTextPreference;

import android.preference.PreferenceActivity;

import android.text.method.DigitsKeyListener;

import android.widget.EditText;


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
