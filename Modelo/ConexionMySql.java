package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySql {

    Connection con;

    public Connection getConnection() {

        String ruta = "jdbc:mysql://localhost:3306/sistemadeventas?serverTimezone=UTC";
        String user = "root";
        String pass = "";

        try {
            con = DriverManager.getConnection(ruta, user, pass);
            return con;
        } catch (SQLException ex) {
            System.out.println(ex.toString());

        }

        return null;

    }

}
