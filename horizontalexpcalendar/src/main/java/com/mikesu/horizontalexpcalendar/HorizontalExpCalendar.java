package com.mikesu.horizontalexpcalendar;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mikesu.horizontalexpcalendar.adapter.CalendarAdapter;
import com.mikesu.horizontalexpcalendar.animator.CalendarAnimation;
import com.mikesu.horizontalexpcalendar.common.Config;
import com.mikesu.horizontalexpcalendar.common.Constants;
import com.mikesu.horizontalexpcalendar.common.Marks;
import com.mikesu.horizontalexpcalendar.common.Utils;
import com.mikesu.horizontalexpcalendar.listener.SmallAnimationListener;
import com.mikesu.horizontalexpcalendar.listener.SmallPageChangeListener;
import com.mikesu.horizontalexpcalendar.view.cell.BaseCellView;
import com.mikesu.horizontalexpcalendar.view.cell.DayCellView;
import com.mikesu.horizontalexpcalendar.view.page.PageView;
import org.joda.time.DateTime;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class HorizontalExpCalendar extends RelativeLayout implements PageView.PageViewListener {

  private TextView titleTextView;
  private RelativeLayout centerContainer;
  private GridLayout animateContainer;
  private RelativeLayout.LayoutParams animateContainerParams;

  private ViewPager monthViewPager;
  private CalendarAdapter monthPagerAdapter;
  private int monthViewPagerHeight;

  private ViewPager weekViewPager;
  private CalendarAdapter weekPagerAdapter;
  private int weekViewPagerHeight;

  private HorizontalExpCalListener horizontalExpCalListener;

  private CalendarAnimation decreasingAnimation;
  private CalendarAnimation increasingAnimation;

  public HorizontalExpCalendar(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public HorizontalExpCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  public void setHorizontalExpCalListener(HorizontalExpCalListener horizontalExpCalListener) {
    this.horizontalExpCalListener = horizontalExpCalListener;
  }

  public void removeHorizontalExpCalListener() {
    this.horizontalExpCalListener = null;
  }

  @Override
  protected void onDetachedFromWindow() {
    Marks.clear();
    super.onDetachedFromWindow();
  }

  private void init(AttributeSet attributeSet) {
    inflate(getContext(), R.layout.horizontal_exp_calendar, this);

    centerContainer = (RelativeLayout) findViewById(R.id.center_container);

    setValuesFromAttr(attributeSet);
    setupCellWidth();

    Marks.init();
    Marks.markToday();
    Marks.refreshMarkSelected(Config.selectionDate);
    renderCustomMarks();

    initAnimation();
  }

  private void initAnimation() {
    decreasingAnimation = new CalendarAnimation();
    decreasingAnimation.setFloatValues(Constants.ANIMATION_DECREASING_VALUES[0], Constants.ANIMATION_DECREASING_VALUES[1]);
    decreasingAnimation.setDuration(Constants.ANIMATION_ALPHA_DURATION);

    increasingAnimation = new CalendarAnimation();
    increasingAnimation.setFloatValues(Constants.ANIMATION_INCREASING_VALUES[0], Constants.ANIMATION_INCREASING_VALUES[1]);
    increasingAnimation.setDuration(Constants.ANIMATION_ALPHA_DURATION);
  }

  private void renderCustomMarks() {
    // custom1
    Marks.refreshCustomMark(new DateTime().minusDays(5), Marks.CustomMarks.CUSTOM1, true);
    Marks.refreshCustomMark(new DateTime().plusDays(1), Marks.CustomMarks.CUSTOM1, true);
    Marks.refreshCustomMark(new DateTime().plusDays(4), Marks.CustomMarks.CUSTOM1, true);
    // custom2
    Marks.refreshCustomMark(new DateTime().minusDays(7), Marks.CustomMarks.CUSTOM2, true);
    Marks.refreshCustomMark(new DateTime().plusDays(1), Marks.CustomMarks.CUSTOM2, true);
    Marks.refreshCustomMark(new DateTime().plusDays(10), Marks.CustomMarks.CUSTOM2, true);
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
    initAnimateContainer();
    refreshTitleTextView();
  }

  private void initAnimateContainer() {
    animateContainer = (GridLayout) findViewById(R.id.animate_container);
    animateContainer.getLayoutParams().height = Config.cellHeight;
    animateContainerParams = (LayoutParams) animateContainer.getLayoutParams();
  }

  private void initTopContainer() {
    titleTextView = (TextView) findViewById(R.id.title);
    findViewById(R.id.scroll_to_today_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        scrollToDate(new DateTime(), true, true, true);
      }
    });
  }

  private void initCenterContainer() {
    initMonthViewPager();
    initWeekViewPager();
  }

  private void initBottomContainer() {
    findViewById(R.id.collapse_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (Config.currentViewPager != Config.ViewPagerType.WEEK) {
          switchToView(Config.ViewPagerType.WEEK);
        }
      }
    });

    findViewById(R.id.expand_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (Config.currentViewPager != Config.ViewPagerType.MONTH) {
          switchToView(Config.ViewPagerType.MONTH);
        }
      }
    });
  }


  private void switchToView(final Config.ViewPagerType switchTo) {
    Config.currentViewPager = switchTo;

    removeAnimationsListener();
    decreasingAnimation.setListener(new SmallAnimationListener() {
      @Override
      public void animationStart(Animator animation) {
        if (Utils.isMonthView()) {
          monthViewPager.setVisibility(GONE);
          weekViewPager.setVisibility(VISIBLE);
        } else {
          monthViewPager.setVisibility(VISIBLE);
          weekViewPager.setVisibility(GONE);
        }
      }

      @Override
      public void animationEnd(Animator animation) {
        monthViewPager.setVisibility(GONE);
        weekViewPager.setVisibility(GONE);

        removeAnimationsListener();
        increasingAnimation.setListener(new SmallAnimationListener() {
          @Override
          public void animationStart(Animator animation) {
            if (Utils.isMonthView()) {
              monthViewPager.setVisibility(VISIBLE);
              weekViewPager.setVisibility(GONE);
            } else {
              monthViewPager.setVisibility(GONE);
              weekViewPager.setVisibility(VISIBLE);
            }

            if (!Utils.isMonthView() && Config.SCROLL_TO_SELECTED_AFTER_COLLAPSE && Utils.isTheSameMonthToScrollDate(Config.selectionDate)) {
              Config.scrollDate = Config.selectionDate.plusDays(-Utils.firstDayOffset());
            } else {
              Config.scrollDate = Config.scrollDate.withDayOfMonth(1);
            }

            if (Utils.isMonthView()) {
              scrollToDate(Config.scrollDate, true, false, false);
              setHeightToCenterContainer(monthViewPagerHeight);
              if (horizontalExpCalListener != null) {
                horizontalExpCalListener.onChangeViewPager(Config.ViewPagerType.MONTH);
              }
            } else {
//        addCellsToAnimateContainer();
//        animateContainerParams.topMargin = Config.cellHeight * Utils.getWeekOfMonth(Config.scrollDate);
              scrollToDate(Config.scrollDate, false, true, false);
              setHeightToCenterContainer(weekViewPagerHeight);
              if (horizontalExpCalListener != null) {
                horizontalExpCalListener.onChangeViewPager(Config.ViewPagerType.WEEK);
              }
            }
            weekPagerAdapter.updateMarks();
          }

          @Override
          public void animationEnd(Animator animation) {
            if (Utils.isMonthView()) {
              monthViewPager.setVisibility(VISIBLE);
              weekViewPager.setVisibility(GONE);
            } else {
              monthViewPager.setVisibility(GONE);
              weekViewPager.setVisibility(VISIBLE);
            }
          }

          @Override
          public void animationUpdate(Object value) {
            if (Utils.isMonthView()) {
              monthViewPager.setAlpha((Float) value);
            } else {
              weekViewPager.setAlpha((Float) value);
            }
          }
        });
      }

      @Override
      public void animationUpdate(Object value) {
        if (Utils.isMonthView()) {
          weekViewPager.setAlpha((Float) value);
        } else {
          monthViewPager.setAlpha((Float) value);

        }
      }
    });
  }

  private void removeAnimationsListener() {
    decreasingAnimation.removeAllListeners();
  }

  private void addCellsToAnimateContainer() {
    animateContainer.removeAllViews();
    DateTime animateInitDate = Config.scrollDate.withDayOfWeek(1).plusDays(Utils.firstDayOffset());
    for (int d = 0; d < 7; d++) {
      DateTime cellDate = animateInitDate.plusDays(d);

      DayCellView dayCellView = new DayCellView(getContext());

      GridLayout.LayoutParams cellParams = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(d));
      cellParams.height = Config.cellHeight;
      cellParams.width = Config.cellWidth;
      dayCellView.setLayoutParams(cellParams);
      dayCellView.setDayNumber(cellDate.getDayOfMonth());
      dayCellView.setDayType(Utils.isWeekendByColumnNumber(d) ? BaseCellView.DayType.WEEKEND : BaseCellView.DayType.NO_WEEKEND);
      dayCellView.setMark(Marks.getMark(cellDate), Config.cellHeight);
      dayCellView.setTimeType(getTimeType(cellDate));

      animateContainer.addView(dayCellView);
    }
  }

  private DayCellView.TimeType getTimeType(DateTime cellTime) {
    if (cellTime.getMonthOfYear() < Config.scrollDate.getMonthOfYear()) {
      return DayCellView.TimeType.PAST;
    } else if (cellTime.getMonthOfYear() > Config.scrollDate.getMonthOfYear()) {
      return DayCellView.TimeType.FUTURE;
    } else {
      return DayCellView.TimeType.CURRENT;
    }
  }

  private void initMonthViewPager() {
    monthViewPager = (ViewPager) findViewById(R.id.month_view_pager);
    monthPagerAdapter = new CalendarAdapter(getContext(), Config.ViewPagerType.MONTH, this);
    monthViewPager.setAdapter(monthPagerAdapter);
    monthViewPager.setCurrentItem(Utils.monthPositionFromDate(Config.INIT_DATE));
    monthViewPager.addOnPageChangeListener(new SmallPageChangeListener() {
      @Override
      public void scrollStateChanged(int state) {
        if (Utils.isMonthView()) {
          if (state == ViewPager.SCROLL_STATE_IDLE) {
            Config.scrollDate = Utils.getDateByMonthPosition(monthViewPager.getCurrentItem());
            refreshTitleTextView();
            if (horizontalExpCalListener != null) {
              horizontalExpCalListener.onCalendarScroll(Config.scrollDate.withDayOfMonth(1));
            }
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
    setWeekViewPagerPosition(Utils.weekPositionFromDate(Config.INIT_DATE), false);
    weekViewPager.addOnPageChangeListener(new SmallPageChangeListener() {
      @Override
      public void scrollStateChanged(int state) {
        if (!Utils.isMonthView()) {
          if (state == ViewPager.SCROLL_STATE_IDLE) {
            Config.scrollDate = Utils.getDateByWeekPosition(weekViewPager.getCurrentItem());
            refreshTitleTextView();
            if (horizontalExpCalListener != null) {
              horizontalExpCalListener.onCalendarScroll(Config.scrollDate.withDayOfWeek(1));
            }
          }
        }
      }
    });
    weekViewPager.setVisibility(!Utils.isMonthView() ? VISIBLE : GONE);
  }


  private void setHeightToCenterContainer(int height) {
    ((LinearLayout.LayoutParams) centerContainer.getLayoutParams()).height = height;
    centerContainer.requestLayout();
  }

  public void scrollToDate(DateTime dateTime, boolean animate) {
    if (Config.currentViewPager == Config.ViewPagerType.MONTH && Utils.isTheSameMonthToScrollDate(dateTime)) {
      return;
    }
    if (Config.currentViewPager == Config.ViewPagerType.WEEK && Utils.isTheSameWeekToScrollDate(dateTime)) {
      return;
    }

    boolean isMonthView = Utils.isMonthView();
    scrollToDate(dateTime, isMonthView, !isMonthView, animate);
  }

  private void scrollToDate(DateTime dateTime, boolean scrollMonthPager, boolean scrollWeekPager, boolean animate) {
    if (scrollMonthPager) {
      setMonthViewPagerPosition(Utils.monthPositionFromDate(dateTime), animate);
    }
    if (scrollWeekPager) {
      setWeekViewPagerPosition(Utils.weekPositionFromDate(dateTime), animate);
    }
  }

  private void setWeekViewPagerPosition(int position, boolean animate) {
    weekViewPager.setCurrentItem(position, animate);
  }

  private void setMonthViewPagerPosition(int position, boolean animate) {
    monthViewPager.setCurrentItem(position, animate);
  }

  private void refreshTitleTextView() {
    titleTextView.setText(String.format("%s - %s", Config.scrollDate.getYear(), Config.scrollDate.getMonthOfYear()));
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
    scrollToDate(dateTime, true);

    Marks.refreshMarkSelected(dateTime);
    updateMarks();

    if (horizontalExpCalListener != null) {
      horizontalExpCalListener.onDateSelected(dateTime);
    }
  }

  public interface HorizontalExpCalListener {
    void onCalendarScroll(DateTime dateTime);

    void onDateSelected(DateTime dateTime);

    void onChangeViewPager(Config.ViewPagerType viewPagerType);
  }
}