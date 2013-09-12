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
import javax.swing.text.DefaultEditorKit;

/**
 * Convenience class/factory to get a context menu for a text pane. This context menu contains two actions as of yet:
 * - copy
 * - select all
 *
 * @author Michael Demey
 */
public class StreamPanelContextMenu {

    final static String COPY = "Copy";
    final static String SELECTALL = "Select All";

    /**
     * Creates a context menu (right click menu) with two actions:
     * - copy
     * - select all
     *
     * Copy copies the selected text or when no text is selected, it copies the entire text.
     *
     * @param textPane
     * @return
     */
    public static JPopupMenu getContextMenu(final JTextPane textPane) {
        final JPopupMenu menu = new JPopupMenu();
        final JMenuItem copyItem = new JMenuItem();
        copyItem.setAction(new CopyToClipboardAction(COPY, textPane));
        copyItem.setText(COPY);

        final JMenuItem selectAllItem = new JMenuItem(SELECTALL);
        selectAllItem.setAction(textPane.getActionMap().get(DefaultEditorKit.selectAllAction));
        selectAllItem.setText(SELECTALL);

        JMenuItem saveToFile = new JMenuItem();
        saveToFile.setText("Save to File");
        saveToFile.setAction(new SaveToFileJTextPaneAction("Save to File", textPane));

        menu.add(copyItem);
        menu.add(saveToFile);
        menu.add(new JSeparator());
        menu.add(selectAllItem);

        return menu;
    }
}
