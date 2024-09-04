
package Modelo;

import Controladora.Usuarios;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


public class UsuariosDao {
    
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    ConexionMySql conexion = new ConexionMySql();
    
    
    public Usuarios log(int iden, String contras){
        
        Usuarios usuario = new Usuarios();
        String sql = "SELECT * FROM usuarios WHERE identificacion = ? OR contraseña = ?";
        
          try{
            con = conexion.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, iden);
            ps.setString(2, contras);
            rs = ps.executeQuery();
            if(rs.next()){
                usuario.setIdenticacionUsuario(rs.getInt("identificacion"));
                usuario.setContrasena(rs.getString("contraseña"));
                
            }
            
        }catch(SQLException e){
            System.out.println(e.toString());
        }
        
        return usuario;
    }
    
    
    
}
