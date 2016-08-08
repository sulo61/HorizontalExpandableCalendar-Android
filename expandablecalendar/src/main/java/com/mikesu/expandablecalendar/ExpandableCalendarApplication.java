package com.mikesu.expandablecalendar;

import android.app.Application;
import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by MikeSu on 08/08/16.
 * www.michalsulek.pl
 */
public class ExpandableCalendarApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    JodaTimeAndroid.init(this);
  }
}
