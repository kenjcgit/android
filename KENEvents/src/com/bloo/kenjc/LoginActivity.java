package com.bloo.kenjc;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;
import com.fipl.kenjc.Utils.IsNetworkConnection;
import com.fipl.kenjc.Utils.post_async;
import com.google.android.gcm.GCMRegistrar;

public class LoginActivity extends Activity implements OnClickListener,
		OnCheckedChangeListener {

	EditText MailAddress, Password;
	CheckBox autologin;
	TextView forgotpwd;
	TextView clickhere;
	Button signin, cancel;
	Display display;
	int height, width;
	Gallery loginimage;
	Editor editor;
	SharedPreferences preference;
	private CheckBox remember;

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (preference.getString("fromMore", "").toString().length() > 0) {
			Intent i = new Intent(LoginActivity.this, MoreActivity.class);

			Constants.intDrawerSelection = 4;
			startActivity(i);
			finish();
		} else {

			Intent i = new Intent(LoginActivity.this, EventsActivity.class);
			Constants.intDrawerSelection = 1;
			startActivity(i);
			finish();
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginscreen);
		display = getWindowManager().getDefaultDisplay();
		height = display.getHeight();
		width = display.getWidth();
		preference = getSharedPreferences(Constants.PREF, 0);
		editor = preference.edit();
		init();
		if (IsNetworkConnection.checkNetworkConnection(LoginActivity.this)) {

			RegisterWithGCM();
			getUUID();
		} else {
			Toast.makeText(LoginActivity.this, "No Network", 2000).show();
		}

		if (preference.getBoolean("isremember", true) == true) {

			MailAddress.setText(preference.getString("useremail", ""));
			Password.setText(preference.getString("password", ""));
		}
	}

	private void init() {
		MailAddress = (EditText) findViewById(R.id.et_login_madd);
		Password = (EditText) findViewById(R.id.et_login_pwd);
		forgotpwd = (TextView) findViewById(R.id.tv_frgtpwd);
		clickhere = (TextView) findViewById(R.id.tv_login_nwuser_click);
		signin = (Button) findViewById(R.id.bt_login_signin);
		remember = (CheckBox) findViewById(R.id.cb_autologin);
		cancel = (Button) findViewById(R.id.bt_login_cancel);
		loginimage = (Gallery) findViewById(R.id.logingallery);

		loginimage.getLayoutParams().height = (height * 40) / 100;
		remember.setOnClickListener(this);
		remember.setOnCheckedChangeListener(this);
		cancel.setOnClickListener(this);
		signin.setOnClickListener(this);
		clickhere.setOnClickListener(this);
		forgotpwd.setOnClickListener(this);
	}

	private boolean checkEmail(String email) {
		return Constants.EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	@SuppressWarnings("deprecation")
	private void RegisterWithGCM() {

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			GCMRegistrar.register(this, Constants.SENDER_ID); // Note: get the

		} else {
			Constants.DEVICE_TOKEN = regId;
		}
	}

	public void getUUID() {

		String android_id = Secure.getString(getApplicationContext()
				.getContentResolver(), Secure.ANDROID_ID);

		final TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		String UUID = deviceUuid.toString();
		Constants.DEVICE_ID = tmDevice;

		editor.putString("UUID", UUID + "");
		editor.commit();

	}

	@Override
	public void onClick(View v) {
		if (v == cancel) {
			if (preference.getString("fromMore", "").toString().length() > 0) {
				Intent i = new Intent(LoginActivity.this, MoreActivity.class);

				Constants.intDrawerSelection = 4;
				startActivity(i);
				finish();
			} else {

				Intent i = new Intent(LoginActivity.this, EventsActivity.class);
				Constants.intDrawerSelection = 1;
				startActivity(i);
				finish();
			}

		}
		if (v == signin) {

			if (!checkEmail(MailAddress.getText().toString())) {
				Toast.makeText(LoginActivity.this, "Enter Your Email id", 5000)
						.show();

			} else if (TextUtils.isEmpty(Password.getText().toString())) {
				Toast.makeText(this, "Enter Password", 5000).show();
			} else {
				loginapi();

			}

		}
		if (v == clickhere) {
			Intent signup = new Intent(LoginActivity.this, SignupActivity.class);
			startActivity(signup);
		}
		if (v == forgotpwd) {

			Intent recoverpwd = new Intent(LoginActivity.this,
					RecoverPasswordActivity.class);
			startActivity(recoverpwd);

		}
		if (v == remember) {

		}

	}

	private void loginapi() {
		if (IsNetworkConnection.checkNetworkConnection(LoginActivity.this)) {
			String jsn = "[{\"" + "memEmail" + "\":" + "\""
					+ MailAddress.getText().toString() + "\"" + ",\""
					+ "memPassword" + "\":" + "\""
					+ Password.getText().toString() + "\"" + "}]";

			String url = Constants.SERVER_URL + "action=Login";

			new post_async(LoginActivity.this, "Login").execute(url, jsn);
		} else {
			Toast.makeText(LoginActivity.this, "No Network", 2000).show();
		}
	}

	public void responseinserlogin(String resultString) {

		try {

			JSONArray jarray = new JSONArray(resultString);
			for (int i = 0; i < jarray.length(); i++) {

				JSONObject jobj = jarray.getJSONObject(0);

				if (jobj.has("memId")) {

					editor.putString("MemID", jobj.getString("memId"));
					editor.putString("name", jobj.getString("memName"));
					editor.putString("mobile", jobj.getString("memMobile"));
					editor.putString("email", jobj.getString("memEmail"));
					editor.putString("dob", jobj.getString("memDob"));
					editor.putString("gender", jobj.getString("memGender"));
					editor.putString("status", jobj.getString("memStatus"));
					editor.putString("devicetype",
							jobj.getString("memDeviceType"));
					editor.putString("udid", jobj.getString("memUDID"));
					editor.putString("nwevtpush",
							jobj.getString("memNewEventPush"));
					editor.putString("generalpush",
							jobj.getString("memGeneralPush"));
					editor.putString("actremindpush",
							jobj.getString("memActivityReminderPush"));
					editor.putString("eventremindpush",
							jobj.getString("memEventReminderPush"));
					editor.putString("mempush", jobj.getString("memPush"));
					editor.putString("memETC", jobj.getString("memETC"));
					editor.putString("memAdultActivities",
							jobj.getString("memAdultActivities"));
					editor.putString("memYouthActivities",
							jobj.getString("memYouthActivities"));
					editor.putString("memMiniMaccabiActivities",
							jobj.getString("memMiniMaccabiActivities"));
					editor.putString("memBogrim", jobj.getString("memBogrim"));
					editor.putString("memNonCommunityEvents",
							jobj.getString("memNonCommunityEvents"));
					editor.putString("memcreateddate",
							jobj.getString("memCreatedDate"));
					editor.putString("memUserOrigin",
							jobj.getString("memUserOrigin"));

					editor.putString("Pwd", Password.getText().toString());
					editor.putString("memBadgeCount",
							jobj.getString("memBadgeCount"));
					editor.putString("user_mail", MailAddress.getText()
							.toString());
					editor.commit();
					if (preference.getString("fromMore", "").equalsIgnoreCase(
							"nsettings")) {
						editor.putString("fromMore", "").commit();
						Intent login = new Intent(LoginActivity.this,
								MoreActivity.class);
						startActivity(login);
						finish();
					} else if (preference.getString("fromMore", "")
							.equalsIgnoreCase("changepwd")) {
						editor.putString("fromMore", "").commit();
						Intent login = new Intent(LoginActivity.this,
								MoreActivity.class);
						startActivity(login);
						finish();
					} else if (preference.getString("fromMore", "")
							.equalsIgnoreCase("changepro")) {
						editor.putString("fromMore", "").commit();
						Intent login = new Intent(LoginActivity.this,
								MoreActivity.class);
						startActivity(login);
						finish();
					} else if (preference.getString("fromMore", "")
							.equalsIgnoreCase("eventdetail")) {
						Intent login = new Intent(LoginActivity.this,
								EventsActivity.class);
						Constants.intDrawerSelection = 1;
						startActivity(login);
						finish();
					} else if (preference.getString("fromMore", "")
							.equalsIgnoreCase("notification")) {
						editor.putString("fromMore", "").commit();

						LoginActivity.this.finish();
					}

					else {
						Intent login = new Intent(LoginActivity.this,
								EventsActivity.class);
						Constants.intDrawerSelection = 1;
						startActivity(login);
						finish();
					}
				}

				else {
					Toast.makeText(LoginActivity.this,
							jobj.getString("status"), 3000).show();
				}
			}
		} catch (Exception e) {
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {

			remember.setButtonDrawable(R.drawable.checked);
			editor.putString("useremail", MailAddress.getText().toString());
			editor.putString("password", Password.getText().toString());
			editor.putBoolean("isremember", true);
			editor.commit();

		} else {

			remember.setButtonDrawable(R.drawable.unchecked);
			editor.putString("useremail", "");
			editor.putString("password", "");
			editor.putBoolean("isremember", false);
			editor.commit();
		}

	}

}
