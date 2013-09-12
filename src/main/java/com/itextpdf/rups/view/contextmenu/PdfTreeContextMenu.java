/*
 * $Id:  $
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
 * Convenience class/factory that return the popup menu for the PdfTree panel.
 *
 * @author Michael Demey
 */

public class PdfTreeContextMenu {

    public static JPopupMenu getPopupMenu(final Component component) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem saveRawToFile = new JMenuItem();
        saveRawToFile.setText("Save Raw Bytes to File");
        saveRawToFile.setAction(new SaveToFilePdfTreeAction("Save Raw Bytes to File", component, true));
        popup.add(saveRawToFile);

        JMenuItem saveToFile = new JMenuItem();
        saveToFile.setText("Save to File");
        saveToFile.setAction(new SaveToFilePdfTreeAction("Save to File", component, false));
        popup.add(saveToFile);
        return popup;
    }
}
