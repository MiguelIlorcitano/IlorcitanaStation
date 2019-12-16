/*
 * To change this license header, choose License Headers in Project Propiedades.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satation;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UltimateProcessPro
 */
public class Propiedades {
    static java.util.Properties config = new java.util.Properties();
    static InputStream configInput = null;
    
    static{
        try {
            configInput = new FileInputStream("C:\\IlorcitanaStation\\Configuracion\\ilorcitana_config.properties");
            config.load(configInput);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Propiedades.class.getName()).log(Level.SEVERE, null, ex);
        }   catch (IOException ex) {
                Logger.getLogger(Propiedades.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public static String getPropiedad(String propiedad){
        return config.getProperty(propiedad);
    }
    
    public static void setPropiedad(String propiedad, String valor){
        config.setProperty(propiedad, valor);
        try {
            config.store(new FileWriter("C:\\IlorcitanaStation\\Configuracion\\ilorcitana_config.properties"),"modificado");
        } catch (IOException ex) {
            Logger.getLogger(Propiedades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
