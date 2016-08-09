package com.mikesu.expandablecalendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.mikesu.expandablecalendar.common.Config;
import com.mikesu.expandablecalendar.common.Utils;
import com.mikesu.expandablecalendar.view.page.MonthPageView;
import org.joda.time.DateTime;
import org.joda.time.Months;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class MonthPagerAdapter extends PagerAdapter {

  private Context context;

  public MonthPagerAdapter(Context context) {
    this.context = context;
  }

  @Override
  public int getCount() {
    return Months.monthsBetween(Config.START_DATE, Config.END_DATE).getMonths();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    MonthPageView monthPageView = new MonthPageView(context);
    container.addView(monthPageView, 0);

    monthPageView.setBackgroundColor(Color.rgb(
        Utils.getRandomColor(),
        Utils.getRandomColor(),
        Utils.getRandomColor()));

    monthPageView.setup(new DateTime().withDayOfMonth(1).plusMonths(-Config.monthsBetweenStartAndInit).plusMonths(position));

    return monthPageView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((MonthPageView) object);
  }
}
