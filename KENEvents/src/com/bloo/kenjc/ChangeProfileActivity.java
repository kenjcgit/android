package com.bloo.kenjc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;
import com.fipl.kenjc.Utils.IsNetworkConnection;
import com.fipl.kenjc.Utils.post_async;

public class ChangeProfileActivity extends Activity implements OnClickListener {
	Button submit, cancel;
	EditText Name, MailAddress, PhoneNo;
	RadioButton Male, Female;
	ImageView backbtn;
	String Gender;
	SharedPreferences prefrence;
	Editor editor;
	TextView dob;

	public static String MobilePattern = "[0-9]{10}";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changeprofile);
		prefrence = getSharedPreferences(Constants.PREF, 0);
		editor = prefrence.edit();
		init();
		Name.setText(prefrence.getString("name", ""));
		MailAddress.setText(prefrence.getString("email", ""));
		PhoneNo.setText(prefrence.getString("mobile", ""));

		String inputPattern = "yyyy-MM-dd";
		String outputPattern = "MM-dd-yyyy";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
		Date ddate = null;
		String str = null;
		try {
			ddate = inputFormat.parse(prefrence.getString("dob", ""));
			str = outputFormat.format(ddate);
		} catch (Exception e) {
		}

		dob.setText("" + str);
		String gender = prefrence.getString("gender", "");
		if (gender.equalsIgnoreCase("Male")) {
			Female.setButtonDrawable(R.drawable.opt_btn_unselected);
			Male.setButtonDrawable(R.drawable.opt_btn_selected);
			Gender = gender;
		} else {
			Male.setButtonDrawable(R.drawable.opt_btn_unselected);
			Female.setButtonDrawable(R.drawable.opt_btn_selected);
			Gender = gender;
		}
	}

	private void init() {
		submit = (Button) findViewById(R.id.bt_changepro_submit);
		cancel = (Button) findViewById(R.id.bt_changepro_cancel);
		Name = (EditText) findViewById(R.id.et_name);
		MailAddress = (EditText) findViewById(R.id.et_add);
		PhoneNo = (EditText) findViewById(R.id.et_phnno);
		Male = (RadioButton) findViewById(R.id.rb_male);
		Female = (RadioButton) findViewById(R.id.rb_female);
		backbtn = (ImageView) findViewById(R.id.headerbackbtn);
		dob = (TextView) findViewById(R.id.bt_changepro_date);
		backbtn.setVisibility(View.VISIBLE);
		Male.setOnClickListener(this);
		Female.setOnClickListener(this);
		cancel.setOnClickListener(this);
		submit.setOnClickListener(this);
		dob.setOnClickListener(this);
		backbtn.setOnClickListener(this);
	}

	protected Dialog onCreateDialog(int id) {
		int year;
		int month;
		int day;
		switch (id) {

		case 1:
			final Calendar c1 = Calendar.getInstance();
			year = c1.get(Calendar.YEAR);
			month = c1.get(Calendar.MONTH);
			day = c1.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);

		}

		return null;
	}

	@Override
	public void onClick(View v) {

		if (v == submit) {
			if (TextUtils.isEmpty(Name.getText().toString())) {
				Toast.makeText(this, "Enter Name", 5000).show();
			} else if (Name.getText().length() > 25) {
				Toast.makeText(getApplicationContext(),
						"pls enter less the 25 characher in user name",
						Toast.LENGTH_SHORT).show();

			} else if (!checkEmail(MailAddress.getText().toString())) {
				Toast.makeText(ChangeProfileActivity.this,
						"Please enter valid email address", 5000).show();
			} else if (TextUtils.isEmpty(PhoneNo.getText().toString())) {
				Toast.makeText(this, "Enter phonenumber", 5000).show();
			} else if (TextUtils.isEmpty(PhoneNo.getText().toString())) {
				Toast.makeText(this, "Enter phonenumber", 5000).show();
			} else if (!PhoneNo.getText().toString().matches(MobilePattern)) {
				Toast.makeText(ChangeProfileActivity.this,
						"Please enter valid 10 digit Phone No", 5000).show();
			} else {
				String d = dob.getText().toString();
				String inputPattern = "MM-dd-yyyy";
				String outputPattern = "yyyy-MM-dd";
				SimpleDateFormat inputFormat = new SimpleDateFormat(
						inputPattern);
				SimpleDateFormat outputFormat = new SimpleDateFormat(
						outputPattern);
				Date ddate = null;
				String str = null;
				try {
					ddate = inputFormat.parse(d);
					str = outputFormat.format(ddate);
				} catch (Exception e) {
				}
				if (IsNetworkConnection
						.checkNetworkConnection(ChangeProfileActivity.this)) {
					String json = "[{\"" + "memName" + "\":" + "\""
							+ Name.getText().toString() + "\"" + ",\""
							+ "memMobile" + "\":" + "\""
							+ PhoneNo.getText().toString() + "\"" + "," + "\""
							+ "memEmail" + "\":" + "\""
							+ MailAddress.getText().toString() + "\"" + ",\""
							+ "memDob" + "\":" + "\"" + str + "\"" + ",\""
							+ "memGender" + "\":" + "\"" + Gender + "\""
							+ ",\"" + "memDeviceType" + "\":" + "\""
							+ "android" + "\"" + ",\"" + "memUDID" + "\":"
							+ "\"" + prefrence.getString("udid", "") + "\""
							+ ",\"" + "memId" + "\":" + "\""
							+ prefrence.getString("MemID", "") + "\"" + "}]";

					String url = Constants.SERVER_URL + "action=EditProfile";
					editor.putString("email", MailAddress.getText().toString());
					new post_async(ChangeProfileActivity.this, "changeprofile")
							.execute(url, json);
				} else {
					Toast.makeText(ChangeProfileActivity.this, "No Network",
							2000).show();
				}
			}
		}
		if (v == dob) {
			showDialog(1);
		}

		if (v == Female) {
			Male.setButtonDrawable(R.drawable.opt_btn_unselected);
			Female.setButtonDrawable(R.drawable.opt_btn_selected);
			Gender = Female.getText().toString();
		}
		if (v == Male) {
			Female.setButtonDrawable(R.drawable.opt_btn_unselected);
			Male.setButtonDrawable(R.drawable.opt_btn_selected);
			Gender = Male.getText().toString();

		}
		if (v == cancel) {
			finish();

		}
		if (v == backbtn) {
			Intent backcp = new Intent(ChangeProfileActivity.this,
					MoreActivity.class);
			startActivity(backcp);
			finish();
		}

	}

	private boolean checkEmail(String email) {
		return Constants.EMAIL_ADDRESS_PATTERN.matcher(email).matches();

	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		private String stYear;
		private String stMonth;
		private String stDay;
		private String stDate;

		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			stYear = "" + selectedYear;

			selectedMonth += 1;
			if (selectedMonth < 10) {
				stMonth = "" + 0 + selectedMonth;
			} else {
				stMonth = "" + selectedMonth;
			}
			if (selectedDay < 10) {
				stDay = "" + 0 + selectedDay;
			} else {
				stDay = "" + selectedDay;
			}
			stDate = stDay + "-" + stMonth + "-" + stYear;
			String dateofbirth = stDate;
			String inputPattern = "dd-MM-yyyy";
			String outputPattern = "MM-dd-yyyy";
			SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
			SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

			Date ddate = null;
			String str = null;

			try {
				ddate = inputFormat.parse(dateofbirth);
				str = outputFormat.format(ddate);
				Date now = new Date(System.currentTimeMillis());

				SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
				String s = formatter.format(now);
				System.out.println(s);

				if (ddate.before(new Date())) {
					if (str.equals(s)) {

						Toast.makeText(ChangeProfileActivity.this,
								"Cannot Enter Current Date", Toast.LENGTH_LONG)
								.show();
					} else {
						dob.setText(str + "");
					}

				} else {
					Toast.makeText(ChangeProfileActivity.this,
							"Please select past date", Toast.LENGTH_LONG)
							.show();
				}
			} catch (Exception e) {
			}
		}

	};

	public void responseofChangeProfile(String resultString) {
		try {
			JSONArray jarray = new JSONArray(resultString);
			JSONObject job = jarray.getJSONObject(0);
			String status = job.getString("status");
			if (status.equalsIgnoreCase("true")) {
				Toast.makeText(ChangeProfileActivity.this,
						"Your Profile Edited Successfully", 1000).show();
				editor.putString("name", Name.getText().toString());
				editor.putString("email", MailAddress.getText().toString());
				editor.putString("mobile", PhoneNo.getText().toString());
				editor.putString("dob", dob.getText().toString());
				editor.putString("gender", Gender);
				editor.commit();
				finish();
			} else {
				Toast.makeText(ChangeProfileActivity.this,
						"Some Problem occurred", 1000).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
