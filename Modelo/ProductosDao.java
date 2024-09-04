package Modelo;


import Controladora.Productos;
import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProductosDao {

    Connection con;
    PreparedStatement pre;
    ResultSet res;
    ConexionMySql conexion = new ConexionMySql();

    public boolean crearProducto(Productos producto) {

        String sql = "INSERT INTO productos (codigo,nombre,inventario,precio,impuesto,"
                + "presentacion,marca,tipoProducto) VALUES (?,?,?,?,?,?,?,?)";

        try {
            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            pre.setInt(1, producto.getCodigoProducto());
            pre.setString(2, producto.getNombreProducto());
            pre.setInt(3, producto.getInventario());
            pre.setInt(4, (int) producto.getPrecioProducto());
            pre.setString(5, producto.getImpuestoProducto());
            pre.setString(6, producto.getPresentacionProducto());
            pre.setString(7, producto.getMarcaProducto());
            pre.setString(8, producto.getTipoProducto());
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

    public ObservableList<Productos> cargaProductos() {

        ObservableList<Productos> almacenar = FXCollections.observableArrayList();
        String sql = "SELECT * FROM productos";

        try {
            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            res = pre.executeQuery();

            while (res.next()) {

                Productos leer = new Productos();
                leer.setId_Producto(res.getInt("id_Producto"));
                leer.setCodigoProducto(res.getInt("codigo"));
                leer.setNombreProducto(res.getString("nombre"));
                leer.setInventario(res.getInt("inventario"));
                leer.setPrecioProducto(res.getInt("precio"));
                leer.setImpuestoProducto(res.getString("impuesto"));
                leer.setPresentacionProducto(res.getString("presentacion"));
                leer.setMarcaProducto(res.getString("marca"));
                leer.setTipoProducto(res.getString("tipoProducto"));
                almacenar.add(leer);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());

        } finally {

            try {
                con.close();
                pre.close();
                res.close();
            } catch (SQLException e) {
                System.out.println(e.toString());

            }
        }

        return almacenar;

    }

    public boolean eliminarProducto(int id) {

        String sql = "DELETE FROM  productos WHERE id_Producto = ?";

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

    public boolean editarProducto(Productos producto) {

        String sql = "UPDATE productos SET codigo = ?,"
                + "nombre =?,"
                + "inventario = ?,"
                + "precio = ?,"
                + "impuesto = ?,"
                + "presentacion = ?,"
                + "marca = ?, "
                + "tipoProducto = ? "
                + "WHERE id_Producto = ?";

        try {

            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            pre.setInt(1, producto.getCodigoProducto());
            pre.setString(2, producto.getNombreProducto());
            pre.setInt(3, producto.getInventario());
            pre.setInt(4, (int) producto.getPrecioProducto());
            pre.setString(5, producto.getImpuestoProducto());
            pre.setString(6, producto.getPresentacionProducto());
            pre.setString(7, producto.getMarcaProducto());
            pre.setString(8, producto.getTipoProducto());
            pre.setInt(9, producto.getId_Producto());
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

}
