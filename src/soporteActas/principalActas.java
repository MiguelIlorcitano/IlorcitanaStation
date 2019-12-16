/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soporteActas;

import tareasIlorcitana.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diseño
 */
public class principalActas {


    /**
     */
   public principalActas() {
       
       hilosActas mihilo = null;
       try {
           mihilo = new hilosActas();
           mihilo.start();
       } catch (SQLException ex) {
           Logger.getLogger(principalActas.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
    public static void main(String[] args) throws SQLException {
        // TODO code application logic here
        hilosActas mihilo = new hilosActas();
         mihilo.start();
    }
}
