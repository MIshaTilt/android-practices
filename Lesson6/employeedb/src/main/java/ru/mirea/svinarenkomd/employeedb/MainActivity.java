package ru.mirea.svinarenkomd.employeedb;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Получаем доступ к базе
        AppDatabase db = App.getInstance().getDatabase();
        SuperheroDao superheroDao = db.superheroDao();

        // Создаем героя
        Superhero hero = new Superhero();
        hero.name = "Бэтмен";
        hero.superpower = "Деньги";

        // Записываем в БД
        superheroDao.insert(hero);
        Log.d(TAG, "Добавлен герой: " + hero.name + " с силой: " + hero.superpower);

        // Получаем героя с ID 1
        Superhero savedHero = superheroDao.getById(1);

        // Обновляем его данные
        savedHero.superpower = "Интеллект и технологии";
        superheroDao.update(savedHero);

        Log.d(TAG, "Обновленный герой: " + savedHero.name + " с силой: " + savedHero.superpower);
    }
}