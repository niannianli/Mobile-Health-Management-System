package mhms.menu;

import java.util.ArrayList;
import java.util.HashMap;


import mhms.healthrecords.HealthRecords;
import mhms.healthrecords.R;
import mhms.healthtestingtools.HealthTestingTools;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
//import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class MenuAction {
	public AlertDialog menuDialog;// menu Dialog 
	public GridView menuGrid;
	public Context menucontext;
	public View menuView;
	
	//construction, context parameter: get menu page
	public MenuAction(Context context){	
		menucontext = context;
	};

	public void menucreate() {
	
		//menu items Subscript
		final int ITEM_Healthrecords = 0;
		final int ITEM_Helthanalysis = 1;
		final int ITEM_Testingtools = 2;
		final int ITEM_Lifeassistant = 3;
		final int ITEM_Systemsetting = 4;
		final int ITEM_Onlinesearch = 5;
		final int ITEM_Backtomain = 6;
		final int ITEM_About = 7;
		final int ITEM_Quit = 8;

		
		//menu images
		int[] menu_image_array = { R.drawable.menu_healthrecords,
				R.drawable.menu_healthanalysis, R.drawable.menu_testingtools,
				R.drawable.menu_lifeassistant, R.drawable.menu_systemsetting,
				R.drawable.menu_onlinesearch, R.drawable.menu_backtomain,
				R.drawable.menu_about, R.drawable.menu_quit };

		//menu words
		String[] menu_name_array = { "health records", "health analysis", "health testing tools", "life assistant", "system settings",
				
				"online query", "return to home page", "about", "quit" };

		//set menu items
		menuView = View.inflate(menucontext, R.layout.gridview_menu, null);
	
		//create AlertDialog
		menuDialog = new AlertDialog.Builder(menucontext).create();
		menuDialog.setView(menuView);
		menuDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_MENU)// listener button
					dialog.dismiss();
				return false;
			}
		});
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
       
	
		
		//listener menu
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent =new Intent();
				switch (arg2) {
				case ITEM_Healthrecords:// health records
					intent.setClass(menucontext, HealthRecords.class);
					menucontext.startActivity(intent);
					((Activity) menucontext).finish();
					menuDialog.dismiss();
					break;
				case ITEM_Helthanalysis:// health analysis
					// intent.setClass(menucontext, HealthAnalysis.class);
					// startActivity(intent);
					menuDialog.dismiss();
					break;
				case ITEM_Testingtools:// health testing tools
					intent.setClass(menucontext, HealthTestingTools.class);
					menucontext.startActivity(intent);
					((Activity) menucontext).finish();
					menuDialog.dismiss();
					break;
				case ITEM_Lifeassistant:// life assistant
					// intent.setClass(menucontext, LifeAssistant.class);
					// startActivity(intent);
					menuDialog.dismiss();
					break;
				case ITEM_Systemsetting:// system settings
					// intent.setClass(menucontext, SystemSetting.class);
					// startActivity(intent);
					menuDialog.dismiss();
					break;
				case ITEM_Onlinesearch:// online query
					// intent.setClass(menucontext, OnlineSearch.class);
					// startActivity(intent);
					menuDialog.dismiss();
					break;
				case ITEM_Backtomain:// back to home page
					// intent.setClass(menucontext, main.class);
					// startActivity(intent);
					menuDialog.dismiss();
					break;
				case ITEM_About:// about
					Builder aboutdialog = new Builder(menucontext);
					aboutdialog.setTitle("about info");
					aboutdialog.setMessage("mobile health management system\n Niannian Li");
					aboutdialog.setPositiveButton("submit", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					aboutdialog.setNegativeButton("cancel", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					aboutdialog.create().show();
					menuDialog.dismiss();
					break;
				case ITEM_Quit:// quit
					menuDialog.dismiss();
					System.exit(0);
					break;
				}
			}
		});
			}
	/**
	 * construct menu Adapter
	 * 
	 * @param menuNameArray
	 *            name
	 * @param imageResourceArray
	 *            image
	 * @return SimpleAdapter
	 */
	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(menucontext, data,
				R.layout.item_menu, new String[] { "itemImage", "itemText" },
				new int[] { R.id.item_image, R.id.item_text });
		return simperAdapter;
	}
	
}

