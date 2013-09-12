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
package com.itextpdf.rups.view.contextmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Custom action to save raw bytes of a stream to a file from the stream panel.
 * This allows selections to be saved as well.
 *
 * @author Michael Demey
 */
public class SaveToFileJTextPaneAction extends AbstractRupsAction {

    /** Serial version uid. */
	private static final long serialVersionUID = -5984892284970574660L;

	public SaveToFileJTextPaneAction(String name) {
        super(name);
    }

    public SaveToFileJTextPaneAction(String name, Component invoker) {
        super(name, invoker);
    }

    public void actionPerformed(ActionEvent e) {

        // get saving location
        JFileChooser fileChooser = new JFileChooser();

        int choice = fileChooser.showSaveDialog(null);
        String path = null;

        if (choice == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().getPath();


            boolean nothingSelected = false;
            JTextPane textPane = (JTextPane) invoker;

            if (textPane.getSelectedText() == null || textPane.getSelectedText().trim().length() == 0) {
                nothingSelected = true;
                textPane.selectAll();
            }

            BufferedWriter writer = null;

            try {
                writer = new BufferedWriter(new FileWriter(path));
                writer.write(textPane.getSelectedText());

            } catch (IOException e1) {
                e1.printStackTrace(); // TODO
            } finally {
                try {
                    if (writer != null)
                        writer.close();
                } catch (IOException e2) {
                    e2.printStackTrace(); // TODO
                }
            }

            if (nothingSelected) {
                textPane.select(0, 0);
            }
        }
    }
}