package Vista;

import Controladora.Clientes;
import Modelo.ClientesDao;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javax.swing.JOptionPane;

public class CrearClienteController implements Initializable {

    Clientes cliente = new Clientes();
    ClientesDao clienteDao = new ClientesDao();
    private ClientesController clientesController;

    @FXML
    private ComboBox<String> tipoCliente, naturaleza, diasPlazo;

    @FXML
    private TextField contacto, correo, nit, direccion, nombre;

    @FXML
    private Button guardar, regresar;
    @FXML
    private Label txtadvertencia;

    @FXML
    private void ingresarnaturaleza(ActionEvent e) {

    }

    public void clientesController(ClientesController clientesController) {

        this.clientesController = clientesController;

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tipoCliente.getItems().addAll("Credito", "Contado");
        naturaleza.getItems().addAll("Persona Juridica", "Persona Natural");
        diasPlazo.getItems().addAll("Contado", "a 30 Dias", "a 60 Dias", "a 90 Dias");

    }

    @FXML
    private void ingresardiasPlazo(ActionEvent e) {

        String texto = diasPlazo.getValue();

        if (texto.equals("Contado")) {
            tipoCliente.setValue("Contado");
        } else {

            tipoCliente.setValue("Credito");

        }

    }

    @FXML
    private void ingresartipoCliente(ActionEvent e) {

    }

    @FXML
    private void guardarCliente(ActionEvent e) throws IOException {

        String campos[] = new String[8];
        campos[0] = nit.getText();
        campos[1] = nombre.getText();
        campos[2] = contacto.getText();
        campos[3] = correo.getText();
        campos[4] = direccion.getText();
        campos[5] = naturaleza.getSelectionModel().getSelectedItem();
        campos[6] = diasPlazo.getSelectionModel().getSelectedItem();
        campos[7] = tipoCliente.getSelectionModel().getSelectedItem();

        ArrayList<String> agregar = new ArrayList();
        String nombres[] = {"Nit", "Nombre", "Contacto", "Correo", "Direccion", "Naturaleza", "Dias de Plazo", "Tipo de Cliente"};

        for (int i = 0; i < campos.length; i++) {
            if (campos[i] == null || campos[i].isEmpty()) {
                agregar.add(nombres[i]);

            }

        }

        if (agregar.size() == 1) {

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Todos los Campos son Obligatorios");
            alerta.setHeaderText(null);
            alerta.setContentText("El Campo " + agregar + " se Encuentra Vacio");
            alerta.showAndWait();

        } else if (agregar.size() > 1) {

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Todos los Campos son Obligatorios");
            alerta.setHeaderText(null);
            alerta.setContentText("Los Campos " + agregar + " se Encuentran Vacios");
            alerta.showAndWait();

        } else {

            capturarDatos();
            clientesController.recargarTabla();
            ((Node) (e.getSource())).getScene().getWindow().hide();

        }
    }

    @FXML
    private void regresar(ActionEvent e) throws IOException {

        ((Node) (e.getSource())).getScene().getWindow().hide();

    }

    public void capturarDatos() {

        cliente.setIdentificacionCliente(Integer.parseInt(nit.getText()));
        cliente.setNombreCliente(nombre.getText());
        cliente.setContactoCliente(Long.parseLong(contacto.getText()));
        cliente.setCorreoCliente(correo.getText());
        cliente.setDireccionCliente(direccion.getText());
        cliente.setNaturalezaCliente(naturaleza.getValue());
        cliente.setTipoCliente(tipoCliente.getValue());
        cliente.setDiasDePlazo(diasPlazo.getValue());
        clienteDao.crearCliente(cliente);

        limpiar();
        mensaje("Cliente Creado Correctamente", "Cliente Creado", "Info");

    }

    public void limpiar() {

        nit.setText("");
        nombre.setText("");
        contacto.setText("");
        correo.setText("");
        direccion.setText("");
        naturaleza.setValue("Seleccione");
        tipoCliente.setValue("Seleccione");
        diasPlazo.setValue("Seleccione");

    }

    public void mensaje(String mensaje, String titulo, String alerta) {

        switch (alerta) {

            case "Info":

                JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
                break;

            case "Error":

                JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
                break;
        }

    }

    @FXML
    private void validarNit(KeyEvent event) {

        String valor = nit.getText();

        if (!valor.matches("\\d*")) {

            nit.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 10) {

            nit.setText(valor.substring(0, 10));
            txtadvertencia.setText("¡Maximo 10 digitos!");
        }

        nit.positionCaret(valor.length());
    }

    @FXML
    private void contacto(KeyEvent event) {

        String valor = contacto.getText();

        if (!valor.matches("\\d*")) {

            contacto.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 10) {

            contacto.setText(valor.substring(0, 10));
            txtadvertencia.setText("¡Maximo 10 digitos!");
        }

        contacto.positionCaret(valor.length());
    }

    @FXML
    private void correo(KeyEvent event) {

        String expresion = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String valor = correo.getText();

        if (!valor.matches(expresion)) {

            txtadvertencia.setText("Formato Correo No Valido");
            guardar.setDisable(true);
        } else {

            txtadvertencia.setText("");
            guardar.setDisable(false);

        }

    }

    @FXML
    private void validarNit2(ActionEvent event) {

        nombre.requestFocus();
    }

    @FXML
    private void nombre2(ActionEvent event) {
        contacto.requestFocus();
    }

    @FXML
    private void contacto2(ActionEvent event) {
        correo.requestFocus();
    }

    @FXML
    private void correo2(ActionEvent event) {
        direccion.requestFocus();
    }

    @FXML
    private void direccion2(ActionEvent event) {
    }

}
