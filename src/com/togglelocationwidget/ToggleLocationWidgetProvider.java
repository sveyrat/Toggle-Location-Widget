package com.togglelocationwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class ToggleLocationWidgetProvider extends AppWidgetProvider {
	
	private static boolean isFirstOnUpdateCall = true;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		ComponentName thisWidget = new ComponentName(context, ToggleLocationWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		
		Intent changeLocationProvidersServiceIntent = new Intent(context.getApplicationContext(), ChangeLocationProvidersStateService.class);
		changeLocationProvidersServiceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
		changeLocationProvidersServiceIntent.putExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, isFirstOnUpdateCall);

		context.startService(changeLocationProvidersServiceIntent);
		
		isFirstOnUpdateCall = false;
	}
}
