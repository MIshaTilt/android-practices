package ru.mirea.svinarenkomd.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.svinarenkomd.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkinfo = null;
                if (connectivityManager != null) {
                    networkinfo = connectivityManager.getActiveNetworkInfo();
                }

                if (networkinfo != null && networkinfo.isConnected()) {
                    // Запуск первого запроса - получение IP и координат
                    new DownloadIpTask().execute("https://ipinfo.io/json");
                } else {
                    Toast.makeText(MainActivity.this, "Нет интернета", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // 1-й AsyncTask: Получаем данные о местоположении по IP
    private class DownloadIpTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.cityTextView.setText("Загружаем местоположение...");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(MainActivity.class.getSimpleName(), result);
            try {
                JSONObject responseJson = new JSONObject(result);
                String city = responseJson.getString("city");
                String region = responseJson.getString("region");
                String loc = responseJson.getString("loc"); // Координаты в формате "широта,долгота"

                binding.cityTextView.setText("Город: " + city);
                binding.regionTextView.setText("Регион: " + region);

                // Разбиваем координаты для передачи в сервис погоды
                String[] coords = loc.split(",");
                String latitude = coords[0];
                String longitude = coords[1];

                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude="
                        + latitude + "&longitude=" + longitude + "&current_weather=true";

                // Запускаем 2-й запрос - получение погоды
                new DownloadWeatherTask().execute(weatherUrl);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }

    // 2-й AsyncTask: Получаем погоду по координатам
    private class DownloadWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            binding.weatherTextView.setText("Загружаем погоду...");
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject responseJson = new JSONObject(result);
                JSONObject currentWeather = responseJson.getJSONObject("current_weather");
                String temperature = currentWeather.getString("temperature");

                binding.weatherTextView.setText("Температура: " + temperature + " °C");
            } catch (JSONException e) {
                e.printStackTrace();
                binding.weatherTextView.setText("Ошибка загрузки погоды");
            }
            super.onPostExecute(result);
        }
    }

    // Универсальный метод для скачивания данных по URL (из методички)
    private String downloadInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                data = bos.toString();
            } else {
                data = connection.getResponseMessage() + ". Error Code: " + responseCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }
}