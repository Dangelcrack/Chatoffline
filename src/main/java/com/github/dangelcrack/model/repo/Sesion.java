package com.github.dangelcrack.model.repo;

import com.github.dangelcrack.model.entity.User;

public class Sesion {
    //Atributos de la clase
    private static Sesion _instance;
    private User user;

    // Constructor privado para evitar la creación de instancias directamente
    private Sesion(User user) {
        this.user = user;
    }

    // Método para iniciar sesión con una persona
    public static void iniciateSesion(User user) {
        if (_instance == null) {
            _instance = new Sesion(user);
        } else {
            System.out.println("Ya hay una sesión activa. Cierra la sesión actual antes de iniciar una nueva.");
        }
    }

    public static Sesion getInstance() {
        return _instance;
    }

    //Constructor vacio
    private Sesion() {
    }
    //Constructor con los atributos


    //Getter de persona
    public User getUser() {
        return user;
    }

    //Setter de persona
    public void setUser(User user) {
        this.user = user;
    }

    //Funcion que cierra la sesion del usuario
    public static void closeSession() {
        _instance = null;
    }
}