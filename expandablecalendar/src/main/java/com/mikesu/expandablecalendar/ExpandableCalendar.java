package com.mikesu.expandablecalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mikesu.expandablecalendar.adapter.MonthViewPagerAdapter;
import com.mikesu.expandablecalendar.listener.SmallOnPageChangeListener;
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

  private RelativeLayout topContainer;
  private RelativeLayout centerContainer;
  private RelativeLayout bottomContainer;
  private TextView title;

  private ViewPager monthViewPager;
  private MonthViewPagerAdapter monthViewPagerAdapter;
  private int monthViewPagerHeight;

  private ViewPager weekViewPager;
  private CurrentVisibleViewPager currentVisibleViewPager;
  private int weekViewPagerHeight;

  public ExpandableCalendar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public ExpandableCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  private void init(AttributeSet attributeSet) {
    initVariables();
    initViews();
    setValuesFromAttr(attributeSet);
  }

  private void setValuesFromAttr(AttributeSet attributeSet) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.ExpandableCalendar);
    if (typedArray != null) {
      if (typedArray.hasValue(R.styleable.ExpandableCalendar_top_container_height)) {
        ((LinearLayout.LayoutParams) topContainer.getLayoutParams()).height =
            typedArray.getDimensionPixelSize(R.styleable.ExpandableCalendar_top_container_height,
                LinearLayout.LayoutParams.WRAP_CONTENT);
      }
      if (typedArray.hasValue(R.styleable.ExpandableCalendar_center_container_height)) {
        ((LinearLayout.LayoutParams) centerContainer.getLayoutParams()).height =
            typedArray.getDimensionPixelSize(R.styleable.ExpandableCalendar_center_container_height,
                LinearLayout.LayoutParams.WRAP_CONTENT);
      }
      if (typedArray.hasValue(R.styleable.ExpandableCalendar_bottom_container_height)) {
        ((LinearLayout.LayoutParams) bottomContainer.getLayoutParams()).height =
            typedArray.getDimensionPixelSize(R.styleable.ExpandableCalendar_bottom_container_height,
                LinearLayout.LayoutParams.WRAP_CONTENT);
      }
      typedArray.recycle();
    }
  }

  private void initViews() {
    inflate(getContext(), R.layout.expandable_calendar, this);
    initContainers();
    initTitle();
    initMonthViewPager();
    initWeekViewPager();
    setTitle(INIT_DAY);
    initSizes();
  }

  private void initTitle() {
    title = (TextView) findViewById(R.id.title);
  }

  private void initSizes() {
    getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        ExpandableCalendar.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        monthViewPagerHeight = monthViewPager.getMeasuredHeight();
        weekViewPagerHeight = weekViewPager.getMeasuredHeight();
      }
    });
  }

  private void initContainers() {
    topContainer = (RelativeLayout) findViewById(R.id.top_container);
    centerContainer = (RelativeLayout) findViewById(R.id.center_container);
    bottomContainer = (RelativeLayout) findViewById(R.id.bottom_container);
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
    title.setText(String.format("%s - %s", titleDateTime.getYear(), titleDateTime.getMonthOfYear()));
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
