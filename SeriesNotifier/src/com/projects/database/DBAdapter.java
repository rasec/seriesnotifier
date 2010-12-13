package com.projects.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	private static final String DATABASE_NAME = "seriesnotifier";
	private static final String DATABASE_TABLE = "series";
	private static final int DATABASE_VERSION = 1;
	
	// The index (key) column name for use in where clauses.
	public static final String KEY_ID="_id";
	// The name and column index of each column in your database.
	public static final String KEY_NAME="name";
	public static final int NAME_COLUMN = 1;
	
	private static final String TAG = "DBAdapter";

	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " (" + KEY_ID + " integer primary key autoincrement, "
			+ KEY_NAME + " text not null unique);";

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

	public long insertSerie(String serie) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, serie);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public long deleteAllSerie() {
		return db.delete(DATABASE_TABLE, null, null);
	}
	
	public long deleteSerie(String serie)
	{
		return db.delete(DATABASE_TABLE, KEY_NAME+" = '"+serie+"'", null);
	}

	public Cursor getSeries() {
		return db.query(DATABASE_TABLE, new String[] { KEY_ID, KEY_NAME },
				null, null, null, null, KEY_NAME);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS serie");
			onCreate(db);
		}
	}

}
