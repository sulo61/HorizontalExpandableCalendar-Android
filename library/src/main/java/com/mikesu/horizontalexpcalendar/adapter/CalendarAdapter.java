package com.mikesu.horizontalexpcalendar.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;

import com.mikesu.horizontalexpcalendar.common.Config;
import com.mikesu.horizontalexpcalendar.common.Utils;
import com.mikesu.horizontalexpcalendar.view.page.PageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MikeSu on 09.08.2016.
 * www.michalsulek.pl
 */
public class CalendarAdapter extends PagerAdapter {

  private PageView.PageViewListener pageViewListener;
  private Config.ViewPagerType viewPagerType;
  private List<PageView> visiblePages;
  private Context context;

  public CalendarAdapter(Context context, Config.ViewPagerType viewPagerType, PageView.PageViewListener pageViewListener) {
    this.pageViewListener = pageViewListener;
    this.viewPagerType = viewPagerType;
    this.visiblePages = new ArrayList<>();
    this.context = context;
  }

  @Override
  public int getCount() {
    switch (viewPagerType) {
      case MONTH:
        return Utils.monthPositionFromDate(Config.END_DATE);
      case WEEK:
        return Utils.weekPositionFromDate(Config.END_DATE);
      default:
        return 0;
    }
  }

  @Override
  public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
    return view == object;
  }

  @NotNull
  @Override
  public Object instantiateItem(@NotNull ViewGroup container, int position) {
    PageView pageView = new PageView(context, viewPagerType, pageViewListener);
    visiblePages.add(pageView);

    container.addView(pageView, 0);

    switch (viewPagerType) {
      case MONTH:
        pageView.setup(Utils.getDateByMonthPosition(position));
        break;
      case WEEK:
        pageView.setup(Utils.getDateByWeekPosition(position));
        break;
      default:
        Log.e(CalendarAdapter.class.getName(), "instantiateItem, unknown view pager type");
    }

    return pageView;
  }

  @Override
  public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
    visiblePages.remove(object);
    container.removeView((PageView) object);
  }

  public void updateMarks() {
    for (PageView pageView : visiblePages) {
      pageView.updateMarks();
    }
  }


}
