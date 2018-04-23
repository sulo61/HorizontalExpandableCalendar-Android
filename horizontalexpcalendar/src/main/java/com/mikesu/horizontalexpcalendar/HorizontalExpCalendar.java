package com.mikesu.horizontalexpcalendar;

import android.annotation.SuppressLint;
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
import com.mikesu.horizontalexpcalendar.common.Animations;
import com.mikesu.horizontalexpcalendar.common.Config;
import com.mikesu.horizontalexpcalendar.common.Marks;
import com.mikesu.horizontalexpcalendar.common.Utils;
import com.mikesu.horizontalexpcalendar.listener.SmallPageChangeListener;
import com.mikesu.horizontalexpcalendar.view.page.PageView;

import org.joda.time.DateTime;

/**
 * Created by MikeSu on 23/04/18.
 * www.michalsulek.pl
 */

public class HorizontalExpCalendar extends RelativeLayout implements PageView.PageViewListener, Animations.AnimationsListener {

  private TextView titleTextView;
  private RelativeLayout centerContainer;
  private GridLayout animateContainer;

  private ViewPager monthViewPager;
  private CalendarAdapter monthPagerAdapter;

  private ViewPager weekViewPager;
  private CalendarAdapter weekPagerAdapter;

  private Animations animations;
  private HorizontalExpCalListener horizontalExpCalListener;

