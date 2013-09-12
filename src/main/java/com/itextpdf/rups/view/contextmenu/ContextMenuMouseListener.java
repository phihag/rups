/*
 * $Id: Console.java 34 2012-12-26 12:08:37Z blowagie $
 *
 * Copyright 2007 Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
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
