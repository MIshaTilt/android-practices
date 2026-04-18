package ru.mirea.svinarenkomd.serviceapp;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PlayerService extends Service {
    private MediaPlayer mediaPlayer;
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public IBinder onBind(Intent intent) {
        return null; // У нас не привязанный сервис, поэтому null
    }

    @Override
    public void onCreate() {
        super.onCreate();


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Играет: 9mice, Kai Angel - Blum - Big City Life")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("9mice, Kai Angel - Blum - Big City Life"))
                .setContentTitle("Музыкальный Плеер");

        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Свинаренко Михаил Notification", importance);
        channel.setDescription("MIREA Channel");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(channel);

        // Запускаем сервис на переднем плане (показывает неудаляемое уведомление)
        startForeground(1, builder.build(), android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK);

        // --- Настройка Плеера ---
        mediaPlayer = MediaPlayer.create(this, R.raw.music); // Берет файл из res/raw
        mediaPlayer.setLooping(false); // Играет 1 раз
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();

        // Слушатель окончания музыки
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopForeground(true); // Убирает уведомление
                stopSelf(); // Останавливает сам сервис
            }
        });

        return START_STICKY; // Перезапустит сервис, если система его убьет
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release(); // Освобождаем ресурсы
        }
    }
}
