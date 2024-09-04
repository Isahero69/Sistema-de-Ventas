package Controladora;

import java.util.Date;

public class Venta {
    
    private int id_venta;
    private int item;
    private int identificacionCliente;
    private String nombreCliente;
    private long contactoCliente;
    private String correoCliente;
    private String direccionCliente;
    private String naturalezaCliente;
    private String tipoCliente; //Credito o contado
    private String diasDePlazo;
    private int codigo;
    private String producto;
    private int precio;
    private int cantidad;
    private int subtotal;
    private int valorIva;
    private int total;
    private int totalCantidades;
    private int totalSubtotal;
    private int totalIVA;
    private int totalGeneral;
    private int abonoscredito;
    private int numeroFactura = 0;
    private String factura;
    private Date fechaActual;
    private Date fechavencimiento;
    private String fechdepago;
    private String status;
    private String saldofactura;
    private int diasVencido;

    public Venta() {
    }

    public Venta(int item, int codigo, String producto, int precio, int cantidad, int subtotal, int valorIva, int total) {
        this.item = item;
        this.codigo = codigo;
        this.producto = producto;
        this.precio = precio;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.valorIva = valorIva;
        this.total = total;

    }

    public int getDiasVencido() {
        return diasVencido;
    }

    public void setDiasVencido(int diasVencido) {
        this.diasVencido = diasVencido;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSaldofactura() {
        return saldofactura;
    }

    public void setSaldofactura(String saldofactura) {
        this.saldofactura = saldofactura;
    }

    
    
    public String getFechdepago() {
        return fechdepago;
    }

    public void setFechdepago(String fechdepago) {
        this.fechdepago = fechdepago;
    }
 
    public int getIdentificacionCliente() {
        return identificacionCliente;
    }

    public void setIdentificacionCliente(int identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public long getContactoCliente() {
        return contactoCliente;
    }

    public void setContactoCliente(long contactoCliente) {
        this.contactoCliente = contactoCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getNaturalezaCliente() {
        return naturalezaCliente;
    }

    public void setNaturalezaCliente(String naturalezaCliente) {
        this.naturalezaCliente = naturalezaCliente;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getDiasDePlazo() {
        return diasDePlazo;
    }

    public void setDiasDePlazo(String diasDePlazo) {
        this.diasDePlazo = diasDePlazo;
    }

    
    
    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public int getId_venta() {
        return id_venta;
    }

    public void setId_venta(int id_venta) {
        this.id_venta = id_venta;
    }
    
    
    
    public Date getFechavencimiento() {
        return fechavencimiento;
    }

    public void setFechavencimiento(Date fechavencimiento) {
        this.fechavencimiento = fechavencimiento;
    }

   
    
    public int getAbonoscredito() {
        return abonoscredito;
    }

    public void setAbonoscredito(int abonoscredito) {
        this.abonoscredito = abonoscredito;
    }

    
    
    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public int getTotalCantidades() {
        return totalCantidades;
    }

    public void setTotalCantidades(int totalCantidades) {
        this.totalCantidades = totalCantidades;
    }

    public int getTotalSubtotal() {
        return totalSubtotal;
    }

    public void setTotalSubtotal(int totalSubtotal) {
        this.totalSubtotal = totalSubtotal;
    }

    public int getTotalIVA() {
        return totalIVA;
    }

    public void setTotalIVA(int totalIVA) {
        this.totalIVA = totalIVA;
    }

    public int getTotalGeneral() {
        return totalGeneral;
    }

    public void setTotalGeneral(int totalGeneral) {
        this.totalGeneral = totalGeneral;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getValorIva() {
        return valorIva;
    }

    public void setValorIva(int valorIva) {
        this.valorIva = valorIva;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
