/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soporteMaquinas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Diseño
 */
public class Panel_Operaciones extends javax.swing.JFrame {
    
    private Connection con = null;
    private Statement st;
    private ResultSet rs;
    DefaultTableModel n;
    boolean primero = true;
    String id_maquina = "0";
    String numero_maquina = "0";

    /**
     * Creates new form Panel_Operaciones
     */
    public Panel_Operaciones() {
        initComponents();
        conectarMy();
        llenarMaquinas();
        this.setLocationRelativeTo(null);
    }
    
    /**
     * Conectar con la base de datos.
     */
    public final void conectarMy(){
        if (con == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://192.168.0.132:3307/ilorcitana", "irobotica", "1233");
            } catch (ClassNotFoundException | SQLException ex) {
                JOptionPane.showMessageDialog(null,"Error al realizar la conexion "+ex);
                Logger.getLogger(Maquinas_Principal.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
    private void llenarMaquinas(){
        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT descripcion FROM maquinas");
            while (rs.next()) {
                jComboBox_maquinas.addItem(rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla maquinas.");
        }
    }
    
    private int extraeID(){
        String maquina = jComboBox_maquinas.getSelectedItem().toString();
        if (jComboBox_maquinas.getSelectedItem().toString().equals("") || jComboBox_maquinas.getSelectedItem().toString() == null) {
            return Integer.parseInt(id_maquina);
        } else {
            try {
                st = con.createStatement();
                rs = st.executeQuery("SELECT id_maquina,numero_maquina FROM maquinas WHERE descripcion=\"" + maquina + "\"");
                while (rs.next()) {
                    id_maquina = rs.getString("id_maquina");
                    numero_maquina = rs.getString("numero_maquina");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al cargar los valores de la tabla maquinas.");
            }
            return Integer.parseInt(id_maquina);
        }
    }
    
    /**
     * Extrae el contenido de la base de datos y lo inserta en una jtable.
     * @param i
     */
    public void mostrarTabla(int i) {
        
        JComboBox comboBox1 = new JComboBox();
        comboBox1.addItem("diaria");
        comboBox1.addItem("semanal");
        comboBox1.addItem("mensual");
        comboBox1.addItem("trimestral");
        comboBox1.addItem("anual");
        
        JComboBox comboBox2 = new JComboBox();
        comboBox2.addItem("CNC/ Robot/ Máquina");
        comboBox2.addItem("Bomba de vacio");
        comboBox2.addItem("Brazo manipulador");
        comboBox2.addItem("Mesa de trabajo");
        comboBox2.addItem("Cuadro eléctrico");
        comboBox2.addItem("Bomba de presión (W-J)");
        
        try {
            st = con.createStatement();
            ResultSet r = st.executeQuery("SELECT * FROM Operaciones WHERE id_maquina="+i);
            String titulos[] = {"IdOperacion", "Codigo", "Descripcion", "Periodicidad", "Especificacion"};
            n = new DefaultTableModel(null, titulos);
            String fila[] = new String[5];
            while (r.next()) {
                fila[0] = r.getString("IdOperacion");
                fila[1] = r.getString("Codigo");
                fila[2] = r.getString("Descripcion");
                fila[3] = r.getString("Periodicidad");
                fila[4] = r.getString("Especificacion");
                n.addRow(fila);
            }
            Tabla.setModel(n);
            //Tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            Tabla.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboBox1)); 
            Tabla.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBox2));
            //Tabla.setDefaultRenderer(Object.class, new CeldaRenderer(1));
            
            //Tabla.setDefaultRenderer(Object.class, new RenderM());
            Tabla.getColumnModel().getColumn(0).setPreferredWidth(20);
            Tabla.getColumnModel().getColumn(1).setPreferredWidth(20);
            Tabla.getColumnModel().getColumn(2).setPreferredWidth(550);
            Tabla.getColumnModel().getColumn(3).setPreferredWidth(30);
            Tabla.getColumnModel().getColumn(4).setPreferredWidth(100);
            JTableHeader th;
            th = Tabla.getTableHeader();
            Font fuente = new Font("3ds Light", Font.BOLD, 14);
            Color cl = new Color(14, 70, 126);
            th.setForeground(cl);
            th.setFont(fuente);
            Tabla.setShowHorizontalLines(true);
            Tabla.setShowVerticalLines(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de la tabla hhhhh");
        }
    }
    
    private void modificarOperacion() {
        for (int i = 0; i < Tabla.getRowCount(); i++) {
            try {
                String query  = "UPDATE Operaciones SET Codigo="+Integer.parseInt(Tabla.getValueAt(i,1).toString())+",Descripcion=\"" + Tabla.getValueAt(i,2).toString() + "\",Periodicidad=\"" + Tabla.getValueAt(i,3).toString() + "\",Especificacion=\"" + Tabla.getValueAt(i,4).toString() + "\" WHERE IdOperacion =" + Integer.parseInt(Tabla.getValueAt(i,0).toString());
                st = con.createStatement();
                st.executeUpdate(query);
            } catch (SQLException ex) {
                Logger.getLogger(PanelMaquinas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        JOptionPane.showMessageDialog(null, "Operación modificada.");
        mostrarTabla(Integer.parseInt(id_maquina));
    }
    
    private void insertarOperacion() {
        String esp = String.valueOf(jComboBox_especificacion.getSelectedIndex());
        if(esp.length()==1){
            esp ="0"+ esp;
        }
        try {
            String query ="INSERT INTO Operaciones (TipoOperacion, Codigo, Descripcion, Periodicidad, TipoUsuario, Especificacion, id_maquina) VALUES (\"mantenimiento\",\""+Integer.parseInt(numero_maquina+esp+jTextField_codigo.getText())+"\",\""+jTextField_descripcion.getText()+"\",\""+jComboBox_periodo.getSelectedItem().toString()+"\",\"operario\",\""+jComboBox_especificacion.getSelectedItem().toString()+"\","+Integer.parseInt(id_maquina)+")";
            st = con.createStatement();
            st.executeUpdate(query);
            JOptionPane.showMessageDialog(null,"Operación añadida.");   
            mostrarTabla(Integer.parseInt(id_maquina));
        } catch (SQLException ex) {
            Logger.getLogger(PanelMaquinas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void utilJTablePrint(JTable jTable, String header, String footer, boolean showPrintDialog){        
        boolean fitWidth = true;        
        boolean interactive = true;
        // We define the print mode (Definimos el modo de impresión)
        JTable.PrintMode mode = fitWidth ? JTable.PrintMode.FIT_WIDTH : JTable.PrintMode.NORMAL;
        try {
            // Print the table (Imprimo la tabla)             
            boolean complete = jTable.print(mode,
                    new MessageFormat(header),
                    new MessageFormat(footer),
                    showPrintDialog,
                    null,
                    interactive);                 
            if (complete) {
                // Mostramos el mensaje de impresión existosa
                JOptionPane.showMessageDialog(jTable,
                        "Print complete (Impresión completa)",
                        "Print result (Resultado de la impresión)",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Mostramos un mensaje indicando que la impresión fue cancelada                 
                JOptionPane.showMessageDialog(jTable,
                        "Print canceled (Impresión cancelada)",
                        "Print result (Resultado de la impresión)",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(jTable, 
                    "Print fail (Fallo de impresión): " + pe.getMessage(), 
                    "Print result (Resultado de la impresión)", 
                    JOptionPane.ERROR_MESSAGE);
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
        jComboBox_maquinas = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField_codigo = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jComboBox_periodo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jComboBox_especificacion = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jTextField_descripcion = new javax.swing.JTextField();
        jButton_añadir = new javax.swing.JButton();
        jButton_modificar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Operaciones de mantenimineto");
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/editor.png")).getImage());

        jPanel1.setBackground(new java.awt.Color(213, 213, 233));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));

        jComboBox_maquinas.setFont(new java.awt.Font("3ds Light", 0, 14)); // NOI18N
        jComboBox_maquinas.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jComboBox_maquinasInputMethodTextChanged(evt);
            }
        });
        jComboBox_maquinas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jComboBox_maquinasKeyPressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N
        jLabel5.setText("Elíge máquina:");

        jButton1.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/printer.png"))); // NOI18N
        jButton1.setText("Imprimir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox_maquinas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jButton1))
                        .addGap(0, 207, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox_maquinas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPane1.setAutoscrolls(true);

        Tabla.setAutoCreateRowSorter(true);
        Tabla.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Tabla.setFont(new java.awt.Font("3ds Light", 0, 14)); // NOI18N
        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "IdOperación", "Código", "Descripción", "Periodicidad", "Especificación"
            }
        ));
        Tabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        Tabla.setFillsViewportHeight(true);
        Tabla.setSelectionBackground(new java.awt.Color(255, 204, 204));
        Tabla.setSelectionForeground(new java.awt.Color(153, 0, 0));
        jScrollPane1.setViewportView(Tabla);

        jPanel2.setBackground(new java.awt.Color(255, 233, 233));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 204, 204)));

        jLabel1.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N
        jLabel1.setText("Código:");

        jTextField_codigo.setFont(new java.awt.Font("3ds Light", 0, 14)); // NOI18N
        jTextField_codigo.setToolTipText("Tres primeros (número de máquina), dos siguientes (especificación), dos últimos (operación). ");
        jTextField_codigo.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextField_codigo.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N
        jLabel2.setText("Periodicidad:");

        jComboBox_periodo.setFont(new java.awt.Font("3ds Light", 0, 14)); // NOI18N
        jComboBox_periodo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "diaria", "semanal", "mensual", "trimestral", "anual" }));
        jComboBox_periodo.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N
        jLabel3.setText("Especificación:");

        jComboBox_especificacion.setFont(new java.awt.Font("3ds Light", 0, 14)); // NOI18N
        jComboBox_especificacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CNC/ Robot/ Máquina", "Bomba de vacio", "Brazo manipulador", "Mesa de trabajo", "Cuadro eléctrico", "Bomba de presión (W-J)" }));
        jComboBox_especificacion.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N
        jLabel4.setText("Descripción:");

        jTextField_descripcion.setFont(new java.awt.Font("3ds Light", 0, 14)); // NOI18N
        jTextField_descripcion.setEnabled(false);

        jButton_añadir.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N
        jButton_añadir.setText("Añadir operarción");
        jButton_añadir.setEnabled(false);
        jButton_añadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_añadirActionPerformed(evt);
            }
        });

