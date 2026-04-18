package ru.mirea.svinarenkomd.work_manager;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;


import ru.mirea.svinarenkomd.work_manager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonStartWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Задаем условия: нужен Wi-Fi и зарядка
                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED) // Wi-Fi
                        .setRequiresCharging(true) // На зарядке
                        .build();

                // 2. Создаем запрос на разовое выполнение (OneTime)
                OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                        .setConstraints(constraints)
                        .build();

                // 3. Передаем задачу диспетчеру WorkManager
                WorkManager.getInstance(MainActivity.this).enqueue(uploadWorkRequest);
            }
        });
    }
}
