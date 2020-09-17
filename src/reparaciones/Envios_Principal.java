/**
 * En esta clase de tipo frame es donde se configura la interfaz gráfica principal y desde donde se ralizan las 
 * llamadas a las distintas clases.
 */

package reparaciones;

import soporteMaquinas.*;;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import satation.Main;



/**
 *
 * @author Miguel Angel Carrillo Garcia
 */
public final class Envios_Principal extends javax.swing.JFrame {
    
    private static ServerSocket SERVER_SOCKET;
    
    DefaultTableModel n;
    Panel_Reparaciones_Mod mod;
    
    /**
     * Constructo
     * @throws java.sql.SQLException
     */
    public Envios_Principal() throws SQLException {
        initComponents();
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
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
        mostrarTabla();
    }
    
    /**
    * Metodo para saber si los datos pasados son numéricos o no.
     * @param x pasa el string que queremos comprobar.
     * @return debuelve verdadero si es numérico y falso si no lo es.
    */
    public static boolean esNumero(String x){
        return (x.matches("[+-]?\\d*(\\.\\d+)?") && x.equals("")==false);
    }
    
    private String generaConsulta(){
        return "SELECT * FROM envioReparaciones";
//        if(jR_Estado.isSelected()){
//            return "SELECT * FROM envioReparaciones WHERE numero_maquina="+Integer.parseInt(txt_numeroMaquina.getText());
//        }else if(jR_Descripcion.isSelected()){
//            return "SELECT * FROM maquinas WHERE descripcion LIKE \"%"+txt_descripcion.getText()+"%\"";
//        }else if(jR_Fabricante.isSelected()){
//            return "SELECT * FROM maquinas WHERE fabricante LIKE \"%"+txt_fabricante.getText()+"%\"";
//        }else if(jR_Producto.isSelected()&&jR_Ubicacion.isSelected()==false){
//            return "SELECT * FROM maquinas WHERE Empresa=\""+jC_Empresa.getSelectedItem().toString()+"\"";
//        }else if(jR_Producto.isSelected()&&jR_Ubicacion.isSelected()==true){
//            return "SELECT * FROM maquinas WHERE Empresa=\""+jC_Empresa.getSelectedItem().toString()+"\" AND ubicacion=\""+jC_Ubicacion.getSelectedItem().toString()+"\"";
//        }else{
//            return "SELECT * FROM maquinas";
//        }
    }
   
