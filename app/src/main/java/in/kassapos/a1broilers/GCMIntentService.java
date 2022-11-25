package in.kassapos.a1broilers;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.gson.Gson;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import in.kassapos.a1broilers.api.Ordermaster;
import in.kassapos.a1broilers.api.ReceivedData;
import in.kassapos.a1broilers.api.SSENotificationData;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCM Service";

	// Use your PROJECT ID from Google API into SENDER_ID
	public static final String SENDER_ID = "857814646218";

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		SharedPreferences sett = SplashScreenActivity.getSettings();
		if(sett!=null){
			Editor edit = SplashScreenActivity.getSettings().edit();
			edit.putString(SplashScreenActivity.GCMREGID, registrationId);
			edit.apply();
			sendMessage(registrationId, "gcmregistered");
		}
		Log.i(TAG, "onRegistered: registrationId=" + registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Editor edit = SplashScreenActivity.getSettings().edit();
		edit.putString(SplashScreenActivity.GCMREGID, null);
		edit.apply();
		Log.i(TAG, "onUnregistered: registrationId=" + registrationId);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onMessage(Context context, Intent data) {
		String message;
		// Message from PHP server
		String title=data.getStringExtra("title");
		message = data.getStringExtra("message");
		if(title!=null) {
			Log.e(title, message);
		}
		if(SplashScreenActivity.userinfo==null){
			return;
		}
		if(title.equalsIgnoreCase("")){
			SSENotificationData sseNotificationData=new Gson().fromJson(message,SSENotificationData.class);

				String text="";
				switch (sseNotificationData.getKey()){
					case "accepted":{
						text="Your Order Accepted by Shop. Order No : "+new Gson().fromJson(sseNotificationData.getData(), Ordermaster.class).ordermasterid;
						break;
						//makePopup("ordercanceled");break;
					}
					case "delivered":{
						text="Your Order Delivered Succesfully. Your Order No : "+new Gson().fromJson(sseNotificationData.getData(), Ordermaster.class).ordermasterid+". Thank you for using Antony Chicken Express";

						//makePopup(sseNotificationData.getKey());
						break;
					}
					case "newoffer":{
						text=sseNotificationData.data;
						break;
					}
					case "driverAccepted":{

					}
				}
			if(text.equalsIgnoreCase("")){
				return;
			}
			ReceivedData receivedData=new ReceivedData();
			receivedData.data=text;
			SplashScreenActivity.getDb().addObject(receivedData);
			Intent intent = new Intent(this, Notifications.class);
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			// Create the notification with a notification builder
			@SuppressWarnings("deprecation")
			Notification notification = new Notification.Builder(this)
					.setSmallIcon(R.drawable.an)
					.setWhen(System.currentTimeMillis())
					.setContentTitle(getString(R.string.app_name))
					.setContentText(text).setContentIntent(pIntent)
					.setSound(Uri.parse("android.resource://" + context.getPackageName()+"/" + R.raw.pedantic))
					.getNotification();
			Log.e(context.getPackageName(),context.getPackageName());
			// Remove the notification on click
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			manager.notify(R.string.app_name, notification);
			{
				// Wake Android Device when notification received
				PowerManager pm = (PowerManager) context
						.getSystemService(Context.POWER_SERVICE);
				@SuppressWarnings("deprecation")
				final PowerManager.WakeLock mWakelock = pm.newWakeLock(
						PowerManager.FULL_WAKE_LOCK
								| PowerManager.ACQUIRE_CAUSES_WAKEUP, "GCM_PUSH");
				mWakelock.acquire();

				// Timer before putting Android Device to sleep mode.
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					public void run() {
						mWakelock.release();
					}
				};
				timer.schedule(task, 5000);
			}


		}
		/*
		if(title.equalsIgnoreCase("neworder")||title.equalsIgnoreCase("newclientdeliveryrequest")){
			
			if(!AppConstants.updatelocationstatus){
				return;
			}
			if(isRunning("in.kassapos.gourmeda.gps.map.WelcomeActivity")){
				if(WelcomeActivity.getForground()){
					makeNotificationSound();
					if(title.equalsIgnoreCase("neworder")){
						sendMessage(message, "neworder");
					}else{
						sendMessage(message, "newdriverrequest");
					}
					return;
				}else{					
					if(title.equalsIgnoreCase("neworder")){
						sendMessage(message, "neworder");
						makePopup();
					}else{
						sendMessage(message, "newdriverrequest");
						makePopup("newdriverrequest");
					}
				}
				
			}else{
				if(title.equalsIgnoreCase("newclientdeliveryrequest")){
					makePopup("newdriverrequest");
				}else{
					makePopup();
				}
				
			}
			
			// Open a new activity called GCMMessageView
			Intent intent = new Intent(this, First.class);
			//Intent intent = new Intent(this, GCMMessageView.class);
			// Pass data to the new activity
			try{
			startActivity(intent);
			}catch(Exception e){}
			intent.putExtra("message", message);
			try{
			//new AgasthyaDataBase(this).createNotification("message", message);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			// Starts the activity on notification click
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			// Create the notification with a notification builder
			@SuppressWarnings("deprecation")
			Notification notification = new Notification.Builder(this)
					.setSmallIcon(R.drawable.logo)
					.setWhen(System.currentTimeMillis())
					.setContentTitle(getString(R.string.app_name))
					.setContentText(message).setContentIntent(pIntent)
					//.setSound( Uri.parse("android.resource://in.kassapos.gourmeda.gps.map/" + R.raw.pedantic))
					.getNotification();
			// Remove the notification on click
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			manager.notify(R.string.app_name, notification);
			{
				// Wake Android Device when notification received
				PowerManager pm = (PowerManager) context
						.getSystemService(Context.POWER_SERVICE);
				@SuppressWarnings("deprecation")
				final PowerManager.WakeLock mWakelock = pm.newWakeLock(
						PowerManager.FULL_WAKE_LOCK
								| PowerManager.ACQUIRE_CAUSES_WAKEUP, "GCM_PUSH");
				mWakelock.acquire();

				// Timer before putting Android Device to sleep mode.
				Timer timer = new Timer();
				TimerTask task = new TimerTask() {
					public void run() {
						mWakelock.release();
					}
				};
				timer.schedule(task, 5000);
		}
		
		}else if(title.equalsIgnoreCase("acceptedorder")){
			sendMessage(message,"acceptedorders");
		}else if(title.equalsIgnoreCase("cancelledorder")){
			sendMessage(message,"cancelledorder");
		}else if(title.equalsIgnoreCase("clientdriverrequestdriveraccepted")){
			sendMessage(message,"clientdriverrequestdriveraccepted");
		}else if(title.equalsIgnoreCase("clientdriverrequestdriverrejected")){
			sendMessage(message,"clientdriverrequestdriverrejected");
		}else if(title.equalsIgnoreCase("orderupdated")){
			sendMessage(message,"orderupdated");
		}else if(title.equalsIgnoreCase("ordercanceled")){
			sendMessage(message,"ordercanceled");
			if(!WelcomeActivity.getForground()){
				makePopup("ordercanceled");
			}
		}
*/
	}
	private void sendMessage(String message,String title) {
		  Intent intent = new Intent(title);
		  // add data
		  intent.putExtra("message", message);
		  LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	void makeNotificationSound(){
		String url=PreferenceManager.getDefaultSharedPreferences(
				this).getString("notifications_new_message_ringtone", "");
		if(!url.equalsIgnoreCase("")){
			Ringtone ringtone = RingtoneManager.getRingtone(this, Uri.parse(url));
			if(ringtone!=null){  
				ringtone.play();
			}
		}
		boolean vib=PreferenceManager.getDefaultSharedPreferences(
				this).getBoolean("notifications_new_message_vibrate", false);
		if(vib){
			 Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			 // Vibrate for 1000 milliseconds
			 v.vibrate(1000);
		}
	}

	void makePopup(String...args) {

		boolean ispop = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("popup_new_message", true);
		if (ispop) {
			Intent in = new Intent(this, Notifications.class);
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (args.length > 0 && args[0] != null && args[0].equalsIgnoreCase("newdriverrequest")) {
				// in.putExtra("data", "New Driver Request Assigned for you.");
			} else if (args.length > 0 && args[0] != null && args[0].equalsIgnoreCase("ordercanceled")) {
				// in.putExtra("data", getString(R.string.oneordercancelledbyadmin));
			} else {
				// in.putExtra("data", getString(R.string.neworderavailabledoyouwanttoopen));
			}
			startActivity(in);
			Boolean playsong = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("ringtone", true);
			if (playsong) {

				String url = PreferenceManager.getDefaultSharedPreferences(this).getString("ringtone_path", "");
				if (!url.equalsIgnoreCase("")) {
					if (AppConstants.ringtone != null && AppConstants.ringtone.isPlaying()) {
						AppConstants.ringtone.stop();
					}
					playSound(url);
				} else {
					playSound(this);
				}
			} else {
				String url = PreferenceManager.getDefaultSharedPreferences(
						this).getString("popup_new_message_ringtone", "");
				if (!url.equalsIgnoreCase("")) {
					playSound(url);
				}
			}

			boolean vib = PreferenceManager.getDefaultSharedPreferences(
					this).getBoolean("popup_new_message_vibrate", false);
			if (vib) {
				Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
				// Vibrate for 1000 milliseconds
				v.vibrate(1000);
			}
		} else {
			//makeNotificationSound();


		}
	}
	public static void playSound(Context con,Uri uriString) {
		try {
			 //  Uri alert =  RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			  MediaPlayer mMediaPlayer = new MediaPlayer();
			  mMediaPlayer.setDataSource(con, uriString);
			  final AudioManager audioManager = (AudioManager) con.getSystemService(Context.AUDIO_SERVICE);
			 if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
			 mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
			 mMediaPlayer.setLooping(false);
			 mMediaPlayer.prepare();
			 mMediaPlayer.start();
			 if(AppConstants.ringtone!=null&&AppConstants.ringtone.isPlaying()){
				 AppConstants.ringtone.stop();
			 }
			 AppConstants.ringtone=mMediaPlayer;
			}
			} catch(Exception e) {
			}  
	}
	public  void playSound(String uriString) {
		
		playSound(this, Uri.parse(uriString));
	}
	public static void stopMusic(){
		MediaPlayer ring=AppConstants.ringtone;
		if(ring!=null&&ring.isPlaying()){
			ring.stop();
		}
	}
	public static void playSound(Context con) {
		playSound(con,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
	}
	protected Boolean isActivityRunning(Class activityClass)
	{
	        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
	        List<RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

	        for (RunningTaskInfo task : tasks) {
	            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
	                return true;
	        }
	        return false;
	}
	public boolean isRunning(String activity) {
		StringBuilder runningactivities = new StringBuilder();

		ActivityManager activityManager = (ActivityManager)getBaseContext().getSystemService (Context.ACTIVITY_SERVICE); 

		List<RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE); 

		for (int i1 = 0; i1 < services.size(); i1++) { 
		    runningactivities.append(services.get(i1).topActivity.toString());  
		} 

		if(runningactivities.indexOf(activity)!=-1){
		   return true;
		}

        return false;
    }
	@Override
	protected void onError(Context arg0, String errorId) {

		Log.e(TAG, "onError: errorId=" + errorId);
	}

}