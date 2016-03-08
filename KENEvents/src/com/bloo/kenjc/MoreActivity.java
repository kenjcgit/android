package com.bloo.kenjc;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fipl.kenjc.Utils.Constants;

public class MoreActivity extends Activity implements OnClickListener {
	TextView noti_setting, noti_changepwd, noti_changeprofile, noti_ken_s_cal,
			noti_buseresrevation, noti_logout;
	ImageView moreselector;
	SharedPreferences prefrence;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moretab);
		prefrence = getSharedPreferences(Constants.PREF, 0);
		editor = prefrence.edit();
		init();

	}

	private void init() {
		noti_setting = (TextView) findViewById(R.id.tv_noti_setting);
		noti_changepwd = (TextView) findViewById(R.id.tv_noti_changepwd);
		noti_changeprofile = (TextView) findViewById(R.id.tv_noti_changeprofile);
		noti_ken_s_cal = (TextView) findViewById(R.id.tv_noti_calender);
		noti_buseresrevation = (TextView) findViewById(R.id.tv_noti_busreservation);
		noti_logout = (TextView) findViewById(R.id.tv_noti_logout);

		if (prefrence.getString("MemID", "").toString().length() > 0) {
			noti_logout.setVisibility(View.VISIBLE);

		} else {
			noti_logout.setVisibility(View.GONE);
		}
		noti_setting.setOnClickListener(this);
		noti_changepwd.setOnClickListener(this);
		noti_changeprofile.setOnClickListener(this);
		noti_buseresrevation.setOnClickListener(this);
		noti_ken_s_cal.setOnClickListener(this);
		noti_logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == noti_setting) {
			if (prefrence.getString("MemID", "").toString().length() > 0) {
				Intent notificationsetting = new Intent(MoreActivity.this,
						NotificationSettingsActivity.class);
				startActivity(notificationsetting);
			} else {
				prefrence.edit().putString("fromMore", "nsettings").commit();
				Intent i = new Intent(MoreActivity.this, LoginActivity.class);
				Constants.intDrawerSelection = 4;
				startActivity(i);
			}

		}

		if (v == noti_changepwd) {
			if (prefrence.getString("MemID", "").toString().length() > 0) {
				Intent changepassword = new Intent(MoreActivity.this,
						ChangePasswordActivity.class);
				startActivity(changepassword);
			} else {
				prefrence.edit().putString("fromMore", "changepwd").commit();
				Intent i = new Intent(MoreActivity.this, LoginActivity.class);
				Constants.intDrawerSelection = 4;
				startActivity(i);
			}

		}
		if (v == noti_changeprofile) {
			if (prefrence.getString("MemID", "").toString().length() > 0) {

				Intent changeprofile = new Intent(MoreActivity.this,
						ChangeProfileActivity.class);
				startActivity(changeprofile);
			} else {
				prefrence.edit().putString("fromMore", "changepro").commit();
				Intent i = new Intent(MoreActivity.this, LoginActivity.class);
				Constants.intDrawerSelection = 4;
				startActivity(i);
			}
		}

		if (v == noti_logout) {
			editor.clear().commit();
			Intent intent = new Intent(MoreActivity.this, LoginActivity.class);
			ComponentName cn = intent.getComponent();
			Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
			mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainIntent);
		}

		if (v == noti_ken_s_cal) {

			try {
				Intent myIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://kenjc.org/calendar/"));
				startActivity(myIntent);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(
						this,
						"No application can handle this request."
								+ " Please install a webbrowser",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}

		}
		if (v == noti_buseresrevation) {
			try {
				Intent myIntent = new Intent(Intent.ACTION_VIEW,
						Uri.parse("http://kenjc.org/bus-registration-s/"));
				startActivity(myIntent);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(
						this,
						"No application can handle this request."
								+ " Please install a webbrowser",
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}

	}
}
