package sunnysoft.presentapp.Interfaz;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;

import sunnysoft.presentapp.R;

public class CreareventoActivity extends AppCompatActivity implements View.OnClickListener {

     EditText fechacomienzo;
     EditText fechafin;
     String selectedDate;
     String selectedDatefin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crearevento);

        //TextView

        fechacomienzo = (EditText) findViewById(R.id.fechacomienzo);
        fechacomienzo.setOnClickListener(this);

        fechafin = (EditText) findViewById(R.id.fechafin);
        fechafin.setOnClickListener(this);

        //Tooblar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView toolbar_title = (TextView)toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbar_title.setText("CALENDARIO");
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //Tooblar
        Toolbar toolbarfecha = (Toolbar) findViewById(R.id.toolbarfecha);
        TextView toolbar_titlefecha = (TextView)toolbarfecha.findViewById(R.id.toolbar_titlefecha);
        setSupportActionBar(toolbarfecha);
        toolbar_titlefecha.setText("Crear evento");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fechacomienzo:
                showDatePickerDialog();
                break;
            case R.id.fechafin:
                showDatePickerDialog_fin();
                break;
        }

    }

    private void showDatePickerDialog() {
        FragmentManager fm = getFragmentManager();

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                selectedDate = twoDigits(day) + "-" + twoDigits(month+1) + "-" + twoDigits(year);
                //fechacomienzo.setText(selectedDate);
                showTimePickerDialog_time();
            }
        });
        newFragment.show(fm, "datePicker");


    }

    private void showDatePickerDialog_fin() {
        FragmentManager fm = getFragmentManager();

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                selectedDatefin = twoDigits(day) + "-" + twoDigits(month+1) + "-" + twoDigits(year);
                //fechafin.setText(selectedDate);
                showTimePickerDialog_timefin();
            }
        });
        newFragment.show(fm, "datePicker");

    }

    private void showTimePickerDialog_time() {
        FragmentManager fm = getFragmentManager();

        TimePickerFragment newFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                fechacomienzo.setText(selectedDate + " " + String.valueOf(twoDigits(hourOfDay)) + " : " + String.valueOf(twoDigits(minute)));
            }
        });
        newFragment.show(fm, "datePicker");

    }


    private void showTimePickerDialog_timefin() {
        FragmentManager fm = getFragmentManager();

        TimePickerFragment newFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
                fechafin.setText(selectedDatefin + " " + String.valueOf(twoDigits(hourOfDay)) + " : " + String.valueOf(twoDigits(minute)));
            }
        });
        newFragment.show(fm, "datePicker");

    }


}
