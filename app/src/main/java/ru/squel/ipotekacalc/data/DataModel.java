package ru.squel.ipotekacalc.data;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by sq on 30.07.2017.
 * Обслуживание данных
 */
public class DataModel {

    public static final String LOG_TAG = DataModel.class.getSimpleName();

    private double summDebt;
    private double percentDebt;
    private int periodDebt;

    private double monthPay;

    /// информация для отображения в списке графика платежей
    private ArrayList<MonthlyData> dataByMonth = new ArrayList<>();

    public void setDataModel(double summDebt, double percentDebt, int periodDebt) {
        this.percentDebt = percentDebt;
        this.periodDebt = periodDebt;
        this.summDebt = summDebt;
    }



    /**
     * Обнволение данных
     */
    public void recalculateAll() {

        /// очистка предыдущих данных
        this.dataByMonth.clear();

        /// расчеты
        double monthPercent = (percentDebt / 12) / 100.0;
        monthPay = summDebt * (monthPercent + (monthPercent)/(Math.pow(1 + monthPercent, periodDebt) - 1));
        double ostatokDebt = summDebt;
        double percenFull = 0;

        /// заполнение помесячных данных
        for (int i = 0; i < periodDebt; i++) {
            double percentPay = ostatokDebt * (percentDebt / 100.0) / 12;
            double debtPay = (monthPay - percentPay);
            ostatokDebt = ostatokDebt - debtPay;
            percenFull += percentPay;

            MonthlyData monthlyData = new MonthlyData(monthPay, percentPay, debtPay, ostatokDebt, percenFull);
            this.dataByMonth.add(monthlyData);
        }

        /// устновка общей суммы платежа и общей суммы процентов
        MonthlyData.MonthlyDataSetStaticValues(this.summDebt, percenFull, this.periodDebt);

        Log.d(LOG_TAG, "recalculate done:" + dataByMonth.size());
    }

    public ArrayList<MonthlyData> getListData() {
        return dataByMonth;
    }

}
