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
