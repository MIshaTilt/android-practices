package ru.mirea.svinarenkomd.cryptoloader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import ru.mirea.svinarenkomd.cryptoloader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private ActivityMainBinding binding;
    private final int LoaderID = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phrase = binding.editPhrase.getText().toString();
                if(phrase.isEmpty()) return;

                // 1. Генерируем ключ
                SecretKey key = generateKey();
                // 2. Шифруем нашу фразу
                byte[] shiper = encryptMsg(phrase, key);

                // 3. Упаковываем зашифрованные байты и ключ в Bundle
                Bundle bundle = new Bundle();
                bundle.putByteArray(MyLoader.ARG_WORD, shiper);
                bundle.putByteArray("key", key.getEncoded());

                // 4. Запускаем Loader. Используем restartLoader, чтобы можно было жать кнопку много раз
                LoaderManager.getInstance(MainActivity.this).restartLoader(LoaderID, bundle, MainActivity.this);
            }
        });
    }

    // Методы обратного вызова Loader'a
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle bundle) {
        if (id == LoaderID) {
            Toast.makeText(this, "Loader запущен!", Toast.LENGTH_SHORT).show();
            return new MyLoader(this, bundle);
        }
        throw new IllegalArgumentException("Неверный ID Loader'a");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        if (loader.getId() == LoaderID) {
            // Выводим расшифрованную фразу в Toast
            Toast.makeText(this, "Расшифровано: " + s, Toast.LENGTH_LONG).show();
            Log.d("MainActivity", "Расшифрованная фраза: " + s);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        // Оставляем пустым
    }

    // --- МЕТОДЫ ШИФРОВАНИЯ ИЗ МЕТОДИЧКИ ---
    public static SecretKey generateKey() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            return new javax.crypto.spec.SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptMsg(String message, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return cipher.doFinal(message.getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
