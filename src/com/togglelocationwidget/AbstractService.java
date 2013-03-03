package com.togglelocationwidget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.RemoteViews;

public abstract class AbstractService extends Service {
	
	private int getStateLabelId() {
		int labelId = 0;
		if (!Settings.Secure.isLocationProviderEnabled(getContentResolver(),LocationManager.NETWORK_PROVIDER)) {
			labelId = R.string.state_off;
		} else if (!Settings.Secure.isLocationProviderEnabled(getContentResolver(),LocationManager.GPS_PROVIDER)) {
			labelId = R.string.state_network;
		} else {
			labelId = R.string.state_gps_net;
		}
		return labelId;
	}
	
	protected RemoteViews updateRemoteViews(int widgetId) {
		RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget);
		remoteViews.setTextViewText(R.id.widgetTextView, getString(getStateLabelId()));
		return remoteViews;
	}
	
	protected void updateDisplay(int widgetId, RemoteViews remoteViews) {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
		appWidgetManager.updateAppWidget(widgetId, remoteViews);
	}
}
