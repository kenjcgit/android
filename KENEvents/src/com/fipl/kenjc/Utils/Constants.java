package com.fipl.kenjc.Utils;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.Log;




public class Constants {
	
	
	public static int intDrawerSelection=0;
	public static int intBackbtn=0;
	static File cacheDir;
	public static String SERVER_URL="http://kenjc.org/mobile/app/json.php?";
	public static String IMAGEURL="http://kenjc.org/mobile/app/uploads/events/";
	public static String IMAGEDATAURL="http://kenjc.org/mobile/app/timthumb.php?src=uploads/events/";
	public static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
	          "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
	          "\\@" +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
	          "(" +
	          "\\." +
	          "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
	          ")+"
	      ); 
	public static String DEVICE_ID="";
	public static String PREF="My_Pref";
	 public static String DEVICE_TOKEN="";
	 public static String SENDER_ID="111255332447";
	
	 
	 public static void executeLogcat(Context context){
			if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
				cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"KENJC");
		    else
		        cacheDir=context.getCacheDir();
			
		    if(!cacheDir.exists())
		        cacheDir.mkdirs();
			
		    	File logFile = new File(cacheDir, "logoutput.log"); // log file name
		    	int sizePerFile = 60; // size in kilobytes
		    	int rotationCount = 10; // file rotation count
		    	String filter = "D"; // Debug priority

		    	String[] args = new String[] { "logcat",
		    					"-v", "time",
		    					"-f",logFile.getAbsolutePath(),
		    					"-r", Integer.toString(sizePerFile),
		    					"-n", Integer.toString(rotationCount),
		    					"*:" + filter };

		    	try {
		    		Runtime.getRuntime().exec(args);
		    	} catch (IOException e) {
		    		e.printStackTrace();
		    	}
		    }
		
	}
