/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tareasIlorcitana;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diseño
 */
public class principalTareas {


    /**
     */
   public principalTareas() {
       
       hilosTareas mihilo = null;
       try {
           mihilo = new hilosTareas();
           mihilo.start();
       } catch (SQLException ex) {
           Logger.getLogger(principalTareas.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        hilosTareas mihilo = new hilosTareas();
         mihilo.start();
    }
}
