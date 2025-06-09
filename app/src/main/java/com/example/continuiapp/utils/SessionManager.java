package com.example.continuiapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.continuiapp.LoginActivity;

public class SessionManager {

    private static final String PREF_NAME = "SesionPrefs";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Guardar sesión
    public void guardarSesion(String email) {
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    // Obtener email actual
    public String obtenerEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    // Verifica si hay sesión iniciada
    public boolean haySesionActiva() {
        return obtenerEmail() != null;
    }

    // Cierra sesión
    public void cerrarSesion() {
        editor.clear();
        editor.apply();

        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
