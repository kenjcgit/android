package com.fipl.kenjc.fb;



import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Window;

import com.bloo.kenjc.EventDetailActivity;
import com.fipl.kenjc.Utils.Constants;
import com.fipl.kenjc.fb.Facebook.DialogListener;


public class FbLoginCommon 
{

	private final Facebook facebook = new Facebook("1434012063564344");
	private SharedPreferences mPrefs, myPref;
	private AsyncFacebookRunner mAsyncRunner;

	private Dialog dialog;

	float scale;
	int orientation;
	float[] dimensions;
	String postString;
	static final float[] DIMENSIONS_DIFF_LANDSCAPE = { 20, 60 };
	static final float[] DIMENSIONS_DIFF_PORTRAIT = { 40, 60 };

	private String fb_id = "", fb_email = "", fb_firstname = "",
			fb_username = "", fb_lastname = "", fb_gender = "",
			fb_birthday = "", fb_location = "";

	private Display display; // Provides information about the display size and
	// density.
	private int width, height; // store the screen height and width of the
	// screen
	private SharedPreferences.Editor editor, editor1;
	private Activity activity;
	private String actionHomePage, actionFbFriend;;
	private EventDetailActivity Eventdetailactivity;

	public FbLoginCommon(EventDetailActivity Eventdetailactivity, String action)
	{
		// TODO Auto-generated constructor stub
		this.Eventdetailactivity = Eventdetailactivity;
		this.activity = Eventdetailactivity;
		this.postString=action;
		connectToFB();
	}
	
	private void connectToFB(){
		
		myPref = this.activity.getSharedPreferences(Constants.PREF,0);
		
		// to get the screen height/width
		display = activity.getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		
		dialog = new Dialog(activity);// , R.style.CustomDialogTheme);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		scale = activity.getResources().getDisplayMetrics().density;
		orientation = activity.getResources().getConfiguration().orientation;
		dimensions = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? DIMENSIONS_DIFF_LANDSCAPE
				: DIMENSIONS_DIFF_PORTRAIT;
		
		/*
		 * Get existing access_token if any
		 */
		mPrefs = activity.getSharedPreferences(Constants.PREF, 0);/*getPreferences(MODE_PRIVATE);*/
		String access_token = mPrefs.getString("access_token", null);
		long expires = mPrefs.getLong("access_expires", 0);
		if (access_token != null) {
			facebook.setAccessToken(access_token);
		}
		if (expires != 0) {
			facebook.setAccessExpires(expires);
		}
		
		/*
		 * Only call authorize if the access_token has expired.
		 */
//		Log.i("System out","access_token:"+access_token);
//		Log.i("System out","expires:"+expires);
//		Log.i("System out","facebook.isSessionValid():"+facebook.isSessionValid());
		if (!facebook.isSessionValid()) {
			Log.i("System out","SESSION IS NOT VALID");
			facebook.authorize(activity,
					new String[] {"user_birthday", "email", "publish_checkins","user_photos","publish_actions","read_stream","publish_stream" },
					Facebook.FORCE_DIALOG_AUTH,
					new DialogListener() {
//						@Override
						public void onComplete(Bundle values) {
							
							editor1 = myPref.edit();
							
							editor = mPrefs.edit();
							editor.putString("access_token",
									facebook.getAccessToken());
							editor.putLong("access_expires",
									facebook.getAccessExpires());
							editor.commit();

							new Thread(new Runnable() {
								
//								@Override
								public void run() {
									// TODO Auto-generated method stub
									JSONObject jObjectFB;
									try {
										jObjectFB = new JSONObject(facebook
												.request("me"));
										Log.i("System out", "res: jObjectFB :" + jObjectFB.toString());
										editor = mPrefs.edit();
										
										if(jObjectFB.has("id"))
											fb_id = jObjectFB.getString("id");
										if(jObjectFB.has("name"))
											fb_username = jObjectFB.getString("name");
										if(jObjectFB.has("email"))
											fb_email = jObjectFB.getString("email");
										if(jObjectFB.has("first_name"))
											fb_firstname = jObjectFB.getString("first_name");
										if(jObjectFB.has("last_name"))
											fb_lastname = jObjectFB.getString("last_name");
										if(jObjectFB.has("gender"))
												fb_gender = jObjectFB.getString("gender");
										if(jObjectFB.has("birthday"))
												fb_birthday = jObjectFB.getString("birthday");
										if(jObjectFB.has("location")){
											JSONObject locObj = jObjectFB.getJSONObject("location");
											if(locObj.has("name"))
												fb_location = locObj.getString("name");
										}
										
										
										editor.putString("fb_username", fb_username);
										editor.putString("fb_email", fb_email);
										editor.putString("fb_id", fb_id);
										editor.putString("fb_firstname", fb_firstname);
										editor.putString("fb_lastname", fb_lastname);
										editor.putString("fb_gender", fb_gender);
										editor.putString("fb_birthday", fb_birthday);
										editor.putString("fb_location", fb_location);
										editor.commit();
										
										handler.sendEmptyMessage(0);
										
//										setResult(MyConstant.FB_LOGIN_SUCCESS);
//										FacebookTest1Activity.this.finish();
									} catch (MalformedURLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
//										setResult(MyConstant.PARSING_ERROR);
//										FacebookTest1Activity.this.finish();
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
//										setResult(MyConstant.PARSING_ERROR);
//										FacebookTest1Activity.this.finish();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
//										setResult(MyConstant.PARSING_ERROR);
//										FacebookTest1Activity.this.finish();
									}
								}
							}).start();
						}

//						@Override
						public void onFacebookError(FacebookError error) {
							Log.i("System out", "onFB Error " + error);
//							setResult(MyConstant.FB_LOGIN_FAILED);
//							FacebookTest1Activity.this.finish();
													
						}

//						@Override
						public void onCancel() {
							Log.i("System out", "onCancel");
//							setResult(MyConstant.FB_LOGIN_FAILED);
//							FacebookTest1Activity.this.finish();
							
						}

//						@Override
						public void onError(DialogError e) {
							// TODO Auto-generated method stub
							Log.i("System out", "onError " + e.getMessage());
//							setResult(MyConstant.FB_LOGIN_FAILED);
//							FacebookTest1Activity.this.finish();
							
						}
					});			
		}else {
//			handler.sendEmptyMessage(0);
		}
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {	
				if(Eventdetailactivity!=null){
					Eventdetailactivity.postOnWall(postString);
				}
				
					
		}
	};	

}
