/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reparaciones;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import satation.Main;
import satation.Propiedades;
import soporteMaquinas.PanelMaquinas;

/**
 *
 * @author Diseño
 */
public class Panel_Reparaciones extends javax.swing.JFrame {
    
    String ruta = "\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\ENVIO_REPARACIONES\\";
    int resultado=0;

    /**
     * Creates new form Panel_Reparaciones
     */
    public Panel_Reparaciones() {
        initComponents();
        this.setLocationRelativeTo(null);
        llenarUsuarios();
        compruebaId();
    }
    
    private void compruebaId(){
        String query ="SELECT MAX(id)+1 FROM envioReparaciones";
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                resultado = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PanelMaquinas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void llenarUsuarios(){
        combo_responsable.removeAllItems();
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT Nombre,Clave FROM UsuariosP ORDER BY nombre ASC;")) {
            combo_responsable.setEnabled(true);
            combo_responsable.removeAllItems();
            while (rs.next()) {
                combo_responsable.addItem(rs.getString("Nombre"));
                if (rs.getString("Clave").equals(Propiedades.getPropiedad("clave"))) {
                    combo_responsable.setSelectedItem(rs.getString("Nombre"));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(rootPane, e);
        }
    }
    
    private void crearArchivo(){
        File directorio;
        directorio = new File(ruta + resultado);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }
    
    private void insertarMaquina() {
        String fecha = new SimpleDateFormat("yyyy-MM-dd").format(calendar_fecha.getDate());
        String query ="INSERT INTO `envioReparaciones`(`tipo_producto`, `modelo`, `fabricante`, `descripcion`, `lugar_envio`, `persona_envio`, `fecha_envio`) VALUES (\""+combo_producto.getSelectedItem().toString()+"\",\""+txt_modelo.getText()+"\",\""+txt_fabricante.getText()+"\",\""+area_problema.getText()+"\",\""+txt_envio.getText()+"\",\""+combo_responsable.getSelectedItem().toString()+"\",'"+fecha+"')";           
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();) {
            stmt.executeUpdate(query);
            JOptionPane.showMessageDialog(null,"Máquina MODIFICADA correctamente.");   
            crearArchivo();
            //dispose();
        } catch (SQLException ex) {
            Logger.getLogger(PanelMaquinas.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_modelo = new javax.swing.JTextField();
        lavel_problema = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        area_problema = new javax.swing.JTextArea();
        lavel_envio = new javax.swing.JLabel();
        txt_envio = new javax.swing.JTextField();
        lavel_responsable = new javax.swing.JLabel();
        combo_responsable = new javax.swing.JComboBox<>();
        boton_registra = new javax.swing.JButton();
        calendar_fecha = new org.freixas.jcalendar.JCalendarCombo();
        lavel_fecha = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_fabricante = new javax.swing.JTextField();
        boton_adjunta = new javax.swing.JButton();
        combo_producto = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registro de reparaciones");
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/satelite_p.png")).getImage());
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setBackground(new java.awt.Color(0, 102, 102));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tipo de producto:");
        jLabel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 51), 1, true));
        jLabel1.setOpaque(true);

        jLabel2.setBackground(new java.awt.Color(0, 102, 102));
        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Modelo:");
        jLabel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 51), 1, true));
        jLabel2.setOpaque(true);

        txt_modelo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        lavel_problema.setBackground(new java.awt.Color(0, 102, 102));
        lavel_problema.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lavel_problema.setForeground(new java.awt.Color(255, 255, 255));
        lavel_problema.setText("Descripción del problema:");
        lavel_problema.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 51), 1, true));
        lavel_problema.setOpaque(true);

        area_problema.setColumns(20);
        area_problema.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        area_problema.setRows(5);
        jScrollPane1.setViewportView(area_problema);

        lavel_envio.setBackground(new java.awt.Color(0, 102, 102));
        lavel_envio.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lavel_envio.setForeground(new java.awt.Color(255, 255, 255));
        lavel_envio.setText("Donde se envía:");
        lavel_envio.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 51), 1, true));
        lavel_envio.setOpaque(true);

        txt_envio.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        lavel_responsable.setBackground(new java.awt.Color(0, 102, 102));
        lavel_responsable.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lavel_responsable.setForeground(new java.awt.Color(255, 255, 255));
        lavel_responsable.setText("Responsable:");
        lavel_responsable.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 51), 1, true));
        lavel_responsable.setOpaque(true);

        combo_responsable.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        combo_responsable.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        boton_registra.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        boton_registra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/archivar.png"))); // NOI18N
        boton_registra.setToolTipText("Registar envio");
        boton_registra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_registraActionPerformed(evt);
            }
        });

        calendar_fecha.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.gray, java.awt.Color.lightGray, java.awt.Color.gray, java.awt.Color.lightGray));
        calendar_fecha.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        lavel_fecha.setBackground(new java.awt.Color(0, 102, 102));
        lavel_fecha.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lavel_fecha.setForeground(new java.awt.Color(255, 255, 255));
        lavel_fecha.setText("Fecha de envío:");
        lavel_fecha.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 51), 1, true));
        lavel_fecha.setOpaque(true);

        jLabel3.setBackground(new java.awt.Color(0, 102, 102));
        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Fabricante:");
        jLabel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 51, 51), 1, true));
        jLabel3.setOpaque(true);

        txt_fabricante.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        boton_adjunta.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        boton_adjunta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/adjuntar.png"))); // NOI18N
        boton_adjunta.setToolTipText("Adjuntar documentos");
        boton_adjunta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_adjuntaActionPerformed(evt);
            }
        });

        combo_producto.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        combo_producto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sin producto", "Regulador", "Tarjeta", "Fuente de alimentación", "Motor", "Máquina", "Ordenador", "Consola", "CNC", "Impresora", "Otros" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lavel_envio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(combo_responsable, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_envio)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_modelo)
                            .addComponent(txt_fabricante)
                            .addComponent(combo_producto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lavel_problema)
                            .addComponent(boton_adjunta)
                            .addComponent(lavel_responsable, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 301, Short.MAX_VALUE)
                        .addComponent(boton_registra))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(calendar_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lavel_fecha))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(combo_producto))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_modelo)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_fabricante)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lavel_problema, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_envio)
                            .addComponent(lavel_envio, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(combo_responsable)
                            .addComponent(lavel_responsable, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(lavel_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calendar_fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(123, 123, 123)
                        .addComponent(boton_adjunta))
                    .addComponent(boton_registra))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void boton_adjuntaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_adjuntaActionPerformed
        crearArchivo();
        Adjunta_ArchivosEnvio archivo = new Adjunta_ArchivosEnvio(ruta+resultado);
        archivo.setVisible(true);
    }//GEN-LAST:event_boton_adjuntaActionPerformed

    private void boton_registraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_registraActionPerformed
        insertarMaquina();
    }//GEN-LAST:event_boton_registraActionPerformed

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
            java.util.logging.Logger.getLogger(Panel_Reparaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Panel_Reparaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Panel_Reparaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Panel_Reparaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Panel_Reparaciones().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea area_problema;
    private javax.swing.JButton boton_adjunta;
    private javax.swing.JButton boton_registra;
    private org.freixas.jcalendar.JCalendarCombo calendar_fecha;
    private javax.swing.JComboBox<String> combo_producto;
    private javax.swing.JComboBox<String> combo_responsable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lavel_envio;
    private javax.swing.JLabel lavel_fecha;
    private javax.swing.JLabel lavel_problema;
    private javax.swing.JLabel lavel_responsable;
    private javax.swing.JTextField txt_envio;
    private javax.swing.JTextField txt_fabricante;
    private javax.swing.JTextField txt_modelo;
    // End of variables declaration//GEN-END:variables
}
