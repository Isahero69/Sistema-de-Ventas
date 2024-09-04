package Vista;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MenuPrincipalController implements Initializable {

    @FXML
    private Button btnVender, btnClientes, btnProductos, btnPagarCredito;
    @FXML
    private Button btnestadoCuenta;
    @FXML
    private Button btnreporteVentas;
    @FXML
    private Button salir;

    @FXML
    private void eventAction(ActionEvent event) throws IOException {

        Object evento = event.getSource();

        if (evento.equals(btnVender)) {

            ventanaVentas("/Vista/Ventas.fxml", event);

        }
        if (evento.equals(btnClientes)) {

            ventanaClientes("/Vista/Clientes.fxml", event);

        }
        if (evento.equals(btnProductos)) {

            ventanaProductos("/Vista/Productos.fxml", event);

        }
        if (evento.equals(btnPagarCredito)) {

            ventanaPagos("/Pagos/ModuloPagosCreditos.fxml", event);

        }
        if (evento.equals(btnestadoCuenta)) {

            ventanaEstadoCuenta("/EstadosdeCuenta/EstadoCuentaCliente.fxml", event);

        }
        if (evento.equals(btnreporteVentas)) {

            ventanareporteventas("/Vista/ReporteVentas.fxml", event);

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void ventanaClientes(String url, Event event) throws IOException {

        ((Node) (event.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    public void ventanaProductos(String url, Event event) throws IOException {

        ((Node) (event.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    public void ventanaVentas(String url, Event event) throws IOException {

        ((Node) (event.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    public void ventanaPagos(String url, Event event) throws IOException {

        ((Node) (event.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    public void ventanaEstadoCuenta(String url, Event event) throws IOException {

        ((Node) (event.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    public void ventanareporteventas(String url, Event event) throws IOException {

        ((Node) (event.getSource())).getScene().getWindow().hide();

        Parent root = FXMLLoader.load(getClass().getResource(url));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void salir(ActionEvent event) {

        Platform.exit();
        System.exit(0);

    }

}
