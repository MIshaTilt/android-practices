package ru.mirea.svinarenkomd.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.svinarenkomd.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Обработчик, который будет принимать ответы из MyLooper и выводить их в лог
        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("result");
                Log.d("MainActivity", "Результат задачи: " + result);
            }
        };

        // Создаем и запускаем наш фоновый Looper
        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ageStr = binding.editAge.getText().toString();
                String workStr = binding.editWork.getText().toString();

                if (!ageStr.isEmpty() && !workStr.isEmpty() && myLooper.mHandler != null) {
                    int age = Integer.parseInt(ageStr);

                    // Создаем сообщение и упаковываем в него данные
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("WORK", workStr);
                    bundle.putInt("AGE", age);
                    msg.setData(bundle);

                    // Отправляем сообщение в фоновый поток (в очередь MyLooper)
                    myLooper.mHandler.sendMessage(msg);
                }
            }
        });
    }
}
