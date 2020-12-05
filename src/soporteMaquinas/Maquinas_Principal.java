/**
 * En esta clase de tipo frame es donde se configura la interfaz gráfica principal y desde donde se ralizan las 
 * llamadas a las distintas clases.
 */

package soporteMaquinas;

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
public final class Maquinas_Principal extends javax.swing.JFrame {
    
    private static ServerSocket SERVER_SOCKET;
    
    DefaultTableModel n;
    PanelMaquinas mod;
    
    /**
     * Constructo
     * @throws java.sql.SQLException
     */
    public Maquinas_Principal() throws SQLException {
        initComponents();
        jLabel_sala.setVisible(false);
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
        if(jR_numeroMaquina.isSelected()){
            return "SELECT * FROM maquinas WHERE numero_maquina="+Integer.parseInt(txt_numeroMaquina.getText())+" AND  estado=\"habilitada\"";
        }else if(jR_Descripcion.isSelected()){
            return "SELECT * FROM maquinas WHERE descripcion LIKE \"%"+txt_descripcion.getText()+"%\" AND estado=\"habilitada\"";
        }else if(jR_Fabricante.isSelected()){
            return "SELECT * FROM maquinas WHERE fabricante LIKE \"%"+txt_fabricante.getText()+"%\" AND estado=\"habilitada\"";
        }else if(jR_Empresa.isSelected()&&jR_Ubicacion.isSelected()==false){
            return "SELECT * FROM maquinas WHERE Empresa=\""+jC_Empresa.getSelectedItem().toString()+"\" AND  estado=\"habilitada\"";
        }else if(jR_Empresa.isSelected()&&jR_Ubicacion.isSelected()==true){
            return "SELECT * FROM maquinas WHERE Empresa=\""+jC_Empresa.getSelectedItem().toString()+"\" AND ubicacion=\""+jC_Ubicacion.getSelectedItem().toString()+"\" AND estado=\"habilitada\"";
        }else{
            return "SELECT * FROM maquinas WHERE estado=\"habilitada\"";
        }
    }
    
    private String generaConsultaDeshabilitadas(){
        if(jR_numeroMaquina.isSelected()){
            return "SELECT * FROM maquinas WHERE numero_maquina="+Integer.parseInt(txt_numeroMaquina.getText())+" AND  estado=\"deshabilitada\"";
        }else if(jR_Descripcion.isSelected()){
            return "SELECT * FROM maquinas WHERE descripcion LIKE \"%"+txt_descripcion.getText()+"%\" AND estado=\"deshabilitada\"";
        }else if(jR_Fabricante.isSelected()){
            return "SELECT * FROM maquinas WHERE fabricante LIKE \"%"+txt_fabricante.getText()+"%\" AND estado=\"deshabilitada\"";
        }else if(jR_Empresa.isSelected()&&jR_Ubicacion.isSelected()==false){
            return "SELECT * FROM maquinas WHERE Empresa=\""+jC_Empresa.getSelectedItem().toString()+"\" AND  estado=\"deshabilitada\"";
        }else if(jR_Empresa.isSelected()&&jR_Ubicacion.isSelected()==true){
            return "SELECT * FROM maquinas WHERE Empresa=\""+jC_Empresa.getSelectedItem().toString()+"\" AND ubicacion=\""+jC_Ubicacion.getSelectedItem().toString()+"\" AND estado=\"deshabilitada\"";
        }else{
            return "SELECT * FROM maquinas WHERE estado=\"deshabilitada\"";
        }
    }
    
    private String generaConsultaSala(String sala){
        switch (sala) {
            case "1A":
                return "SELECT * FROM maquinas WHERE sala=\"Sala 1A (CIM, N1)\"";
            case "1B":
                return "SELECT * FROM maquinas WHERE sala=\"Sala 1B (CIM, N1)\"";
            case "2":
                return "SELECT * FROM maquinas WHERE sala=\"Sala 2 (CIM, N3)\"";
            case "3":
                return "SELECT * FROM maquinas WHERE sala=\"Sala 3 (5A)\"";
            case "4":
                return "SELECT * FROM maquinas WHERE sala=\"Sala 4 (CS)\"";
            default:
                return "SELECT * FROM maquinas WHERE sala !=null";
        }
    }
   
