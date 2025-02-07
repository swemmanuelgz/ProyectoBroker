package database;

import com.example.proyectobroker.Main;
import com.example.proyectobroker.controller.CryptoController;
import com.example.proyectobroker.model.Inversion;
import com.example.proyectobroker.model.User;
import com.example.proyectobroker.model.UserConfig;
import com.example.proyectobroker.view.AlertView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public class ConnectMysql {
    //esta clase es para simular una conexión a una base de datos jdbc
    //private final String URL = "jdbc:mysql://localhost:3307/bdbrokeremmanuel";
   // private final String username ="root";
   // private final String password = "";
   // private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:sqlite:src/main/java/database/bdbrokeremmanuel.db";

    private CryptoController cryptoController = new CryptoController();

    public ConnectMysql() {

    }
    public void verificarConexion() {
        try (Connection con = conectar()) {
            if (con != null) {
                System.out.println("✅ Conexión establecida con SQLite.");

                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("PRAGMA database_list;");

                while (rs.next()) {
                    System.out.println("📂 Base de datos en uso: " + rs.getString("file"));
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar a la base de datos: " + e.getMessage());
        }

        File dbFile = new File("bdbrokeremmanuel.db");
        System.out.println("Ruta esperada: " + dbFile.getAbsolutePath());

        if (!dbFile.exists()) {
            System.out.println("⚠️ El archivo de la base de datos NO existe en esa ruta.");
        } else {
            System.out.println("✅ El archivo de la base de datos sí existe.");
        }
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
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("✅ Usuario encontrado: " + nombre);
                return true;
            } else {
                System.out.println("❌ Usuario no encontrado: " + nombre);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error en checkUserExists: " + e.getMessage());
        }
        return false;
    }

    public User getUserBD(User user) {
        String nombre = user.getUsername();
        // Utilizamos un parámetro en lugar de concatenar la cadena para evitar inyección SQL
        String query = "SELECT * FROM users WHERE username LIKE ?";
        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, nombre);  // Asigna el valor al parámetro

            // Ejecuta la consulta y guarda el ResultSet en una variable
            try (ResultSet rs = ps.executeQuery()) {
                // Si hay resultados, extrae los datos del usuario
                if (rs.next()) {
                    user.setId(rs.getInt("idusername"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    System.out.println(Main.ANSI_GREEN + "Usuario encontrado: " + user.getUsername() + Main.ANSI_RESET);
                    return user;
                } else {
                    System.out.println(Main.ANSI_RED + "Usuario no encontrado: " + user.getUsername() + Main.ANSI_RESET);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public UserConfig getUserConfigBD(User user) {
        int id = user.getId();
        String query = "SELECT * FROM config WHERE iduser = ?";

        try (Connection con = conectar();
             PreparedStatement ps = con.prepareStatement(query)) {

            // Asigna el parámetro
            ps.setInt(1, id);

            // Ejecuta la consulta y almacena el ResultSet
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserConfig userConfig = new UserConfig();
                    userConfig.setUser(user);
                    // Si en algún momento deseas recuperar la imagen, descomenta la siguiente línea:
                    // userConfig.setProfileImage(rs.getBlob("img"));
                    userConfig.setDivisa(rs.getString("divisa"));
                    userConfig.setSaldo(rs.getDouble("saldo"));
                    System.out.println(Main.ANSI_GREEN + "Configuración encontrada" + Main.ANSI_RESET);
                    return userConfig;
                } else {
                    System.out.println(Main.ANSI_GREEN + "Configuración no encontrada" + Main.ANSI_RESET);
                }
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
