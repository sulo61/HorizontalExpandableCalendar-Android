package com.mikesu.horizontalexpcalendar.listener;

import android.support.v4.view.ViewPager;

/**
 * Created by MikeSu on 05/08/16.
 * www.michalsulek.pl
 */

public abstract class SmallPageChangeListener implements ViewPager.OnPageChangeListener {

  @Override
  public void onPageScrollStateChanged(int state) {
    if (state == ViewPager.SCROLL_STATE_IDLE) {
      scrollEnd();
    } else if (state == ViewPager.SCROLL_STATE_DRAGGING) {
      scrollStart();
    }
  }

  @Override
  public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    // IGNORE
  }

  @Override
  public void onPageSelected(int position) {
    // IGNORE
  }

  public abstract void scrollStart();

  public abstract void scrollEnd();
}
