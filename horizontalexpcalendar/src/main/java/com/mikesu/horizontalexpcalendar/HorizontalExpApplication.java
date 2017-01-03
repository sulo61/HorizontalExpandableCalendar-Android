package com.mikesu.horizontalexpcalendar;

import android.app.Application;
import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by MikeSu on 04.01.2017.
 * www.michalsulek.pl
 */

public class HorizontalExpApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    JodaTimeAndroid.init(this);
  }
}
