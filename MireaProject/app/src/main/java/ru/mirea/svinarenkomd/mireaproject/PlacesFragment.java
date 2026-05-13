package ru.mirea.svinarenkomd.mireaproject; // Не забудь проверить свой пакет!

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.traffic.TrafficLayer;
import com.yandex.runtime.image.ImageProvider;

public class PlacesFragment extends Fragment {

    private MapView mapView;
    private MapObjectCollection mapObjects;
    private TrafficLayer trafficLayer;

    // Вспомогательный класс для хранения данных о месте
    private static class PlaceInfo {
        String title;
        String address;
        String description;

        PlaceInfo(String title, String address, String description) {
            this.title = title;
            this.address = address;
            this.description = description;
        }
    }

    // Обработчик клика по маркеру
    private final MapObjectTapListener placeTapListener = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
            // Проверяем, что в userData лежит наш объект PlaceInfo
            if (mapObject.getUserData() instanceof PlaceInfo) {
                PlaceInfo info = (PlaceInfo) mapObject.getUserData();

                // Создаем и показываем AlertDialog
                new AlertDialog.Builder(requireContext())
                        .setTitle(info.title) // Заголовок - название заведения
                        .setMessage("Адрес: " + info.address + "\n\nОписание: \n" + info.description) // Текст диалога
                        .setPositiveButton("Закрыть", (dialog, which) -> dialog.dismiss()) // Кнопка закрытия
                        .setIcon(android.R.drawable.ic_dialog_info) // Иконка рядом с заголовком
                        .show();

                // Центрируем камеру на выбранном маркере (небольшой бонус для красоты)
                mapView.getMap().move(
                        new CameraPosition(point, 14.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 0.5f),
                        null);
            }
            return true;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        mapView = view.findViewById(R.id.yandexMapView);

        setupMap();
        addEstablishmentMarkers();

        // Дополнительная функция: Слой пробок
        trafficLayer = MapKitFactory.getInstance().createTrafficLayer(mapView.getMapWindow());
        trafficLayer.setTrafficVisible(true);

        return view;
    }

    private void setupMap() {
        mapView.getMap().move(
                new CameraPosition(new Point(55.740000, 37.600000), 11.5f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        mapObjects = mapView.getMap().getMapObjects().addCollection();
    }

    private void addEstablishmentMarkers() {
        // Создаем подробные описания, так как теперь в диалоге много места

        Point point1 = new Point(55.756736, 37.613997);
        PlaceInfo info1 = new PlaceInfo(
                "Ресторан «Белуга»",
                "Моховая ул., 15/1 стр. 1 (Отель Националь)",
                "Шикарный ресторан с панорамным видом на Кремль и Красную площадь. " +
                        "Славится огромным выбором икры, изысканными блюдами русской кухни в авторском прочтении и премиальным сервисом."
        );
        createMarker(point1, info1);

        Point point2 = new Point(55.747485, 37.581307);
        PlaceInfo info2 = new PlaceInfo(
                "Ресторан «White Rabbit»",
                "Смоленская пл., 3",
                "Один из самых известных ресторанов Москвы, регулярно попадающий в мировые рейтинги. " +
                        "Расположен под стеклянным куполом. Высокая кухня от шеф-повара Владимира Мухина."
        );
        createMarker(point2, info2);

        Point point3 = new Point(55.727780, 37.601600);
        PlaceInfo info3 = new PlaceInfo(
                "Кафе «Гараж»",
                "Крымский Вал, 9 стр. 32",
                "Уютное кафе на территории музея современного искусства в Парке Горького. " +
                        "Отличное место, чтобы выпить кофе, съесть вкусный десерт и обсудить выставку после долгой прогулки."
        );
        createMarker(point3, info3);
    }

    private void createMarker(Point point, PlaceInfo info) {
        PlacemarkMapObject mark = mapObjects.addPlacemark(
                point,
                ImageProvider.fromResource(requireContext(), android.R.drawable.btn_star_big_on)
        );
        mark.setUserData(info);
        mark.addTapListener(placeTapListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }
}