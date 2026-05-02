package ru.mirea.svinarenkomd.notebook;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Notebook";
    private static final int REQUEST_CODE_STORAGE = 101;

    private EditText editTextFileName;
    private EditText editTextQuote;
    private Button buttonSave;
    private Button buttonLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextFileName = findViewById(R.id.editTextFileName);
        editTextQuote = findViewById(R.id.editTextQuote);
        buttonSave = findViewById(R.id.buttonSave);
        buttonLoad = findViewById(R.id.buttonLoad);

        buttonSave.setOnClickListener(v -> saveToFile());
        buttonLoad.setOnClickListener(v -> loadFromFile());

        // Проверяем разрешения при запуске
        checkStoragePermission();
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Разрешения получены", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Разрешения необходимы для работы с файлами", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveToFile() {
        String fileName = editTextFileName.getText().toString().trim();
        String content = editTextQuote.getText().toString().trim();

        if (fileName.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Введите имя файла и текст!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }

        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(path, fileName);

            // Создаём папку Documents, если её нет
            if (!path.exists()) {
                path.mkdirs();
            }

            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            osw.write(content);
            osw.close();
            fos.close();

            Toast.makeText(this, "Файл сохранён: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "Файл сохранён: " + file.getAbsolutePath());

        } catch (Exception e) {
            Log.e(TAG, "Ошибка сохранения", e);
            Toast.makeText(this, "Ошибка сохранения: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadFromFile() {
        String fileName = editTextFileName.getText().toString().trim();
        if (fileName.isEmpty()) {
            Toast.makeText(this, "Введите имя файла!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!fileName.endsWith(".txt")) {
            fileName += ".txt";
        }

        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(path, fileName);

            if (!file.exists()) {
                Toast.makeText(this, "Файл не найден!", Toast.LENGTH_SHORT).show();
                return;
            }

            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            reader.close();
            isr.close();
            fis.close();

            String content = stringBuilder.toString().trim();
            editTextQuote.setText(content);

            Toast.makeText(this, "Файл загружен", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Файл загружен: " + file.getAbsolutePath());

        } catch (Exception e) {
            Log.e(TAG, "Ошибка чтения", e);
            Toast.makeText(this, "Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}