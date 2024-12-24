package com.example.proyectobroker.exceptions;

import com.example.proyectobroker.view.AlertView;

public class Exceptions  {

    //metodo validar campo usuario , solo se pueden poner letras o numeros
    public void validateUserField(String usernameField){
        //Comprobamos si está el campo vacio
        if (usernameField.isEmpty()){
            throw new RuntimeException("Campo vacio");
        }
        //validamos que NO tenga espacios en blanco
        if (usernameField.contains(" ")){
            throw new RuntimeException("No se permiten espacios en blanco");
        }
        //validamos longitud minimo 5 letras y maximo 25
        if (usernameField.length()<5 || usernameField.length()>25){
            throw new RuntimeException("Longitud mínina 5 caracteres y maxima 25");
        }
        //validamos que solo tenga letras y numeros
        if (!usernameField.matches("[a-zA-Z0-9]+")){
            throw new RuntimeException("Solo se permiten letras y numeros");
        }
    }
    //Metodo validar campo contraseña
    public void validatePasswordField(String passwordField){
        //comprobar si está vacio
        if (passwordField.isEmpty()){
            throw new RuntimeException("Campo vacio");
        }
        //comprobamos que no tenga espacios en blanco
        if (passwordField.contains(" ")){
            throw new RuntimeException("No se permiten espacios en blanco");
        }
        //validamos longitud minimo 8 letras y maximo 40
        if (passwordField.length()<8 || passwordField.length()>40){
            throw new RuntimeException("Longitud mínina 8 caracteres y maxima 40");
        }
    }
    public void checkPasswordMatch(String password, String passwordCheck) {
        if (!password.equalsIgnoreCase(passwordCheck)){
            AlertView alerta = new AlertView("Error", "Error en la contraseña", "Las contraseñas no coinciden");
            alerta.mostrarAlerta();
            throw new RuntimeException("Las contraseñas no coinciden");
        }
    }
}
