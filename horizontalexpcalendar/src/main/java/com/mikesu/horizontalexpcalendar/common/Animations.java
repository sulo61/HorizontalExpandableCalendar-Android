package com.mikesu.horizontalexpcalendar.common;

import android.animation.Animator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import com.mikesu.horizontalexpcalendar.animator.CalendarAnimation;
import com.mikesu.horizontalexpcalendar.listener.SmallAnimationListener;
import com.mikesu.horizontalexpcalendar.view.cell.BaseCellView;
import com.mikesu.horizontalexpcalendar.view.cell.DayCellView;
import org.joda.time.DateTime;

/**
 * Created by MikeSu on 18/08/16.
 * www.michalsulek.pl
 */
public class Animations {

  private static final String TAG = Animations.class.getName();

  private CalendarAnimation decreasingAlphaAnimation;
  private CalendarAnimation increasingAlphaAnimation;
  private CalendarAnimation decreasingSizeAnimation;
  private CalendarAnimation increasingSizeAnimation;
  private int animContainerExpandedMargin;
  private int animContainerCollapsedMargin;

  private AnimationsListener animationsListener;
  private Context context;

  public Animations(Context context, AnimationsListener animationsListener) {
    this.context = context;
    this.animationsListener = animationsListener;
    initAnimation();
  }

  public void initAnimation() {
    decreasingAlphaAnimation = new CalendarAnimation();
    decreasingAlphaAnimation.setFloatValues(Constants.ANIMATION_DECREASING_VALUES[0], Constants.ANIMATION_DECREASING_VALUES[1]);
    decreasingAlphaAnimation.setDuration(Constants.ANIMATION_ALPHA_DURATION);

    increasingAlphaAnimation = new CalendarAnimation();
    increasingAlphaAnimation.setFloatValues(Constants.ANIMATION_INCREASING_VALUES[0], Constants.ANIMATION_INCREASING_VALUES[1]);
    increasingAlphaAnimation.setDuration(Constants.ANIMATION_ALPHA_DURATION);

    decreasingSizeAnimation = new CalendarAnimation();
    decreasingSizeAnimation.setFloatValues(Constants.ANIMATION_DECREASING_VALUES[0], Constants.ANIMATION_DECREASING_VALUES[1]);
    decreasingSizeAnimation.setDuration(Constants.ANIMATION_SIZE_DURATION);

    increasingSizeAnimation = new CalendarAnimation();
    increasingSizeAnimation.setFloatValues(Constants.ANIMATION_INCREASING_VALUES[0], Constants.ANIMATION_INCREASING_VALUES[1]);
    increasingSizeAnimation.setDuration(Constants.ANIMATION_SIZE_DURATION);

    animContainerExpandedMargin = 0;
    animContainerCollapsedMargin = 0;
  }

  public void startDecreaseSizeAnimation() {
    decreasingSizeAnimation.setListener(new SmallAnimationListener() {
      @Override
      public void animationStart(Animator animation) {
        animationsListener.setHeightToCenterContainer(Config.monthViewPagerHeight);
      }

      @Override
      public void animationEnd(Animator animation) {
        animationsListener.setHeightToCenterContainer(Config.weekViewPagerHeight);
        clearAnimationsListener();
        startShowPagerAnimation();
      }

      @Override
      public void animationUpdate(Object value) {
        animationsListener.setHeightToCenterContainer(getAnimationCenterContainerHeight((float) value));
        animationsListener.setTopMarginToAnimationContainer(
            (int) ((animContainerExpandedMargin - animContainerCollapsedMargin) * (float) value) + animContainerCollapsedMargin);
      }
    });
  }

  public void startIncreaseSizeAnimation() {
    increasingSizeAnimation.setListener(new SmallAnimationListener() {
      @Override
      public void animationStart(Animator animation) {
        animationsListener.setHeightToCenterContainer(Config.weekViewPagerHeight);
      }

      @Override
      public void animationEnd(Animator animation) {
        animationsListener.setHeightToCenterContainer(Config.monthViewPagerHeight);
        clearAnimationsListener();
        startShowPagerAnimation();
      }

      @Override
      public void animationUpdate(Object value) {
        animationsListener.setHeightToCenterContainer(getAnimationCenterContainerHeight((float) value));
        animationsListener.setTopMarginToAnimationContainer(
            (int) ((animContainerExpandedMargin - animContainerCollapsedMargin) * (float) value) + animContainerCollapsedMargin);
      }
    });
  }

  public void startHidePagerAnimation() {
    if (animationsListener == null) {
      Log.e(TAG, "startHidePagerAnimation, animationsListener is null");
      return;
    }
    decreasingAlphaAnimation.setListener(new SmallAnimationListener() {
      @Override
      public void animationStart(Animator animation) {
        if (Utils.isMonthView()) {
          animationsListener.setMonthPagerVisibility(View.GONE);
          animationsListener.setWeekPagerVisibility(View.VISIBLE);
        } else {
          animationsListener.setMonthPagerVisibility(View.VISIBLE);
          animationsListener.setWeekPagerVisibility(View.GONE);
        }

        animationsListener.setAnimatedContainerVisibility(View.VISIBLE);
        addCellsToAnimateContainer();
        animContainerExpandedMargin =
            Config.cellHeight * Utils.getWeekOfMonth(Config.scrollDate) - 1 + Utils.dayLabelExtraRow();
        animContainerCollapsedMargin = Config.cellHeight * (Utils.dayLabelExtraRow());
        animationsListener.setTopMarginToAnimationContainer(animContainerExpandedMargin);
      }

      @Override
      public void animationEnd(Animator animation) {
        animationsListener.setMonthPagerVisibility(View.GONE);
        animationsListener.setWeekPagerVisibility(View.GONE);
        clearAnimationsListener();
        if (Utils.isMonthView()) {
          startIncreaseSizeAnimation();
        } else {
          startDecreaseSizeAnimation();
        }
      }

      @Override
      public void animationUpdate(Object value) {
        if (Utils.isMonthView()) {
          animationsListener.setWeekPagerAlpha((float) value);
        } else {
          animationsListener.setMonthPagerAlpha((float) value);
        }
      }
    });
  }

