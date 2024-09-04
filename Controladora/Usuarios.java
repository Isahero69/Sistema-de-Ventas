
package Controladora;


public class Usuarios {
    
    private int id_Usuario;
    private int identicacionUsuario;
    private String contrasena;

    public Usuarios() {
    }

    public Usuarios(int id_Usuario, int identicacionUsuario, String contrasena) {
        this.id_Usuario = id_Usuario;
        this.identicacionUsuario = identicacionUsuario;
        this.contrasena = contrasena;
    }

    public int getId_Usuario() {
        return id_Usuario;
    }

    public void setId_Usuario(int id_Usuario) {
        this.id_Usuario = id_Usuario;
    }

    public int getIdenticacionUsuario() {
        return identicacionUsuario;
    }

    public void setIdenticacionUsuario(int identicacionUsuario) {
        this.identicacionUsuario = identicacionUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    
    
    
}
