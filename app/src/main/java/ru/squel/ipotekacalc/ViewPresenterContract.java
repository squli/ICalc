package ru.squel.ipotekacalc;

import java.util.ArrayList;

import ru.squel.ipotekacalc.data.MonthlyData;

/**
 * Created by sq on 30.07.2017.
 */
public interface ViewPresenterContract {

    interface ViewInterface {

        void displayNewGraph();

    }

    interface PresenterInterface {

        void updateData(double debtSize, double percent, int period);
        ArrayList<MonthlyData> getMonthlyData();

    }
}
