package ru.mirea.svinarenkomd.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class WebViewFragment extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        WebView webView = view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient()); // Открывать ссылки внутри приложения
        webView.getSettings().setJavaScriptEnabled(true); // Включить JS
        webView.loadUrl("https://www.mirea.ru/"); // Загружаем сайт МИРЭА

        return view;
    }
}