package com.mikesu.expandablecalendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.mikesu.expandablecalendar.adapter.MonthViewPagerAdapter;
import org.joda.time.DateTime;
import org.joda.time.Months;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class ExpandableCalendar extends RelativeLayout {

  public static final DateTime START_DATE = new DateTime().plusYears(-1);
  public static final DateTime END_DATE = new DateTime().plusYears(1);
  public static final DateTime TODAY_DATE = new DateTime();

  private ViewPager monthViewPager;
  private MonthViewPagerAdapter monthViewPagerAdapter;
  private ViewPager weekViewPager;
  private CurrentVisibleViewPager currentVisibleViewPager;

  private int width;
  private int height;

  public ExpandableCalendar(Context context) {
    super(context);
    init();
  }

  public ExpandableCalendar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public ExpandableCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    initViews();
    initVariables();
  }

  private void initViews() {
    inflate(getContext(), R.layout.expandable_calendar, this);
    initMonthViewPager();
    initWeekViewPager();
  }

  private void initWeekViewPager() {
    weekViewPager = (ViewPager) findViewById(R.id.week_view_pager);
  }

  private void initMonthViewPager() {
    monthViewPager = (ViewPager) findViewById(R.id.month_view_pager);
    monthViewPagerAdapter = new MonthViewPagerAdapter(getContext());
    monthViewPager.setAdapter(monthViewPagerAdapter);
    monthViewPager.setCurrentItem(Months.monthsBetween(START_DATE, TODAY_DATE).getMonths());
  }

  private void initVariables() {
    height = 0;
    width = 0;
    currentVisibleViewPager = CurrentVisibleViewPager.MONTH;
  }


  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    width = MeasureSpec.getSize(widthMeasureSpec);
    height = MeasureSpec.getSize(heightMeasureSpec);
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  private enum CurrentVisibleViewPager {
    MONTH,
    WEEK
  }
}
