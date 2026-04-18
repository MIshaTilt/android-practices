package ru.mirea.svinarenkomd.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.svinarenkomd.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Инициализация ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Создаем и запускаем новый поток
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            // Получаем данные из полей ввода
                            String pairsStr = binding.editPairs.getText().toString();
                            String daysStr = binding.editDays.getText().toString();

                            if (!pairsStr.isEmpty() && !daysStr.isEmpty()) {
                                float pairs = Float.parseFloat(pairsStr);
                                float days = Float.parseFloat(daysStr);

                                // Имитируем сложную работу (задержка 2 секунды)
                                Thread.sleep(2000);

                                float result = pairs / days;

                                // ВАЖНО: Обновлять интерфейс можно ТОЛЬКО в главном (UI) потоке
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.textResult.setText("Среднее кол-во пар в день: " + result);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            Log.e("ThreadApp", "Ошибка в вычислениях", e);
                        }
                    }
                }).start(); // Не забываем запустить поток!
            }
        });
    }
}
