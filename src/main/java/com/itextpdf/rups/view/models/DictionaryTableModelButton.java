package com.itextpdf.rups.view.models;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Michael Demey
 */

public class DictionaryTableModelButton extends JButton implements TableCellRenderer {

    public DictionaryTableModelButton(String text, final JTable table) {
        super(text);
        setOpaque(true);
        setEnabled(true);
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("hello!");
            }
        });
    }

    private Icon deleteIcon;
    private Icon addIcon;

    public DictionaryTableModelButton(Icon deleteIcon, Icon addIcon) {
        this.deleteIcon = deleteIcon;
        this.addIcon = addIcon;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);

        if (table.getRowCount() - 1 == row) {
            label.setIcon(addIcon);
        } else {
            label.setIcon(deleteIcon);
        }

        return label;
    }


}