package com.example.continuiapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.continuiapp.basedatos.*;

public class RegistroActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextEmail, editTextPassword;
    private Button btnRegistrar;
    private UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Referencias UI
        editTextNombre = findViewById(R.id.etNombre);
        editTextEmail = findViewById(R.id.etEmailRegistro);
        editTextPassword = findViewById(R.id.etPasswordRegistro);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Acceso a la base de datos
        usuarioDAO = BaseDeDatos.getInstance(this).usuarioDAO();

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombre.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegistroActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar si ya existe un usuario con ese correo
                Usuario existente = usuarioDAO.login(email, password); // solo valida si existe usuario con ese email y pass
                if (usuarioDAO.login(email, password) != null) {
                    Toast.makeText(RegistroActivity.this, "Ya existe una cuenta con ese correo", Toast.LENGTH_SHORT).show();
                    return;
                }

                Usuario nuevoUsuario = new Usuario(nombre, email, password);
                usuarioDAO.insertarUsuario(nuevoUsuario);
                Toast.makeText(RegistroActivity.this, "Registro exitoso. Inicia sesión.", Toast.LENGTH_LONG).show();

                // Regresar al login
                startActivity(new Intent(RegistroActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
