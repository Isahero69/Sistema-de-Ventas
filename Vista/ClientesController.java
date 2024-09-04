package Vista;

import Controladora.Clientes;
import Modelo.ClientesDao;
import Modelo.ConexionMySql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.ResultSet;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

public class ClientesController implements Initializable {

    EditarClienteController editar = new EditarClienteController();

    ClientesDao clientes = new ClientesDao();
    Connection con;
    PreparedStatement pre;
    ResultSet res;
    ConexionMySql conexion = new ConexionMySql();

    @FXML
    private TextField buscarCliente;

    @FXML
    private Button salir, regresar, editarCliente, eliminarCliente, crearCliente;

    @FXML
    private TableView<Clientes> tabla;

    @FXML
    private TableColumn<Clientes, Integer> id, NIT, Contacto;

    @FXML
    private TableColumn<Clientes, String> Nombre, Correo, Direccion, Naturaleza, Tipo_Credito, Dias_Plazo;

    @FXML
    public void crearCliente(ActionEvent e) throws IOException {

        FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Vista/crearCliente.fxml"));
        Parent root = cargar.load();

        CrearClienteController crearCliente = cargar.getController();
        crearCliente.clientesController(this);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    private void escribir(KeyEvent event) {

        String valor = buscarCliente.getText();
        filtrarCliente(valor);

        if (tabla.getItems().isEmpty()) {

            Label mesaje = new Label("Sin registros en base de datos");
            mesaje.setStyle("-fx-font-size: 20px; -fx-font-style: italic; -fx-text-fill: gray;");
            tabla.setPlaceholder(mesaje);
        }

    }

    public void filtrarCliente(String valorBuscado) {

        clientes.cliente().clear();
        ObservableList<Clientes> almacenar = FXCollections.observableArrayList();

        String valor = "" + valorBuscado + "%";
        String sql = "SELECT * FROM clientes WHERE Nombre LIKE " + '"' + valor + '"';

        try {

            con = conexion.getConnection();
            pre = con.prepareStatement(sql);
            res = pre.executeQuery();

            while (res.next()) {
                Clientes cliente = new Clientes();
                cliente.setId_Cliente(res.getInt("id_Clientes"));
                cliente.setIdentificacionCliente(res.getInt("Nit"));
                cliente.setNombreCliente(res.getString("Nombre"));
                cliente.setContactoCliente(res.getLong("Contacto"));
                cliente.setCorreoCliente(res.getString("Correo"));
                cliente.setDireccionCliente(res.getString("Direccion"));
                cliente.setNaturalezaCliente(res.getString("Naturaleza"));
                cliente.setTipoCliente(res.getString("TipoCredito"));
                cliente.setDiasDePlazo(res.getString("DiasPlazo"));
                almacenar.add(cliente);

            }

            tabla.setItems(almacenar);

        } catch (SQLException e) {
            System.out.println(e.toString());

        }

    }

    @FXML
    public void editarCliente(ActionEvent event) throws IOException {

        int i = tabla.getSelectionModel().getSelectedIndex();

        if (i < 0) {

            JOptionPane.showMessageDialog(null, "No Existe Registro Seleccionado en la Tabla", "Seleccione un registro", JOptionPane.INFORMATION_MESSAGE);

        } else if (i >= 0) {

            FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Vista/editarCliente.fxml"));
            Parent root = cargar.load();

            EditarClienteController crearCliente = cargar.getController();
            crearCliente.clientesController(this);
            crearCliente.cargarDatos(tabla.getSelectionModel().getSelectedItem(), event);
            crearCliente.validarOriginales(tabla.getSelectionModel().getSelectedItems());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        }

    }

    @FXML
    public void eliminarCliente(ActionEvent e) {

        int validar = tabla.getSelectionModel().getSelectedIndex();

        if (validar < 0) {

            JOptionPane.showMessageDialog(null, "No Existe Registro Seleccionado en la Tabla", "Seleccione un registro", JOptionPane.INFORMATION_MESSAGE);

        } else if (validar >= 0) {

            int i = JOptionPane.showConfirmDialog(null, "¿Esta Seguro de Eliminar el Registro?", "Confirmar Accion", JOptionPane.INFORMATION_MESSAGE);

            switch (i) {
                case 0:
                    int id = tabla.getSelectionModel().getSelectedItem().getId_Cliente();
                    clientes.eliminarCliente(id);
                    recargarTabla();
                    JOptionPane.showMessageDialog(null, "Registro Eliminado Correctamente", "Confirmacion", JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 1:
                    break;

            }

        }

    }

    @FXML
    public void regresar(ActionEvent e) throws IOException {

        ((Node) (e.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource("/Vista/MenuPrincipal.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void seleccionar(MouseEvent event) {
        tabla.setOnMouseClicked(ir -> {

            if (ir.getClickCount() == 2) {

                try {
                    FXMLLoader cargar = new FXMLLoader(getClass().getResource("/Vista/editarCliente.fxml"));
                    Parent root = cargar.load();

                    EditarClienteController crearCliente = cargar.getController();
                    crearCliente.clientesController(this);
                    crearCliente.cargarDatos(tabla.getSelectionModel().getSelectedItem(), event);
                    crearCliente.validarOriginales(tabla.getSelectionModel().getSelectedItems());

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
    public void salir(ActionEvent e) {

    }

    public void recargarTabla() {

        tabla.refresh();

        id.setCellValueFactory(new PropertyValueFactory<>("id_Cliente"));
        NIT.setCellValueFactory(new PropertyValueFactory<>("identificacionCliente"));
        Contacto.setCellValueFactory(new PropertyValueFactory<>("contactoCliente"));
        Nombre.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        Correo.setCellValueFactory(new PropertyValueFactory<>("correoCliente"));
        Direccion.setCellValueFactory(new PropertyValueFactory<>("direccionCliente"));
        Naturaleza.setCellValueFactory(new PropertyValueFactory<>("naturalezaCliente"));
        Tipo_Credito.setCellValueFactory(new PropertyValueFactory<>("tipoCliente"));
        Dias_Plazo.setCellValueFactory(new PropertyValueFactory<>("diasDePlazo"));

        tabla.setItems(clientes.cliente());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        recargarTabla();

    }

}
