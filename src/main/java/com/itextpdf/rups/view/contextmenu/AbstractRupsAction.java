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
 * Abstract class to provide uniform constructors for Actions.
 *
 * @author Michael Demey
 */

public abstract class AbstractRupsAction extends AbstractAction {

    /** Serial version uid. */
	private static final long serialVersionUID = -3581333233604596899L;
	
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
