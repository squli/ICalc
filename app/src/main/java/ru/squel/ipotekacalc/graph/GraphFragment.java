package ru.squel.ipotekacalc.graph;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ru.squel.ipotekacalc.R;
import ru.squel.ipotekacalc.data.MonthlyData;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {

    /// данные для построителя графика
    private ArrayList<MonthlyData> dataByMonth;

    /// вьюха построителя графиков
    GraphPlotter plotView = null;


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

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
