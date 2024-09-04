package Vista;

import Controladora.Productos;
import Modelo.ProductosDao;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CrearProductosController implements Initializable {

    Productos producto = new Productos();
    ProductosDao productoDao = new ProductosDao();
    private ProductosController crearProductosController;

    @FXML
    private TextField codigo;
    @FXML
    private TextField nombre;
    @FXML
    private TextField inventario;
    @FXML
    private TextField precio;
    @FXML
    private ComboBox<String> impuesto;
    @FXML
    private ComboBox<String> presentacion;
    @FXML
    private ComboBox<String> marca;
    @FXML
    private ComboBox<String> TipoProducto;
    @FXML
    private Button guardar;
    @FXML
    private Button regresar;
    @FXML
    private Label txtadvertencia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        marca.getItems().addAll("Colombiana", "Pepsi", "7 Up", "Hipinto", "Jugo Hit", "Hatsu", "Agua Cristal", "Agua Oasis", "Gatorade");
        TipoProducto.getItems().addAll("Gaseosa", "Agua", "Hidratante", "Jugos");
        presentacion.getItems().addAll("3.000ml", "2.500ml", "1.500ml", "1.000ml", "800ml", "500ml", "400ml", "250ml");
        impuesto.getItems().addAll("No", "IVA 19%", "IVA 10%", "IBUA 5%", "ICO 6%");
    }

    public void crearProducto(ProductosController crearProductosController) {

        this.crearProductosController = crearProductosController;
    }

    @FXML
    private void presentacion(ActionEvent event) {
    }

    @FXML
    private void marca(ActionEvent event) {
    }

    @FXML
    private void TipoProducto(ActionEvent event) {
    }

    @FXML
    private void impuesto(ActionEvent event) {

    }

    @FXML
    private void guardarProducto(ActionEvent event) throws IOException {

        String validar[] = new String[8];
        validar[0] = codigo.getText();
        validar[1] = nombre.getText();
        validar[2] = inventario.getText();
        validar[3] = precio.getText();
        validar[4] = impuesto.getValue();
        validar[5] = presentacion.getValue();
        validar[6] = marca.getValue();
        validar[7] = TipoProducto.getValue();

        ArrayList<String> agregar = new ArrayList();
        String campos[] = {"Codigo",
            "Nombre Producto",
            "Inventario",
            "Precio",
            "Impuesto",
            "Presentacion",
            "Marca",
            "Tipo Producto"};

        for (int i = 0; i < validar.length; i++) {

            if (validar[i] == null || validar[i].isEmpty()) {
                agregar.add(campos[i]);

            }

        }

        if (agregar.size() == 1) {

            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setTitle("Campo Vacio");
            alerta.setHeaderText(null);
            alerta.setContentText("El Campo " + agregar + " Esta Vacio");
            alerta.showAndWait();

        } else if (agregar.size() > 1) {

            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setTitle("Campos Vacios");
            alerta.setHeaderText(null);
            alerta.setContentText("Los Campos " + agregar + " Estan Vacios");
            alerta.showAndWait();

        } else {

            producto.setCodigoProducto(Integer.parseInt(codigo.getText()));
            producto.setNombreProducto(nombre.getText());
            producto.setInventario(Integer.parseInt(inventario.getText()));
            producto.setPrecioProducto(Integer.parseInt(precio.getText()));
            producto.setImpuestoProducto(impuesto.getValue());
            producto.setPresentacionProducto(presentacion.getValue());
            producto.setMarcaProducto(marca.getValue());
            producto.setTipoProducto(TipoProducto.getValue());
            productoDao.crearProducto(producto);

            codigo.setText("");
            nombre.setText("");
            inventario.setText("");
            precio.setText("");
            impuesto.setValue("Seleccione");
            presentacion.setValue("Seleccione");
            marca.setValue("Seleccione");
            TipoProducto.setValue("Seleccione");

            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setTitle("Producto Creado");
            alerta.setHeaderText(null);
            alerta.setContentText("El producto fue  creado correctamente");
            alerta.showAndWait();

            crearProductosController.recargarTabla();

            ((Node) (event.getSource())).getScene().getWindow().hide();

        }

    }

    @FXML
    private void regresar(ActionEvent event) throws IOException {

        ((Node) (event.getSource())).getScene().getWindow().hide();

    }

    @FXML
    private void codigo(ActionEvent event) {

        nombre.requestFocus();

    }

    @FXML
    private void validartextoCodigo(KeyEvent event) {

        String valor = codigo.getText();

        if (!valor.matches("\\d*")) {

            codigo.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 8) {

            codigo.setText(valor.substring(0, 8));
            txtadvertencia.setText("¡Maximo 8 digitos!");
        }

        codigo.positionCaret(valor.length());
    }

    @FXML
    private void nombreproducto(ActionEvent event) {

        inventario.requestFocus();
    }

    @FXML
    private void inventario(ActionEvent event) {
        precio.requestFocus();
    }

    @FXML
    private void validartextoinventario(KeyEvent event) {

        String valor = inventario.getText();

        if (!valor.matches("\\d*")) {

            inventario.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 10) {

            inventario.setText(valor.substring(0, 10));
            txtadvertencia.setText("¡Maximo 10 digitos!");
        }

        inventario.positionCaret(valor.length());
    }

    @FXML
    private void validartextoprecio(KeyEvent event) {

        String valor = precio.getText();

        if (!valor.matches("\\d*")) {

            precio.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 10) {

            precio.setText(valor.substring(0, 10));
            txtadvertencia.setText("¡Maximo 10 digitos!");
        }

        precio.positionCaret(valor.length());
    }

}
