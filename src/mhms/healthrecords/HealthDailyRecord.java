package mhms.healthrecords;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import mhms.db.DatabaseHelper;
import mhms.menu.MenuAction;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

public class HealthDailyRecord extends TabActivity {
	private TabHost myTabhost;
	protected int myMenuSettingTag = 0;
	private EditText healthdaily_date_text;
	private EditText healthdaily_text;
	private Button healthdaily_selectbtn;
	private Button healthdaily_toall;
	private Button healthdaily_back;
	private TextView healthdaily_search_status;
	private TextView healthdaily_all_status;
	
	private int mYear,mMonth,mDay;
	private String dateSelect;
	
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healthdailyrecord);
        
        healthdaily_date_text=(EditText)findViewById(R.id.healthdaily_date_text);
        healthdaily_text=(EditText)findViewById(R.id.healthdaily_text);
        healthdaily_selectbtn=(Button)findViewById(R.id.healthdaily_selectbtn);
        healthdaily_toall=(Button)findViewById(R.id.healthdaily_toall);
        healthdaily_back=(Button)findViewById(R.id.healthdaily_back);
        healthdaily_selectbtn.setOnClickListener(new ButtonListener());
        healthdaily_toall.setOnClickListener(new ButtonListener());
        healthdaily_back.setOnClickListener(new ButtonListener());
        
        healthdaily_search_status=(TextView)findViewById(R.id.healthdaily_search_status);
        healthdaily_all_status=(TextView)findViewById(R.id.healthdaily_all_status);
        healthdaily_search_status.setVisibility(TextView.GONE);
        healthdaily_all_status.setVisibility(TextView.GONE);
        
        healthdaily_date_text.setHint("query as date");
        healthdaily_text.setHint("query as event name");
        
        healthdaily_date_text.addTextChangedListener(new EditTextListener1());
        healthdaily_text.addTextChangedListener(new EditTextListener2());
        
        myTabhost = this.getTabHost();
		myTabhost.addTab(myTabhost.newTabSpec("tab1")// make a new Tab
				.setIndicator("choose health event", null)
				// set the Title and Icon
				.setContent(R.id.healthdaily_tab1));
		// set the layout

		myTabhost.addTab(myTabhost.newTabSpec("tab2")// make a new Tab
				.setIndicator("health event daily", null)
				// set the Title and Icon
				.setContent(R.id.healthdaily_tab2));
		// set the layout
    }
    
    class ButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.healthdaily_selectbtn) {
				datepicker();
			} 
			else if(v.getId() == R.id.healthdaily_toall){
				healthdailyrecord_list_show();
				myTabhost.setCurrentTabByTag("tab2");
				
			}
			else if(v.getId() == R.id.healthdaily_back){
				myTabhost.setCurrentTabByTag("tab1");
			}
		}
    }
    

    //date input text
	class EditTextListener1 implements TextWatcher{
		public void afterTextChanged(Editable s) {
			dateSelect = "%"+s.toString()+"%";
			healthdailyrecord_select_show();        
		}
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	//log content input 
	class EditTextListener2 implements TextWatcher{
		public void afterTextChanged(Editable s) {
			String logstring =   "%"+s.toString()+"%";  
			 DatabaseHelper dbHelper =new DatabaseHelper(HealthDailyRecord.this, "mhms_db");
			  SQLiteDatabase db =dbHelper.getReadableDatabase();
			  List<HashMap<String, Object>> lists = new ArrayList<HashMap<String, Object>>();
			  Cursor cursor = db.query("mhmslog",new String[]{"id,systemtime,log"}, "log like ?", new String[]{logstring}, null, null, null,null);
			  while(cursor.moveToNext())
			  {
				  HashMap<String, Object> listmap = new HashMap<String, Object>();
				  listmap.put("id", cursor.getString(cursor.getColumnIndex("id")));
				  listmap.put("systemtime", cursor.getString(cursor.getColumnIndex("systemtime")));
				  listmap.put("log", cursor.getString(cursor.getColumnIndex("log")));
				  lists.add(listmap);
			  }
			  if(cursor.getCount()==0){
				  healthdaily_search_status.setVisibility(TextView.VISIBLE);
				  healthdaily_search_status.setText("no match record");	  
			  }
			  else if(cursor.getCount()>0){
				  healthdaily_search_status.setVisibility(TextView.GONE);	  
			  }
			  SimpleAdapter adapter = new SimpleAdapter(HealthDailyRecord.this, lists,
						R.layout.search_show_list, new String[] { "id", "systemtime","log" }, 
						new int[] { R.id.query_id,R.id.query_time, R.id.query_action });
				ListView listview1 = (ListView) findViewById(R.id.healthdailyrecord_select);
				listview1.setAdapter(adapter);
				cursor.close();
				db.close();
		}
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}
    
  //choose date
	 private void datepicker(){
     final Calendar c = Calendar.getInstance();
     mYear = c.get(Calendar.YEAR);  
     mMonth = c.get(Calendar.MONTH);  
     mDay = c.get(Calendar.DAY_OF_MONTH);
     showDialog(0);
     updateDateDisplay(); 
     }

 	private void updateDateDisplay(){
 		healthdaily_date_text.setText(new StringBuilder().append(mYear).append("-")
		   .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
            .append((mDay < 10) ? "0" + mDay : mDay));
 		}   		

 	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {  
 	       public void onDateSet(DatePicker view, int year, int monthOfYear,  
 	              int dayOfMonth) {  
 	           mYear = year;  
 	           mMonth = monthOfYear;  
 	           mDay = dayOfMonth;  
 	           updateDateDisplay();
 	          dateSelect = "%"+healthdaily_date_text.getText().toString()+"%";
 	           healthdailyrecord_select_show();
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
 	           ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);}
            }
 	    
 	    
  private void healthdailyrecord_select_show(){
	  DatabaseHelper dbHelper =new DatabaseHelper(HealthDailyRecord.this, "mhms_db");
	  SQLiteDatabase db =dbHelper.getReadableDatabase();
	  List<HashMap<String, Object>> lists = new ArrayList<HashMap<String, Object>>();
	  Cursor cursor = db.query("mhmslog",new String[]{"id,systemtime,log"}, "systemtime like ?", new String[]{dateSelect}, null, null, null,null);
	  while(cursor.moveToNext())
	  {
		  HashMap<String, Object> listmap = new HashMap<String, Object>();
		  listmap.put("id", cursor.getString(cursor.getColumnIndex("id")));
		  listmap.put("systemtime", cursor.getString(cursor.getColumnIndex("systemtime")));
		  listmap.put("log", cursor.getString(cursor.getColumnIndex("log")));
		  lists.add(listmap);
	  }
	  if(cursor.getCount()==0){
		  healthdaily_search_status.setVisibility(TextView.VISIBLE);
		  healthdaily_search_status.setText("no matech record");	  
	  }
	  else if(cursor.getCount()>0){
		  healthdaily_search_status.setVisibility(TextView.GONE);	  
	  }
	  SimpleAdapter adapter = new SimpleAdapter(HealthDailyRecord.this, lists,
				R.layout.search_show_list, new String[] { "id", "systemtime","log" }, 
				new int[] { R.id.query_id,R.id.query_time, R.id.query_action });
		ListView listview1 = (ListView) findViewById(R.id.healthdailyrecord_select);
		listview1.setAdapter(adapter);
		cursor.close();
		db.close();
  }  
  
  private void healthdailyrecord_list_show(){
	  DatabaseHelper dbHelper =new DatabaseHelper(HealthDailyRecord.this, "mhms_db");
	  SQLiteDatabase db =dbHelper.getWritableDatabase();
	  List<HashMap<String, Object>> lists = new ArrayList<HashMap<String, Object>>();
	  Cursor cursor = db.query("mhmslog",new String[]{"id,systemtime,log"}, null, null, null, null, null,null);
	  while(cursor.moveToNext())
	  {
		  HashMap<String, Object> listmap = new HashMap<String, Object>();
		  listmap.put("id", cursor.getString(cursor.getColumnIndex("id")));
		  listmap.put("systemtime", cursor.getString(cursor.getColumnIndex("systemtime")));
		  listmap.put("log", cursor.getString(cursor.getColumnIndex("log")));
		  lists.add(listmap);
	  }
	  if(cursor.getCount()==0){
		  healthdaily_all_status.setVisibility(TextView.VISIBLE);
		  healthdaily_all_status.setText("no health event daily yet");	  
	  }
	  else if(cursor.getCount()>0){
		  healthdaily_all_status.setVisibility(TextView.GONE);	  
	  }
	  SimpleAdapter adapter = new SimpleAdapter(HealthDailyRecord.this, lists,
				R.layout.search_show_list, new String[] { "id", "systemtime","log" }, 
				new int[] { R.id.query_id,R.id.query_time, R.id.query_action });
		ListView listview1 = (ListView) findViewById(R.id.healthdailyrecord_list);
		listview1.setAdapter(adapter);
		cursor.close();
		db.close();
  }   
  
  /**
	 * create MENU
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("menu");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	/**
	 * prevent MENU
	 */
	public boolean onMenuOpened(int featureId, Menu menu) {
		AlertDialog menudialog;
		View menuview;
		MenuAction addmenu = new MenuAction(HealthDailyRecord.this);
		addmenu.menucreate();
		menudialog = addmenu.menuDialog;
		menuview = addmenu.menuView;
		if (menudialog == null) {
			menudialog = new AlertDialog.Builder(this).setView(menuview).show();
		} else {
			menudialog.show();
		}

		return false;
	}

	
		public void onBackPressed() {
			if (myTabhost.getCurrentTabTag() == "tab2") {
				myTabhost.setCurrentTabByTag("tab1");
			} else {
				myTabhost.setCurrentTabByTag("tab1");
				// Intent intent =new Intent(this,Main.class);
				// startActivity(intent);
			}
		}
}
