package mhms.healthrecords;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.Zxing.CaptureActivity;

import mhms.healthrecords.R;
import mhms.db.DatabaseHelper;
import mhms.log.MhmsLog;
import mhms.menu.MenuAction;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MedicineRecord extends TabActivity {
	
	private TextView medicinelist_title;
	private TextView medicine_systemtime_get;
	private TextView medicine_id_get;
	
	private EditText medicine_illname;
	private EditText medicine_illbegin;
	private EditText medicine_illend;
	private EditText medicine_pillusing;
	private EditText medicine_illdescribe;
	private EditText medicine_illname_get;
	private EditText medicine_illbegin_get;
	private EditText medicine_illend_get;
	private EditText medicine_pillusing_get;
	private EditText medicine_illdescribe_get;

	private Button medicine_toaddbtn;
	private Button medicine_addbtn;
	private Button medicine_clearbtn;
	private Button medicine_returnlist1;
	private Button medicine_barcodebtn;
	private Button medicine_updatebtn;
	private Button medicine_deletebtn;
	private Button medicine_returnlist2;
	private Button medicine_returnadd;
	private Button illbegin_selectbtn;
	private Button illend_selectbtn;
	private Button illbegin_get_selectbtn;
	private Button illend_get_selectbtn;

	private List<HashMap<String, Object>> lists;

	public static TabHost myTabhost;
	protected int myMenuSettingTag = 0;
    
	private int mYear,mMonth,mDay;
	private int datepickerid=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.medicinerecord);
		
		medicinelist_title = (TextView)findViewById(R.id.medicinelist_title);
		medicine_id_get = (TextView)findViewById(R.id.medicine_id_get);
		medicine_systemtime_get = (TextView)findViewById(R.id.medicine_systemtime_get);
		
		medicine_illname = (EditText) findViewById(R.id.medicine_illname);
		medicine_illbegin = (EditText) findViewById(R.id.medicine_illbegin);
		medicine_illend = (EditText) findViewById(R.id.medicine_illend);
		medicine_pillusing = (EditText) findViewById(R.id.medicine_pillusing);
		medicine_illdescribe = (EditText) findViewById(R.id.medicine_illdescribe);

		medicine_illname_get = (EditText) findViewById(R.id.medicine_illname_get);
		medicine_illbegin_get = (EditText) findViewById(R.id.medicine_illbegin_get);
		medicine_illend_get = (EditText) findViewById(R.id.medicine_illend_get);
		medicine_pillusing_get = (EditText) findViewById(R.id.medicine_pillusing_get);
		medicine_illdescribe_get = (EditText) findViewById(R.id.medicine_illdescribe_get);
        
		medicine_illname.setHint("put disease name");medicine_illbegin.setHint("put disease begin date");
		medicine_illend.setHint("put disease end date");medicine_pillusing.setHint("put medicine record");
		medicine_illdescribe.setHint("describe disease");		
		
		medicine_toaddbtn = (Button) findViewById(R.id.medicine_toaddbtn);
		medicine_addbtn = (Button) findViewById(R.id.medicine_addbtn);
		medicine_clearbtn = (Button) findViewById(R.id.medicine_clearbtn);
		medicine_returnlist1 = (Button) findViewById(R.id.medicine_returnlist1);
		medicine_barcodebtn = (Button) findViewById(R.id.medicine_barcodebtn);
		medicine_updatebtn = (Button) findViewById(R.id.medicine_updatebtn);
		medicine_deletebtn = (Button) findViewById(R.id.medicine_deletebtn);
		medicine_returnlist2 = (Button) findViewById(R.id.medicine_returnlist2);
		medicine_returnadd = (Button) findViewById(R.id.medicine_returnadd);
		
		illbegin_selectbtn= (Button) findViewById(R.id.illbegin_selectbtn);
		illend_selectbtn=(Button) findViewById(R.id.illend_selectbtn);
		illbegin_get_selectbtn=(Button) findViewById(R.id.illbegin_get_selectbtn);
		illend_get_selectbtn=(Button) findViewById(R.id.illend_get_selectbtn);
		
		medicine_toaddbtn.setOnClickListener(new ButtonListener());
		medicine_addbtn.setOnClickListener(new ButtonListener());
		medicine_clearbtn.setOnClickListener(new ButtonListener());
		medicine_returnlist1.setOnClickListener(new ButtonListener());
		medicine_barcodebtn.setOnClickListener(new ButtonListener());
		medicine_updatebtn.setOnClickListener(new ButtonListener());
		medicine_deletebtn.setOnClickListener(new ButtonListener());
		medicine_returnlist2.setOnClickListener(new ButtonListener());
		medicine_returnadd.setOnClickListener(new ButtonListener());
		illbegin_selectbtn.setOnClickListener(new ButtonListener());
		illend_selectbtn.setOnClickListener(new ButtonListener());
		illbegin_get_selectbtn.setOnClickListener(new ButtonListener());
		illend_get_selectbtn.setOnClickListener(new ButtonListener());

		myTabhost = this.getTabHost();
		Intent intent_camera = new Intent(this, CaptureActivity.class);
		intent_camera.putExtra("from_where", 2);
		myTabhost.addTab(myTabhost.newTabSpec("tab1")// make a new Tab
				.setIndicator("medicine record list", null)
				// set the Title and Icon
				.setContent(R.id.medicinerecord_tab1));
		// set the layout

		myTabhost.addTab(myTabhost.newTabSpec("tab2")// make a new Tab
				.setIndicator("add record", null)
				// set the Title and Icon
				.setContent(R.id.medicinerecord_tab2));
		// set the layout

		myTabhost.addTab(myTabhost.newTabSpec("tab3")// make a new Tab
				.setIndicator("QRcode scan", null)
				// set the Title and Icon
				.setContent(intent_camera));
		// set the layout
		myTabhost.addTab(myTabhost.newTabSpec("tab4")// make a new Tab
				.setIndicator("modify record", null)
				// set the Title and Icon
				.setContent(R.id.medicinerecord_tab4));
		// set the layout
		medicinelist_show();
	}
	

	//ButtonListener
	class ButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.medicine_toaddbtn) {
				myTabhost.setCurrentTabByTag("tab2");
			} else if (v.getId() == R.id.medicine_addbtn) {
					dbinsert();
					MhmsLog.mhmslog(MedicineRecord.this, MhmsLog.MedicineRecord_CREATE, "added a medicine record");				
						AlertDialog.Builder returnbuilder = new Builder(
						MedicineRecord.this);
				returnbuilder.setMessage("save successfully");
				returnbuilder.setTitle("hint");
				returnbuilder.setPositiveButton("submit",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								myTabhost.setCurrentTabByTag("tab1");
								medicinelist_show();
								dialog.dismiss();
							}
						});
				returnbuilder.setNegativeButton("cancel",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				returnbuilder.create().show();
			} else if (v.getId() == R.id.medicine_clearbtn) {
				medicine_illname.setText(null);
				medicine_illbegin.setText(null);
				medicine_illend.setText(null);
				medicine_pillusing.setText(null);
				medicine_illdescribe.setText(null);

			} else if (v.getId() == R.id.medicine_returnlist1) {
				myTabhost.setCurrentTabByTag("tab1");
				medicinelist_show();
			} else if (v.getId() == R.id.medicine_barcodebtn) {
				myTabhost.setCurrentTabByTag("tab3");
			} else if (v.getId() == R.id.medicine_updatebtn) {
                dbupdate();
                AlertDialog.Builder returnbuilder = new Builder(
						MedicineRecord.this);
				returnbuilder.setMessage("modify successfully");
				returnbuilder.setTitle("hint");
				returnbuilder.setPositiveButton("submit",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								myTabhost.setCurrentTabByTag("tab1");
								medicinelist_show();
								dialog.dismiss();
							}
						});
				returnbuilder.setNegativeButton("cancel",
						new android.content.DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						});
				returnbuilder.create().show();
			} else if (v.getId() == R.id.medicine_deletebtn) {
				dbdelete();
				Toast.makeText(MedicineRecord.this, "delete successfully", Toast.LENGTH_SHORT).show();
			} else if (v.getId() == R.id.medicine_returnlist2) {
				myTabhost.setCurrentTabByTag("tab1");
				medicinelist_show();
			} else if (v.getId() == R.id.medicine_returnadd) {
				myTabhost.setCurrentTabByTag("tab2");
			}
			else if (v.getId() == R.id.illbegin_selectbtn) {
				  datepickerid=1;
				  datepicker();
			}
			else if (v.getId() == R.id.illend_selectbtn) {
				datepickerid=2;
				datepicker();
			}
			else if (v.getId() == R.id.illbegin_get_selectbtn) {
				datepickerid=3;
				datepicker();
			}
			else if (v.getId() == R.id.illend_get_selectbtn) {
				datepickerid=4;
				datepicker();
			}
		}

	}
	//choose date
	   private void datepicker(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);  
        mMonth = c.get(Calendar.MONTH);  
        mDay = c.get(Calendar.DAY_OF_MONTH);
        showDialog(0);
        setDateTime();
        }
    	private void setDateTime(){
           final Calendar c = Calendar.getInstance();             
           mYear = c.get(Calendar.YEAR);  
           mMonth = c.get(Calendar.MONTH);  
           mDay = c.get(Calendar.DAY_OF_MONTH);      
           updateDateDisplay(datepickerid); 
    	}
    	private void updateDateDisplay(int id){
    		switch(id){
    	case 1:
    		medicine_illbegin.setText(new StringBuilder().append(mYear).append("-")
    	    		   .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
    	               .append((mDay < 10) ? "0" + mDay : mDay));
    		break;
    	case 2:
    		medicine_illend.setText(new StringBuilder().append(mYear).append("-")
 	    		   .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
 	               .append((mDay < 10) ? "0" + mDay : mDay));
    		break;
    	case 3:
    		medicine_illbegin_get.setText(new StringBuilder().append(mYear).append("-")
 	    		   .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
 	               .append((mDay < 10) ? "0" + mDay : mDay));
    		break;
    	case 4:
    		medicine_illend_get.setText(new StringBuilder().append(mYear).append("-")
 	    		   .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
 	               .append((mDay < 10) ? "0" + mDay : mDay));
    		break;
    		}   		
    		}
    	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {  
    	       public void onDateSet(DatePicker view, int year, int monthOfYear,  
    	              int dayOfMonth) {  
    	           mYear = year;  
    	           mMonth = monthOfYear;  
    	           mDay = dayOfMonth;  
    	           updateDateDisplay(datepickerid);
    	       }  
    	    };
    	    @Override  
    	    protected Dialog onCreateDialog(int id) {  
    	    	switch(id) {
    	    	case 0:
    	    		return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
    	    		}
				return null;} 	  
    	    @Override  
    	    protected void onPrepareDialog(int id, Dialog dialog){  
               switch(id){case 0:
    	           ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);}}
	// show query result
	private void medicinelist_show(){
		lists = new ArrayList<HashMap<String, Object>>();
		DatabaseHelper dbHelper = new DatabaseHelper(MedicineRecord.this,"mhms_db");
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("medicinerecord",
				new String[] { "id,systemtime" }, null, null, null, null, null,
				null);
		while (cursor.moveToNext()) {
			HashMap<String, Object> listmap = new HashMap<String, Object>();
			listmap.put("id", cursor.getString(cursor.getColumnIndex("id")));
			listmap.put("systemtime",
					cursor.getString(cursor.getColumnIndex("systemtime")));
			listmap.put("action", "view");
			lists.add(listmap);
		}
		int cursorcount = cursor.getCount();
		SimpleAdapter adapter = new SimpleAdapter(MedicineRecord.this, lists,
				R.layout.search_show_list, new String[] { "id", "systemtime",
						"action" }, new int[] { R.id.query_id,
						R.id.query_time, R.id.query_action });
		ListView listview1 = (ListView) findViewById(R.id.medicine_list);
		listview1.setAdapter(adapter);
		cursor.close();
		db.close();
		listview1.setOnItemClickListener(new ListItemlistener());
		// the rows in a list; 0: none;>0,show the list
		if (cursorcount > 0) {
			medicinelist_title.setText("medicine record");
		} else {
			medicinelist_title.setText("no medicine record");
		}
	}
	// Listener on every item in the list
	class ListItemlistener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			String id_click = (String) lists.get(arg2).get("id");
			DatabaseHelper dbHelper = new DatabaseHelper(MedicineRecord.this,"mhms_db");
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor cursor = db
					.query("medicinerecord",
							new String[] { "id,systemtime,ill_name,ill_begin,ill_end,pill_using,ill_describe"},
							"id=?", new String[] { id_click }, null, null,null, null);
			cursor.moveToNext();
			String systemtime_send = cursor.getString(cursor.getColumnIndex("systemtime"));
			String illname_send = cursor.getString(cursor.getColumnIndex("ill_name"));
			String illbegin_send = cursor.getString(cursor.getColumnIndex("ill_begin"));
			String illend_send = cursor.getString(cursor.getColumnIndex("ill_end"));
			String pillusing_send = cursor.getString(cursor.getColumnIndex("pill_using"));
			String illdescribe_send = cursor.getString(cursor.getColumnIndex("ill_describe"));
			cursor.close();
			db.close();
			myTabhost.setCurrentTabByTag("tab4");
			medicine_id_get.setText(id_click);
			medicine_systemtime_get.setText(systemtime_send);
			medicine_illname_get.setText(illname_send);
			medicine_illbegin_get.setText(illbegin_send);
			medicine_illend_get.setText(illend_send);
			medicine_pillusing_get.setText(pillusing_send);
			medicine_illdescribe_get.setText(illdescribe_send);
		}
	}
	
    //insert into database
	private void dbinsert(){
		DatabaseHelper dbHelper = new DatabaseHelper(MedicineRecord.this,"mhms_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SimpleDateFormat sTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    String systemtime = sTime.format(new java.util.Date());	
	
		String illname = medicine_illname.getText().toString().trim();
		String illbegin = medicine_illbegin.getText().toString().trim();
		String illend = medicine_illend.getText().toString().trim();
		String pillusing = medicine_pillusing.getText().toString().trim();
		String illdescribe = medicine_illdescribe.getText().toString().trim();
		//  insert data to database
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
 //update database
	private void dbupdate(){
		DatabaseHelper dbHelper = new DatabaseHelper(MedicineRecord.this,"mhms_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String idStr = medicine_id_get.getText().toString();
		String illname = medicine_illname_get.getText().toString().trim();
		String illbegin = medicine_illbegin_get.getText().toString().trim();
		String illend = medicine_illend_get.getText().toString().trim();
		String pillusing = medicine_pillusing_get.getText().toString().trim();
		String illdescribe = medicine_illdescribe_get.getText().toString().trim();
		ContentValues values = new ContentValues();
		values.put("ill_name", illname);
		values.put("ill_begin", illbegin);
		values.put("ill_end", illend);
		values.put("pill_using", pillusing);
		values.put("ill_describe", illdescribe);
		db.update("medicinerecord", values, "id=?", new String[] { idStr });
		db.close();
	}
	
 //delete data
	private void dbdelete(){
		DatabaseHelper dbHelper = new DatabaseHelper(MedicineRecord.this,"mhms_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String idStr = medicine_id_get.getText().toString();
		db.delete("medicinerecord", "id= ?", new String[]{idStr});
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
		View menuview;// view of the menu
		MenuAction addmenu = new MenuAction(MedicineRecord.this);// create the object of menu
		addmenu.menucreate();//  get create method of menu
		menudialog = addmenu.menuDialog;// get menu dialog
		menuview = addmenu.menuView;// show the view of menu
		if (menudialog == null) {
			menudialog = new AlertDialog.Builder(this).setView(menuview).show();
		} else {
			menudialog.show();
		}

		return false;//return true, show the menu
	}

	//get back button;tab1:quit?; others: back to tab1
	public void onBackPressed() {
		if (myTabhost.getCurrentTabTag() == "tab2"
				|| myTabhost.getCurrentTabTag() == "tab3"
				|| myTabhost.getCurrentTabTag() == "tab4") {
			myTabhost.setCurrentTabByTag("tab1");
			medicinelist_show();
		} else {
			myTabhost.setCurrentTabByTag("tab1");
			medicinelist_show();
			// Intent intent =new Intent(this,Main.class);
			// startActivity(intent);
		}
	}
}
