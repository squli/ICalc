package layout;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import ru.squel.ipotekacalc.R;
import ru.squel.ipotekacalc.ViewPresenterContract;
import ru.squel.ipotekacalc.data.DataModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private ViewPresenterContract.PresenterInterface mainActivityPresenter;
    private static final String LOG_TAG = "ConfigFragment";

    //ODO Разлелить DataModel на два класса - для хранения отображаемых параметров и для всего остального
    /// хранилище данных для отображения на экране
    private DataModel dataModelToDisplay = null;

    EditText textViewDebtSize;
    EditText textViewPercent;
    EditText textViewPeriod;
    EditText textBeginDate;
    CheckBox ensuranceCheckbox;

    public ConfigFragment() {
        // Required empty public constructor
    }

    /**
     * Устновить презентера основного окна
     * @param presenter
     */
    public void setMainActivityPresenter(ViewPresenterContract.PresenterInterface presenter) {
        this.mainActivityPresenter = presenter;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        Button saveButton = (Button) getView().findViewById(R.id.saveButton);
        Button calcButton = (Button) getView().findViewById(R.id.calculteSingle);

        textViewDebtSize = (EditText) getView().findViewById(R.id.TotalDebtSizeEditText);
        textViewPercent = (EditText) getView().findViewById(R.id.percentEditText);
        textViewPeriod = (EditText) getView().findViewById(R.id.longEditText);
        textBeginDate = (EditText) getView().findViewById(R.id.dateBeginEditText);
        ensuranceCheckbox = (CheckBox) getView().findViewById(R.id.ensuranceChackbox);

        if (dataModelToDisplay != null) {
            textViewDebtSize.setText(String.valueOf(dataModelToDisplay.getSummDebt()));
            textViewPercent.setText(String.valueOf(dataModelToDisplay.getPercentDebt()));
            textViewPeriod.setText(String.valueOf(dataModelToDisplay.getPeriodDebt()));

            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("MMM.yyyy");
            String formattedDate = df.format(c.getTime());
            textBeginDate.setText(formattedDate);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivityPresenter != null) {

                    DataModel newRawData = formDataModelFromInputs();

                    if (newRawData.dataOK == true)
                        mainActivityPresenter.saveButtonHandler(newRawData);
                    else {
                        Toast toast = Toast.makeText(getContext(),
                                "Вы ввели что-то не то",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }
        });

        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivityPresenter != null) {

                    DataModel newRawData = formDataModelFromInputs();

                    if (newRawData.dataOK == true)
                        mainActivityPresenter.calcButtonHandler(newRawData);
                    else {
                        Toast toast = Toast.makeText(getContext(),
                                "Вы ввели что-то не то",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            }
        });

        textBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar;
                calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getActivity(), ConfigFragment.this, yy, mm, dd).show();
            }
        });
    }

    private DataModel formDataModelFromInputs() {
        long cDebt = 0;
        float cPercent = 0;
        int cPeriod = 0;
        String dateString;
        final DataModel newRawData = new DataModel();

        try {
            cDebt = Long.parseLong(textViewDebtSize.getText().toString());
            cPercent = Float.parseFloat(textViewPercent.getText().toString());
            cPeriod = Integer.parseInt(textViewPeriod.getText().toString());
            dateString = textBeginDate.getText().toString();
            newRawData.setDataModel(cDebt, cPercent, cPeriod);
            newRawData.setBeginDate(dateString);
            newRawData.setEnsuranceFlag(ensuranceCheckbox.isChecked());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newRawData;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setDisplayedData(DataModel dm) {
        dataModelToDisplay = dm;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d(LOG_TAG, "onDateSet " + year + " " + monthOfYear + " " + dayOfMonth);
        if (dataModelToDisplay != null) {
            textBeginDate.setText(monthOfYear + "." + year);
        }
    }

}
