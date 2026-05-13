package ru.mirea.svinarenkomd.osmmaps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import ru.mirea.svinarenkomd.osmmaps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private MapView mapView = null;
    private ActivityMainBinding binding;
    private MyLocationNewOverlay locationNewOverlay;
    private static final int REQUEST_CODE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ВАЖНО: Инициализация конфигурации OSM ДО создания интерфейса
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mapView = binding.mapView;

        // Настройка карты
        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        // Перемещение камеры
        IMapController mapController = mapView.getController();
        mapController.setZoom(11.0);
        GeoPoint startPoint = new GeoPoint(55.751244, 37.618423); // Центр Москвы
        mapController.setCenter(startPoint);

        // Проверяем разрешения
        checkPermissionsAndInitOverlays();
    }

    private void checkPermissionsAndInitOverlays() {
        int locPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (locPermission == PackageManager.PERMISSION_GRANTED) {
            initMapFeatures();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initMapFeatures();
        }
    }

    // Здесь собираем все слои и задания
    private void initMapFeatures() {
        // 1. Местоположение устройства
        locationNewOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), mapView);
        locationNewOverlay.enableMyLocation();
        mapView.getOverlays().add(this.locationNewOverlay);

        // 2. Добавление компаса
        CompassOverlay compassOverlay = new CompassOverlay(getApplicationContext(), new InternalCompassOrientationProvider(getApplicationContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        // 3. Отображение метрической шкалы масштаба
        final Context context = this.getApplicationContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        scaleBarOverlay.setCentred(true);
        scaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        mapView.getOverlays().add(scaleBarOverlay);

        // 4. ВЫПОЛНЕНИЕ ЗАДАНИЯ: Добавление маркеров интересующих мест
        addMarker(new GeoPoint(55.753930, 37.620795), "Красная Площадь", "Сердце Москвы!");
        addMarker(new GeoPoint(55.729705, 37.601550), "Парк Горького", "Отличное место для прогулок.");
        addMarker(new GeoPoint(55.828298, 37.631580), "ВДНХ", "Главная выставка страны.");
    }

    private void addMarker(GeoPoint point, String title, String description) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        // Используем стандартную иконку OSM (или можешь задать свою из drawable)
        marker.setIcon(ResourcesCompat.getDrawable(getResources(), org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
        marker.setTitle(title);

        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(), title + ": " + description, Toast.LENGTH_LONG).show();
                return true;
            }
        });
        mapView.getOverlays().add(marker);
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if (mapView != null) {
            mapView.onPause();
        }
    }
}