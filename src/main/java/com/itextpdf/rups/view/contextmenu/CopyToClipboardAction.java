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
