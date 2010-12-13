package com.projects.utils;


import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.projects.database.DBAdapter;
import com.projects.seriesnotifier.R;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;

public class SeriesUtils {
	public static String SERIES = "series";
	public static String OWNSERIES = "ownseries";
	
	public static String fileToString(FileInputStream fis){
			
		int ch = 0;
		StringBuffer buffer = new StringBuffer();
		try {
			while((ch = fis.read()) != -1)
			{
				buffer.append((char) ch);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return buffer.toString();
	}
	  
	  public static boolean isEmptyFile(String name, Context context){
		  FileInputStream fis = null;
		try {
			fis = context.openFileInput(name);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
		  return fileToString(fis).equals("");
	  
	  }
	  
	  public static String[] getSeries(Context context, String tipo){
		  if(tipo == SERIES){
			  return getListSeries(context);
			  
		  }else if(tipo == OWNSERIES){
			  //return getOwnSeries(context);
			  return getDBSeries(context);
		  }else{
			  return null;
		  }
	  }
	  
	  
	  
	  public static String[] getListSeries(Context context){
		String[] list = new String[100];
		String names;
		FileInputStream fis;
		list = context.fileList();
		if(list.length>0){
			try {
				fis = context.openFileInput(SERIES);
				names = fileToString(fis);
				list = names.split(",");
				fis.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return list;
	}
	  
	public static String[] getOwnSeries(Context context){
		String[] listFiles = null;
		String[] list = null;
		FileInputStream fis;
		String names;
		listFiles = context.fileList();
		if(listFiles.length>1){
			try {
				fis = context.openFileInput(OWNSERIES);
				names = fileToString(fis);
				fis.close();
				if(names != ""){
				
					list = names.split(",");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static int addSerie(String file, String serie, Context context){
		String text = "";
		int ret = 0;
		if(serieAlreadyExists(file, serie, context)){
			// Ya exist�a la serie
			ret = -1;
		}else if(file == OWNSERIES && !serieExists(serie, context)){
			// La serie no se encutran en la BBDD
			ret = -2;
		}else{
			if(!isEmptyFile(file, context)){
	    		text += ",";
	    	}
	    	text += serie;
	    	FileOutputStream fos;
			try {
				fos = context.openFileOutput(file, Context.MODE_APPEND);
				fos.write(text.getBytes());
				fos.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		return ret;
	}
	
	public static int deleteSerie(String file, String serie, Context context){
		int ret = 0;
		if(!serieAlreadyExists(file, serie, context)){
			// No exist�a la serie
			ret = -1;
		}else{
			String[] list = getSeries(context, file);
			if(list != null && list.length > 1){
				int i = 0,j = 0;
				String[] listAux = new String[list.length-1];
				for (String item : list) {
					if(!item.toLowerCase().equals(serie.toLowerCase())){
						listAux[i] = list[j];
						i++;
					}
					j++;
				}
				String text = toSimpleString(listAux);
				FileOutputStream fos;
				try {
					fos = context.openFileOutput(file, Context.MODE_PRIVATE);					
					fos.write(text.getBytes());
					fos.close();					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else if(list != null)
			{
				String text = "";
				FileOutputStream fos;
				try {
					fos = context.openFileOutput(file, Context.MODE_PRIVATE);					
					fos.write(text.getBytes());
					fos.close();					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			
			}
		}
		return ret;
	}
	
	private static String toSimpleString(String[] listAux) {
		String ret = listAux[0];
		for (int i = 1; i < listAux.length; i++) {
			ret += "," + listAux[i];
		}
		return ret;
	}

	private static boolean serieAlreadyExists(String file, String serie, Context context) {
		boolean exists = false;		
		String[] list = null;
		FileInputStream fis;
		String names;
		try {
			fis = context.openFileInput(file);
			names = fileToString(fis);
			fis.close();
			list = names.split(",");
			for (String s : list) {
				if(s.toLowerCase().equals(serie.toLowerCase())){
					exists = true;
					break;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return exists;
	}
	
	private static boolean serieExists(String serie, Context context) {
		boolean exists = false;		
		String[] list = null;
		FileInputStream fis;
		String names;
		try {
			fis = context.openFileInput(SERIES);
			names = fileToString(fis);
			fis.close();
			list = names.split(",");
			for (String s : list) {
				if(s.toLowerCase().equals(serie.toLowerCase())){
					exists = true;
					break;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return exists;
	}

	public static String[] getSeriesByQuery(Context applicationContext,	String query) {
		String[] list = null;
		//String[] list = new String[100];
		/*String names;
		FileInputStream fis;
		list = applicationContext.fileList();
		if(list.length>0){
			try {
				fis = applicationContext.openFileInput(SERIES);
				names = fileToString(fis);
				//list = names.split(",");
				list = filterSeries(names.split(","), query);
				//list[0] = Integer.toString(getNumberOcurrences(names.split(","), query));
				fis.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		list = getSeriesTvDB(query, applicationContext).split(",");
		
		return list;
	}

	private static String[] filterSeries(String[] split, String query) {
		String[] listaRet = new String[getNumberOcurrences(split, query)];
		int i = 0;
		for (String serie : split) {
			if(serie.toLowerCase().contains(query.toLowerCase())){
				listaRet[i] = serie;
				i++;
			}
		}
		return listaRet;
	}
	
	private static int getNumberOcurrences(String[] split, String query) {
		int ret = 0;
		//String[] ret = new String[100];
		for (String serie : split) {
			if((serie.toLowerCase()).contains((query.toLowerCase()))){
				ret++;
			}
		}
		return ret;
	}
	
	/* DataBase Methods */
		
	public static String[] getDBSeries(Context context)
	{
		DBAdapter db = new DBAdapter(context);
		String[] ret = null;
		String series = "";
		int cols = 0;
		db.open();
  
		Cursor cursor = db.getSeries();
		cursor.moveToFirst();
		cols = cursor.getCount();
		if(cols > 0){
			for(int i = 0; i < cursor.getCount(); i++)
			{
				cursor.moveToPosition(i);
				if(i == 0)
					series += cursor.getString(1);
				else
					series += "," + cursor.getString(1);
				
			}
			ret = series.split(",");
		}
  
		db.close();
		return ret;		  
	}
	
	public static long addDBSerie(String serie, Context context){
		DBAdapter db = new DBAdapter(context);
		long ret = 0;
		db.open();
		ret =  db.insertSerie(serie);
		db.close();
		return ret;
	}
	
	public static long deleteDBSerie(String serie, Context context)
	{
		long ret = 0;
		DBAdapter db = new DBAdapter(context);
		db.open();
		
		// Delete de serie by name
		db.deleteSerie(serie);
		
		db.close();
		return ret;
	}
	
	
	/* METODOS PARA EL ACCESO ATHETVDB */
	public static String getSeriesTvDB(String name, Context context)
	{
		URL url;
		String ret = "";
		String site = context.getString(R.string.getSeries);
		String paramName = context.getString(R.string.getSeriesParam);
		try {
			url = new URL(site+"?"+paramName+"="+URLEncoder.encode(name));
			URLConnection connection;
			connection = url.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection)connection;
			int responseCode = httpConnection.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK){
				//ret += "Peticion Correcta";
				InputStream in = httpConnection.getInputStream();
				DocumentBuilderFactory dbf;
				dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				// Parse the earthquake feed.
				Document dom = db.parse(in);
				Element docEle = dom.getDocumentElement();
				// Get a list of each earthquake entry.
				NodeList nl = docEle.getElementsByTagName("Series");
				if (nl != null && nl.getLength() > 0) {
					for (int i = 0 ; i < nl.getLength(); i++) {
						Element entry = (Element)nl.item(i);
						Element SerieName =
						   (Element)entry.getElementsByTagName("SeriesName").item(0);
						if(i==0){
							ret += SerieName.getFirstChild().getNodeValue();;
						}else{
							ret += "," + SerieName.getFirstChild().getNodeValue();
						}
						
					}
				}								
				
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ret;
	}
	
}