/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package satation;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import tareasIlorcitana.principalTareas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Diseño.
 */
public class panel_loging extends javax.swing.JFrame {
    
    private static ServerSocket SERVER_SOCKET;

    /**
     * Creates new form pnael_loging
     */
    public panel_loging() {
        
        //ImageFondo image = new ImageFondo();
        //image.setImage("/imagenes/estacion.jpg");
        //setContentPane(image);
        initComponents();
        this.setLocationRelativeTo(null);
        jPassword.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    iniciarSesion(); //Método que tienes que crearte
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        jPassword1.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    iniciarSesion(); //Método que tienes que crearte
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }
    
    private boolean buscaUsuario() {
        boolean existe = false;
        
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM UsuariosP")) {
            while (rs.next()) {
                char[] us = rs.getString("Clave").toCharArray();
                if(Arrays.equals(jPassword.getPassword(), us)){
                    existe = true;
                    Propiedades.setPropiedad("clave", String.valueOf(jPassword.getPassword()));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(panel_loging.class.getName()).log(Level.SEVERE, null, ex);
        }
        return existe;
    }

    /**
     * metodo para iniciar sesion.
     */
    void iniciarSesion() {
        char passM[] = {'0', '0', '1'};
        char passA[] = {'a', 'd', 'm', 'i', 'n'};
        char passI[] = {'i', 'n', 'f', 'o', 'r'};
        if (buscaUsuario()) {
            if(jComboBox1.getSelectedItem().equals("Mantenimiento Operario")){
                ecribe("mantenimiento operario");
                principalTareas prin = new principalTareas();
                dispose();
            } else if (jComboBox1.getSelectedItem().equals("Mecánica")) {
                if (Arrays.equals(jPassword1.getPassword(), passM)) {
                    ecribe("mecanica");
                    principalTareas prin = new principalTareas();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
                }
            } else if (jComboBox1.getSelectedItem().equals("Programación")) {
                if (Arrays.equals(jPassword1.getPassword(), passI)) {
                    ecribe("programacion");
                    principalTareas prin = new principalTareas();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
                }
            } else if (jComboBox1.getSelectedItem().equals("Administrador")) {
                
                if (Arrays.equals(jPassword1.getPassword(), passA)) {
                    ecribe("administrador");
                    PanelStation panel = new PanelStation();
                    panel.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta");
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "El usuario no existe.");
        }
    }
    
    public void ecribe(String reg) {
       Propiedades.setPropiedad("usuario", reg);
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
        jLabel3 = new javax.swing.JLabel();
        jPassword = new javax.swing.JPasswordField();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPassword1 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Loging");
        setBackground(java.awt.SystemColor.info);
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/satelite_p.png")).getImage());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("3ds Light", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(46, 125, 176));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/autorizacion.png"))); // NOI18N
        jLabel1.setText("Control de acceso");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(23, 77, 113));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/blanco.jpg"))); // NOI18N
        jLabel3.setText("Código de usuario:");
        jLabel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(46, 125, 176)));
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jPassword.setFont(new java.awt.Font("3ds Light", 0, 18)); // NOI18N
        jPassword.setForeground(new java.awt.Color(23, 77, 113));
        jPassword.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(46, 125, 176)));

        jComboBox1.setFont(new java.awt.Font("3ds Light", 1, 18)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mantenimiento Operario", "Mecánica", "Programación", "Administrador" }));
        jComboBox1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(46, 125, 176)));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jPassword1.setFont(new java.awt.Font("3ds Light", 0, 18)); // NOI18N
        jPassword1.setForeground(new java.awt.Color(23, 77, 113));
        jPassword1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPassword1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(46, 125, 176)));
        jPassword1.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPassword1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPassword1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(101, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        if(jComboBox1.getSelectedItem().equals("Mecánica")||jComboBox1.getSelectedItem().equals("Programación")||jComboBox1.getSelectedItem().equals("Administrador")){
            jPassword1.setEnabled(true);
        }else{
            jPassword1.setEnabled(false);
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]){

        try {
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
                java.util.logging.Logger.getLogger(panel_loging.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(panel_loging.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(panel_loging.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(panel_loging.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
            //</editor-fold>
            SERVER_SOCKET = new ServerSocket(1111);
            /* Create and display the form */
            java.awt.EventQueue.invokeLater(() -> {
                new panel_loging().setVisible(true);
            });
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "La aplicación ya está abierta.", "IlorcitanaStation", 1);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPassword;
    private javax.swing.JPasswordField jPassword1;
    // End of variables declaration//GEN-END:variables
}
