package com.example.continuiapp.basedatos;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Usuario.class}, version = 1)
public abstract class BaseDeDatos extends RoomDatabase {

    private static BaseDeDatos INSTANCE;

    public abstract UsuarioDAO usuarioDAO();

    public static synchronized BaseDeDatos getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BaseDeDatos.class, "continuapp_db")
                    .allowMainThreadQueries() // Solo para pruebas
                    .build();
        }
        return INSTANCE;
    }
}

