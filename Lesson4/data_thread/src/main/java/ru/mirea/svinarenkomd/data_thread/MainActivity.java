package ru.mirea.svinarenkomd.data_thread;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.TimeUnit;

import ru.mirea.svinarenkomd.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Создаем три задачи (Runnable), которые просто дописывают текст на экран
        final Runnable runn1 = new Runnable() {
            public void run() {
                binding.tvInfo.append("Выполнен runn1 (runOnUiThread)\n");
            }
        };

        final Runnable runn2 = new Runnable() {
            public void run() {
                binding.tvInfo.append("Выполнен runn2 (post)\n");
            }
        };

        final Runnable runn3 = new Runnable() {
            public void run() {
                binding.tvInfo.append("Выполнен runn3 (postDelayed)\n");
            }
        };

        // Создаем фоновый поток
        Thread t = new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(2); // Ждем 2 секунды
                    runOnUiThread(runn1);      // Отправляем 1-ю задачу

                    TimeUnit.SECONDS.sleep(1); // Ждем еще 1 секунду

                    // Запускаем 3-ю задачу с задержкой в 2 секунды
                    binding.tvInfo.postDelayed(runn3, 2000);

                    // Запускаем 2-ю задачу без задержки
                    binding.tvInfo.post(runn2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start(); // Запускаем поток
    }
}
