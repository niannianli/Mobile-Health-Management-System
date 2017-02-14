package mhms.healthrecords;

import java.text.SimpleDateFormat;

import mhms.db.DatabaseHelper;
import mhms.healthrecords.R;
import mhms.log.MhmsLog;
import mhms.menu.MenuAction;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.*;

public class MedicineRecord_Camera extends Activity {
	private TextView medicinerecord_camera_result;
	private Button save_to_medicinerecord;
	private Button back_to_medicinerecord;
	private String medicinerecord_camera_get;
	String illname = null, illbegin = null, illend = null, pillusing = null, illdescribe = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.medicinerecord_camera);

		medicinerecord_camera_result = (TextView) findViewById(R.id.medicinerecord_camera_result);
		save_to_medicinerecord = (Button) findViewById(R.id.save_to_medicinerecord);
		back_to_medicinerecord = (Button) findViewById(R.id.back_to_medicinerecord);
		Intent intent = getIntent();
		medicinerecord_camera_get = intent.getStringExtra("medicinerecord_camera_send");

		save_to_medicinerecord.setText("save");
		back_to_medicinerecord.setText("back");
		parse_medicinerecord_camera_get();

		save_to_medicinerecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dbinsert();
				MhmsLog.mhmslog(MedicineRecord_Camera.this, MhmsLog.MedicineRecord_CREATE,
						"add QR code medicine record");
				Toast.makeText(MedicineRecord_Camera.this, "saved", Toast.LENGTH_SHORT).show();
			}
		});
		back_to_medicinerecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MedicineRecord_Camera.this.finish();
				Intent intent = new Intent(MedicineRecord_Camera.this, HealthRecords.class);
				startActivity(intent);
				MedicineRecord_Camera.this.finish();
			}
		});
	}

	private String jsonparse(String key) {
		String keyStr;
		try {
			JSONObject jsonobj = new JSONObject(medicinerecord_camera_get);
			keyStr = jsonobj.getString(key);
		} catch (JSONException e) {
			keyStr = null;
		}
		return keyStr;
	}

	private void parse_medicinerecord_camera_get() {

		illname = jsonparse("ill_name");
		illbegin = jsonparse("ill_begin");
		illend = jsonparse("ill_end");
		pillusing = jsonparse("pill_using");
		illdescribe = jsonparse("ill_describe");

		medicinerecord_camera_result
				.setText("disease name:" + illname + "\n" + "begin date:" + illbegin + "\n" + "end date:" + illend
						+ "\n" + "medicine record:" + pillusing + "\n" + "disease description:" + illdescribe);
	}

	private void dbinsert() {
		// TODO Auto-generated method stub
		DatabaseHelper dbHelper = new DatabaseHelper(MedicineRecord_Camera.this, "mhms_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SimpleDateFormat sTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String systemtime = sTime.format(new java.util.Date());

		// insert data to database
		ContentValues values = new ContentValues();
		values.put("systemtime", systemtime);
		values.put("ill_name", illname);
		values.put("ill_begin", illbegin);
		values.put("ill_end", illend);
		values.put("pill_using", pillusing);
		values.put("ill_describe", illdescribe);
		db.insert("medicinerecord", null, values);
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
		AlertDialog menudialog;// menu dialog
		View menuview;// view of menu
		MenuAction addmenu = new MenuAction(MedicineRecord_Camera.this);// create
																		// object
																		// of
																		// menu
		addmenu.menucreate();// get method to create menu
		menudialog = addmenu.menuDialog;// get menu dialog
		menuview = addmenu.menuView;// get view of menu
		if (menudialog == null) {
			menudialog = new AlertDialog.Builder(this).setView(menuview).show();
		} else {
			menudialog.show();
		}

		return false;// get true, show menu
	}
}
