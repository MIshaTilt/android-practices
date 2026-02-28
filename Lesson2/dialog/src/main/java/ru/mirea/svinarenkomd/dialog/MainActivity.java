package ru.mirea.svinarenkomd.dialog;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_show_dialog).setOnClickListener(v -> {
            MyDialogFragment dialogFragment = new MyDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "mirea");
        });

        findViewById(R.id.button_time_picker).setOnClickListener(v -> {
            MyTimeDialogFragment timePickerDialog = new MyTimeDialogFragment();
            timePickerDialog.show(getSupportFragmentManager(), "timePicker");
        });

        findViewById(R.id.button_date_picker).setOnClickListener(v -> {
            MyDateDialogFragment datePickerDialog = new MyDateDialogFragment();
            datePickerDialog.show(getSupportFragmentManager(), "datePicker");
        });

        findViewById(R.id.button_progress_dialog).setOnClickListener(v -> {
            MyProgressDialogFragment progressDialogFragment = new MyProgressDialogFragment();
            progressDialogFragment.show(getSupportFragmentManager(), "progressDialog");
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onOkClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Иду дальше\"!", Toast.LENGTH_LONG).show();
    }

    public void onCancelClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Нет\"!", Toast.LENGTH_LONG).show();
    }

    public void onNeutralClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"На паузе\"!", Toast.LENGTH_LONG).show();
    }

    public void onDateSet() {
        Snackbar.make(findViewById(R.id.main), "Дата выбрана", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Toast.makeText(getApplicationContext(), "Выбранное время: " + hourOfDay + ":" + minute, Toast.LENGTH_LONG).show();
    }
}