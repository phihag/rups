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

import com.itextpdf.rups.view.itext.PdfTree;
import com.itextpdf.rups.view.itext.treenodes.PdfObjectTreeNode;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * Additional check to see whether the selected node is a Stream (and not an image stream)
 *
 * @author Michael Demey
 */

public class PdfTreeContextMenuMouseListener extends ContextMenuMouseListener {

    public PdfTreeContextMenuMouseListener(JPopupMenu popup, JComponent component) {
        super(popup, component);
    }

    @Override
    public boolean showPopupHook() {
        TreeSelectionModel selectionModel = ((PdfTree)component).getSelectionModel();
        TreePath[] paths = selectionModel.getSelectionPaths();

        if ( paths.length < 1 ) {
            return false;
        }

        PdfObjectTreeNode lastPath = ((PdfObjectTreeNode) paths[0].getLastPathComponent());

        if ( !lastPath.isStream() ) {
            return false;
        }

        PRStream stream = (PRStream) lastPath.getPdfObject();
        return stream.get(PdfName.SUBTYPE) != PdfName.IMAGE;
    }
}
