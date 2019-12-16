/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tareasIlorcitana;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Renderizado
 */
public class Render extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
        
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof String) {
            String valor = (String) value;
            switch (valor) {
                case "urgente":
                    cell.setBackground(Color.RED);
                    break;
                case "en proceso":
                    cell.setBackground(Color.green);
                    break;
                case "finalizado":
                    cell.setBackground(Color.GREEN);
                    break;
                case "prioritaria":
                    cell.setBackground(Color.YELLOW);
                    break;
                case "normal":
                    cell.setBackground(Color.LIGHT_GRAY);
                    break;
                default:
                    cell.setBackground(Color.white);
                    break;
            }
        }

        return cell;
    }
}
