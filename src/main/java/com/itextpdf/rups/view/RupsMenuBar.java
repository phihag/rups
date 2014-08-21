/*
 * $Id$
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

package com.itextpdf.rups.view;

import com.itextpdf.rups.controller.RupsController;
import com.itextpdf.rups.io.FileChooserAction;
import com.itextpdf.rups.io.FileCloseAction;
import com.itextpdf.rups.io.filters.PdfFilter;
import com.itextpdf.rups.model.PdfFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class RupsMenuBar extends JMenuBar implements Observer {

	/** Caption for the file menu. */
	public static final String FILE_MENU = "File";
	/** Caption for "Open file". */
	public static final String OPEN = "Open";
    /** Caption for "Open in PDF Viewer". */
    public static final String OPENINVIEWER = "Open in PDF Viewer";
    /** Caption for "Close file". */
	public static final String CLOSE = "Close";
    /** Caption for "Save as..." */
    public static final String SAVE_AS = "Save as...";
	/** Caption for the help menu. */
	public static final String HELP_MENU = "Help";
	/** Caption for "Help about". */
	public static final String ABOUT = "About";
	/**
	 * Caption for "Help versions".
	 * @since iText 5.0.0 (renamed from VERSIONS)
	 */
	public static final String VERSION = "Version";
	
	/** The Observable object. */
	protected Observable observable;
	/** The action needed to open a file. */
	protected FileChooserAction fileChooserAction;
	/** The action needed to save a file. */
	protected FileChooserAction fileSaverAction;
	/** The HashMap with all the actions. */
	protected HashMap<String, JMenuItem> items;
	
	/**
	 * Creates a JMenuBar.
	 * @param observable	the controller to which this menu bar is added
	 */
	public RupsMenuBar(final Observable observable) {
		this.observable = observable;
		items = new HashMap<String, JMenuItem>();
		fileChooserAction = new FileChooserAction(observable, "Open", PdfFilter.INSTANCE, false);
		fileSaverAction = new FileChooserAction(observable, "Save As...", PdfFilter.INSTANCE, true);
		MessageAction message = new MessageAction();
		JMenu file = new JMenu(FILE_MENU);
		addItem(file, OPEN, fileChooserAction, KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
		addItem(file, CLOSE, new FileCloseAction(observable), KeyStroke.getKeyStroke('W', KeyEvent.CTRL_DOWN_MASK));
        addItem(file, SAVE_AS, fileSaverAction, KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
        file.addSeparator();
        JMenu recentFilesSubMenu = new JMenu("Open in PDF Viewer Application");
        addItem(file, OPENINVIEWER, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        PdfFile pdfFile = ((RupsController)observable).getPdfFile();
                        if ( pdfFile != null ) {
                            if ( pdfFile.getDirectory() != null ) {
                                File myFile = new File(pdfFile.getDirectory(), pdfFile.getFilename());
                                Desktop.getDesktop().open(myFile);
                            }
                        }
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
            }
        }, KeyStroke.getKeyStroke('O', KeyEvent.ALT_DOWN_MASK));
        add(file);
        add(Box.createGlue());
        JMenu help = new JMenu(HELP_MENU);
        addItem(help, ABOUT, message);
        addItem(help, VERSION, message);
        add(help);
		enableItems(false);
	}
	
	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable observable, Object obj) {
		if (OPEN.equals(obj)) {
			enableItems(true);
			return;
		}
		if (CLOSE.equals(obj)) {
			enableItems(false);
			return;
		}
		if (FILE_MENU.equals(obj)) {
			fileChooserAction.actionPerformed(null);
		}
	}
	
	/**
	 * Create an item with a certain caption and a certain action,
	 * then add the item to a menu.
	 * @param menu	the menu to which the item has to be added
	 * @param caption	the caption of the item
	 * @param action	the action corresponding with the caption
	 */
	protected void addItem(JMenu menu, String caption, ActionListener action) {
        addItem(menu, caption, action, null);
	}

    protected void addItem(JMenu menu, String caption, ActionListener action, KeyStroke keyStroke) {
        JMenuItem item = new JMenuItem(caption);
        item.addActionListener(action);
        if ( keyStroke != null ) {
            item.setAccelerator(keyStroke);
        }
        menu.add(item);
        items.put(caption, item);
    }
	
	/**
	 * Enables/Disables a series of menu items.
	 * @param enabled	true for enabling; false for disabling
	 */
	protected void enableItems(boolean enabled) {
		enableItem(CLOSE, enabled);
	}
	
	/**
	 * Enables/disables a specific menu item
	 * @param caption	the caption of the item that needs to be enabled/disabled
	 * @param enabled	true for enabling; false for disabling
	 */
	protected void enableItem(String caption, boolean enabled) {
		items.get(caption).setEnabled(enabled);
	}
	
	/** A Serial Version UID. */
	private static final long serialVersionUID = 6403040037592308742L;
}