package com.example.proyectobroker.exceptions;

import com.example.proyectobroker.view.AlertView;
import javafx.scene.image.Image;

public class Exceptions  {
    private AlertView alert;


    //CREAR USER
    //metodo validar campo usuario , solo se pueden poner letras o numeros
    /**
     * Metodo para validar el campo usuario
     * @param usernameField
     */
    public void validateUserField(String usernameField){
        //Comprobamos si está el campo vacio
        if (usernameField.isEmpty()){
            alert = new AlertView("Error", "Campo vacio", "El campo usuario no puede estar vacio");
            alert.mostrarAlerta();
            throw new RuntimeException("Campo vacio");
        }
        //validamos que NO tenga espacios en blanco
        if (usernameField.contains(" ")){
            alert = new AlertView("Error", "Espacios en blanco", "No se permiten espacios en blanco");
            alert.mostrarAlerta();
            throw new RuntimeException("No se permiten espacios en blanco");
        }
        //validamos longitud minimo 5 letras y maximo 25
        if (usernameField.length()<5 || usernameField.length()>25){
            alert = new AlertView("Error", "Longitud incorrecta", "Longitud mínina 5 caracteres y maxima 25");
            alert.mostrarAlerta();
            throw new RuntimeException("Longitud mínina 5 caracteres y maxima 25");
        }
        //validamos que solo tenga letras y numeros
        if (!usernameField.matches("[a-zA-Z0-9]+")){
            alert = new AlertView("Error", "Caracteres no permitidos", "Solo se permiten letras y numeros");
            alert.mostrarAlerta();
            throw new RuntimeException("Solo se permiten letras y numeros");
        }
    }
    //Metodo validar campo contraseña
    /**
     * Metodo para validar el campo contraseña
     * @param passwordField
     */
    public void validatePasswordField(String passwordField){
        //comprobar si está vacio
        if (passwordField.isEmpty()){
            alert = new AlertView("Error", "Campo vacio", "El campo contraseña no puede estar vacio");
            alert.mostrarAlerta();
            throw new RuntimeException("Campo vacio");
        }
        //comprobamos que no tenga espacios en blanco
        if (passwordField.contains(" ")){
            alert = new AlertView("Error", "Espacios en blanco", "No se permiten espacios en blanco");
            alert.mostrarAlerta();
            throw new RuntimeException("No se permiten espacios en blanco");
        }
        //validamos longitud minimo 8 letras y maximo 40
        if (passwordField.length()<8 || passwordField.length()>40){
            alert = new AlertView("Error", "Longitud incorrecta", "Longitud mínina 8 caracteres y maxima 40");
            alert.mostrarAlerta();
            throw new RuntimeException("Longitud mínina 8 caracteres y maxima 40");
        }
    }
    //Metodo validar campo contraseña
    /**
     * Metodo para validar el campo contraseña
     * @param password
     * @param passwordCheck
     */
    public void checkPasswordMatch(String password, String passwordCheck) {
        if (!password.equalsIgnoreCase(passwordCheck)){
            AlertView alerta = new AlertView("Error", "Error en la contraseña", "Las contraseñas no coinciden");
            alerta.mostrarAlerta();
            throw new RuntimeException("Las contraseñas no coinciden");
        }
    }
    //CONFIGURACION
    //Metodo para validar el campo dinero
    /**
     * Metodo para validar el campo dinero
     * @param money
     */
    public void validateMoney(String money){

        //comprobar si está vacio
        if (money.isEmpty()){
            alert = new AlertView("Error", "Campo vacio", "El campo dinero no puede estar vacio");
            alert.mostrarAlerta();
            throw new RuntimeException("Campo vacio");
        }
        //comprobar que sea un numero
        if (!money.matches("[0-9]+(\\.[0-9]+)?")){
            alert = new AlertView("Error", "Solo se permiten numeros", "Solo se permiten numeros");
            alert.mostrarAlerta();
            throw new RuntimeException("Solo se permiten numeros");
        }
        //Comprobar que no sea negativo
        if (Double.parseDouble(money)<0){
            alert = new AlertView("Error", "No se permiten valores negativos", "No se permiten valores negativos");
            alert.mostrarAlerta();
            throw new RuntimeException("No se permiten valores negativos");
        }
    }
    //metodo validar imagen
    /**
     * Metodo para validar la imagen
     * @param image
     */
    public void validateImage(Image image){
        if (image == null){
            alert = new AlertView("Error", "Imagen no válida", "Imagen no válida");
            alert.mostrarAlerta();
            throw new RuntimeException("Imagen no válida");
        }
        if (image.getHeight() > 1000 || image.getWidth() > 1000){
            alert = new AlertView("Error", "Imagen muy grande", "Imagen muy grande");
            alert.mostrarAlerta();
            throw new RuntimeException("Imagen muy grande");
        }

    }
}
