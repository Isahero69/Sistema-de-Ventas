package Pagos;

import Controladora.Clientes;
import Controladora.Venta;
import Modelo.VentasDao;
import Vista.VentasController;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class PagosCreditosController implements Initializable {

    private static ListadoFacturasVencidasController pagarfacturas;
    NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance();
    private VentasController ventasController;
    private ModuloPagosCreditosController ModuloPagosCreditosController;
    VentasDao pagarFacturas = new VentasDao();
    LocalDate fechaActual = LocalDate.now();
    private int puntero, regresarVariante = 0;

    @FXML
    private TableColumn<Venta, Integer> id;
    @FXML
    private TableColumn<Venta, String> factura;
    @FXML
    private TableColumn<Venta, String> dias_vencidos;
    @FXML
    private TableColumn<Venta, String> valorfactura;
    @FXML
    private TextField totalpagar;
    @FXML
    private TextField valorpagado;
    @FXML
    private TextField diferencia;
    @FXML
    private TextField ajustepeso;
    @FXML
    private Button pagar;
    @FXML
    private Button ajustealpeso;
    @FXML
    private Button salirdeventa;
    @FXML
    private Button contado;
    @FXML
    private TextField nombrecliente;
    @FXML
    private TableView<Venta> tablafacturaspagar;
    @FXML
    private TextField nitcliente;
    @FXML
    private Label txtdiferencia;
    @FXML
    private Button regresar;
    @FXML
    private Label txtadvertencia;

    public void puntero(int puntero) {

        this.puntero = puntero;

        switch (puntero) {
            case 1:

                contado.setDisable(true);
                break;
            case 2:

                contado.setDisable(false);
                break;
            default:

                break;
        }

        if (puntero == 2) {

            regresarVariante = 2;
        } else {

            regresarVariante = 1;

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        id.setCellValueFactory(new PropertyValueFactory<>("id_venta"));
        factura.setCellValueFactory(new PropertyValueFactory<>("factura"));
        dias_vencidos.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();
            LocalDate fechaActual = LocalDate.now();
            java.sql.Date fechaDateVencimiento = (java.sql.Date) venta.getFechavencimiento();

            LocalDate fechaVencimiento = null;
            try {
                fechaVencimiento = fechaDateVencimiento.toLocalDate();
            } catch (Exception e) {
                System.err.println("Error al convertir la fecha: " + e.getMessage());
            }

            if (fechaVencimiento == null) {
                // Retornar un valor predeterminado en caso de error
                return new SimpleStringProperty("Error en fecha");
            }

            StringBuilder concatenar = new StringBuilder();
            int dias = (int) ChronoUnit.DAYS.between(fechaVencimiento, fechaActual);
            String dias_de_Vencimiento;

            if (dias == 1) {

                dias_de_Vencimiento = concatenar.append(dias).append(" Dia Vencida").toString();

            } else if (dias > 1) {

                dias_de_Vencimiento = concatenar.append(dias).append(" Dias Vencida").toString();
            } else if (dias == 0) {

                dias_de_Vencimiento = "Se vence Hoy";

            } else if (dias == -1) {

                dias_de_Vencimiento = "Vence Mañana";

            } else if (dias <= -2) {

                String recortar[] = String.valueOf(dias).split("-");
                dias_de_Vencimiento = concatenar.append(recortar[1]).append(" Dias x Vencerse").toString();

            } else {

                dias_de_Vencimiento = "";
            }

            return new SimpleStringProperty(dias_de_Vencimiento);

        });
        valorfactura.setCellValueFactory(recorrer -> {
            Venta venta = recorrer.getValue();

            int validar = venta.getTotalGeneral();

            if (validar == 0) {

                String valorfactura = formatoMoneda.format(venta.getAbonoscredito());
                return new SimpleStringProperty(valorfactura);

            } else {
                String valorfacturaTotal = formatoMoneda.format(venta.getTotalGeneral());
                return new SimpleStringProperty(valorfacturaTotal);
            }

        });//valor a pagar

        factura.setCellFactory(column -> new CenteredTableCell<>());
        dias_vencidos.setCellFactory(column -> new CenteredTableCell<>());
        valorfactura.setCellFactory(column -> new CenteredTableCell<>());

        pagar.setDisable(true);

    }

    @FXML
    private void regresar(ActionEvent event) {

        ((Node) (event.getSource())).getScene().getWindow().hide();

        Parent cargar = null;

        switch (regresarVariante) {
            case 1:
                try {

                cargar = FXMLLoader.load(getClass().getResource("/Pagos/ModuloPagosCreditos.fxml"));
                Scene escena = new Scene(cargar);
                Stage stage = new Stage();
                stage.setScene(escena);
                stage.show();

            } catch (IOException ex) {
                System.out.println(ex.toString());

            }
            break;
            case 2:
                Stage stage = (Stage) regresar.getScene().getWindow();
                stage.close();

                break;
            default:
                break;
        }

    }

    public void ventanaVentas(VentasController ventasController) {
        this.ventasController = ventasController;
    }

    public void ModuloPagosCreditosController(ModuloPagosCreditosController ModuloPagosCreditosController) {
        this.ModuloPagosCreditosController = ModuloPagosCreditosController;
    }

    public void facturapendiente(ListadoFacturasVencidasController pagarfacturas) {
        this.pagarfacturas = pagarfacturas;
    }

    public void recibirfacturas(ObservableList<Venta> recibirfacturas) {
        tablafacturaspagar.setItems(recibirfacturas);

    }

    public void recibirvalorapagar(int valor) {
        totalpagar.setText(formatoMoneda.format(valor));
        valorTotal_a_Pagar = valor;
    }

    public void clientes(Clientes cliente) {

        nombrecliente.setText(cliente.getNombreCliente());
        nitcliente.setText(String.valueOf(cliente.getIdentificacionCliente()));
    }

    public void clientesventas(String cliente, String nit) {

        nombrecliente.setText(cliente);
        nitcliente.setText(nit);
    }

    @FXML
    private void pagar(ActionEvent event) throws IOException {

        List<Venta> aplicar = new ArrayList();
        aplicar.addAll(tablafacturaspagar.getItems());
        String fechaPago = fechaActual.toString();

        pagarFacturas.cambiarEstadoFacturasCredito(aplicar, fechaPago);

        JOptionPane.showMessageDialog(null, "Pago Aplicado Correctamente", "Pago Aplicado", JOptionPane.INFORMATION_MESSAGE);

        ((Node) (event.getSource())).getScene().getWindow().hide();

        if (puntero == 2) {

            ventasController.invocarBuscarnit(event);
        } else {

            Parent cargar = FXMLLoader.load(getClass().getResource("/Pagos/ModuloPagosCreditos.fxml"));
            Scene escena = new Scene(cargar);
            Stage stage = new Stage();
            stage.setScene(escena);
            stage.show();
        }
    }

    public void borrar() {

        nombrecliente.setText("");
        nitcliente.setText("");
        totalpagar.setText("");
        ajustepeso.setText("");
        valorpagado.setText("");
        diferencia.setText("");
        tablafacturaspagar.getItems().clear();

    }

    private static int valorTotal_a_Pagar;
    private static int dif_formula;

    @FXML
    private void ingresovalorpagado(ActionEvent event) {

        int factura;
        int pago;
        int dif;

        String validarVacio = valorpagado.getText();

        if (validarVacio.isEmpty()) {

            JOptionPane.showMessageDialog(null, "No has Ingresado el Pago", "Valor no Permitido", JOptionPane.INFORMATION_MESSAGE);

        } else {

            pago = Integer.parseInt(valorpagado.getText());
            factura = valorTotal_a_Pagar;
            dif = factura - pago;

            if (dif > 1000 || dif < -1000) {

                int k = JOptionPane.showConfirmDialog(null, "Diferencia " + formatoMoneda.format(dif) + " demasiado grande, ¿Esta Seguro del Valor Ingresado?", "Valide Pago Ingresado", JOptionPane.ERROR_MESSAGE);

                if (k == 0) {

                    diferencia.setText(formatoMoneda.format(dif));

                    if (dif > 0) {

                        txtdiferencia.setText("Saldo por PAGAR");
                        ajustealpeso.setDisable(false);

                    } else if (dif < 0) {

                        txtdiferencia.setText("Saldo a FAVOR");
                        ajustealpeso.setDisable(false);

                    } else {

                        txtdiferencia.setText("Sin Diferencia");
                        ajustealpeso.setDisable(true);
                        pagar.setDisable(true);
                        //vender.setDisable(true);

                    }

                } else {

                    totalpagar.setStyle(
                            "-fx-border-color: red; "
                            + // Cambia el color del borde a rojo
                            "-fx-border-width: 2px; "
                            + // Define el grosor del borde
                            "-fx-border-radius: 5px; "
                            + // Define el radio de las esquinas del borde
                            "-fx-background-radius: 5px;"
                    );

                }

            } else {

                totalpagar.setStyle("");
                diferencia.setText(formatoMoneda.format(dif));

                if (dif > 0) {

                    txtdiferencia.setText("Saldo por PAGAR");
                    ajustealpeso.setDisable(false);

                } else if (dif < 0) {

                    txtdiferencia.setText("Saldo a FAVOR");
                    ajustealpeso.setDisable(false);

                } else {
                    txtdiferencia.setText("Sin Diferencia");
                    ajustealpeso.setDisable(true);
                    totalpagar.setDisable(true);
                    pagar.setDisable(false);
                }

            }

            dif_formula = dif;

        }

    }

    @FXML
    private void ajustealpeso(ActionEvent event) {

        int factura_a_pagar;
        int pago;
        int nuevovalor;
        int ajuste = 0;

        if (dif_formula > 1000 || dif_formula < -1000) {

            int i = JOptionPane.showConfirmDialog(null, "Esta seguro de ajustar " + formatoMoneda.format(dif_formula) + "?", "Considera diferencia", JOptionPane.INFORMATION_MESSAGE);

            if (i == 0) {
                pago = Integer.parseInt(valorpagado.getText());
                ajuste = pago + dif_formula;

                factura_a_pagar = valorTotal_a_Pagar;
                nuevovalor = factura_a_pagar - ajuste;
                diferencia.setText(String.valueOf(nuevovalor));
                ajustepeso.setText(String.valueOf(dif_formula));
                pagar.setDisable(false);
                valorpagado.setDisable(true);
                ajustealpeso.setDisable(true);

            }
        } else {
            pago = Integer.parseInt(valorpagado.getText());
            ajuste = pago + dif_formula;

            factura_a_pagar = valorTotal_a_Pagar;
            nuevovalor = factura_a_pagar - ajuste;
            diferencia.setText(String.valueOf(nuevovalor));
            ajustepeso.setText(String.valueOf(dif_formula));
            pagar.setDisable(false);
            valorpagado.setDisable(true);
            ajustealpeso.setDisable(true);
        }

    }

    @FXML
    private void contado(ActionEvent event) {

        if (puntero == 2) {

            ventasController.valorRecibido();

            Stage stage = (Stage) contado.getScene().getWindow();
            stage.close();
        }

    }

    @FXML
    private void salirdeventa(ActionEvent event) {

        Stage stage = (Stage) salirdeventa.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void validarvalores(KeyEvent event) {

        String valor = valorpagado.getText();

        if (!valor.matches("\\d*")) {

            valorpagado.setText(valor.replaceAll("[^\\d]", ""));
            txtadvertencia.setText("¡Solo valores numericos!");

        } else {

            txtadvertencia.setText("");

        }

        if (valor.length() > 10) {

            valorpagado.setText(valor.substring(0, 10));
            txtadvertencia.setText("¡Maximo 8 digitos!");
        }

        valorpagado.positionCaret(valor.length());
    }

    public class CenteredTableCell<S, T> extends TableCell<S, T> {

        private final CheckBox checkBox;

        public CenteredTableCell() {
            this.checkBox = new CheckBox();
            setAlignment(Pos.CENTER);
        }

        @Override
        protected void updateItem(T item, boolean vacio) {
            super.updateItem(item, vacio);
            if (vacio || item == null) {
                setText(null);
                setGraphic(null);
            } else if (item instanceof CheckBox checkPagar) {
                setGraphic(checkPagar);
            } else {
                setText(item.toString());
                setGraphic(null);
            }
        }
    }
}
