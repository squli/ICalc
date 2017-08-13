package ru.squel.ipotekacalc.graph;

import ru.squel.ipotekacalc.data.MonthlyData;

/**
 * Created by sq on 11.08.2017.
 */
public interface OnPlotClickCallback {

    void onPlotCallback(MonthlyData clickedMonth, int monthImdex);
}
