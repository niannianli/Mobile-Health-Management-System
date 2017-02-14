package mhms.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int Version = 1;

	/**
	 * construct methods of four parameters
	 * 
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	/**
	 * construct methods of three parameters
	 * 
	 * @param context
	 * @param name
	 * @param version
	 */
	public DatabaseHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	/**
	 * construct methods of two parameters
	 * 
	 * @param context
	 * @param name
	 */
	public DatabaseHelper(Context context, String name) {
		this(context, name, Version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		// db = this.getReadableDatabase();
		db.execSQL("create table bmitable(id integer primary key autoincrement,"
				+ "systemtime varchar(20),bodyheight float,bodyweight float,bmi float)");

		db.execSQL("create table heartratetable(id integer primary key autoincrement,"
				+ "systemtime varchar(20),heartrate integer)");

		db.execSQL("create table eyesighttable(id integer primary key autoincrement,systemtime varchar(20),"
				+ "eyesight double)");

		db.execSQL("create table healthreport(id integer primary key autoincrement,systemtime varchar(20),"
				+ "bodyheight float,bodyweight float,eyesight float,heartrate integer,"
				+ "bloodpressure float,bloodsugar float,bloodlipid float,"
				+ "surgery varchar(20),internal varchar(20),bloodroutine varchar(20))");

		db.execSQL("create table medicinerecord(id integer primary key autoincrement,systemtime varchar(20),"
				+ "ill_name varchar(20),ill_begin varchar(20),ill_end varchar(20),"
				+ "pill_using varchar(100),ill_describe varchar(100))");

		db.execSQL("create table mhmslog(id integer primary key autoincrement,"
				+ "systemtime varchar(20),logtype integer,log varchar(100))");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
