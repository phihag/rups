package com.itextpdf.rups.view.contextmenu;

import com.itextpdf.rups.view.itext.PdfTree;
import com.itextpdf.rups.view.itext.treenodes.PdfObjectTreeNode;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Custom action to save raw bytes of a stream to a file from the PdfTree view.
 *
 * @author Michael Demey
 */
public class SaveToFilePdfTreeAction extends AbstractRupsAction {

    private boolean saveRawBytes;

    public SaveToFilePdfTreeAction(String name) {
        super(name);
    }

    public SaveToFilePdfTreeAction(String name, Component invoker) {
        super(name, invoker);
    }

    public SaveToFilePdfTreeAction(String name, Component invoker, boolean raw) {
        super(name, invoker);
        saveRawBytes = raw;
    }

    public void actionPerformed(ActionEvent e) {

        // get saving location
        JFileChooser fileChooser = new JFileChooser();

        if (saveRawBytes) {
            fileChooser.setDialogTitle(fileChooser.getDialogTitle() + " raw bytes");
        }

        int choice = fileChooser.showSaveDialog(null);
        String path = null;

        if (choice == JFileChooser.APPROVE_OPTION) {
            path = fileChooser.getSelectedFile().getPath();

            // get the stream
            PdfTree tree = (PdfTree) invoker;
            TreeSelectionModel selectionModel = tree.getSelectionModel();
            TreePath[] paths = selectionModel.getSelectionPaths();
            PdfObjectTreeNode lastPath = (PdfObjectTreeNode) paths[0].getLastPathComponent();
            PdfObject object = lastPath.getPdfObject();
            PRStream stream = (PRStream) object;

            // get the bytes and write away
            try {
                byte[] array = null;

                if (saveRawBytes) {
                    array = PdfReader.getStreamBytesRaw(stream);
                } else {
                    array = PdfReader.getStreamBytes(stream);
                }

                FileOutputStream fos = new FileOutputStream(path);
                fos.write(array);
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace(); // TODO : Catch this exception properly
            }
        }
    }
}
