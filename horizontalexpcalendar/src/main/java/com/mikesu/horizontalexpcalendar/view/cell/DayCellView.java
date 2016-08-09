package com.mikesu.horizontalexpcalendar.view.cell;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.mikesu.horizontalexpcalendar.R;
import com.mikesu.horizontalexpcalendar.model.MarkSetup;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class DayCellView extends BaseCellView {

  private TextView text;
  private TimeType timeType;
  private FrameLayout markContainer;
  private View markToday;
  private View markSelected;
  private MarkSetup markSetup;
  private long markTimestamp;

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
      text.setTextColor(Color.GRAY);
    }
  }

  public MarkSetup getMarkSetup() {
    return markSetup;
  }

  public void setMark(MarkSetup markSetup, int size) {
    LayoutParams markParams = (LayoutParams) markContainer.getLayoutParams();
    markParams.height = size;
    markParams.width = size;
    setMarkSetup(markSetup);
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
    }
  }

  public long getMarkTimestamp() {
    return markTimestamp;
  }

}
