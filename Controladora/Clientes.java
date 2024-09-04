
package Controladora;


public class Clientes {
    
    private int id_Cliente;
    private int identificacionCliente;
    private String nombreCliente;
    private long contactoCliente;
    private String correoCliente;
    private String direccionCliente;
    private String naturalezaCliente;
    private String tipoCliente; //Credito o contado
    private String diasDePlazo; //30,60 o 90 dias

    public Clientes() {
    }

    public Clientes(int id_Cliente, int identificacionCliente, String nombreCliente, long contactoCliente, String correoCliente, String direccionCliente, String naturalezaCliente, String tipoCliente, String diasDePlazo) {
        this.id_Cliente = id_Cliente;
        this.identificacionCliente = identificacionCliente;
        this.nombreCliente = nombreCliente;
        this.contactoCliente = contactoCliente;
        this.correoCliente = correoCliente;
        this.direccionCliente = direccionCliente;
        this.naturalezaCliente = naturalezaCliente;
        this.tipoCliente = tipoCliente;
        this.diasDePlazo = diasDePlazo;
    }

    public int getId_Cliente() {
        return id_Cliente;
    }

    public void setId_Cliente(int id) {
        this.id_Cliente = id;
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
    
    
    
    
    
    
}
