package com.example.continuiapp.basedatos;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UsuarioDAO {

    @Insert
    void insertarUsuario(Usuario usuario);

    // Método para login: busca un usuario con email y password dados
    @Query("SELECT * FROM usuarios WHERE email = :email AND password = :password LIMIT 1")
    Usuario login(String email, String password);

    // Se pueden agregar otros métodos, como obtener todos, eliminar, etc.
}
