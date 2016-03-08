package com.fipl.kenjc.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ProgressBar;

import com.bloo.kenjc.ChangePasswordActivity;
import com.bloo.kenjc.ChangeProfileActivity;
import com.bloo.kenjc.ContactsActivity;
import com.bloo.kenjc.EventDetailActivity;
import com.bloo.kenjc.EventsActivity;
import com.bloo.kenjc.LoginActivity;
import com.bloo.kenjc.NotificationSettingsActivity;
import com.bloo.kenjc.NotificationsActivity;
import com.bloo.kenjc.R;
import com.bloo.kenjc.RecoverPasswordActivity;
import com.bloo.kenjc.SignupActivity;

@SuppressLint("NewApi")
public class post_async extends AsyncTask<String, Integer, String> {

	private LoginActivity loginActivity;
	private SignupActivity signupActivity;
	private ChangePasswordActivity changePasswordActivity;
	private ChangeProfileActivity changeProfileActivity;
	private RecoverPasswordActivity recoverPasswordActivity;
	private EventsActivity eventsActivity;
	private NotificationSettingsActivity notificationSettingsActivity;
	private NotificationsActivity notificationsActivity;
	private ContactsActivity contactsActivity;

	EventDetailActivity eventDetailActivity;

	static String action = "", resultString = "";
	Activity activity;
	ProgressBar progressbar;
	Dialog dialog;

	private int action1 = -1;

	public post_async(LoginActivity loginactivity, String action) {
		this.loginActivity = loginactivity;
		this.activity = loginactivity;
		this.action = action;
	}

	public post_async(SignupActivity signupActivity, String action) {
		this.signupActivity = signupActivity;
		this.activity = signupActivity;
		this.action = action;
	}

	public post_async(ChangePasswordActivity changePasswordActivity,
			String action) {
		this.changePasswordActivity = changePasswordActivity;
		this.activity = changePasswordActivity;
		this.action = action;
	}

	public post_async(ContactsActivity contactsActivity, String action) {
		this.contactsActivity = contactsActivity;
		this.activity = contactsActivity;
		this.action = action;
	}

	public post_async(ChangeProfileActivity changeProfileActivity, String action) {
		this.changeProfileActivity = changeProfileActivity;
		this.activity = changeProfileActivity;
		this.action = action;
	}

	public post_async(RecoverPasswordActivity recoverPasswordActivity,
			String action) {
		this.recoverPasswordActivity = recoverPasswordActivity;
		this.activity = recoverPasswordActivity;
		this.action = action;
	}

	public post_async(EventsActivity eventsActivity, String action) {
		this.eventsActivity = eventsActivity;
		this.activity = eventsActivity;
		this.action = action;
	}

	public post_async(
			NotificationSettingsActivity notificationSettingsActivity,
			String action) {
		this.notificationSettingsActivity = notificationSettingsActivity;
		this.activity = notificationSettingsActivity;
		this.action = action;
	}

	

	public post_async(EventDetailActivity eventDetailActivity, String action) {
		this.eventDetailActivity=eventDetailActivity;
		this.activity=eventDetailActivity;
		this.action=action;
	}

	public post_async(NotificationsActivity notificationsActivity,
			String action) {
		this.notificationsActivity=notificationsActivity;
		this.activity=notificationsActivity;
		this.action=action;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		dialog = new Dialog(activity,android.R.style.Theme_Translucent_NoTitleBar);
		progressbar = new ProgressBar(activity);
		progressbar.setBackgroundResource(R.drawable.progress_background);
		dialog.addContentView(progressbar, new LayoutParams(40, 40));
		Window window = dialog.getWindow();
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
		dialog.show();
	}

	@Override
	protected String doInBackground(String... params) {

		if (params.length > 1) {
			invoke(params[0], params[1]);
		} else {
			connect_post(params[0]);
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			dialog.dismiss();
			System.gc();
			Runtime.getRuntime().gc();

		} catch (Exception e) {
			e.printStackTrace();
		}
		sendResult();
	}

