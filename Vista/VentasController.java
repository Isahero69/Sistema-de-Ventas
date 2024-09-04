package Vista;

import Controladora.Clientes;
import Controladora.Productos;
import Controladora.Venta;
import Modelo.VentasDao;
import Pagos.ListadoFacturasVencidasController;
import Pagos.OpcionesFacturasVencidasController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class VentasController implements Initializable {

    VentasDao buscar = new VentasDao();
    Productos proBuscado = new Productos();
    Clientes cliente = new Clientes();
    ObservableList<Venta> ventas = FXCollections.observableArrayList();
    ObservableList<Venta> facturas = FXCollections.observableArrayList();
    int item = 0;
    int iniciofact = 0;
    Venta numerofactura = new Venta();
    String prefijo = "PAT-";
    VentasDao ingresarVenta = new VentasDao();
    VentasDao validarfecha = new VentasDao();
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance();
    LocalDate fechaActual = LocalDate.now();
    DateTimeFormatter formatofecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private int precioGlobal, subtotalGlobal, valorIvaGlobal, totalGeneralGlobal, diferenciaGlobal;

    @FXML
    private TextField buscarNIT;
    @FXML
    private TextField ingresarcodigo;
    @FXML
    private TableView<Venta> tablaventa;
    @FXML
    private Label subtotal;
    @FXML
    private Label totaliva;
    @FXML
    private Label totalventa;
    @FXML
    private Button vender;
    @FXML
    private Button borrardatos;
    @FXML
    private Button regresar;
    @FXML
    private TextField nombrecliente;
    @FXML
    private TextField tipocliente;
    @FXML
    private TextField plazodepago;
    @FXML
    private TextField producto;
    @FXML
    private TextField inventario;
    @FXML
    private TextField precio;
    @FXML
    private TextField iva;
    @FXML
    private TextField cantidad;
    @FXML
    private TableColumn<Venta, String> item_Col;
    @FXML
    private TableColumn<Venta, String> codigo_Col;
    @FXML
    private TableColumn<Venta, String> producto_Col;
    @FXML
    private TableColumn<Venta, String> cantidades_Col;
    @FXML
    private TableColumn<Venta, String> precio_Col;
    @FXML
    private TableColumn<Venta, String> subtotal_Col;
    @FXML
    private TableColumn<Venta, String> iva_Col;
    @FXML
    private TableColumn<Venta, String> total_Col;
    @FXML
    private Button ingresarregistro;
    @FXML
    private Label subtotal1;
    @FXML
    private Label totalcantidades;
    @FXML
    private Label totalsubtotal;
    private Label totalgeneral;
    @FXML
    private TextField correo;
    @FXML
    private TextField telefono;
    @FXML
    private TextField direccion;
    @FXML
    private Label factura;
    @FXML
    private TextField pagado;
    @FXML
    private TextField valorfactura;
    @FXML
    private TextField diferencia;
    @FXML
    private Button abono;
    @FXML
    private Button ajustealpeso;
    @FXML
    private TextField ajustado;
    @FXML
    private Label txtdiferencia;
    @FXML
    private Label txtvencimiento;
    private Label venci;
    @FXML
    private Button EliminarItem;
    @FXML
    private Button EditarCantidades;
    @FXML
    private Label txtadvertencia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Label mesaje = new Label("Sin Productos Seleccionados");
        mesaje.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: gray;");
        tablaventa.setPlaceholder(mesaje);
        ingresarcodigo.setDisable(true);
        cantidad.setDisable(true);
        ingresarregistro.setDisable(true);
        vender.setDisable(true);
        abono.setDisable(true);
        pagado.setDisable(true);
        ajustealpeso.setDisable(true);
        ajustado.setDisable(true);
        EliminarItem.setDisable(true);
        EditarCantidades.setDisable(true);
        borrardatos.setDisable(true);
        facturas.clear();
        factura.setText(String.valueOf(consecutivofactura()));

    }

    @FXML
    private void ingresarcantidades(KeyEvent event) {
    }

    @FXML
    private void borrardatos(ActionEvent event) {
        borrar();
    }

    public double setearNumero(String valor) {

        String valorInicial = valor;
        double formatoFinal = 0;

        DecimalFormatSymbols simbolo = new DecimalFormatSymbols(Locale.GERMANY);
        simbolo.setCurrencySymbol("");

        DecimalFormat formato = new DecimalFormat("#,##0.00", simbolo);
        formato.setParseBigDecimal(true);

        try {

            Number numero = formato.parse(valorInicial.replaceAll("[^\\d,\\.]", ""));
            formatoFinal = numero.doubleValue();

        } catch (ParseException e) {

            System.out.println(e.toString());
        }

        return formatoFinal;
    }

    @FXML
    private void vender(ActionEvent event) {

        StringBuilder conca = new StringBuilder();
        String texto = diferencia.getText();
        String facturaConse = consecutivofactura();
        String tipoCliente = tipocliente.getText();

        if (texto == null || texto.isEmpty()) {

            int i = JOptionPane.showConfirmDialog(null, "¿Esta Seguro de Finalizar la  Venta?", "Finalizar Venta", JOptionPane.INFORMATION_MESSAGE);
            if (i == 0) {

                ingresarVenta.ingresarVenta(datosTotales(), ventas, datosClientesFactura());
                borrar();
                JOptionPane.showMessageDialog(null, "Venta Finalizada con Numero de Factura " + facturaConse);

                String valorfactura[] = facturaConse.split("-");
                int aumento = Integer.parseInt(valorfactura[1]);
                String nuevaFact = conca.append("PAT-").append(aumento + 1).toString();
                factura.setText(nuevaFact);

            }

        } else if (!texto.equals("")) {

            int validardif = (int) setearNumero(diferencia.getText());

            if (tipoCliente.equals("Credito")) {

                if (validardif > 0 || validardif < 0) {

                    int i = JOptionPane.showConfirmDialog(null, "Factura queda con Saldo Pendiente por pagar de " + formatoMoneda.format(validardif) + ". ¿Deseas Finalizar la Venta?", "Saldo Pendiente por Pagar", JOptionPane.INFORMATION_MESSAGE);

                    if (i == 0) {

                        ingresarVenta.ingresarVenta(datosTotales(), ventas, datosClientesFactura());
                        borrar();
                        JOptionPane.showMessageDialog(null, "Venta Finalizada con Numero de Factura " + facturaConse, "Venta Finalizada", JOptionPane.INFORMATION_MESSAGE);

                        String valorfactura[] = facturaConse.split("-");
                        int aumento = Integer.parseInt(valorfactura[1]);
                        String nuevaFact = conca.append("PAT-").append(aumento + 1).toString();
                        factura.setText(nuevaFact);
                    }

                } else {

                    int i = JOptionPane.showConfirmDialog(null, "¿Esta Seguro de Finalizar la  Venta?", "Finalizar Venta", JOptionPane.INFORMATION_MESSAGE);
                    if (i == 0) {

                        ingresarVenta.ingresarVenta(datosTotales(), ventas, datosClientesFactura());
                        borrar();
                        JOptionPane.showMessageDialog(null, "Venta Finalizada con Numero de Factura " + facturaConse + " ", "Venta Finalizada", JOptionPane.INFORMATION_MESSAGE);

                        String valorfactura[] = facturaConse.split("-");
                        int aumento = Integer.parseInt(valorfactura[1]);
                        String nuevaFact = conca.append("PAT-").append(aumento + 1).toString();
                        factura.setText(nuevaFact);
                    }
                }

            } else {

                if (validardif > 0 || validardif < 0) {

                    JOptionPane.showMessageDialog(null, "Diferencia " + formatoMoneda.format(validardif) + " Pendiente por Gestionar", "Valide Diferencia", JOptionPane.INFORMATION_MESSAGE);

                } else {

                    int i = JOptionPane.showConfirmDialog(null, "¿Esta Seguro de Finalizar la  Venta?", "Finalizar Venta", JOptionPane.INFORMATION_MESSAGE);
                    if (i == 0) {

                        ingresarVenta.ingresarVenta(datosTotales(), ventas, datosClientesFactura());
                        borrar();
                        JOptionPane.showMessageDialog(null, "Venta Finalizada con Numero de Factura " + facturaConse, "Venta Finalizada", JOptionPane.INFORMATION_MESSAGE);

                        String valorfactura[] = facturaConse.split("-");
                        int aumento = Integer.parseInt(valorfactura[1]);
                        String nuevaFact = conca.append("PAT-").append(aumento + 1).toString();
                        factura.setText(nuevaFact);
                    }
                }

            }

        }

    }

    public void borrar() {

        buscarNIT.setText("");
        nombrecliente.setText("");
        tipocliente.setText("");
        plazodepago.setText("");
        ingresarcodigo.setText("");
        producto.setText("");
        inventario.setText("");
        precio.setText("");
        iva.setText("");
        cantidad.setText("");
        totalcantidades.setText("");
        totalsubtotal.setText("");
        totaliva.setText("");
        valorfactura.setText("");
        diferencia.setText("");
        pagado.setText("");
        ajustado.setText("");
        txtvencimiento.setText("");
        ventas.clear();
        item = 0;
        ingresarcodigo.setDisable(true);
        cantidad.setDisable(true);
        ingresarregistro.setDisable(true);
        buscarNIT.setDisable(false);
        ajustealpeso.setDisable(true);
        borrardatos.setDisable(true);
        vender.setDisable(true);
        pagado.setDisable(true);
        abono.setDisable(true);
        EliminarItem.setDisable(true);
        EditarCantidades.setDisable(true);
        buscarNIT.requestFocus();

    }

    public String consecutivofactura() {

        String nuevoNumero = null;
        String numero = ingresarVenta.consultarfactura().getFactura();

        if (numero == null) {

            StringBuilder concatenar = new StringBuilder();
            String numFactura = concatenar.append(prefijo).append(numerofactura.getNumeroFactura() + 1).toString();
            factura.setText(numFactura);

        } else {

            StringBuilder concatenar = new StringBuilder();
            String array[] = numero.split("-");
            int pos = Integer.parseInt(array[1]);

            nuevoNumero = concatenar.append(prefijo).append(pos + 1).toString();

        }

        return nuevoNumero;

    }

    public Venta datosTotales() {

        Venta datos = new Venta();
        Date fechaActualSql = Date.valueOf(fechaActual);

        datos.setFactura(factura.getText());
        datos.setTotalCantidades(Integer.parseInt(totalcantidades.getText()));
        datos.setTotalSubtotal(subtotalGlobal);
        datos.setTotalIVA(valorIvaGlobal);
        datos.setTotalGeneral(totalGeneralGlobal);

        if (tipocliente.getText().equals("Credito")) {

            String validarpagado = pagado.getText();
            String validarfactura = valorfactura.getText();

            if (validarpagado.isEmpty() && !validarfactura.isEmpty()) {

                datos.setAbonoscredito(totalGeneralGlobal);

            } else if (!validarpagado.isEmpty() && !validarfactura.isEmpty()) {

                datos.setAbonoscredito(diferenciaGlobal);

            }

        } else {

            datos.setAbonoscredito(diferenciaGlobal);
        }

        if (txtvencimiento.getText().equals("PAGO INMEDIATO")) {

            datos.setFechavencimiento(fechaActualSql);//fecha actual si es contado

        } else {

            datos.setFechavencimiento(fechaSql());//fecha de vencimiento si es credito
        }

        datos.setFechaActual(fechaActualSql);

        return datos;
    }

    public Clientes datosClientesFactura() {

        Clientes datos = new Clientes();

        datos.setIdentificacionCliente(Integer.parseInt(buscarNIT.getText()));
        datos.setNombreCliente(nombrecliente.getText());
        datos.setDiasDePlazo(plazodepago.getText());
        datos.setTipoCliente(tipocliente.getText());
        datos.setCorreoCliente(correo.getText());
        datos.setContactoCliente(Long.parseLong(telefono.getText()));
        datos.setDireccionCliente(direccion.getText());
        return datos;

    }

    @FXML
    private void regresar(ActionEvent event) throws IOException {

        ((Node) (event.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource("/Vista/MenuPrincipal.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    public void cerrarventana(ActionEvent event) throws IOException {

        ((Node) (event.getSource())).getScene().getWindow().hide();

       
    }

    public Date fechaSql() {

        String fechareal = txtvencimiento.getText();
        Pattern patron = Pattern.compile("\\d{2}-\\d{2}-\\d{4}");
        Matcher igualar = patron.matcher(fechareal);
        Date fechaDate = null;

        if (igualar.find()) {

            String fechaString = igualar.group();
            DateTimeFormatter formatofecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate fechasql = LocalDate.parse(fechaString, formatofecha);
            fechaDate = Date.valueOf(fechasql);

        } else {

            System.out.println("Error: Parametros No Encontrados");

        }

        return fechaDate;

    }

    public String fechaVencimiento() {

        String vencimiento;
        vencimiento = plazodepago.getText();
        StringBuilder conca = new StringBuilder();
        String venciFecha;

        switch (vencimiento) {
            case "Contado":
                txtvencimiento.setText("PAGO INMEDIATO");
                break;
            case "a 30 Dias":
                venciFecha = conca.append("Fecha Vencimiento ").append(fechaActual.plusDays(30).format(formatofecha)).toString();
                txtvencimiento.setText(venciFecha);

                break;
            case "a 60 Dias":
                venciFecha = conca.append("Fecha Vencimiento ").append(fechaActual.plusDays(60).format(formatofecha)).toString();
                txtvencimiento.setText(venciFecha);
                break;
            case "a 90 Dias":
                venciFecha = conca.append("Fecha Vencimiento ").append(fechaActual.plusDays(90).format(formatofecha)).toString();
                txtvencimiento.setText(venciFecha);
                break;

        }

        return txtvencimiento.toString();

    }

    //metodo se ejecuta desde el boton pagar en la clase PagosCreditosController
    public void invocarBuscarnit(ActionEvent event) throws IOException {

        buscarnit(event);
    }

    @FXML
    private void buscarnit(ActionEvent event) throws IOException {

        String validarNIT = buscarNIT.getText();

        if (validarNIT.isEmpty()) {

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Campo Vacio");
            alerta.setHeaderText(null);
            alerta.setContentText("Debes Diligenciar un NIT");
            alerta.showAndWait();

        } else {

            int nit = Integer.parseInt(validarNIT);
            List<Venta> validarfactura = new ArrayList(validarfecha.validarFacturasVencidas(nit));
            cliente = buscar.consultarCliente(nit);
            Date fechaHoy = Date.valueOf(fechaActual);
            borrardatos.setDisable(false);

            if (cliente.getIdentificacionCliente() != nit) {

                buscarNIT.clear();

                JOptionPane.showMessageDialog(null, "El NIT " + nit + " NO Existe en Base de Datos", "Nit NO Existe", JOptionPane.INFORMATION_MESSAGE);

            } else {

                facturas.clear();

                if (!validarfactura.isEmpty()) {

                    int conteo = 0;

                    for (Venta reco : validarfactura) {

                        if (reco.getFechavencimiento().compareTo(fechaHoy) <= 0 && reco.getStatus().equals("pendiente")) {
                            conteo += 1;
                            facturas.add(reco);

                        }
                    }

                    if (conteo == 0) {

                        nombrecliente.setText(cliente.getNombreCliente());
                        tipocliente.setText(cliente.getTipoCliente());
                        plazodepago.setText(cliente.getDiasDePlazo());
                        correo.setText(cliente.getCorreoCliente());
                        telefono.setText(String.valueOf(cliente.getContactoCliente()));
                        direccion.setText(cliente.getDireccionCliente());

                        if (tipocliente.getText().equals("Credito")) {

                            abono.setDisable(false);
                            pagado.setDisable(true);

                        } else if (tipocliente.getText().equals("Contado")) {

                            abono.setDisable(true);
                            pagado.setDisable(false);
                        }

                        ingresarcodigo.setDisable(false);
                        ingresarcodigo.requestFocus();

                        fechaVencimiento();

                    }

                    if (conteo >= 1) {

                        FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Pagos/OpcionesFacturasVencidas.fxml"));
                        Parent root = cargar.load();

                        OpcionesFacturasVencidasController ventanaOpciones = cargar.getController();
                        ventanaOpciones.llamardatos(cliente, conteo);
                        ventanaOpciones.ventanaVentas(this);

                        Stage stage2 = new Stage();
                        stage2.setScene(new Scene(root));
                        stage2.show();

                    }
                } else {

                    nombrecliente.setText(cliente.getNombreCliente());
                    tipocliente.setText(cliente.getTipoCliente());
                    plazodepago.setText(cliente.getDiasDePlazo());
                    correo.setText(cliente.getCorreoCliente());
                    telefono.setText(String.valueOf(cliente.getContactoCliente()));
                    direccion.setText(cliente.getDireccionCliente());

                    if (tipocliente.getText().equals("Credito")) {

                        abono.setDisable(false);
                        pagado.setDisable(true);

                    } else if (tipocliente.getText().equals("Contado")) {

                        abono.setDisable(true);
                        pagado.setDisable(false);
                    }

                    ingresarcodigo.setDisable(false);
                    ingresarcodigo.requestFocus();

                    fechaVencimiento();
                }

            }

        }

    }

    public void listarecibida() throws IOException {

        FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Pagos/ListadoFacturasVencidas.fxml"));
        Parent root = cargar.load();

        ListadoFacturasVencidasController facturasvencidas = cargar.getController();
        facturasvencidas.cargarDatos(facturas);
        facturasvencidas.clientes(cliente);
        facturasvencidas.ventanaVentas(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void valorRecibido() {

        nombrecliente.setText(cliente.getNombreCliente());
        tipocliente.setText("Contado");
        plazodepago.setText("Contado");
        correo.setText(cliente.getCorreoCliente());
        telefono.setText(String.valueOf(cliente.getContactoCliente()));
        direccion.setText(cliente.getDireccionCliente());

        if (tipocliente.getText().equals("Credito")) {

            abono.setDisable(false);
            pagado.setDisable(true);

        } else if (tipocliente.getText().equals("Contado")) {

            abono.setDisable(true);
            pagado.setDisable(false);
        }

        ingresarcodigo.setDisable(false);
        ingresarcodigo.requestFocus();

        fechaVencimiento();
    }

    @FXML
    private void ingresarcodigo(ActionEvent event) {

        String validarCodigo = ingresarcodigo.getText();

        if (validarCodigo.isEmpty()) {

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Campo Vacio");
            alerta.setHeaderText(null);
            alerta.setContentText("Debes Diligenciar el Codigo de un Producto");
            alerta.showAndWait();

        } else {

            int codigo = Integer.parseInt(validarCodigo);
            proBuscado = buscar.consultarProducto(codigo);

            if (proBuscado.getCodigoProducto() != codigo) {

                ingresarcodigo.clear();
                JOptionPane.showMessageDialog(null, "El Codigo " + codigo + " NO Existe en Base de Datos", "Codigo NO Existe", JOptionPane.INFORMATION_MESSAGE);

            } else {

                boolean encontrar = false;
                precioGlobal = (int) proBuscado.getPrecioProducto();

                for (Venta reco : ventas) {

                    if (ingresarcodigo.getText().equals(String.valueOf(reco.getCodigo()))) {

                        encontrar = true;
                        int actual = reco.getCantidad();
                        int inventariofinal = proBuscado.getInventario() - actual;
                        inventario.setText(String.valueOf(inventariofinal));

                        if (inventario.getText().equals("0")) {
                            JOptionPane.showMessageDialog(null, "Producto Sin Inventario Disponible, Valide Unidades Previamente Ingresadas", "Sin Stock Disponible", JOptionPane.INFORMATION_MESSAGE);
                            inventario.setText("");
                            cantidad.setDisable(true);
                        } else {

                            precio.setText(formatoMoneda.format(proBuscado.getPrecioProducto()));
                            iva.setText(proBuscado.getImpuestoProducto());
                            producto.setText(proBuscado.getNombreProducto());

                            cantidad.setDisable(false);
                            cantidad.requestFocus();
                            ingresarregistro.setDisable(false);
                        }

                        break;
                    }

                }

                if (encontrar == false) {

                    inventario.setText(String.valueOf(proBuscado.getInventario()));
                    precio.setText(formatoMoneda.format(proBuscado.getPrecioProducto()));
                    iva.setText(proBuscado.getImpuestoProducto());
                    producto.setText(proBuscado.getNombreProducto());
                    cantidad.setDisable(false);
                    cantidad.requestFocus();
                    ingresarregistro.setDisable(false);
                }

            }
        }

    }

    @FXML
    private void unidades(ActionEvent event) {

        String validarCantidad = cantidad.getText();
        String validarCodigo = ingresarcodigo.getText();

        if (validarCantidad.isEmpty() && validarCodigo.isEmpty()) {

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Campos Vacios");
            alerta.setHeaderText(null);
            alerta.setContentText("Primero debes ingresar un producto");
            alerta.showAndWait();

        } else {

            if (validarCantidad.isEmpty()) {

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("Campo Vacio");
                alerta.setHeaderText(null);
                alerta.setContentText("Por favor Ingresa Las Cantidades a Facturar");
                alerta.showAndWait();

            } else {

                int subtotal, valorIva = 0, total;
                int valorPrecio, cantidades, codigo;
                String impuesto, prodc, vencimiento;

                valorPrecio = precioGlobal;
                cantidades = Integer.parseInt(validarCantidad);
                codigo = Integer.parseInt(validarCodigo);
                prodc = producto.getText();
                impuesto = iva.getText();

                subtotal = cantidades * valorPrecio;

                switch (impuesto) {
                    case "No":
                        valorIva = 0;
                        break;
                    case "IVA 19%":
                        valorIva = (int) (subtotal * 0.19);
                        break;
                    case "IVA 10%":
                        valorIva = (int) (subtotal * 0.10);
                        break;
                    case "IBUA 5%":
                        valorIva = (int) (subtotal * 0.05);
                        break;
                    case "ICO 6%":
                        valorIva = (int) (subtotal * 0.06);
                        break;

                }

                total = subtotal + valorIva;

                if (ventas.isEmpty()) {
                    if (Integer.parseInt(inventario.getText()) < Integer.parseInt(cantidad.getText())) {//valida que la cantidad de prouctos del array sea menor que la cantidad ingresda en el textfield

                        JOptionPane.showMessageDialog(null, "Las Unidades Ingresadas Superan al Inventario Disponible", "Stock no Disponible", JOptionPane.INFORMATION_MESSAGE);
                        cantidad.requestFocus();
                    } else {

                        item += 1;
                        ventas.add(new Venta(item, codigo, prodc, valorPrecio, cantidades, subtotal, valorIva, total));

                        int totalCantidades = 0;
                        int totalSubtotal = 0;
                        int totiva = 0;
                        int totalG = 0;

                        for (Venta r : ventas) {

                            totalCantidades += r.getCantidad();
                            totalSubtotal += r.getSubtotal();
                            totiva += r.getValorIva();
                            totalG += r.getTotal();
                        }

                        subtotalGlobal = totalSubtotal;
                        valorIvaGlobal = totiva;
                        totalGeneralGlobal = totalG;
                        totalcantidades.setText(String.valueOf(totalCantidades));
                        totalsubtotal.setText(formatoMoneda.format(totalSubtotal));
                        totaliva.setText(formatoMoneda.format(totiva));
                        valorfactura.setText(formatoMoneda.format(totalG));

                        ingresarcodigo.setText("");
                        producto.setText("");
                        inventario.setText("");
                        precio.setText("");
                        cantidad.setText("");
                        iva.setText("");

                        cantidad.setDisable(true);
                        ingresarregistro.setDisable(true);

                        EliminarItem.setDisable(false);
                        EditarCantidades.setDisable(false);

                        String tipoCliente = tipocliente.getText();
                        String pago = pagado.getText();

                        if (tipoCliente.equals("Contado") && !pago.isEmpty()) {

                            vender.setDisable(false);

                        } else if (tipoCliente.equals("Credito")) {

                            vender.setDisable(false);
                        }

                        ingresarcodigo.requestFocus();
                    }

                } else {

                    boolean encontrado = false;

                    for (Venta reco : ventas) {

                        if (codigo == reco.getCodigo()) {

                            encontrado = true;

                            if (Integer.parseInt(inventario.getText()) < Integer.parseInt(cantidad.getText())) {//calida que la cantidad de prouctos del array sea menor que la cantidad ingresda en el textfield

                                JOptionPane.showMessageDialog(null, "Las Unidades Ingresadas Superan al Inventario Disponible", "Stock no Disponible", JOptionPane.INFORMATION_MESSAGE);
                                cantidad.requestFocus();
                            } else {

                                int newitem = reco.getItem();
                                int posicion = reco.getItem() - 1;
                                int cant = cantidades + reco.getCantidad();
                                int subt = subtotal + reco.getSubtotal();
                                int sumaiva = valorIva + reco.getValorIva();
                                int sumatotal = total + reco.getTotal();
                                ventas.set(posicion, new Venta(newitem, codigo, prodc, valorPrecio, cant, subt, sumaiva, sumatotal));

                                int totalCantidades = 0;
                                int totalSubtotal = 0;
                                int totiva = 0;
                                int totalG = 0;

                                for (Venta r : ventas) {

                                    totalCantidades += r.getCantidad();
                                    totalSubtotal += r.getSubtotal();
                                    totiva += r.getValorIva();
                                    totalG += r.getTotal();
                                }

                                subtotalGlobal = totalSubtotal;
                                valorIvaGlobal = totiva;
                                totalGeneralGlobal = totalG;
                                totalcantidades.setText(String.valueOf(totalCantidades));
                                totalsubtotal.setText(formatoMoneda.format(totalSubtotal));
                                totaliva.setText(formatoMoneda.format(totiva));
                                valorfactura.setText(formatoMoneda.format(totalG));

                                ingresarcodigo.setText("");
                                producto.setText("");
                                inventario.setText("");
                                precio.setText("");
                                cantidad.setText("");
                                iva.setText("");

                                cantidad.setDisable(true);
                                ingresarregistro.setDisable(true);

                                EliminarItem.setDisable(false);
                                EditarCantidades.setDisable(false);
                                ingresarcodigo.requestFocus();
                            }

                            break;
                        }

                    }

                    if (encontrado == false) {

                        if (Integer.parseInt(inventario.getText()) < Integer.parseInt(cantidad.getText())) {//calida que la cantidad de prouctos del array sea menor que la cantidad ingresda en el textfield

                            JOptionPane.showMessageDialog(null, "Las Unidades Ingresadas Superan al Inventario Disponible", "Stock no Disponible", JOptionPane.INFORMATION_MESSAGE);
                            cantidad.requestFocus();
                        } else {

                            item += 1;
                            ventas.add(new Venta(item, codigo, prodc, valorPrecio, cantidades, subtotal, valorIva, total));

                            int totalCantidades = 0;
                            int totalSubtotal = 0;
                            int totiva = 0;
                            int totalG = 0;

                            for (Venta r : ventas) {

                                totalCantidades += r.getCantidad();
                                totalSubtotal += r.getSubtotal();
                                totiva += r.getValorIva();
                                totalG += r.getTotal();
                            }

                            subtotalGlobal = totalSubtotal;
                            valorIvaGlobal = totiva;
                            totalGeneralGlobal = totalG;
                            totalcantidades.setText(String.valueOf(totalCantidades));
                            totalsubtotal.setText(formatoMoneda.format(totalSubtotal));
                            totaliva.setText(formatoMoneda.format(totiva));
                            valorfactura.setText(formatoMoneda.format(totalG));

                            ingresarcodigo.setText("");
                            producto.setText("");
                            inventario.setText("");
                            precio.setText("");
                            cantidad.setText("");
                            iva.setText("");

                            cantidad.setDisable(true);
                            ingresarregistro.setDisable(true);

                            EliminarItem.setDisable(false);
                            EditarCantidades.setDisable(false);

                            ingresarcodigo.requestFocus();
                        }

                    }

                }

                item_Col.setCellValueFactory(new PropertyValueFactory<>("item"));
                codigo_Col.setCellValueFactory(new PropertyValueFactory<>("codigo"));
                producto_Col.setCellValueFactory(new PropertyValueFactory<>("producto"));
                cantidades_Col.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
                precio_Col.setCellValueFactory(recorrer -> {
                    Venta venta = recorrer.getValue();
                    String valorfactura = formatoMoneda.format(venta.getPrecio());

                    return new SimpleStringProperty(valorfactura);

                });
                subtotal_Col.setCellValueFactory(recorrer -> {
                    Venta venta = recorrer.getValue();
                    String valorfactura = formatoMoneda.format(venta.getSubtotal());

                    return new SimpleStringProperty(valorfactura);

                });
                iva_Col.setCellValueFactory(recorrer -> {
                    Venta venta = recorrer.getValue();
                    String valorfactura = formatoMoneda.format(venta.getValorIva());

                    return new SimpleStringProperty(valorfactura);

                });
                total_Col.setCellValueFactory(recorrer -> {
                    Venta venta = recorrer.getValue();
                    String valorfactura = formatoMoneda.format(venta.getTotal());

                    return new SimpleStringProperty(valorfactura);

                });

                item_Col.setCellFactory(column -> new CenteredTableCell<>());
                codigo_Col.setCellFactory(column -> new CenteredTableCell<>());
                producto_Col.setCellFactory(column -> new CenteredTableCell<>());
                cantidades_Col.setCellFactory(column -> new CenteredTableCell<>());
                precio_Col.setCellFactory(column -> new CenteredTableCell<>());
                subtotal_Col.setCellFactory(column -> new CenteredTableCell<>());
                iva_Col.setCellFactory(column -> new CenteredTableCell<>());
                total_Col.setCellFactory(column -> new CenteredTableCell<>());

                tablaventa.setItems(ventas);

                if (!totalcantidades.equals("")) {

                    buscarNIT.setDisable(true);

                }

            }

        }
    }

    @FXML
    private void EliminarItem(ActionEvent event) {

        int validar = tablaventa.getSelectionModel().getSelectedIndex();

        if (validar < 0) {

            JOptionPane.showMessageDialog(null, "No Existe Registro Seleccionado en la Tabla", "Seleccione un registro", JOptionPane.INFORMATION_MESSAGE);

        } else if (validar >= 0) {

            int i = JOptionPane.showConfirmDialog(null, "¿Esta Seguro de Eliminar el Registro?", "Confirmar Accion", JOptionPane.INFORMATION_MESSAGE);

            switch (i) {
                case 0:
                    int id = tablaventa.getSelectionModel().getSelectedItem().getItem();
                    ventas.remove(id - 1);

                    int contar = 0;
                    int contarUnidades = 0;
                    int sumarSubtotal = 0;
                    int sumarIva = 0;
                    int sumarTotal = 0;

                    for (Venta reco : ventas) {
                        Venta venta = new Venta();
                        contar += 1;
                        venta.setItem(contar);
                        venta.setCodigo(reco.getCodigo());
                        venta.setProducto(reco.getProducto());
                        venta.setCantidad(reco.getCantidad());
                        venta.setPrecio(reco.getPrecio());
                        venta.setSubtotal(reco.getSubtotal());
                        venta.setValorIva(reco.getValorIva());
                        venta.setTotal(reco.getTotal());
                        ventas.set(contar - 1, venta);

                        contarUnidades += venta.getCantidad();
                        sumarSubtotal += venta.getSubtotal();
                        sumarIva += venta.getValorIva();
                        sumarTotal += venta.getTotal();

                    }

                    item = contar;
                    tablaventa.setItems(ventas);

                    subtotalGlobal = sumarSubtotal;
                    valorIvaGlobal = sumarIva;
                    totalGeneralGlobal = sumarTotal;

                    totalcantidades.setText(String.valueOf(contarUnidades));
                    totaliva.setText(formatoMoneda.format(sumarIva));
                    totalsubtotal.setText(formatoMoneda.format(sumarSubtotal));
                    valorfactura.setText(formatoMoneda.format(sumarTotal));

                    JOptionPane.showMessageDialog(null, "Registro Eliminado Correctamente", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
                    vender.setDisable(true);
                    
                    break;

                case 1:
                    break;

            }

        }

    }

//consulta que tipo de iva tiene el producto al cual se le van a modificar las cantidades en la tabla
    public String consultaIva() {

        int codigo = tablaventa.getSelectionModel().getSelectedItem().getCodigo();
        proBuscado = buscar.consultarProducto(codigo);

        String iva = proBuscado.getImpuestoProducto();

        return iva;

    }

    public void modificarUnidadesVentas(int valor, int id) {
        Venta venta = new Venta();

        int codigo = tablaventa.getSelectionModel().getSelectedItem().getCodigo();
        proBuscado = buscar.consultarProducto(codigo);

        int inventario = proBuscado.getInventario();

        if (inventario < valor) {

            JOptionPane.showMessageDialog(null, "Las Unidadades ingresadas superan al inventario actual", "Stock Superado", JOptionPane.INFORMATION_MESSAGE);
        } else {

            int numero = valor;
            String impuesto;
            int valorIva = 0, total = 0;
            int precio = tablaventa.getSelectionModel().getSelectedItem().getPrecio();
            int subtotal = numero * precio;

            venta.setCantidad(numero);
            venta.setItem(tablaventa.getSelectionModel().getSelectedItem().getItem());
            venta.setCodigo(tablaventa.getSelectionModel().getSelectedItem().getCodigo());
            venta.setProducto(tablaventa.getSelectionModel().getSelectedItem().getProducto());
            venta.setPrecio(tablaventa.getSelectionModel().getSelectedItem().getPrecio());
            venta.setSubtotal(subtotal);

            impuesto = consultaIva();

            switch (impuesto) {
                case "No":
                    valorIva = 0;
                    break;
                case "IVA 19%":
                    valorIva = (int) (subtotal * 0.19);
                    break;
                case "IVA 10%":
                    valorIva = (int) (subtotal * 0.10);
                    break;
                case "IBUA 5%":
                    valorIva = (int) (subtotal * 0.05);
                    break;
                case "ICO 6%":
                    valorIva = (int) (subtotal * 0.06);
                    break;

            }

            venta.setValorIva(valorIva);

            total = subtotal + valorIva;

            venta.setTotal(total);
            ventas.set(id - 1, venta);

            int contarUnidades = 0;
            int sumarIva = 0;
            int sumarSubtotal = 0;
            int sumarTotal = 0;

            for (Venta reco : tablaventa.getItems()) {

                contarUnidades += reco.getCantidad();
                sumarIva += reco.getValorIva();
                sumarSubtotal += reco.getSubtotal();
                sumarTotal += reco.getTotal();

            }

            subtotalGlobal = sumarSubtotal;
            valorIvaGlobal = sumarIva;
            totalGeneralGlobal = sumarTotal;

            totalcantidades.setText(String.valueOf(contarUnidades));
            totaliva.setText(formatoMoneda.format(sumarIva));
            totalsubtotal.setText(formatoMoneda.format(sumarSubtotal));
            valorfactura.setText(formatoMoneda.format(sumarTotal));

        }

    }

    @FXML
    private void EditarCantidades(ActionEvent event) throws IOException {

        int validar = tablaventa.getSelectionModel().getSelectedIndex();

        if (validar < 0) {

            JOptionPane.showMessageDialog(null, "No Existe Registro Seleccionado en la Tabla", "Seleccione un registro", JOptionPane.INFORMATION_MESSAGE);

        } else if (validar >= 0) {

            int idEditarUnids = tablaventa.getSelectionModel().getSelectedItem().getItem();

            FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Vista/ModificarUnds.fxml"));
            Parent root = cargar.load();

            ModificarUndsController ventanaOpciones = cargar.getController();
            ventanaOpciones.ventanaVentas(this);
            ventanaOpciones.idModificado(idEditarUnids);

            Stage stage2 = new Stage();
            stage2.setScene(new Scene(root));
            stage2.show();

        }

    }

    @FXML
    private void ingresarregistro(ActionEvent event) {

        registrodeVenta();

    }

    public void registrodeVenta() {

        int subtotal, valorIva = 0, total;
        int valorPrecio, cantidades, codigo;
        String impuesto, prodc, vencimiento;

        if (ingresarcodigo.getText().equals("0")) {

            JOptionPane.showMessageDialog(null, "Ingresa un Codigo", "Informacion", JOptionPane.INFORMATION_MESSAGE);

        } else {

            if (cantidad.getText().isEmpty() && ingresarcodigo.getText().isEmpty()) {

                JOptionPane.showMessageDialog(null, "Primero Debes Buscar el Producto e Ingresar las Cantidades", "Falta Informacion", JOptionPane.INFORMATION_MESSAGE);

            } else if (cantidad.getText().isEmpty() || cantidad.getText().equals("0")) {

                JOptionPane.showMessageDialog(null, "Primero Debes Ingresar las Cantidades", "Falta Informacion", JOptionPane.INFORMATION_MESSAGE);

            } else {

                valorPrecio = Integer.parseInt(precio.getText().substring(0, precio.getText().length() - 2));
                cantidades = Integer.parseInt(cantidad.getText());
                codigo = Integer.parseInt(ingresarcodigo.getText());
                prodc = producto.getText();
                impuesto = iva.getText();

                subtotal = cantidades * valorPrecio;

                switch (impuesto) {
                    case "No":
                        valorIva = 0;
                        break;
                    case "IVA 19%":
                        valorIva = (int) (subtotal * 0.19);
                        break;
                    case "IVA 10%":
                        valorIva = (int) (subtotal * 0.10);
                        break;
                    case "IBUA 5%":
                        valorIva = (int) (subtotal * 0.05);
                        break;
                    case "ICO 6%":
                        valorIva = (int) (subtotal * 0.06);
                        break;

                }

                total = subtotal + valorIva;

                if (ventas.isEmpty()) {
                    if (Integer.parseInt(inventario.getText()) < Integer.parseInt(cantidad.getText())) {//valida que la cantidad de prouctos del array sea menor que la cantidad ingresda en el textfield

                        JOptionPane.showMessageDialog(null, "Las Unidades Ingresadas Superan al Inventario Disponible", "Stock no Disponible", JOptionPane.INFORMATION_MESSAGE);
                        cantidad.requestFocus();
                    } else {

                        item += 1;
                        ventas.add(new Venta(item, codigo, prodc, valorPrecio, cantidades, subtotal, valorIva, total));

                        int totalCantidades = 0;
                        int totalSubtotal = 0;
                        int totiva = 0;
                        int totalG = 0;

                        for (Venta r : ventas) {

                            totalCantidades += r.getCantidad();
                            totalSubtotal += r.getSubtotal();
                            totiva += r.getValorIva();
                            totalG += r.getTotal();
                        }

                        totalcantidades.setText(String.valueOf(totalCantidades));
                        totalsubtotal.setText(String.valueOf(totalSubtotal));
                        totaliva.setText(String.valueOf(totiva));
                        valorfactura.setText(String.valueOf(totalG));

                        ingresarcodigo.setText("");
                        producto.setText("");
                        inventario.setText("");
                        precio.setText("");
                        cantidad.setText("");
                        iva.setText("");

                        cantidad.setDisable(true);
                        ingresarregistro.setDisable(true);

                        String tipoCliente = tipocliente.getText();
                        String pago = pagado.getText();

                        if (tipoCliente.equals("Contado") && !pago.isEmpty()) {

                            vender.setDisable(false);

                        } else if (tipoCliente.equals("Credito")) {

                            vender.setDisable(false);
                        }

                        ingresarcodigo.requestFocus();
                    }

                } else {

                    boolean encontrado = false;

                    for (Venta reco : ventas) {

                        if (codigo == reco.getCodigo()) {

                            encontrado = true;

                            if (Integer.parseInt(inventario.getText()) < Integer.parseInt(cantidad.getText())) {//calida que la cantidad de prouctos del array sea menor que la cantidad ingresda en el textfield

                                JOptionPane.showMessageDialog(null, "Las Unidades Ingresadas Superan al Inventario Disponible", "Stock no Disponible", JOptionPane.INFORMATION_MESSAGE);
                                cantidad.requestFocus();
                            } else {

                                int newitem = reco.getItem();
                                int posicion = reco.getItem() - 1;
                                int cant = cantidades + reco.getCantidad();
                                int subt = subtotal + reco.getSubtotal();
                                int sumaiva = valorIva + reco.getValorIva();
                                int sumatotal = total + reco.getTotal();
                                ventas.set(posicion, new Venta(newitem, codigo, prodc, valorPrecio, cant, subt, sumaiva, sumatotal));

                                int totalCantidades = 0;
                                int totalSubtotal = 0;
                                int totiva = 0;
                                int totalG = 0;

                                for (Venta r : ventas) {

                                    totalCantidades += r.getCantidad();
                                    totalSubtotal += r.getSubtotal();
                                    totiva += r.getValorIva();
                                    totalG += r.getTotal();
                                }

                                totalcantidades.setText(String.valueOf(totalCantidades));
                                totalsubtotal.setText(String.valueOf(totalSubtotal));
                                totaliva.setText(String.valueOf(totiva));
                                valorfactura.setText(String.valueOf(totalG));

                                ingresarcodigo.setText("");
                                producto.setText("");
                                inventario.setText("");
                                precio.setText("");
                                cantidad.setText("");
                                iva.setText("");

                                cantidad.setDisable(true);
                                ingresarregistro.setDisable(true);

                                ingresarcodigo.requestFocus();
                            }

                            break;
                        }

                    }

                    if (encontrado == false) {

                        if (Integer.parseInt(inventario.getText()) < Integer.parseInt(cantidad.getText())) {//calida que la cantidad de prouctos del array sea menor que la cantidad ingresda en el textfield

                            JOptionPane.showMessageDialog(null, "Las Unidades Ingresadas Superan al Inventario Disponible", "Stock no Disponible", JOptionPane.INFORMATION_MESSAGE);
                            cantidad.requestFocus();
                        } else {

                            item += 1;
                            ventas.add(new Venta(item, codigo, prodc, valorPrecio, cantidades, subtotal, valorIva, total));

                            int totalCantidades = 0;
                            int totalSubtotal = 0;
                            int totiva = 0;
                            int totalG = 0;

                            for (Venta r : ventas) {

                                totalCantidades += r.getCantidad();
                                totalSubtotal += r.getSubtotal();
                                totiva += r.getValorIva();
                                totalG += r.getTotal();
                            }

                            totalcantidades.setText(String.valueOf(totalCantidades));
                            totalsubtotal.setText(String.valueOf(totalSubtotal));
                            totaliva.setText(String.valueOf(totiva));
                            valorfactura.setText(String.valueOf(totalG));

                            ingresarcodigo.setText("");
                            producto.setText("");
                            inventario.setText("");
                            precio.setText("");
                            cantidad.setText("");
                            iva.setText("");

                            cantidad.setDisable(true);
                            ingresarregistro.setDisable(true);

                            ingresarcodigo.requestFocus();
                        }

                    }

                }

                item_Col.setCellValueFactory(new PropertyValueFactory<>("item"));
                codigo_Col.setCellValueFactory(new PropertyValueFactory<>("codigo"));
                producto_Col.setCellValueFactory(new PropertyValueFactory<>("producto"));
                cantidades_Col.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
                precio_Col.setCellValueFactory(new PropertyValueFactory<>("precio"));
                subtotal_Col.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
                iva_Col.setCellValueFactory(new PropertyValueFactory<>("valorIva"));
                total_Col.setCellValueFactory(new PropertyValueFactory<>("total"));

                tablaventa.setItems(ventas);

                if (!totalcantidades.equals("")) {

                    buscarNIT.setDisable(true);

                }

            }

        }

    }

    @FXML
    private void pagado(ActionEvent event) {

        int factura;
        int pago;
        int dif;

        String validarVacio = pagado.getText();
        String validarPrecioFactura = valorfactura.getText();
        String tipoCliente = tipocliente.getText();

        if (validarPrecioFactura.isEmpty()) {

            JOptionPane.showMessageDialog(null, "Primero debes Ingresar todos los Datos de la Venta", "Faltan Datos de la Venta", JOptionPane.INFORMATION_MESSAGE);

        } else {

            if (validarVacio.isEmpty()) {

                JOptionPane.showMessageDialog(null, "No has Ingresado el Pago", "Valor no Permitido", JOptionPane.INFORMATION_MESSAGE);

            } else {

                pago = Integer.parseInt(pagado.getText());
                factura = totalGeneralGlobal;
                diferenciaGlobal = factura - pago;

                if (diferenciaGlobal > 1000 || diferenciaGlobal < -1000) {

                    int k = JOptionPane.showConfirmDialog(null, "Diferencia " + formatoMoneda.format(diferenciaGlobal) + " Demasiado Grande, ¿Esta Seguro del Valor Ingresado?", "Valide Pago Recibido", JOptionPane.ERROR_MESSAGE);

                    if (k == 0) {

                        if (tipoCliente.equals("Contado")) {

                            if (diferenciaGlobal > 0) {

                                diferencia.setText(formatoMoneda.format(diferenciaGlobal));
                                txtdiferencia.setText("Saldo por PAGAR");
                                ajustealpeso.setDisable(false);

                            } else if (diferenciaGlobal < 0) {

                                diferencia.setText(formatoMoneda.format(diferenciaGlobal));
                                txtdiferencia.setText("Saldo a FAVOR");
                                ajustealpeso.setDisable(false);

                            } else {

                                diferencia.setText(formatoMoneda.format(diferenciaGlobal));
                                txtdiferencia.setText("Sin Diferencia");
                                ajustealpeso.setDisable(true);
                                pagado.setDisable(true);
                                //vender.setDisable(true);

                            }

                        } else {////aquiii

                            pago = Integer.parseInt(pagado.getText());
                            factura = totalGeneralGlobal;
                            diferenciaGlobal = factura - pago;

                            if (diferenciaGlobal > 0) {

                                diferencia.setText(formatoMoneda.format(diferenciaGlobal));
                                txtdiferencia.setText("Saldo por PAGAR");
                                ajustealpeso.setDisable(false);

                            } else if (diferenciaGlobal < 0) {

                                diferencia.setText(formatoMoneda.format(diferenciaGlobal));
                                txtdiferencia.setText("Saldo a FAVOR");
                                ajustealpeso.setDisable(false);

                            } else {

                                diferencia.setText(formatoMoneda.format(diferenciaGlobal));
                                txtdiferencia.setText("Sin Diferencia");
                                ajustealpeso.setDisable(true);
                                pagado.setDisable(true);
                                //vender.setDisable(true);

                            }

                        }

                    } else {

                        pagado.setStyle(
                                "-fx-border-color: red;"
                                + // Cambia el color del borde a rojo
                                "-fx-border-width: 2px;"
                                + // Define el grosor del borde
                                "-fx-border-radius: 5px; "
                                + // Define el radio de las esquinas del borde
                                "-fx-background-radius: 5px;"
                        );

                    }

                } else {

                    pagado.setStyle("");

                    if (diferenciaGlobal > 0) {

                        diferencia.setText(formatoMoneda.format(diferenciaGlobal));
                        txtdiferencia.setText("Saldo por PAGAR");
                        ajustealpeso.setDisable(false);

                    } else if (diferenciaGlobal < 0) {

                        diferencia.setText(formatoMoneda.format(diferenciaGlobal));
                        txtdiferencia.setText("Saldo a FAVOR");
                        ajustealpeso.setDisable(false);

                    } else {

                        diferencia.setText(formatoMoneda.format(diferenciaGlobal));
                        txtdiferencia.setText("Sin Diferencia");
                        ajustealpeso.setDisable(true);
                        pagado.setDisable(true);
                        vender.setDisable(false);

                    }

                }

            }

        }

    }

    @FXML
    private void valorfactura(ActionEvent event) {
    }

    @FXML
    private void diferencia(ActionEvent event) {
    }

    @FXML
    private void abono(ActionEvent event) {

        Object evento = event.getSource();

        if (evento.equals(abono) && !valorfactura.getText().equals("")) {

            pagado.setDisable(false);
            pagado.requestFocus();

        } else {

            JOptionPane.showMessageDialog(null, "Debes Ingresar Todos los Datos de la Venta");
        }

    }

    @FXML
    private void ajustealpeso(ActionEvent event) {
        int factura;
        int pago;
        int dif;
        int nuevovalor;
        int ajuste = 0;

        String valor = diferencia.getText();

        if (valor == null || valor.isEmpty()) {

            JOptionPane.showMessageDialog(null, "No Existe Diferencia por Ajustar");

        } else {

            dif = diferenciaGlobal;

            if (dif > 1000 || dif < -1000) {

                int i = JOptionPane.showConfirmDialog(null, "Esta Seguro de Ajustar " + formatoMoneda.format(diferenciaGlobal) + "?", "Considera la Diferencia", JOptionPane.INFORMATION_MESSAGE);

                if (i == 0) {
                    pago = Integer.parseInt(pagado.getText());
                    ajuste = pago + dif;

                    factura = totalGeneralGlobal;
                    nuevovalor = factura - ajuste;
                    diferencia.setText(formatoMoneda.format(nuevovalor));
                    ajustado.setText(formatoMoneda.format(dif));
                    ajustado.setDisable(true);
                    abono.setDisable(true);
                    pagado.setDisable(true);
                    ajustealpeso.setDisable(true);
                    vender.setDisable(false);

                }
            } else {

                pago = Integer.parseInt(pagado.getText());
                ajuste = pago + dif;

                factura = totalGeneralGlobal;
                nuevovalor = factura - ajuste;
                diferencia.setText(formatoMoneda.format(nuevovalor));
                ajustado.setText(formatoMoneda.format(dif));
                ajustado.setDisable(true);
                abono.setDisable(true);
                pagado.setDisable(true);
                ajustealpeso.setDisable(true);
                vender.setDisable(false);
            }

        }

    }

    @FXML
    private void validarvaloresnit(KeyEvent event) {

        String valor = buscarNIT.getText();

        if (!valor.matches("\\d*")) {

            buscarNIT.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Campo NIT Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 10) {

            buscarNIT.setText(valor.substring(0, 10));
            txtadvertencia.setText("¡Campo NIT Maximo 10 digitos!");
        }

        buscarNIT.positionCaret(valor.length());
    }

    @FXML
    private void validarvalorescodigo(KeyEvent event) {

        String valor = ingresarcodigo.getText();

        if (!valor.matches("\\d*")) {

            ingresarcodigo.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Campo Codigo Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 8) {

            ingresarcodigo.setText(valor.substring(0, 8));
            txtadvertencia.setText("¡Campo Codigo Maximo 8 digitos!");
        }

        ingresarcodigo.positionCaret(valor.length());
    }

    @FXML
    private void validarvaloresUnids(KeyEvent event) {
        String valor = cantidad.getText();

        if (!valor.matches("\\d*")) {

            cantidad.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Campo Unidades Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 4) {

            cantidad.setText(valor.substring(0, 4));
            txtadvertencia.setText("¡Campo Unidades Maximo 4 digitos!");
        }

        cantidad.positionCaret(valor.length());
    }

    @FXML
    private void validarvaloresrecibido(KeyEvent event) {

        String valor = pagado.getText();

        if (!valor.matches("\\d*")) {

            pagado.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 8) {

            pagado.setText(valor.substring(0, 8));
            txtadvertencia.setText("¡Maximo 8 digitos!");
        }

        pagado.positionCaret(valor.length());
    }

    public class CenteredTableCell<S, T> extends TableCell<S, T> {

        private final CheckBox checkBox;

        public CenteredTableCell() {
            this.checkBox = new CheckBox();
            setAlignment(Pos.CENTER);
        }

        @Override
        protected void updateItem(T item, boolean vacio) {
            super.updateItem(item, vacio);
            if (vacio || item == null) {
                setText(null);
                setGraphic(null);
            } else if (item instanceof CheckBox checkPagar) {
                setGraphic(checkPagar);
            } else {
                setText(item.toString());
                setGraphic(null);
            }
        }
    }
}
