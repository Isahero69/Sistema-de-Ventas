package Vista;

import Controladora.Venta;
import Modelo.VentasDao;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ReporteVentasController implements Initializable {

    VentasDao consultas = new VentasDao();
    NumberFormat formatonumero = NumberFormat.getCurrencyInstance();
    String currentYear = String.valueOf(LocalDate.now().getYear());

    @FXML
    private DatePicker fechaInicial;
    @FXML
    private Label totalventa;
    @FXML
    private Label totalcontado;
    @FXML
    private Label totalcredito;
    @FXML
    private Label facturascredito;
    @FXML
    private Label facturascontado;
    @FXML
    private Label totalfacturas;
    @FXML
    private ComboBox<String> mes;
    @FXML
    private ComboBox<String> year;
    @FXML
    private ComboBox<String> cliente;
    @FXML
    private DatePicker fechaFinal;
    @FXML
    private Button limpiar;
    @FXML
    private Button regresar;
    @FXML
    private LineChart<String, Number> graficoLinea;
    @FXML
    private BarChart<String, Number> graficoBarras;

    @FXML
    private BarChart<String, Number> barrasContado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mes.getItems().addAll("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        year.getItems().addAll(consultas.totalyears());
        cliente.getItems().addAll(consultas.clientesfacturados());

        totales();
        linea();
        barras();
        barrasContadografico();

    }

    public void linea() {

        String totales = null;
        String mes = null;
        XYChart.Series<String, Number> series = new XYChart.Series();
        series.setName("Tendencia Mensual");
        String mes_español = null;
        String[][] datosLinea = consultas.totalMensual();
        int menor = Integer.MAX_VALUE; // Inicializamos con el valor máximo posible
        int mayor = Integer.MIN_VALUE; // Inicializamos con el valor mínimo posible

        for (String[] reco2 : datosLinea) {
            int numeroMes = Integer.parseInt(reco2[0]);

            if (numeroMes < menor) {

                menor = numeroMes;

            }

            if (numeroMes > mayor) {
                mayor = numeroMes;

            }

        }
        String mes1 = diaMesLetra(menor);
        String mes2 = diaMesLetra(mayor);
        graficoLinea.setTitle("Totales desde " + mes1 + " hasta " + mes2);

        for (String[] reco : datosLinea) {
            //recorrer filas
            mes = reco[0];

            totales = reco[1];
            mes_español = switch (mes) {

                case "1" ->
                    "Enero";
                case "2" ->
                    "Febrero";
                case "3" ->
                    "Marzo";
                case "4" ->
                    "Abril";
                case "5" ->
                    "Mayo";
                case "6" ->
                    "Junio";
                case "7" ->
                    "Julio";
                case "8" ->
                    "Agosto";
                case "9" ->
                    "Septiembre";
                case "10" ->
                    "Octubre";
                case "11" ->
                    "Noviembre";
                case "12" ->
                    "Diciembre";
                default ->
                    throw new IllegalArgumentException("Mes no reconocido");
            };
            series.getData().addAll(new XYChart.Data(mes_español, Double.valueOf(totales)));

        }

        graficoLinea.layout();

        graficoLinea.getData().add(series);

        graficoLinea.getXAxis().setAnimated(false);
        graficoLinea.getYAxis().setAnimated(false);

    }

    public void barras() {

        String totales = null;
        String mes = null;
        XYChart.Series<String, Number> series = new XYChart.Series();
        series.setName("Tendencia Mensual Creditos");
        String mes_español = null;
        String[][] datosLinea = consultas.creditos();
        int menor = Integer.MAX_VALUE; // Inicializamos con el valor máximo posible
        int mayor = Integer.MIN_VALUE; // Inicializamos con el valor mínimo posible

        for (String[] reco2 : datosLinea) {
            int numeroMes = Integer.parseInt(reco2[0]);

            if (numeroMes < menor) {

                menor = numeroMes;

            }

            if (numeroMes > mayor) {
                mayor = numeroMes;

            }

        }
        String mes1 = diaMesLetra(menor);
        String mes2 = diaMesLetra(mayor);
        graficoBarras.setTitle("Totales desde " + mes1 + " hasta " + mes2);

        for (String[] reco : datosLinea) {
            //recorrer filas
            mes = reco[0];
            totales = reco[1];
            mes_español = switch (mes) {

                case "1" ->
                    "Enero";
                case "2" ->
                    "Febrero";
                case "3" ->
                    "Marzo";
                case "4" ->
                    "Abril";
                case "5" ->
                    "Mayo";
                case "6" ->
                    "Junio";
                case "7" ->
                    "Julio";
                case "8" ->
                    "Agosto";
                case "9" ->
                    "Septiembre";
                case "10" ->
                    "Octubre";
                case "11" ->
                    "Noviembre";
                case "12" ->
                    "Diciembre";
                default ->
                    throw new IllegalArgumentException("Mes no reconocido");
            };
            series.getData().addAll(new XYChart.Data(mes_español, Double.valueOf(totales)));
        }

        graficoBarras.layout();

        graficoBarras.getData().add(series);

        graficoBarras.getXAxis().setAnimated(false);
        graficoBarras.getYAxis().setAnimated(false);

    }

    public void barrasContadografico() {

        String totales = null;
        String mes = null;
        XYChart.Series<String, Number> series = new XYChart.Series();
        series.setName("Tendencia Mensual Contado");
        String mes_español = null;
        String[][] datosLinea = consultas.contados();

        int menor = Integer.MAX_VALUE; // Inicializamos con el valor máximo posible
        int mayor = Integer.MIN_VALUE; // Inicializamos con el valor mínimo posible

        for (String[] reco2 : datosLinea) {
            int numeroMes = Integer.parseInt(reco2[0]);

            if (numeroMes < menor) {

                menor = numeroMes;

            }

            if (numeroMes > mayor) {
                mayor = numeroMes;

            }

        }
        String mes1 = diaMesLetra(menor);
        String mes2 = diaMesLetra(mayor);
        barrasContado.setTitle("Totales desde " + mes1 + " hasta " + mes2);

        for (String[] reco : datosLinea) {
            //recorrer filas
            mes = reco[0];
            totales = reco[1];
            mes_español = switch (mes) {

                case "1" ->
                    "Enero";
                case "2" ->
                    "Febrero";
                case "3" ->
                    "Marzo";
                case "4" ->
                    "Abril";
                case "5" ->
                    "Mayo";
                case "6" ->
                    "Junio";
                case "7" ->
                    "Julio";
                case "8" ->
                    "Agosto";
                case "9" ->
                    "Septiembre";
                case "10" ->
                    "Octubre";
                case "11" ->
                    "Noviembre";
                case "12" ->
                    "Diciembre";
                default ->
                    throw new IllegalArgumentException("Mes no reconocido");
            };
            series.getData().addAll(new XYChart.Data(mes_español, Double.valueOf(totales)));
        }

        barrasContado.layout();

        barrasContado.getData().add(series);

        barrasContado.getXAxis().setAnimated(false);
        barrasContado.getYAxis().setAnimated(false);

    }

    public void totales() {

        double total = 0;
        double totalContado = 0;
        double totalCredito = 0;
        int totalfactura = 0;
        int totalfacturascontado = 0;
        int totalfacturascredito = 0;

        List<Venta> recibirVentas = new ArrayList();
        recibirVentas = consultas.consultarVentasActuales();

        for (Venta reco : recibirVentas) {

            total += reco.getTotalGeneral();
            totalfactura += 1;

            switch (reco.getTipoCliente()) {

                case "Contado":
                    totalContado += reco.getTotalGeneral();
                    totalfacturascontado += 1;
                    break;
                case "Credito":
                    totalCredito += reco.getTotalGeneral();
                    totalfacturascredito += 1;
                    break;
                default:
                    break;
            }

        }

        totalventa.setText(formatonumero.format(total));
        totalcontado.setText(formatonumero.format(totalContado));
        totalcredito.setText(formatonumero.format(totalCredito));
        totalfacturas.setText(String.valueOf(totalfactura));
        facturascontado.setText(String.valueOf(totalfacturascontado));
        facturascredito.setText(String.valueOf(totalfacturascredito));
    }

    public void graficosCreditos(String datos[]) {

        String totales = null;
        String mes = null;
        String mes_español = null;

        XYChart.Series<String, Number> series = new XYChart.Series();
        series.setName("Tendencia Mensual Creditos");

        String[][] datosCreditos = consultas.consultarVentasGraficosCredito(datos);

        int menor = Integer.MAX_VALUE; // Inicializamos con el valor máximo posible
        int mayor = Integer.MIN_VALUE; // Inicializamos con el valor mínimo posible
        boolean confirmar = false;

        for (String[] reco2 : datosCreditos) {

            confirmar = true;
            int numeroMes = Integer.parseInt(reco2[0]);

            if (numeroMes < menor) {

                menor = numeroMes;

            }

            if (numeroMes > mayor) {
                mayor = numeroMes;

            }

        }

        if (!confirmar) {
            graficoBarras.setTitle("Sin Datos de Credito");
        } else {
            String mes1 = diaMesLetra(menor);
            String mes2 = diaMesLetra(mayor);
            graficoBarras.setTitle("Totales desde " + mes1 + " hasta " + mes2);
        }

        // cargue de info de tabla de barras
        for (String[] reco : datosCreditos) {
            //recorrer filas
            mes = reco[0];
            totales = reco[1];
            mes_español = switch (mes) {

                case "1" ->
                    "Enero";
                case "2" ->
                    "Febrero";
                case "3" ->
                    "Marzo";
                case "4" ->
                    "Abril";
                case "5" ->
                    "Mayo";
                case "6" ->
                    "Junio";
                case "7" ->
                    "Julio";
                case "8" ->
                    "Agosto";
                case "9" ->
                    "Septiembre";
                case "10" ->
                    "Octubre";
                case "11" ->
                    "Noviembre";
                case "12" ->
                    "Diciembre";
                default ->
                    throw new IllegalArgumentException("Mes no reconocido");
            };
            series.getData().addAll(new XYChart.Data(mes_español, Double.valueOf(totales)));
        }

        graficoBarras.getData().clear();
        graficoBarras.layout();

        graficoBarras.getData().add(series);

        graficoBarras.getXAxis().setAnimated(false);
        graficoBarras.getYAxis().setAnimated(false);

    }

    public void graficosContado(String datos[]) {

        String totales = null;
        String mes = null;
        String mes_español = null;

        XYChart.Series<String, Number> series = new XYChart.Series();
        series.setName("Tendencia Mensual Contado");

        String[][] datosCreditos = consultas.consultarVentasGraficosContado(datos);

        int menor = Integer.MAX_VALUE; // Inicializamos con el valor máximo posible
        int mayor = Integer.MIN_VALUE; // Inicializamos con el valor mínimo posible
        boolean confirmar = false;

        for (String[] reco2 : datosCreditos) {

            confirmar = true;
            int numeroMes = Integer.parseInt(reco2[0]);

            if (numeroMes < menor) {

                menor = numeroMes;

            }

            if (numeroMes > mayor) {
                mayor = numeroMes;

            }

        }

        if (!confirmar) {
            barrasContado.setTitle("Sin Datos de Contado");
        } else {
            String mes1 = diaMesLetra(menor);
            String mes2 = diaMesLetra(mayor);
            barrasContado.setTitle("Totales desde " + mes1 + " hasta " + mes2);
        }

        // cargue de info de tabla de barras
        for (String[] reco : datosCreditos) {
            //recorrer filas
            mes = reco[0];
            totales = reco[1];
            mes_español = switch (mes) {

                case "1" ->
                    "Enero";
                case "2" ->
                    "Febrero";
                case "3" ->
                    "Marzo";
                case "4" ->
                    "Abril";
                case "5" ->
                    "Mayo";
                case "6" ->
                    "Junio";
                case "7" ->
                    "Julio";
                case "8" ->
                    "Agosto";
                case "9" ->
                    "Septiembre";
                case "10" ->
                    "Octubre";
                case "11" ->
                    "Noviembre";
                case "12" ->
                    "Diciembre";
                default ->
                    throw new IllegalArgumentException("Mes no reconocido");
            };
            series.getData().addAll(new XYChart.Data(mes_español, Double.valueOf(totales)));
        }

        barrasContado.getData().clear();
        barrasContado.layout();

        barrasContado.getData().add(series);

        barrasContado.getXAxis().setAnimated(false);
        barrasContado.getYAxis().setAnimated(false);

    }

    public void graficosTotales(String datos[]) {

        String totales = null;
        String mes = null;
        String mes_español = null;
        String[][] datosTotales = consultas.consultarVentasGraficosTotales(datos);

        XYChart.Series<String, Number> series = new XYChart.Series();
        series.setName("Tendencia Mensual Consolidado");
        int menor = Integer.MAX_VALUE; // Inicializamos con el valor máximo posible
        int mayor = Integer.MIN_VALUE; // Inicializamos con el valor mínimo posible
        boolean confirmar = false;

        for (String[] reco2 : datosTotales) {

            confirmar = true;
            int numeroMes = Integer.parseInt(reco2[0]);

            if (numeroMes < menor) {

                menor = numeroMes;

            }

            if (numeroMes > mayor) {
                mayor = numeroMes;

            }

        }

        if (!confirmar) {
            graficoLinea.setTitle("Sin Datos de Credito");
        } else {
            String mes1 = diaMesLetra(menor);
            String mes2 = diaMesLetra(mayor);
            graficoLinea.setTitle("Totales desde " + mes1 + " hasta " + mes2);
        }

        // cargue de info de tabla de barras
        for (String[] reco : datosTotales) {
            //recorrer filas
            mes = reco[0];
            totales = reco[1];
            mes_español = switch (mes) {

                case "1" ->
                    "Enero";
                case "2" ->
                    "Febrero";
                case "3" ->
                    "Marzo";
                case "4" ->
                    "Abril";
                case "5" ->
                    "Mayo";
                case "6" ->
                    "Junio";
                case "7" ->
                    "Julio";
                case "8" ->
                    "Agosto";
                case "9" ->
                    "Septiembre";
                case "10" ->
                    "Octubre";
                case "11" ->
                    "Noviembre";
                case "12" ->
                    "Diciembre";
                default ->
                    throw new IllegalArgumentException("Mes no reconocido");
            };
            series.getData().addAll(new XYChart.Data(mes_español, Double.valueOf(totales)));
        }

        graficoLinea.getData().clear();
        graficoLinea.layout();

        graficoLinea.getData().add(series);

        graficoLinea.getXAxis().setAnimated(false);
        graficoLinea.getYAxis().setAnimated(false);
    }

    @FXML
    private void fechaInicial(ActionEvent event) {

        mes.setDisable(true);

        String recibirDatos[] = new String[5];

        if (fechaInicial.getValue() != null) {

            recibirDatos[0] = String.valueOf(fechaInicial.getValue());

        }

        if (fechaFinal.getValue() != null) {

            recibirDatos[1] = String.valueOf(fechaFinal.getValue());

        }

        if (mes.getValue() != null && !mes.getValue().isEmpty()) {
            // Convertir el nombre del mes en mayúsculas a un valor de Month
            int numeroMes = Month.valueOf(diaMes().toUpperCase(Locale.ROOT)).getValue();

            recibirDatos[2] = String.valueOf(numeroMes);

        }

        if (year.getValue() != null && !year.getValue().isEmpty()) {

            recibirDatos[3] = year.getValue();
        }

        if (cliente.getValue() != null && !cliente.getValue().isEmpty()) {

            recibirDatos[4] = cliente.getValue();
        }

        if (null != recibirDatos[1]) {

            double total = 0;
            double totalContado = 0;
            double totalCredito = 0;
            int totalfactura = 0;
            int totalfacturascontado = 0;
            int totalfacturascredito = 0;

            List<Venta> recibirVentas = consultas.consultarVentasFechas(recibirDatos);
            graficosCreditos(recibirDatos);
            graficosTotales(recibirDatos);
            graficosContado(recibirDatos);

            for (Venta reco : recibirVentas) {

                total += reco.getTotalGeneral();
                totalfactura += 1;

                switch (reco.getTipoCliente()) {

                    case "Contado":
                        totalContado += reco.getTotalGeneral();
                        totalfacturascontado += 1;
                        break;
                    case "Credito":
                        totalCredito += reco.getTotalGeneral();
                        totalfacturascredito += 1;
                        break;
                    default:
                        break;
                }

            }

            totalventa.setText(formatonumero.format(total));
            totalcontado.setText(formatonumero.format(totalContado));
            totalcredito.setText(formatonumero.format(totalCredito));
            totalfacturas.setText(String.valueOf(totalfactura));
            facturascontado.setText(String.valueOf(totalfacturascontado));
            facturascredito.setText(String.valueOf(totalfacturascredito));

        }

    }

    @FXML
    private void fechafinal(ActionEvent event) {

        mes.setDisable(true);
        String recibirDatos[] = new String[5];

        if (fechaInicial.getValue() != null) {

            recibirDatos[0] = String.valueOf(fechaInicial.getValue());

        }

        if (fechaFinal.getValue() != null) {

            recibirDatos[1] = String.valueOf(fechaFinal.getValue());

        }

        if (mes.getValue() != null && !mes.getValue().isEmpty()) {

            int numeroMes = Month.valueOf(diaMes().toUpperCase(Locale.ROOT)).getValue();
            recibirDatos[2] = String.valueOf(numeroMes);

        }

        if (year.getValue() != null && !year.getValue().isEmpty()) {

            recibirDatos[3] = year.getValue();
        }

        if (cliente.getValue() != null && !cliente.getValue().isEmpty()) {

            recibirDatos[4] = cliente.getValue();
        }

        if (null != recibirDatos[0]) {

            double total = 0;
            double totalContado = 0;
            double totalCredito = 0;
            int totalfactura = 0;
            int totalfacturascontado = 0;
            int totalfacturascredito = 0;

            List<Venta> recibirVentas = consultas.consultarVentasFechas(recibirDatos);
            graficosCreditos(recibirDatos);
            graficosTotales(recibirDatos);
            graficosContado(recibirDatos);

            for (Venta reco : recibirVentas) {

                total += reco.getTotalGeneral();
                totalfactura += 1;

                switch (reco.getTipoCliente()) {

                    case "Contado":
                        totalContado += reco.getTotalGeneral();
                        totalfacturascontado += 1;
                        break;
                    case "Credito":
                        totalCredito += reco.getTotalGeneral();
                        totalfacturascredito += 1;
                        break;
                    default:
                        break;
                }

            }

            totalventa.setText(formatonumero.format(total));
            totalcontado.setText(formatonumero.format(totalContado));
            totalcredito.setText(formatonumero.format(totalCredito));
            totalfacturas.setText(String.valueOf(totalfactura));
            facturascontado.setText(String.valueOf(totalfacturascontado));
            facturascredito.setText(String.valueOf(totalfacturascredito));

        }
    }

    @FXML
    private void mes(ActionEvent event) {

        fechaInicial.setDisable(true);
        fechaFinal.setDisable(true);

        String recibirDatos[] = new String[5];

        if (fechaInicial.getValue() != null) {

            recibirDatos[0] = String.valueOf(fechaInicial.getValue());

        }

        if (fechaFinal.getValue() != null) {

            recibirDatos[1] = String.valueOf(fechaFinal.getValue());

        }

        if (mes.getValue() != null && !mes.getValue().isEmpty()) {
            // Convertir el nombre del mes en mayúsculas a un valor de Month
            int numeroMes = Month.valueOf(diaMes().toUpperCase(Locale.ROOT)).getValue();

            recibirDatos[2] = String.valueOf(numeroMes);

        }

        if (year.getValue() != null && !year.getValue().isEmpty()) {

            recibirDatos[3] = year.getValue();
        }

        if (cliente.getValue() != null && !cliente.getValue().isEmpty()) {

            recibirDatos[4] = cliente.getValue();
        }

        double total = 0;
        double totalContado = 0;
        double totalCredito = 0;
        int totalfactura = 0;
        int totalfacturascontado = 0;
        int totalfacturascredito = 0;

        List<Venta> recibirVentas = consultas.consultarVentasFechas(recibirDatos);
        graficosCreditos(recibirDatos);
        graficosTotales(recibirDatos);
        graficosContado(recibirDatos);

        for (Venta reco : recibirVentas) {

            total += reco.getTotalGeneral();
            totalfactura += 1;

            switch (reco.getTipoCliente()) {

                case "Contado":
                    totalContado += reco.getTotalGeneral();
                    totalfacturascontado += 1;
                    break;
                case "Credito":
                    totalCredito += reco.getTotalGeneral();
                    totalfacturascredito += 1;
                    break;
                default:
                    break;
            }

        }

        totalventa.setText(formatonumero.format(total));
        totalcontado.setText(formatonumero.format(totalContado));
        totalcredito.setText(formatonumero.format(totalCredito));
        totalfacturas.setText(String.valueOf(totalfactura));
        facturascontado.setText(String.valueOf(totalfacturascontado));
        facturascredito.setText(String.valueOf(totalfacturascredito));

    }

    @FXML
    private void year(ActionEvent event) {

        String recibirDatos[] = new String[5];

        if (fechaInicial.getValue() != null) {

            recibirDatos[0] = String.valueOf(fechaInicial.getValue());

        }

        if (fechaFinal.getValue() != null) {

            recibirDatos[1] = String.valueOf(fechaFinal.getValue());

        }

        if (mes.getValue() != null && !mes.getValue().isEmpty()) {

            // Convertir el nombre del mes en mayúsculas a un valor de Month
            int numeroMes = Month.valueOf(diaMes().toUpperCase(Locale.ROOT)).getValue();

            recibirDatos[2] = String.valueOf(numeroMes);

        }

        if (year.getValue() != null && !year.getValue().isEmpty()) {

            recibirDatos[3] = year.getValue();
        }

        if (cliente.getValue() != null && !cliente.getValue().isEmpty()) {

            recibirDatos[4] = cliente.getValue();
        }

        double total = 0;
        double totalContado = 0;
        double totalCredito = 0;
        int totalfactura = 0;
        int totalfacturascontado = 0;
        int totalfacturascredito = 0;

        List<Venta> recibirVentas = consultas.consultarVentasFechas(recibirDatos);
        graficosCreditos(recibirDatos);
        graficosTotales(recibirDatos);
        graficosContado(recibirDatos);

        for (Venta reco : recibirVentas) {

            total += reco.getTotalGeneral();
            totalfactura += 1;

            switch (reco.getTipoCliente()) {

                case "Contado":
                    totalContado += reco.getTotalGeneral();
                    totalfacturascontado += 1;
                    break;
                case "Credito":
                    totalCredito += reco.getTotalGeneral();
                    totalfacturascredito += 1;
                    break;
                default:
                    break;
            }

        }

        totalventa.setText(formatonumero.format(total));
        totalcontado.setText(formatonumero.format(totalContado));
        totalcredito.setText(formatonumero.format(totalCredito));
        totalfacturas.setText(String.valueOf(totalfactura));
        facturascontado.setText(String.valueOf(totalfacturascontado));
        facturascredito.setText(String.valueOf(totalfacturascredito));

    }

    @FXML
    private void cliente(ActionEvent event) {

        String recibirDatos[] = new String[5];

        if (fechaInicial.getValue() != null) {

            recibirDatos[0] = String.valueOf(fechaInicial.getValue());

        }

        if (fechaFinal.getValue() != null) {

            recibirDatos[1] = String.valueOf(fechaFinal.getValue());

        }

        if (mes.getValue() != null && !mes.getValue().isEmpty()) {

            // Convertir el nombre del mes en mayúsculas a un valor de Month
            int numeroMes = Month.valueOf(diaMes().toUpperCase(Locale.ROOT)).getValue();

            recibirDatos[2] = String.valueOf(numeroMes);

        }

        if (year.getValue() != null && !year.getValue().isEmpty()) {

            recibirDatos[3] = year.getValue();
        }

        if (cliente.getValue() != null && !cliente.getValue().isEmpty()) {

            recibirDatos[4] = cliente.getValue();
        }

        double total = 0;
        double totalContado = 0;
        double totalCredito = 0;
        int totalfactura = 0;
        int totalfacturascontado = 0;
        int totalfacturascredito = 0;

        List<Venta> recibirVentas = consultas.consultarVentasFechas(recibirDatos);
        graficosCreditos(recibirDatos);
        graficosTotales(recibirDatos);
        graficosContado(recibirDatos);

        for (Venta reco : recibirVentas) {

            total += reco.getTotalGeneral();
            totalfactura += 1;

            switch (reco.getTipoCliente()) {

                case "Contado":
                    totalContado += reco.getTotalGeneral();
                    totalfacturascontado += 1;
                    break;
                case "Credito":
                    totalCredito += reco.getTotalGeneral();
                    totalfacturascredito += 1;
                    break;
                default:
                    break;
            }

        }

        totalventa.setText(formatonumero.format(total));
        totalcontado.setText(formatonumero.format(totalContado));
        totalcredito.setText(formatonumero.format(totalCredito));
        totalfacturas.setText(String.valueOf(totalfactura));
        facturascontado.setText(String.valueOf(totalfacturascontado));
        facturascredito.setText(String.valueOf(totalfacturascredito));

    }

    @FXML
    private void limpiar(ActionEvent event) {

        mes.setValue(null);
        mes.setPromptText("Seleccione Mes");
        year.setValue(null);
        year.setPromptText("Seleccione Año");
        cliente.setValue(null);
        cliente.setPromptText("Seleccione Cliente");

        fechaInicial.setValue(null);
        fechaFinal.setValue(null);

        fechaInicial.setDisable(false);
        fechaFinal.setDisable(false);
        mes.setDisable(false);

    }

    public String diaMes() {

        String mes_letra = mes.getValue();//tomo el mes en letras
        String mes_en_ingles = switch (mes_letra) {
            case "Enero" ->
                "JANUARY";
            case "Febrero" ->
                "FEBRUARY";
            case "Marzo" ->
                "MARCH";
            case "Abril" ->
                "APRIL";
            case "Mayo" ->
                "MAY";
            case "Junio" ->
                "JUNE";
            case "Julio" ->
                "JULY";
            case "Agosto" ->
                "AUGUST";
            case "Septiembre" ->
                "SEPTEMBER";
            case "Octubre" ->
                "OCTOBER";
            case "Noviembre" ->
                "NOVEMBER";
            case "Diciembre" ->
                "DECEMBER";
            default ->
                throw new IllegalArgumentException("Mes no reconocido");
        };

        return mes_en_ingles;
    }

    public String diaMesLetra(int numero) {

        String mes_letra = switch (numero) {
            case 1 ->
                "Enero";
            case 2 ->
                "Febrero";
            case 3 ->
                "Marzo";
            case 4 ->
                "Abril";
            case 5 ->
                "Mayo";
            case 6 ->
                "Junio";
            case 7 ->
                "Julio";
            case 8 ->
                "Agosto";
            case 9 ->
                "Septiembre";
            case 10 ->
                "Octubre";
            case 11 ->
                "Noviembre";
            case 12 ->
                "Diciembre";
            default ->
                throw new IllegalArgumentException("Mes en numero no reconocido");
        };

        return mes_letra;
    }

    @FXML
    private void regresar(ActionEvent event) {

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

    }

}
