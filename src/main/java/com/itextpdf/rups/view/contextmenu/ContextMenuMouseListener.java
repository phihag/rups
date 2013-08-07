package com.itextpdf.rups.view.contextmenu;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Generic mouse listener that checks if a menu popup should be shown or not.
 * A hook has been provided for your convenience: showPopupHook()
 *
 * @author Michael Demey
 */
public class ContextMenuMouseListener extends MouseAdapter {

    private JPopupMenu popup;
    protected JComponent component;

    public ContextMenuMouseListener(JPopupMenu popup, JComponent component) {
        this.popup = popup;
        this.component = component;
    }

    private void showMenuIfPopupTrigger(MouseEvent e) {
        if (e.isPopupTrigger() && showPopupHook() ) {
            popup.show(component, e.getX() + 3, e.getY() + 3);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        showMenuIfPopupTrigger(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        showMenuIfPopupTrigger(e);
    }

    public boolean showPopupHook() {
        return true;
    }
}
