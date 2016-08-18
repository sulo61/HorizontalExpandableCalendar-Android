package com.mikesu.horizontalexpcalendar.animator;

import android.animation.ValueAnimator;
import com.mikesu.horizontalexpcalendar.listener.SmallAnimationListener;

/**
 * Created by miquido on 18/08/16.
 */
public class CalendarAnimation extends ValueAnimator {

  public void setListener(SmallAnimationListener smallAnimationListener) {
    addUpdateListener(smallAnimationListener);
    addListener(smallAnimationListener);
    start();
  }
}
