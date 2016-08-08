package com.mikesu.expandablecalendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.mikesu.expandablecalendar.Config;
import com.mikesu.expandablecalendar.view.page.WeekPageView;
import java.util.Random;
import org.joda.time.DateTime;
import org.joda.time.Weeks;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class WeekViewPagerAdapter extends PagerAdapter {

  private Context context;

  public WeekViewPagerAdapter(Context context) {
    this.context = context;
  }

  @Override
  public int getCount() {
    return Weeks.weeksBetween(Config.START_DATE, Config.END_DATE).getWeeks();
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    WeekPageView weekPageView = new WeekPageView(context);
    container.addView(weekPageView, 0);

    // tmp colors
    weekPageView.setBackgroundColor(Color.rgb(new Random().nextInt(50) + 200, new Random().nextInt(50) + 200, new Random().nextInt(50) + 200));
    // tmp colors
    weekPageView.setup(new DateTime().withDayOfWeek(1).plusWeeks(-Config.weeksBetweenStartAndInit).plusWeeks(position));

    return weekPageView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((WeekPageView) object);
  }
}
