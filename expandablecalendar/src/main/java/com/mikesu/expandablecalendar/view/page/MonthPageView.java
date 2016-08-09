package com.mikesu.expandablecalendar.view.page;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import com.mikesu.expandablecalendar.R;
import com.mikesu.expandablecalendar.common.Config;
import com.mikesu.expandablecalendar.common.Constants;
import com.mikesu.expandablecalendar.common.Marks;
import com.mikesu.expandablecalendar.common.Utils;
import com.mikesu.expandablecalendar.view.cell.CellBaseView;
import com.mikesu.expandablecalendar.view.cell.DayCellView;
import com.mikesu.expandablecalendar.view.cell.LabelCellView;
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
    initViews();
  }

  private void initViews() {
    inflate(getContext(), R.layout.month_page_view, this);
    gridLayout = (GridLayout) findViewById(R.id.grid_layout);
    gridLayout.setColumnCount(Config.COLUMNS);
    gridLayout.setRowCount(Config.MONTH_ROWS + (Utils.dayLabelExtraRow()));
  }

  public void setup(DateTime pageDate) {
    this.pageDate = pageDate;
    addCellsToGrid();
  }

  private void addCellsToGrid() {
    DateTime cellDate = pageDate.plusDays(-pageDate.getDayOfWeek() + 1);
    if (Config.USE_DAY_LABELS) {
      for (int l = 0; l < Config.COLUMNS; l++) {
        LabelCellView label = new LabelCellView(getContext());

        GridLayout.LayoutParams labelParams = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(l));
        labelParams.height = Config.cellHeight;
        labelParams.width = Config.cellWidth;
        label.setLayoutParams(labelParams);
        label.setText(Constants.NAME_OF_DAYS[l]);
        label.setDayType(Utils.isWeekendByColumnNumber(l) ? CellBaseView.DayType.WEEKEND : CellBaseView.DayType.NO_WEEKEND);

        gridLayout.addView(label);

      }
    }
    for (int r = Utils.dayLabelExtraRow(); r < Config.MONTH_ROWS + (Utils.dayLabelExtraRow()); r++) {
      for (int c = 0; c < Config.COLUMNS; c++) {
        DayCellView dayView = new DayCellView(getContext());

        GridLayout.LayoutParams cellParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));
        cellParams.height = Config.cellHeight;
        cellParams.width = Config.cellWidth;
        dayView.setTag(cellDate);
        dayView.setLayoutParams(cellParams);
        dayView.setDayNumber(cellDate.getDayOfMonth());
        dayView.setTimeType(getTimeType(cellDate));
        dayView.setDayType(Utils.isWeekendByColumnNumber(c) ? CellBaseView.DayType.WEEKEND : CellBaseView.DayType.NO_WEEKEND);
        dayView.setMark(Marks.getMark(cellDate), Config.cellHeight);

        gridLayout.addView(dayView);

        cellDate = cellDate.plusDays(1);
      }
    }
  }

  private DayCellView.TimeType getTimeType(DateTime cellTime) {
    if (cellTime.getMonthOfYear() < pageDate.getMonthOfYear()) {
      return DayCellView.TimeType.PAST;
    } else if (cellTime.getMonthOfYear() > pageDate.getMonthOfYear()) {
      return DayCellView.TimeType.FUTURE;
    } else {
      return DayCellView.TimeType.CURRENT;
    }
  }

  public void updateMarks() {
    for (int c = Utils.dayLabelExtraChildCount(); c < gridLayout.getChildCount(); c++) {
      DayCellView dayCellView = (DayCellView) gridLayout.getChildAt(c);
      dayCellView.setMarkSetup(Marks.getMark((DateTime) dayCellView.getTag()));
    }
  }
}
