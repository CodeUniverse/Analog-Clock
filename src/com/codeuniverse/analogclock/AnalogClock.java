/*
 * Copyright (C) 2013 Code Universe. All rights reserved.
 * 
 * http://www.codeuniverse.org
 * 
 * Created by Jaison Brooks 5/12/2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codeuniverse.analogclock;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.RemoteViews;

public class AnalogClock extends AppWidgetProvider {
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action)) {

			//Set our layout file
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.jellybean);

			PackageManager packageManager = context.getPackageManager();
			Intent alarmClockIntent = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER);

			// Here we attempt to launch the alarm clock, depending on the device the package name may be different, so we list all of the following
			String clockImpls[][] = {
			        {"HTC Alarm Clock", "com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl" },
			        {"Standar Alarm Clock", "com.android.deskclock", "com.android.deskclock.AlarmClock"},
			        {"Froyo Nexus Alarm Clock", "com.google.android.deskclock", "com.android.deskclock.DeskClock"},
			        {"Moto Blur Alarm Clock", "com.motorola.blur.alarmclock",  "com.motorola.blur.alarmclock.AlarmClock"},
			        {"Samsung Galaxy Clock", "com.sec.android.app.clockpackage","com.sec.android.app.clockpackage.ClockPackage"},
			        {"Standar Alarm Clock2", "com.google.android.deskclock", "com.android.deskclock.AlarmClock"}
			};

			boolean foundClockImpl = false;

			for(int i=0; i<clockImpls.length; i++) {
			    String vendor = clockImpls[i][0];
			    String packageName = clockImpls[i][1];
			    String className = clockImpls[i][2];
			    try {
			        ComponentName cn = new ComponentName(packageName, className);
			        ActivityInfo aInfo = packageManager.getActivityInfo(cn, PackageManager.GET_META_DATA);
			        alarmClockIntent.setComponent(cn);
			      //  debug("Found " + vendor + " --> " + packageName + "/" + className);
			        foundClockImpl = true;
			    } catch (NameNotFoundException e) {
			      //  debug(vendor + " does not exists");
			    }
			}

			if (foundClockImpl) {
			    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, alarmClockIntent, 0);
			    views.setOnClickPendingIntent(R.id.JellyBean, pendingIntent);
			}

			AppWidgetManager
					.getInstance(context)
					.updateAppWidget(
							intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS),
							views);
		}
	}
}
