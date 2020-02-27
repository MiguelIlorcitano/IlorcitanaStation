/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tareasIlorcitana;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import net.sf.jasperreports.engine.JasperPrint;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import satation.Main;

/**
 *
 * @author Usuario
 */
public class Proyecto_Report1 {

    public Proyecto_Report1() {
        
    }

    /**
     * Función principaljj
     * 
     * @param i
     */
    public void creaReporte(int i) {
        Connection conn;
        InputStream inputStream = null;
        JasperPrint jasperPrint = null;
        
        try {
            if(i==2){
                inputStream = new FileInputStream("src\\tareasIlorcitana\\report2.jrxml");
            }else if(i==1){
                inputStream = new FileInputStream("src\\tareasIlorcitana\\report1.jrxml");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Proyecto_Report1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport reporteJasper = JasperCompileManager.compileReport(jasperDesign);
            JasperPrint mostrarReporte = JasperFillManager.fillReport(reporteJasper, null,conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave));
            JasperViewer.viewReport(mostrarReporte);
            
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null,"ffff"+ex);
        } catch (SQLException ex) {
            Logger.getLogger(Proyecto_Report1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
