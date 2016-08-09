package com.mikesu.horizontalexpcalendar.view.page;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import com.mikesu.horizontalexpcalendar.R;
import com.mikesu.horizontalexpcalendar.common.Config;
import com.mikesu.horizontalexpcalendar.common.Constants;
import com.mikesu.horizontalexpcalendar.common.Marks;
import com.mikesu.horizontalexpcalendar.common.Utils;
import com.mikesu.horizontalexpcalendar.view.cell.BaseCellView;
import com.mikesu.horizontalexpcalendar.view.cell.DayCellView;
import com.mikesu.horizontalexpcalendar.view.cell.LabelCellView;
import org.joda.time.DateTime;

/**
 * Created by MikeSu on 09.08.2016.
 * www.michalsulek.pl
 */
public class PageView extends FrameLayout {

  private GridLayout gridLayout;
  private DateTime pageDate;

  private Config.ViewPagerType viewPagerType;
  private int rows;
  private int layout;

  public PageView(Context context) {
    this(context, null);
  }

  public PageView(Context context, Config.ViewPagerType viewPagerType) {
    super(context);
    if (viewPagerType != null) {
      this.viewPagerType = viewPagerType;
      this.rows = viewPagerType == Config.ViewPagerType.MONTH ? Config.MONTH_ROWS : Config.WEEK_ROWS;
      this.layout = viewPagerType == Config.ViewPagerType.MONTH ? R.layout.month_page_view : R.layout.week_page_view;
      init();
    }
  }

  private void init() {
    initViews();
  }

  private void initViews() {
    inflate(getContext(), layout, this);
    gridLayout = (GridLayout) findViewById(R.id.grid_layout);
    gridLayout.setColumnCount(Config.COLUMNS);
    gridLayout.setRowCount(rows + (Utils.dayLabelExtraRow()));
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
        label.setDayType(Utils.isWeekendByColumnNumber(l) ? BaseCellView.DayType.WEEKEND : BaseCellView.DayType.NO_WEEKEND);

        gridLayout.addView(label);

      }
    }
    for (int r = Utils.dayLabelExtraRow(); r < rows + (Utils.dayLabelExtraRow()); r++) {
      for (int c = 0; c < Config.COLUMNS; c++) {
        DayCellView dayView = new DayCellView(getContext());

        GridLayout.LayoutParams cellParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));
        cellParams.height = Config.cellHeight;
        cellParams.width = Config.cellWidth;
        dayView.setTag(cellDate);
        dayView.setLayoutParams(cellParams);
        dayView.setDayNumber(cellDate.getDayOfMonth());
        dayView.setDayType(Utils.isWeekendByColumnNumber(c) ? BaseCellView.DayType.WEEKEND : BaseCellView.DayType.NO_WEEKEND);
        dayView.setMark(Marks.getMark(cellDate), Config.cellHeight);

        if (viewPagerType == Config.ViewPagerType.MONTH) {
          dayView.setTimeType(getTimeType(cellDate));
        }

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
