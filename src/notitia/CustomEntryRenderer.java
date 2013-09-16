/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package notitia;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Juan
 */
public class CustomEntryRenderer extends JLabel implements ListCellRenderer {

    public CustomEntryRenderer() {
        setOpaque(true);
        setFont(new Font("Serif", Font.PLAIN, 15));
        unselected = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(EtchedBorder.RAISED), "");
        selected = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(EtchedBorder.LOWERED), "");
        normal = new Color(230, 230, 255);
        focus = new Color(230, 240, 230);
        unselected.setTitleFont(new Font("Serif", Font.BOLD, 16));
        selected.setTitleFont(new Font("Serif", Font.BOLD, 16));
    }

    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        /*
        String[] split = value.toString().split("#7gg7#");
        setText(split[1]);
        selected.setTitle(split[0]);         
        unselected.setTitle(split[0]);
         */
        
        setText(((Entry)value).format(list.getParent().getWidth()));
        selected.setTitle(((Entry)value).getTitle());
        unselected.setTitle(((Entry)value).getTitle());

        setBorder(unselected);
        
        if (isSelected) {
            //setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            setBackground(focus);
            setBorder(selected);
        } else {
            //setBackground(list.getBackground());
            setForeground(list.getForeground());
            setBackground(normal);
            setBorder(unselected);
        }
        return this;
    }
    TitledBorder unselected, selected;
    Color normal, focus;
}
