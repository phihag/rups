package com.itextpdf.rups.view.contextmenu;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * TODO: Write Documentation
 *
 * @author Michael Demey
 */
public class ContextMenuMouseListener extends MouseAdapter {

    private JPopupMenu popup;
    private JComponent component;

    public ContextMenuMouseListener(JPopupMenu popup, JComponent component) {
        this.popup = popup;
        this.component = component;
    }

    private void showMenuIfPopupTrigger(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.show(component, e.getX() + 3, e.getY() + 3);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("hello");
        showMenuIfPopupTrigger(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        System.out.println("hello");
        showMenuIfPopupTrigger(e);
    }

}
