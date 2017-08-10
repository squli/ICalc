package ru.squel.ipotekacalc;

import java.util.ArrayList;

import ru.squel.ipotekacalc.data.DataModel;
import ru.squel.ipotekacalc.data.MonthlyData;

/**
 * Created by sq on 30.07.2017.
 */
public interface ViewPresenterContract {

    interface ViewInterface {

        void saveToPreferences(DataModel dm);
    }

    interface PresenterInterface {

        ArrayList<MonthlyData> getMonthlyData();

        void saveButtonHandler(DataModel dm);
        void calcButtonHandler(DataModel dm);

        void displaySavedData(DataModel dm);

    }
}
