package ru.mirea.svinarenkomd.internalfilestorage;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText editTextDate;
    private Button buttonSave;
    private static final String FILE_NAME = "history_date.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextDate = findViewById(R.id.editTextDate);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editTextDate.getText().toString();

                // Простая проверка на пустоту
                if (!text.isEmpty()) {
                    saveTextToFile(text);
                } else {
                    Toast.makeText(MainActivity.this, "Введите текст!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveTextToFile(String text) {
        try {
            // Открываем поток для записи в файл (создаст файл, если его нет)
            FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(text.getBytes());
            fos.close();
            Toast.makeText(this, "Данные сохранены в файл", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка сохранения", Toast.LENGTH_SHORT).show();
        }
    }
}