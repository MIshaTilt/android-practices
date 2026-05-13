package ru.mirea.svinarenkomd.mireaproject;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class SensorFragment extends Fragment implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private TextView azimuthTextView, pitchTextView, rollTextView, logicTextView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        azimuthTextView = view.findViewById(R.id.textViewAzimuth);
        pitchTextView = view.findViewById(R.id.textViewPitch);
        rollTextView = view.findViewById(R.id.textViewRoll);
        logicTextView = view.findViewById(R.id.textViewLogic);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float valueAzimuth = event.values[0];
            float valuePitch = event.values[1];
            float valueRoll = event.values[2];

            azimuthTextView.setText("Ось X (Боковое): " + valueAzimuth);
            pitchTextView.setText("Ось Y (Продольное): " + valuePitch);
            rollTextView.setText("Ось Z (Вертикальное): " + valueRoll);

            // Логическая задача: Определение "Телефон лежит ровно на столе"
            if (valueRoll > 9.0 && valueRoll < 10.5 && Math.abs(valueAzimuth) < 1.0 && Math.abs(valuePitch) < 1.0) {
                logicTextView.setText("Телефон лежит ровно на столе!");
                logicTextView.setTextColor(0xFF00AA00); // Зеленый
            } else {
                logicTextView.setText("Телефон под наклоном");
                logicTextView.setTextColor(0xFFFF0000); // Красный
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}