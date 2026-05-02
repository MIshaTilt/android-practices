package ru.mirea.svinarenkomd.securesharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.security.GeneralSecurityException;

import ru.mirea.svinarenkomd.securesharedpreferences.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textViewPoet = findViewById(R.id.textViewPoet);

        try {
            // 1. Создаем ключ для шифрования
            KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
            String mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec);

            // 2. Инициализируем EncryptedSharedPreferences
            SharedPreferences secureSharedPreferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    mainKeyAlias,
                    getBaseContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

            // 3. Записываем имя поэта (Оно зашифруется автоматически)
            secureSharedPreferences.edit().putString("secure", "Александр Пушкин").apply();

            // 4. Читаем имя поэта (Оно расшифруется автоматически)
            String result = secureSharedPreferences.getString("secure", "ЛЮБИМЫЙ ПОЭТ");

            // 5. Выводим на экран
            textViewPoet.setText(result);

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}