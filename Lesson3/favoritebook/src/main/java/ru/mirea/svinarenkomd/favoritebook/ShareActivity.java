package ru.mirea.svinarenkomd.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView ageView = findViewById(R.id.textView);
            String university = extras.getString("book_name");
            ageView.setText(String.format("Любимая книга разработчика – %s", university));
        }

        Button button = findViewById(R.id.button);
        EditText editText = findViewById(R.id.editText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                String text = editText.getText().toString();
                data.putExtra("MESSAGE", text);
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });
    }
}