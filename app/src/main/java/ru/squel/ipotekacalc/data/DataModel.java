package ru.squel.ipotekacalc.data;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sq on 30.07.2017.
 * Обслуживание данных
 */
public class DataModel {

    public static final String LOG_TAG = DataModel.class.getSimpleName();

    private long summDebt;
    private float percentDebt;
    private int periodDebt;
    private double monthPay;
    private Date beginDate;
    private boolean ensuranceFlag = false;

    public boolean dataOK = false;

    /// информация для отображения в списке графика платежей
    private ArrayList<MonthlyData> dataByMonth = new ArrayList<>();

    public void setDataModel(long summDebt, float percentDebt, int periodDebt) {
        this.percentDebt = percentDebt;
        this.periodDebt = periodDebt;
        this.summDebt = summDebt;

        this.dataOK = true;
    }

    public long getSummDebt() {return summDebt;}
    public float getPercentDebt() {return percentDebt;}
    public int getPeriodDebt() {return periodDebt;}

    public void setEnsuranceFlag(boolean flag) {
        this.ensuranceFlag = flag;
    }

    public void setBeginDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM.yyyy");
        try {
            beginDate = formatter.parse(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
            Calendar c = Calendar.getInstance();
            beginDate = c.getTime();
        }
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
        double ensurancePerYear = 0;

        /// заполнение помесячных данных
        for (int i = 0; i < periodDebt; i++) {
            double percentPay = ostatokDebt * (percentDebt / 100.0) / 12;
            double debtPay = (monthPay - percentPay);
            ostatokDebt = ostatokDebt - debtPay;
            percenFull += percentPay;

            if (ensuranceFlag && (i%12 == 0)) {
                ensurancePerYear = ostatokDebt * 0.01;
            }

            MonthlyData monthlyData = new MonthlyData(monthPay, percentPay, debtPay, ostatokDebt, percenFull, ensurancePerYear/12);
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
