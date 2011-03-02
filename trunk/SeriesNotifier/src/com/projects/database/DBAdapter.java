package com.projects.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	private static final String DATABASE_NAME = "seriesnotifier";
	private static final String DATABASE_TABLE_SERIES = "series";
	private static final String DATABASE_TABLE_SERIES_UPDATES = "seriesUpdates";
	private static final String DATABASE_TABLE_EPOCH_DATE = "epochDate";
	
	private static final String EPOCH_ID = "epoch";
	
	private static final int DATABASE_VERSION = 2;
	
	// The index (key) column name for use in where clauses.
	public static final String KEY_ID="_id";
	// The name and column index of each column in your database.
	public static final String KEY_NAME="name";
	
	public static final String KEY_VISTO="visto";
	
	public static final String KEY_EPOCH="epoch";
	
	public static final int NAME_COLUMN = 1;
	
	private static final String TAG = "DBAdapter";

	private static final String CREATE_TABLE_SERIES = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_SERIES + " (" + KEY_ID + " integer primary key autoincrement, "
			+ KEY_NAME + " text not null unique);";
	private static final String CREATE_TABLE_UPDATES = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_SERIES_UPDATES + " (" + KEY_ID + " integer primary key autoincrement, "
	+ KEY_NAME + " text not null, " + KEY_VISTO + " BOOLEAN);";
	
	private static final String CREATE_TABLE_EPOCH = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE_EPOCH_DATE + " (" + KEY_ID + " text primary key, "
	+ KEY_EPOCH + " BIGINT not null);";

	private final Context context;

	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	public DBAdapter open() {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public long insertSerie(String serie, int id) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, serie);
		initialValues.put(KEY_ID, id);
		return db.insert(DATABASE_TABLE_SERIES, null, initialValues);
	}
	
	public long insertSerieUpdate(String serie, int id) {
		long ret = 0;
		ContentValues initialValues = new ContentValues();
		Cursor series = getSeriesUpdates(id);
		if (series.getCount()>0) {
			series.moveToFirst();
			series.moveToPosition(0);
			initialValues.put(KEY_NAME, series.getString(1));
			initialValues.put(KEY_ID, series.getString(0));
			initialValues.put(KEY_VISTO, 0);
			ret = db.update(DATABASE_TABLE_SERIES_UPDATES, initialValues, KEY_ID + " = " + series.getString(0), null);
		} else {
			initialValues.put(KEY_NAME, serie);
			initialValues.put(KEY_ID, id);
			initialValues.put(KEY_VISTO, 0);
			ret = db.insert(DATABASE_TABLE_SERIES_UPDATES, null, initialValues);
			
		}
		return ret;
	}
	
	public long insertEpochDate(long epoch) {
		long ret = 0;
		ContentValues initialValues = new ContentValues();
		Cursor epochs = getEpochDate();
		if (epochs.getCount() > 0) {
			epochs.moveToFirst();
			initialValues.put(KEY_ID, EPOCH_ID);
			initialValues.put(KEY_EPOCH, epoch);
			ret = db.update(DATABASE_TABLE_EPOCH_DATE, initialValues, null, null);
		} else {
			initialValues.put(KEY_ID, EPOCH_ID);
			initialValues.put(KEY_EPOCH, epoch);
			ret = db.insert(DATABASE_TABLE_EPOCH_DATE, null, initialValues);
			
		}
		return ret;
	}
	
	public boolean existsSerie(int id) {
		return db.query(DATABASE_TABLE_SERIES_UPDATES, new String[] { KEY_ID, KEY_NAME },
				KEY_ID + " = " + id, null, null, null, KEY_ID).getCount() > 0 ? true : false;
	}
	
	public long deleteAllSerie() {
		return db.delete(DATABASE_TABLE_SERIES, null, null);
	}
	
	public long deleteAllUpdatesSerie() {
		return db.delete(DATABASE_TABLE_SERIES_UPDATES, null, null);
	}
	
	public long deleteSerie(String serie)
	{
		return db.delete(DATABASE_TABLE_SERIES, KEY_NAME+" = '"+serie+"'", null);
	}
	
	public long deleteSerieUpdate(String serie)
	{
		return db.delete(DATABASE_TABLE_SERIES_UPDATES, KEY_NAME+" = '"+serie+"'", null);
	}
	
	public long deleteSerie(int id)
	{
		return db.delete(DATABASE_TABLE_SERIES, KEY_ID+" = '"+id+"'", null);
	}
	
	public long deleteSerieUpdate(int id)
	{
		return db.delete(DATABASE_TABLE_SERIES_UPDATES, KEY_ID+" = '"+id+"'", null);
	}

	public Cursor getSeries() {
		return db.query(DATABASE_TABLE_SERIES, new String[] { KEY_ID, KEY_NAME },
				null, null, null, null, KEY_NAME);
	}
	
	public Cursor getSeriesUpdates() {
		return db.query(DATABASE_TABLE_SERIES_UPDATES, new String[] { KEY_ID, KEY_NAME },
				KEY_VISTO + "= 0", null, null, null, KEY_NAME);
	}
	
	public Cursor getSeriesUpdates(int id) {
		return db.query(DATABASE_TABLE_SERIES_UPDATES, new String[] { KEY_ID, KEY_NAME },
				KEY_ID + " = " + id, null, null, null, KEY_NAME);
	}
	
	public Cursor getEpochDate() {
		return db.query(DATABASE_TABLE_EPOCH_DATE, new String[] { KEY_EPOCH },
				null, null, null, null, KEY_EPOCH);
	}
	
	public int updateSeriesUpdates() {
		ContentValues updatesValues = new ContentValues();
		updatesValues.put(KEY_VISTO, 1);
		return db.update(DATABASE_TABLE_SERIES_UPDATES, updatesValues, null, null);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE_SERIES);
			db.execSQL(CREATE_TABLE_UPDATES);
			db.execSQL(CREATE_TABLE_EPOCH);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SERIES);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SERIES_UPDATES);
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_EPOCH_DATE);
			onCreate(db);
		}
	}

}

