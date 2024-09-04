package Principal;

import Controladora.Usuarios;
import Modelo.UsuariosDao;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class LoginFXController implements Initializable {

    Usuarios usuario = new Usuarios();
    UsuariosDao login = new UsuariosDao();

    @FXML
    private Button btnLogin;

    @FXML
    private TextField txtIdentificacionLogin, txtContrasenaLogin;
    @FXML
    private Label txtadvertencia;

    @FXML
    private void actionEvent(ActionEvent event) throws IOException {

        iniciarSesion(event);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void enterID(ActionEvent event) {

        txtContrasenaLogin.requestFocus();
    }

    @FXML
    private void enter(ActionEvent event) {

        iniciarSesion(event);
    }

    @FXML
    private void validar(KeyEvent event) {

        String valor = txtIdentificacionLogin.getText();

        if (!valor.matches("\\d*")) {

            txtIdentificacionLogin.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        }else{
            
            txtadvertencia.setText("");
        
        }

        if (valor.length() > 10) {

            txtIdentificacionLogin.setText(valor.substring(0, 10));
            txtadvertencia.setText("¡Maximo 10 digitos!");
        }
        
        txtIdentificacionLogin.positionCaret(valor.length());

    }

    public void iniciarSesion(ActionEvent event) {

        String recorrer[] = new String[2];
        recorrer[0] = txtIdentificacionLogin.getText();
        recorrer[1] = txtContrasenaLogin.getText();

        ArrayList<String> agregar = new ArrayList();
        String textField[] = {"Identificacion", "Contraseña"};

        for (int i = 0; i < recorrer.length; i++) {

            if (recorrer[i].isEmpty()) {

                agregar.add(textField[i]);

            }
        }

        switch (agregar.size()) {
            case 1:
                JOptionPane.showMessageDialog(null, "El campo " + agregar + " se encuentra vacio", "Campo vacio", JOptionPane.INFORMATION_MESSAGE);
                break;
            case 2:
                JOptionPane.showMessageDialog(null, "Los Campos " + agregar + " se encuentran vacios", "Campos vacios", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:

                int identificacion = Integer.parseInt(txtIdentificacionLogin.getText());
                String contrasena = txtContrasenaLogin.getText();

                usuario = login.log(identificacion, contrasena);

                if (usuario.getIdenticacionUsuario() == identificacion && usuario.getContrasena().equals(contrasena)) {

                    ((Node) (event.getSource())).getScene().getWindow().hide();

                    Parent root = null;
                    try {
                        root = FXMLLoader.load(getClass().getResource("/Vista/MenuPrincipal.fxml"));
                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                    }
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();

                } else {

                    JOptionPane.showMessageDialog(null, "Datos Incorrectos", "Valide los datos ingresados", JOptionPane.INFORMATION_MESSAGE);
                }

                break;
        }
    }

}
