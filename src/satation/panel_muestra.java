/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import tareasIlorcitana.Tareas_Principal;

/**
 *
 * @author Diseño
 */
public class panel_muestra extends javax.swing.JFrame {
    
    private final String driver = "jdbc:mysql://192.168.0.132:3307/ilorcitana";
    private final String usuario = "irobotica";
    private final String clave = "1233";
    
    private int id_maquina;
    private String periodo;
    private String descripcion;
    
    public panel_muestra() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * Creates new form panel_muestra
     * @param id_maquina
     * @param periodo
     * @param descripcion
     */
    public panel_muestra(String id_maquina, String periodo, String descripcion) {
        this.descripcion=descripcion;
        this.id_maquina=Integer.parseInt(id_maquina);
        this.periodo=periodo;
        initComponents();
        this.setLocationRelativeTo(null);
        extraeOrdenes();
        jLabel3.setText("¡¡¡  MANTENIMIENTO "+periodo.toUpperCase()+" _ "+descripcion.toUpperCase()+"  !!!");
    }
    
    private void extraeOrdenes(){
        DefaultListModel modelo = new DefaultListModel();
        switch (periodo) {
            case "anual":
                try (Connection conn = DriverManager.getConnection(driver, usuario, clave);
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT Codigo,Descripcion,Especificacion FROM Operaciones WHERE Periodicidad=\"" + periodo + "\" AND id_maquina=" + id_maquina)) {
                    while (rs.next()) {
                        modelo.addElement(rs.getString("Codigo") + "__" + rs.getString("Descripcion") + "__" + rs.getString("Especificacion"));
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla Operaciones en panelMestra. " + e);
                }
                break;
            case "trimestral":
                try (Connection conn = DriverManager.getConnection(driver, usuario, clave);
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT Codigo,Descripcion,Especificacion FROM Operaciones WHERE Periodicidad=\"" + periodo + "\" AND id_maquina=" + id_maquina)) {
                    while (rs.next()) {
                        modelo.addElement(rs.getString("Codigo") + "__" + rs.getString("Descripcion") + "__" + rs.getString("Especificacion"));
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla Operaciones en panelMestra. " + e);
                }
                break;
            case "mensual":
                try (Connection conn = DriverManager.getConnection(driver, usuario, clave);
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT Codigo,Descripcion,Especificacion FROM Operaciones WHERE Periodicidad=\"" + periodo + "\" AND id_maquina=" + id_maquina)) {
                    while (rs.next()) {
                        modelo.addElement(rs.getString("Codigo") + "__" + rs.getString("Descripcion") + "__" + rs.getString("Especificacion"));
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla Operaciones en panelMestra. " + e);
                }
                break;
            case "semanal":
                try (Connection conn = DriverManager.getConnection(driver, usuario, clave);
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT Codigo,Descripcion,Especificacion FROM Operaciones WHERE Periodicidad=\"" + periodo + "\" AND id_maquina=" + id_maquina)) {
                    while (rs.next()) {
                        modelo.addElement(rs.getString("Codigo") + "__" + rs.getString("Descripcion") + "__" + rs.getString("Especificacion"));
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla Operaciones en panelMestra. " + e);
                }
                break;
            default:
                break;
        }
        jList.setModel(modelo);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("IlorcitanaStation");
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/satelite_p.png")).getImage());
        setLocationByPlatform(true);
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/warning.png"))); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));

        jLabel3.setBackground(new java.awt.Color(235, 222, 147));
        jLabel3.setFont(new java.awt.Font("3ds Light", 1, 20)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("¡¡¡ ALERTA DE MANTENIMEINTO SEMANAL !!!");
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        jLabel3.setOpaque(true);

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.background"));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("Se ha inclido una tarea en IlorcitanaStation.\nRevisa la aplicación para realizar el matenimiento.");
        jTextArea1.setAlignmentX(3.0F);
        jTextArea1.setAlignmentY(3.0F);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        jList.setBackground(new java.awt.Color(204, 255, 204));
        jList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 102, 51)));
        jList.setFont(new java.awt.Font("3ds Light", 0, 14)); // NOI18N
        jList.setDoubleBuffered(true);
        jScrollPane1.setViewportView(jList);

        jLabel1.setBackground(new java.awt.Color(235, 222, 147));
        jLabel1.setFont(new java.awt.Font("3ds Light", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Ordenes de mantenimiento.");
        jLabel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 0))));
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(panel_muestra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(panel_muestra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(panel_muestra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(panel_muestra.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new panel_muestra().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JList<String> jList;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
