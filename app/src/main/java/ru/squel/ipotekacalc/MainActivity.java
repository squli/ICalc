package ru.squel.ipotekacalc;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import ru.squel.ipotekacalc.data.MonthlyData;
import ru.squel.ipotekacalc.graph.GraphFragment;
import ru.squel.ipotekacalc.list.ItemFragment;
import ru.squel.ipotekacalc.presenter.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements ViewPresenterContract.ViewInterface{

    /**
     * TODO Два макета - горизонтальный, оба фрагмента рядом
     * TODO двигающийся ползунок по графику
     * TODO примерная стоимость страховки + к расчетам
     */

    /// Презентер этой вьюхи
    private ViewPresenterContract.PresenterInterface presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /// подключить презентера
        if (presenter == null) {
            presenter = new MainActivityPresenter(this);
        }

        final EditText textViewDebtSize = (EditText) findViewById(R.id.TotalDebtSizeEditText);
        final EditText textViewPercent = (EditText) findViewById(R.id.percentEditText);
        final EditText textViewPeriod = (EditText) findViewById(R.id.longEditText);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Update", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                double cDebt = 0;
                double cPercent = 0;
                int cPeriod = 0;

                try {
                    cDebt = Double.parseDouble(textViewDebtSize.getText().toString());
                    cPercent = Double.parseDouble(textViewPercent.getText().toString());
                    cPeriod = Integer.parseInt(textViewPeriod.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                presenter.updateData(cDebt, cPercent, cPeriod);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_plot) {
            showGraphFragment(presenter.getMonthlyData());
            //displayNewGraph();
            return true;
        }
        if (id == R.id.action_graph) {
            showListFragment(presenter.getMonthlyData());
            //displayNewDebtList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Отобразить новый график
     */
    @Override
    public void displayNewGraph() {

    }


    /**
     * Отобразить фрагмент со списком графика платежей
     */
    public void displayNewDebtList() {

    }

    /**
     * Отображение фрагмента с графиком
     */
    public void showGraphFragment(ArrayList<MonthlyData> md) {
        GraphFragment graphFragment = new GraphFragment();
        //установил данные для отображения
        graphFragment.setData(md);
        // новая транзакция
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // замена фрейма на созданный фрагмент
        transaction.replace(R.id.fragment_container, graphFragment);
        // запуск транзакции без названия
        transaction.addToBackStack(null);
        // настройка анимации
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // отправка фрагмента на исполнение
        transaction.commit();
    }

    /**
     * Отображение фрагмента со списком
     */
    public void showListFragment(ArrayList<MonthlyData> md) {
        ItemFragment listFragment = new ItemFragment();
        listFragment.setContext(this);
        //установил данные для отображения
        listFragment.setData(md);
        // новая транзакция
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // замена фрейма на созданный фрагмент
        transaction.replace(R.id.fragment_container, listFragment);
        // запуск транзакции без названия
        transaction.addToBackStack(null);
        // настройка анимации
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // отправка фрагмента на исполнение
        transaction.commit();
    }

}
