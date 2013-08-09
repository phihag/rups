package com.itextpdf.rups.view.contextmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Clears the console screen.
 *
 * @author Michael Demey
 */
public class ClearConsoleAction extends AbstractRupsAction {

    public ClearConsoleAction(String name) {
        super(name);
    }

    public ClearConsoleAction(String name, Component invoker) {
        super(name, invoker);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ((JTextPane)this.invoker).setText("");
    }
}
