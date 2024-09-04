package Pagos;

import Controladora.Venta;
import Modelo.ConexionMySql;
import java.io.IOException;
import java.sql.Connection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ModuloPagosCreditosController implements Initializable {

    Connection con;
    PreparedStatement pre;
    ResultSet res;
    ConexionMySql conexion = new ConexionMySql();
    ObservableList<Venta> ventasSeleccionadas = FXCollections.observableArrayList();
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance();
    DateTimeFormatter formatofecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Venta venta;

    @FXML
    private TextField pendeintesVencidas;
    @FXML
    private TextField totalVencido;
    @FXML
    private TextField totalFacturas;
    @FXML
    private TextField totalCartera;
    @FXML
    private Button pagarFacturas;
    @FXML
    private Button salirPagos;
    @FXML
    private TextField facturasPagar;
    @FXML
    private TextField totalCorriente;
    @FXML
    private TextField buscarCliente;
    @FXML
    private TextField BuscarNitCliente;
    @FXML
    private TableView<Venta> tablaFacturas;
    @FXML
    private TableColumn<Venta, Integer> id;
    @FXML
    private TableColumn<Venta, String> factura;
    @FXML
    private TableColumn<Venta, String> valor;
    @FXML
    private TableColumn<Venta, String> expedicion;
    @FXML
    private TableColumn<Venta, String> vencimiento;
    @FXML
    private TableColumn<Venta, String> diasvencido;
    @FXML
    private TableColumn<Venta, String> plazo;
    @FXML
    private TableColumn<Venta, CheckBox> pagar;
    @FXML
    private TextField total_a_Pagar;
    @FXML
    private Label txtadvertencia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Label mesaje = new Label("Consultar Facturas a Credito por Pagar");
        mesaje.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: gray;");
        tablaFacturas.setPlaceholder(mesaje);

        id.setCellValueFactory(new PropertyValueFactory<>("id_venta"));
        factura.setCellValueFactory(new PropertyValueFactory<>("factura"));
        valor.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            String valorfactura = formatoMoneda.format(venta.getAbonoscredito());

            return new SimpleStringProperty(valorfactura);

        });
        expedicion.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            java.sql.Date fechaDate = (java.sql.Date) venta.getFechaActual();

            if (fechaDate != null) {
                LocalDate fechaLocalDate = fechaDate.toLocalDate();
                String fechaAS = formatofecha.format(fechaLocalDate);

                return new SimpleStringProperty(fechaAS);

            } else {

                return new SimpleStringProperty("Fecha Error");
            }

        });

        vencimiento.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            java.sql.Date fechaDate = (java.sql.Date) venta.getFechavencimiento();

            if (fechaDate != null) {
                LocalDate fechaLocalDate = fechaDate.toLocalDate();
                String fechaAS = formatofecha.format(fechaLocalDate);

                return new SimpleStringProperty(fechaAS);

            } else {

                return new SimpleStringProperty("Fecha Error");
            }

        });

        diasvencido.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            LocalDate fechaActual = LocalDate.now();
            java.sql.Date fechaDateVencimiento = (java.sql.Date) venta.getFechavencimiento();

            LocalDate fechaVencimiento = null;
            try {
                fechaVencimiento = fechaDateVencimiento.toLocalDate();
            } catch (Exception e) {
                System.err.println("Error al convertir la fecha: " + e.getMessage());
            }

            if (fechaVencimiento == null) {
                // Retornar un valor predeterminado en caso de error
                return new SimpleStringProperty("Error en fecha");
            }

            StringBuilder concatenar = new StringBuilder();
            int dias = (int) ChronoUnit.DAYS.between(fechaVencimiento, fechaActual);
            String dias_de_Vencimiento;

            if (dias == 1) {

                dias_de_Vencimiento = concatenar.append(dias).append(" Dia Vencida").toString();

            } else if (dias > 1) {

                dias_de_Vencimiento = concatenar.append(dias).append(" Dias Vencida").toString();

            } else if (dias == 0) {

                dias_de_Vencimiento = "Se vence Hoy";

            } else if (dias == -1) {

                dias_de_Vencimiento = "Vence Mañana";

            } else if (dias <= -2) {

                String recortar[] = String.valueOf(dias).split("-");
                dias_de_Vencimiento = concatenar.append(recortar[1]).append(" Dias x Vencerse").toString();

            } else {

                dias_de_Vencimiento = "";
            }

            return new SimpleStringProperty(dias_de_Vencimiento);

        });//calcular

        plazo.setCellValueFactory(new PropertyValueFactory<>("diasDePlazo"));
        pagar.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            CheckBox checkBox = new CheckBox();

            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    // Si se selecciona, agregar la venta a la lista de ventas seleccionadas
                    ventasSeleccionadas.add(venta);
                    almacenar();

                } else {
                    // Si se deselecciona, remover la venta de la lista de ventas seleccionadas
                    ventasSeleccionadas.remove(venta);
                    almacenar();

                }
            });

            return new SimpleObjectProperty<>(checkBox);

        });//generar

        factura.setCellFactory(column -> new CenteredTableCell<>());
        valor.setCellFactory(column -> new CenteredTableCell<>());
        expedicion.setCellFactory(column -> new CenteredTableCell<>());
        vencimiento.setCellFactory(column -> new CenteredTableCell<>());
        diasvencido.setCellFactory(column -> new CenteredTableCell<>());
        plazo.setCellFactory(column -> new CenteredTableCell<>());
        pagar.setCellFactory(column -> new CenteredTableCell<>());

        pagarFacturas.setDisable(true);

    }

    @FXML
    private void aplicarpago(ActionEvent event) {

        ((Node) (event.getSource())).getScene().getWindow().hide();

        FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Pagos/PagosCreditos.fxml"));
        Parent root = null;

        try {
            root = cargar.load();
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }

        PagosCreditosController pagarfacturas = cargar.getController();
        pagarfacturas.recibirfacturas(ventasSeleccionadas);
        pagarfacturas.ModuloPagosCreditosController(this);
        pagarfacturas.recibirvalorapagar(total_a_Pagar_Global);
        pagarfacturas.puntero(1);
        
        boolean dato = false;
        String cliente = null;
        String nit = null;

        for (Venta reco : tablaFacturas.getItems()) {

            while (!dato) {

                cliente = reco.getNombreCliente();
                nit = String.valueOf(reco.getIdentificacionCliente());
                dato = true;

            }

            if (dato) {
                break;
            }
        }

        pagarfacturas.clientesventas(cliente, nit);
        

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    private void salir(ActionEvent event) {

        ((Node) (event.getSource())).getScene().getWindow().hide();

        Parent cargar = null;
        try {
            cargar = FXMLLoader.load(getClass().getResource("/Vista/MenuPrincipal.fxml"));
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        Scene escena = new Scene(cargar);
        Stage stage = new Stage();
        stage.setScene(escena);
        stage.show();

    }

    @FXML
    private void tablavencimiento(MouseEvent event) {
    }

    private void buscarNitCliente(ActionEvent event) {

    }

    @FXML
    private void buscarCliente(KeyEvent event) {

        String nombreCliente = buscarCliente.getText();

        if (nombreCliente.isEmpty()) {

            Label mesaje = new Label("Consultar Facturas a Credito por Pagar");
            mesaje.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: gray;");
            tablaFacturas.setPlaceholder(mesaje);
            tablaFacturas.getItems().clear();
            totalCartera.setText("");
            totalCorriente.setText("");
            totalVencido.setText("");
            totalFacturas.setText("");
            pendeintesVencidas.setText("");
            ventasSeleccionadas.clear();
            facturasPagar.setText("");
            total_a_Pagar.setText("");
            pagarFacturas.setDisable(true);

        } else {

            filtrar_por_Cliente(nombreCliente);
            sumar();
            reiniciarDatos();
            ventasSeleccionadas.clear();
            facturasPagar.setText("");
            total_a_Pagar.setText("");
           
        }
    }

    @FXML
    private void buscarNitCliente(KeyEvent event) {

        String nitCliente = BuscarNitCliente.getText();

        if (nitCliente.isEmpty()) {

            Label mesaje = new Label("Consultar Facturas a Credito por Pagar");
            mesaje.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: gray;");
            tablaFacturas.setPlaceholder(mesaje);
            tablaFacturas.getItems().clear();
            totalCartera.setText("");
            totalCorriente.setText("");
            totalVencido.setText("");
            totalFacturas.setText("");
            pendeintesVencidas.setText("");
            ventasSeleccionadas.clear();
            facturasPagar.setText("");
            total_a_Pagar.setText("");
            pagarFacturas.setDisable(true);
        } else {

            filtrar_por_nit(nitCliente);
            sumar();
            reiniciarDatos();
            ventasSeleccionadas.clear();
            facturasPagar.setText("");
            total_a_Pagar.setText("");
            
        }
    }

    @FXML
    private void buscarNitCliente2(ActionEvent event) {

        String nitCliente = BuscarNitCliente.getText();
        filtrar_por_nit_Enter(nitCliente);
        sumar();
        reiniciarDatos();
        ventasSeleccionadas.clear();
        facturasPagar.setText("");
        total_a_Pagar.setText("");
       
    }

    public void reiniciarDatos() {

        if (tablaFacturas.getItems().isEmpty()) {

            totalCartera.setText("");
            totalCorriente.setText("");
            totalVencido.setText("");
            totalFacturas.setText("");
            pendeintesVencidas.setText("");

        }

    }

    public void filtrar_por_Cliente(String dato) {//metodo que filtra al escribir el dato se va mostrando

        ObservableList<Venta> almacenar = FXCollections.observableArrayList();

        String nombrecliente = "" + dato + "%";
        String sql = "SELECT * FROM ventas WHERE tipofactura = 'Credito' AND estatus = 'pendiente' AND nombrecliente LIKE " + '"' + nombrecliente + '"';

        try {

            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            res = pre.executeQuery();

            while (res.next()) {
                Venta cliente = new Venta();
                cliente.setId_venta(res.getInt("id_Venta"));
                cliente.setNombreCliente(res.getString("nombrecliente"));
                cliente.setIdentificacionCliente(res.getInt("nit"));
                cliente.setFactura(res.getString("factura"));
                cliente.setAbonoscredito(res.getInt("saldofactura"));
                cliente.setFechaActual(res.getDate("fecha"));
                cliente.setFechavencimiento(res.getDate("vencimiento"));
                cliente.setDiasDePlazo(res.getString("plazopago"));
                almacenar.add(cliente);
            }

            tablaFacturas.setItems(almacenar);

            tablaFacturas.refresh();

        } catch (SQLException e) {
            System.out.println(e.toString());

        }

    }

    public void filtrar_por_nit(String dato) {

        ObservableList<Venta> almacenar = FXCollections.observableArrayList();

        String nitCliente = "" + dato + "%";
        String sql = "SELECT * FROM ventas WHERE tipofactura = 'Credito' AND estatus = 'pendiente' AND nit LIKE " + '"' + nitCliente + '"';

        try {

            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            res = pre.executeQuery();

            while (res.next()) {
                Venta cliente = new Venta();
                cliente.setId_venta(res.getInt("id_Venta"));
                cliente.setNombreCliente(res.getString("nombrecliente"));
                cliente.setIdentificacionCliente(res.getInt("nit"));
                cliente.setFactura(res.getString("factura"));
                cliente.setAbonoscredito(res.getInt("saldofactura"));
                cliente.setFechaActual(res.getDate("fecha"));
                cliente.setFechavencimiento(res.getDate("vencimiento"));
                cliente.setDiasDePlazo(res.getString("plazopago"));
                almacenar.add(cliente);
            }

            tablaFacturas.setItems(almacenar);
            tablaFacturas.refresh();

        } catch (SQLException e) {
            System.out.println(e.toString());

        }

    }

    public void filtrar_por_nit_Enter(String dato) {//metodo que filtra al dar enter en textfield nit

        ObservableList<Venta> almacenar = FXCollections.observableArrayList();

        String sql = "SELECT * FROM ventas WHERE tipofactura = 'Credito' AND estatus = 'pendiente' AND nit = '" + dato + "'";

        try {

            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            res = pre.executeQuery();

            while (res.next()) {
                Venta cliente = new Venta();
                cliente.setId_venta(res.getInt("id_Venta"));
                cliente.setNombreCliente(res.getString("nombrecliente"));
                cliente.setIdentificacionCliente(res.getInt("nit"));
                cliente.setFactura(res.getString("factura"));
                cliente.setAbonoscredito(res.getInt("saldofactura"));
                cliente.setFechaActual(res.getDate("fecha"));
                cliente.setFechavencimiento(res.getDate("vencimiento"));
                cliente.setDiasDePlazo(res.getString("plazopago"));
                almacenar.add(cliente);
            }

            tablaFacturas.setItems(almacenar);
            tablaFacturas.refresh();

        } catch (SQLException e) {
            System.out.println(e.toString());

        }

    }

    int total_a_Pagar_Global;

    public void almacenar() {//metodo para almacenar lo que el usuario vaya seleccionando

        int totalfacturas_a_Pagar = 0;
        int totalApagar = 0;

        for (Venta reco : ventasSeleccionadas) {

            total_a_Pagar_Global += reco.getAbonoscredito();
            totalApagar += reco.getAbonoscredito();
            totalfacturas_a_Pagar += 1;

        }

        total_a_Pagar.setText(formatoMoneda.format(totalApagar));
        facturasPagar.setText(String.valueOf(totalfacturas_a_Pagar));

        int validar = Integer.parseInt(facturasPagar.getText());

        if (validar > 0) {

            pagarFacturas.setDisable(false);
        } else {

            pagarFacturas.setDisable(true);

        }
    }

    public void sumar() {//metodo para sumar la cartera, corriente, vencidos y cantidades

        int totalCarteraFacturas = 0;
        int totalCorrienteFacturas = 0;
        int totalVencidoFacturas = 0;
        int totalfacturasCliente = 0;
        int pendientesvencidas = 0;

        for (Venta reco : tablaFacturas.getItems()) {

            LocalDate fechaActual = LocalDate.now();
            java.sql.Date fechaDateVencimiento = (java.sql.Date) reco.getFechavencimiento();
            LocalDate fechaVencimiento = fechaDateVencimiento.toLocalDate();

            int dias = (int) ChronoUnit.DAYS.between(fechaVencimiento, fechaActual);

            if (tablaFacturas.getItems().isEmpty()) {

                totalCartera.setText("");
                totalVencido.setText("");
                totalCorriente.setText("");

            } else {

                totalCarteraFacturas += reco.getAbonoscredito();
                totalCartera.setText(formatoMoneda.format(totalCarteraFacturas));

                if (dias >= 1) {

                    totalVencidoFacturas += reco.getAbonoscredito();
                    totalVencido.setText(formatoMoneda.format(totalVencidoFacturas));
                    pendientesvencidas += 1;

                } else if (dias <= 0) {

                    totalCorrienteFacturas += reco.getAbonoscredito();
                    totalCorriente.setText(formatoMoneda.format(totalCorrienteFacturas));
                }

            }

            totalfacturasCliente += 1;
        }

        totalFacturas.setText(String.valueOf(totalfacturasCliente));
        pendeintesVencidas.setText(String.valueOf(pendientesvencidas));
    }

    @FXML
    private void validartiponit(KeyEvent event) {
        
         String valor = BuscarNitCliente.getText();

        if (!valor.matches("\\d*")) {

            BuscarNitCliente.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 10) {

            BuscarNitCliente.setText(valor.substring(0, 10));
            txtadvertencia.setText("¡Maximo 10 digitos!");
        }

        BuscarNitCliente.positionCaret(valor.length());
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
