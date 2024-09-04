package Vista;

import Controladora.Clientes;
import Controladora.Productos;
import Modelo.ProductosDao;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;

public class EditarProductoController implements Initializable {

    Productos producto = new Productos();
    ProductosDao productosDao = new ProductosDao();
    ObservableList<Productos> dataArray = FXCollections.observableArrayList();
    private ProductosController productosController;

    @FXML
    private TextField editarCodigo;
    @FXML
    private TextField editarproducto;
    @FXML
    private TextField editarinventario;
    @FXML
    private TextField editarprecio;
    @FXML
    private ComboBox<String> editarimpuesto;
    @FXML
    private ComboBox<String> editarpresentacion;
    @FXML
    private ComboBox<String> editarmarca;
    @FXML
    private ComboBox<String> editarcatgoria;
    @FXML
    private Button guardarcambios;
    @FXML
    private Button salir;
    @FXML
    private TextField editarID;
    @FXML
    private Label txtadvertencia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        editarmarca.getItems().addAll("Colombiana", "Pepsi", "7 Up", "Hipinto", "Jugo Hit", "Hatsu", "Agua Cristal", "Agua Oasis", "Gatorade");
        editarcatgoria.getItems().addAll("Gaseosa", "Agua", "Hidratante", "Jugos");
        editarpresentacion.getItems().addAll("3.000ml", "2.500ml", "1.500ml", "1.000ml", "800ml", "500ml", "400ml", "250ml");
        editarimpuesto.getItems().addAll("No", "IVA 19%", "IVA 10%", "IBUA 5%", "ICO 6%");

    }

    public void recibirDatosArray(ObservableList<Productos> datosProductos) {

        dataArray = datosProductos;
    }

    public void editarPro(ProductosController productosController) {

        this.productosController = productosController;

    }

    public void editarProductos(Productos producto, Event event) throws IOException {

        editarCodigo.setText(String.valueOf(producto.getCodigoProducto()));
        editarproducto.setText(String.valueOf(producto.getNombreProducto()));
        editarinventario.setText(String.valueOf(producto.getInventario()));
        editarprecio.setText(String.valueOf((int) producto.getPrecioProducto()));
        editarimpuesto.setValue(String.valueOf(producto.getImpuestoProducto()));
        editarpresentacion.setValue(String.valueOf(producto.getPresentacionProducto()));
        editarmarca.setValue(String.valueOf(producto.getMarcaProducto()));
        editarcatgoria.setValue(String.valueOf(producto.getTipoProducto()));
        editarID.setText(String.valueOf(producto.getId_Producto()));

    }

    @FXML
    private void guardarcambios(ActionEvent event) throws IOException {

        producto.setId_Producto(Integer.parseInt(editarID.getText()));
        producto.setCodigoProducto(Integer.parseInt(editarCodigo.getText()));
        producto.setNombreProducto(editarproducto.getText());
        producto.setInventario(Integer.parseInt(editarinventario.getText()));
        producto.setPrecioProducto(Integer.parseInt(editarprecio.getText()));
        producto.setImpuestoProducto(editarimpuesto.getValue());
        producto.setPresentacionProducto(editarpresentacion.getValue());
        producto.setMarcaProducto(editarmarca.getValue());
        producto.setTipoProducto(editarcatgoria.getValue());

        ObservableList<String> nuevosDatos = FXCollections.observableArrayList();
        nuevosDatos.add(producto.getId_Producto() + " "
                + producto.getCodigoProducto() + " "
                + producto.getNombreProducto() + " "
                + producto.getInventario() + " "
                + producto.getPrecioProducto() + " "
                + producto.getImpuestoProducto() + " "
                + producto.getPresentacionProducto() + " "
                + producto.getMarcaProducto() + " "
                + producto.getTipoProducto());

        ObservableList<String> datosOriginales = FXCollections.observableArrayList();

        for (Productos reco : dataArray) {
            datosOriginales.add(reco.getId_Producto() + " "
                    + reco.getCodigoProducto() + " "
                    + reco.getNombreProducto() + " "
                    + reco.getInventario() + " "
                    + reco.getPrecioProducto() + " "
                    + reco.getImpuestoProducto() + " "
                    + reco.getPresentacionProducto() + " "
                    + reco.getMarcaProducto() + " "
                    + reco.getTipoProducto());
        }

        boolean validar = false;
        for (int i = 0; i < datosOriginales.size(); i++) {

            if (datosOriginales.get(i).equals(nuevosDatos.get(i))) {
                validar = true;
            }

        }

        if (validar) {

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("No Existen Datos a Modificar");
            alerta.setHeaderText(null);
            alerta.setContentText("No se detectaron datos nuevos para grabar");
            alerta.showAndWait();

        } else {

            productosDao.editarProducto(producto);
            productosController.recargarTabla();
            
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Producto Editado");
            alerta.setHeaderText(null);
            alerta.setContentText("Producto Editado Correctamente");
            alerta.showAndWait();

            ((Node) (event.getSource())).getScene().getWindow().hide();
        }
    }

    @FXML
    private void salir(ActionEvent event) throws IOException {

        ((Node) (event.getSource())).getScene().getWindow().hide();

    }

    @FXML
    private void validarCodigo(KeyEvent event) {
        
         String valor = editarCodigo.getText();

        if (!valor.matches("\\d*")) {

            editarCodigo.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 8) {

            editarCodigo.setText(valor.substring(0, 8));
            txtadvertencia.setText("¡Maximo 8 digitos!");
        }

        editarCodigo.positionCaret(valor.length());
    }

    @FXML
    private void validarInventario(KeyEvent event) {
        
        String valor = editarinventario.getText();

        if (!valor.matches("\\d*")) {

            editarinventario.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 10) {

            editarinventario.setText(valor.substring(0, 10));
            txtadvertencia.setText("¡Maximo 10 digitos!");
        }

        editarinventario.positionCaret(valor.length());
    }

    @FXML
    private void validarPrecio(KeyEvent event) {
        
         String valor = editarprecio.getText();

        if (!valor.matches("\\d*")) {

            editarprecio.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 10) {

            editarprecio.setText(valor.substring(0, 10));
            txtadvertencia.setText("¡Maximo 10 digitos!");
        }

        editarprecio.positionCaret(valor.length());
    }

}
