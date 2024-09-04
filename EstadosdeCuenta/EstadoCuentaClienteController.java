package EstadosdeCuenta;

import Controladora.Venta;
import Modelo.VentasDao;
import Pagos.PagosCreditosController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class EstadoCuentaClienteController implements Initializable {

    VentasDao estadocuenta = new VentasDao();
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance();
    DateTimeFormatter formatofecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter formatofecha2 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm.ss");
    LocalDate fechaActual = LocalDate.now();
    VentasDao detallefactura = new VentasDao();

    @FXML
    private TableView<Venta> tablaEstadoCuenta;
    @FXML
    private TableColumn<Venta, Integer> id;
    @FXML
    private TableColumn<Venta, String> nombre;
    @FXML
    private TableColumn<Venta, String> tipo;
    @FXML
    private TableColumn<Venta, String> estatus;
    @FXML
    private TableColumn<Venta, String> factura;
    @FXML
    private TableColumn<Venta, String> valor;
    @FXML
    private TableColumn<Venta, String> expedicion;
    @FXML
    private TableColumn<Venta, String> vencimiento;
    @FXML
    private TableColumn<Venta, String> plazo;
    @FXML
    private TextField BuscarNitCliente;
    @FXML
    private Button ConsultarEstado;
    @FXML
    private Button descargaEstado;
    @FXML
    private Button salirEstadosCuenta;
    @FXML
    private DatePicker fechaInicial;
    @FXML
    private DatePicker fechaFinal;
    @FXML
    private TextField facturasPagar;
    @FXML
    private TextField facturasVencidas;
    @FXML
    private Button detalleFactura;
    @FXML
    private TextField valor_por_Pagar;
    @FXML
    private TextField valor_Vencido;
    @FXML
    private Label txtmensaje;
    @FXML
    private Label txtadvertencia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Label mesaje = new Label("Consultar Estados de Cuenta");
        mesaje.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: gray;");
        tablaEstadoCuenta.setPlaceholder(mesaje);

        id.setCellValueFactory(new PropertyValueFactory<>("id_venta"));
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        tipo.setCellValueFactory(new PropertyValueFactory<>("tipoCliente"));
        plazo.setCellValueFactory(new PropertyValueFactory<>("diasDePlazo"));
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

        estatus.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            String estatus = venta.getFechdepago();
            int facturasxPagar;

            if (estatus.equals("pendiente")) {

                return new SimpleStringProperty("PENDIENTE");

            } else {

                return new SimpleStringProperty("Pagada");
            }

        });

        nombre.setCellFactory(column -> new CenteredTableCell<>());
        tipo.setCellFactory(column -> new CenteredTableCell<>());
        factura.setCellFactory(column -> new CenteredTableCell<>());
        factura.setCellFactory(column -> new CenteredTableCell<>());
        factura.setCellFactory(column -> new CenteredTableCell<>());
        valor.setCellFactory(column -> new CenteredTableCell<>());
        expedicion.setCellFactory(column -> new CenteredTableCell<>());
        vencimiento.setCellFactory(column -> new CenteredTableCell<>());
        plazo.setCellFactory(column -> new CenteredTableCell<>());
        estatus.setCellFactory(column -> new CenteredTableCell<>());

        fechaInicial.setDayCellFactory(dayCellFactory);
        fechaFinal.setDayCellFactory(dayCellFactory);

    }

    @FXML
    private void descargaEstado(ActionEvent event) {

        if (tablaEstadoCuenta.getItems().isEmpty()) {

            Alert alerta = new Alert(AlertType.WARNING);
            alerta.setTitle("Sin datos");
            alerta.setHeaderText("No Existen Datos para Descargar");
            alerta.setContentText("Primero debes realizar la consulta del estado de cuenta de un NIT");
            alerta.showAndWait();

        } else {

            Stage stage = new Stage();
            LocalDateTime fechaActual = LocalDateTime.now();

            FileChooser seleccionarUbicacion = new FileChooser();
            seleccionarUbicacion.setTitle("Guardar Excel");
            seleccionarUbicacion.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos Excel", "*.xlsx"));
            seleccionarUbicacion.setInitialFileName("Estado de Cuenta " + formatofecha2.format(fechaActual));

            File file = seleccionarUbicacion.showSaveDialog(stage);

            if (file != null) {

                descargarExcel(file);

            }
        }
    }

    public void descargarExcel(File file) {

        XSSFWorkbook libro = new XSSFWorkbook();
        XSSFSheet hoja = libro.createSheet("Consulta"); //crear hoja dentro del libro
        XSSFRow encabezados = hoja.createRow(0);
        encabezados.createCell(0).setCellValue("Nombre Cliente");
        encabezados.createCell(1).setCellValue("Tipo");
        encabezados.createCell(2).setCellValue("Plazo");
        encabezados.createCell(3).setCellValue("Factura");
        encabezados.createCell(4).setCellValue("Valor Factura");
        encabezados.createCell(5).setCellValue("Fecha Expedicion");
        encabezados.createCell(6).setCellValue("Fecha Vencimiento");
        encabezados.createCell(7).setCellValue("Estatus");

        int i = 1;

        for (Venta reco : tablaEstadoCuenta.getItems()) {

            XSSFRow datos = hoja.createRow(i);
            datos.createCell(0).setCellValue(reco.getNombreCliente());
            datos.createCell(1).setCellValue(reco.getTipoCliente());
            datos.createCell(2).setCellValue(reco.getDiasDePlazo());
            datos.createCell(3).setCellValue(reco.getFactura());
            datos.createCell(4).setCellValue(reco.getTotalGeneral());
            datos.createCell(5).setCellValue(reco.getFechaActual().toString());
            datos.createCell(6).setCellValue(reco.getFechavencimiento().toString());
            datos.createCell(7).setCellValue(reco.getStatus());
            i++;
        }

        try {
            try ( FileOutputStream fichero = new FileOutputStream(file)) {
                libro.write(fichero);
                fichero.close();

                Alert alerta = new Alert(AlertType.INFORMATION);
                alerta.setTitle("Arhivo Guardado");
                alerta.setHeaderText(null);
                alerta.setContentText("El Archivo Excel se ha guardado correctamente");

                alerta.showAndWait();
            }

        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }

    }

    @FXML
    private void salirEstadosCuenta(ActionEvent event) {

        ((Node) (event.getSource())).getScene().getWindow().hide();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/Vista/MenuPrincipal.fxml"));
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

  
    @FXML
    private void BuscarNitCliente(ActionEvent event) {
    }

    @FXML
    private void ConsultarEstado(ActionEvent event) {

        String fecha1, fecha2, nitCliente;

        fecha1 = String.valueOf(fechaInicial.getValue());
        fecha2 = String.valueOf(fechaFinal.getValue());
        nitCliente = BuscarNitCliente.getText();

        LocalDate fechainicial = fechaInicial.getValue();
        LocalDate fechafinal = fechaFinal.getValue();

        String valores[] = new String[3];
        valores[0] = fecha1;
        valores[1] = fecha2;
        valores[2] = nitCliente;

        String campos[] = {"Fecha Inicial", "Fecha Final", "Nit Cliente"};

        List<String> listado = new ArrayList();

        for (int i = 0; i < valores.length; i++) {

            if (valores[i] == null || valores[i].isEmpty() || "null".equals(valores[i])) {

                listado.add(campos[i]);

            }
        }

        if (listado.size() == 1) {

            JOptionPane.showMessageDialog(null, "El campo "
                    + listado
                    + " debe ser diligenciado",
                    "Diligencie todos los campos", JOptionPane.INFORMATION_MESSAGE);

        } else if (listado.size() > 1) {

            JOptionPane.showMessageDialog(null, "Los campos "
                    + String.join(", ", listado)
                    + " deben ser diligenciados",
                    "Diligencie todos los campos", JOptionPane.INFORMATION_MESSAGE);
        } else {

            ObservableList<Venta> almacenar = FXCollections.observableArrayList();
            almacenar.addAll(estadocuenta.estados_de_Cuenta(nitCliente, fecha1, fecha2));

            if (!almacenar.isEmpty()) {

                tablaEstadoCuenta.setItems(almacenar);
                facturasxPagar(); //total facturas pendientes x pagar
                facturas_vencidas();//facturas vencidas x pagar

            } else {

                tablaEstadoCuenta.getItems().clear();
                Label mesaje = new Label("Sin Resultados para el NIT " + nitCliente + " entre fechas del " + formatofecha.format(fechainicial) + " al " + formatofecha.format(fechafinal));
                mesaje.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: gray;");
                tablaEstadoCuenta.setPlaceholder(mesaje);
                facturasVencidas.setText("");
                valor_Vencido.setText("");
                facturasPagar.setText("");
                valor_por_Pagar.setText("");

            }

        }

    }

    public void facturas_vencidas() {

        int cantidad = 0;
        double valores = 0;
        LocalDate fechavencimiento;
        LocalDate fechaActual = LocalDate.now();
        Date fechafacturaDate;

        for (Venta reco : tablaEstadoCuenta.getItems()) {

            if (reco.getFechdepago().equals("pendiente")) {

                fechafacturaDate = new Date(reco.getFechavencimiento().getTime());
                fechavencimiento = fechafacturaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if (fechaActual.compareTo(fechavencimiento) > 0) {

                    cantidad += 1;
                    valores += reco.getTotalGeneral();

                }

            }

        }

        facturasVencidas.setText(String.valueOf(cantidad));
        valor_Vencido.setText(formatoMoneda.format(valores));

    }

    public void facturasxPagar() {

        int cantidades = 0;
        double valores = 0;

        for (Venta reco : tablaEstadoCuenta.getItems()) {

            if (reco.getFechdepago().equals("pendiente")) {

                cantidades += 1;
                valores += reco.getTotalGeneral();

            }

        }

        facturasPagar.setText(String.valueOf(cantidades));
        valor_por_Pagar.setText(formatoMoneda.format(valores));

    }

    @FXML
    private void detalleFactura(ActionEvent event) {

        if (tablaEstadoCuenta.getItems().isEmpty()) {

            JOptionPane.showMessageDialog(null, "No Exiten Datos en Tabla", "Valide los Datos", JOptionPane.INFORMATION_MESSAGE);
        } else {

            int i = tablaEstadoCuenta.getSelectionModel().getSelectedIndex();

            if (i >= 0) {

                int id = tablaEstadoCuenta.getSelectionModel().getSelectedItem().getId_venta();
                String nombre = tablaEstadoCuenta.getSelectionModel().getSelectedItem().getNombreCliente();
                String factura = tablaEstadoCuenta.getSelectionModel().getSelectedItem().getFactura();

                FXMLLoader cargar = new FXMLLoader(getClass().getResource("/EstadosdeCuenta/DetalleFactura.fxml"));
                Parent root = null;
                try {
                    root = cargar.load();
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                }

                DetalleFacturaController detallado = cargar.getController();
                detallado.detallefacturas(detallefactura.consultaDetalle(id));
                detallado.nombreyfactura(nombre, factura);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            } else {

                JOptionPane.showMessageDialog(null, "Seleccione un registro", "Sin registro seleccionado", JOptionPane.INFORMATION_MESSAGE);
            }

        }

    }

    @FXML
    private void fechaInicial(ActionEvent event) {

        LocalDate fechainicial = fechaInicial.getValue();
        LocalDate fechafinal = fechaFinal.getValue();

        if (null != fechafinal) {

            if (fechainicial.compareTo(fechafinal) > 0) {//si la fecha inicial es mayor a la final

                txtmensaje.setText("La fecha Inicial "
                        + formatofecha.format(fechainicial)
                        + " es mayor a la fecha Final "
                        + formatofecha.format(fechafinal));
                txtmensaje.setStyle("-fx-font-size: 15px; "
                        + "-fx-font-family: 'Arial';"
                        + " -fx-font-weight: bold;"
                        + " -fx-text-fill: red;");

                ConsultarEstado.setDisable(true);

            } else {

                long meses = ChronoUnit.MONTHS.between(fechainicial, fechafinal);

                if (meses > 6) {

                    txtmensaje.setText("El rango de fechas no puede superar los 6 meses");
                    txtmensaje.setStyle("-fx-font-size: 15px; "
                            + "-fx-font-family: 'Arial';"
                            + " -fx-font-weight: bold;"
                            + " -fx-text-fill: red;");

                    ConsultarEstado.setDisable(true);
                } else {

                    txtmensaje.setText("");
                    ConsultarEstado.setDisable(false);
                }
            }

        }

    }

    @FXML
    private void fechaFinal(ActionEvent event) {

        LocalDate fechainicial = fechaInicial.getValue();
        LocalDate fechafinal = fechaFinal.getValue();

        if (null != fechainicial) {

            if (fechainicial.compareTo(fechafinal) > 0) {//si la fecha inicial es mayor a la final

                txtmensaje.setText("La fecha Inicial "
                        + formatofecha.format(fechainicial)
                        + " es mayor a la fecha Final "
                        + formatofecha.format(fechafinal));
                txtmensaje.setStyle("-fx-font-size: 15px; "
                        + "-fx-font-family: 'Arial';"
                        + " -fx-font-weight: bold;"
                        + " -fx-text-fill: red;");

                ConsultarEstado.setDisable(true);

            } else {

                long meses = ChronoUnit.MONTHS.between(fechainicial, fechafinal);

                if (meses > 6) {

                    txtmensaje.setText("El rango de fechas no puede superar los 6 meses");
                    txtmensaje.setStyle("-fx-font-size: 15px; "
                            + "-fx-font-family: 'Arial';"
                            + " -fx-font-weight: bold;"
                            + " -fx-text-fill: red;");

                    ConsultarEstado.setDisable(true);
                } else {

                    txtmensaje.setText("");
                    ConsultarEstado.setDisable(false);
                }
            }

        }

    }

    Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
        @Override
        public void updateItem(LocalDate item, boolean empty) {

            super.updateItem(item, empty);

            this.setDisable(false);
            this.setBackground(null);
            this.setTextFill(Color.BLACK);

            // deshabilitar las fechas futuras
            if (item.isAfter(LocalDate.now())) {
                this.setDisable(true);
            }

        }
    };

    @FXML
    private void tablaEstadoCuenta(MouseEvent event) {

        tablaEstadoCuenta.setOnMouseClicked(ir -> {

            if (ir.getClickCount() == 2) {
                
                int id = tablaEstadoCuenta.getSelectionModel().getSelectedItem().getId_venta();
                String nombre = tablaEstadoCuenta.getSelectionModel().getSelectedItem().getNombreCliente();
                String factura = tablaEstadoCuenta.getSelectionModel().getSelectedItem().getFactura();

                FXMLLoader cargar = new FXMLLoader(getClass().getResource("/EstadosdeCuenta/DetalleFactura.fxml"));
                Parent root = null;
                try {
                    root = cargar.load();
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                }

                DetalleFacturaController detallado = cargar.getController();
                detallado.detallefacturas(detallefactura.consultaDetalle(id));
                detallado.nombreyfactura(nombre, factura);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            }

        });

    }

    @FXML
    private void validarnitcliente(KeyEvent event) {
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
