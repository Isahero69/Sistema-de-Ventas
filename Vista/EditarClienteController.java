package Vista;

import Controladora.Clientes;
import Modelo.ClientesDao;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javax.swing.JOptionPane;

public class EditarClienteController implements Initializable {

    Clientes cliente = new Clientes();
    ClientesDao modificar = new ClientesDao();
    private ClientesController clientesController;
    ObservableList<Clientes> dataArray = FXCollections.observableArrayList();

    @FXML
    private TextField editarnit;
    @FXML
    private TextField editarid;
    @FXML
    private TextField editarnombre;
    @FXML
    private TextField editarcontacto;
    @FXML
    private TextField editarcorreo;
    @FXML
    private TextField editardireccion;
    @FXML
    private ComboBox<String> editarnaturaleza;
    @FXML
    private ComboBox<String> editardiasplazo;
    @FXML
    private ComboBox<String> editartipocliente;
    @FXML
    private Button guardarcambios;
    @FXML
    private Button salir;
    @FXML
    private Label txtadvertencia;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        editarnaturaleza.getItems().addAll("Persona Juridica", "Persona Natural");
        editardiasplazo.getItems().addAll("Contado", "a 30 Dias", "a 60 Dias", "a 90 Dias");
        editartipocliente.getItems().addAll("Contado", "Credito");

    }

    public void clientesController(ClientesController clientesController) {

        this.clientesController = clientesController;

    }

    public void validarOriginales(ObservableList<Clientes> datosCliente) {

        dataArray = datosCliente;

    }

    @FXML
    public void guardarcambios(ActionEvent event) throws IOException {

        cliente.setId_Cliente(Integer.parseInt(editarid.getText()));
        cliente.setIdentificacionCliente(Integer.parseInt(editarnit.getText()));
        cliente.setNombreCliente(editarnombre.getText());
        cliente.setContactoCliente(Long.parseLong(editarcontacto.getText()));
        cliente.setCorreoCliente(editarcorreo.getText());
        cliente.setDireccionCliente(editardireccion.getText());
        cliente.setNaturalezaCliente(editarnaturaleza.getValue());
        cliente.setDiasDePlazo(editardiasplazo.getValue());
        cliente.setTipoCliente(editartipocliente.getValue());

        ObservableList<String> nuevosDatos = FXCollections.observableArrayList();
        nuevosDatos.add(cliente.getId_Cliente() + " "
                + cliente.getIdentificacionCliente() + " "
                + cliente.getNombreCliente() + " "
                + cliente.getContactoCliente() + " "
                + cliente.getCorreoCliente() + " "
                + cliente.getDireccionCliente() + " "
                + cliente.getNaturalezaCliente() + " "
                + cliente.getDiasDePlazo() + " "
                + cliente.getTipoCliente());

        ObservableList<String> datosOriginales = FXCollections.observableArrayList();

        for (Clientes reco : dataArray) {
            datosOriginales.add(reco.getId_Cliente() + " "
                    + reco.getIdentificacionCliente() + " "
                    + reco.getNombreCliente() + " "
                    + reco.getContactoCliente() + " "
                    + reco.getCorreoCliente() + " "
                    + reco.getDireccionCliente() + " "
                    + reco.getNaturalezaCliente() + " "
                    + reco.getDiasDePlazo() + " "
                    + reco.getTipoCliente());
        }

        boolean validar = false;
        for (int i = 0; i < datosOriginales.size(); i++) {

            if (datosOriginales.get(i).equals(nuevosDatos.get(i))) {
                validar = true;
            }

        }

        if (validar) {

            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setTitle("No Existen Datos a Modificar");
            alerta.setHeaderText(null);
            alerta.setContentText("No se detectaron datos nuevos para grabar");
            alerta.showAndWait();

        } else {

            modificar.editarCliente(cliente);
            clientesController.recargarTabla();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Cliente Editado");
            alerta.setHeaderText(null);
            alerta.setContentText("Cliente Editado Correctamente");
            alerta.showAndWait();

            ((Node) (event.getSource())).getScene().getWindow().hide();

        }
    }

    @FXML
    private void salir(ActionEvent event) throws IOException {

        ((Node) (event.getSource())).getScene().getWindow().hide();

    }

    public void cargarDatos(Clientes cliente, Event event) throws IOException {

        editarid.setText(String.valueOf(cliente.getId_Cliente()));
        editarnit.setText(String.valueOf(cliente.getIdentificacionCliente()));
        editarnombre.setText(cliente.getNombreCliente());
        editarcontacto.setText(String.valueOf(cliente.getContactoCliente()));
        editarcorreo.setText(cliente.getCorreoCliente());
        editardireccion.setText(cliente.getDireccionCliente());
        editarnaturaleza.setValue(cliente.getNaturalezaCliente());
        editardiasplazo.setValue(cliente.getDiasDePlazo());
        editartipocliente.setValue(cliente.getTipoCliente());

    }

    @FXML
    private void validarnit(KeyEvent event) {
        String valor = editarnit.getText();

        if (!valor.matches("\\d*")) {

            editarnit.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 10) {

            editarnit.setText(valor.substring(0, 10));
            txtadvertencia.setText("¡Maximo 10 digitos!");
        }

        editarnit.positionCaret(valor.length());
    }

    @FXML
    private void validarcontacto(KeyEvent event) {

        String valor = editarcontacto.getText();

        if (!valor.matches("\\d*")) {

            editarcontacto.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 10) {

            editarcontacto.setText(valor.substring(0, 10));
            txtadvertencia.setText("¡Maximo 10 digitos!");
        }

        editarcontacto.positionCaret(valor.length());
    }

    @FXML
    private void validarcorreo(KeyEvent event) {
        String expresion = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String valor = editarcorreo.getText();

        if (!valor.matches(expresion)) {

            txtadvertencia.setText("Formato Correo No Valido");
            guardarcambios.setDisable(true);
        } else {

            txtadvertencia.setText("");
            guardarcambios.setDisable(false);

        }
    }

}
