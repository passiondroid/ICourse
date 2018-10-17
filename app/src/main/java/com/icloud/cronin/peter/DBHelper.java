package com.icloud.cronin.peter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper {

	private static final String TAG = DBHelper.class.getSimpleName();

	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_NAME = "mydatabase.db";
	private static final String DB_FILE_NAME = "mydatabase.sqlite";
	private static DBHelper dbHelper = null;

	private static final String TABLE_NAME = "RACE_COURSE";
	private static final String COURSE_TABLE_COLUMN_ID = "_id";
	private static final String COURSE_TABLE_COLUMN_COURSE = "COURSE";
	private static final String COURSE_TABLE_COLUMN_COURSE_NUMBER ="COURSE_NUMBER";
	//private static final String COURSE_TABLE_COLUMN_COURSE_COLOR ="COURSE_COLOR";


	private static final String[]ALL_COLUMNS = new String[]{COURSE_TABLE_COLUMN_ID, COURSE_TABLE_COLUMN_COURSE, COURSE_TABLE_COLUMN_COURSE_NUMBER};

	private DatabaseOpenHelper openHelper;
	private SQLiteDatabase database;
	private static String DB_PATH = ""; 
	private final Context mContext;


	private DBHelper(Context context){
		openHelper = new DatabaseOpenHelper(context);
		database = openHelper.getWritableDatabase();
		if(android.os.Build.VERSION.SDK_INT >= 17){
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";         
		}
		else
		{
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		}
		this.mContext = context;



	}

	/**
	 * @param context
	 * @return
	 */
	public static synchronized DBHelper getInstance(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
		}
		return dbHelper;
	}

	public long insertData (String aCourse, String aCourseNumber){

		ContentValues contentValues = new ContentValues();

		contentValues.put(COURSE_TABLE_COLUMN_COURSE, aCourse);
		contentValues.put(COURSE_TABLE_COLUMN_COURSE_NUMBER, aCourseNumber);

		return database.insert(TABLE_NAME, null, contentValues);
	}

	public Cursor getAllData(){
		String buildSQL = "SELECT * FROM " + TABLE_NAME;
		Log.d(TAG, "getAllData SQL: " + buildSQL);

		return database.rawQuery(buildSQL, null);
	}

	private class DatabaseOpenHelper extends SQLiteOpenHelper{

		public DatabaseOpenHelper(Context context) {
			super(context, DATABASE_NAME , null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase sqLiteDatabase) {

			String buildSQL = "CREATE TABLE " + TABLE_NAME + "( " +  COURSE_TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COURSE_TABLE_COLUMN_COURSE + " VARCHAR NOT NULL, " + COURSE_TABLE_COLUMN_COURSE_NUMBER + " VARCHAR NOT NULL)"; 

			Log.d(TAG, "onCreate SQL: " + buildSQL);

			sqLiteDatabase.execSQL(buildSQL);

		}

		@Override
		public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

			String buildSQL = "DROP TABLE IF EXISTS " + TABLE_NAME;

			Log.d(TAG, "onUpgrade SQL " + buildSQL);
			onCreate(sqLiteDatabase);

		}

	}



	public String getCourse(String l)throws SQLException {
		String[] columns = new String[]{COURSE_TABLE_COLUMN_COURSE};
		String Course = "";
		Cursor c = database.query(TABLE_NAME, columns, COURSE_TABLE_COLUMN_COURSE_NUMBER + "='" + l+"'", null, null, null, null);
		if (c != null && c.getCount() > 0){
			c.moveToFirst();
			Course = c.getString(0);
		}
		return Course;
	}

	public String getCourse_Number(long l) {
		String[] columns = new String[]{COURSE_TABLE_COLUMN_ID, COURSE_TABLE_COLUMN_COURSE, COURSE_TABLE_COLUMN_COURSE_NUMBER};
		Cursor c = database.query(TABLE_NAME, columns, COURSE_TABLE_COLUMN_ID + "=" + l, null, null, null, null);
		if (c != null){
			c.moveToFirst();
			String Course_Number = c.getString(2);
			return Course_Number;
		}
		return null;
	}

	public String getCourses(long l) {
		String[] columns = new String[]{COURSE_TABLE_COLUMN_ID, COURSE_TABLE_COLUMN_COURSE, COURSE_TABLE_COLUMN_COURSE_NUMBER};
		Cursor c = database.query(TABLE_NAME, columns, COURSE_TABLE_COLUMN_ID + "=" + l, null, null, null, null);
		if (c != null){
			c.moveToFirst();
			String Course = c.getString(1);
			return Course;
		}
		return null;
	}

	public int deleteEntry(String courseNumber) {
		return database.delete(TABLE_NAME, COURSE_TABLE_COLUMN_COURSE_NUMBER + "='" + courseNumber+"'", null);
	}

	public int updateEntry(String mCourseNumber,String mCourse) {
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(COURSE_TABLE_COLUMN_COURSE, mCourse);
		return database.update(TABLE_NAME, cvUpdate, COURSE_TABLE_COLUMN_COURSE_NUMBER + "='"+mCourseNumber+"'", null);
	}

	public Cursor getAllRows(){
		String where = null;
		Cursor c = database.query(true, TABLE_NAME, ALL_COLUMNS, where, null, null, null, null, null);
		if (c != null){
			c.moveToFirst();
		}

		return c;
	}

	public String getData(){
		String[] columns = new String[]{COURSE_TABLE_COLUMN_ID, COURSE_TABLE_COLUMN_COURSE, COURSE_TABLE_COLUMN_COURSE_NUMBER};
		Cursor c = database.query(TABLE_NAME, columns, null, null, null, null, null);
		String result = "  ";

		int iRow = c.getColumnIndex(COURSE_TABLE_COLUMN_ID);
		int iCourseNumber = c.getColumnIndex(COURSE_TABLE_COLUMN_COURSE_NUMBER);
		int iCourse = c.getColumnIndex(COURSE_TABLE_COLUMN_COURSE);

		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			result = result + c.getString(iRow) + " " + c.getString(iCourseNumber) + "    " + c.getString(iCourse) + "   \n";
		}
		return result;
	}

	public boolean createdatabase()  {
		boolean dbexist = checkdatabase();
		try {
			if (!dbexist) {
				Log.i("DBHelper", "creating db at location : " + DB_PATH
						+ Constants.db_name);
				SQLiteDatabase triDatabase = SQLiteDatabase
						.openOrCreateDatabase(DB_PATH
								+ Constants.db_name, null);
				triDatabase.close();
				copydatabase(DB_PATH + Constants.db_name);
			}
		} catch (IOException e) {
			Log.e("DB Exception", e.getMessage(),e);
		} catch (SQLiteException e) {
			Log.e("DB Exception", e.getMessage(),e);
		}
		return dbexist;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * 
	 * @return
	 */
	private boolean checkdatabase() {
		boolean checkdb = false;
		File dbfile;
		try {
			dbfile = new File(DB_PATH + Constants.db_name);
			checkdb = dbfile.exists();
			Log.i("DBHelper", "Checking the database at location : "
					+ DB_PATH + Constants.db_name
					+ "is DB Present " + checkdb);
		} catch (Exception e) {
			Log.e("Exception", "Exception occured while checking database",
					e);
		}
		return checkdb;
	}

	/**
	 * Copies your database from assets-folder to the just created empty
	 * database in the system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * 
	 * @param path
	 * @throws IOException
	 */
	private void copydatabase(String path) throws IOException {
		// Open your local db as the input stream
		Log.i("DBHelper", "Copying db at path : " + path);

		InputStream inputStream = mContext.getAssets().open(DB_FILE_NAME);

		// Open the empty db as the output stream
		OutputStream outputStream = new FileOutputStream(path);

		// transfer byte to inputfile to outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = inputStream.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}

		// Close the streams
		outputStream.flush();
		outputStream.close();
		inputStream.close();
	}

	public SQLiteDatabase getWritableDatabase() {
		database = SQLiteDatabase.openDatabase(DB_PATH+DATABASE_NAME, null,SQLiteDatabase.OPEN_READWRITE);
		return database;
	}

}


