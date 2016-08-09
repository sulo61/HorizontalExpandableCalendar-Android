package com.mikesu.expandablecalendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.mikesu.expandablecalendar.common.Config;
import com.mikesu.expandablecalendar.common.Utils;
import com.mikesu.expandablecalendar.view.page.PageView;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Created by MikeSu on 09.08.2016.
 * www.michalsulek.pl
 */
public class CalendarAdapter extends PagerAdapter {

  private Config.ViewPagerType viewPagerType;
  private List<PageView> visiblePages;
  private Context context;

  public CalendarAdapter(Context context, Config.ViewPagerType viewPagerType) {
    this.viewPagerType = viewPagerType;
    this.visiblePages = new ArrayList<>();
    this.context = context;
  }

  @Override
  public int getCount() {
    switch (viewPagerType) {
      case MONTH:
        return Utils.monthsBetween(Config.START_DATE, Config.END_DATE);
      case WEEK:
        return Utils.weeksBetween(Config.START_DATE, Config.END_DATE);
      default:
        return 0;
    }
  }

  @Override
  public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    PageView pageView = new PageView(context, viewPagerType);
    visiblePages.add(pageView);

    container.addView(pageView, 0);

    pageView.setBackgroundColor(Color.rgb(
        Utils.getRandomColor(),
        Utils.getRandomColor(),
        Utils.getRandomColor()));

    switch (viewPagerType) {
      case MONTH:
        pageView.setup(new DateTime().withDayOfMonth(1).plusMonths(-Config.monthsBetweenStartAndInit).plusMonths(position));
        break;
      case WEEK:
        pageView.setup(new DateTime().withDayOfWeek(1).plusWeeks(-Config.weeksBetweenStartAndInit).plusWeeks(position - 1));
        break;
      default:
        Log.e(CalendarAdapter.class.getName(), "instantiateItem, unknown view pager type");
    }

    return pageView;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    visiblePages.remove(object);
    container.removeView((PageView) object);
  }

  public void updateMarks() {
    for (PageView pageView : visiblePages) {
      pageView.updateMarks();
    }
  }


}
