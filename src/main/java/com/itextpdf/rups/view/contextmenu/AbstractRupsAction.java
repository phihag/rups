package com.itextpdf.rups.view.contextmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Abstract class to provide uniform constructors for Actions.
 *
 * @author Michael Demey
 */

public abstract class AbstractRupsAction extends AbstractAction {

    protected Component invoker;

    public AbstractRupsAction(String name) {
        super(name);
    }
    
    public AbstractRupsAction(String name, Component invoker) {
        super(name);
        this.invoker = invoker;
    }
    
    public abstract void actionPerformed(ActionEvent e);
}
