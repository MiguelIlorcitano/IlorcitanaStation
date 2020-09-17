/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soporteMaquinas;

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
public class RenderM extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
        
        JLabel cell = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        switch (column) {
            case 1:
                cell.setBackground(new Color(204,204,255));
                //cell.setForeground(Color.WHITE);
                cell.setFont(new Font("3ds Light", Font.BOLD, 14));
                break;
            case 2:
                cell.setBackground(new Color(255,204,204));
                //cell.setForeground(Color.black);
                cell.setFont(new Font("3ds Light", Font.PLAIN, 14));
                break;
            case 6:
                cell.setBackground(new Color(204,255,204));
                //cell.setForeground(Color.black);
                cell.setFont(new Font("3ds Light", Font.PLAIN, 14));
                break;
            case 7:
                cell.setBackground(new Color(204,255,255));
                //cell.setForeground(Color.black);
                cell.setFont(new Font("3ds Light", Font.PLAIN, 14));
                break;
            case 8:
                cell.setBackground(new Color(255,255,204));
                //cell.setForeground(Color.black);
                cell.setFont(new Font("3ds Light", Font.PLAIN, 14));
                break;
            case 9:
                cell.setBackground(new Color(204,204,204));
                //cell.setForeground(Color.black);
                cell.setFont(new Font("3ds Light", Font.PLAIN, 14));
                break;
            default:
                cell.setBackground(Color.white);
                //cell.setForeground(Color.black);
                cell.setFont(new Font("3ds Light", Font.PLAIN, 14));
                break;
        }
        
//        if (value instanceof String) {
//            String valor = (String) value;
//            switch (valor) {
//                case "urgente":
//                    cell.setBackground(Color.RED);
//                    break;
//                case "en proceso":
//                    cell.setBackground(Color.green);
//                    break;
//                case "finalizado":
//                    cell.setBackground(Color.RED);
//                    break;
//                case "prioritaria":
//                    cell.setBackground(Color.YELLOW);
//                    break;
//                case "normal":
//                    cell.setBackground(Color.LIGHT_GRAY);
//                    break;
//                default:
//                    cell.setBackground(Color.white);
//                    break;
//            }
//        }

        return cell;
    }
}
