package ru.mirea.svinarenkomd.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class MyLooper extends Thread {
    public Handler mHandler;
    private Handler mainHandler;

    public MyLooper(Handler mainThreadHandler) {
        mainHandler = mainThreadHandler;
    }

    public void run() {
        Log.d("MyLooper", "run");
        Looper.prepare();

        mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                // Получаем данные из MainActivity
                String work = msg.getData().getString("WORK");
                int age = msg.getData().getInt("AGE");

                Log.d("MyLooper", "Получено сообщение: " + work + ", возраст: " + age);

                // Задержка равная количеству лет (лет = секунды)
                try {
                    Thread.sleep(age * 1000L); // Умножаем на 1000, т.к. sleep принимает миллисекунды
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Отправляем ответ обратно в MainActivity
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", String.format("Вы работаете: %s. Возраст: %d", work, age));
                message.setData(bundle);

                mainHandler.sendMessage(message);
            }
        };
        Looper.loop();
    }
}

