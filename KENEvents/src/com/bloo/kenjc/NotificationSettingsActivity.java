package com.bloo.kenjc;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;
import com.fipl.kenjc.Utils.IsNetworkConnection;
import com.fipl.kenjc.Utils.post_async;

public class NotificationSettingsActivity extends Activity implements
		OnClickListener {
	ImageView backbtn, neweventnotibtnon, neweventnotibtnoff,
			generalannouncementbtnon, generalannouncementbtnoff,
			activityreminderbtnon, activityreminderbtnoff, eventreminderbtnon,
			eventreminderbtnoff, muteallbtnon, muteallbtnoff,adultacton,adultactoff,youthacton,youthactoff,minimaccabiacton,minimaccabiactoff,bogrimacton,bogrimactoff,noncommunityon,noncommunityoff;
	TextView neweventnoti, generalannouncement, activityreminder,
			eventreminder, muteall,adultact,youthjact,minimaccabiact,bogrimact,noncommunityact;
	String genannouncementstatus="", actreminderstatus="", neweventstatus="",
			eventreminderstatus="", mutestatus="",adultactstatus="",bogrimstatus="",youthstatus="",noncommunitystatus="",minimaccabistatus="";
	SharedPreferences prefrence;
	Editor editor;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notificationsettings);
		prefrence = getSharedPreferences(Constants.PREF, 0);
		editor = prefrence.edit();
		init();

		editor.commit();
		if (prefrence.getString("nwevtpush", "") != null
				&& prefrence.getString("generalpush", "") != null
				&& prefrence.getString("actremindpush", "") != null
				&& prefrence.getString("eventremindpush", "") != null
				&& prefrence.getString("mempush", "") != null 
				&& prefrence.getString("memAdultActivities", "") != null
				&& prefrence.getString("memYouthActivities", "") != null 
				&& prefrence.getString("memMiniMaccabiActivities", "") != null 
				&& prefrence.getString("memBogrim", "") != null && prefrence.getString("memNonCommunityEvents", "") != null)
		{
			if (prefrence.getString("nwevtpush","").equalsIgnoreCase("Yes")) 
			{
				neweventon();

			} else {
				neweventoff();
			}
			if (prefrence.getString("generalpush","").equalsIgnoreCase("Yes")) 
			{
				genannounceon();

			} else {
				genannounceoff();
			}
			if (prefrence.getString("actremindpush","").equalsIgnoreCase("yes"))
			{
				actreminon();

			} else {
				actremioff();
			}
			if (prefrence.getString("eventremindpush","").equalsIgnoreCase("yes")) 
			{
				evtremion();
			

			} else {
				
				evtremioff();
			}
			if (prefrence.getString("mempush","").equalsIgnoreCase("yes"))
			{
				editor.putString("Mute", "yes");

			} else 
			{
				editor.putString("Mute", "no");
			}
			if (prefrence.getString("memAdultActivities","").equalsIgnoreCase("yes"))
			{
			 adulton();

			} else 
			{
				adultoff();
			}

			if (prefrence.getString("memYouthActivities","").equalsIgnoreCase("yes"))
			{
				youthon();

			} else 
			{
				youthoff();
			}
			if (prefrence.getString("memMiniMaccabiActivities","").equalsIgnoreCase("yes"))
			{
				minimaccabion();

			} else 
			{
				minimaccabioff();
			}
			if (prefrence.getString("memBogrim","").equalsIgnoreCase("yes"))
			{
				bogrimon();

			} else 
			{
				bogrimoff();
			}

			if (prefrence.getString("memNonCommunityEvents","").equalsIgnoreCase("yes"))
			{
				noncommunityon();

			} else 
			{
				noncommunityoff();
			}


		}
		
	}

	private void init() {
		backbtn = (ImageView) findViewById(R.id.headerbackbtn);
		backbtn.setVisibility(View.VISIBLE);
		neweventnoti = (TextView) findViewById(R.id.tv_noti_newevent);
		neweventnotibtnon = (ImageView) findViewById(R.id.iv_noti_newevent_on);
		neweventnotibtnoff = (ImageView) findViewById(R.id.iv_noti_newevent_off);
		generalannouncement = (TextView) findViewById(R.id.tv_noti_genannounce);
		generalannouncementbtnon = (ImageView) findViewById(R.id.iv_noti_genannounce_on);
		generalannouncementbtnoff = (ImageView) findViewById(R.id.iv_noti_genannounce_off);
		activityreminder = (TextView) findViewById(R.id.tv_noti_actreminder);
		activityreminderbtnon = (ImageView) findViewById(R.id.iv_noti_actreminder_on);
		activityreminderbtnoff = (ImageView) findViewById(R.id.iv_noti_actreminder_off);
		eventreminder = (TextView) findViewById(R.id.tv_noti_eventreminder);
		eventreminderbtnon = (ImageView) findViewById(R.id.iv_noti_eventreminder_on);
		eventreminderbtnoff = (ImageView) findViewById(R.id.iv_noti_eventreminder_off);
		muteall = (TextView) findViewById(R.id.tv_noti_muteall);
		muteallbtnon = (ImageView) findViewById(R.id.iv_noti_noti_muteall_on);
		muteallbtnoff = (ImageView) findViewById(R.id.iv_noti_muteall_off);
		adultact = (TextView) findViewById(R.id.tv_noti_adultactivities);
		adultacton = (ImageView) findViewById(R.id.iv_noti_adultact_on);
		adultactoff = (ImageView) findViewById(R.id.iv_noti_adultact_off);		
		youthjact = (TextView) findViewById(R.id.tv_noti_youth);
		youthacton = (ImageView) findViewById(R.id.iv_noti_youth_on);
		youthactoff = (ImageView) findViewById(R.id.iv_noti_youth_off);
		minimaccabiact = (TextView) findViewById(R.id.tv_noti_minimaccabi);
		minimaccabiacton = (ImageView) findViewById(R.id.iv_noti_minimaccabi_on);
		minimaccabiactoff = (ImageView) findViewById(R.id.iv_noti_minimaccabi_off);
		bogrimact = (TextView) findViewById(R.id.tv_noti_bogrim);
		bogrimacton = (ImageView) findViewById(R.id.iv_noti_bogrim_on);
		bogrimactoff = (ImageView) findViewById(R.id.iv_noti_bogrim_off);
		noncommunityact = (TextView) findViewById(R.id.tv_noti_noncommunity);
		noncommunityon = (ImageView) findViewById(R.id.iv_noti_noncommunity_on);
		noncommunityoff = (ImageView) findViewById(R.id.iv_noti_noncommunity_off);
		neweventnotibtnon.setOnClickListener(this);
		neweventnotibtnoff.setOnClickListener(this);
		generalannouncementbtnon.setOnClickListener(this);
		generalannouncementbtnoff.setOnClickListener(this);
		activityreminderbtnon.setOnClickListener(this);
		activityreminderbtnoff.setOnClickListener(this);
		eventreminderbtnon.setOnClickListener(this);
		eventreminderbtnoff.setOnClickListener(this);
		muteallbtnon.setOnClickListener(this);
		muteallbtnoff.setOnClickListener(this);
		adultacton.setOnClickListener(this);
		adultactoff.setOnClickListener(this);
		youthacton.setOnClickListener(this);
		youthactoff.setOnClickListener(this);
		minimaccabiactoff.setOnClickListener(this);
		minimaccabiacton.setOnClickListener(this);
		bogrimactoff.setOnClickListener(this);
		bogrimacton.setOnClickListener(this);
		noncommunityon.setOnClickListener(this);
		noncommunityoff.setOnClickListener(this);
		backbtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		if (v == neweventnotibtnon) {
			neweventoff();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));
		}
		if (v == neweventnotibtnoff) {
			neweventon();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));
		}

		if (v == generalannouncementbtnon) {
			genannounceoff();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));
		}
		if (v == generalannouncementbtnoff) {
			genannounceon();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));
		}

		if (v == activityreminderbtnon) {
			actremioff();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));
		}
		if (v == activityreminderbtnoff) {
			actreminon();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));
		}

		if (v == eventreminderbtnon) {
			evtremioff();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));
		}

		if (v == eventreminderbtnoff) {
			evtremion();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));
		}
		if (v == muteallbtnon) {

			muteoff();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));
		}

		if (v == muteallbtnoff) {
			muteon();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));
		}
		if (v==adultacton) {
			adultoff();
			
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));

		}
		if (v==adultactoff) {
			adulton();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));

		}
		if (v==youthacton) {
			youthoff();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));

		}
		if (v==youthactoff) {
			
			youthon();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));

		}
		
		if (v==minimaccabiacton) {
			minimaccabioff();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));

		}
		if (v==minimaccabiactoff) {
			
			minimaccabion();
			
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));

		}
		
		if (v==bogrimacton) {
			bogrimoff();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));

		}
		if (v==bogrimactoff) {
			
			bogrimon();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));

		}
		
		
		if (v==noncommunityon) {
			noncommunityoff();
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));

		}
		if (v==noncommunityoff) {
			noncommunityon();
		
			Notificationsetting(prefrence.getString("EventNoti", ""),
					prefrence.getString("General", ""),
					prefrence.getString("Activity", ""),
					prefrence.getString("EventReminder", ""),
					prefrence.getString("Mute", ""),
					prefrence.getString("AdultAct", ""),
					prefrence.getString("YouthAct", ""),
					prefrence.getString("MiniMaccabiAct", ""),
					prefrence.getString("BogrimAct", ""),
					prefrence.getString("NonCommunityAct", ""));

		}
		if (v == backbtn) {

			Intent backnfs = new Intent(NotificationSettingsActivity.this,
					MoreActivity.class);
			startActivity(backnfs);
		}

	}

	private void muteon() {
		muteallbtnon.setVisibility(View.VISIBLE);
		muteallbtnoff.setVisibility(View.GONE);
		mutestatus = "yes";
		neweventnotibtnoff.setVisibility(View.VISIBLE);
		neweventnotibtnon.setVisibility(View.GONE);
		generalannouncementbtnoff.setVisibility(View.VISIBLE);
		generalannouncementbtnon.setVisibility(View.GONE);
		activityreminderbtnoff.setVisibility(View.VISIBLE);
		activityreminderbtnon.setVisibility(View.GONE);
		eventreminderbtnoff.setVisibility(View.VISIBLE);
		eventreminderbtnon.setVisibility(View.GONE);
		adultactoff.setVisibility(View.VISIBLE);
		adultacton.setVisibility(View.GONE);
		youthactoff.setVisibility(View.VISIBLE);
		youthacton.setVisibility(View.GONE);
		minimaccabiactoff.setVisibility(View.VISIBLE);
		minimaccabiacton.setVisibility(View.GONE);
		bogrimactoff.setVisibility(View.VISIBLE);
		bogrimacton.setVisibility(View.GONE);
		noncommunityoff.setVisibility(View.VISIBLE);
		noncommunityon.setVisibility(View.GONE);

		editor.putString("EventNoti", "no");
		editor.putString("General", "no");
		editor.putString("Activity", "no");
		editor.putString("EventReminder", "no");
		editor.putString("Mute", "yes");
		editor.putString("AdultAct", "no");
		editor.putString("YouthAct", "no");
		editor.putString("MiniMaccabiAct", "no");
		editor.putString("BogrimAct", "no");
		editor.putString("NonCommunityAct", "no");
		editor.commit();
	}

	private void muteoff() {
		muteallbtnoff.setVisibility(View.VISIBLE);
		muteallbtnon.setVisibility(View.GONE);
		mutestatus = "no";
		editor.putString("Mute", "no").commit();

	}

	private void evtremion() {
		eventreminderbtnoff.setVisibility(View.GONE);
		eventreminderbtnon.setVisibility(View.VISIBLE);
		eventreminderstatus = "yes";
		editor.putString("EventReminder", "yes").commit();
	}

	private void evtremioff() {
		eventreminderbtnon.setVisibility(View.GONE);
		eventreminderbtnoff.setVisibility(View.VISIBLE);
		eventreminderstatus = "no";
		editor.putString("EventReminder", "no").commit();
		
	}
	
	private void adulton() {
		adultactoff.setVisibility(View.GONE);
		adultacton.setVisibility(View.VISIBLE);
		adultactstatus = "yes";
		editor.putString("AdultAct", "yes").commit();
	}

	private void adultoff() {
		adultacton.setVisibility(View.GONE);
		adultactoff.setVisibility(View.VISIBLE);
		adultactstatus = "no";
		editor.putString("AdultAct", "no").commit();
	}

	private void youthon() {
		youthactoff.setVisibility(View.GONE);
		youthacton.setVisibility(View.VISIBLE);
		youthstatus = "yes";
		editor.putString("YouthAct", "yes").commit();
	}

	private void youthoff() {
		youthacton.setVisibility(View.GONE);
		youthactoff.setVisibility(View.VISIBLE);
		youthstatus = "no";
		editor.putString("YouthAct", "no").commit();
	}
	
	private void minimaccabion() {
		minimaccabiactoff.setVisibility(View.GONE);
		minimaccabiacton.setVisibility(View.VISIBLE);
		minimaccabistatus = "yes";
		editor.putString("MiniMaccabiAct", "yes").commit();
	}

	private void minimaccabioff() {
		minimaccabiacton.setVisibility(View.GONE);
		minimaccabiactoff.setVisibility(View.VISIBLE);
		minimaccabistatus = "no";
		editor.putString("MiniMaccabiAct", "no").commit();
	}
	private void bogrimon() {
		bogrimactoff.setVisibility(View.GONE);
		bogrimacton.setVisibility(View.VISIBLE);
		bogrimstatus = "yes";
		editor.putString("BogrimAct", "yes").commit();
	}

	private void bogrimoff() {
		bogrimacton.setVisibility(View.GONE);
		bogrimactoff.setVisibility(View.VISIBLE);
		bogrimstatus = "no";
		editor.putString("BogrimAct", "no").commit();
	}
	private void noncommunityon() {
		noncommunityoff.setVisibility(View.GONE);
		noncommunityon.setVisibility(View.VISIBLE);
		noncommunitystatus = "yes";
		editor.putString("NonCommunityAct", "yes").commit();
	}

	private void noncommunityoff() {
		noncommunityon.setVisibility(View.GONE);
		noncommunityoff.setVisibility(View.VISIBLE);
		noncommunitystatus = "no";
		editor.putString("NonCommunityAct", "no").commit();
	}
	

	private void actreminon() {
		activityreminderbtnoff.setVisibility(View.GONE);
		activityreminderbtnon.setVisibility(View.VISIBLE);
		actreminderstatus = "yes";
		editor.putString("Activity", "yes").commit();

	}

	private void actremioff() {
		activityreminderbtnon.setVisibility(View.GONE);
		activityreminderbtnoff.setVisibility(View.VISIBLE);
		actreminderstatus = "no";
		editor.putString("Activity", "no").commit();

	}

	private void genannounceon() {
		generalannouncementbtnoff.setVisibility(View.GONE);
		generalannouncementbtnon.setVisibility(View.VISIBLE);
		genannouncementstatus = "yes";
		editor.putString("General", "yes").commit();
	}

	private void genannounceoff() {
		generalannouncementbtnoff.setVisibility(View.VISIBLE);
		generalannouncementbtnon.setVisibility(View.GONE);
		genannouncementstatus = "no";
		editor.putString("General", "no").commit();
	}

	private void neweventon() {
		Log.d("System out", "in new eve on");
		neweventnotibtnoff.setVisibility(View.GONE);
		neweventnotibtnon.setVisibility(View.VISIBLE);
		neweventstatus = "yes";
		editor.putString("EventNoti", "yes").commit();

	}

	private void neweventoff() {

		neweventnotibtnoff.setVisibility(View.VISIBLE);
		neweventnotibtnon.setVisibility(View.GONE);
		neweventstatus = "no";
		editor.putString("EventNoti", "no").commit();

	}

	void Notificationsetting(String neweventstatus,
			String genannouncementstatus, String actreminderstatus,
			String eventreminderstatus, String mutestatus,String adultstatus,String youthstatus,String minimaccabistatus,String bogrimstatus,String noncommunitystatus) {
		if (IsNetworkConnection
				.checkNetworkConnection(NotificationSettingsActivity.this)) {
			String json = "[{\"" + "memNewEventPush" + "\":" + "\""
					+ neweventstatus + "\"" + ",\"" + "memGeneralPush" + "\":"
					+ "\"" + genannouncementstatus + "\"" + ",\""
					+ "memActivityReminderPush" + "\":" + "\""
					+ actreminderstatus + "\"" + ",\"" + "memEventReminderPush"
					+ "\":" + "\"" + eventreminderstatus + "\"" + ",\""
					+ "memPush" + "\":" + "\"" + mutestatus + "\"" + ",\""
					+ "memAdultActivities" + "\":" + "\"" + adultstatus
					+ "\"" + ",\""
					+ "memYouthActivities" + "\":" + "\"" + youthstatus
					+ "\"" + ",\""
					+ "memMiniMaccabiActivities" + "\":" + "\"" + minimaccabistatus
					+ "\"" + ",\""
					+ "memBogrim" + "\":" + "\"" + bogrimstatus
					+ "\"" + ",\""
					+ "memNonCommunityEvents" + "\":" + "\"" + noncommunitystatus
					+ "\"" + ",\""
					+ "memId" + "\":" + "\"" + prefrence.getString("MemID", "")
					+ "\"" + "}]";

			String url = Constants.SERVER_URL
					+ "action=EditNotificationSettings";
			Log.d("System out", "url" + url);
			new post_async(NotificationSettingsActivity.this,
					"EditNotificationSettings").execute(url, json);
		} else {
			Toast.makeText(NotificationSettingsActivity.this, "No Network",
					2000).show();
		}

	}

	public void responseofnotificationsettings(String resultString) {

		try {
			JSONArray jarray = new JSONArray(resultString);
			JSONObject jobj = jarray.getJSONObject(0);
			editor.putString("MemID", jobj.getString("memId"));
			editor.putString("name", jobj.getString("memName"));
			editor.putString("mobile", jobj.getString("memMobile"));
			editor.putString("email", jobj.getString("memEmail"));
			editor.putString("dob", jobj.getString("memDob"));
			editor.putString("gender", jobj.getString("memGender"));
			editor.putString("status", jobj.getString("memStatus"));
			editor.putString("devicetype", jobj.getString("memDeviceType"));
			editor.putString("udid", jobj.getString("memUDID"));
			editor.putString("nwevtpush", jobj.getString("memNewEventPush"));			
			editor.putString("generalpush", jobj.getString("memGeneralPush"));			
			editor.putString("actremindpush",jobj.getString("memActivityReminderPush"));		
			editor.putString("eventremindpush",jobj.getString("memEventReminderPush"));			
			editor.putString("mempush", jobj.getString("memPush"));	
			editor.putString("memETC",
					jobj.getString("memETC"));
			editor.putString("memAdultActivities",
					jobj.getString("memAdultActivities"));
			editor.putString("memYouthActivities",
					jobj.getString("memYouthActivities"));
			editor.putString("memMiniMaccabiActivities",
					jobj.getString("memMiniMaccabiActivities"));
			editor.putString("memBogrim",
					jobj.getString("memBogrim"));
			editor.putString("memNonCommunityEvents",
					jobj.getString("memNonCommunityEvents"));
			editor.putString("memcreateddate", jobj.getString("memCreatedDate"));
			editor.putString("memBadgeCount", jobj.getString("memBadgeCount"));
			editor.commit();
			String status = jobj.getString("status");
           if (status.equalsIgnoreCase("true")) {
        	   Toast.makeText(NotificationSettingsActivity.this,"Successfully updated", 1000).show();
		    }
           else
           {
        	   Toast.makeText(NotificationSettingsActivity.this,"Problem occurred", 1000).show();
           }
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
