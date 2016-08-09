package com.mikesu.expandablecalendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.mikesu.expandablecalendar.common.Config;
import com.mikesu.expandablecalendar.common.Utils;
import com.mikesu.expandablecalendar.view.page.MonthPageView;
import com.mikesu.expandablecalendar.view.page.WeekPageView;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class WeekPagerAdapter extends PagerAdapter {

  private List<WeekPageView> visiblePages;
  private Context context;

  public WeekPagerAdapter(Context context) {
    this.visiblePages = new ArrayList<>();
    this.context = context;
  }

  @Override
  public int getCount() {
    return Utils.weeksBetween(Config.START_DATE, Config.END_DATE);
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    WeekPageView weekPageView = new WeekPageView(context);
    visiblePages.add(weekPageView);

    container.addView(weekPageView, 0);

    weekPageView.setBackgroundColor(Color.rgb(
        Utils.getRandomColor(),
        Utils.getRandomColor(),
        Utils.getRandomColor()));

    weekPageView.setup(new DateTime().withDayOfWeek(1).plusWeeks(-Config.weeksBetweenStartAndInit).plusWeeks(position - 1));

    return weekPageView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    visiblePages.remove(object);
    container.removeView((WeekPageView) object);
  }

  public void verifyMarks() {
    for (WeekPageView weekPageView : visiblePages) {
      weekPageView.verifyMarks();
    }
  }
}
