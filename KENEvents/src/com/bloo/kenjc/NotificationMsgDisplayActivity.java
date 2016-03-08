
package com.bloo.kenjc;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bloo.kenjc.R;

public class NotificationMsgDisplayActivity extends Activity {

	private SharedPreferences preferences;
	Editor editor;

	String Message = "", whichActivityOpenNamekey = "";
	private SharedPreferences preferencesRemember;
	TextView pushtext;
	Button pushok;
	int Badgecount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pushdialog);
		preferences = getSharedPreferences("My_Pref", 0);
		editor = preferences.edit();

		pushtext = (TextView) findViewById(R.id.txt_message);
		pushok = (Button) findViewById(R.id.btn_ok);

		whichActivityOpenNamekey = preferences.getString(
				"whichActivityOpenNamekey", "");
		Message = preferences.getString("PushMsgkey", "");

		pushtext.setText(Message);

		pushok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Badgecount = Integer.parseInt(preferences.getString(
						"memBadgeCount", "0"));
				Badgecount = Badgecount + 1;
				editor.putString("memBadgeCount", Badgecount + "").commit();
				if (preferences.getInt("badge", 0) == 0) {

				} else {
					int intget = preferences.getInt("badge", 0) - 1;
					editor.putInt("", intget);
				}
				finish();
			}
		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			setResult(100);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
