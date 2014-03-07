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

public class HealthReport_Camera extends Activity{
	private TextView healthreport_camera_result;
	private Button save_to_healthreport;
	private Button back_to_healthreport;
	private String heathreport_camera_get;
	float heightFloat=0,weightFloat=0,eyesightFloat=0,bloodpressureFloat=0,bloodsugarFloat=0,bloodlipidFloat=0;
	int heartrateInt=0;
	String surgeryStr=null,internalStr=null,bloodroutineStr=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.healthreport_camera);
				
		healthreport_camera_result = (TextView)findViewById(R.id.healthreport_camera_result);
		save_to_healthreport=(Button)findViewById(R.id.save_to_healthreport);
		back_to_healthreport=(Button)findViewById(R.id.back_to_healthreport);
		Intent intent =getIntent();
		heathreport_camera_get=intent.getStringExtra("healthreport_camera_send");

		
		save_to_healthreport.setText("save");
		back_to_healthreport.setText("return");
		parse_heathreport_camera_get();
		
		save_to_healthreport.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dbinsert();
				MhmsLog.mhmslog(HealthReport_Camera.this, MhmsLog.HealthReport_CREATE, "add QR code health report");
				Toast.makeText(HealthReport_Camera.this, "saved", Toast.LENGTH_SHORT).show();
				}});
		back_to_healthreport.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HealthReport_Camera.this.finish();
				Intent intent =new Intent(HealthReport_Camera.this,HealthRecords.class);
				startActivity(intent);
				HealthReport_Camera.this.finish();
				}});
	}
	
	private String jsonparse(String key){
		String keyStr;
		try{JSONObject jsonobj = new JSONObject(heathreport_camera_get);
		keyStr=jsonobj.getString(key);
		}
		catch (JSONException e){keyStr="0";}
		return keyStr;
	}
	
    private void parse_heathreport_camera_get(){ 
			 heightFloat = Float.parseFloat(jsonparse("bodyheight"));
			 weightFloat = Float.parseFloat(jsonparse("bodyweight"));
			 eyesightFloat = Float.parseFloat(jsonparse("eyesight"));
			 heartrateInt = Integer.parseInt(jsonparse("heartrate"));
			 bloodpressureFloat = Float.parseFloat(jsonparse("bloodpressure"));
			 bloodsugarFloat = Float.parseFloat(jsonparse("bloodsugar"));
			 bloodlipidFloat = Float.parseFloat(jsonparse("bloodlipid"));
			 surgeryStr=jsonparse("surgery");
			 internalStr = jsonparse("internal");
			 bloodroutineStr = jsonparse("bloodroutine");
			
		   healthreport_camera_result.setText("height:"+heightFloat+"\n"+"weight:"+weightFloat+"\n"+"eyesight:"+eyesightFloat+"\n"+"heartrate:"+heartrateInt+"\n"+
				   "Ѫѹ:"+bloodpressureFloat+"\n"+"blood sugar:"+bloodsugarFloat+"\n"+"blood lipid:"+bloodlipidFloat+"\n"+"surgery problem:"+surgeryStr+"\n"+"internal problem:"+internalStr
				   +"\n"+"blood routine problem:"+bloodroutineStr);
    }
	private void dbinsert() {		
		// TODO Auto-generated method stub
		DatabaseHelper dbHelper = new DatabaseHelper(HealthReport_Camera.this,"mhms_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SimpleDateFormat sTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String systemtime = sTime.format(new java.util.Date());
		
			//insert data
			ContentValues values = new ContentValues();
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
	MenuAction addmenu = new MenuAction(HealthReport_Camera.this);
	addmenu.menucreate();
	menudialog = addmenu.menuDialog;
	menuview = addmenu.menuView;
	if (menudialog == null) {
		menudialog = new AlertDialog.Builder(this).setView(menuview).show();
	} else {
		menudialog.show();
	}

	return false;// ture, show menu
}
}
