/*
 * $Id$
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
import java.awt.*;

/**
 * Adds copy and clear functionality to the console screen.
 */
public class ConsoleContextMenu {

    private static Component invoker;

    public static JPopupMenu getPopupMenu(final Component component) {
        invoker = component;
        JPopupMenu popup = new JPopupMenu();
        JMenuItem copyToClipboard = new JMenuItem();
        copyToClipboard.setText("Copy to Clipboard");
        copyToClipboard.setAction(new CopyToClipboardAction("Copy to Clipboard", invoker));
        popup.add(copyToClipboard);
        
        JMenuItem clear = new JMenuItem();
        clear.setText("Clear");
        clear.setAction(new ClearConsoleAction("Clear", invoker));
        popup.add(clear);
        return popup;
    }
}
