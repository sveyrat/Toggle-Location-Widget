package com.togglelocationwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.RemoteViews;

public class ChangeLocationProvidersStateService extends AbstractService {
	
	@Override
	public void onStart(final Intent intent, int startId) {
		// Change the location providers state
		boolean isFirstOnUpdateCall = intent.getBooleanExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, false);
		
		if (!isFirstOnUpdateCall) {
			switchState();
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
						for (int widgetId : allWidgetIds) {
							RemoteViews remoteViews = updateRemoteViews(widgetId);
							updateDisplay(widgetId, remoteViews);
						}
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							stopSelf();
						}
					}
				}
			}).start();
		}

		int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		for (int widgetId : allWidgetIds) {
			RemoteViews remoteViews = updateRemoteViews(widgetId);

			Intent clickIntent = new Intent(this.getApplicationContext(), ToggleLocationWidgetProvider.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.widgetTextView, pendingIntent);
			
			updateDisplay(widgetId, remoteViews);
		}
		stopSelf();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private void switchState() {
		if (!Settings.Secure.isLocationProviderEnabled(getContentResolver(),LocationManager.NETWORK_PROVIDER)) {
			Settings.Secure.setLocationProviderEnabled(getContentResolver(), LocationManager.NETWORK_PROVIDER, true);
		} else if (!Settings.Secure.isLocationProviderEnabled(getContentResolver(),LocationManager.GPS_PROVIDER)) {
			Settings.Secure.setLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER, true);
		} else {
			Settings.Secure.setLocationProviderEnabled(getContentResolver(), LocationManager.NETWORK_PROVIDER, false);
			Settings.Secure.setLocationProviderEnabled(getContentResolver(), LocationManager.GPS_PROVIDER, false);
		}
	}
}
