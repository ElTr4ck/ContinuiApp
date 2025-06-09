package com.example.continuiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.continuiapp.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    MaterialButton btnStartCalculation;
    LinearLayout llHistory, llFormulas, llHelp, llSettings;
    TextView tvFacebook, tvInstagram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Oculta la barra de acción
        setContentView(R.layout.activity_main);

        SessionManager sessionManager = new SessionManager(getApplicationContext());

        if (!sessionManager.haySesionActiva()) {
            // Redirigir al login
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Referencias a botones y tarjetas
        btnStartCalculation = findViewById(R.id.btnStartCalculation);
        llHistory = findViewById(R.id.llHistory);
        llFormulas = findViewById(R.id.llFormulas);
        llHelp = findViewById(R.id.llHelp);
        llSettings = findViewById(R.id.llSettings);
        TextView tvFacebook = findViewById(R.id.tvAbout);
        TextView tvInstagram = findViewById(R.id.tvTerms);

        // Ir a actividad de cálculo
        btnStartCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CalculoDetalladoActivity.class);
                startActivity(intent);
            }
        });

        // Ir a "Acerca de nosotros"
        llHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AcercaActivity.class);
                startActivity(intent);
            }
        });

        // Ir a "historial"
        llFormulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HistorialActivity.class);
                startActivity(intent);
            }
        });

        // Ir a "Ayuda"
        llHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AyudaActivity.class);
                startActivity(intent);
            }
        });

        // Cerrar sesión de la aplicación
        llSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                sessionManager.cerrarSesion(); // Esto borra la sesión y redirige al login
            }
        });

        tvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirURL("https://www.facebook.com/share/195wMyzHaf/");
            }
        });

        tvInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirURL("https://www.instagram.com/fisicaa_paraa_todos?igsh=OXFwYjlnNWdtdDVl");
            }
        });
    }

    private void abrirURL(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(android.net.Uri.parse(url));
        startActivity(intent);
    }
}
