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
import java.awt.event.ActionEvent;

/**
 * Clears the console screen.
 *
 * @author Michael Demey
 */
public class ClearConsoleAction extends AbstractRupsAction {

   /** Serial version uid. */
	private static final long serialVersionUID = -7021881171745264584L;

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
