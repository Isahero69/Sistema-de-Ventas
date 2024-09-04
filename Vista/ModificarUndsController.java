package Vista;

import Controladora.Venta;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModificarUndsController implements Initializable {

    private VentasController ventasController;
    private Venta venta;
    @FXML
    private Button actualizar;
    @FXML
    private TextField nuevasunidades;
    @FXML
    private Button salir;

    public void ventanaVentas(VentasController ventasController) {

        this.ventasController = ventasController;

    }

    private int idM;

    public void idModificado(int id) {

        this.idM = id;

    }

    public void modificarUnidades(ActionEvent event) {

        String unidades = nuevasunidades.getText();

        if (unidades.isEmpty()) {

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Campo Vacio");
            alerta.setHeaderText(null);
            alerta.setContentText("Ingresa Cantidades a Modificar");
            alerta.showAndWait();

        } else {

            int nuevoNumero = Integer.parseInt(unidades);
            ventasController.modificarUnidadesVentas(nuevoNumero, idM);
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        nuevasunidades.requestFocus();

    }

    @FXML
    private void actualizar(ActionEvent event) {

        modificarUnidades(event);

    }

    @FXML
    private void nuevasunidades(ActionEvent event) {

        modificarUnidades(event);

    }

    @FXML
    private void salir(ActionEvent event) {
        Stage stage = (Stage) salir.getScene().getWindow();
        stage.close();

    }

}
