/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soporteMaquinas;

import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
/**
 * @author Miguel Ángel Carrillo García
 */
public class CeldaRenderer extends DefaultTableCellRenderer {

    private int col = -1;
    
    public CeldaRenderer(int col) {
        this.col = col;
    }
      
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        //retorna un combo con el valor seleccionado
        if (col == column) {//la columna que tiene el JComboBox
            JComboBox comboBox = new JComboBox();            
            comboBox.addItem(value);                        
            return comboBox;            
        }

        return cellComponent;
    }
}
