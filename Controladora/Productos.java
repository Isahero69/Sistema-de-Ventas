
package Controladora;


public class Productos {
    
    private int id_Producto;
    private int codigoProducto;
    private String nombreProducto;
    private String presentacionProducto;
    private String marcaProducto;
    private int  inventario;
    private String tipoProducto; //Gaseosa, agua, energizante, etc..
    private double precioProducto;
    private String impuestoProducto;//IVA 19%, IBUA, ICO, etc...

    public Productos() {
    }

    public Productos(int id_Producto, int codigoProducto, String nombreProducto, String presentacionProducto, String marcaProducto, int inventario, String tipoProducto, double precioProducto, String impuestoProducto) {
        this.id_Producto = id_Producto;
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.presentacionProducto = presentacionProducto;
        this.marcaProducto = marcaProducto;
        this.inventario = inventario;
        this.tipoProducto = tipoProducto;
        this.precioProducto = precioProducto;
        this.impuestoProducto = impuestoProducto;
    }

    public int getId_Producto() {
        return id_Producto;
    }

    public void setId_Producto(int id_Producto) {
        this.id_Producto = id_Producto;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getPresentacionProducto() {
        return presentacionProducto;
    }

    public void setPresentacionProducto(String presentacionProducto) {
        this.presentacionProducto = presentacionProducto;
    }

    public String getMarcaProducto() {
        return marcaProducto;
    }

    public void setMarcaProducto(String marcaProducto) {
        this.marcaProducto = marcaProducto;
    }

    public int getInventario() {
        return inventario;
    }

    public void setInventario(int inventario) {
        this.inventario = inventario;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto = precioProducto;
    }

    public String getImpuestoProducto() {
        return impuestoProducto;
    }

    public void setImpuestoProducto(String impuestoProducto) {
        this.impuestoProducto = impuestoProducto;
    }

   
    
    
}
