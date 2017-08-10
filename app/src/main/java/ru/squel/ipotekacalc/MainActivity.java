package ru.squel.ipotekacalc;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import layout.ConfigFragment;
import ru.squel.ipotekacalc.data.DataModel;
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

    /// это будет именем файла настроек
    public static final String APP_PREFERENCES = "myCalcPreferences";
    /// названия параметров для сохранения настроек
    public static final String APP_PREFERENCES_VALIDFLAG = "pref_valid_flag";
    public static final String APP_PREFERENCES_TOTAL_DEBT = "pref_total_debt";
    public static final String APP_PREFERENCES_PERCENT = "pref_percent";
    public static final String APP_PREFERENCES_LONG = "pref_long";
    public static final String APP_PREFERENCES_DATEBEGIN = "pref_begin_date";
    public static final String APP_PREFERENCES_ENSURANCE = "pref_ensurance";
    /// настройки для приложения
    SharedPreferences mySharedPreferences;

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

        mySharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        /// если настроек не было, то показать фрагмент с настройками в основном окне
        /// если были, то отобразить фрагмент с приветствием
        if(mySharedPreferences.contains(APP_PREFERENCES_VALIDFLAG) && mySharedPreferences.getBoolean(APP_PREFERENCES_VALIDFLAG, false) == true) {
                presenter.displaySavedData(loadDataFromSharedPreferences());
        }
        else {
            showConfFragment(null);
        }
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
        if (id == R.id.action_config) {
            // если есть сохраненные значения, то отобразить их, если нет то показать пустые
            showConfFragment(loadDataFromSharedPreferences());
            return true;
        }
        if (id == R.id.action_plot) {
            showGraphFragment(presenter.getMonthlyData());
            return true;
        }
        if (id == R.id.action_graph) {
            showListFragment(presenter.getMonthlyData());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveToPreferences(DataModel dm) {

        SharedPreferences.Editor e = mySharedPreferences.edit();

        e.putBoolean(APP_PREFERENCES_VALIDFLAG, true);
        e.putLong(APP_PREFERENCES_TOTAL_DEBT, dm.getSummDebt());
        e.putFloat(APP_PREFERENCES_PERCENT, dm.getPercentDebt());
        e.putInt(APP_PREFERENCES_LONG, dm.getPeriodDebt());

        e.commit(); // не забудьте подтвердить изменения

    }

    /**
     * Отображение фрагмента с настройками
     */
    public void showConfFragment(DataModel dataModel) {
        // настроек не было и будет отображаться фрагмент настроек:
        ConfigFragment configFragment = new ConfigFragment();
        configFragment.setMainActivityPresenter(presenter);
        if (dataModel != null) {
            configFragment.setDisplayedData(dataModel);
        }
        // новая транзакция
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // замена фрейма на созданный фрагмент
        transaction.replace(R.id.fragment_container, configFragment);
        // запуск транзакции без названия
        transaction.addToBackStack(null);
        // настройка анимации
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // отправка фрагмента на исполнение
        transaction.commit();
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


    private DataModel loadDataFromSharedPreferences() {
        DataModel dm = new DataModel();
        // значит были сохранены настройки и будет отображаться окно приветствия
        if(mySharedPreferences.contains(APP_PREFERENCES_TOTAL_DEBT) &&
                mySharedPreferences.contains(APP_PREFERENCES_TOTAL_DEBT) &&
                mySharedPreferences.contains(APP_PREFERENCES_LONG)) {
            long debtSize = mySharedPreferences.getLong(APP_PREFERENCES_TOTAL_DEBT, 0);
            float percentSize = mySharedPreferences.getFloat(APP_PREFERENCES_PERCENT, 0);
            int periodSize = mySharedPreferences.getInt(APP_PREFERENCES_LONG, 0);

            dm.setDataModel(debtSize, percentSize, periodSize);
        }
        return  dm;
    }

}
