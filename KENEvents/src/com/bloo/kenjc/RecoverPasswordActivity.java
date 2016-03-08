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
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;
import com.fipl.kenjc.Utils.IsNetworkConnection;
import com.fipl.kenjc.Utils.post_async;

public class RecoverPasswordActivity extends Activity {
	Button recoverpwd;
	EditText EmailAddress;
	ImageView backbtn;
	Gallery rcvrpwdimage;
	Display display;
	int height, width;
	SharedPreferences prefrence;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recoverpassword);
		display = getWindowManager().getDefaultDisplay();
		height = display.getHeight();
		width = display.getWidth();
		prefrence = getSharedPreferences(Constants.PREF, 0);
		editor = prefrence.edit();

		recoverpwd = (Button) findViewById(R.id.bt_rcvrpwd);
		EmailAddress = (EditText) findViewById(R.id.et_rem_madd);
		backbtn = (ImageView) findViewById(R.id.headerbackbtn);
		rcvrpwdimage = (Gallery) findViewById(R.id.galleryrecoverpwd);
		rcvrpwdimage.getLayoutParams().height = (height * 30) / 100;
		backbtn.setVisibility(View.VISIBLE);
		backbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(RecoverPasswordActivity.this,LoginActivity.class);
				startActivity(i);
				finish();
			}
		});
		recoverpwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!checkEmail(EmailAddress.getText().toString())) {
					Toast.makeText(getApplicationContext(),"Enter Valid Email Address", Toast.LENGTH_SHORT).show();
				} else {
					if(IsNetworkConnection.checkNetworkConnection(RecoverPasswordActivity.this))
					{
					String json = "[{\"" + "memEmail" + "\":" + "\""
							+ EmailAddress.getText().toString() + "\"" + "}]";

					String url = Constants.SERVER_URL + "action=ForgotPassword";
					Log.d("System out", "url" + url);
					new post_async(RecoverPasswordActivity.this,"ForgotPassword").execute(url, json);
					}
					else {
						Toast.makeText(RecoverPasswordActivity.this, "No Network", 2000).show();
					}

				}

			}

		});
	}

	private boolean checkEmail(String email) {
		return Constants.EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	public void responseofRecovePassword(String resultString) {
		try {
			JSONArray jarray = new JSONArray(resultString);
			JSONObject job = jarray.getJSONObject(0);
			String status = job.getString("status");
			if (status.contains("true")) {
				Toast.makeText(RecoverPasswordActivity.this, "Password has been sent to your email id" , 1000).show();
				finish();
			} else {
				Toast.makeText(RecoverPasswordActivity.this, "Enter Valid Email Address", 1000).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