    /**
     * Extrae el contenido de la base de datos y lo inserta en una jtable.
     */
    public void mostrarTabla() {
        
        String consulta= "";
        
        if (jRadioButtonMenuItem_deshabilitadas.isSelected()) {
            consulta = generaConsultaDeshabilitadas();
        } else if (jRadioButtonMenuItem_Sala1A.isSelected()) {
            consulta = generaConsultaSala("1A");
        } else if (jRadioButtonMenuItem_Sala1B.isSelected()) {
            consulta = generaConsultaSala("1B");
        } else if (jRadioButtonMenuItem_Sala2.isSelected()) {
            consulta = generaConsultaSala("2");
        } else if (jRadioButtonMenuItem_Sala3.isSelected()) {
            consulta = generaConsultaSala("3");
        } else if (jRadioButtonMenuItem_Sala4.isSelected()) {
            consulta = generaConsultaSala("4");
        } else {
            consulta = generaConsulta();
        }

        try (Connection conn = DriverManager.getConnection(Main.driver, Main.usuario, Main.clave);
            Statement stmt = conn.createStatement();
            ResultSet r = stmt.executeQuery(consulta)) {
            String titulos[] = {"Modelo", "Descripción", "Número de máquina", "Fabricante", "Empresa", "Ubicación", "M_semanal", "M_mensual", "M_trimestral", "M_anual"};
            n = new DefaultTableModel(null, titulos);
            String fila[] = new String[10];
            while (r.next()) {
                fila[0] = r.getString("modelo");
                fila[1] = r.getString("descripcion");
                fila[2] = r.getString("numero_maquina");
                fila[3] = r.getString("fabricante");
                fila[4] = r.getString("Empresa");
                fila[5] = r.getString("ubicacion");
                fila[6] = r.getString("M_Semanal");
                fila[7] = r.getString("M_Mensual");
                fila[8] = r.getString("M_Trimestral");
                fila[9] = r.getString("M_Anual");
                n.addRow(fila);
            }
            Tabla.setModel(n);
            Tabla.setDefaultRenderer(Object.class, new RenderM());
            Tabla.getColumnModel().getColumn(0).setPreferredWidth(250);
            Tabla.getColumnModel().getColumn(1).setPreferredWidth(300);
            Tabla.getColumnModel().getColumn(2).setPreferredWidth(150);
            Tabla.getColumnModel().getColumn(3).setPreferredWidth(300);
            Tabla.getColumnModel().getColumn(4).setPreferredWidth(250);
            Tabla.getColumnModel().getColumn(5).setPreferredWidth(250);
            Tabla.getColumnModel().getColumn(6).setPreferredWidth(150);
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
        botonPrincipal = new javax.swing.JButton();
        jLabel_titulo = new javax.swing.JLabel();
        txt_numeroMaquina = new javax.swing.JTextField();
        jR_numeroMaquina = new javax.swing.JRadioButton();
        jR_Descripcion = new javax.swing.JRadioButton();
        txt_descripcion = new javax.swing.JTextField();
        jR_Fabricante = new javax.swing.JRadioButton();
        txt_fabricante = new javax.swing.JTextField();
        jR_Empresa = new javax.swing.JRadioButton();
        jC_Empresa = new javax.swing.JComboBox<>();
        jC_Ubicacion = new javax.swing.JComboBox<>();
        jR_Ubicacion = new javax.swing.JRadioButton();
        boton_documentos = new javax.swing.JButton();
        Boton_Añadir = new javax.swing.JButton();
        jLabel_sala = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jRadioButtonMenuItem_deshabilitadas = new javax.swing.JRadioButtonMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jRadioButtonMenuItem_Sala1A = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem_Sala1B = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem_Sala2 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem_Sala3 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem_Sala4 = new javax.swing.JRadioButtonMenuItem();

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
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tares", "Preferencia", "Estado", "Fecha", "Observaciones"
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

        botonPrincipal.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        botonPrincipal.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        botonPrincipal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/editor_boton.png"))); // NOI18N
        botonPrincipal.setText("Panel Principal");
        botonPrincipal.setToolTipText("Ir a panel principal");
        botonPrincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botonPrincipal.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        botonPrincipal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPrincipalActionPerformed(evt);
            }
        });

        jLabel_titulo.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        jLabel_titulo.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel_titulo.setForeground(new java.awt.Color(153, 0, 0));
        jLabel_titulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_titulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/z_robot_1.png"))); // NOI18N
        jLabel_titulo.setText("Gestión de máquinas");
        jLabel_titulo.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel_titulo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        jLabel_titulo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel_titulo.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jLabel_titulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel_tituloMousePressed(evt);
            }
        });

        txt_numeroMaquina.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        txt_numeroMaquina.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_numeroMaquina.setText("0");
        txt_numeroMaquina.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                txt_numeroMaquinaAncestorRemoved(evt);
            }
        });
        txt_numeroMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_numeroMaquinaActionPerformed(evt);
            }
        });

        jR_numeroMaquina.setBackground(new java.awt.Color(0, 102, 102));
        jR_numeroMaquina.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jR_numeroMaquina.setForeground(new java.awt.Color(255, 255, 255));
        jR_numeroMaquina.setText("Nº de máquina:");
        jR_numeroMaquina.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_numeroMaquina.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_numeroMaquinaActionPerformed(evt);
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

        jR_Empresa.setBackground(new java.awt.Color(0, 102, 102));
        jR_Empresa.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jR_Empresa.setForeground(new java.awt.Color(255, 255, 255));
        jR_Empresa.setText("Empresa:");
        jR_Empresa.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_Empresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_EmpresaActionPerformed(evt);
            }
        });

        jC_Empresa.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jC_Empresa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Comercial Ilorcitana", "Canela Spring" }));
        jC_Empresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jC_EmpresaActionPerformed(evt);
            }
        });

        jC_Ubicacion.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jC_Ubicacion.setEnabled(false);
        jC_Ubicacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jC_UbicacionActionPerformed(evt);
            }
        });

        jR_Ubicacion.setBackground(new java.awt.Color(0, 102, 102));
        jR_Ubicacion.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jR_Ubicacion.setForeground(new java.awt.Color(255, 255, 255));
        jR_Ubicacion.setText("Ubicación:");
        jR_Ubicacion.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jR_Ubicacion.setEnabled(false);
        jR_Ubicacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jR_UbicacionActionPerformed(evt);
            }
        });

        boton_documentos.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        boton_documentos.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        boton_documentos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/z_buscar documentos.png"))); // NOI18N
        boton_documentos.setText("Ver Documentos");
        boton_documentos.setToolTipText("Ver documentos");
        boton_documentos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        boton_documentos.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        boton_documentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boton_documentosActionPerformed(evt);
            }
        });

        Boton_Añadir.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.light"));
        Boton_Añadir.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        Boton_Añadir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/z_add documentos.png"))); // NOI18N
        Boton_Añadir.setText("Add Documentos");
        Boton_Añadir.setToolTipText("Add documentos");
        Boton_Añadir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Boton_Añadir.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Boton_Añadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Boton_AñadirActionPerformed(evt);
            }
        });

        jLabel_sala.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel_sala.setForeground(new java.awt.Color(153, 0, 0));
        jLabel_sala.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_sala.setText("jLabel1");
        jLabel_sala.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));

        javax.swing.GroupLayout Panel1Layout = new javax.swing.GroupLayout(Panel1);
        Panel1.setLayout(Panel1Layout);
        Panel1Layout.setHorizontalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel_titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addComponent(jR_numeroMaquina)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_numeroMaquina))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jC_Ubicacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jR_Ubicacion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24))
                    .addComponent(botonPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Boton_Añadir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jR_Descripcion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_descripcion)
                    .addComponent(jR_Fabricante, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_fabricante)
                    .addComponent(jR_Empresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jC_Empresa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(boton_documentos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 755, Short.MAX_VALUE)
                    .addComponent(jLabel_sala, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        Panel1Layout.setVerticalGroup(
            Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addComponent(jLabel_sala, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3))
                    .addGroup(Panel1Layout.createSequentialGroup()
                        .addComponent(jLabel_titulo)
                        .addGap(18, 18, 18)
                        .addGroup(Panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_numeroMaquina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jR_numeroMaquina))
                        .addGap(18, 18, 18)
                        .addComponent(jR_Descripcion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jR_Fabricante)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_fabricante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jR_Empresa, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jC_Empresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jR_Ubicacion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jC_Ubicacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addComponent(Boton_Añadir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(boton_documentos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botonPrincipal)))
                .addContainerGap())
        );

        jMenu1.setBackground(new java.awt.Color(255, 255, 255));
        jMenu1.setText("Operaciones");
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

        jMenu2.setText("Opciones");

        jRadioButtonMenuItem_deshabilitadas.setText("Máquinas deshabilitadas");
        jRadioButtonMenuItem_deshabilitadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem_deshabilitadasActionPerformed(evt);
            }
        });
        jMenu2.add(jRadioButtonMenuItem_deshabilitadas);

        jMenu3.setText("Salas compresores");

        jRadioButtonMenuItem_Sala1A.setText("Sala 1A (CIM, N1)");
        jRadioButtonMenuItem_Sala1A.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem_Sala1AActionPerformed(evt);
            }
        });
        jMenu3.add(jRadioButtonMenuItem_Sala1A);

        jRadioButtonMenuItem_Sala1B.setText("Sala 1B (CIM, N1)");
        jRadioButtonMenuItem_Sala1B.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem_Sala1BActionPerformed(evt);
            }
        });
        jMenu3.add(jRadioButtonMenuItem_Sala1B);

        jRadioButtonMenuItem_Sala2.setText("Sala 2 (CIM, N3)");
        jRadioButtonMenuItem_Sala2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem_Sala2ActionPerformed(evt);
            }
        });
        jMenu3.add(jRadioButtonMenuItem_Sala2);

        jRadioButtonMenuItem_Sala3.setText("Sala 3 (5A)");
        jRadioButtonMenuItem_Sala3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem_Sala3ActionPerformed(evt);
            }
        });
        jMenu3.add(jRadioButtonMenuItem_Sala3);

        jRadioButtonMenuItem_Sala4.setText("Sala 4 (CS)");
        jRadioButtonMenuItem_Sala4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem_Sala4ActionPerformed(evt);
            }
        });
        jMenu3.add(jRadioButtonMenuItem_Sala4);

        jMenu2.add(jMenu3);

        jMenuBar1.add(jMenu2);

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

    private void botonPrincipalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPrincipalActionPerformed
        this.dispose();
        mod.dispose();
    }//GEN-LAST:event_botonPrincipalActionPerformed

    private void jR_numeroMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_numeroMaquinaActionPerformed
        if(jR_numeroMaquina.isSelected()){
            jR_Descripcion.setSelected(false);
            jR_Fabricante.setSelected(false);
            jR_Empresa.setSelected(false);
        }else{
            mostrarTabla();
            repaint();
        }
    }//GEN-LAST:event_jR_numeroMaquinaActionPerformed

    private void jR_DescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_DescripcionActionPerformed
        if(jR_Descripcion.isSelected()){
            jR_numeroMaquina.setSelected(false);
            jR_Fabricante.setSelected(false);
            jR_Empresa.setSelected(false);
        }else{
            mostrarTabla();
            repaint();
        }
    }//GEN-LAST:event_jR_DescripcionActionPerformed

    private void jR_FabricanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_FabricanteActionPerformed
        if(jR_Fabricante.isSelected()){
            jR_Descripcion.setSelected(false);
            jR_numeroMaquina.setSelected(false);
            jR_Empresa.setSelected(false);
        }else{
            mostrarTabla();
            repaint();
        }
    }//GEN-LAST:event_jR_FabricanteActionPerformed

    private void jR_EmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_EmpresaActionPerformed
        if (jR_Empresa.isSelected()) {
            jR_Descripcion.setSelected(false);
            jR_Fabricante.setSelected(false);
            jR_numeroMaquina.setSelected(false);
            jR_Ubicacion.setEnabled(true);
            jC_Ubicacion.setEnabled(true);
            if (jC_Empresa.getSelectedItem().equals("Comercial Ilorcitana")) {
                jC_Ubicacion.addItem("Nave 1");
                jC_Ubicacion.addItem("Nave 2");
                jC_Ubicacion.addItem("Nave 3");
                jC_Ubicacion.addItem("Nave 4");
                jC_Ubicacion.addItem("Pasillo 1");
                jC_Ubicacion.addItem("Pasillo 2");
                jC_Ubicacion.addItem("Pasillo 3");
                jC_Ubicacion.addItem("Altillo 1");
                jC_Ubicacion.addItem("Altillo 2");
                jC_Ubicacion.addItem("Altillo 3");
                jC_Ubicacion.addItem("Nave 5_A");
                jC_Ubicacion.addItem("Nave 5_B");
            } else if (jC_Empresa.getSelectedItem().equals("Canela Spring")) {
                jC_Ubicacion.addItem("Nave 1");
                jC_Ubicacion.addItem("Nave 2");
                jC_Ubicacion.addItem("Nave 3");
                jC_Ubicacion.addItem("Nave 4");
                jC_Ubicacion.addItem("Nave 5");
                jC_Ubicacion.addItem("Pasillo");
                jC_Ubicacion.addItem("Exterior 1");
            }
        }else{
            mostrarTabla();
            repaint();
        }
    }//GEN-LAST:event_jR_EmpresaActionPerformed

    private void txt_numeroMaquinaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_numeroMaquinaActionPerformed
        mostrarTabla();
        repaint();
    }//GEN-LAST:event_txt_numeroMaquinaActionPerformed

    private void jR_UbicacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jR_UbicacionActionPerformed
        
        mostrarTabla();
        repaint();
    }//GEN-LAST:event_jR_UbicacionActionPerformed

    private void txt_numeroMaquinaAncestorRemoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_txt_numeroMaquinaAncestorRemoved
       mostrarTabla();
       repaint();
    }//GEN-LAST:event_txt_numeroMaquinaAncestorRemoved

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

    private void jC_UbicacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jC_UbicacionActionPerformed
        mostrarTabla();
        repaint();
    }//GEN-LAST:event_jC_UbicacionActionPerformed

    private void boton_documentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boton_documentosActionPerformed
        //Muestra_Archivos_M m = new Muestra_Archivos_M();
        //m.setVisible(true);
        String rut = "\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA";
        try {
            Runtime.getRuntime().exec("explorer.exe /start," + rut);
        } catch (IOException ex) {
            Logger.getLogger(Maquinas_Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_boton_documentosActionPerformed

    private void Boton_AñadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Boton_AñadirActionPerformed
        Adjunta_Archivos_M m = new Adjunta_Archivos_M("\\\\server\\datos\\GESCIM\\Gescim\\MODFACTUSOL\\DOCUMENTOS\\INFO_MAQUINARIA\\");
        m.setVisible(true);
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
            ResultSet r = stmt.executeQuery("SELECT * FROM maquinas WHERE descripcion=\"" + Tabla.getValueAt(Tabla.getSelectedRow(), 1) + "\"")) {
            while (r.next()) {
                String h = r.getString("descripcion");
                mod = new PanelMaquinas(h);
                mod.setVisible(true);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Maquinas_Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        mostrarTabla();
    }//GEN-LAST:event_TablaMousePressed

    private void jRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem1ActionPerformed
        PanelMaquinas mod = new PanelMaquinas();
        mod.setVisible(true);
    }//GEN-LAST:event_jRadioButtonMenuItem1ActionPerformed

    private void jRadioButtonMenuItem_deshabilitadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem_deshabilitadasActionPerformed
        jRadioButtonMenuItem_Sala1A.setSelected(false);
        jRadioButtonMenuItem_Sala1B.setSelected(false);
        jRadioButtonMenuItem_Sala2.setSelected(false);
        jRadioButtonMenuItem_Sala3.setSelected(false);
        jRadioButtonMenuItem_Sala4.setSelected(false);
        jLabel_sala.setVisible(false);
        mostrarTabla();
    }//GEN-LAST:event_jRadioButtonMenuItem_deshabilitadasActionPerformed

    private void jRadioButtonMenuItem_Sala1AActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem_Sala1AActionPerformed
        jRadioButtonMenuItem_deshabilitadas.setSelected(false);
        if(jRadioButtonMenuItem_Sala1A.isSelected()){
            jRadioButtonMenuItem_Sala1B.setSelected(false);
            jRadioButtonMenuItem_Sala2.setSelected(false);
            jRadioButtonMenuItem_Sala3.setSelected(false);
            jRadioButtonMenuItem_Sala4.setSelected(false);
            jLabel_sala.setVisible(true);
            jLabel_sala.setText(jRadioButtonMenuItem_Sala1A.getText());
            mostrarTabla();
        }else{
            jLabel_sala.setVisible(false);
            mostrarTabla();
        }
    }//GEN-LAST:event_jRadioButtonMenuItem_Sala1AActionPerformed

    private void jRadioButtonMenuItem_Sala1BActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem_Sala1BActionPerformed
        jRadioButtonMenuItem_deshabilitadas.setSelected(false);
        if(jRadioButtonMenuItem_Sala1B.isSelected()){
            jRadioButtonMenuItem_Sala1A.setSelected(false);
            jRadioButtonMenuItem_Sala2.setSelected(false);
            jRadioButtonMenuItem_Sala3.setSelected(false);
            jRadioButtonMenuItem_Sala4.setSelected(false);
            jLabel_sala.setVisible(true);
            jLabel_sala.setText(jRadioButtonMenuItem_Sala1B.getText());
            mostrarTabla();
        }else{
            jLabel_sala.setVisible(false);
            mostrarTabla();
        }
    }//GEN-LAST:event_jRadioButtonMenuItem_Sala1BActionPerformed

    private void jRadioButtonMenuItem_Sala2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem_Sala2ActionPerformed
        jRadioButtonMenuItem_deshabilitadas.setSelected(false);
        if(jRadioButtonMenuItem_Sala2.isSelected()){
            jRadioButtonMenuItem_Sala1A.setSelected(false);
            jRadioButtonMenuItem_Sala1B.setSelected(false);
            jRadioButtonMenuItem_Sala3.setSelected(false);
            jRadioButtonMenuItem_Sala4.setSelected(false);
            jLabel_sala.setVisible(true);
            jLabel_sala.setText(jRadioButtonMenuItem_Sala2.getText());
            mostrarTabla();
        }else{
            jLabel_sala.setVisible(false);
            mostrarTabla();
        }
    }//GEN-LAST:event_jRadioButtonMenuItem_Sala2ActionPerformed

    private void jRadioButtonMenuItem_Sala3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem_Sala3ActionPerformed
        jRadioButtonMenuItem_deshabilitadas.setSelected(false);
        if(jRadioButtonMenuItem_Sala3.isSelected()){
            jRadioButtonMenuItem_Sala1A.setSelected(false);
            jRadioButtonMenuItem_Sala1B.setSelected(false);
            jRadioButtonMenuItem_Sala2.setSelected(false);
            jRadioButtonMenuItem_Sala4.setSelected(false);
            jLabel_sala.setVisible(true);
            jLabel_sala.setText(jRadioButtonMenuItem_Sala3.getText());
            mostrarTabla();
        }else{
            jLabel_sala.setVisible(false);
            mostrarTabla();
        }
    }//GEN-LAST:event_jRadioButtonMenuItem_Sala3ActionPerformed

    private void jRadioButtonMenuItem_Sala4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonMenuItem_Sala4ActionPerformed
        jRadioButtonMenuItem_deshabilitadas.setSelected(false);
        if(jRadioButtonMenuItem_Sala4.isSelected()){
            jRadioButtonMenuItem_Sala1A.setSelected(false);
            jRadioButtonMenuItem_Sala1B.setSelected(false);
            jRadioButtonMenuItem_Sala2.setSelected(false);
            jRadioButtonMenuItem_Sala3.setSelected(false);
            jLabel_sala.setVisible(true);
            jLabel_sala.setText(jRadioButtonMenuItem_Sala4.getText());
            mostrarTabla();
        }else{
            jLabel_sala.setVisible(false);
            mostrarTabla();
        }
    }//GEN-LAST:event_jRadioButtonMenuItem_Sala4ActionPerformed

    private void jLabel_tituloMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_tituloMousePressed
        jRadioButtonMenuItem_deshabilitadas.setSelected(false);
        jRadioButtonMenuItem_Sala1A.setSelected(false);
        jRadioButtonMenuItem_Sala1B.setSelected(false);
        jRadioButtonMenuItem_Sala2.setSelected(false);
        jRadioButtonMenuItem_Sala3.setSelected(false);
        jRadioButtonMenuItem_Sala4.setSelected(false);
        jLabel_sala.setVisible(false);
        mostrarTabla();
    }//GEN-LAST:event_jLabel_tituloMousePressed

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
            java.util.logging.Logger.getLogger(Maquinas_Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                SERVER_SOCKET = new ServerSocket(1332);
                new Maquinas_Principal().setVisible(true);
            } catch (SQLException | IOException ex) {
                Logger.getLogger(Maquinas_Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Boton_Añadir;
    public javax.swing.JPanel Panel1;
    private javax.swing.JTable Tabla;
    private javax.swing.JButton botonPrincipal;
    private javax.swing.JButton boton_documentos;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> jC_Empresa;
    private javax.swing.JComboBox<String> jC_Ubicacion;
    private javax.swing.JLabel jLabel_sala;
    private javax.swing.JLabel jLabel_titulo;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JRadioButton jR_Descripcion;
    private javax.swing.JRadioButton jR_Empresa;
    private javax.swing.JRadioButton jR_Fabricante;
    private javax.swing.JRadioButton jR_Ubicacion;
    private javax.swing.JRadioButton jR_numeroMaquina;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem_Sala1A;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem_Sala1B;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem_Sala2;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem_Sala3;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem_Sala4;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem_deshabilitadas;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField txt_descripcion;
    private javax.swing.JTextField txt_fabricante;
    private javax.swing.JTextField txt_numeroMaquina;
    // End of variables declaration//GEN-END:variables
}

