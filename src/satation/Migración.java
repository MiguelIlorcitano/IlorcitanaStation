/*
 * Clase encargarda de copiar los usuarios de la tabla de bases de datos de empresa a tabla de base de datos de aplicaión
 */
package satation;

import java.awt.Font;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Miguel Ángel Carrillo García
 */
public class Migración {
    
    ArrayList <String> id_usuario = new ArrayList();
    ArrayList <String> usuario = new ArrayList();
    ArrayList <String> nombre = new ArrayList();
    ArrayList <String> clave = new ArrayList();
    //Variables de conexión para bases de datos
    private Connection con;
    private Statement st;
    
    public Migración(){
    
    }
    
    /**
     * Metodo para conectar con base de datos.
     *
     * @return
     */
    private void conectar() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection("jdbc:ucanaccess://I:\\ClasGes6\\Modulo\\DB.mdb");
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al realizar la conexion " + ex);
        }
    }
    
    private void recogeDatos(){
        conectar();
        try {
            st = con.createStatement();
            ResultSet r = st.executeQuery("SELECT * FROM UsuariosP");
            while (r.next()) {
                id_usuario.add(r.getString("IdUsuario"));
                usuario.add(r.getString("Usuario"));
                nombre.add(r.getString("Nombre"));
                clave.add(r.getString("Clave"));
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Migración.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
        
    private void pasaDatos(){
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            PreparedStatement pst = conn.prepareStatement("INSERT INTO UsuariosP(IdUsuario, Usuario, Nombre, Clave)VALUES(?,?,?,?)")) {
            for (int i = 0; i < id_usuario.size(); i++) {
                    pst.setString(1, id_usuario.get(i));
                    pst.setString(2, usuario.get(i));
                    pst.setString(3, nombre.get(i));
                    pst.setString(4, clave.get(i));
                    pst.executeUpdate();
                }
            
            } catch (SQLException | HeadlessException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
    }
    
    private void borraDatos(){
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            PreparedStatement pst = conn.prepareStatement("TRUNCATE UsuariosP")) {
            pst.executeUpdate();
        } catch (SQLException | HeadlessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    void main() {
        recogeDatos();
        borraDatos();
        pasaDatos();
        int i = 0;
    }
}
