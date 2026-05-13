package ru.mirea.svinarenkomd.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkFragment extends Fragment {

    private TextView factTextView;
    private Button loadFactButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network, container, false);

        factTextView = view.findViewById(R.id.factTextView);
        loadFactButton = view.findViewById(R.id.loadFactButton);

        // Настройка Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://catfact.ninja/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        loadFactButton.setOnClickListener(v -> {
            factTextView.setText("Загрузка...");

            // Выполняем асинхронный сетевой запрос
            apiService.getCatFact().enqueue(new Callback<CatFact>() {
                @Override
                public void onResponse(Call<CatFact> call, Response<CatFact> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        factTextView.setText(response.body().getFact());
                    } else {
                        factTextView.setText("Ошибка ответа от сервера");
                    }
                }

                @Override
                public void onFailure(Call<CatFact> call, Throwable t) {
                    factTextView.setText("Ошибка сети: " + t.getMessage());
                }
            });
        });

        return view;
    }
}