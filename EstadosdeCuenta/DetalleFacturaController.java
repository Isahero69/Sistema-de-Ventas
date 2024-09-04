package EstadosdeCuenta;

import Controladora.Venta;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DetalleFacturaController implements Initializable {

    private ObservableList<Venta> detallado;
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance();

    @FXML
    private AnchorPane tablaDetalle;
    @FXML
    private Label titulo;
    @FXML
    private TableView<Venta> tabladetalle;
    @FXML
    private TableColumn<Venta, String> id;
    @FXML
    private TableColumn<Venta, String> item;
    @FXML
    private TableColumn<Venta, String> codigo;
    @FXML
    private TableColumn<Venta, String> producto;
    @FXML
    private TableColumn<Venta, String> cantidades;
    @FXML
    private TableColumn<Venta, String> precio;
    @FXML
    private TableColumn<Venta, String> subtotal;
    @FXML
    private TableColumn<Venta, String> iva;
    @FXML
    private TableColumn<Venta, String> total;
    @FXML
    private Button salirDetalle;
    @FXML
    private TextField nombreCliente;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        id.setCellValueFactory(new PropertyValueFactory<>("id_ventaD"));
        item.setCellValueFactory(new PropertyValueFactory<>("item"));
        codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        producto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        cantidades.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        precio.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            String precio = formatoMoneda.format(venta.getPrecio());

            return new SimpleStringProperty(precio);

        });
        subtotal.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            String subtotal = formatoMoneda.format(venta.getSubtotal());

            return new SimpleStringProperty(subtotal);

        });
        iva.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            String iva = formatoMoneda.format(venta.getValorIva());

            return new SimpleStringProperty(iva);

        });
        total.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            String total = formatoMoneda.format(venta.getTotal());

            return new SimpleStringProperty(total);

        });

        item.setCellFactory(column -> new CenteredTableCell<>());
        codigo.setCellFactory(column -> new CenteredTableCell<>());
        producto.setCellFactory(column -> new CenteredTableCell<>());
        cantidades.setCellFactory(column -> new CenteredTableCell<>());
        precio.setCellFactory(column -> new CenteredTableCell<>());
        subtotal.setCellFactory(column -> new CenteredTableCell<>());
        iva.setCellFactory(column -> new CenteredTableCell<>());
        total.setCellFactory(column -> new CenteredTableCell<>());

    }

    @FXML
    private void salirDetalle(ActionEvent event) {

        Stage stage = (Stage) salirDetalle.getScene().getWindow();
        stage.close();
    }

    public void detallefacturas(ObservableList<Venta> detallado) {

        this.detallado = detallado;

        tabladetalle.setItems(detallado);

    }

    public void nombreyfactura(String nombre, String factura) {

        titulo.setText("Detalle Factura N° " + factura);
        nombreCliente.setText(nombre);

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
