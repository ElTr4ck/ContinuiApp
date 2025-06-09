package com.example.continuiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.continuiapp.basedatos.*;
import com.example.continuiapp.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView tvRegistro;
    Button btnIniciarSesion, btnIrRegistro;
    UsuarioDAO usuarioDAO; // DAO para acceder a la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); // Oculta la barra de acción
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnIniciarSesion = findViewById(R.id.btnLogin);
        tvRegistro = findViewById(R.id.tvRegisterLink);

        usuarioDAO = BaseDeDatos.getInstance(this).usuarioDAO();

        btnIniciarSesion.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                Usuario usuario = usuarioDAO.login(email, password);
                if (usuario != null) {
                    Toast.makeText(this, "¡Bienvenido " + usuario.nombre + "!", Toast.LENGTH_SHORT).show();
                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                    sessionManager.guardarSesion(usuario.getEmail());
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void irRegistro(View view) {
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }


}
