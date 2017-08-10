package ru.squel.ipotekacalc.presenter;

import java.util.ArrayList;

import ru.squel.ipotekacalc.ViewPresenterContract;
import ru.squel.ipotekacalc.data.DataModel;
import ru.squel.ipotekacalc.data.MonthlyData;

/**
 * Created by sq on 30.07.2017.
 */
public class MainActivityPresenter implements ViewPresenterContract.PresenterInterface {

    /// вьюха для отображения
    private ViewPresenterContract.ViewInterface view;

    private DataModel dataModel = new DataModel();

    public MainActivityPresenter(ViewPresenterContract.ViewInterface view) {
        this.view = view;
    }

    /**
     * Обновление данных,
      */

    private void updateData() {
        dataModel.recalculateAll();
    }

    @Override
    public ArrayList<MonthlyData> getMonthlyData() {
        return dataModel.getListData();
    }

    @Override
    public void saveButtonHandler(DataModel newData) {
        this.dataModel = newData;
        view.saveToPreferences(dataModel);

        updateData();
    }

    @Override
    public void calcButtonHandler(DataModel newData) {
        this.dataModel = newData;

        updateData();
    }

    public void displaySavedData(DataModel dm) {
        this.dataModel = dm;

        updateData();
    }
}
