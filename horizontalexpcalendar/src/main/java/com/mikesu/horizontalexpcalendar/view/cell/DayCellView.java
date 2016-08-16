package com.mikesu.horizontalexpcalendar.view.cell;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.mikesu.horizontalexpcalendar.R;
import com.mikesu.horizontalexpcalendar.common.Marks;
import com.mikesu.horizontalexpcalendar.model.MarkSetup;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class DayCellView extends BaseCellView {

  private TextView text;
  private TimeType timeType;
  private FrameLayout markContainer;
  
  private MarkSetup markSetup;
  private View markToday;
  private View markSelected;
  private View markCustom1;
  private View markCustom2;

  public DayCellView(Context context) {
    super(context);
    init();
  }

  public DayCellView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public DayCellView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    initView();
  }

  private void initView() {
    inflate(getContext(), R.layout.day_cell_view, this);

    text = (TextView) findViewById(R.id.text);
    markContainer = (FrameLayout) findViewById(R.id.mark_container);
    markToday = findViewById(R.id.mark_today_view);
    markSelected = findViewById(R.id.mark_selected_view);
    markCustom1 = findViewById(R.id.mark_custom1);
    markCustom2 = findViewById(R.id.mark_custom2);
  }

  public void setTimeType(TimeType timeType) {
    this.timeType = timeType;
    setTextColorByTimeType();
  }

  public void setDayNumber(int dayNumber) {
    this.text.setText(String.valueOf(dayNumber));
  }

  public void setDayType(DayType dayType) {
    this.dayType = dayType;
    setTextBackgroundByDayType();
  }


  private void setTextColorByTimeType() {
    if (this.timeType == TimeType.CURRENT) {
      text.setTextColor(Color.BLACK);
    } else {
      text.setTextColor(Color.LTGRAY);
    }
  }

  public void setMark(MarkSetup markSetup, int size) {
    setSize(size);
    setMarkSetup(markSetup);
  }

  private void setSize(int size) {
    LayoutParams markParams = (LayoutParams) markContainer.getLayoutParams();
    markParams.height = size;
    markParams.width = size;

    setupCustom1Mark(size);
    setupCustom2Mark(size);
  }

  private void setupCustom1Mark(int size) {
    LayoutParams markCustomParams = (LayoutParams) markCustom1.getLayoutParams();
    int markCustomPercentSize = (int) (size * Marks.MARK_CUSTOM1_SIZE_PROPORTION_TO_CELL);
    markCustomParams.height = markCustomPercentSize;
    markCustomParams.width = markCustomPercentSize;
  }

  private void setupCustom2Mark(int size) {
    LayoutParams markCustomParams = (LayoutParams) markCustom2.getLayoutParams();
    markCustomParams.height = (int) (size * Marks.MARK_CUSTOM2_HEIGHT_PROPORTION_TO_CELL);
    markCustomParams.width = (int) (size * Marks.MARK_CUSTOM2_WIDTH_PROPORTION_TO_CELL);
  }

  public void setMarkSetup(MarkSetup markSetup) {
    this.markSetup = markSetup;
    setMarkToView();
  }

  private void setMarkToView() {
    if (markSetup == null) {
      markContainer.setVisibility(GONE);
    } else {
      markContainer.setVisibility(VISIBLE);
      markToday.setVisibility(markSetup.isToday() ? VISIBLE : GONE);
      markSelected.setVisibility(markSetup.isSelected() ? VISIBLE : GONE);
      markCustom1.setVisibility(markSetup.isCustom1() ? VISIBLE : GONE);
      markCustom2.setVisibility(markSetup.isCustom2() ? VISIBLE : GONE);
    }
  }
}
