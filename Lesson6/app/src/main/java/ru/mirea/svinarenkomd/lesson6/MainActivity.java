package ru.mirea.svinarenkomd.lesson6;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextGroup;
    private EditText editTextNumber;
    private EditText editTextMovie;
    private Button buttonSave;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextGroup = findViewById(R.id.editTextGroup);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextMovie = findViewById(R.id.editTextMovie);
        buttonSave = findViewById(R.id.buttonSave);

        // Инициализируем SharedPreferences
        sharedPref = getSharedPreferences("mirea_settings", Context.MODE_PRIVATE);

        // При запуске приложения пытаемся загрузить данные, если они уже есть
        loadData();

        // Обработка нажатия на кнопку "Сохранить"
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        SharedPreferences.Editor editor = sharedPref.edit();
        // Сохраняем значения из полей ввода
        editor.putString("GROUP", editTextGroup.getText().toString());
        editor.putString("NUMBER", editTextNumber.getText().toString());
        editor.putString("MOVIE", editTextMovie.getText().toString());

        editor.apply(); // асинхронное сохранение
        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
    }

    private void loadData() {
        // Достаем данные. Второй параметр - значение по умолчанию, если данных нет
        String group = sharedPref.getString("GROUP", "");
        String number = sharedPref.getString("NUMBER", "");
        String movie = sharedPref.getString("MOVIE", "");

        // Вставляем данные в поля ввода
        editTextGroup.setText(group);
        editTextNumber.setText(number);
        editTextMovie.setText(movie);
    }
}