package com.eowise.recyclerview.stickyheaders.samples.GCM;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.eowise.recyclerview.stickyheaders.samples.Main_TabHost;
import com.eowise.recyclerview.stickyheaders.samples.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMNotificationIntentService extends IntentService {
	// Sets an ID for the notification, so it can be updated
	public static final int notifyID = 9001;
	NotificationCompat.Builder builder;

	public GCMNotificationIntentService() {
		super("GcmIntentService");
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				sendNotification("Deleted messages on server: "
						+ extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				sendNotification("Message Received from Google GCM Server:\n\n"
						+ extras.get(ApplicationConstants.MSG_KEY));
			}
		}
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(String msg) {
	        Intent resultIntent = new Intent(this,Main_TabHost.class);
	        resultIntent.putExtra("msg", msg);
	        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
	                resultIntent, PendingIntent.FLAG_ONE_SHOT);
	        
	        NotificationCompat.Builder mNotifyBuilder;
	        NotificationManager mNotificationManager;
	        
	        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

		mNotifyBuilder = new NotificationCompat.Builder(this)
	                .setContentTitle("Last Night Dress")
	                .setContentText("You've received new message.")
	                .setLargeIcon(largeIcon)
			     	.setPriority(Notification.PRIORITY_MAX)
				  	.setSmallIcon(R.drawable.logo);

	        // Set pending intent
	        mNotifyBuilder.setContentIntent(resultPendingIntent);
	        
	        // Set Vibrate, Sound and Light	        
	        int defaults = 0;
	        defaults = defaults | Notification.DEFAULT_LIGHTS;
	        defaults = defaults | Notification.DEFAULT_VIBRATE;
	        defaults = defaults | Notification.DEFAULT_SOUND;
	        
	        mNotifyBuilder.setDefaults(defaults);
	        // Set the content for Notification 
	        mNotifyBuilder.setContentText("New message from Server");
	        // Set autocancel
	        mNotifyBuilder.setAutoCancel(true);
	        // Post a notification
	        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
	}
}
