package com.mikesu.expandablecalendar.view.page;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;
import com.mikesu.expandablecalendar.R;
import com.mikesu.expandablecalendar.adapter.MonthViewPagerAdapter;
import com.mikesu.expandablecalendar.view.cell.MonthCellView;
import org.joda.time.DateTime;

/**
 * Created by MikeSu on 04/08/16.
 * www.michalsulek.pl
 */

public class MonthPageView extends GridLayout {

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
    initViews();

  }

  private void initViews() {
    inflate(getContext(), R.layout.month_page_view, this);
  }

  public void setup(int year, int month) {
    DateTime currentDate = new DateTime().withYear(year).withMonthOfYear(month);
    currentDate = currentDate.plusDays(currentDate.getDayOfWeek());

    fillCellsWithDayNumbers(currentDate);
    addCellsToGrid();
  }

  private void addCellsToGrid() {
    for (int r = 0; r < MonthViewPagerAdapter.ROWS; r++) {
      for (int c = 0; c < MonthViewPagerAdapter.COLUMNS; c++) {
        MonthCellView monthCellView = new MonthCellView(getContext());
        GridLayout.LayoutParams cellParams = (LayoutParams) monthCellView.getLayoutParams();
        cellParams.rowSpec = GridLayout.spec(r);
        cellParams.columnSpec = GridLayout.spec(c);
        monthCellView.setLayoutParams(cellParams);
      }
    }
  }

  private void fillCellsWithDayNumbers(DateTime currentDate) {
    for (MonthCellView monthCellView : monthCellViewList) {
      monthCellView = new MonthCellView(getContext());
      monthCellView.setText(currentDate.getDayOfMonth());
    }
  }
}
