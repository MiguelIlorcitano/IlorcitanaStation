/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reporte;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import net.sf.jasperreports.engine.JasperPrint;
import java.io.InputStream;
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

/**
 *
 * @author Usuario
 */
public class Proyecto_Report {
    
    private ActaDataSource dataSource;

    public Proyecto_Report() {
        dataSource = new ActaDataSource();
    }
    
    public void addMantenimiento(String []g){
        ActaMantenimiento mantenimiento;
        mantenimiento = new ActaMantenimiento(g[0], g[1], g[2], g[8], g[3], g[4], g[5], g[6], g[7]);
        dataSource.AddActa(mantenimiento);
    }

    /**
     * Función principaljj
     * 
     */
    public void creaReporte() {
        // TODO code application logic here

        

        InputStream inputStream = null;
        JasperPrint jasperPrint = null;

        
        try {
            inputStream = new FileInputStream("C:\\IlorcitanaStation\\Tareas\\Reportes.jrxml");
            //inputStream = new FileInputStream("src\\reporte\\Reportes.jrxml");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Proyecto_Report.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            
            JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);
            
            JasperViewer.viewReport(jasperPrint,false);

            //JasperExportManager.exportReportToPdfFile(jasperPrint,ruta);
            //JasperExportManager.exportReportToPdfFile(jasperPrint,ruta.getPath());
            
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null,"ffff"+ex);
        }
    }
}
