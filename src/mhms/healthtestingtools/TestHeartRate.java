package mhms.healthtestingtools;


import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicBoolean;

import mhms.db.DatabaseHelper;
import mhms.healthrecords.R;

import mhms.log.MhmsLog;
import mhms.menu.MenuAction;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;

public class TestHeartRate extends TabActivity {
	private TabHost myTabhost;
	protected int myMenuSettingTag = 0;
	
	private Button startHeartRate;
	private Button backHeartRate;
	private int heartrate_db;
	
	private static Button okheartrate;
	private static TextView popresulthr1;
	private static TextView popresulthr2;
	private static PopupWindow popupheartrate;
	
	
	private static final String TAG = "TestHeartRate";
	private static final AtomicBoolean processing = new AtomicBoolean(false);

	private static SurfaceView preview = null;
	private static SurfaceHolder previewHolder = null;
	private static Camera camera = null;
	private static View image = null;

	private static WakeLock wakeLock = null;
	
	private static int averageIndex = 0;
	private static final int averageArraySize = 4;
	private static final int[] averageArray = new int[averageArraySize];

	public static enum TYPE { GREEN, RED };
	private static TYPE currentType = TYPE.GREEN;
	public static TYPE getCurrent() {
		return currentType;
	}

	private static int beatsIndex = 0;
	private static final int beatsArraySize = 3;
	private static final int[] beatsArray = new int[beatsArraySize];
	private static double beats = 0;
	private static long startTime = 0;
	private static PreviewCallback previewCallback;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testheartrate);
		startHeartRate = (Button) findViewById(R.id.startHeartRate);
		startHeartRate.setOnClickListener(new ButtonListener());
		backHeartRate = (Button) findViewById(R.id.backHeartRate);
		backHeartRate.setOnClickListener(new ButtonListener());

		myTabhost = this.getTabHost();
		myTabhost.addTab(myTabhost.newTabSpec("tab1")// make a new Tab
				.setIndicator("about heart rate testing", null)
				// set the Title and Icon
				.setContent(R.id.heartrate_start_tab));
		// set the layout

		myTabhost.addTab(myTabhost.newTabSpec("tab2")// make a new Tab
				.setIndicator("heart rate testing", null)
				// set the Title and Icon
				.setContent(R.id.testheartrate_tab));
		// set the layout

		preview = (SurfaceView) findViewById(R.id.preview);
		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		image = findViewById(R.id.image);
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = pm
				.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

	}

	// create PopupWindow

	protected void initPopupWindow() {

		View popupWindow_view = getLayoutInflater().inflate(
				R.layout.heartratepopresult, null, false);
		popupheartrate = new PopupWindow(popupWindow_view, 260, 160,
				true);// create PopupWindow

		popresulthr1 = (TextView) popupWindow_view
				.findViewById(R.id.popresulthr1);
		popresulthr2 = (TextView) popupWindow_view
				.findViewById(R.id.popresulthr2);

		popupheartrate.setBackgroundDrawable(getResources()
				.getDrawable(R.drawable.white));

		popupheartrate.setFocusable(false);

		okheartrate = (Button) popupWindow_view
				.findViewById(R.id.okheartrate);
		okheartrate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dbinsert();
				MhmsLog.mhmslog(TestHeartRate.this, MhmsLog.HeartRate_Test, "did a heart rate testing");
				popupheartrate.dismiss();
				camera.setPreviewCallback(null);
				myTabhost.setCurrentTabByTag("tab1");
			}
		});
		popupheartrate.update();
	}
	  //insert database
			private void dbinsert(){
				DatabaseHelper dbHelper = new DatabaseHelper(TestHeartRate.this,"mhms_db");
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				SimpleDateFormat sTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			    String systemtime = sTime.format(new java.util.Date());	
				// insert data
				ContentValues values = new ContentValues();
				values.put("systemtime", systemtime);
				values.put("heartrate", heartrate_db);
				db.insert("heartratetable", null, values);
				db.close();
			}
	
	class ButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.startHeartRate) {				
				myTabhost.setCurrentTabByTag("tab2");
				camera.setPreviewCallback(previewCallback);
				previewCallback = new PreviewCallback() {
					@Override
					public void onPreviewFrame(byte[] data, Camera cam) {

						if (data == null)
							throw new NullPointerException();
						Camera.Size size = cam.getParameters().getPreviewSize();
						if (size == null)
							throw new NullPointerException();

						if (!processing.compareAndSet(false, true))
							return;

						int width = size.width;
						int height = size.height;
						// get ImageProcessing.java
					
						int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(
								data.clone(), height, width);
						Log.i(TAG, "imgAvg=" + imgAvg);
						if (imgAvg == 0 || imgAvg == 255) {
							processing.set(false);
							return;
						}

						int averageArrayAvg = 0;
						int averageArrayCnt = 0;
						for (int i = 0; i < averageArray.length; i++) {
							if (averageArray[i] > 0) {
								averageArrayAvg += averageArray[i];
								averageArrayCnt++;
							}
						}

						int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt)
								: 0;
						TYPE newType = currentType;
						if (imgAvg < rollingAverage) {
							newType = TYPE.RED;
						
							//red means heart beat
							if (newType != currentType) {
								beats++;
								Log.e(TAG, "BEAT!! beats=" + beats);
							}
						} else if (imgAvg > rollingAverage) {
							newType = TYPE.GREEN;
						}

					
						//when get right image, means get heart beat, add one
						if (averageIndex == averageArraySize)
							averageIndex = 0;
						averageArray[averageIndex] = imgAvg;
						averageIndex++;

						// Transitioned from one state to another to the same
						if (newType != currentType) {
							currentType = newType;
							image.postInvalidate();
						}

						long endTime = System.currentTimeMillis();
						double totalTimeInSecs = (endTime - startTime) / 1000d;
						
						//time over 10s, calculate heart rate once
						if (totalTimeInSecs >= 10) {
							double bps = (beats / totalTimeInSecs);
						
							//dmp is heart rate, only the once, we should get average
							int dpm = (int) (bps * 60d);
							
							//heart rate in this range, no use
							if (dpm < 30 || dpm > 180) {
								startTime = System.currentTimeMillis();
							
								//heart beat to zero, no use, calculate again
								beats = 0;
								processing.set(false);
								return;
							}

							Log.e(TAG, "totalTimeInSecs=" + totalTimeInSecs + " beats="
									+ beats);
						
							//add right heart rate to array
							if (beatsIndex == beatsArraySize)
								beatsIndex = 0;
							beatsArray[beatsIndex] = dpm;
							beatsIndex++;

							int beatsArrayAvg = 0;
							int beatsArrayCnt = 0;
							for (int i = 0; i < beatsArray.length; i++) {
								if (beatsArray[i] > 0) {
									
									//total
									beatsArrayAvg += beatsArray[i];
									// number of values
									beatsArrayCnt++;
								}
							}

					
							//averagy
							int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
							heartrate_db =beatsAvg;

							//show user the average result
							getPopupWindow();
							popresulthr1.setText("Your heart rate is" + String.valueOf(beatsAvg)
									+ "!");
							if (beatsAvg > 100) {
								popresulthr2.setText("Your heart rate is fast£¡");
							} else if (beatsAvg >= 60 & beatsAvg <= 100) {
								popresulthr2.setText("Your heart rate is normal£¡");
							} else {
								popresulthr2.setText("Your heart rate is slow£¡");
							}

							popupheartrate.showAtLocation(findViewById(R.id.testheartrate_tab),
									Gravity.CENTER, 0, 0);

							startTime = System.currentTimeMillis();
							beats = 0;
						}
						processing.set(false);

					}

					private void getPopupWindow() {
						// popupheartrate.showAtLocation(findViewById(R.id.testheartrate_tab),Gravity.BOTTOM,
						// 0, 0);
						if (null != popupheartrate) {
							popupheartrate.dismiss();
							return;
						}
						else {
							initPopupWindow();
						}
					}
			
				};
			} else if (v.getId() == R.id.backHeartRate) {
				camera.setPreviewCallback(null);
				myTabhost.setCurrentTabByTag("tab1");
			
			}
		}
	}




	//switch screen
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		camera = Camera.open();
		startTime = System.currentTimeMillis();
	}

	@Override
	public void onPause() {
		super.onPause();
		wakeLock.release();
		camera.setPreviewCallback(null);
		camera.stopPreview();
		camera.release();
		camera = null;
	}

	private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				camera.setPreviewDisplay(previewHolder);
				camera.setPreviewCallback(previewCallback);
			} catch (Throwable t) {
				Log.e("PreviewDemo-surfaceCallback",
						"Exception in setPreviewDisplay()", t);
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Camera.Parameters parameters = camera.getParameters();
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
			Camera.Size size = getSmallestPreviewSize(width, height, parameters);
			if (size != null) {
				parameters.setPreviewSize(size.width, size.height);
				Log.d(TAG, "Using width=" + size.width + " height="
						+ size.height);
			}
			camera.setParameters(parameters);
			camera.startPreview();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// Ignore
		}
	};

	private static Camera.Size getSmallestPreviewSize(int width, int height,
			Camera.Parameters parameters) {
		Camera.Size result = null;

		for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
			if (size.width <= width && size.height <= height) {
				if (result == null) {
					result = size;
				} else {
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;

					if (newArea < resultArea)
						result = size;
				}
			}
		}

		return result;
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
		MenuAction addmenu = new MenuAction(TestHeartRate.this);
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
