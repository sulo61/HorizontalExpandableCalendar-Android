package com.mikesu.expandablecalendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.mikesu.expandablecalendar.ExpandableCalendar;
import com.mikesu.expandablecalendar.view.pages.MonthPageView;
import java.util.Random;
import org.joda.time.Months;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */
public class MonthViewPagerAdapter extends PagerAdapter {

  private Context context;

  public MonthViewPagerAdapter(Context context) {
    this.context = context;
  }

  @Override
  public int getCount() {
    return Months.monthsBetween(ExpandableCalendar.START_DATE, ExpandableCalendar.END_DATE).getMonths();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    MonthPageView monthPageView = new MonthPageView(context);
    monthPageView.setBackgroundColor(Color.rgb(new Random().nextInt(200), new Random().nextInt(200), new Random().nextInt(200)));
    container.addView(monthPageView, 0);
    return monthPageView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((MonthPageView) object);
  }
}
