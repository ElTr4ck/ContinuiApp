package com.example.continuiapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AyudaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Oculta la barra de acción
        setContentView(R.layout.activity_ayuda);

        //Referencia al botón
        //Botón para volver a la pantalla principal
        Button btnVolver = findViewById(R.id.btnVolverAyuda);

        //Evento click para el botón
        btnVolver.setOnClickListener(this::volver);
    }

    //Método para volver a la pantalla principal
    public void volver(View view) {
        finish();
    }
}
