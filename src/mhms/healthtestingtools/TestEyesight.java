package mhms.healthtestingtools;

import java.text.SimpleDateFormat;
import java.util.Random;

import mhms.db.DatabaseHelper;
import mhms.healthrecords.R;
import mhms.log.MhmsLog;
import mhms.menu.MenuAction;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;

public class TestEyesight extends TabActivity {
	private TabHost myTabhost;
	protected int myMenuSettingTag = 0;
	private Button startEyesight;

	private static ImageView imageView;
	private Button up;
	private Button down;
	private Button left;
	private Button right;
	private Button backEyesight;
	private PopupWindow popupeyesight;
	private TextView popresult;
	private Button okeyesight;
	private double eyesight_db;

	// store images in array
	private static int[] imgs0 = new int[] { R.drawable.up0, R.drawable.down0, R.drawable.left0, R.drawable.right0, };
	private static int[] imgs1 = new int[] { R.drawable.up1, R.drawable.down1, R.drawable.left1, R.drawable.right1, };
	private static int[] imgs2 = new int[] { R.drawable.up2, R.drawable.down2, R.drawable.left2, R.drawable.right2, };
	private static int[] imgs3 = new int[] { R.drawable.up3, R.drawable.down3, R.drawable.left3, R.drawable.right3, };
	private static int[] imgs4 = new int[] { R.drawable.up4, R.drawable.down4, R.drawable.left4, R.drawable.right4, };
	private static int[] imgs5 = new int[] { R.drawable.up5, R.drawable.down5, R.drawable.left5, R.drawable.right5, };

	static Random rand = new Random();
	static int index = rand.nextInt(4);

	// right times
	static int i = 0;
	// images already shown
	int j;