    /**
     * Extrae el contenido de la base de datos y lo inserta en una jtable.
     */
    public void mostrarTabla() {
        String consulta = generaConsulta();
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery(consulta)) {
            String titulos[] = {"Id", "Tipo Producto", "Modelo", "Fabricante", "Descripción", "Lugar de envio", "Responsable de envio", "Estado", "Fecha de envio", "Fecha de recibo"};
            n = new DefaultTableModel(null, titulos);
            String fila[] = new String[10];
            while (r.next()) {
                fila[0] = r.getString("id");
                fila[1] = r.getString("tipo_producto");
                fila[2] = r.getString("modelo");
                fila[3] = r.getString("fabricante");
                fila[4] = r.getString("descripcion");
                fila[5] = r.getString("lugar_envio");
                fila[6] = r.getString("persona_envio");
                fila[7] = r.getString("estado");
                fila[8] = r.getString("fecha_envio");
                fila[9] = r.getString("fecha_recibido");
                n.addRow(fila);
            }
            Tabla.setModel(n);
            Tabla.setDefaultRenderer(Object.class, new RenderEnvios());
            Tabla.getColumnModel().getColumn(0).setPreferredWidth(30);
            Tabla.getColumnModel().getColumn(1).setPreferredWidth(200);
            Tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
            Tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
            Tabla.getColumnModel().getColumn(4).setPreferredWidth(350);
            Tabla.getColumnModel().getColumn(5).setPreferredWidth(200);
            Tabla.getColumnModel().getColumn(6).setPreferredWidth(300);
            Tabla.getColumnModel().getColumn(7).setPreferredWidth(150);
            Tabla.getColumnModel().getColumn(8).setPreferredWidth(150);
            Tabla.getColumnModel().getColumn(9).setPreferredWidth(150);
            JTableHeader th;
            th = Tabla.getTableHeader();
            Font fuente = new Font("Arial", Font.BOLD, 14);
            //Color cl = new Color(0,102,102);
            //th.setForeground(cl);
            th.setFont(fuente);
            Tabla.setShowHorizontalLines(true);
            Tabla.setShowVerticalLines(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos de la tabla Maquinas");
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        Panel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        Tabla = new javax.swing.JTable();
        jR_Estado = new javax.swing.JRadioButton();
        jR_Descripcion = new javax.swing.JRadioButton();
        txt_descripcion = new javax.swing.JTextField();
        jR_Fabricante = new javax.swing.JRadioButton();
        txt_fabricante = new javax.swing.JTextField();
        jR_Producto = new javax.swing.JRadioButton();
        jC_Empresa = new javax.swing.JComboBox<>();
        Boton_Añadir = new javax.swing.JButton();
        jR_Modelo = new javax.swing.JRadioButton();
        txt_fabricante1 = new javax.swing.JTextField();
        jR_Producto1 = new javax.swing.JRadioButton();
        jC_Empresa1 = new javax.swing.JComboBox<>();
        jC_Empresa2 = new javax.swing.JComboBox<>();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Gestión de máquinas");
        setFont(new java.awt.Font("Century", 0, 14)); // NOI18N
        setIconImage(new ImageIcon(getClass().getResource("/imagenes/satelite_p.png")).getImage());
        setPreferredSize(new java.awt.Dimension(1024, 768));

        Panel1.setBackground(new java.awt.Color(204, 204, 204));

        jScrollPane3.setAutoscrolls(true);

        Tabla.setAutoCreateRowSorter(true);
        Tabla.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        Tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id", "Producto", "Modelo", "Fabricante", "Descripción de error", "Lugar de envío", "Gestiona", "Estado", "Fecha de envio", "Fecha recibido"
            }
        ));
        Tabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        Tabla.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Tabla.setFillsViewportHeight(true);
        Tabla.setGridColor(new java.awt.Color(51, 51, 51));
        Tabla.setIntercellSpacing(new java.awt.Dimension(10, 10));
        Tabla.setRowHeight(25);
        Tabla.setSelectionBackground(new java.awt.Color(0, 102, 102));
        Tabla.setSelectionForeground(new java.awt.Color(0, 0, 0));
        Tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                click_tarea(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TablaMousePressed(evt);
            }
        });
        jScrollPane3.setViewportView(Tabla);

        jR_Estado.setBackground(new java.awt.Color(0, 102, 102));
        jR_Estado.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jR_Estado.setForeground(new java.awt.Color(255, 255, 255));
        jR_Estado.setText("Estado:");
        jR_Estado.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_Estado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_EstadoActionPerformed(evt);
            }
        });

        jR_Descripcion.setBackground(new java.awt.Color(0, 102, 102));
        jR_Descripcion.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jR_Descripcion.setForeground(new java.awt.Color(255, 255, 255));
        jR_Descripcion.setText("Descripción:");
        jR_Descripcion.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_Descripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_DescripcionActionPerformed(evt);
            }
        });

        txt_descripcion.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txt_descripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_descripcionActionPerformed(evt);
            }
        });

        jR_Fabricante.setBackground(new java.awt.Color(0, 102, 102));
        jR_Fabricante.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jR_Fabricante.setForeground(new java.awt.Color(255, 255, 255));
        jR_Fabricante.setText("Fabricante:");
        jR_Fabricante.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_Fabricante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_FabricanteActionPerformed(evt);
            }
        });

        txt_fabricante.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txt_fabricante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_fabricanteActionPerformed(evt);
            }
        });

        jR_Producto.setBackground(new java.awt.Color(0, 102, 102));
        jR_Producto.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jR_Producto.setForeground(new java.awt.Color(255, 255, 255));
        jR_Producto.setText("Producto:");
        jR_Producto.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_Producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_ProductoActionPerformed(evt);
            }
        });

        jC_Empresa.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jC_Empresa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Regulador", "Tarjeta", "Fuente de alimentación", "Motor", "Máquina", "Ordenador", "Impresora", "Otros" }));
        jC_Empresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jC_EmpresaActionPerformed(evt);
            }
        });

        Boton_Añadir.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        Boton_Añadir.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Boton_Añadir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/camion-de-mudanzas.png"))); // NOI18N
        Boton_Añadir.setText("Nuevo envio");
        Boton_Añadir.setToolTipText("Add documentos");
        Boton_Añadir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Boton_Añadir.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Boton_Añadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Boton_AñadirActionPerformed(evt);
            }
        });

        jR_Modelo.setBackground(new java.awt.Color(0, 102, 102));
        jR_Modelo.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jR_Modelo.setForeground(new java.awt.Color(255, 255, 255));
        jR_Modelo.setText("Modelo:");
        jR_Modelo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_Modelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_ModeloActionPerformed(evt);
            }
        });

        txt_fabricante1.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txt_fabricante1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_fabricante1ActionPerformed(evt);
            }
        });

        jR_Producto1.setBackground(new java.awt.Color(0, 102, 102));
        jR_Producto1.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jR_Producto1.setForeground(new java.awt.Color(255, 255, 255));
        jR_Producto1.setText("Responsable:");
        jR_Producto1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_Producto1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_Producto1ActionPerformed(evt);
            }
        });

        jC_Empresa1.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jC_Empresa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jC_Empresa1ActionPerformed(evt);
            }
        });

        jC_Empresa2.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jC_Empresa2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "enviado", "recibido", "desechado" }));
        jC_Empresa2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jC_Empresa2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Boton_Añadir, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(jR_Descripcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_descripcion)
                    .addComponent(jR_Fabricante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_fabricante)
                    .addComponent(jR_Producto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jC_Empresa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jR_Estado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jR_Modelo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_fabricante1)
                    .addComponent(jR_Producto1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jC_Empresa1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jC_Empresa2, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 759, Short.MAX_VALUE))
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 721, Short.MAX_VALUE)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addComponent(jR_Producto, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jC_Empresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jR_Modelo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_fabricante1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jR_Fabricante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_fabricante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jR_Estado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jC_Empresa2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(jR_Descripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jR_Producto1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jC_Empresa1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Boton_Añadir)))
                .addContainerGap())
        );

        jMenu1.setBackground(new java.awt.Color(255, 255, 255));
        jMenu1.setText("Operaciones");
        jMenu1.setFont(new java.awt.Font("3ds Light", 1, 14)); // NOI18N
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jRadioButtonMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("Registrar máquina");
        jRadioButtonMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jRadioButtonMenuItem1);

        jMenuItem1.setText("mantenimiento");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("imprimir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void click_tarea(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_click_tarea
        
        
    }//GEN-LAST:event_click_tarea

    private void jR_EstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_EstadoActionPerformed
        if(jR_Estado.isSelected()){
            jR_Descripcion.setSelected(false);
            jR_Fabricante.setSelected(false);
            jR_Producto.setSelected(false);
        }else{
            mostrarTabla();
            repaint();
        }
    }//GEN-LAST:event_jR_EstadoActionPerformed

    private void jR_DescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_DescripcionActionPerformed
        if(jR_Descripcion.isSelected()){
            jR_Estado.setSelected(false);
            jR_Fabricante.setSelected(false);
            jR_Producto.setSelected(false);
        }else{
            mostrarTabla();
            repaint();
        }
    }//GEN-LAST:event_jR_DescripcionActionPerformed

    private void jR_FabricanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_FabricanteActionPerformed
        if(jR_Fabricante.isSelected()){
            jR_Descripcion.setSelected(false);
            jR_Estado.setSelected(false);
            jR_Producto.setSelected(false);
        }else{
            mostrarTabla();
            repaint();
        }
    }//GEN-LAST:event_jR_FabricanteActionPerformed

    private void jR_ProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_ProductoActionPerformed
        if (jR_Producto.isSelected()) {
            jR_Descripcion.setSelected(false);
            jR_Fabricante.setSelected(false);
            jR_Estado.setSelected(false);
        }else{
            mostrarTabla();
            repaint();
        }
    }//GEN-LAST:event_jR_ProductoActionPerformed

    private void txt_descripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_descripcionActionPerformed
        mostrarTabla();
        repaint();
    }//GEN-LAST:event_txt_descripcionActionPerformed

    private void txt_fabricanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_fabricanteActionPerformed
        mostrarTabla();
        repaint();
    }//GEN-LAST:event_txt_fabricanteActionPerformed

    private void jC_EmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jC_EmpresaActionPerformed
        mostrarTabla();
        repaint();
    }//GEN-LAST:event_jC_EmpresaActionPerformed

    private void Boton_AñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Boton_AñadirActionPerformed
        Panel_Reparaciones p = new Panel_Reparaciones();
        p.setVisible(true);
    }//GEN-LAST:event_Boton_AñadirActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Panel_Operaciones panel = new Panel_Operaciones();
        panel.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        utilJTablePrint(Tabla, getTitle(), "Ilorcitana_Station", true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void TablaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaMousePressed
        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery("SELECT * FROM envioReparaciones WHERE id=\"" + Tabla.getValueAt(Tabla.getSelectedRow(), 0) + "\"")) {
            while (r.next()) {
                String h = r.getString("id");
                mod = new Panel_Reparaciones_Mod(h);
                mod.setVisible(true);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Envios_Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        mostrarTabla();
    }//GEN-LAST:event_TablaMousePressed

    private void jRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem1ActionPerformed
        PanelMaquinas mod = new PanelMaquinas();
        mod.setVisible(true);
    }//GEN-LAST:event_jRadioButtonMenuItem1ActionPerformed

    private void jR_ModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_ModeloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jR_ModeloActionPerformed

    private void txt_fabricante1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_fabricante1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_fabricante1ActionPerformed

    private void jR_Producto1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_Producto1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jR_Producto1ActionPerformed

    private void jC_Empresa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jC_Empresa1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jC_Empresa1ActionPerformed

    private void jC_Empresa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jC_Empresa2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jC_Empresa2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Envios_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                SERVER_SOCKET = new ServerSocket(1332);
                new Envios_Principal().setVisible(true);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(Envios_Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Boton_Añadir;
    public javax.swing.JPanel Panel1;
    private javax.swing.JTable Tabla;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> jC_Empresa;
    private javax.swing.JComboBox<String> jC_Empresa1;
    private javax.swing.JComboBox<String> jC_Empresa2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JRadioButton jR_Descripcion;
    private javax.swing.JRadioButton jR_Estado;
    private javax.swing.JRadioButton jR_Fabricante;
    private javax.swing.JRadioButton jR_Modelo;
    private javax.swing.JRadioButton jR_Producto;
    private javax.swing.JRadioButton jR_Producto1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField txt_descripcion;
    private javax.swing.JTextField txt_fabricante;
    private javax.swing.JTextField txt_fabricante1;
    // End of variables declaration//GEN-END:variables
}

