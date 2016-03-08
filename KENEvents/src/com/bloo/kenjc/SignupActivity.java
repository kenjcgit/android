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
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;
import com.fipl.kenjc.Utils.IsNetworkConnection;
import com.fipl.kenjc.Utils.post_async;

public class SignupActivity extends Activity implements OnClickListener{
	EditText MailAddress, Name, phnno,pwd,cpwd;
	RadioButton male, female;
	ImageView backbtn;
	Button signup, cancel;
	TextView dob;
	Gallery signupimage;
	Display display;
	int height, width;
	CheckBox terms;
	RadioGroup RadioGroupsignup;
	RadioButton signup_female, signup_male;
	String Gender;
	Editor editor ;
	
	SharedPreferences preference;
	private TextView termsncond;

	public static String MobilePattern = "[0-9]{10}";
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
   SignupActivity.this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signupscreen);
		display = getWindowManager().getDefaultDisplay();

		preference = getSharedPreferences(Constants.PREF, 0);
		editor = preference.edit();
		
		height = display.getHeight();
		width = display.getWidth();
		init();
 
	}

	private void init() {
		signup = (Button) findViewById(R.id.bt_signup);
		cancel = (Button) findViewById(R.id.bt_signup_cancel);
		dob = (TextView) findViewById(R.id.tv_signup_birthdate);
		backbtn=(ImageView) findViewById(R.id.headerbackbtn);
		backbtn.setVisibility(View.VISIBLE);
		RadioGroupsignup = (RadioGroup) findViewById(R.id.signup_rg);
		signup_male = (RadioButton) findViewById(R.id.rb_male);
		signup_female = (RadioButton) findViewById(R.id.rb_female);
		MailAddress = (EditText) findViewById(R.id.et_signup_add);
		Name = (EditText) findViewById(R.id.et_signup_name);
		phnno = (EditText) findViewById(R.id.et_signup_phnno);
		pwd = (EditText) findViewById(R.id.et_signup_pwd);
		cpwd=(EditText)findViewById(R.id.et_signup_cpwd);
		terms = (CheckBox) findViewById(R.id.cb_terms);
		signupimage = (Gallery) findViewById(R.id.signupgallery);
		termsncond=(TextView)findViewById(R.id.tv_termsncond);
		
		signupimage.getLayoutParams().height = (height * 40) / 100;
		
		signup.setOnClickListener(this);
		termsncond.setOnClickListener(this);
		cancel.setOnClickListener(this);
		terms.setOnClickListener(this);
		signup_female.setOnClickListener(this);
		signup_male.setOnClickListener(this);
		dob.setOnClickListener(this);
		backbtn.setOnClickListener(this);
	}

	private boolean checkEmail(String email) {
		return Constants.EMAIL_ADDRESS_PATTERN.matcher(email).matches();
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
			return new DatePickerDialog(this, datePickerListener, year, month,day);

		}

		return null;
	}

	@Override
	public void onClick(View v) {
		if (v == signup) {
			if (TextUtils.isEmpty(Name.getText().toString())) 
			{
				Toast.makeText(this, "Enter Name", 5000).show();
			}
			
			if (Name.getText().length() > 25)
			{
				Toast.makeText(getApplicationContext(),"pls enter less the 25 characher in user name",Toast.LENGTH_SHORT).show();

			} if (TextUtils.isEmpty(MailAddress.getText().toString())) 
			{
				Toast.makeText(this, "Enter Email Address", 5000).show();
			}
			
			else if (!checkEmail(MailAddress.getText().toString()))
			{
				Toast.makeText(SignupActivity.this,
						"Please enter valid email address", 5000).show();
			} else if (TextUtils.isEmpty(phnno.getText().toString()))
			{
				Toast.makeText(this, "Enter phonenumber", 5000).show();
			} else if (TextUtils.isEmpty(phnno.getText().toString())) 
			{
				Toast.makeText(this, "Enter phonenumber", 5000).show();
			} else if (!phnno.getText().toString().matches(MobilePattern))
			{
				Toast.makeText(SignupActivity.this,
						"Please enter valid 10 digit Phone No", 5000).show();
			} else if (TextUtils.isEmpty(pwd.getText().toString())) 
			{
				Toast.makeText(this, "Enter Password", 5000).show();
			}else if ((cpwd.getText().toString() == null)) 
			{
				Toast.makeText(getApplicationContext(),"Password can not be null", 5000).show();
			} else if (!(cpwd.getText().toString().equals(pwd.getText().toString()))) 
			{
				Toast.makeText(getApplicationContext(), "Enter same password",
						5000).show();
			}
			else if (terms.isChecked()==false) {
				Toast.makeText(getApplicationContext(), "Accept terms and condition first",5000).show();
				
			}else {
				UserSignUp();
				
			}
		}
		
		if (v == signup_female)
		{

			signup_male.setButtonDrawable(R.drawable.opt_btn_unselected);
			signup_female.setButtonDrawable(R.drawable.opt_btn_selected);
			 Gender=signup_female.getText().toString();
		}
	    if (v == signup_male) 
	    {
		signup_female.setButtonDrawable(R.drawable.opt_btn_unselected);
		signup_male.setButtonDrawable(R.drawable.opt_btn_selected);
		Gender=signup_male.getText().toString();
		
		}
		if (v == terms) 
		{
			terms();
		}
		if (v == dob)
		{
			showDialog(1);
		}
		if (v==backbtn)
		{
			Intent i=new Intent(SignupActivity.this,LoginActivity.class);
			startActivity(i);
		}
		if (v==termsncond) {
			Intent i=new Intent(SignupActivity.this,TermsAndConditionActivity.class);
			startActivity(i);
			
		}
		if (v == cancel) 
		{
			Intent i=new Intent(SignupActivity.this,LoginActivity.class);
			startActivity(i);
			finish();
		}
	}
	private void terms() 
	{
		if (((CheckBox) terms).isChecked()) 
		{
			terms.setButtonDrawable(R.drawable.checked);
		
		} else 
		{
			terms.setButtonDrawable(R.drawable.unchecked);
		}
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		private String stYear;
		private String stMonth;
		private String stDay;
		private String stDate;

		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) 
		{
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
				
				
				
				if (ddate.before(new Date()) ) 
				{
					if (str.equals(s)) {
					
					Toast.makeText(SignupActivity.this,"Cannot Enter Current Date", Toast.LENGTH_LONG).show();
				   }
					else
					{
						dob.setText(str + "");
					}
					
				} else 
				{
					Toast.makeText(SignupActivity.this,"Please select past date", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
			}
		}

	};


	private void UserSignUp() {
		
		String d=dob.getText().toString();
		
		String inputPattern = "MM-dd-yyyy";
		String outputPattern = "yyyy-MM-dd";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date ddate = null;
		String str = null;

		try {
			ddate = inputFormat.parse(d);
			str = outputFormat.format(ddate);

		} catch (Exception e) {
			e.printStackTrace();
		}
	
		if(IsNetworkConnection.checkNetworkConnection(SignupActivity.this))
		{
		String json = "[{\"" + "memName" + "\":" + "\""+ Name.getText().toString() + "\"" + ",\"" 
				+ "memMobile"+ "\":" + "\"" + phnno.getText().toString() + "\"" + ",\""
				+ "memEmail" + "\":" + "\""+ MailAddress.getText().toString() + "\"" + ",\"" 
				+ "memDob"+ "\":" + "\"" + str + "\"" + ",\""
				+ "memGender" + "\":" + "\"" +Gender + "\"" + ",\""
				+ "memDeviceType" + "\":" + "\""+  "android"+ "\"" + ",\""
				+ "memUDID" + "\":" + "\""+Constants.DEVICE_TOKEN+ "\"" + ",\""
				+ "memPassword" + "\":" + "\""+ pwd.getText().toString() + "\"" +"}]";
	
		String url = Constants.SERVER_URL + "action=SignUp";
		new post_async(SignupActivity.this, "SignUp").execute(url, json);
		
		}
		else {
			Toast.makeText(SignupActivity.this, "No Network", 2000).show();
		}
		

	}
	public void responseofSignUp(String resultString) {
		 try {
				JSONArray jarray =new JSONArray(resultString);
				JSONObject job=jarray.getJSONObject(0);
				String memId=job.getString("memId");
				String status=job.getString("status");
				if(!memId.equalsIgnoreCase("0")){
					Toast.makeText(SignupActivity.this, "Signup done Successfully ", 1000).show();				
					if(preference.getString("fromMore", "").equalsIgnoreCase("more")){
						editor.putString("fromMore", "").commit();
						Intent login = new Intent(SignupActivity.this, MoreActivity.class);
						startActivity(login);
						finish();
					}else if(preference.getString("fromMore", "").equalsIgnoreCase("eventdetail")){
						Intent login = new Intent(SignupActivity.this, EventDetailActivity.class);
						startActivity(login);
						editor.putString("fromMore", "").commit();
						finish();
					}else{
						Intent login = new Intent(SignupActivity.this, EventsActivity.class);
						Constants.intDrawerSelection= 1;
						startActivity(login);
						finish();
					}
				}else{
					Toast.makeText(SignupActivity.this, "Problem in Signup", 1000).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}
}
