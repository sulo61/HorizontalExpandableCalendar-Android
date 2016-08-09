package com.mikesu.horizontalexpcalendar.view.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.mikesu.horizontalexpcalendar.common.Config;

/**
 * Created by MikeSu on 08.08.2016.
 * www.michalsulek.pl
 */

public abstract class BaseCellView extends FrameLayout {

  protected DayType dayType;

  public BaseCellView(Context context) {
    super(context);
  }

  public BaseCellView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public BaseCellView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }


  protected void setTextBackgroundByDayType() {
    if (this.dayType == DayType.WEEKEND) {
      setBackgroundColor(Config.CELL_WEEKEND_BACKGROUND);
    } else {
      setBackgroundColor(Config.CELL_NON_WEEKEND_BACKGROUND);
    }
  }

  public enum DayType {
    WEEKEND,
    NO_WEEKEND
  }

  public enum TimeType {
    PAST,
    CURRENT,
    FUTURE
  }
}
