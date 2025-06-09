package com.example.continuiapp.basedatos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String nombre;

    public String email;

    public String password;

    // Constructor
    public Usuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    // Metodo getEmail
    public String getEmail() {
        return email;
    }
}
