package mhms.healthrecords;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.Zxing.CaptureActivity;

import mhms.healthrecords.R;
import mhms.db.DatabaseHelper;
import mhms.log.MhmsLog;
import mhms.menu.MenuAction;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class HealthReport extends TabActivity {
	private TextView healthreportlist_title;
	private TextView systemtime_get;
	private TextView id_get;

	private EditText bodyheight;
	private EditText bodyweight;
	private EditText eyesight;
	private EditText heartrate;
	private EditText bloodpressure;
	private EditText bloodsugar;
	private EditText bloodlipid;
	private EditText surgery;
	private EditText internal;
	private EditText bloodroutine;

	private EditText bodyheight_get;
	private EditText bodyweight_get;
	private EditText eyesight_get;
	private EditText heartrate_get;
	private EditText bloodpressure_get;
	private EditText bloodsugar_get;
	private EditText bloodlipid_get;
	private EditText surgery_get;
	private EditText internal_get;
	private EditText bloodroutine_get;

	private Button toaddbtn;
	private Button addbtn;
	private Button clearbtn;
	private Button barcodebtn;
	private Button updatebtn;
	private Button deletebtn;
	private Button returnbtn1;
	private Button returnbtn2;
	private Button returnadd;
	private Intent intent_camera;

	private List<HashMap<String, Object>> lists;

	public static TabHost myTabhost;
	protected int myMenuSettingTag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.healthreport);

		healthreportlist_title = (TextView) findViewById(R.id.healthreportlist_title);
		id_get = (TextView) findViewById(R.id.report_id_get);
		systemtime_get = (TextView) findViewById(R.id.report_systemtime_get);

		bodyheight = (EditText) findViewById(R.id.report_height);
		bodyweight = (EditText) findViewById(R.id.report_weight);
		eyesight = (EditText) findViewById(R.id.report_eyesight);
		heartrate = (EditText) findViewById(R.id.report_heartrate);
		bloodpressure = (EditText) findViewById(R.id.report_bloodpressure);
		bloodsugar = (EditText) findViewById(R.id.report_bloodsugar);
		bloodlipid = (EditText) findViewById(R.id.report_bloodlipid);
		surgery = (EditText) findViewById(R.id.report_surgery);
		internal = (EditText) findViewById(R.id.report_internal);
		bloodroutine = (EditText) findViewById(R.id.report_bloodroutine);

		bodyheight_get = (EditText) findViewById(R.id.report_height_get);
		bodyweight_get = (EditText) findViewById(R.id.report_weight_get);
		eyesight_get = (EditText) findViewById(R.id.report_eyesight_get);
		heartrate_get = (EditText) findViewById(R.id.report_heartrate_get);
		bloodpressure_get = (EditText) findViewById(R.id.report_bloodpressure_get);
		bloodsugar_get = (EditText) findViewById(R.id.report_bloodsugar_get);
		bloodlipid_get = (EditText) findViewById(R.id.report_bloodlipid_get);
		surgery_get = (EditText) findViewById(R.id.report_surgery_get);
		internal_get = (EditText) findViewById(R.id.report_internal_get);
		bloodroutine_get = (EditText) findViewById(R.id.report_bloodroutine_get);

		bodyheight.setHint("put height");
		bodyweight.setHint("put weight:");
		eyesight.setHint("put eyesight");
		heartrate.setHint("put heart rate(integer)");
		bloodpressure.setHint("put blood pressure");
		bloodsugar.setHint("put blood sugar");
		bloodlipid.setHint("put blood lipid");
		surgery.setHint("surgery problem: if no, leave blank");
		internal.setHint("internal problem:if no, leave blank");
		bloodroutine.setHint("blood routine problem: if no, leave blank");

		toaddbtn = (Button) findViewById(R.id.healthreport_toaddbtn);
		addbtn = (Button) findViewById(R.id.healthreport_addbtn);
		clearbtn = (Button) findViewById(R.id.healthreport_clearbtn);
		barcodebtn = (Button) findViewById(R.id.healthreport_barcodebtn);
		updatebtn = (Button) findViewById(R.id.healthreport_updatebtn);
		deletebtn = (Button) findViewById(R.id.healthreport_deletebtn);
		returnbtn1 = (Button) findViewById(R.id.healthreport_returnlist1);
		returnbtn2 = (Button) findViewById(R.id.healthreport_returnlist2);
		returnadd = (Button) findViewById(R.id.healthreport_returnadd);

		toaddbtn.setOnClickListener(new ButtonListener());
		addbtn.setOnClickListener(new ButtonListener());
		clearbtn.setOnClickListener(new ButtonListener());
		barcodebtn.setOnClickListener(new ButtonListener());
		updatebtn.setOnClickListener(new ButtonListener());
		deletebtn.setOnClickListener(new ButtonListener());
		returnbtn1.setOnClickListener(new ButtonListener());
		returnbtn2.setOnClickListener(new ButtonListener());
		returnadd.setOnClickListener(new ButtonListener());
		myTabhost = this.getTabHost();
		intent_camera = new Intent(this, CaptureActivity.class);
		intent_camera.putExtra("from_where", 1);
		myTabhost.addTab(myTabhost.newTabSpec("tab1")// make a new Tab
				.setIndicator("body examination report", null)
				// set the Title and Icon
				.setContent(R.id.healthreportlist_tab));
		// set the layout

		myTabhost.addTab(myTabhost.newTabSpec("tab2")// make a new Tab
				.setIndicator("add body examination report", null)
				// set the Title and Icon
				.setContent(R.id.healthreportadd_tab));
		// set the layout

		myTabhost.addTab(myTabhost.newTabSpec("tab3")// make a new Tab
				.setIndicator("QR code scan", null)
				// set the Title and Icon
				.setContent(intent_camera));
		// set the layout
		myTabhost.addTab(myTabhost.newTabSpec("tab4")// make a new Tab
				.setIndicator("modify body examination report", null)
				// set the Title and Icon
				.setContent(R.id.healthreportupdate_tab));
		// set the layout
		healthreportlist_show();
	}

	// Listener
	class ButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.healthreport_toaddbtn) {
				myTabhost.setCurrentTabByTag("tab2");
			} else if (v.getId() == R.id.healthreport_barcodebtn) {
				myTabhost.setCurrentTabByTag("tab3");
			} else if (v.getId() == R.id.healthreport_clearbtn) {
				bodyheight.setText(null);
				bodyweight.setText(null);
				eyesight.setText(null);
				heartrate.setText(null);
				bloodsugar.setText(null);
				bloodlipid.setText(null);
				bloodpressure.setText(null);
				surgery.setText(null);
				internal.setText(null);
				bloodroutine.setText(null);
			} else if (v.getId() == R.id.healthreport_addbtn) {

				String heightStr = bodyheight.getText().toString().trim();
				String weightStr = bodyweight.getText().toString().trim();
				String eyesightStr = eyesight.getText().toString().trim();
				String heartrateStr = heartrate.getText().toString().trim();
				String bloodpressureStr = bloodpressure.getText().toString().trim();
				String bloodsugarStr = bloodsugar.getText().toString().trim();
				String bloodlipidStr = bloodlipid.getText().toString().trim();

				if (heightStr.equals("") || heightStr.equals(".") || heightStr.equals(null) || weightStr.equals("")
						|| weightStr.equals(".") || weightStr.equals(null) || eyesightStr.equals("")
						|| eyesightStr.equals(".") || eyesightStr.equals(null) || heartrateStr.equals("")
						|| heartrateStr.equals(".") || heartrateStr.equals(null) || bloodpressureStr.equals("")
						|| bloodpressureStr.equals(".") || bloodpressureStr.equals(null) || bloodsugarStr.equals("")
						|| bloodsugarStr.equals(".") || bloodsugarStr.equals(null) || bloodlipidStr.equals("")
						|| bloodlipidStr.equals(".") || bloodlipidStr.equals(null)) {
					Toast.makeText(HealthReport.this, "put correct data", Toast.LENGTH_SHORT).show();
				} else {
					dbinsert();
					MhmsLog.mhmslog(HealthReport.this, MhmsLog.HealthReport_CREATE, "added a health report");
					AlertDialog.Builder returnbuilder = new Builder(HealthReport.this);
					returnbuilder.setMessage("saved");
					returnbuilder.setTitle("hint");
					returnbuilder.setPositiveButton("submit", new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							myTabhost.setCurrentTabByTag("tab1");
							healthreportlist_show();
							dialog.dismiss();
						}
					});
					returnbuilder.setNegativeButton("cancel", new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					returnbuilder.create().show();
				}
			} else if (v.getId() == R.id.healthreport_updatebtn) {
				String heightStr = bodyheight_get.getText().toString().trim();
				String weightStr = bodyweight_get.getText().toString().trim();
				String eyesightStr = eyesight_get.getText().toString().trim();
				String heartrateStr = heartrate_get.getText().toString().trim();
				String bloodpressureStr = bloodpressure_get.getText().toString().trim();
				String bloodsugarStr = bloodsugar_get.getText().toString().trim();
				String bloodlipidStr = bloodlipid_get.getText().toString().trim();

				if (heightStr.equals("") || heightStr.equals(".") || heightStr.equals(null) || weightStr.equals("")
						|| weightStr.equals(".") || weightStr.equals(null) || eyesightStr.equals("")
						|| eyesightStr.equals(".") || eyesightStr.equals(null) || heartrateStr.equals("")
						|| heartrateStr.equals(".") || heartrateStr.equals(null) || bloodpressureStr.equals("")
						|| bloodpressureStr.equals(".") || bloodpressureStr.equals(null) || bloodsugarStr.equals("")
						|| bloodsugarStr.equals(".") || bloodsugarStr.equals(null) || bloodlipidStr.equals("")
						|| bloodlipidStr.equals(".") || bloodlipidStr.equals(null)) {
					Toast.makeText(HealthReport.this, "put correct data", Toast.LENGTH_SHORT).show();
				} else {
					dbupdate();
					AlertDialog.Builder returnbuilder = new Builder(HealthReport.this);
					returnbuilder.setMessage("modified");
					returnbuilder.setTitle("hint");
					returnbuilder.setPositiveButton("submit", new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							myTabhost.setCurrentTabByTag("tab1");
							healthreportlist_show();
							dialog.dismiss();
						}
					});
					returnbuilder.setNegativeButton("cancel", new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					returnbuilder.create().show();
				}

			} else if (v.getId() == R.id.healthreport_deletebtn) {
				dbdelete();
				Toast.makeText(HealthReport.this, "deleted", Toast.LENGTH_SHORT).show();
			} else if (v.getId() == R.id.healthreport_returnlist1) {
				myTabhost.setCurrentTabByTag("tab1");
				healthreportlist_show();
			} else if (v.getId() == R.id.healthreport_returnlist2) {
				myTabhost.setCurrentTabByTag("tab1");
				healthreportlist_show();
			} else if (v.getId() == R.id.healthreport_returnadd) {
				myTabhost.setCurrentTabByTag("tab2");
			}

		}

	}

	// query database, show list
	private void healthreportlist_show() {
		DatabaseHelper dbHelper = new DatabaseHelper(HealthReport.this, "mhms_db");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		lists = new ArrayList<HashMap<String, Object>>();
		Cursor cursor = db.query("healthreport", new String[] { "id,systemtime" }, null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			HashMap<String, Object> listmap = new HashMap<String, Object>();
			listmap.put("id", cursor.getString(cursor.getColumnIndex("id")));
			listmap.put("systemtime", cursor.getString(cursor.getColumnIndex("systemtime")));
			listmap.put("action", "view");
			lists.add(listmap);
		}
		int cursorcount = cursor.getCount();
		SimpleAdapter adapter = new SimpleAdapter(HealthReport.this, lists, R.layout.search_show_list,
				new String[] { "id", "systemtime", "action" },
				new int[] { R.id.query_id, R.id.query_time, R.id.query_action });
		ListView listview1 = (ListView) findViewById(R.id.healthreport_list);
		listview1.setAdapter(adapter);
		cursor.close();
		db.close();
		listview1.setOnItemClickListener(new ListItemlistener());

		// rows of the table; 0: none; >0, show health report
		if (cursorcount > 0) {
			healthreportlist_title.setText("health report");
		} else {
			healthreportlist_title.setText("not yet");
		}
	}

	// ItemListener
	class ListItemlistener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			// TODO Auto-generated method stub
			String id_click = (String) lists.get(arg2).get("id");
			DatabaseHelper dbHelper = new DatabaseHelper(HealthReport.this, "mhms_db");
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor cursor = db.query("healthreport",
					new String[] { "id,systemtime,bodyheight,bodyweight,eyesight,heartrate,"
							+ "bloodpressure,bloodsugar,bloodlipid,surgery,internal,bloodroutine" },
					"id=?", new String[] { id_click }, null, null, null, null);
			cursor.moveToNext();
			String systemtime_send = cursor.getString(cursor.getColumnIndex("systemtime"));
			System.out.println(systemtime_send);
			String bodyheight_send = cursor.getString(cursor.getColumnIndex("bodyheight"));
			String bodyweight_send = cursor.getString(cursor.getColumnIndex("bodyweight"));
			String eyesight_send = cursor.getString(cursor.getColumnIndex("eyesight"));
			String heartrate_send = cursor.getString(cursor.getColumnIndex("heartrate"));
			String bloodpressure_send = cursor.getString(cursor.getColumnIndex("bloodpressure"));
			String bloodsugar_send = cursor.getString(cursor.getColumnIndex("bloodsugar"));
			String bloodlipid_send = cursor.getString(cursor.getColumnIndex("bloodlipid"));
			String surgery_send = cursor.getString(cursor.getColumnIndex("surgery"));
			String internal_send = cursor.getString(cursor.getColumnIndex("internal"));
			String bloodroutine_send = cursor.getString(cursor.getColumnIndex("bloodroutine"));
			cursor.close();
			db.close();
			myTabhost.setCurrentTabByTag("tab4");
			id_get.setText(id_click);
			systemtime_get.setText(systemtime_send);
			bodyheight_get.setText(bodyheight_send);
			bodyweight_get.setText(bodyweight_send);
			eyesight_get.setText(eyesight_send);
			heartrate_get.setText(heartrate_send);
			bloodpressure_get.setText(bloodpressure_send);
			bloodsugar_get.setText(bloodsugar_send);
			bloodlipid_get.setText(bloodlipid_send);
			surgery_get.setText(surgery_send);
			internal_get.setText(internal_send);
			bloodroutine_get.setText(bloodroutine_send);
		}
	}

	// insert
	private void dbinsert() {

		DatabaseHelper dbHelper = new DatabaseHelper(HealthReport.this, "mhms_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SimpleDateFormat sTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String systemtime = sTime.format(new java.util.Date());
		// get data from input text
		float heightFloat = Float.parseFloat(bodyheight.getText().toString().trim());
		float weightFloat = Float.parseFloat(bodyweight.getText().toString().trim());
		float eyesightFloat = Float.parseFloat(eyesight.getText().toString().trim());
		int heartrateInt = Integer.parseInt(heartrate.getText().toString().trim());
		float bloodpressureFloat = Float.parseFloat(bloodpressure.getText().toString().trim());
		float bloodsugarFloat = Float.parseFloat(bloodsugar.getText().toString().trim());
		float bloodlipidFloat = Float.parseFloat(bloodlipid.getText().toString().trim());
		String surgeryStr = surgery.getText().toString().trim();
		String internalStr = internal.getText().toString().trim();
		String bloodroutineStr = bloodroutine.getText().toString().trim();

		// insert data
		ContentValues values = new ContentValues();
		ContentValues values_bmi = new ContentValues();
		ContentValues values_eyesight = new ContentValues();
		ContentValues values_heartrate = new ContentValues();
		values.put("systemtime", systemtime);
		values.put("bodyheight", heightFloat);
		values.put("bodyweight", weightFloat);
		values.put("eyesight", eyesightFloat);
		values.put("heartrate", heartrateInt);
		values.put("bloodpressure", bloodpressureFloat);
		values.put("bloodsugar", bloodsugarFloat);
		values.put("bloodlipid", bloodlipidFloat);
		values.put("surgery", surgeryStr);
		values.put("internal", internalStr);
		values.put("bloodroutine", bloodroutineStr);
		db.insert("healthreport", null, values);

		values_bmi.put("systemtime", systemtime);
		values_bmi.put("bodyheight", heightFloat);
		values_bmi.put("bodyweight", weightFloat);
		values_bmi.put("bmi", weightFloat * 10000 / (heightFloat * heightFloat));
		db.insert("bmitable", null, values_bmi);

		values_eyesight.put("systemtime", systemtime);
		values_eyesight.put("eyesight", (double) eyesightFloat);
		db.insert("eyesighttable", null, values_eyesight);

		values_heartrate.put("systemtime", systemtime);
		values_heartrate.put("heartrate", heartrateInt);
		db.insert("heartratetable", null, values_heartrate);

		db.close();
	}

	// update database
	private void dbupdate() {
		DatabaseHelper dbHelper = new DatabaseHelper(HealthReport.this, "mhms_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String idStr = id_get.getText().toString();
		float heightFloat = Float.parseFloat(bodyheight_get.getText().toString().trim());
		float weightFloat = Float.parseFloat(bodyweight_get.getText().toString().trim());
		float eyesightFloat = Float.parseFloat(eyesight_get.getText().toString().trim());
		int heartrateInt = Integer.parseInt(heartrate_get.getText().toString().trim());
		float bloodpressureFloat = Float.parseFloat(bloodpressure_get.getText().toString().trim());
		float bloodsugarFloat = Float.parseFloat(bloodsugar_get.getText().toString().trim());
		float bloodlipidFloat = Float.parseFloat(bloodlipid_get.getText().toString().trim());
		String surgeryStr = surgery_get.getText().toString().trim();
		String internalStr = internal_get.getText().toString().trim();
		String bloodroutineStr = bloodroutine_get.getText().toString().trim();

		ContentValues values = new ContentValues();
		values.put("bodyheight", heightFloat);
		values.put("bodyweight", weightFloat);
		values.put("eyesight", eyesightFloat);
		values.put("heartrate", heartrateInt);
		values.put("bloodpressure", bloodpressureFloat);
		values.put("bloodsugar", bloodsugarFloat);
		values.put("bloodlipid", bloodlipidFloat);
		values.put("surgery", surgeryStr);
		values.put("internal", internalStr);
		values.put("bloodroutine", bloodroutineStr);
		db.update("healthreport", values, "id=?", new String[] { idStr });
		db.close();
	}

	// delete database
	private void dbdelete() {
		DatabaseHelper dbHelper = new DatabaseHelper(HealthReport.this, "mhms_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String idStr = id_get.getText().toString();
		db.delete("healthreport", "id= ?", new String[] { idStr });
		db.close();
	}

	/**
	 * create MENU
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");// must create one
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	/**
	 * prevent MENU
	 */
	public boolean onMenuOpened(int featureId, Menu menu) {
		AlertDialog menudialog;
		View menuview;
		MenuAction addmenu = new MenuAction(HealthReport.this);
		addmenu.menucreate();
		menudialog = addmenu.menuDialog;
		menuview = addmenu.menuView;
		if (menudialog == null) {
			menudialog = new AlertDialog.Builder(this).setView(menuview).show();
		} else {
			menudialog.show();
		}

		return false;// true, show menu
	}
	// back; tag:tab1, quit?;tag:others, back to tab1

	public void onBackPressed() {
		if (myTabhost.getCurrentTabTag() == "tab2" || myTabhost.getCurrentTabTag() == "tab3"
				|| myTabhost.getCurrentTabTag() == "tab4") {
			myTabhost.setCurrentTabByTag("tab1");
			healthreportlist_show();
		} else {
			myTabhost.setCurrentTabByTag("tab1");
			healthreportlist_show();
			// Intent intent =new Intent(this,Main.class);
			// startActivity(intent);
		}
	}

}
