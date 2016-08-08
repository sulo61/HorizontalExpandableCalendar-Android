package com.mikesu.expandablecalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mikesu.expandablecalendar.adapter.MonthViewPagerAdapter;
import com.mikesu.expandablecalendar.adapter.WeekViewPagerAdapter;
import com.mikesu.expandablecalendar.listener.SmallOnPageChangeListener;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.joda.time.Weeks;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class ExpandableCalendar extends RelativeLayout {

  private static final String TAG = ExpandableCalendar.class.getName();

  private RelativeLayout topContainer;
  private RelativeLayout centerContainer;
  private RelativeLayout bottomContainer;
  private TextView titleTextView;
  private Button switchViewButton;
  private Button scrollToInitButton;

  private CurrentVisibleViewPager currentVisibleViewPager;

  private ViewPager monthViewPager;
  private MonthViewPagerAdapter monthViewPagerAdapter;
  private int monthViewPagerHeight;

  private ViewPager weekViewPager;
  private WeekViewPagerAdapter weekViewPagerAdapter;
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
    bindViews();
    initVariables();
    setValuesFromAttr(attributeSet);
    setupCellSize();
  }

  private void setupCellSize() {
    getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        ExpandableCalendar.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        ExpandableConfig.cellHeight = weekViewPagerHeight;
        ExpandableConfig.cellWidth = getMeasuredWidth() / ExpandableConfig.COLUMNS;
        setupViews();
      }
    });
  }

  private void setValuesFromAttr(AttributeSet attributeSet) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.ExpandableCalendar);
    if (typedArray != null) {
      if (typedArray.hasValue(R.styleable.ExpandableCalendar_top_container_height)) {
        ((LinearLayout.LayoutParams) topContainer.getLayoutParams()).height =
            typedArray.getDimensionPixelSize(R.styleable.ExpandableCalendar_top_container_height,
                LinearLayout.LayoutParams.WRAP_CONTENT);
      }
      if (typedArray.hasValue(R.styleable.ExpandableCalendar_center_container_expanded_height)) {
        monthViewPagerHeight = typedArray.getDimensionPixelSize(R.styleable.ExpandableCalendar_center_container_expanded_height,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        weekViewPagerHeight = monthViewPagerHeight / ExpandableConfig.MONTH_ROWS;

        setHeightToCenterContainer(
            currentVisibleViewPager==CurrentVisibleViewPager.MONTH ? monthViewPagerHeight : weekViewPagerHeight);
      }
      if (typedArray.hasValue(R.styleable.ExpandableCalendar_bottom_container_height)) {
        ((LinearLayout.LayoutParams) bottomContainer.getLayoutParams()).height =
            typedArray.getDimensionPixelSize(R.styleable.ExpandableCalendar_bottom_container_height,
                LinearLayout.LayoutParams.WRAP_CONTENT);
      }
      typedArray.recycle();
    }
  }

  private void bindViews() {
    inflate(getContext(), R.layout.expandable_calendar, this);
    bindContainers();
  }

  private void setupViews() {
    initTopContainer();
    initCenterContainer();
    initBottomContainer();
    refreshTitleTextView();
  }

  private void initTopContainer() {
    titleTextView = (TextView) findViewById(R.id.title);
    scrollToInitButton = (Button) findViewById(R.id.scroll_to_init_button);
    scrollToInitButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        scrollToDate(ExpandableConfig.INIT_DATE, true, true);
      }
    });
  }

  private void initCenterContainer() {
    initMonthViewPager();
    initWeekViewPager();
  }

  private void initBottomContainer() {
    switchViewButton = (Button) findViewById(R.id.change_calendar_view_button);
    switchViewButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        switch (currentVisibleViewPager) {
          case MONTH:
            monthViewPager.setVisibility(View.GONE);
            weekViewPager.setVisibility(View.VISIBLE);
            currentVisibleViewPager = CurrentVisibleViewPager.WEEK;
            setHeightToCenterContainer(weekViewPagerHeight);
            break;
          case WEEK:
            monthViewPager.setVisibility(View.VISIBLE);
            weekViewPager.setVisibility(View.GONE);
            currentVisibleViewPager = CurrentVisibleViewPager.MONTH;
            setHeightToCenterContainer(monthViewPagerHeight);
            break;
          default:
            Log.e(TAG, "switchViewButton click, unknown type of currentVisibleViewPager");
        }
      }
    });
  }

  private void bindContainers() {
    topContainer = (RelativeLayout) findViewById(R.id.top_container);
    centerContainer = (RelativeLayout) findViewById(R.id.center_container);
    bottomContainer = (RelativeLayout) findViewById(R.id.bottom_container);
  }

  private void initMonthViewPager() {
    monthViewPager = (ViewPager) findViewById(R.id.month_view_pager);
    monthViewPagerAdapter = new MonthViewPagerAdapter(getContext());
    monthViewPager.setAdapter(monthViewPagerAdapter);
    monthViewPager.setCurrentItem(ExpandableConfig.monthsBetweenStartAndInit);
    monthViewPager.addOnPageChangeListener(new SmallOnPageChangeListener() {
      @Override
      public void scrollStateChanged(int state) {
        if (currentVisibleViewPager == CurrentVisibleViewPager.MONTH) {
          if (state == ViewPager.SCROLL_STATE_IDLE) {
            ExpandableConfig.currentDate = new DateTime()
                .plusMonths(-ExpandableConfig.monthsBetweenStartAndInit)
                .plusMonths(monthViewPager.getCurrentItem());
            refreshTitleTextView();
            scrollToCurrentDate(false, true);
          }
        }
      }
    });
    monthViewPager.setVisibility(currentVisibleViewPager==CurrentVisibleViewPager.MONTH ? VISIBLE : GONE);
  }

  private void initWeekViewPager() {
    weekViewPager = (ViewPager) findViewById(R.id.week_view_pager);
    weekViewPagerAdapter = new WeekViewPagerAdapter(getContext());
    weekViewPager.setAdapter(weekViewPagerAdapter);
    weekViewPager.setCurrentItem(ExpandableConfig.weeksBetweenStartAndInit);
    weekViewPager.addOnPageChangeListener(new SmallOnPageChangeListener() {
      @Override
      public void scrollStateChanged(int state) {
        if (currentVisibleViewPager == CurrentVisibleViewPager.WEEK) {
          if (state == ViewPager.SCROLL_STATE_IDLE) {
            ExpandableConfig.currentDate = new DateTime()
                .plusWeeks(-ExpandableConfig.weeksBetweenStartAndInit)
                .plusWeeks(weekViewPager.getCurrentItem());
            refreshTitleTextView();
            scrollToCurrentDate(true, false);
          }
        }
      }
    });
    weekViewPager.setVisibility(currentVisibleViewPager==CurrentVisibleViewPager.WEEK ? VISIBLE : GONE);
  }

  private void initVariables() {
    currentVisibleViewPager = CurrentVisibleViewPager.WEEK;
    ExpandableConfig.monthsBetweenStartAndInit = Months.monthsBetween(ExpandableConfig.START_DATE, ExpandableConfig.INIT_DATE).getMonths();
    ExpandableConfig.weeksBetweenStartAndInit = Weeks.weeksBetween(ExpandableConfig.START_DATE, ExpandableConfig.INIT_DATE).getWeeks();
  }

  private void setHeightToCenterContainer(int height) {
    ((LinearLayout.LayoutParams) centerContainer.getLayoutParams()).height = height;
  }

  private void scrollToCurrentDate(boolean scrollMonthPager, boolean scrollWeekPager) {
    scrollToDate(ExpandableConfig.currentDate, scrollMonthPager, scrollWeekPager);
  }

  private void scrollToDate(DateTime dateTime, boolean scrollMonthPager, boolean scrollWeekPager) {
    if (scrollMonthPager) {
      monthViewPager.setCurrentItem(Months.monthsBetween(ExpandableConfig.START_DATE, dateTime).getMonths());
    }
    if (scrollWeekPager) {
      weekViewPager.setCurrentItem(Weeks.weeksBetween(ExpandableConfig.START_DATE, dateTime).getWeeks());
    }
  }

  private void refreshTitleTextView() {
    titleTextView.setText(String.format("%s - %s", ExpandableConfig.currentDate.getYear(), ExpandableConfig.currentDate.getMonthOfYear()));
  }

  private enum CurrentVisibleViewPager {
    MONTH,
    WEEK
  }
}
