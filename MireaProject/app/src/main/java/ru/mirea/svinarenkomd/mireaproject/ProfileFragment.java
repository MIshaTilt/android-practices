package ru.mirea.svinarenkomd.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    public ProfileFragment() { super(R.layout.fragment_profile); }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        EditText etName = view.findViewById(R.id.etName);
        EditText etJob = view.findViewById(R.id.etJob);
        Button btnSave = view.findViewById(R.id.btnSaveProfile);

        SharedPreferences prefs = requireActivity().getSharedPreferences("profile_prefs", Context.MODE_PRIVATE);
        etName.setText(prefs.getString("name", ""));
        etJob.setText(prefs.getString("job", ""));

        btnSave.setOnClickListener(v -> {
            prefs.edit().putString("name", etName.getText().toString())
                    .putString("job", etJob.getText().toString()).apply();
        });
    }
}