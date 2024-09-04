package Modelo;

import Controladora.Clientes;
import Controladora.Productos;
import Controladora.Venta;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VentasDao {

    Connection con;
    PreparedStatement pre;
    ResultSet res;
    ConexionMySql conexion = new ConexionMySql();
    int currentYear = LocalDate.now().getYear();

    public List<Venta> consultarVentasFechas(String[] filtro) {

        List<Venta> consultarVentas = new ArrayList();

        StringBuilder sqlfiltro = new StringBuilder("SELECT * FROM ventas WHERE 1=1");

        if (filtro[0] != null) {//fecha inicial

            sqlfiltro.append(" AND fecha >= ?");
        }
        if (filtro[1] != null) {//fecha final

            sqlfiltro.append(" AND fecha <= ?");

        }
        if (filtro[2] != null) {//mes

            sqlfiltro.append(" AND MONTH(fecha) = ?");

        }

        if (filtro[3] != null) {//año

            sqlfiltro.append(" AND YEAR(fecha) = ?");

        }
        if (filtro[4] != null) {//nombre cliente

            sqlfiltro.append(" AND nombrecliente = ?");

        }

        try {
            con = conexion.getConnection();
            pre = con.prepareStatement(sqlfiltro.toString());
            int index = 1;

            if (filtro[0] != null) {//fecha inicial

                pre.setString(index++, filtro[0]);
            }
            if (filtro[1] != null) {//fecha final

                pre.setString(index++, filtro[1]);

            }
            if (filtro[2] != null) {//mes

                pre.setString(index++, filtro[2]);

            }
            if (filtro[3] != null) {//año

                pre.setString(index++, filtro[3]);

            }
            if (filtro[4] != null) {//nombre cliente

                pre.setString(index++, filtro[4]);

            }

            res = pre.executeQuery();

            while (res.next()) {
                Venta consultas = new Venta();
                consultas.setIdentificacionCliente(res.getInt("nit"));
                consultas.setNombreCliente(res.getString("nombrecliente"));
                consultas.setDiasDePlazo(res.getString("plazopago"));
                consultas.setFechaActual(res.getDate("fecha"));
                consultas.setFechavencimiento(res.getDate("vencimiento"));
                consultas.setFactura(res.getString("factura"));
                consultas.setTipoCliente(res.getString("tipofactura"));
                consultas.setTotalCantidades(res.getInt("totalCantidades"));
                consultas.setTotalSubtotal(res.getInt("totalSubtotal"));
                consultas.setTotalIVA(res.getInt("totalIVA"));
                consultas.setTotalGeneral(res.getInt("totalGeneral"));
                consultas.setStatus(res.getString("estatus"));

                consultarVentas.add(consultas);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());

        } finally {

            try {
                con.close();
                pre.close();
                //res.close();
            } catch (SQLException e) {
                System.out.println(e.toString());

            }
        }

        return consultarVentas;

    }

    public String[][] consultarVentasGraficosCredito(String[] filtro) {

        List<String> meses = new ArrayList();
        List<String> totales_Credito = new ArrayList();
        String[][] matrizDatos = null;

        PreparedStatement pre2 = null;
        ResultSet res2 = null;

        Connection con = null;

        StringBuilder sqlfiltro2 = new StringBuilder("SELECT MONTH(fecha) AS mes, SUM(totalGeneral) AS total FROM ventas WHERE tipofactura = 'Credito' AND 1=1");

        if (filtro[0] != null) {//fecha inicial

            sqlfiltro2.append(" AND fecha >= ?");
        }
        if (filtro[1] != null) {//fecha final

            sqlfiltro2.append(" AND fecha <= ?");
        }
        if (filtro[2] != null) {//mes

            sqlfiltro2.append(" AND MONTH(fecha) = ?");
        }

        if (filtro[3] != null) {//año

            sqlfiltro2.append(" AND YEAR(fecha) = ?");
        }
        if (filtro[4] != null) {//nombre cliente

            sqlfiltro2.append(" AND nombrecliente = ?");
        }

        sqlfiltro2.append(" GROUP BY MONTH(fecha) ORDER BY mes");

        try {
            con = conexion.getConnection();

            pre2 = con.prepareStatement(sqlfiltro2.toString());

            int index2 = 1;

            if (filtro[0] != null) {//fecha inicial

                pre2.setString(index2++, filtro[0]);
            }
            if (filtro[1] != null) {//fecha final

                pre2.setString(index2++, filtro[1]);

            }
            if (filtro[2] != null) {//mes

                pre2.setString(index2++, filtro[2]);

            }
            if (filtro[3] != null) {//año

                pre2.setString(index2++, filtro[3]);

            }
            if (filtro[4] != null) {//nombre cliente

                pre2.setString(index2++, filtro[4]);

            }

            res2 = pre2.executeQuery();

            while (res2.next()) {

                meses.add(res2.getString("mes"));
                totales_Credito.add(res2.getString("total"));

            }

            matrizDatos = new String[meses.size()][2];

            for (int i = 0; i < meses.size(); i++) {

                matrizDatos[i][0] = meses.get(i);
                matrizDatos[i][1] = totales_Credito.get(i);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());

        } finally {

            try {

                if (pre2 != null) {
                    pre2.close();
                }

                if (res2 != null) {
                    res2.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                System.out.println(e.toString());

            }
        }

        return matrizDatos;

    }

    public String[][] consultarVentasGraficosContado(String[] filtro) {

        List<String> meses = new ArrayList();
        List<String> totales_Credito = new ArrayList();
        String[][] matrizDatos = null;

        PreparedStatement pre2 = null;
        ResultSet res2 = null;

        Connection con = null;

        StringBuilder sqlfiltro2 = new StringBuilder("SELECT MONTH(fecha) AS mes, SUM(totalGeneral) AS total FROM ventas WHERE tipofactura = 'Contado' AND 1=1");

        if (filtro[0] != null) {//fecha inicial

            sqlfiltro2.append(" AND fecha >= ?");
        }
        if (filtro[1] != null) {//fecha final

            sqlfiltro2.append(" AND fecha <= ?");
        }
        if (filtro[2] != null) {//mes

            sqlfiltro2.append(" AND MONTH(fecha) = ?");
        }

        if (filtro[3] != null) {//año

            sqlfiltro2.append(" AND YEAR(fecha) = ?");
        }
        if (filtro[4] != null) {//nombre cliente

            sqlfiltro2.append(" AND nombrecliente = ?");
        }

        sqlfiltro2.append(" GROUP BY MONTH(fecha) ORDER BY mes");

        try {
            con = conexion.getConnection();

            pre2 = con.prepareStatement(sqlfiltro2.toString());

            int index2 = 1;

            if (filtro[0] != null) {//fecha inicial

                pre2.setString(index2++, filtro[0]);
            }
            if (filtro[1] != null) {//fecha final

                pre2.setString(index2++, filtro[1]);

            }
            if (filtro[2] != null) {//mes

                pre2.setString(index2++, filtro[2]);

            }
            if (filtro[3] != null) {//año

                pre2.setString(index2++, filtro[3]);

            }
            if (filtro[4] != null) {//nombre cliente

                pre2.setString(index2++, filtro[4]);

            }

            res2 = pre2.executeQuery();

            while (res2.next()) {

                meses.add(res2.getString("mes"));
                totales_Credito.add(res2.getString("total"));

            }

            matrizDatos = new String[meses.size()][2];

            for (int i = 0; i < meses.size(); i++) {

                matrizDatos[i][0] = meses.get(i);
                matrizDatos[i][1] = totales_Credito.get(i);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());

        } finally {

            try {

                if (pre2 != null) {
                    pre2.close();
                }

                if (res2 != null) {
                    res2.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                System.out.println(e.toString());

            }
        }

        return matrizDatos;

    }

    public String[][] consultarVentasGraficosTotales(String[] filtro) {

        List<String> meses = new ArrayList();
        List<String> totales_Credito = new ArrayList();
        String[][] matrizDatos = null;

        PreparedStatement pre2 = null;
        ResultSet res2 = null;

        Connection con = null;

        StringBuilder sqlfiltro2 = new StringBuilder("SELECT MONTH(fecha) AS mes, SUM(totalGeneral) AS total FROM ventas WHERE 1=1");

        if (filtro[0] != null) {//fecha inicial

            sqlfiltro2.append(" AND fecha >= ?");
        }
        if (filtro[1] != null) {//fecha final

            sqlfiltro2.append(" AND fecha <= ?");
        }
        if (filtro[2] != null) {//mes

            sqlfiltro2.append(" AND MONTH(fecha) = ?");
        }

        if (filtro[3] != null) {//año

            sqlfiltro2.append(" AND YEAR(fecha) = ?");
        }
        if (filtro[4] != null) {//nombre cliente

            sqlfiltro2.append(" AND nombrecliente = ?");
        }

        sqlfiltro2.append(" GROUP BY MONTH(fecha) ORDER BY mes");

        try {
            con = conexion.getConnection();

            pre2 = con.prepareStatement(sqlfiltro2.toString());

            int index2 = 1;

            if (filtro[0] != null) {//fecha inicial

                pre2.setString(index2++, filtro[0]);
            }
            if (filtro[1] != null) {//fecha final

                pre2.setString(index2++, filtro[1]);

            }
            if (filtro[2] != null) {//mes

                pre2.setString(index2++, filtro[2]);

            }
            if (filtro[3] != null) {//año

                pre2.setString(index2++, filtro[3]);

            }
            if (filtro[4] != null) {//nombre cliente

                pre2.setString(index2++, filtro[4]);

            }

            res2 = pre2.executeQuery();

            while (res2.next()) {

                meses.add(res2.getString("mes"));
                totales_Credito.add(res2.getString("total"));

            }

            matrizDatos = new String[meses.size()][2];

            for (int i = 0; i < meses.size(); i++) {

                matrizDatos[i][0] = meses.get(i);
                matrizDatos[i][1] = totales_Credito.get(i);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());

        } finally {

            try {

                if (pre2 != null) {
                    pre2.close();
                }

                if (res2 != null) {
                    res2.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException e) {
                System.out.println(e.toString());

            }
        }

        return matrizDatos;

    }

    public String[][] creditos() {

        List<String> meses = new ArrayList();
        List<String> totales_Credito = new ArrayList();
        String[][] matrizDatos = null;

        PreparedStatement pre2 = null;
        ResultSet res2 = null;

        Connection con = null;

        String sql_totalCreditos = "SELECT MONTH(fecha) AS mes"
                + ", SUM(totalGeneral) AS total FROM ventas WHERE tipofactura = 'Credito' GROUP BY MONTH(fecha) "
                + "ORDER BY mes";

        try {
            con = conexion.getConnection();

            pre2 = con.prepareStatement(sql_totalCreditos);
            res2 = pre2.executeQuery();

            while (res2.next()) {

                meses.add(res2.getString("mes"));
                totales_Credito.add(res2.getString("total"));

            }

            matrizDatos = new String[meses.size()][2];

            for (int i = 0; i < meses.size(); i++) {

                matrizDatos[i][0] = meses.get(i);
                matrizDatos[i][1] = totales_Credito.get(i);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());

        } finally {

            try {

                if (res2 != null) {
                    res2.close();
                }

                if (pre2 != null) {
                    pre2.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());

            }
        }

        return matrizDatos;
    }

    public String[][] contados() {

        List<String> meses = new ArrayList();
        List<String> totales_Credito = new ArrayList();
        String[][] matrizDatos = null;

        PreparedStatement pre2 = null;
        ResultSet res2 = null;

        Connection con = null;

        String sql_totalCreditos = "SELECT MONTH(fecha) AS mes"
                + ", SUM(totalGeneral) AS total FROM ventas WHERE tipofactura = 'Contado' GROUP BY MONTH(fecha) "
                + "ORDER BY mes";

        try {
            con = conexion.getConnection();

            pre2 = con.prepareStatement(sql_totalCreditos);
            res2 = pre2.executeQuery();

            while (res2.next()) {

                meses.add(res2.getString("mes"));
                totales_Credito.add(res2.getString("total"));

            }

            matrizDatos = new String[meses.size()][2];

            for (int i = 0; i < meses.size(); i++) {

                matrizDatos[i][0] = meses.get(i);
                matrizDatos[i][1] = totales_Credito.get(i);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());

        } finally {

            try {

                if (res2 != null) {
                    res2.close();
                }

                if (pre2 != null) {
                    pre2.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());

            }
        }

        return matrizDatos;
    }

    public String[][] totalMensual() {

        List<String> meses = new ArrayList();
        List<String> totales_mes = new ArrayList();
        String[][] matrizDatos = null;

        PreparedStatement pre2 = null;

        ResultSet res2 = null;

        Connection con = null;

        String sql_total = "SELECT DISTINCT MONTH(fecha) AS mes"
                + ", SUM(totalGeneral) AS total FROM ventas GROUP BY MONTH(fecha) "
                + "ORDER BY mes";

        try {
            con = conexion.getConnection();

            pre2 = con.prepareStatement(sql_total);
            res2 = pre2.executeQuery();

            while (res2.next()) {

                meses.add(res2.getString("mes"));
                totales_mes.add(res2.getString("total"));

            }

            matrizDatos = new String[meses.size()][4];

            for (int i = 0; i < meses.size(); i++) {

                matrizDatos[i][0] = meses.get(i);
                matrizDatos[i][1] = totales_mes.get(i);

            }

        } catch (SQLException e) {
            System.out.println(e.toString());

        } finally {

            try {

                if (res2 != null) {
                    res2.close();
                }

                if (pre2 != null) {
                    pre2.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());

            }
        }

        return matrizDatos;

    }

    public List<String> clientesfacturados() {

        List<String> clientefacturado = new ArrayList();

        String sql = "SELECT DISTINCT nombrecliente AS clientes"
                + " FROM ventas ORDER BY clientes";

        try {
            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            res = pre.executeQuery();

            while (res.next()) {

                clientefacturado.add(res.getString("clientes"));

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

        return clientefacturado;

    }

    public List<String> totalyears() {

        List<String> years = new ArrayList();

        String sql = "SELECT DISTINCT YEAR(fecha) AS years_of_sale"
                + " FROM ventas ORDER BY years_of_sale";

        try {
            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            res = pre.executeQuery();

            while (res.next()) {

                years.add(res.getString("years_of_sale"));

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

        return years;

    }

    public List<Venta> consultarVentasActuales() {

        List<Venta> consultarVentas = new ArrayList();

        String sql = "SELECT * FROM ventas WHERE YEAR(fecha) = ?";

        try {
            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            pre.setInt(1, currentYear);
            res = pre.executeQuery();

            while (res.next()) {
                Venta consultas = new Venta();
                consultas.setIdentificacionCliente(res.getInt("nit"));
                consultas.setNombreCliente(res.getString("nombrecliente"));
                consultas.setDiasDePlazo(res.getString("plazopago"));
                consultas.setFechaActual(res.getDate("fecha"));
                consultas.setFechavencimiento(res.getDate("vencimiento"));
                consultas.setFactura(res.getString("factura"));
                consultas.setTipoCliente(res.getString("tipofactura"));
                consultas.setTotalCantidades(res.getInt("totalCantidades"));
                consultas.setTotalSubtotal(res.getInt("totalSubtotal"));
                consultas.setTotalIVA(res.getInt("totalIVA"));
                consultas.setTotalGeneral(res.getInt("totalGeneral"));
                consultas.setStatus(res.getString("estatus"));

                consultarVentas.add(consultas);
            }

        } catch (SQLException e) {
            System.out.println(e.toString());

        } finally {

            try {
                con.close();
                pre.close();
                //res.close();
            } catch (SQLException e) {
                System.out.println(e.toString());

            }
        }

        return consultarVentas;

    }

    public Clientes consultarCliente(int nit) {

        Clientes cliente = new Clientes();
        String sql = "SELECT * FROM clientes WHERE Nit = ?";

        try {

            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            pre.setInt(1, nit);
            res = pre.executeQuery();

            while (res.next()) {

                cliente.setIdentificacionCliente(res.getInt("Nit"));
                cliente.setNombreCliente(res.getString("Nombre"));
                cliente.setTipoCliente(res.getString("TipoCredito"));
                cliente.setDiasDePlazo(res.getString("DiasPlazo"));
                cliente.setCorreoCliente(res.getString("Correo"));
                cliente.setContactoCliente(res.getLong("Contacto"));
                cliente.setDireccionCliente(res.getString("Direccion"));

            }

        } catch (SQLException e) {

            System.out.println(e.toString());

        }

        return cliente;
    }

    public Productos consultarProducto(int codigo) {

        Productos producto = new Productos();
        String sql = "SELECT * FROM productos WHERE codigo = ?";

        try {

            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            pre.setInt(1, codigo);
            res = pre.executeQuery();

            while (res.next()) {

                producto.setCodigoProducto(res.getInt("codigo"));
                producto.setNombreProducto(res.getString("nombre"));
                producto.setInventario(res.getInt("inventario"));
                producto.setPrecioProducto(res.getInt("precio"));
                producto.setImpuestoProducto(res.getString("impuesto"));

            }

        } catch (SQLException e) {

            System.out.println(e.toString());

        }

        return producto;
    }

    public void ingresarVenta(Venta venta, List<Venta> productos, Clientes cliente) {

        PreparedStatement prefactura = null;
        PreparedStatement preproductos = null;

        String insertarfacturas = "INSERT INTO ventas "
                + "(nit,nombrecliente,plazopago, correo, contacto, direccion,fecha,"
                + "vencimiento,factura,tipofactura, totalCantidades,totalSubtotal,totalIVA ,"
                + "totalGeneral,estatus,fechadepago,saldofactura) "//,estatus,fechadepago,diasdemora
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String insertarproductos = "INSERT INTO detalleventas "
                + "(item,codigo,producto,cantidades, precio, subtotal, iva, total,id_Venta) "
                + "VALUES (?,?,?,?,?,?,?,?,?)";

        try {
            con = conexion.getConnection();
            con.setAutoCommit(false);//iniciar transccion;   
            prefactura = con.prepareStatement(insertarfacturas, Statement.RETURN_GENERATED_KEYS);
            prefactura.setInt(1, cliente.getIdentificacionCliente());
            prefactura.setString(2, cliente.getNombreCliente());
            prefactura.setString(3, cliente.getDiasDePlazo());
            prefactura.setString(4, cliente.getCorreoCliente());
            prefactura.setLong(5, cliente.getContactoCliente());
            prefactura.setString(6, cliente.getDireccionCliente());
            prefactura.setDate(7, (Date) venta.getFechaActual());
            prefactura.setDate(8, (Date) venta.getFechavencimiento());
            prefactura.setString(9, venta.getFactura());
            prefactura.setString(10, cliente.getTipoCliente());
            prefactura.setInt(11, venta.getTotalCantidades());
            prefactura.setInt(12, venta.getTotalSubtotal());
            prefactura.setInt(13, venta.getTotalIVA());
            prefactura.setInt(14, venta.getTotalGeneral());

            if (cliente.getTipoCliente().equals("Contado")) {

                prefactura.setString(15, "pagado");
                prefactura.setString(16, venta.getFechaActual().toString());
                prefactura.setInt(17, venta.getAbonoscredito());

            } else if (cliente.getTipoCliente().equals("Credito")) {

                if (venta.getAbonoscredito() == 0) {

                    prefactura.setString(15, "pagado");
                    prefactura.setString(16, venta.getFechaActual().toString());
                    prefactura.setInt(17, venta.getAbonoscredito());

                } else {

                    prefactura.setString(15, "pendiente");
                    prefactura.setString(16, "pendiente");
                    prefactura.setInt(17, venta.getAbonoscredito());

                }

            }

            prefactura.executeUpdate();

            int idFactura;
            ResultSet rs2 = prefactura.getGeneratedKeys();
            idFactura = 0;
            if (rs2.next()) {

                idFactura = rs2.getInt(1);
            }

            preproductos = con.prepareStatement(insertarproductos);
            for (Venta producto : productos) {

                preproductos.setInt(1, producto.getItem());
                preproductos.setInt(2, producto.getCodigo());
                preproductos.setString(3, producto.getProducto());
                preproductos.setInt(4, producto.getCantidad());
                preproductos.setInt(5, producto.getPrecio());
                preproductos.setInt(6, producto.getSubtotal());
                preproductos.setInt(7, producto.getValorIva());
                preproductos.setInt(8, producto.getTotal());
                preproductos.setInt(9, idFactura);
                preproductos.addBatch();

            }

            preproductos.executeBatch();

            con.commit();

        } catch (SQLException e) {
            System.out.println(e.toString());
            if (con != null) {
                try {
                    con.rollback(); // Revertir transacción en caso de error
                } catch (SQLException ex) {
                    System.out.println(ex.toString());
                }
            }
        } finally {
            if (prefactura != null) {
                try {
                    prefactura.close();
                } catch (SQLException e) {
                    System.out.println(e.toString());
                }
            }
            if (preproductos != null) {
                try {

                    preproductos.close();
                } catch (SQLException e) {
                    System.out.println(e.toString());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.out.println(e.toString());
                }
            }
        }
    }

    public Venta consultarfactura() {

        Venta factura = new Venta();
        String sql = "SELECT * FROM ventas ORDER BY id_Venta DESC LIMIT 1";

        try {
            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            res = pre.executeQuery();

            while (res.next()) {

                factura.setFactura(res.getString("factura"));

            }

        } catch (SQLException e) {
            System.out.println(e.toString());

        } finally {

            try {
                con.close();
                pre.close();
                //res.close();
            } catch (SQLException e) {
                System.out.println(e.toString());

            }
        }

        return factura;

    }

    public List<Venta> validarFacturasVencidas(int nit) {

        List<Venta> consultar = new ArrayList();

        String sqlnit = "SELECT * FROM ventas WHERE nit = ? AND tipofactura = 'Credito'";

        try {
            con = conexion.getConnection();
            pre = con.prepareStatement(sqlnit);
            pre.setInt(1, nit);
            res = pre.executeQuery();

            while (res.next()) {
                Venta facturasvencidas = new Venta();
                facturasvencidas.setId_venta(res.getInt("id_Venta"));
                facturasvencidas.setIdentificacionCliente(res.getInt("nit"));
                facturasvencidas.setNombreCliente(res.getString("nombrecliente"));
                facturasvencidas.setDiasDePlazo(res.getString("plazopago"));
                facturasvencidas.setCorreoCliente(res.getString("correo"));
                facturasvencidas.setContactoCliente(res.getLong("contacto"));
                facturasvencidas.setDireccionCliente(res.getString("direccion"));
                facturasvencidas.setFechaActual(res.getDate("fecha"));
                facturasvencidas.setFechavencimiento(res.getDate("vencimiento"));
                facturasvencidas.setFactura(res.getString("factura"));
                facturasvencidas.setTipoCliente(res.getString("tipofactura"));
                facturasvencidas.setTotalCantidades(res.getInt("totalCantidades"));
                facturasvencidas.setTotalSubtotal(res.getInt("totalSubtotal"));
                facturasvencidas.setTotalIVA(res.getInt("totalIVA"));
                facturasvencidas.setTotalGeneral(res.getInt("totalGeneral"));
                facturasvencidas.setStatus(res.getString("estatus"));

                consultar.add(facturasvencidas);

            }

        } catch (SQLException e) {

            System.out.println(e.toString());
        }

        return consultar;

    }

    public void cambiarEstadoFacturasCredito(List<Venta> facturas_pagadas, String fechaActual) {

        String sql = "UPDATE ventas SET estatus = ?,fechadepago = ?, saldofactura = ? WHERE id_Venta = ?";

        try {

            con = conexion.getConnection();
            pre = con.prepareStatement(sql);

            for (Venta recorrer_facturas : facturas_pagadas) {

                pre.setInt(4, recorrer_facturas.getId_venta());
                pre.setString(1, "pagado");
                pre.setString(2, fechaActual);
                pre.setString(3, "0");
                pre.addBatch();

            }

            pre.executeBatch();

        } catch (SQLException e) {

            System.out.println(e.toString());

        } finally {

            try {
                if (pre != null) {
                    pre.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }

    }

    public ObservableList<Venta> estados_de_Cuenta(String nit, String fechaInicial, String fechaFinal) {

        ObservableList<Venta> consultar = FXCollections.observableArrayList();

        String sql = "SELECT * FROM ventas WHERE (nit = ? AND fecha >= ? AND fecha <= ?)";

        try {
            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            pre.setString(1, nit);
            pre.setString(2, fechaInicial);
            pre.setString(3, fechaFinal);
            res = pre.executeQuery();

            while (res.next()) {
                Venta ventasCliente = new Venta();
                ventasCliente.setId_venta(res.getInt("id_Venta"));
                ventasCliente.setIdentificacionCliente(res.getInt("nit"));
                ventasCliente.setNombreCliente(res.getString("nombrecliente"));
                ventasCliente.setDiasDePlazo(res.getString("plazopago"));
                ventasCliente.setCorreoCliente(res.getString("correo"));
                ventasCliente.setContactoCliente(res.getLong("contacto"));
                ventasCliente.setDireccionCliente(res.getString("direccion"));
                ventasCliente.setFechaActual(res.getDate("fecha"));
                ventasCliente.setFechavencimiento(res.getDate("vencimiento"));
                ventasCliente.setFactura(res.getString("factura"));
                ventasCliente.setTipoCliente(res.getString("tipofactura"));
                ventasCliente.setTotalCantidades(res.getInt("totalCantidades"));
                ventasCliente.setTotalSubtotal(res.getInt("totalSubtotal"));
                ventasCliente.setTotalIVA(res.getInt("totalIVA"));
                ventasCliente.setTotalGeneral(res.getInt("totalGeneral"));
                ventasCliente.setStatus(res.getString("estatus"));
                ventasCliente.setFechdepago(res.getString("fechadepago"));
                ventasCliente.setSaldofactura(res.getString("saldofactura"));

                consultar.add(ventasCliente);

            }

        } catch (SQLException e) {

            System.out.println(e.toString());
        }

        return consultar;

    }

    public ObservableList<Venta> consultaDetalle(int id) {

        ObservableList<Venta> consultar = FXCollections.observableArrayList();

        String sql = "SELECT pro.id_ventaD, pro.item, pro.codigo, pro.producto, pro.cantidades, pro.precio, pro.subtotal, pro.iva, pro.total "
                + "FROM detalleventas pro "
                + "JOIN ventas vent ON pro.id_Venta = vent.id_Venta "
                + "WHERE vent.id_Venta = ?";

        try {
            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            pre.setInt(1, id);
            res = pre.executeQuery();

            while (res.next()) {
                Venta detallfactura = new Venta();
                detallfactura.setId_venta(res.getInt("id_VentaD"));
                detallfactura.setItem(res.getInt("item"));
                detallfactura.setCodigo(res.getInt("codigo"));
                detallfactura.setProducto(res.getString("producto"));
                detallfactura.setCantidad(res.getInt("cantidades"));
                detallfactura.setPrecio(res.getInt("precio"));
                detallfactura.setSubtotal(res.getInt("subtotal"));
                detallfactura.setValorIva(res.getInt("iva"));
                detallfactura.setTotal(res.getInt("total"));
                consultar.add(detallfactura);

            }

        } catch (SQLException e) {
        } finally {
            // Cerrar recursos
            try {
                if (res != null) {
                    res.close();
                }
                if (pre != null) {
                    pre.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            }
        }

        return consultar;

    }

}
