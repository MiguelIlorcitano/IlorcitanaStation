/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satation;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.ServerSocket;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Main {

    panel_loging panel = null;
    HiloStation mihilo = new HiloStation();
    private static ServerSocket SERVER_SOCKET;
    
    public static String driver = "jdbc:mysql://192.168.0.132:3307/ilorcitana";
    public static String usuario = "irobotica";
    public static String clave = "1233";
    
    public Main() {
        /*El objeto trayIcon representa el tray icon valga la redundancia
          a este objeto se le pueden asigna imágenes, popups, tooltips y
          una serie de listeners asociados a el*/
        final TrayIcon trayIcon;

        /*Se verifica si el sistema soporta los try icons*/
        if (SystemTray.isSupported()) {

            SystemTray tray = SystemTray.getSystemTray();

            //Se asigna la imagen que del tray icon
            ImageIcon im = new ImageIcon(Main.class.getResource("/satelite_p.png"));
            Image image = Toolkit.getDefaultToolkit().getImage("/satelite_p.png");

            //Este listener permite salir de la aplicacion
            ActionListener exitListener = (ActionEvent e) -> {
                mihilo.detenElHilo();
                System.exit(0);
            };

            //Aquí se crea un popup menu
            PopupMenu popup = new PopupMenu();

            //Se agrega la opción de salir
            MenuItem defaultItem = new MenuItem("Exit");

            //Se le asigna al item del popup el listener para salir de la app
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);

            /*Aqui creamos una instancia del tray icon y asignamos
            La imagen, el nombre del tray icon y el popup*/
            trayIcon = new TrayIcon(im.getImage(), "IlorcitanaStation", popup);

            /*Creamos un acction listener que se ejecuta cuando le damos
            doble click al try icon*/
            ActionListener actionListener = (ActionEvent e) -> {
                //trayIcon.displayMessage("IlorcitanaStation",
                        //"ESTE ORDENADOR HA SIDO INFECTADOO CON UN VIRUS MALICIOSO!!!!. EN BREVES MOMENTOS SE EXTENDERÁ A TODA LA RED!!!",
                        //TrayIcon.MessageType.ERROR);

                //Preparamos la apertura del panel
                if (panel == null || panel.isShowing() == false) {
                    panel = new panel_loging();
                    panel.setVisible(true);
                }

            };

            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(actionListener);

            try {

                tray.add(trayIcon);

            } catch (AWTException ex) {
                ex.printStackTrace();
            }

        } else {
            System.err.println("System tray is currently not supported.");
        }
        mihilo.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            SERVER_SOCKET = new ServerSocket(9876);
            Main main = new Main();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "La aplicación ya está abierta. "+ex, "Ilorcitana_Station", 1);
        }
    }
}
