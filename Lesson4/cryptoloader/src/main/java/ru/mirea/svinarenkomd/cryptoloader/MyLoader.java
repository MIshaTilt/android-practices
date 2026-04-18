package ru.mirea.svinarenkomd.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MyLoader extends AsyncTaskLoader<String> {
    public static final String ARG_WORD = "word";
    private byte[] cryptText;
    private byte[] key;

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null) {
            // Достаем зашифрованную фразу и ключ из Bundle
            cryptText = args.getByteArray(ARG_WORD);
            key = args.getByteArray("key");
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        // Имитируем долгую работу (чтобы было видно, что это фон)
        SystemClock.sleep(2000);

        if (cryptText != null && key != null) {
            // Восстанавливаем ключ
            SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
            // Расшифровываем
            return decryptMsg(cryptText, originalKey);
        }
        return "Ошибка: нет данных";
    }

    // Метод дешифровки (из методички)
    public static String decryptMsg(byte[] cipherText, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return new String(cipher.doFinal(cipherText));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

