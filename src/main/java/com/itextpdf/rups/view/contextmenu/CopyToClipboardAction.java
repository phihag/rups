package com.itextpdf.rups.view.contextmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Custom action to copy selected text to the system clipboard.
 *
 * @author Michael Demey
 */

public class CopyToClipboardAction extends AbstractRupsAction {

	public CopyToClipboardAction(String name) {
        super(name);
    }

    public CopyToClipboardAction(String name, Component invoker) {
        super(name, invoker);
    }

    public void actionPerformed(ActionEvent e) {
        boolean nothingSelected = false;
        JTextPane textPane = (JTextPane) invoker;

        if ( textPane.getSelectedText() == null || textPane.getSelectedText().trim().length() == 0 ) {
            nothingSelected = true;
            textPane.selectAll();
        }

        textPane.copy();

        if ( nothingSelected ) {
            textPane.select(0, 0);
        }
    }

    /** A serial version UID */
	private static final long serialVersionUID = 1301101461853323920L;
}
