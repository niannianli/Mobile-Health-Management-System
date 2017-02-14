package mhms.log;

import java.text.SimpleDateFormat;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import mhms.db.DatabaseHelper;

/**
 * log mhmslog(Context context,int Int,String string)
 * ��HealthReport_CREATE��MedicineRecord_CREATE ;HealthSurvey_CREATE;
 * EyeSight_Test ;BMI_Test ;HeartRate_Test ;Medicine_Reminder ;
 * HealthDiets_Select ;Online_Searching ;System_Setting;
 */
public class MhmsLog {

	public static final int HealthReport_CREATE = 1;// add a health report
	public static final int MedicineRecord_CREATE = 2;// add a medicine record
	public static final int HealthSurvey_CREATE = 3;// write a survey
	public static final int EyeSight_Test = 4;// eyesight testing
	public static final int BMI_Test = 5;// BMI testing
	public static final int HeartRate_Test = 6;// heart rate testing
	public static final int Medicine_Reminder = 7;// set medicine remainder
	public static final int Health_Food_Select = 8;// perform a healthy diet
													// choice
	public static final int Online_Searching = 9;// perform a online query
	public static final int System_Setting = 10;// perform a system setting

	/**
	 * mhms action event
	 * 
	 * @param context
	 *            current activity, to get DatabaseHelper
	 * @param Int
	 *            log is int, get MhmsLog's static constant to choose
	 * @param string
	 *            log, write current act description, add a health report, have
	 *            a eyesight testing
	 */
	public static void mhmslog(Context context, int Int, String string) {
		DatabaseHelper dbHelper = new DatabaseHelper(context, "mhms_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SimpleDateFormat sTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String systemtime = sTime.format(new java.util.Date());
		ContentValues values = new ContentValues();
		values.put("systemtime", systemtime);
		values.put("logtype", Int);
		values.put("log", string);
		db.insert("mhmslog", null, values);
		db.close();
	}
}
