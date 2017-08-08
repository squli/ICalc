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
    @Override
    public void updateData(double debtSize, double percent, int period) {

        dataModel.setDataModel(debtSize, percent, period);
        dataModel.recalculateAll();

        // обновить график на фрагменте
        //dataModel.getGraphData();
        view.displayNewGraph();
    }

    @Override
    public ArrayList<MonthlyData> getMonthlyData() {
        return dataModel.getListData();
    }
}
