package com.example.continuiapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AcercaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Oculta la barra de acción
        setContentView(R.layout.activity_acerca);

        Button btnVolver = findViewById(R.id.btnVolverAcerca);

        btnVolver.setOnClickListener(view -> finish());
    }
}

