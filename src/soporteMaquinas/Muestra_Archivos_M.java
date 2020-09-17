/*
 * Clase de tipo Frame donde muestra en pantalla un JFileChooser con los archivos de una ubicación específica
 * donde el usuario puede abrir cualquiera de los ficheros existentes con el programa predeterminado del PC.
 */
package soporteMaquinas;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 *
 * @author IlorcitanaRobotica
 */
public class Muestra_Archivos_M extends javax.swing.JFrame {

    /**
     * Creates new form Muestra_Archivos
     * @param ruta
     */
    public Muestra_Archivos_M() {
        initComponents();
        this.setLocationRelativeTo(null);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (e.getID() == java.awt.event.KeyEvent.KEY_RELEASED
                                && e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                            
                            dispose();
                        }
                        return false;
                    }
                });
    }
    
    public Muestra_Archivos_M(String ruta) {
        initComponents();
        this.setLocationRelativeTo(null);
        jFileChooserAbrir.setCurrentDirectory(new File(ruta));
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if (e.getID() == java.awt.event.KeyEvent.KEY_RELEASED
                                && e.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                            
                            dispose();
                        }
                        return false;
                    }
                });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooserAbrir = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mostrar");
        setBackground(new java.awt.Color(204, 204, 204));
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/satelite_p.png")).getImage());

        jFileChooserAbrir.setApproveButtonText("Aceptar");
        jFileChooserAbrir.setBackground(new java.awt.Color(153, 204, 255));
        jFileChooserAbrir.setCurrentDirectory(new java.io.File("\\\\server\\DATOS\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA"));
        jFileChooserAbrir.setDialogTitle("Abrir ");
        jFileChooserAbrir.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jFileChooserAbrir.setForeground(new java.awt.Color(255, 0, 51));
        jFileChooserAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFileChooserAbrirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jFileChooserAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, 927, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jFileChooserAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jFileChooserAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFileChooserAbrirActionPerformed
        // TODO add your handling code here:
        File a = jFileChooserAbrir.getSelectedFile();
        if (jFileChooserAbrir.accept(a)){
            try {              
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+a);
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
    }//GEN-LAST:event_jFileChooserAbrirActionPerformed

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
            java.util.logging.Logger.getLogger(Muestra_Archivos_M.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Muestra_Archivos_M.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Muestra_Archivos_M.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Muestra_Archivos_M.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Muestra_Archivos_M().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser jFileChooserAbrir;
    // End of variables declaration//GEN-END:variables
}
