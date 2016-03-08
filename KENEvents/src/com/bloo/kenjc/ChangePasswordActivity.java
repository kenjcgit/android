package com.bloo.kenjc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;
import com.fipl.kenjc.Utils.IsNetworkConnection;
import com.fipl.kenjc.Utils.post_async;

public class ChangePasswordActivity extends Activity implements OnClickListener {
	EditText currentpwd, newpwd, repwd;
	Button submit, cancel;
	ImageView backbtn;
	SharedPreferences prefrence;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepassword);
		prefrence = getSharedPreferences(Constants.PREF, 0);
		editor = prefrence.edit();

		init();

	}

	private void init() {
		currentpwd = (EditText) findViewById(R.id.et_curntpwd);
		newpwd = (EditText) findViewById(R.id.et_newpwd);
		repwd = (EditText) findViewById(R.id.et_renwpwd);
		submit = (Button) findViewById(R.id.bt_changepwd_submit);
		cancel = (Button) findViewById(R.id.bt_changepwd_cancel);
		submit.setOnClickListener(this);
		cancel.setOnClickListener(this);
		backbtn = (ImageView) findViewById(R.id.headerbackbtn);
		backbtn.setVisibility(View.VISIBLE);
		backbtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (v == submit) {
			if (TextUtils.isEmpty(currentpwd.getText().toString())) {
				Toast.makeText(this, "Enter Current Psssword", 5000).show();
			} else if (TextUtils.isEmpty(newpwd.getText().toString())) {
				Toast.makeText(this, "Enter New Password", 5000).show();
			} else if (TextUtils.isEmpty(repwd.getText().toString())) {
				Toast.makeText(this, "ReEnter New Password", 5000).show();
			} else if (!currentpwd.getText().toString()
					.equalsIgnoreCase(prefrence.getString("Pwd", ""))) {
				Toast.makeText(getApplicationContext(),
						"Enter correct old password", 5000).show();
			} else if ((newpwd.getText().toString() == null)) {
				Toast.makeText(getApplicationContext(),
						"Password can not be null", 5000).show();
			} else if (!(repwd.getText().toString().equals(newpwd.getText()
					.toString()))) {
				Toast.makeText(getApplicationContext(), "Enter same password",
						5000).show();
			} else {
				if (IsNetworkConnection
						.checkNetworkConnection(ChangePasswordActivity.this)) {

					String json = "[{\"" + "memId" + "\":" + "\""
							+ prefrence.getString("MemID", "") + "\"" + ",\""
							+ "memPassword" + "\":" + "\""
							+ newpwd.getText().toString() + "\"" + "}]";

					editor.putString("password", newpwd.getText().toString());
					String url = Constants.SERVER_URL + "action=ChangePassword";
					new post_async(ChangePasswordActivity.this,
							"changepassword").execute(url, json);
				} else {
					Toast.makeText(ChangePasswordActivity.this, "No Network",
							2000).show();
				}
			}

		}
		if (v == backbtn) {
			Intent backcpwd = new Intent(ChangePasswordActivity.this,
					MoreActivity.class);
			startActivity(backcpwd);
			finish();
		}
		if (v == cancel) {
			finish();
		}

	}

	public void responseofChangePassword(String resultString) {
		try {
			JSONArray jarray = new JSONArray(resultString);
			JSONObject job = jarray.getJSONObject(0);
			String status = job.getString("status");
			if (status.contains("Successfully")) {
				Toast.makeText(ChangePasswordActivity.this, "" + status, 1000)
						.show();
				editor.putString("Pwd", newpwd.getText().toString()).commit();
				finish();
			} else {
				Toast.makeText(ChangePasswordActivity.this, "" + status, 1000)
						.show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
