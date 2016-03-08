package com.bloo.kenjc;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;
import com.fipl.kenjc.Utils.IsNetworkConnection;
import com.fipl.kenjc.Utils.post_async;

public class NotificationsActivity extends Activity implements OnClickListener {
	ListView neweventannounce;
	LinearLayout announcement, youthact, minimaccabiact, bogrimact;
	LinearLayout reminder, noncommunityact, adultact;
	int Notificationcount;
	SharedPreferences prefrence;
	HashMap<String, String> map;
	ArrayList<HashMap<String, String>> neweventannouncelist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> youthactivitieslist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> adultactivitieslist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> minimaccabiactivitieslist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> bogrimactivitieslist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> noncommunityactivitieslist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> reminderlist = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> announcementlist = new ArrayList<HashMap<String, String>>();
	Editor editor;
	LinearLayout newevent;
	private String tag = "";
	private int remainingbadge = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notificationtab);
		prefrence = getSharedPreferences(Constants.PREF, 0);
		editor = prefrence.edit();
		init();
		notificationcount(prefrence.getInt("badge", 0));

	}

	private void init() {
		newevent = (LinearLayout) findViewById(R.id.noti_ll1);
		reminder = (LinearLayout) findViewById(R.id.noti_ll2);
		announcement = (LinearLayout) findViewById(R.id.noti_ll3);
		adultact = (LinearLayout) findViewById(R.id.noti_ll4);
		youthact = (LinearLayout) findViewById(R.id.noti_ll5);
		minimaccabiact = (LinearLayout) findViewById(R.id.noti_ll6);
		bogrimact = (LinearLayout) findViewById(R.id.noti_ll7);
		noncommunityact = (LinearLayout) findViewById(R.id.noti_ll8);

		newevent.setOnClickListener(this);
		reminder.setOnClickListener(this);
		announcement.setOnClickListener(this);

		adultact.setOnClickListener(this);
		youthact.setOnClickListener(this);
		minimaccabiact.setOnClickListener(this);
		bogrimact.setOnClickListener(this);
		noncommunityact.setOnClickListener(this);

	}

	private void notificationcount(Integer remainingbadge) {
		if (IsNetworkConnection
				.checkNetworkConnection(NotificationsActivity.this)) {
			String jsn = "[{\"" + "memId" + "\":" + "\""
					+ prefrence.getString("MemID", "") + "\"" + ",\"" + "badge"
					+ "\":" + "\"" + this.remainingbadge + "\"" + "}]";

			String url = Constants.SERVER_URL + "action=ResetBadge";
			if (prefrence.getString("MemID", "").equalsIgnoreCase("")) {

			} else {
				new post_async(NotificationsActivity.this, "ResetBadge")
						.execute(url, jsn);

			}
		}
	}

	private void userpushnotification(String ntfType) {
		if (IsNetworkConnection
				.checkNetworkConnection(NotificationsActivity.this)) {
			String jsn = "[{\"" + "memId" + "\":" + "\""
					+ prefrence.getString("MemID", "") + "\"" + ",\""
					+ "ntfType" + "\":" + "\"" + ntfType + "\"" + "}]";

			String url = Constants.SERVER_URL + "action=getnotifications";
			if (prefrence.getString("MemID", "").equalsIgnoreCase("")) {

				prefrence.edit().putString("fromMore", "notification").commit();
				Intent logintoattend = new Intent(NotificationsActivity.this,
						LoginActivity.class);
				startActivity(logintoattend);

			} else {
				new post_async(NotificationsActivity.this, "getnotifications")
						.execute(url, jsn);

			}
		}

	}

	@Override
	public void onClick(View v) {
		if (v == reminder) {
			tag = "Event Reminder";
			userpushnotification("Event Reminder");

		}
		if (v == newevent) {
			tag = "New Event";
			userpushnotification("New Event");

		}
		if (v == announcement) {
			tag = "General Annoucements";
			userpushnotification("General");

		}

		if (v == adultact) {
			tag = "Adult activities";
			userpushnotification("Adult activities");

		}
		if (v == youthact) {
			tag = "Youth Activities";
			userpushnotification("Youth Activities");

		}
		if (v == minimaccabiact) {
			tag = "Mini Maccabi activities";
			userpushnotification("Mini Maccabi activities");

		}
		if (v == bogrimact) {
			tag = "Bogrim";
			userpushnotification("Bogrim");

		}
		if (v == noncommunityact) {
			tag = "Non Community Events";
			userpushnotification("Non Community Events");

		}

	}

	public void responseofgetpushnoti(String resultString) {
		neweventannouncelist.clear();

		if (resultString.length() > 0) {

			try {

				JSONArray jarray = new JSONArray(resultString);

				for (int i = 0; i < jarray.length(); i++) {
					JSONObject jobj = jarray.getJSONObject(i);
					map = new HashMap<String, String>();
					map.put("EvtType", jobj.getString("ntfType"));
					map.put("NwEvtName", jobj.getString("ntfmsgText"));
					map.put("NwEvtSenddate", jobj.getString("ntfSendDate"));
					map.put("Name", jobj.getString("evtName"));
					map.put("Nwevtstartdate", jobj.getString("evtStartDate"));
					map.put("Nwevtadd", jobj.getString("evtAddress"));
					map.put("Nwevtinfo", jobj.getString("evtInfo"));
					map.put("Nwevtlat", jobj.getString("evtLatitude"));
					map.put("NwEvtlong", jobj.getString("evtLongitude"));
					map.put("NwEvtmemname", jobj.getString("memName"));
					map.put("NwEvtimage", jobj.getString("evtImage"));
					neweventannouncelist.add(map);

				}
				if (neweventannouncelist.get(0).get("EvtType")
						.equals("New Event")) {
					Intent eventdetail = new Intent();
					eventdetail.putExtra("nwevent", neweventannouncelist);
					eventdetail.setClass(NotificationsActivity.this,
							NewEventDisplayActivity.class);
					startActivity(eventdetail);

				} else if (neweventannouncelist.get(0).get("EvtType")
						.equals("Event Reminder")) {
					Intent reminderdetail = new Intent();
					reminderdetail.putExtra("nwevent", neweventannouncelist);
					reminderdetail.setClass(NotificationsActivity.this,
							NewEventDisplayActivity.class);
					startActivity(reminderdetail);

				} else if (neweventannouncelist.get(0).get("EvtType")
						.equals("Annoucements")) {

					Intent announcementdetail = new Intent();
					announcementdetail
							.putExtra("nwevent", neweventannouncelist);
					announcementdetail.setClass(NotificationsActivity.this,
							AnnouncementDisplay.class);
					startActivity(announcementdetail);

				} else {

					Intent eventdetail = new Intent();
					eventdetail.putExtra("nwevent", neweventannouncelist);
					eventdetail.setClass(NotificationsActivity.this,
							AllEventDetailDisplayActivities.class);
					eventdetail.putExtra("eventname", map.get("EvtType"));
					startActivity(eventdetail);

				}

			} catch (Exception e) {

			}
		} else {
		}
		if (neweventannouncelist.size() == 0) {

			if (tag.equalsIgnoreCase("New Event")) {
				Intent eventdetail = new Intent();
				eventdetail.putExtra("nwevent", neweventannouncelist);
				eventdetail.setClass(NotificationsActivity.this,
						NewEventDisplayActivity.class);
				startActivity(eventdetail);

			} else if (tag.equalsIgnoreCase("Event Reminder")) {
				Intent reminderdetail = new Intent();
				reminderdetail.putExtra("nwevent", neweventannouncelist);
				reminderdetail.setClass(NotificationsActivity.this,
						ReminderDisplayActivity.class);
				startActivity(reminderdetail);

			} else if (tag.equals("General Annoucements")) {

				Intent announcementdetail = new Intent();
				announcementdetail.putExtra("nwevent", neweventannouncelist);
				announcementdetail.setClass(NotificationsActivity.this,
						AnnouncementDisplay.class);
				startActivity(announcementdetail);

			} else {

				Intent eventdetail = new Intent();
				eventdetail.putExtra("nwevent", neweventannouncelist);
				eventdetail.setClass(NotificationsActivity.this,
						AllEventDetailDisplayActivities.class);
				eventdetail.putExtra("eventname", tag);
				startActivity(eventdetail);

			}

		}
	}

	public void responseofnotificationcount(String resultString) {
		try {
			JSONArray jarray = new JSONArray(resultString);
			JSONObject job = jarray.getJSONObject(0);
			String status = job.getString("status");
			if (status.equalsIgnoreCase("true")) {
				Notificationcount = 0;
				editor.putInt("nfcount", Notificationcount);
				if (prefrence.getString("badge", "").equalsIgnoreCase("0")) {
					editor.putString("badge", "0").commit();
				} else {
					if (prefrence.getInt("badge", 0) == 0) {

					} else {
						int intget = Integer.parseInt(prefrence.getString(
								"badge", "")) - 1;
						editor.putInt("", intget);
					}
				}

				editor.commit();

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
