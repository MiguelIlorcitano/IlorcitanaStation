/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soporteActas;


/**
 *
 * @author Diseño
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import tareasIlorcitana.Tareas_Principal;

public class paraeliminar {
    
    private static Connection conMy;
    private static ResultSet rsMy;
    private static Statement stMy;
    
    private static Connection con;
    private static ResultSet rs;
    private static Statement st;

    public paraeliminar() {
        
    }
    
    
    
    /**
     * Conectar con la base de datos.
     */
    public static void conectarMy(){
        if (conMy == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                conMy = DriverManager.getConnection("jdbc:mysql://192.168.0.132:3307/ilorcitana", "irobotica", "1233");
            } catch (ClassNotFoundException | SQLException ex) {
                JOptionPane.showMessageDialog(null,"Error al realizar la conexion "+ex);
                Logger.getLogger(Tareas_Principal.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
    /**
    * Metodo para conectar con base de datos.
    */
    private static void conectar(){
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection("jdbc:ucanaccess://Z:\\Mantenimiento\\Mantenimiento.accdb");
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error al realizar la conexion "+ex);
        }   
    } 
    
    /**
     * Extrae el contenido de la base de datos y lo inserta en una jtable.
     */
    private static void copiarBD(){
        //Extrae el contenido de la tabla y lo almacena en el resultset.
        try{ 
            st= con.createStatement();
            ResultSet r = st.executeQuery("SELECT * FROM Mantenimiento");
            stMy= conMy.createStatement();
            String fila [] = new String[9];
            while (r.next()){
                fila[0]=r.getString("M");                                               
                fila[1]=cambiarFecha(r.getString("F"));
                fila[2]=r.getString("O");
                fila[3]=r.getString("TM");
                if(r.getString("TM").equals("")){
                    fila[4]="Mantenimiento.";
                }else{
                    fila[4]=r.getString("TP");
                }
                fila[5]=r.getString("Op");
                if(r.getString("Ps").equals("")){
                     fila[6]="Sin Operación";
                }else{
                    fila[6]=r.getString("Ps");
                }
                fila[7]=r.getString("Ob");
                fila[8]=r.getString("H");  
                stMy.executeUpdate("INSERT INTO mantenimiento(Id_tarea,Maquina,Fecha,Operario,TipoMantenimiento,TipoProblema,Operaciones,PiezasSustituidas,CodigoRepuesto,Observaciones,Horas)VALUES(NULL,'"+fila[0]+"','"+fila[1]+"','"+fila[2]+"','"+fila[3]+"','"+fila[4]+"','"+fila[5]+"','"+fila[6]+"',NULL,'"+fila[7]+"','"+fila[8]+"')");
            }           
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error al cargar los datos de la tabla Mantenimiento ");
        }
        JOptionPane.showMessageDialog(null, "Tarea creada correctamente.");
    }
    
    private static String cambiarFecha(String f){
        String[] corte=f.split("/");
        String fin = corte[2]+"-"+corte[1]+"-"+corte[0];
        return fin;
    }
    
    public static void main (String [ ] args) {
        conectarMy();
        conectar();
        copiarBD();
        
    }
    
}
