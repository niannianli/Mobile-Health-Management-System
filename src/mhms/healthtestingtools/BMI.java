package mhms.healthtestingtools;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import mhms.db.DatabaseHelper;
import mhms.healthrecords.R;
import mhms.log.MhmsLog;
import mhms.menu.MenuAction;

public class BMI extends Activity {

	private EditText weightEditText;
	private EditText heightEditText;

	private Button startBMI;

	private PopupWindow popupbmi;
	private TextView popresult1;
	private TextView popresult2;
	private Button okbmi;
	private float weight, height, BMI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.bmi);

		weightEditText = (EditText) findViewById(R.id.weightEditText);
		heightEditText = (EditText) findViewById(R.id.heightEditText);

		startBMI = (Button) findViewById(R.id.startBMI);
		startBMI.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String weightStr = weightEditText.getText().toString().trim();
				String heightStr = heightEditText.getText().toString().trim();
				if (weightStr.equals("") || weightStr.equals(null) || weightStr.equals(".") || heightStr.equals("")
						|| heightStr.equals(null) || heightStr.equals(".")) {

					AlertDialog.Builder returnbuilder = new Builder(BMI.this);
					returnbuilder.setMessage("��������ȷ��ߺ�����");
					returnbuilder.setTitle("��ʾ");
					returnbuilder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					returnbuilder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					returnbuilder.create().show();
				}
				// ����BMI
				else {
					DecimalFormat nf = new DecimalFormat("0.00");
					weight = Float.parseFloat(weightStr);
					height = Float.parseFloat(heightStr) / 100;
					BMI = Float.parseFloat(nf.format(weight / (height * height)));

					// get dialog, show BMI result
					getPopupWindow();

					popresult1.setText("Your BMI is" + BMI);
					if (BMI >= 28) {
						popresult2.setText("You are too heavy��");
					} else if (BMI >= 24 & BMI < 28) {
						popresult2.setText("You are overweight��");
					} else if (BMI < 18.5) {
						popresult2.setText("You are too thin��");
					} else {
						popresult2.setText("You are in good shape");
					}

					// the location of the dialog
					popupbmi.showAtLocation(findViewById(R.id.bmi), Gravity.CENTER, 0, 0);
					// else end
				}
				// click end
			}
		});

		// onCreat end
	}

	// define getPopupWindow

	private void getPopupWindow() {

		if (null != popupbmi) {
			popupbmi.dismiss();
			return;
		}

		else {
			initPopupWindow();
		}
	}

	// create PopupWindow

	protected void initPopupWindow() {

		// get layout
		View popupWindow_view = getLayoutInflater().inflate(R.layout.bmipopresult, null, false);

		// create PopupWindow
		popupbmi = new PopupWindow(popupWindow_view, 260, 160, true);

		// show BMI result
		popresult1 = (TextView) popupWindow_view.findViewById(R.id.popresult1);
		popresult2 = (TextView) popupWindow_view.findViewById(R.id.popresult2);

		// change background of PopupWindow
		popupbmi.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));

		// set false, close PopupWindow by OkButton; set true, area outside
		// PopupWindow can close window
		popupbmi.setFocusable(false);

		okbmi = (Button) popupWindow_view.findViewById(R.id.okbmi);
		okbmi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// close dialog
				dbinsert();
				MhmsLog.mhmslog(BMI.this, MhmsLog.BMI_Test, "������һ��BMI����");
				popupbmi.dismiss();

				// restart current activity
				onCreate(null);
				return;
			}
		});

		popupbmi.update();
		// initPopupWindow() end
	}

	// insert data
	private void dbinsert() {
		DatabaseHelper dbHelper = new DatabaseHelper(BMI.this, "mhms_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SimpleDateFormat sTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String systemtime = sTime.format(new java.util.Date());
		// insert data
		ContentValues values = new ContentValues();
		values.put("systemtime", systemtime);
		values.put("bodyheight", height);
		values.put("bodyweight", weight);
		values.put("bmi", BMI);
		db.insert("bmitable", null, values);
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
		MenuAction addmenu = new MenuAction(BMI.this);
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

}
