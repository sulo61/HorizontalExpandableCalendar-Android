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
  private int expandedTopMargin;
  private int collapsedTopMargin;

  private AnimationsListener animationsListener;
  private Context context;
  private int extraTopMarginOffset;

  public Animations(Context context, AnimationsListener animationsListener, int extraTopMarginOffset) {
    this.context = context;
    this.animationsListener = animationsListener;
    this.extraTopMarginOffset = extraTopMarginOffset;
    initAnimation();
  }

  public void unbind() {
    this.context = null;
    this.animationsListener = null;
  }

  private void initAnimation() {
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

    expandedTopMargin = 0;
    collapsedTopMargin = 0;
  }

  public void startHidePagerAnimation() {
    if (animationsListener == null || decreasingAlphaAnimation == null) {
      Log.e(TAG, "startHidePagerAnimation, animationsListener or decreasingAlphaAnimation is null");
      return;
    }
    decreasingAlphaAnimation.setListener(new SmallAnimationListener() {
      @Override
      public void animationStart(Animator animation) {
        if (animationsListener == null) return;

        if (Utils.isMonthView()) {
          animationsListener.setMonthPagerVisibility(View.GONE);
          animationsListener.setWeekPagerVisibility(View.VISIBLE);
        } else {
          animationsListener.setMonthPagerVisibility(View.VISIBLE);
          animationsListener.setWeekPagerVisibility(View.GONE);
        }

        animationsListener.setAnimatedContainerVisibility(View.VISIBLE);
        addCellsToAnimateContainer();
        expandedTopMargin = Config.cellHeight * (Utils.getWeekOfMonth(getAnimateContainerDate()) -
            1 + Utils.dayLabelExtraRow()) + extraTopMarginOffset;
        collapsedTopMargin = Config.cellHeight * (Utils.dayLabelExtraRow());
        animationsListener.setTopMarginToAnimContainer((Utils.isMonthView() ? collapsedTopMargin : expandedTopMargin));
      }

      @Override
      public void animationEnd(Animator animation) {
        if (animationsListener == null) return;

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
        if (animationsListener == null) return;

        if (Utils.isMonthView()) {
          animationsListener.setWeekPagerAlpha((float) value);
        } else {
          animationsListener.setMonthPagerAlpha((float) value);
        }
      }
    });
  }

  private void startShowPagerAnimation() {
    increasingAlphaAnimation.setListener(new SmallAnimationListener() {
      @Override
      public void animationStart(Animator animation) {
        if (animationsListener == null) return;

        if (Utils.isMonthView()) {
          animationsListener.setMonthPagerVisibility(View.VISIBLE);
          animationsListener.setWeekPagerVisibility(View.GONE);
        } else {
          animationsListener.setMonthPagerVisibility(View.GONE);
          animationsListener.setWeekPagerVisibility(View.VISIBLE);
        }

        if (Utils.isMonthView()) {
          if (Utils.isTheSameWeekToScrollDate(Config.selectionDate)) {
            Config.scrollDate = Config.selectionDate;
          }
        } else {
          if (Config.SCROLL_TO_SELECTED_AFTER_COLLAPSE && Utils.isTheSameMonthToScrollDate(Config.selectionDate)) {
            Config.scrollDate = Config.selectionDate;
          } else {
            Config.scrollDate = Config.scrollDate.withDayOfMonth(1);
          }
        }

        if (Utils.isMonthView()) {
          animationsListener.scrollToDate(Config.scrollDate, true, false, false);
          animationsListener.setHeightToCenterContainer(Config.monthViewPagerHeight);
          animationsListener.changeViewPager(Config.ViewPagerType.MONTH);
        } else {
          animationsListener.scrollToDate(Config.scrollDate, false, true, false);
          animationsListener.setHeightToCenterContainer(Config.weekViewPagerHeight);
          animationsListener.changeViewPager(Config.ViewPagerType.WEEK);
        }
        animationsListener.updateMarks();
      }

      @Override
      public void animationEnd(Animator animation) {
        clearAnimationsListener();
        if (animationsListener == null) return;
        animationsListener.setAnimatedContainerVisibility(View.GONE);
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
        if (animationsListener == null) return;
        if (Utils.isMonthView()) {
          animationsListener.setMonthPagerAlpha((float) value);
        } else {
          animationsListener.setWeekPagerAlpha((float) value);
        }
      }
    });
  }

  private void startDecreaseSizeAnimation() {
    decreasingSizeAnimation.setListener(new SmallAnimationListener() {
      @Override
      public void animationStart(Animator animation) {
        if (animationsListener == null) return;
        animationsListener.setHeightToCenterContainer(Config.monthViewPagerHeight);
      }

      @Override
      public void animationEnd(Animator animation) {
        if (animationsListener == null) return;
        animationsListener.setHeightToCenterContainer(Config.weekViewPagerHeight);
        clearAnimationsListener();
        startShowPagerAnimation();
      }

      @Override
      public void animationUpdate(Object value) {
        if (animationsListener == null) return;
        animationsListener.setHeightToCenterContainer(getAnimationCenterContainerHeight((float) value));
        animationsListener.setTopMarginToAnimContainer(
            (int) ((expandedTopMargin - collapsedTopMargin) * (float) value) + collapsedTopMargin);
      }
    });
  }

  private void startIncreaseSizeAnimation() {
    increasingSizeAnimation.setListener(new SmallAnimationListener() {
      @Override
      public void animationStart(Animator animation) {
        if (animationsListener == null) return;
        animationsListener.setHeightToCenterContainer(Config.weekViewPagerHeight);
      }

      @Override
      public void animationEnd(Animator animation) {
        if (animationsListener == null) return;
        animationsListener.setHeightToCenterContainer(Config.monthViewPagerHeight);
        clearAnimationsListener();
        startShowPagerAnimation();
      }

      @Override
      public void animationUpdate(Object value) {
        if (animationsListener == null) return;
        animationsListener.setHeightToCenterContainer(getAnimationCenterContainerHeight((float) value));
        animationsListener.setTopMarginToAnimContainer(
            (int) ((expandedTopMargin - collapsedTopMargin) * (float) value) + collapsedTopMargin);
      }
    });
  }

  private int getAnimationCenterContainerHeight(float value) {
    return (int) ((((Config.monthViewPagerHeight - Config.weekViewPagerHeight) * value)) + Config.weekViewPagerHeight);
  }

  public void clearAnimationsListener() {
    decreasingAlphaAnimation.removeAllListeners();
    increasingAlphaAnimation.removeAllListeners();
    decreasingSizeAnimation.removeAllListeners();
    increasingSizeAnimation.removeAllListeners();
  }

  private DateTime getAnimateContainerDate() {
    if (!Utils.isMonthView()) {
      if (Utils.isTheSameMonthToScrollDate(Config.selectionDate)) {
        return Config.selectionDate;
      } else {
        return Config.scrollDate;
      }
    } else {
      if (Utils.isTheSameWeekToScrollDate(Config.selectionDate)) {
        return Config.selectionDate;
      } else {
        return Config.scrollDate;
      }
    }
  }

  private void addCellsToAnimateContainer() {
    animationsListener.animateContainerRemoveViews();

    DateTime animateInitDate = getAnimateContainerDate().minusDays(Utils.firstDayOffset()).withDayOfWeek(1);

    for (int d = 0; d < 7; d++) {
      DateTime cellDate = animateInitDate.plusDays(d + Utils.firstDayOffset());

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

    void setTopMarginToAnimContainer(int margin);

    void setWeekPagerVisibility(int visibility);

    void setMonthPagerVisibility(int visibility);

    void setAnimatedContainerVisibility(int visibility);

    void setMonthPagerAlpha(float alpha);

    void setWeekPagerAlpha(float alpha);

    void scrollToDate(DateTime dateTime, boolean scrollMonthPager, boolean scrollWeekPager, boolean animate);

    void animateContainerAddView(View view);

    void animateContainerRemoveViews();

    void updateMarks();

    void changeViewPager(Config.ViewPagerType viewPagerType);
  }

}
