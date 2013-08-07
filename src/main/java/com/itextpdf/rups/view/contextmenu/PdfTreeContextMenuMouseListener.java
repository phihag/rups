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