	private void sendResult() {

		try {
			if (this.loginActivity != null) {
				if (action.equalsIgnoreCase("Login")) {
					this.loginActivity.responseinserlogin(resultString);
				}
			} else if (this.signupActivity != null) {
				if (action.equalsIgnoreCase("SignUp")) {
					this.signupActivity.responseofSignUp(resultString);
				}

			} else if (this.changePasswordActivity != null) {
				if (action.equalsIgnoreCase("changepassword")) {
					this.changePasswordActivity
							.responseofChangePassword(resultString);
				}
			} else if (this.changeProfileActivity != null) {
				if (action.equalsIgnoreCase("changeprofile")) {
					this.changeProfileActivity
							.responseofChangeProfile(resultString);
				}
			} else if (this.recoverPasswordActivity != null) {
				if (action.equalsIgnoreCase("ForgotPassword")) {
					this.recoverPasswordActivity
							.responseofRecovePassword(resultString);

				}
			} else if (this.eventsActivity != null) {
				if (action.equalsIgnoreCase("updatedeviceinfo"))
			    {
				this.eventsActivity.responseofupdatedeviceinfo(resultString);
			     } 
				
				else  if (action.equalsIgnoreCase("GetEvents"))
				{
					this.eventsActivity.responseofEvents(resultString);
				}
				else if (action.equalsIgnoreCase("GetActivities"))
			    {
				this.eventsActivity.responseofGetActivities(resultString);
				
			     } 
			
			}
			else if (this.notificationsActivity != null) {
				if (action.equalsIgnoreCase("getnotifications"))
				{
				this.notificationsActivity.responseofgetpushnoti(resultString);
				}
				else if (action.equalsIgnoreCase("ResetBadge")) 
				{
					this.notificationsActivity.responseofnotificationcount(resultString);
				}
				
			}
			else if (this.eventDetailActivity != null) {
				if (action.equalsIgnoreCase("AttendEvent")) {
					this.eventDetailActivity.responseofEvents(resultString);
				}
				
			}else if (this.notificationSettingsActivity != null) {
				if (action.equalsIgnoreCase("EditNotificationSettings")) 
				{
					this.notificationSettingsActivity
							.responseofnotificationsettings(resultString);
				}
				
			} else if (this.contactsActivity != null) {
				this.contactsActivity.responseofgetcontact(resultString);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
			Log.i("System out", "Exception : " + e.toString());
		}
	}

	private void connect_post(String obj) {
		try {

			String urlStr = obj.replaceAll(" ", "%20").replaceAll("'", "%27");
			URL url;
			HttpURLConnection connection;
			StringBuffer buffer = null;

			try {
				url = new URL(urlStr);
				connection = (HttpURLConnection) url.openConnection();
				buffer = new StringBuffer();
				InputStreamReader inputReader = new InputStreamReader(
						connection.getInputStream());
				BufferedReader buffReader = new BufferedReader(inputReader);
				String line = "";
				do {
					line = buffReader.readLine();
					if (line != null)
						buffer.append(line);
				} while (line != null);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			resultString = buffer.toString();
		} catch (Exception e) {
		}
	}

	@SuppressWarnings("deprecation")
	public static String invoke(String url, String postString) {

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		String s = "";
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		HttpParams httpParameters = new BasicHttpParams();
		HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
		HttpProtocolParams.setHttpElementCharset(httpParameters, HTTP.UTF_8);

		HttpClient client = new DefaultHttpClient(httpParameters);
		client.getParams().setParameter("http.protocol.version",HttpVersion.HTTP_1_1);
		client.getParams().setParameter("http.socket.timeout",new Integer(2000));
		client.getParams().setParameter("http.protocol.content-charset",HTTP.UTF_8);
		httpParameters.setBooleanParameter("http.protocol.expect-continue",false);
		HttpPost request = new HttpPost(url);
		request.getParams().setParameter("http.socket.timeout",new Integer(5000));

		try {
			org.apache.http.Header[] headers = request.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
			}

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("json", postString));
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs,
					HTTP.UTF_8));

			HttpResponse response = client.execute(request);
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity != null) {
				resultString = EntityUtils.toString(httpEntity).toString();
			}
			httpEntity = null;
			response = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		httppost = null;
		httpclient = null;
		return resultString;
	}
}
