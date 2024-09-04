package Pagos;

import Controladora.Clientes;
import Vista.VentasController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class OpcionesFacturasVencidasController implements Initializable {

    private VentasController ventasController;

    @FXML
    private Button verfacturas;
    @FXML
    private Button continuar;
    @FXML
    private Button regresar;
    @FXML
    private Label mensaje;
    @FXML
    private Label titulo;
    @FXML
    private Label titulo1;
    @FXML
    private Label numero;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void ventanaVentas(VentasController ventasController) {
        this.ventasController = ventasController;
    }

    @FXML
    private void verfacturas(ActionEvent event) throws IOException {

        ventasController.listarecibida();

        Stage stage = (Stage) verfacturas.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void continuar(ActionEvent event) {

        ventasController.valorRecibido();

        Stage stage = (Stage) continuar.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void regresar(ActionEvent event) throws IOException {

        ventasController.cerrarventana(event);

        Stage stage = (Stage) regresar.getScene().getWindow();
        stage.close();

    }

    public void llamardatos(Clientes dato, int cantidad) {

        String nombreCliente;
        String cantidadfacturas;

        nombreCliente = dato.getNombreCliente();
        cantidadfacturas = String.valueOf(cantidad);

        StringBuilder concatenar = new StringBuilder();

        if (cantidad == 1) {
            titulo.setText("Cliente con Factura Vencida");
            String frase = concatenar.append("El Cliente ")
                    .append(nombreCliente)
                    .append(" tiene ")
                    .append(cantidadfacturas)
                    .append(" factura vencida.").toString();

            mensaje.setText(frase);

        } else {
            titulo.setText("Cliente con Facturas Vencidas");
            String frase = concatenar.append("El Cliente ")
                    .append(nombreCliente)
                    .append(" tiene ")
                    .append(cantidadfacturas)
                    .append(" facturas vencidas.").toString();

            mensaje.setText(frase);

        }

    }
}
