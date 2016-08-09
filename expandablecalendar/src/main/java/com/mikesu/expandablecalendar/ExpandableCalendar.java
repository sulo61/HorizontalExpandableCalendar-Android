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
import com.mikesu.expandablecalendar.adapter.MonthPagerAdapter;
import com.mikesu.expandablecalendar.adapter.WeekPagerAdapter;
import com.mikesu.expandablecalendar.common.Config;
import com.mikesu.expandablecalendar.common.Marks;
import com.mikesu.expandablecalendar.common.Utils;
import com.mikesu.expandablecalendar.listener.SmallPageChangeListener;
import java.util.Random;
import org.joda.time.DateTime;

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
  private Button randomMarkButton;

  private ViewPager monthViewPager;
  private MonthPagerAdapter monthPagerAdapter;
  private int monthViewPagerHeight;

  private ViewPager weekViewPager;
  private WeekPagerAdapter weekPagerAdapter;
  private int weekViewPagerHeight;

  public ExpandableCalendar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public ExpandableCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @Override
  protected void onDetachedFromWindow() {
    Marks.clear();
    super.onDetachedFromWindow();
  }

  private void init(AttributeSet attributeSet) {
    bindViews();
    initVariables();
    setValuesFromAttr(attributeSet);
    setupCellWidth();

    Marks.init();
    Marks.markToday();
    Marks.refreshMarkSelected(true);
  }

  private void setCellHeight() {
    Config.cellHeight = monthViewPagerHeight / (Config.MONTH_ROWS + Utils.dayLabelExtraRow());
  }

  private void setupCellWidth() {
    getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        ExpandableCalendar.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        Config.cellWidth = getMeasuredWidth() / Config.COLUMNS;
        setupViews();
      }
    });
  }

  private void setValuesFromAttr(AttributeSet attributeSet) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.ExpandableCalendar);
    if (typedArray != null) {
      setupTopContainerFromAttr(typedArray);
      setupMiddleContainerFromAttr(typedArray);
      setupBottomContainerFromAttr(typedArray);
      typedArray.recycle();
    }

    setHeightToCenterContainer(Utils.isMonthView() ? monthViewPagerHeight : weekViewPagerHeight);
  }


  private void setupBottomContainerFromAttr(TypedArray typedArray) {
    if (typedArray.hasValue(R.styleable.ExpandableCalendar_bottom_container_height)) {
      ((LinearLayout.LayoutParams) bottomContainer.getLayoutParams()).height =
          typedArray.getDimensionPixelSize(R.styleable.ExpandableCalendar_bottom_container_height,
              LinearLayout.LayoutParams.WRAP_CONTENT);
    }
  }

  private void setupMiddleContainerFromAttr(TypedArray typedArray) {
    if (typedArray.hasValue(R.styleable.ExpandableCalendar_center_container_expanded_height)) {
      monthViewPagerHeight = typedArray.getDimensionPixelSize(
          R.styleable.ExpandableCalendar_center_container_expanded_height, LinearLayout.LayoutParams.WRAP_CONTENT);

      setCellHeight();

      weekViewPagerHeight = Config.cellHeight * (Config.USE_DAY_LABELS ? 2 : 1);
    }
  }

  private void setupTopContainerFromAttr(TypedArray typedArray) {
    if (typedArray.hasValue(R.styleable.ExpandableCalendar_top_container_height)) {
      ((LinearLayout.LayoutParams) topContainer.getLayoutParams()).height =
          typedArray.getDimensionPixelSize(R.styleable.ExpandableCalendar_top_container_height,
              LinearLayout.LayoutParams.WRAP_CONTENT);
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
        scrollToDate(Config.INIT_DATE, true, true, true);
      }
    });
    randomMarkButton = (Button) findViewById(R.id.random_selected);
    randomMarkButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        Marks.refreshMarkSelected(false);
        Config.selectionDate = new DateTime().withDayOfMonth(new Random().nextInt(30) + 1);
        Marks.refreshMarkSelected(true);
        updateMarks();
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
        switch (Config.currentVisibleViewPager) {
          case MONTH:
            scrollToDate(Config.scrollDate, false, true, false);
            monthViewPager.setVisibility(View.GONE);
            weekViewPager.setVisibility(View.VISIBLE);
            Config.currentVisibleViewPager = Config.CurrentVisibleViewPager.WEEK;
            setHeightToCenterContainer(weekViewPagerHeight);
            weekPagerAdapter.verifyMarks();
            break;
          case WEEK:
            scrollToDate(Config.scrollDate, true, false, false);
            monthViewPager.setVisibility(View.VISIBLE);
            weekViewPager.setVisibility(View.GONE);
            Config.currentVisibleViewPager = Config.CurrentVisibleViewPager.MONTH;
            setHeightToCenterContainer(monthViewPagerHeight);
            monthPagerAdapter.updateMarks();
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
    monthPagerAdapter = new MonthPagerAdapter(getContext());
    monthViewPager.setAdapter(monthPagerAdapter);
    monthViewPager.setCurrentItem(Config.monthsBetweenStartAndInit);
    monthViewPager.addOnPageChangeListener(new SmallPageChangeListener() {
      @Override
      public void scrollStateChanged(int state) {
        if (Utils.isMonthView()) {
          if (state == ViewPager.SCROLL_STATE_IDLE) {
            Config.scrollDate = Config.INIT_DATE
                .plusMonths(-Config.monthsBetweenStartAndInit)
                .plusMonths(monthViewPager.getCurrentItem());
            refreshTitleTextView();
          }
        }
      }
    });
    monthViewPager.setVisibility(Utils.isMonthView() ? VISIBLE : GONE);
  }

  private void initWeekViewPager() {
    weekViewPager = (ViewPager) findViewById(R.id.week_view_pager);
    weekPagerAdapter = new WeekPagerAdapter(getContext());
    weekViewPager.setAdapter(weekPagerAdapter);
    setWeekViewPagerPosition(Config.weeksBetweenStartAndInit, false);
    weekViewPager.addOnPageChangeListener(new SmallPageChangeListener() {
      @Override
      public void scrollStateChanged(int state) {
        if (!Utils.isMonthView()) {
          if (state == ViewPager.SCROLL_STATE_IDLE) {
            Config.scrollDate = Config.INIT_DATE
                .plusWeeks(-Config.weeksBetweenStartAndInit)
                .plusWeeks(weekViewPager.getCurrentItem());
            refreshTitleTextView();
          }
        }
      }
    });
    weekViewPager.setVisibility(!Utils.isMonthView() ? VISIBLE : GONE);
  }

  private void initVariables() {
    Config.monthsBetweenStartAndInit = Utils.monthsBetween(Config.START_DATE, Config.INIT_DATE);
    Config.weeksBetweenStartAndInit = Utils.weeksBetween(Config.START_DATE, Config.INIT_DATE);
  }

  private void setHeightToCenterContainer(int height) {
    ((LinearLayout.LayoutParams) centerContainer.getLayoutParams()).height = height;
  }

  private void scrollToDate(DateTime dateTime, boolean scrollMonthPager, boolean scrollWeekPager, boolean animate) {
    if (scrollMonthPager) {
      setMonthViewPagerPosition(Utils.monthsBetween(Config.START_DATE, dateTime), animate);
    }
    if (scrollWeekPager) {
      setWeekViewPagerPosition(Utils.weeksBetween(Config.START_DATE, dateTime.plusDays(-1)), animate);
    }
  }

  private void setWeekViewPagerPosition(int position, boolean animate) {
    weekViewPager.setCurrentItem(position, animate);
  }

  private void setMonthViewPagerPosition(int position, boolean animate) {
    monthViewPager.setCurrentItem(position, animate);
  }

  private void refreshTitleTextView() {
    titleTextView.setText(
        String.format("%s - %s", Config.scrollDate.getYear(), Config.scrollDate.getMonthOfYear()));
  }

  private void updateMarks() {
    if (Config.currentVisibleViewPager == Config.CurrentVisibleViewPager.MONTH) {
      monthPagerAdapter.updateMarks();
    } else {
      weekPagerAdapter.verifyMarks();
    }
  }

}