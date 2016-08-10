package com.mikesu.horizontalexpcalendar;

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
import com.mikesu.horizontalexpcalendar.adapter.CalendarAdapter;
import com.mikesu.horizontalexpcalendar.common.Config;
import com.mikesu.horizontalexpcalendar.common.Marks;
import com.mikesu.horizontalexpcalendar.common.Utils;
import com.mikesu.horizontalexpcalendar.listener.SmallPageChangeListener;
import com.mikesu.horizontalexpcalendar.view.page.PageView;
import java.util.Random;
import org.joda.time.DateTime;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class HorizontalExpCalendar extends RelativeLayout implements PageView.PageViewListener {

  private static final String TAG = HorizontalExpCalendar.class.getName();

  private TextView titleTextView;

  // tmp
  private Button prevDays;
  private Button randDays;
  private Button nextDays;
  private int randNumber = 1;
  private DateTime randDate = new DateTime();

  private ViewPager monthViewPager;
  private CalendarAdapter monthPagerAdapter;
  private int monthViewPagerHeight;

  private ViewPager weekViewPager;
  private CalendarAdapter weekPagerAdapter;
  private int weekViewPagerHeight;

  public HorizontalExpCalendar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public HorizontalExpCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @Override
  protected void onDetachedFromWindow() {
    Marks.clear();
    super.onDetachedFromWindow();
  }

  private void init(AttributeSet attributeSet) {
    inflate(getContext(), R.layout.horizontal_exp_calendar, this);

    //tmp
    prevDays = (Button) findViewById(R.id.prev_x_days);
    randDays = (Button) findViewById(R.id.rand_days);
    nextDays = (Button) findViewById(R.id.next_x_days);

    final Random random = new Random();

    prevDays.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        randDate = randDate.plusDays(-randNumber);
        scrollToDate(randDate, true);
      }
    });
    randDays.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        randNumber = random.nextInt(50);
        prevDays.setText("prev " + randNumber);
        nextDays.setText("next " + randNumber);
      }
    });
    nextDays.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        randDate = randDate.plusDays(randNumber);
        scrollToDate(randDate, true);
      }
    });



    initVariables();
    setValuesFromAttr(attributeSet);
    setupCellWidth();

    Marks.init();
    Marks.markToday();
    Marks.refreshMarkSelected(Config.selectionDate);
  }

  private void setCellHeight() {
    Config.cellHeight = monthViewPagerHeight / (Config.MONTH_ROWS + Utils.dayLabelExtraRow());
  }

  private void setupCellWidth() {
    getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        HorizontalExpCalendar.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        Config.cellWidth = getMeasuredWidth() / Config.COLUMNS;
        setupViews();
      }
    });
  }

  private void setValuesFromAttr(AttributeSet attributeSet) {
    TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.HorizontalExpCalendar);
    if (typedArray != null) {
      setupTopContainerFromAttr(typedArray);
      setupMiddleContainerFromAttr(typedArray);
      setupBottomContainerFromAttr(typedArray);
      typedArray.recycle();
    }

    setHeightToCenterContainer(Utils.isMonthView() ? monthViewPagerHeight : weekViewPagerHeight);
  }


  private void setupBottomContainerFromAttr(TypedArray typedArray) {
    if (typedArray.hasValue(R.styleable.HorizontalExpCalendar_bottom_container_height)) {
      ((LinearLayout.LayoutParams) findViewById(R.id.bottom_container).getLayoutParams()).height =
          typedArray.getDimensionPixelSize(R.styleable.HorizontalExpCalendar_bottom_container_height,
              LinearLayout.LayoutParams.WRAP_CONTENT);
    }
  }

  private void setupMiddleContainerFromAttr(TypedArray typedArray) {
    if (typedArray.hasValue(R.styleable.HorizontalExpCalendar_center_container_expanded_height)) {
      monthViewPagerHeight = typedArray.getDimensionPixelSize(
          R.styleable.HorizontalExpCalendar_center_container_expanded_height, LinearLayout.LayoutParams.WRAP_CONTENT);

      setCellHeight();

      weekViewPagerHeight = Config.cellHeight * (Config.USE_DAY_LABELS ? 2 : 1);
    }
  }

  private void setupTopContainerFromAttr(TypedArray typedArray) {
    if (typedArray.hasValue(R.styleable.HorizontalExpCalendar_top_container_height)) {
      ((LinearLayout.LayoutParams) findViewById(R.id.top_container).getLayoutParams()).height =
          typedArray.getDimensionPixelSize(R.styleable.HorizontalExpCalendar_top_container_height,
              LinearLayout.LayoutParams.WRAP_CONTENT);
    }
  }


  private void setupViews() {
    initTopContainer();
    initCenterContainer();
    initBottomContainer();
    refreshTitleTextView();
  }

  private void initTopContainer() {
    titleTextView = (TextView) findViewById(R.id.title);
    findViewById(R.id.scroll_to_init_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        scrollToDate(Config.INIT_DATE, true, true, true);
      }
    });
  }

  private void initCenterContainer() {
    initMonthViewPager();
    initWeekViewPager();
  }

  private void initBottomContainer() {
    findViewById(R.id.change_calendar_view_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        switch (Config.currentViewPager) {
          case MONTH:
            scrollToDate(Config.scrollDate, false, true, false);
            monthViewPager.setVisibility(View.GONE);
            weekViewPager.setVisibility(View.VISIBLE);
            Config.currentViewPager = Config.ViewPagerType.WEEK;
            setHeightToCenterContainer(weekViewPagerHeight);
            weekPagerAdapter.updateMarks();
            break;
          case WEEK:
            scrollToDate(Config.scrollDate, true, false, false);
            monthViewPager.setVisibility(View.VISIBLE);
            weekViewPager.setVisibility(View.GONE);
            Config.currentViewPager = Config.ViewPagerType.MONTH;
            setHeightToCenterContainer(monthViewPagerHeight);
            monthPagerAdapter.updateMarks();
            break;
          default:
            Log.e(TAG, "switchViewButton click, unknown type of currentViewPager");
        }
      }
    });
  }

  private void initMonthViewPager() {
    monthViewPager = (ViewPager) findViewById(R.id.month_view_pager);
    monthPagerAdapter = new CalendarAdapter(getContext(), Config.ViewPagerType.MONTH, this);
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
    weekPagerAdapter = new CalendarAdapter(getContext(), Config.ViewPagerType.WEEK, this);
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
    ((LinearLayout.LayoutParams) findViewById(R.id.center_container).getLayoutParams()).height = height;
  }

  private void scrollToDate(DateTime dateTime, boolean animate) {
    onDayClick(dateTime);
    boolean isMonthView = Utils.isMonthView();
    scrollToDate(dateTime, isMonthView, !isMonthView, animate);
  }

  private void scrollToDate(DateTime dateTime, boolean scrollMonthPager, boolean scrollWeekPager, boolean animate) {
    if (scrollMonthPager) {
      setMonthViewPagerPosition(Utils.monthsBetween(Config.START_DATE, dateTime), animate);
    }
    if (scrollWeekPager) {
      setWeekViewPagerPosition(Utils.weeksBetween(Config.START_DATE, dateTime.withDayOfMonth(1)), animate);
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
    if (Config.currentViewPager == Config.ViewPagerType.MONTH) {
      monthPagerAdapter.updateMarks();
    } else {
      weekPagerAdapter.updateMarks();
    }
  }

  @Override
  public void onDayClick(DateTime dateTime) {
    Marks.refreshMarkSelected(dateTime);
    updateMarks();
  }
}