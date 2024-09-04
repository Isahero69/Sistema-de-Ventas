package Pagos;

import Controladora.Clientes;
import Controladora.Venta;
import Vista.VentasController;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Date;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class ListadoFacturasVencidasController implements Initializable {

    ObservableList<Venta> ventasSeleccionadas = FXCollections.observableArrayList();
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance();
    DateTimeFormatter formatofecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static Clientes recibircliente;
    private VentasController ventasController;
    private static int recibirvalorpagar;
    private static int facturasvencidas;
    LocalDate fechaActual = LocalDate.now();

    @FXML
    private TableView<Venta> tablavencimiento;
    @FXML
    private TextField nitcliente;
    @FXML
    private TextField nombrecliente;
    @FXML
    private TextField totalpagar;
    @FXML
    private TextField cantidadfacturas;
    @FXML
    private Button salir;
    @FXML
    private Button aplicarpago;
    @FXML
    private TableColumn<Venta, String> id;
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
    private TableColumn<Venta, String> factura;
    @FXML
    private TableColumn<Venta, CheckBox> pagar;
    @FXML
    private TextField totalvencido;
    @FXML
    private TextField cantidadvencido;
    @FXML
    private TextField saldovencido;
    @FXML
    private TextField pendientes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        id.setCellValueFactory(new PropertyValueFactory<>("id_venta"));
        factura.setCellValueFactory(new PropertyValueFactory<>("factura"));
        valor.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            String valorfactura = formatoMoneda.format(venta.getTotalGeneral());

            return new SimpleStringProperty(valorfactura);

        });//valores

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

        });//fechaFactura

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

        });//fechaVencimiento
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

            } else {

                dias_de_Vencimiento = "";
            }

            return new SimpleStringProperty(dias_de_Vencimiento);

        });

        plazo.setCellValueFactory(new PropertyValueFactory<>("diasDePlazo"));

        pagar.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            CheckBox checkBox = new CheckBox();

            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    // Si se selecciona, agregar la venta a la lista de ventas seleccionadas
                    ventasSeleccionadas.add(venta);
                    almacenar();
                    saldos();

                } else {
                    // Si se deselecciona, remover la venta de la lista de ventas seleccionadas
                    ventasSeleccionadas.remove(venta);
                    almacenar();
                    saldos();

                }
            });

            return new SimpleObjectProperty<>(checkBox);

        });

        factura.setCellFactory(column -> new CenteredTableCell<>());
        valor.setCellFactory(column -> new CenteredTableCell<>());
        expedicion.setCellFactory(column -> new CenteredTableCell<>());
        vencimiento.setCellFactory(column -> new CenteredTableCell<>());
        diasvencido.setCellFactory(column -> new CenteredTableCell<>());
        plazo.setCellFactory(column -> new CenteredTableCell<>());
        pagar.setCellFactory(column -> new CenteredTableCell<>());

        aplicarpago.setDisable(true);

    }

   

    public void ventanaVentas(VentasController ventasController) {
        this.ventasController = ventasController;
    }

    public void saldos() {

        int total = facturasvencidas - recibirvalorpagar;
        saldovencido.setText(formatoMoneda.format(total));

        int total_fact = Integer.parseInt(cantidadvencido.getText()) - Integer.parseInt(cantidadfacturas.getText());
        pendientes.setText(String.valueOf(total_fact));

        int validar = Integer.parseInt(cantidadfacturas.getText());

        if (validar >= 1) {//habilita el boton pagar cuando haya mas de 0 datos seleccionados

            aplicarpago.setDisable(false);
        } else {

            aplicarpago.setDisable(true);

        }

    }

    public void almacenar() {//metodo para almacenar lo que el usuario vaya seleccionando

        int totalPagar = 0;
        int totalfacturas = 0;

        for (Venta reco : ventasSeleccionadas) {

            totalPagar += reco.getTotalGeneral();
            totalfacturas += 1;

        }

        totalpagar.setText(formatoMoneda.format(totalPagar));
        cantidadfacturas.setText(String.valueOf(totalfacturas));
        recibirvalorpagar = totalPagar;

    }

    public void cargarDatos(ObservableList<Venta> valores) {

        tablavencimiento.setItems(valores);

        if (!valores.isEmpty()) {
            pagar.setVisible(true);

        } else {
            pagar.setVisible(false);
        }

        int total = 0;
        int totalfacturas = 0;

        for (Venta item : tablavencimiento.getItems()) {
            total += item.getTotalGeneral();
            totalfacturas += 1;
        }
        totalvencido.setText(formatoMoneda.format(total));
        cantidadvencido.setText(String.valueOf(totalfacturas));
        facturasvencidas = total;
    }

    public void clientes(Clientes cliente) {

        nombrecliente.setText(cliente.getNombreCliente());
        nitcliente.setText(String.valueOf(cliente.getIdentificacionCliente()));
        recibircliente = cliente;
    }

    @FXML
    private void tablavencimiento(MouseEvent event) {

    }

    @FXML
    private void salir(ActionEvent event) throws IOException {

        Stage stage = (Stage) salir.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void aplicarpago(ActionEvent event) throws IOException {

        int cantidad_facturas = Integer.parseInt(pendientes.getText());

        if (cantidad_facturas == 1) {

            int respuesta = JOptionPane.showConfirmDialog(null, "Queda " + cantidad_facturas + " Factura Pendiente. ¿Deseas Continuar?", cantidad_facturas + " Factura Pendiente x Seleccionar", JOptionPane.INFORMATION_MESSAGE);

            if (respuesta == 0) {

                ((Node) (event.getSource())).getScene().getWindow().hide();

                FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Pagos/PagosCreditos.fxml"));
                Parent root = cargar.load();

                PagosCreditosController pagarfacturas = cargar.getController();
                pagarfacturas.puntero(2);
                pagarfacturas.recibirfacturas(ventasSeleccionadas);
                pagarfacturas.clientes(recibircliente);
                pagarfacturas.recibirvalorapagar(recibirvalorpagar);
                pagarfacturas.ventanaVentas(ventasController);
                pagarfacturas.facturapendiente(this);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            }

        } else if (cantidad_facturas > 1) {

            int respuesta = JOptionPane.showConfirmDialog(null, "Quedan " + cantidad_facturas + " Facturas Pendientes. ¿Deseas Continuar?", cantidad_facturas + " Facturas Pendientes x Seleccionar", JOptionPane.INFORMATION_MESSAGE);

            if (respuesta == 0) {

                ((Node) (event.getSource())).getScene().getWindow().hide();

                FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Pagos/PagosCreditos.fxml"));
                Parent root = cargar.load();

                PagosCreditosController pagarfacturas = cargar.getController();
                pagarfacturas.puntero(2);
                pagarfacturas.recibirfacturas(ventasSeleccionadas);
                pagarfacturas.clientes(recibircliente);
                pagarfacturas.recibirvalorapagar(recibirvalorpagar);
                pagarfacturas.ventanaVentas(ventasController);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            }

        } else {

            ((Node) (event.getSource())).getScene().getWindow().hide();

            FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Pagos/PagosCreditos.fxml"));
            Parent root = cargar.load();

            PagosCreditosController pagarfacturas = cargar.getController();
            pagarfacturas.puntero(2);
            pagarfacturas.recibirfacturas(ventasSeleccionadas);
            pagarfacturas.clientes(recibircliente);
            pagarfacturas.recibirvalorapagar(recibirvalorpagar);
            pagarfacturas.ventanaVentas(ventasController);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        }

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