  private boolean lock;

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
    animations.unbind();
    Marks.clear();
    super.onDetachedFromWindow();
  }

  private void init(AttributeSet attributeSet) {
    inflate(getContext(), R.layout.horizontal_exp_calendar, this);

    centerContainer = (RelativeLayout) findViewById(R.id.center_container);
    lock = false;

    setValuesFromAttr(attributeSet);
    setupCellWidth();

    Marks.init();
    Marks.markToday();
    Marks.refreshMarkSelected(Config.selectionDate);
    renderCustomMarks();

    initAnimation();
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
    Config.cellHeight = Config.monthViewPagerHeight / (Config.MONTH_ROWS + Utils.dayLabelExtraRow());
  }

  public void setupCellWidth() {
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
      setupExpandedFromAttr(typedArray);
      typedArray.recycle();
    }

    setHeightToCenterContainer(Utils.isMonthView() ? Config.monthViewPagerHeight : Config.weekViewPagerHeight);
  }

  @SuppressLint("WrongViewCast")
  private void setupBottomContainerFromAttr(TypedArray typedArray) {
    if (typedArray.hasValue(R.styleable.HorizontalExpCalendar_bottom_container_height)) {
      ((LinearLayout.LayoutParams) findViewById(R.id.bottom_container).getLayoutParams()).height =
              typedArray.getDimensionPixelSize(R.styleable.HorizontalExpCalendar_bottom_container_height,
                      LinearLayout.LayoutParams.WRAP_CONTENT);
    }
  }

  private void setupMiddleContainerFromAttr(TypedArray typedArray) {
    if (typedArray.hasValue(R.styleable.HorizontalExpCalendar_center_container_expanded_height)) {
      Config.monthViewPagerHeight = typedArray.getDimensionPixelSize(
              R.styleable.HorizontalExpCalendar_center_container_expanded_height, LinearLayout.LayoutParams.WRAP_CONTENT);

      setCellHeight();

      Config.weekViewPagerHeight = Config.cellHeight * (Config.USE_DAY_LABELS ? 2 : 1);
    }
  }

  @SuppressLint("WrongViewCast")
  private void setupTopContainerFromAttr(TypedArray typedArray) {
    if (typedArray.hasValue(R.styleable.HorizontalExpCalendar_top_container_height)) {
      ((LinearLayout.LayoutParams) findViewById(R.id.top_container).getLayoutParams()).height =
              typedArray.getDimensionPixelSize(R.styleable.HorizontalExpCalendar_top_container_height,
                      LinearLayout.LayoutParams.WRAP_CONTENT);
    }
  }

  private void setupExpandedFromAttr(TypedArray typedArray) {
    if (typedArray.hasValue(R.styleable.HorizontalExpCalendar_expanded)) {
      Config.currentViewPager = typedArray.getBoolean(R.styleable.HorizontalExpCalendar_expanded, true) ?
              Config.ViewPagerType.MONTH : Config.ViewPagerType.WEEK;
    }
  }

  private void initAnimation() {
    animations = new Animations(getContext(), HorizontalExpCalendar.this, Utils.animateContainerExtraTopOffset(getResources()));
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
    int sideMargin = Utils.animateContainerExtraSideOffset(getResources());
    animateContainer.setPadding(sideMargin, 0, sideMargin, 0);
  }

  private void initTopContainer() {
    titleTextView = (TextView) findViewById(R.id.title);
    findViewById(R.id.scroll_to_today_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!isLocked()) {
          lock();
          scrollToDate(new DateTime(), true, true, true);
          unlock();
        }
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
        collapse();
      }
    });

    findViewById(R.id.expand_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        expand();
      }
    });
  }

  public void collapse() {
    if (!isLocked()) {
      lock();
      if (Config.currentViewPager != Config.ViewPagerType.WEEK) {
        switchToView(Config.ViewPagerType.WEEK);
      }
      unlock();
      if (horizontalExpCalListener != null) {
        horizontalExpCalListener.onCalendarScroll(Config.scrollDate.withDayOfWeek(1));
      }
    }
  }

  public boolean isExpanded() {
    return Config.currentViewPager == Config.ViewPagerType.MONTH;
  }

  public void expand() {
    if (!isLocked()) {
      lock();
      if (Config.currentViewPager != Config.ViewPagerType.MONTH) {
        switchToView(Config.ViewPagerType.MONTH);
      }
      unlock();
      if (horizontalExpCalListener != null) {
        horizontalExpCalListener.onCalendarScroll(Config.scrollDate.withDayOfWeek(7));
      }
    }
  }

  private void switchToView(final Config.ViewPagerType switchTo) {
    if (monthViewPager == null || weekPagerAdapter == null) return;
    Config.currentViewPager = switchTo;
    animations.clearAnimationsListener();
    animations.startHidePagerAnimation();
  }

  private void initMonthViewPager() {
    monthViewPager = (ViewPager) findViewById(R.id.month_view_pager);
    monthPagerAdapter = new CalendarAdapter(getContext(), Config.ViewPagerType.MONTH, this);
    monthViewPager.setAdapter(monthPagerAdapter);
    monthViewPager.setCurrentItem(Utils.monthPositionFromDate(Config.INIT_DATE));
    monthViewPager.addOnPageChangeListener(new SmallPageChangeListener() {
      @Override
      public void scrollStart() {
        if (Utils.isMonthView()) {
          lock();
        }
      }

      @Override
      public void scrollEnd() {
        if (Utils.isMonthView()) {
          Config.scrollDate = Utils.getDateByMonthPosition(monthViewPager.getCurrentItem());
          if (Utils.isTheSameMonthToScrollDate(Config.selectionDate)) {
            Config.scrollDate = Config.selectionDate.toDateTime();
          }
          refreshTitleTextView();
          if (horizontalExpCalListener != null) {
            horizontalExpCalListener.onCalendarScroll(Config.scrollDate.withDayOfMonth(1));
          }
          unlock();
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
      public void scrollStart() {
        if (!Utils.isMonthView()) {
          lock();
        }
      }

      @Override
      public void scrollEnd() {
        if (!Utils.isMonthView()) {
          Config.scrollDate = Utils.getDateByWeekPosition(weekViewPager.getCurrentItem());
          if (Utils.weekPositionFromDate(Config.scrollDate) == Utils.weekPositionFromDate(Config.selectionDate)) {
            Config.scrollDate = Config.selectionDate;
          }
          refreshTitleTextView();
          if (horizontalExpCalListener != null) {
            horizontalExpCalListener.onCalendarScroll(Config.scrollDate.withDayOfWeek(1));
          }
          unlock();
        }
      }
    });
    weekViewPager.setVisibility(!Utils.isMonthView() ? VISIBLE : GONE);
  }

  public void scrollToDate(DateTime dateTime, boolean animate) {
    if (Config.currentViewPager == Config.ViewPagerType.MONTH && Utils.isTheSameMonthToScrollDate(dateTime)) {
      return;
    }
    if (Config.currentViewPager == Config.ViewPagerType.WEEK && Utils.isTheSameWeekToScrollDate(dateTime) && Utils.isTheSameMonthToScrollDate(dateTime)) {
      return;
    }

    boolean isMonthView = Utils.isMonthView();
    scrollToDate(dateTime, isMonthView, !isMonthView, animate);
  }

  private void setWeekViewPagerPosition(int position, boolean animate) {
    weekViewPager.setCurrentItem(position, animate);
  }

  private void setMonthViewPagerPosition(int position, boolean animate) {
    monthViewPager.setCurrentItem(position, animate);
  }

  private void refreshTitleTextView() {
    DateTime titleDate = Config.scrollDate;
    if (Config.currentViewPager == Config.ViewPagerType.MONTH) {
      if (Utils.isTheSameMonthToScrollDate(Config.selectionDate)) {
        titleDate = Config.selectionDate;
      }
    } else {
      if (Utils.isTheSameWeekToScrollDate(Config.selectionDate)) {
        titleDate = Config.selectionDate;
      }
    }
    titleTextView.setText(String.format("%s - %s", titleDate.getYear(), titleDate.getMonthOfYear()));
  }

  private void lock() {
    lock = true;
  }

  private void unlock() {
    lock = false;
  }

  private boolean isLocked() {
    return lock;
  }

  public void setVisible() {
    setVisibility(View.VISIBLE);
    if (Config.cellWidth == 0) {
      setupCellWidth();
    }
    if (Config.cellHeight == 0) {
      setCellHeight();
    }
  }

  public void setGone() {
    setVisibility(View.GONE);
  }

  @Override
  public void scrollToDate(DateTime dateTime, boolean scrollMonthPager, boolean scrollWeekPager, boolean animate) {
    if (scrollMonthPager) {
      setMonthViewPagerPosition(Utils.monthPositionFromDate(dateTime), animate);
    }
    if (scrollWeekPager) {
      setWeekViewPagerPosition(Utils.weekPositionFromDate(dateTime), animate);
    }
  }

  @Override
  public void animateContainerAddView(View view) {
    animateContainer.addView(view);
  }

  @Override
  public void animateContainerRemoveViews() {
    animateContainer.removeAllViews();
  }

  @Override
  public void updateMarks() {
    if (Config.currentViewPager == Config.ViewPagerType.MONTH) {
      monthPagerAdapter.updateMarks();
    } else {
      weekPagerAdapter.updateMarks();
    }
  }

  @Override
  public void changeViewPager(Config.ViewPagerType viewPagerType) {
    if (horizontalExpCalListener != null) {
      horizontalExpCalListener.onChangeViewPager(viewPagerType);
    }
  }

  @Override
  public void onDayClick(DateTime dateTime) {
    scrollToDate(dateTime, true);

    Marks.refreshMarkSelected(dateTime);
    updateMarks();

    refreshTitleTextView();

    if (horizontalExpCalListener != null) {
      horizontalExpCalListener.onDateSelected(dateTime);
    }
  }

  @Override
  public void setHeightToCenterContainer(int height) {
    ((LinearLayout.LayoutParams) centerContainer.getLayoutParams()).height = height;
    centerContainer.requestLayout();
  }

  @Override
  public void setTopMarginToAnimContainer(int margin) {
    ((RelativeLayout.LayoutParams) animateContainer.getLayoutParams()).topMargin = margin;
  }

  @Override
  public void setWeekPagerVisibility(int visibility) {
    weekViewPager.setVisibility(visibility);
  }

  @Override
  public void setMonthPagerVisibility(int visibility) {
    monthViewPager.setVisibility(visibility);
  }

  @Override
  public void setAnimatedContainerVisibility(int visibility) {
    animateContainer.setVisibility(visibility);
  }

  @Override
  public void setMonthPagerAlpha(float alpha) {
    monthViewPager.setAlpha(alpha);
  }

  @Override
  public void setWeekPagerAlpha(float alpha) {
    weekViewPager.setAlpha(alpha);
  }

  public interface HorizontalExpCalListener {
    void onCalendarScroll(DateTime dateTime);

    void onDateSelected(DateTime dateTime);

    void onChangeViewPager(Config.ViewPagerType viewPagerType);
  }
}