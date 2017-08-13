package ru.squel.ipotekacalc.data;

/**
 * Created by sq on 30.07.2017.
 * информация о платеже за текущий месяц
 */
public class MonthlyData {

    private double monthlyPay;

    /// сумма приведенной страховки
    private double monthlyEnsurance = 0;

    /// доля месячного платежа - на проценты
    private double monthlyPercent = 0;
    /// доля месячного платежа - на кредит
    private double monthlyDebt = 0;

    /// остаток долга на текущий месяц
    private double currentDebtSize = 0;
    /// сумма переплаты на текущий месяц
    private double currentPercentSize = 0;

    /// общая сумма кредита
    private static double totalDebtSize = 0;
    /// общая сумма переплаты - одна у всех
    private static double totalPercentSize = 0;
    /// количество месяцев
    private static int totalMonthes = 0;

    public MonthlyData(double monthPay, double percentPay, double debtPay,
                       double currentDebtSize, double currentPercentSize, double monthlyEnsurance) {

        this.monthlyPay = monthPay;
        this.monthlyPercent = percentPay;
        this.monthlyDebt = debtPay;

        this.currentDebtSize = currentDebtSize;
        this.currentPercentSize = currentPercentSize;
        this.monthlyEnsurance = monthlyEnsurance;
    }

    /**
     * Установка постоянных величин
     * @param tDebtSize
     * @param tPercentSize
     */
    public static void MonthlyDataSetStaticValues(double tDebtSize, double tPercentSize, int tTotalMonthes) {
        totalDebtSize = tDebtSize;
        totalPercentSize = tPercentSize;
        totalMonthes = tTotalMonthes;
    }

    public static int getTotalMonthes() {return totalMonthes;}

    public String getMonthlyPay() {return String.format("%.0f", monthlyPay);}
    public double getDoubleMonthlyPay() {return monthlyPay;}

    public String getMonthlyPercent() {return String.format("%.0f", monthlyPercent);}
    public double getDoubleMonthlyPercent() {return monthlyPercent;}

    public String getMonthlyDebt() {
        return String.format("%.0f", monthlyDebt);
    }
    public double getDoubleMonthlyDebt() {
        return monthlyDebt;
    }

    public String getCurrentPercentSize() {
        return String.format("%.0f", currentPercentSize);
    }

    public String getCurrentDebtSize() {
        return String.format("%.0f", currentDebtSize);
    }

    public String getTotalPercentSize() {
        return String.format("%.0f", totalPercentSize);
    }

    public String getTotalDebtSize() {
        return String.format("%.0f", totalDebtSize);
    }

    public String getEnsurance() {
        return String.format("%.0f", monthlyEnsurance);
    }

}
