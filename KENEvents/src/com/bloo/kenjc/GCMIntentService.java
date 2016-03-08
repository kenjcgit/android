package com.bloo.kenjc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.R.array;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.util.Log;

import com.bloo.kenjc.R;
import com.fipl.kenjc.Utils.Constants;
import com.google.android.gcm.GCMBaseIntentService;

@SuppressWarnings("deprecation")
public class GCMIntentService extends GCMBaseIntentService {

	SharedPreferences preferences;
	String userStatusString = "Active";
	Editor editor;
	String ActivityName = "";
	private String Message;
	private HashMap<String, String> msglist = new HashMap<String, String>();
	private ArrayList<HashMap<String, String>> allmsglist = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onError(Context arg0, String arg1) {
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {

		preferences = getSharedPreferences(Constants.PREF, 0);

		msglist = new HashMap<String, String>();
		msglist.put("notiId", arg1.getStringExtra("notiId"));
		msglist.put("msgType", arg1.getStringExtra("msgType"));
		msglist.put("badge", arg1.getStringExtra("badge"));
		msglist.put("type", arg1.getStringExtra("type"));
		msglist.put("msg", arg1.getStringExtra("msg"));
		allmsglist.add(msglist);

		String action = arg1.getAction();

		Log.i("System out", "Got a action!  " + action);

		if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
			String message = arg1.getStringExtra("msg");

			Log.d("System out", arg0.toString() + arg1.toString());

			if (!isApplicationBroughtToBackground(arg0)) {
				if (message.length() > 0) {
					editor = preferences.edit();
					editor.putInt("badge",
							Integer.parseInt(arg1.getStringExtra("badge")));
					editor.putString("PushMsgkey", message);
					editor.commit();

					Intent intent2 = new Intent(this,
							NotificationMsgDisplayActivity.class);
					intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent2);

				}

			} else {
				if (message.length() > 0) {
					editor = preferences.edit();
					editor.putString("PushMsgkey", message);
					editor.putInt("badge",
							Integer.parseInt(arg1.getStringExtra("badge")));
					editor.commit();
					editor = preferences.edit();
					editor.putString("PushMsgkeyFornotificationbar", "\n"
							+ message);
					editor.commit();
					if (allmsglist.size() > 0) {

						showNotification(allmsglist);
					} else {
						showNotification();
					}

				}

			}
		}

	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		preferences = getSharedPreferences("My_Pref", 0);

		final String regId = arg1;
		if (regId != null) {
			if (!regId.equalsIgnoreCase("")) {
				Constants.DEVICE_TOKEN = regId;
			}
		}
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
	}

	protected void showNotification(
			ArrayList<HashMap<String, String>> rcv_message) {
		Notification notif = null;
		NotificationCompat.Builder builder = new Builder(
				getApplicationContext());
		long[] vibrationPattern = { 0, 200, 800, 200, 600, 200, 400, 200, 200,
				200, 100, 200, 50, 200, 50, 200, 50, 200, 50, 200 };
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, IncomingMessageView.class), 0);

		String tickerText = rcv_message.get(0).get("type");
		NotificationCompat.InboxStyle inboxStyle = prepareBigNotificationDetails(rcv_message);
		Uri defaultRingtoneUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		notif = builder
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(tickerText)
				.setContentText(rcv_message.get(0).get("type"))
				.setAutoCancel(true)
				.setContentIntent(contentIntent)
				.setLargeIcon(
						BitmapFactory.decodeResource(getResources(),
								R.drawable.app_icon_android))
				.setWhen(
						Calendar.getInstance().getTimeInMillis() + 1000 * 60 + 60)
				.setVibrate(vibrationPattern).setStyle(inboxStyle)
				.setSound(defaultRingtoneUri).build();

		nm.notify(0, notif);

	}

	protected void showNotification() {
		Notification notif = null;
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, IncomingMessageView.class), 0);

		Log.d("System out", "Got a message rcv!---" + Message);
		String tickerText = Message;
		Log.d("System out", "Got a message ticket!---" + tickerText);
		notif = new Notification(R.drawable.app_icon_android, tickerText,
				System.currentTimeMillis());
		notif.setLatestEventInfo(this, Message, "", contentIntent);
		notif.setLatestEventInfo(this, Message.toString(), "", contentIntent);
		notif.vibrate = new long[] { 100, 250, 100, 500 };
		Uri defaultRingtoneUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		notif.sound = defaultRingtoneUri;
		nm.notify(0, notif);

	}

	private InboxStyle prepareBigNotificationDetails(
			ArrayList<HashMap<String, String>> rcv_message) {

		NotificationCompat.InboxStyle result = new InboxStyle();
		result.setBigContentTitle(rcv_message.size()
				+ "New Events are coming soon");
		for (int i = 0; i < rcv_message.size(); i++) {
			result.setBigContentTitle(rcv_message.get(i).get("msgType"));
			result.addLine(rcv_message.get(i).get("msg"));

		}
		return result;
	}

	private boolean isApplicationBroughtToBackground(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);

		if (!tasks.isEmpty()) {

			Log.d("System out", " ticket 5!---");
			ComponentName topActivity = tasks.get(0).topActivity;
			Log.d("System out", " ticket 5!---");
			ActivityName = topActivity.getClassName().toString();

			if (!topActivity.getPackageName().equals(context.getPackageName())) {
				return true;
			}
		}
		return false;
	}

}