	// define level, six levels, >5.2 one ignore; not that exact
	int level;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testeyesight);
		startEyesight = (Button) findViewById(R.id.startEyesight);
		startEyesight.setOnClickListener(new ButtonListener());
		backEyesight = (Button) findViewById(R.id.backEyesight);
		backEyesight.setOnClickListener(new ButtonListener());

		myTabhost = this.getTabHost();
		myTabhost.addTab(myTabhost.newTabSpec("tab1") // make a new Tab
				.setIndicator("about eyesight testing", null)

		// set the Title and Icon
				.setContent(R.id.eyesight_start_tab));
		// set the layout
		myTabhost.addTab(myTabhost.newTabSpec("tab2") // make a new Tab
				.setIndicator("eyesight testing", null)

		// set the Title and Icon
				.setContent(R.id.testeyesight_tab));
				// set the layout

		// five click, four for user to get the direction of the E
		up = (Button) findViewById(R.id.up);
		up.setOnClickListener(new upOnClickListener());

		down = (Button) findViewById(R.id.down);
		down.setOnClickListener(new downOnClickListener());

		left = (Button) findViewById(R.id.left);
		left.setOnClickListener(new leftOnClickListener());

		right = (Button) findViewById(R.id.right);
		right.setOnClickListener(new rightOnClickListener());

		imageView = (ImageView) findViewById(R.id.imageView);

		// the fifth click, user click on the image, see not clearly, wrong
		imageView.setOnClickListener(new imageViewOnClickListener());

	}

	class ButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.startEyesight) {
				myTabhost.setCurrentTabByTag("tab2");

				// random get the first picture
				getimageView0();
				index = 0;
				i = 0;
				j = 0;
			} else if (v.getId() == R.id.backEyesight) {
				myTabhost.setCurrentTabByTag("tab1");
			}
		}
	}

	private void getimageView0() {

		rand = new Random();
		index = rand.nextInt(4);

		// imageView = (ImageView)findViewById(R.id.imageView);
		imageView.setImageResource(imgs0[index]);
	}

	private void getimageView1() {
		rand = new Random();
		index = rand.nextInt(4);
		// imageView = (ImageView)findViewById(R.id.imageView);
		imageView.setImageResource(imgs1[index]);
	}

	private void getimageView2() {
		rand = new Random();
		index = rand.nextInt(4);
		// imageView = (ImageView)findViewById(R.id.imageView);
		imageView.setImageResource(imgs2[index]);
	}

	private void getimageView3() {
		rand = new Random();
		index = rand.nextInt(4);
		// imageView = (ImageView)findViewById(R.id.imageView);
		imageView.setImageResource(imgs3[index]);
	}

	private void getimageView4() {
		rand = new Random();
		index = rand.nextInt(4);
		// imageView = (ImageView)findViewById(R.id.imageView);
		imageView.setImageResource(imgs4[index]);
	}

	private void getimageView5() {
		rand = new Random();
		index = rand.nextInt(4);
		// imageView = (ImageView)findViewById(R.id.imageView);
		imageView.setImageResource(imgs5[index]);
	}

	private void decidelevel() {
		if (j <= 4) {
			level0();
		} else if (j > 4 & j <= 8) {
			level1();
		} else if (j > 8 & j <= 12) {
			level2();
		} else if (j > 12 & j <= 16) {
			level3();
		} else if (j > 16 & j <= 20) {
			level4();
		} else if (j > 20 & j <= 24) {
			level5();
		} else {
			return;
		}
	}

	// up
	private class upOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {

			// right, add one
			if (index == 0) {
				// right times add one
				i = i + 1;
			}

			// images show times add one, four times then go to next level or
			// get result
			j = j + 1;
			decidelevel();
		}
	}

	// down
	private class downOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (index == 1) {
				i = i + 1;
			}
			j = j + 1;
			decidelevel();
		}
	}

	// left
	private class leftOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (index == 2) { // add right times one
				i = i + 1;
			}
			j = j + 1;
			decidelevel();
		}
	}

	// right
	private class rightOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (index == 3)

			{
				// right times add 1
				i = i + 1;
			}
			j = j + 1;
			decidelevel();
		}
	}

	// user cannot see clearly, click image, wrong,
	private class imageViewOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// images show times add one, four times then go to next level or
			// get result

			j = j + 1;
			decidelevel();
		}
	}

	// level0()��
	private void level0() {

		// level0 all four pictures show over, right times? system decide next
		// step
		// right times <=2, result show
		if (j == 4 & i <= 2) {
			level = -1;
			getResult();
		}

		// right times>2, next level1();
		else if (j == 4 & i > 2)

		{ // level1();
			// i to 0
			i = 0;
			level1();
		} else {
			getimageView0();
		}

		// level0 end
	}

	// level1();
	private void level1() {
		// right times>2, next level2();

		if (j == 8 & i > 2)

		{
			i = 0;
			// level2();
			level2();
		}
		// right times <=2, result show

		else if (j == 8 & i <= 2) {
			level = 0;
			getResult();
		}

		else {
			getimageView1();
		}
		// level1() end��
	}

	// level2();
	private void level2() {
		if (j == 12 & i > 2)

		{
			i = 0;
			// level3();
			level3();
		} else if (j == 12 & i <= 2) {
			level = 1;
			getResult();
		} else {
			getimageView2();
		}

		// level2() end��
	}

	// level3();
	private void level3() {

		if (j == 16 & i > 2)

		{
			i = 0;
			// level4();
			level4();
		}

		else if (j == 16 & i <= 2) {
			level = 2;

			getResult();

		}

		else {
			getimageView3();
		}
		// level3() end��
	}

	// level4
	private void level4() {

		if (j == 20 & i > 2)

		{
			i = 0;

			// level5();
			level5();
		}

		else if (j == 20 & i <= 2) {
			level = 3;

			getResult();
		} else {
			getimageView4();
		}

		// level4() end��
	}

	// level5
	private void level5() {
		if (j == 24 & i > 2)

		{
			level = 5;
			getResult();
		}

		else if (j == 24 & i <= 2)

		{
			level = 4;

			getResult();

		} else {
			getimageView5();
		}

		// level5() end��
	}

	// getResult();
	private void getResult() {

		getPopupWindow();

		// level0 result
		if (level == 0)

		{
			popresult.setText("Your eyesight is about��\n3.0~3.5(0.01~0.03)");
			eyesight_db = 0.02;
		}

		// level1 result
		else if (level == 1) {
			popresult.setText("Your eyesight is about��\n3.5~3.9(0.03~0.08)");
		}

		// level2 result
		else if (level == 2) {
			popresult.setText("Your eyesight is about��\n3.9~4.3(0.08~0.2)");
			eyesight_db = 0.1;
		}

		// level3 result
		else if (level == 3) {
			popresult.setText("Your eyesight is about��\n4.3~4.7(0.2~0.5)");
			eyesight_db = 0.4;
		}

		// level4 result
		else if (level == 4) {
			popresult.setText("Your eyesight is about��\n4.7~5.0(0.5~1.0)");
			eyesight_db = 0.7;
		}

		// level5 result
		else if (level == 5) {
			popresult.setText("Your eyesight is about��\n5.0~5.2(1.0~1.5)");
			eyesight_db = 1.2;
		}

		// lower than level0 result,level=-1
		else {
			popresult.setText("Your eyesight is����\n lower than 3.0(0.01)");
			eyesight_db = 0.01;
		}

		popupeyesight.showAtLocation(findViewById(R.id.testeyesight_tab), Gravity.CENTER, 0, 0);

		// getResult() end
	}

	// getPopupWindow

	private void getPopupWindow() {

		if (null != popupeyesight) {
			popupeyesight.dismiss();
			return;
		}

		else {
			initPopupWindow();
		}
	}

	// PopupWindow

	protected void initPopupWindow() {

		// get layout
		View popupWindow_view = getLayoutInflater().inflate(R.layout.eyesightpopresult, null, false);

		// PopupWindow
		popupeyesight = new PopupWindow(popupWindow_view, 260, 160, true);

		popresult = (TextView) popupWindow_view.findViewById(R.id.popresult);

		popupeyesight.setBackgroundDrawable(getResources().getDrawable(R.drawable.white));

		popupeyesight.setFocusable(false);

		okeyesight = (Button) popupWindow_view.findViewById(R.id.okeyesight);
		// close window
		okeyesight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dbinsert();
				MhmsLog.mhmslog(TestEyesight.this, MhmsLog.EyeSight_Test, "performed a eyesight testing");
				myTabhost.setCurrentTabByTag("tab1");
				popupeyesight.dismiss();
			}
		});

		popupeyesight.update();

	}

	// insert database
	private void dbinsert() {
		DatabaseHelper dbHelper = new DatabaseHelper(TestEyesight.this, "mhms_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		SimpleDateFormat sTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String systemtime = sTime.format(new java.util.Date());

		// insert data
		ContentValues values = new ContentValues();
		values.put("systemtime", systemtime);
		values.put("eyesight", eyesight_db);
		db.insert("eyesighttable", null, values);
		db.close();
	}
	// all end

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
		MenuAction addmenu = new MenuAction(TestEyesight.this);
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
