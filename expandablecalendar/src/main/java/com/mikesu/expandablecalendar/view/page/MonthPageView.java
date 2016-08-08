package com.mikesu.expandablecalendar.view.page;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import com.mikesu.expandablecalendar.Config;
import com.mikesu.expandablecalendar.R;
import com.mikesu.expandablecalendar.view.cell.CellView;
import org.joda.time.DateTime;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class MonthPageView extends FrameLayout {

  private GridLayout gridLayout;
  private DateTime pageDate;

  public MonthPageView(Context context) {
    super(context);
    init();
  }

  public MonthPageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MonthPageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    initVariables();
    initViews();
    setSizeToCells();
  }

  private void initVariables() {
  }

  private void initViews() {
    inflate(getContext(), R.layout.month_page_view, this);
    gridLayout = (GridLayout) findViewById(R.id.grid_layout);
  }

  public void setup(DateTime pageDate) {
    this.pageDate = pageDate;
    addCellsToGrid();
    setSizeToCells();
  }

  private void setSizeToCells() {
    for (int i = 0; i < gridLayout.getChildCount(); i++) {
      CellView cellView = (CellView) gridLayout.getChildAt(i);
      GridLayout.LayoutParams gridParams = (GridLayout.LayoutParams) cellView.getLayoutParams();
      gridParams.height = Config.cellHeight;
      gridParams.width = Config.cellWidth;
    }
  }

  private void addCellsToGrid() {
    DateTime cellDate = pageDate.plusDays(-pageDate.getDayOfWeek() + 1);
    for (int r = 0; r < Config.MONTH_ROWS; r++) {
      for (int c = 0; c < Config.COLUMNS; c++) {
        CellView cellView = new CellView(getContext());

        GridLayout.LayoutParams cellParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));
        cellView.setLayoutParams(cellParams);
        cellView.setText(String.valueOf(cellDate.getDayOfMonth()));
        cellView.setTimeType(getTimeType(cellDate));

        gridLayout.addView(cellView);

        cellDate = cellDate.plusDays(1);
      }
    }
  }

  private CellView.TimeType getTimeType(DateTime cellTime) {
    if (cellTime.getMonthOfYear() < pageDate.getMonthOfYear()) {
      return CellView.TimeType.PAST;
    } else if (cellTime.getMonthOfYear() > pageDate.getMonthOfYear()) {
      return CellView.TimeType.FUTURE;
    } else {
      return CellView.TimeType.CURRENT;
    }
  }
}
