package com.itextpdf.rups.view.contextmenu;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;

/**
 * Subclass of DefaultEditorKit.CopyAction. actionPerformed is overridden to add selection logic:
 * If no text is selected, then the entire text is copied to the clipboard.
 */
public class ContextMenuCopyAction extends DefaultEditorKit.CopyAction {

    private JTextPane textPane;

    public ContextMenuCopyAction(JTextPane textPane) {
        this.textPane = textPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean nothingSelected = false;

        if ( textPane.getSelectedText() == null || textPane.getSelectedText().isEmpty() ) {
            nothingSelected = true;
            textPane.selectAll();
        }

        textPane.copy();

        if ( nothingSelected ) {
            textPane.select(0, 0);
        }
    }
}
