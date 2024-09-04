package Vista;

import Controladora.Clientes;
import Controladora.Productos;
import Modelo.ConexionMySql;
import Modelo.ProductosDao;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class ProductosController implements Initializable {
    
    Productos producto = new Productos();
    ProductosDao productoDao = new ProductosDao();
    EditarProductoController cargar = new EditarProductoController();
    Connection con;
    PreparedStatement pre;
    ResultSet res;
    ConexionMySql conexion = new ConexionMySql();
    
    @FXML
    private TableView<Productos> tabla;
    @FXML
    private TableColumn<Productos, Integer> id;
    @FXML
    private TableColumn<Productos, Integer> Codigo;
    @FXML
    private TableColumn<Productos, String> Nombre;
    @FXML
    private TableColumn<Productos, Integer> Inventario;
    @FXML
    private TableColumn<Productos, Integer> Precio;
    @FXML
    private TableColumn<Productos, String> Impuesto;
    @FXML
    private TableColumn<Productos, String> Presentacion;
    @FXML
    private TableColumn<Productos, String> Marca;
    @FXML
    private TableColumn<Productos, String> TipoProducto;
    @FXML
    private Button editarPro;
    @FXML
    private Button eliminarPro;
    @FXML
    private Button crearProducto;
    @FXML
    private Button regresar;
    @FXML
    private Button salir;
    @FXML
    private TextField buscarProducto;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        recargarTabla();
        
    }
    
    public void recargarTabla() {
        
        tabla.refresh();
        
        id.setCellValueFactory(new PropertyValueFactory<>("id_Producto"));
        Codigo.setCellValueFactory(new PropertyValueFactory<>("codigoProducto"));
        Nombre.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        Inventario.setCellValueFactory(new PropertyValueFactory<>("inventario"));
        Precio.setCellValueFactory(new PropertyValueFactory<>("precioProducto"));
        Impuesto.setCellValueFactory(new PropertyValueFactory<>("impuestoProducto"));
        Presentacion.setCellValueFactory(new PropertyValueFactory<>("presentacionProducto"));
        Marca.setCellValueFactory(new PropertyValueFactory<>("marcaProducto"));
        TipoProducto.setCellValueFactory(new PropertyValueFactory<>("tipoProducto"));
        
        tabla.setItems(productoDao.cargaProductos());
        
    }
    
    @FXML
    private void seleccionar(MouseEvent event) {
        
        tabla.setOnMouseClicked(ir -> {
            
            if (ir.getClickCount() == 2) {
                
                try {
                    FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Vista/EditarProducto.fxml"));
                    Parent root = cargar.load();
                    
                    EditarProductoController productos = cargar.getController();
                    productos.editarPro(this);
                    productos.editarProductos(tabla.getSelectionModel().getSelectedItem(), event);
                    productos.recibirDatosArray(tabla.getSelectionModel().getSelectedItems());
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                    
                } catch (IOException ex) {
                    
                    System.out.println(ex.toString());
                }
                
            }
            
        });
    }
    
    @FXML
    private void editarPro(ActionEvent event) throws IOException {
        
        int i = tabla.getSelectionModel().getSelectedIndex();
        
        if (i < 0) {
            
            JOptionPane.showMessageDialog(null, "No Existe Registro Seleccionado en la Tabla", "Seleccione un registro", JOptionPane.INFORMATION_MESSAGE);
            
        } else if (i >= 0) {
            
            FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Vista/EditarProducto.fxml"));
            Parent root = cargar.load();
            
            EditarProductoController productos = cargar.getController();
            productos.editarPro(this);
            productos.editarProductos(tabla.getSelectionModel().getSelectedItem(), event);
             productos.recibirDatosArray(tabla.getSelectionModel().getSelectedItems());
             
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            
        }
        
    }
    
    @FXML
    private void eliminarPro(ActionEvent event) {
        
        int validar = tabla.getSelectionModel().getSelectedIndex();
        
        if (validar < 0) {
            
            JOptionPane.showMessageDialog(null, "No Existe Producto Seleccionado", "Seleccione un Producto", JOptionPane.INFORMATION_MESSAGE);
            
        } else if (validar >= 0) {
            
            int i = JOptionPane.showConfirmDialog(null, "¿Esta Seguro de Eliminar el Producto Seleccionado?", "Confirme la Operacion", JOptionPane.INFORMATION_MESSAGE);
            
            if (i == 0) {
                
                int id = tabla.getSelectionModel().getSelectedItem().getId_Producto();
                productoDao.eliminarProducto(id);
                recargarTabla();
                JOptionPane.showMessageDialog(null, "Producto Eliminado", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
                
            }
            
        }
    }
    
    @FXML
    private void crearProducto(ActionEvent event) throws IOException {
        
        FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Vista/CrearProductos.fxml"));
        Parent root = cargar.load();
        
        CrearProductosController productos = cargar.getController();
        productos.crearProducto(this);
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        
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
    
    @FXML
    private void escribir(KeyEvent event) {
        
        String valor = buscarProducto.getText();
        filtrarCliente(valor);
        
        if (tabla.getItems().isEmpty()) {
            Label mensaje = new Label();
            mensaje.setText("Sin registro en base de datos");
            mensaje.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: gray;");
            tabla.setPlaceholder(mensaje);
            
        }
        
    }
    
    public void filtrarCliente(String valorBuscado) {
        
        productoDao.cargaProductos().clear();
        ObservableList<Productos> almacenar = FXCollections.observableArrayList();
        
        String valor = "" + valorBuscado + "%";
        String sql = "SELECT * FROM productos WHERE Nombre LIKE " + '"' + valor + '"';
        
        try {
            
            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            res = pre.executeQuery();
            
            while (res.next()) {
                Productos producto = new Productos();
                producto.setId_Producto(res.getInt("id_Producto"));
                producto.setCodigoProducto(res.getInt("codigo"));
                producto.setNombreProducto(res.getString("nombre"));
                producto.setInventario(res.getInt("inventario"));
                producto.setPrecioProducto(res.getDouble("precio"));
                producto.setImpuestoProducto(res.getString("impuesto"));
                producto.setPresentacionProducto(res.getString("presentacion"));
                producto.setMarcaProducto(res.getString("marca"));
                producto.setTipoProducto(res.getString("tipoProducto"));
                almacenar.add(producto);
                
            }
            
            tabla.setItems(almacenar);
            
        } catch (SQLException e) {
            System.out.println(e.toString());
            
        }
        
    }
    
}
