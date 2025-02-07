package database;

import com.example.proyectobroker.Main;
import com.example.proyectobroker.controller.CryptoController;
import com.example.proyectobroker.model.Inversion;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.model.UserConfig;
import com.example.proyectobroker.view.AlertView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectMysql {
    //esta clase es para simular una conexión a una base de datos jdbc
    //private final String URL = "jdbc:mysql://localhost:3307/bdbrokeremmanuel";
   // private final String username ="root";
   // private final String password = "";
   // private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:sqlite:database/bdbrokeremmanuel.db";

    private CryptoController cryptoController = new CryptoController();

    public ConnectMysql() {

    }
    public Connection conectar(){
        Connection con = null;

        //TODO: cambiar a sqlite
        try {
            con = DriverManager.getConnection(URL);
            System.out.println(Main.ANSI_GREEN+"Conexión establecida"+Main.ANSI_RESET);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return con;
    }
    public boolean checkUserExists(User user){
        String nombre = user.getUsername();
        String query = "SELECT * FROM users WHERE username = '"+nombre+"'";
        PreparedStatement ps;
        try {
           ps = conectar().prepareStatement(query);
              ps.executeQuery();
              //si hay resultados
                if (ps.getResultSet().next()){
                    System.out.println(Main.ANSI_GREEN+"El usuario ya existe: "+user.getUsername()+Main.ANSI_RESET);
                    return true;
                }else {
                    System.out.println(Main.ANSI_RED+"El usuario no existe: "+user.getUsername()+Main.ANSI_RESET);
                    return false;
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public User getUserBD(User user) {
        String nombre = user.getUsername();
        String password = user.getPassword();
        String query = "SELECT * FROM users WHERE username LIKE '"+nombre+"'";
        PreparedStatement ps;
        try {
            ps = conectar().prepareStatement(query);
            ps.executeQuery();
            //cogemos el usuario de la peticion
            if (ps.getResultSet().next()) {
                user.setId(ps.getResultSet().getInt("idusername"));
                user.setUsername(ps.getResultSet().getString("username"));
                user.setPassword(ps.getResultSet().getString("password"));
                System.out.println(Main.ANSI_GREEN + "Usuario encontrado: " +user.getUsername()+ Main.ANSI_RESET);
                return user;
            }else {
                System.out.println(Main.ANSI_RED + "Usuario no encontrado: " +user.getUsername()+ Main.ANSI_RESET);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public UserConfig getUserConfigBD(User user) {
        int id = user.getId();
        String query = "SELECT * FROM config WHERE iduser = "+id;
        PreparedStatement ps;
        try {
            ps = conectar().prepareStatement(query);
            ps.executeQuery();
            //cogemos el usuario de la peticion
            if (ps.getResultSet().next()) {
                UserConfig userConfig = new UserConfig();
                userConfig.setUser(user);
                //userConfig.setProfileImage(ps.getResultSet().getBlob("img"));
                userConfig.setDivisa(ps.getResultSet().getString("divisa"));
                userConfig.setSaldo(ps.getResultSet().getDouble("saldo"));
                System.out.println(Main.ANSI_GREEN + "Configuración encontrada" + Main.ANSI_RESET);

                ps.close();
                return userConfig;
            }else {
                System.out.println(Main.ANSI_GREEN + "Configuración no encontrada" + Main.ANSI_RESET);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //metodo para insertar un usuario
    public void createUser(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String query = "INSERT INTO users (username, password) VALUES (?,?)";
        PreparedStatement ps;
        try {
            ps = conectar().prepareStatement(query);
            ps.setString(1, user.getUsername());
            ps.setString(2, bCryptPasswordEncoder.encode(user.getPassword()));
            ps.executeUpdate();
            System.out.println(Main.ANSI_GREEN+"Usuario creado: "+user.getUsername()+Main.ANSI_RESET);
            ps.close();
        } catch (SQLException e) {
            AlertView alertView = new AlertView("Error", "Error al crear el usuario", "Error al crear el usuario\n"+e.getMessage());
            alertView.mostrarAlerta();
            System.out.println(Main.ANSI_RED+"Error al crear el usuario"+Main.ANSI_RESET);
        }
    }
    //metodo para insertar la configuracion de un usuario y si no existe inserar una default
    public void createUserConfig(User user) {
        String query = "INSERT INTO config (iduser, divisa, saldo) VALUES (?,?,?)";
        PreparedStatement ps;
        try {
            ps = conectar().prepareStatement(query);
            ps.setInt(1, user.getId());
            ps.setDouble(2, 0.0);
            ps.setString(3, "EUR");
            ps.executeUpdate();
            System.out.println(Main.ANSI_GREEN+"Configuración creada"+Main.ANSI_RESET);
            ps.close();
        } catch (SQLException e) {
            AlertView alertView = new AlertView("Error", "Error al crear la configuración", "Error al crear la configuración\n"+e.getMessage());
            alertView.mostrarAlerta();
            e.printStackTrace();
        }
    }
    public ArrayList<Inversion> getInversionesBD(User user) {
        int id = user.getId();
        String query = "SELECT * FROM inversiones WHERE idusername = "+id;
        PreparedStatement ps;
        try {
            ps = conectar().prepareStatement(query);
            ps.executeQuery();
            //cogemos el usuario de la peticion
            ArrayList<Inversion> inversiones = new ArrayList<>();
            while (ps.getResultSet().next()) {
                Inversion inversion = new Inversion();
                inversion.setUser(user);
                inversion.setCrypto(cryptoController.getCoinByName(ps.getResultSet().getString("crypto")));
                inversion.setTransaccion(ps.getResultSet().getString("transaccion"));
                inversion.setFechaInversion(ps.getResultSet().getDate("fecha"));
                inversion.setPrecioCompraCrypto(ps.getResultSet().getDouble("precio_compra"));
                inversion.setImporteInversion(ps.getResultSet().getDouble("importe"));
                inversion.setTipo(ps.getResultSet().getString("tipo"));
                inversion.setDivisa(ps.getResultSet().getString("divisa"));
                inversion.setVendida(ps.getResultSet().getBoolean("vendida"));

                inversion.calcularCantidadCrypto();
                //añadimos la inversion a la lista
                inversiones.add(inversion);
            }
            System.out.println(Main.ANSI_GREEN + "Inversiones encontradas" + Main.ANSI_RESET);

            ps.close();
            return inversiones;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