  public void startShowPagerAnimation() {
    increasingAlphaAnimation.setListener(new SmallAnimationListener() {
      @Override
      public void animationStart(Animator animation) {
        if (Utils.isMonthView()) {
          animationsListener.setMonthPagerVisibility(View.VISIBLE);
          animationsListener.setWeekPagerVisibility(View.GONE);
        } else {
          animationsListener.setMonthPagerVisibility(View.GONE);
          animationsListener.setWeekPagerVisibility(View.VISIBLE);
        }

        if (!Utils.isMonthView() && Config.SCROLL_TO_SELECTED_AFTER_COLLAPSE && Utils.isTheSameMonthToScrollDate(Config.selectionDate)) {
          Config.scrollDate = Config.selectionDate.plusDays(-Utils.firstDayOffset());
        } else {
          Config.scrollDate = Config.scrollDate.withDayOfMonth(1);
        }

        if (Utils.isMonthView()) {
          animationsListener.scrollToDate(Config.scrollDate, true, false, false);
          animationsListener.setHeightToCenterContainer(Config.monthViewPagerHeight);
          animationsListener.changeViewPager(Config.ViewPagerType.MONTH);
          animationsListener.updateMonthMarks();
        } else {
          animationsListener.scrollToDate(Config.scrollDate, false, true, false);
          animationsListener.setHeightToCenterContainer(Config.weekViewPagerHeight);
          animationsListener.changeViewPager(Config.ViewPagerType.WEEK);
          animationsListener.updateWeekMarks();
        }
      }

      @Override
      public void animationEnd(Animator animation) {
        clearAnimationsListener();
        animationsListener.setAnimatedContainerVisibility(View.VISIBLE);
        animationsListener.animateContainerRemoveViews();
        if (Utils.isMonthView()) {
          animationsListener.setMonthPagerVisibility(View.VISIBLE);
          animationsListener.setWeekPagerVisibility(View.GONE);
        } else {
          animationsListener.setMonthPagerVisibility(View.GONE);
          animationsListener.setWeekPagerVisibility(View.VISIBLE);
        }
      }

      @Override
      public void animationUpdate(Object value) {
        if (Utils.isMonthView()) {
          animationsListener.setMonthPagerAlpha((float) value);
        } else {
          animationsListener.setMonthPagerAlpha((float) value);
        }
      }
    });
  }

  public int getAnimationCenterContainerHeight(float value) {
    return (int) ((((Config.monthViewPagerHeight - Config.weekViewPagerHeight) * value)) + Config.weekViewPagerHeight);
  }

  public void clearAnimationsListener() {
    decreasingAlphaAnimation.removeAllListeners();
    increasingAlphaAnimation.removeAllListeners();
    decreasingSizeAnimation.removeAllListeners();
    increasingSizeAnimation.removeAllListeners();
  }

  public void addCellsToAnimateContainer() {
    animationsListener.animateContainerRemoveViews();
    DateTime animateInitDate = Config.scrollDate.withDayOfWeek(1).plusDays(Utils.firstDayOffset());
    for (int d = 0; d < 7; d++) {
      DateTime cellDate = animateInitDate.plusDays(d);

      DayCellView dayCellView = new DayCellView(context);

      GridLayout.LayoutParams cellParams = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(d));
      cellParams.height = Config.cellHeight;
      cellParams.width = Config.cellWidth;
      dayCellView.setLayoutParams(cellParams);
      dayCellView.setDayNumber(cellDate.getDayOfMonth());
      dayCellView.setDayType(Utils.isWeekendByColumnNumber(d) ? BaseCellView.DayType.WEEKEND : BaseCellView.DayType.NO_WEEKEND);
      dayCellView.setMark(Marks.getMark(cellDate), Config.cellHeight);
      dayCellView.setTimeType(getTimeType(cellDate));

      animationsListener.animateContainerAddView(dayCellView);
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

  public interface AnimationsListener {
    void setHeightToCenterContainer(int height);

    void setTopMarginToAnimationContainer(int margin);

    void setWeekPagerVisibility(int visibility);

    void setMonthPagerVisibility(int visibility);

    void setAnimatedContainerVisibility(int visibility);

    void setMonthPagerAlpha(float alpha);

    void setWeekPagerAlpha(float alpha);

    void scrollToDate(DateTime dateTime, boolean scrollMonthPager, boolean scrollWeekPager, boolean animate);

    void animateContainerAddView(View view);

    void animateContainerRemoveViews();

    void updateWeekMarks();

    void updateMonthMarks();

    void changeViewPager(Config.ViewPagerType viewPagerType);
  }

}
