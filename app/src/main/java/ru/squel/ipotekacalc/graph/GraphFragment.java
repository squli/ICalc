package ru.squel.ipotekacalc.graph;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.squel.ipotekacalc.R;
import ru.squel.ipotekacalc.data.MonthlyData;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment implements OnPlotClickCallback {

    /// данные для построителя графика
    private ArrayList<MonthlyData> dataByMonth;

    /// вьюха построителя графиков
    GraphPlotter plotView = null;

    TextView textMonthlyPay;
    TextView textMonthlyDebt;
    TextView textCurrentMonth;
    TextView textMonthlyPercent;
    TextView textMonthlyEnsurance;

    public GraphFragment() {
        // Required empty public constructor
    }

    /**
     * Установка точек для построения графика
     * @param dataByMonth
     */
    public void setData(ArrayList<MonthlyData>  dataByMonth) {
        this.dataByMonth = dataByMonth;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        plotView = (GraphPlotter) view.findViewById(R.id.GraphPlotter);
        plotView.setData(this.dataByMonth);
        plotView.setOnPlotClickCallback(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        textCurrentMonth = (TextView) getView().findViewById(R.id.grapMonthValue);
        textMonthlyPay = (TextView) getView().findViewById(R.id.graphDescrSumValue);
        textMonthlyDebt = (TextView) getView().findViewById(R.id.graphDescrDebtValue);
        textMonthlyPercent = (TextView) getView().findViewById(R.id.graphPercentDebtValue);
        textMonthlyEnsurance = (TextView) getView().findViewById(R.id.graphEnsuranceValue);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPlotCallback(MonthlyData clickedMonth, int monthImdex) {
        textCurrentMonth.setText(String.valueOf(monthImdex));
        textMonthlyPay.setText(clickedMonth.getMonthlyPay());
        textMonthlyDebt.setText(clickedMonth.getMonthlyDebt());
        textMonthlyPercent.setText(clickedMonth.getMonthlyPercent());
        textMonthlyEnsurance.setText(clickedMonth.getEnsurance());
    }
}
