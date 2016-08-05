package com.mikesu.expandablecalendar;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mikesu.expandablecalendar.adapter.MonthViewPagerAdapter;
import com.mikesu.expandablecalendar.listeners.SmallOnPageChangeListener;
import org.joda.time.DateTime;
import org.joda.time.Months;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class ExpandableCalendar extends RelativeLayout {

  public static final DateTime START_DATE = new DateTime().plusYears(-1);
  public static final DateTime END_DATE = new DateTime().plusYears(1).plusMonths(1);
  public static final DateTime INIT_DAY = new DateTime();

  public static int monthsBetweenStartAndInit;

  private TextView title;

  private ViewPager monthViewPager;
  private MonthViewPagerAdapter monthViewPagerAdapter;
  private ViewPager weekViewPager;
  private CurrentVisibleViewPager currentVisibleViewPager;

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
    initVariables();
    initViews();
  }

  private void initViews() {
    inflate(getContext(), R.layout.expandable_calendar, this);
    initTitle();
    initMonthViewPager();
    initWeekViewPager();
    setTitle(INIT_DAY);
  }

  private void initTitle() {
    title = (TextView) findViewById(R.id.title);
  }

  private void initWeekViewPager() {
    weekViewPager = (ViewPager) findViewById(R.id.week_view_pager);
  }

  private void initMonthViewPager() {
    monthViewPager = (ViewPager) findViewById(R.id.month_view_pager);
    monthViewPagerAdapter = new MonthViewPagerAdapter(getContext());
    monthViewPager.setAdapter(monthViewPagerAdapter);
    monthViewPager.setCurrentItem(monthsBetweenStartAndInit);
    monthViewPager.addOnPageChangeListener(new SmallOnPageChangeListener() {
      @Override
      public void scrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
          setTitle(new DateTime().plusMonths(-monthsBetweenStartAndInit).plusMonths(monthViewPager.getCurrentItem()));
        }
      }
    });
  }

  private void setTitle(DateTime titleDateTime) {
    title.setText(titleDateTime.getYear() + " - " + titleDateTime.getMonthOfYear());
  }

  private void initVariables() {
    currentVisibleViewPager = CurrentVisibleViewPager.MONTH;
    monthsBetweenStartAndInit = Months.monthsBetween(START_DATE, INIT_DAY).getMonths();
  }

  private enum CurrentVisibleViewPager {
    MONTH,
    WEEK
  }
}
