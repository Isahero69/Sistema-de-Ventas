package Modelo;

import Controladora.Clientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientesDao {

    Connection con;
    PreparedStatement pre;
    ResultSet resul;
    ConexionMySql conexion = new ConexionMySql();

    //Llamar los clientes al cargar la tabla clientes
    public ObservableList<Clientes> cliente() {

        ObservableList<Clientes> llamarClientes = FXCollections.observableArrayList();

        String sql = "SELECT * FROM clientes";

        try {

            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            resul = pre.executeQuery(sql);

            while (resul.next()) {

                Clientes cliente = new Clientes();
                cliente.setId_Cliente(resul.getInt("id_Clientes"));
                cliente.setIdentificacionCliente(resul.getInt("Nit"));
                cliente.setNombreCliente(resul.getString("Nombre"));
                cliente.setContactoCliente(resul.getLong("Contacto"));
                cliente.setCorreoCliente(resul.getString("Correo"));
                cliente.setDireccionCliente(resul.getString("Direccion"));
                cliente.setNaturalezaCliente(resul.getString("Naturaleza"));
                cliente.setTipoCliente(resul.getString("TipoCredito"));
                cliente.setDiasDePlazo(resul.getString("DiasPlazo"));
                llamarClientes.add(cliente);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return llamarClientes;
    }

    //inyectamos nuevos registros de clientes nuevos
    public boolean crearCliente(Clientes cliente) {

        String sql = "INSERT INTO clientes (Nit,Nombre,Contacto,Correo,Direccion,Naturaleza"
                + ",TipoCredito,DiasPlazo) VALUES (?,?,?,?,?,?,?,?)";

        try {
            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            pre.setInt(1, cliente.getIdentificacionCliente());
            pre.setString(2, cliente.getNombreCliente());
            pre.setLong(3, cliente.getContactoCliente());
            pre.setString(4, cliente.getCorreoCliente());
            pre.setString(5, cliente.getDireccionCliente());
            pre.setString(6, cliente.getNaturalezaCliente());
            pre.setString(7, cliente.getTipoCliente());
            pre.setString(8, cliente.getDiasDePlazo());
            pre.execute();

            return true;

        } catch (SQLException e) {

            System.out.println(e.toString());
        } finally {

            try {

                pre.close();
            } catch (SQLException e) {

                System.out.println(e.toString());

            }

        }

        return false;

    }

    //editar un cliente seleccionado
    public boolean editarCliente(Clientes cliente) {

        String sql = "UPDATE clientes SET Nit = ?,"
                + " Nombre=?,"
                + "Contacto = ?,"
                + "Correo = ?,"
                + "Direccion = ?,"
                + "Naturaleza = ?,"
                + "TipoCredito = ?, "
                + "DiasPlazo = ? "
                + "WHERE id_Clientes = ?";

        try {

            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            pre.setInt(1, cliente.getIdentificacionCliente());
            pre.setString(2, cliente.getNombreCliente());
            pre.setLong(3, cliente.getContactoCliente());
            pre.setString(4, cliente.getCorreoCliente());
            pre.setString(5, cliente.getDireccionCliente());
            pre.setString(6, cliente.getNaturalezaCliente());
            pre.setString(7, cliente.getTipoCliente());
            pre.setString(8, cliente.getDiasDePlazo());
            pre.setInt(9, cliente.getId_Cliente());
            pre.execute();
            return true;

        } catch (SQLException e) {

            System.out.println(e.toString());
            return false;

        } finally {

            try {
                con.close();
                pre.close();
            } catch (SQLException e) {
                System.out.println(e.toString());

            }
        }

    }

    //Eliminar registros de clientes
    public boolean eliminarCliente(int id) {

        String sql = "DELETE FROM clientes WHERE id_Clientes = ?";

        try {

            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            pre.setInt(1, id);
            pre.execute();
            return true;

        } catch (SQLException e) {

            System.out.println(e.toString());

        } finally {

            try {

                con.close();
                pre.close();

            } catch (SQLException e) {

                System.out.println(e.toString());

            }
        }

        return false;

    }

   

}