        jButton_modificar.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N
        jButton_modificar.setText("Modificar operación");
        jButton_modificar.setEnabled(false);
        jButton_modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_modificarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_descripcion))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_codigo, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_periodo, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_especificacion, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton_añadir)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_modificar)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox_periodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox_especificacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_añadir)
                    .addComponent(jButton_modificar))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox_maquinasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox_maquinasKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            int i = extraeID();
            mostrarTabla(i);
            jTextField_codigo.setEnabled(true);
            jComboBox_periodo.setEnabled(true);
            jComboBox_especificacion.setEnabled(true);
            jTextField_descripcion.setEnabled(true);
            jButton_añadir.setEnabled(true);
            jButton_modificar.setEnabled(true);
        }
    }//GEN-LAST:event_jComboBox_maquinasKeyPressed

    private void jButton_añadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_añadirActionPerformed
        insertarOperacion();
    }//GEN-LAST:event_jButton_añadirActionPerformed

    private void jButton_modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_modificarActionPerformed
        modificarOperacion();
    }//GEN-LAST:event_jButton_modificarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        utilJTablePrint(Tabla, getTitle(), "Ilorcitana_Station: "+jComboBox_maquinas.getSelectedItem(), true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox_maquinasInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jComboBox_maquinasInputMethodTextChanged
        int i = extraeID();
            mostrarTabla(i);
            jTextField_codigo.setEnabled(true);
            jComboBox_periodo.setEnabled(true);
            jComboBox_especificacion.setEnabled(true);
            jTextField_descripcion.setEnabled(true);
            jButton_añadir.setEnabled(true);
            jButton_modificar.setEnabled(true);
    }//GEN-LAST:event_jComboBox_maquinasInputMethodTextChanged

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
            java.util.logging.Logger.getLogger(Panel_Operaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Panel_Operaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Panel_Operaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Panel_Operaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Panel_Operaciones().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabla;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton_añadir;
    private javax.swing.JButton jButton_modificar;
    private javax.swing.JComboBox<String> jComboBox_especificacion;
    private javax.swing.JComboBox<String> jComboBox_maquinas;
    private javax.swing.JComboBox<String> jComboBox_periodo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField_codigo;
    private javax.swing.JTextField jTextField_descripcion;
    // End of variables declaration//GEN-END:variables
}
