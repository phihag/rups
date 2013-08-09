package com.itextpdf.rups.view.contextmenu;

import javax.swing.*;
import java.awt.*;

/**
 * Convenience class/factory that return the popup menu for the PdfTree panel.
 *
 * @author Michael Demey
 */

public class PdfTreeContextMenu {

    private static Component invoker;

    public static JPopupMenu getPopupMenu(final Component component) {
        invoker = component;
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
