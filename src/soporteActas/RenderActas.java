/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soporteActas;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Renderizado
 */
public class RenderActas extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
        
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        switch (column) {
            case 6:
                cell.setBackground(new Color(255,204,204));
                //cell.setForeground(Color.black);
                cell.setFont(new Font("Century Gothic", Font.PLAIN, 12));
                break;
            case 1:
                cell.setBackground(new Color(204,255,204));
                //cell.setForeground(Color.black);
                cell.setFont(new Font("Century Gothic", Font.PLAIN, 12));
                break;
           
            case 3:
                cell.setBackground(new Color(255,255,204));
                //cell.setForeground(Color.black);
                cell.setFont(new Font("Century Gothic", Font.PLAIN, 12));
                break;
            default:
                cell.setBackground(Color.white);
                //cell.setForeground(Color.black);
                cell.setFont(new Font("Century Gothic", Font.PLAIN, 12));
                break;
        }

        return cell;
    }
}
