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

package com.itextpdf.rups.controller;

import com.itextpdf.rups.io.FileChooserAction;
import com.itextpdf.rups.io.FileCloseAction;
import com.itextpdf.rups.model.PdfFile;
import com.itextpdf.rups.view.Console;
import com.itextpdf.rups.view.PageSelectionListener;
import com.itextpdf.rups.view.RupsMenuBar;
import com.itextpdf.rups.view.contextmenu.ConsoleContextMenu;
import com.itextpdf.rups.view.contextmenu.ContextMenuMouseListener;
import com.itextpdf.rups.view.itext.treenodes.PdfObjectTreeNode;
import com.itextpdf.rups.view.itext.treenodes.PdfTrailerTreeNode;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfStamper;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.Observable;
import java.util.StringTokenizer;

/**
 * This class controls all the GUI components that are shown in
 * the RUPS application: the menu bar, the panels,...
 */
public class RupsController extends Observable
        implements TreeSelectionListener, PageSelectionListener {

    // member variables

	/* file and controller */
    /**
     * The Pdf file that is currently open in the application.
     */
    protected PdfFile pdfFile;
    protected StringBuilder rawText = new StringBuilder();
    /**
     * Object with the GUI components for iText.
     *
     * @since iText 5.0.0 (renamed from reader which was confusing because reader is normally used for a PdfReader instance)
     */
    protected PdfReaderController readerController;

	/* main components */
    /**
     * The JMenuBar for the RUPS application.
     */
    protected RupsMenuBar menuBar;
    /**
     * Contains all other components: the page panel, the outline tree, etc.
     */
    protected JSplitPane masterComponent;


    // constructor

    /**
     * Constructs the GUI components of the RUPS application.
     */
    public RupsController(Dimension dimension) {
        // creating components and controllers
        menuBar = new RupsMenuBar(this);
        addObserver(menuBar);
        Console console = Console.getInstance();
        addObserver(console);
        readerController = new PdfReaderController(this, this);
        addObserver(readerController);

        // creating the master component
        masterComponent = new JSplitPane();
        masterComponent.setOrientation(JSplitPane.VERTICAL_SPLIT);
        masterComponent.setDividerLocation((int) ( dimension.getHeight() * .70 ));
        masterComponent.setDividerSize(2);
        masterComponent.setDropTarget(new DropTarget() {

            // drag and drop for opening files
            public synchronized void drop(DropTargetDropEvent dtde) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable t = dtde.getTransferable();
                java.util.List<File> files = null;

                try {
                    if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        files = (java.util.List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    }
                    if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) { // fix for Linux

                        String urls = (String) t.getTransferData(DataFlavor.stringFlavor);
                        files = new LinkedList<File>();
                        StringTokenizer tokens = new StringTokenizer(urls);
                        while (tokens.hasMoreTokens()) {
                            String urlString = tokens.nextToken();
                            URL url = new URL(urlString);
                            files.add(new File(URLDecoder.decode(url.getFile(), "UTF-8")));
                        }
                    }

                    if (files == null || files.size() != 1) {
                        JOptionPane.showMessageDialog(masterComponent, "You can only open one file!", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        loadFile(files.get(0));
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(masterComponent, "Error opening file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

                dtde.dropComplete(true);
            }
        });

        JSplitPane content = new JSplitPane();
        masterComponent.add(content, JSplitPane.TOP);
        JSplitPane info = new JSplitPane();
        masterComponent.add(info, JSplitPane.BOTTOM);

        content.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        content.setDividerLocation((int) ( dimension.getWidth() * .6 ));
        content.setDividerSize(1);
        content.add(new JScrollPane(readerController.getPdfTree()), JSplitPane.LEFT);
        content.add(readerController.getNavigationTabs(), JSplitPane.RIGHT);

        info.setDividerLocation((int) ( dimension.getWidth() * .3 ));
        info.setDividerSize(1);
        info.add(readerController.getObjectPanel(), JSplitPane.LEFT);
        JTabbedPane editorPane = readerController.getEditorTabs();
        JScrollPane cons = new JScrollPane(console.getTextArea());
        console.getTextArea().addMouseListener(new ContextMenuMouseListener(ConsoleContextMenu.getPopupMenu(console.getTextArea()), cons));
        editorPane.addTab("Console", null, cons, "Console window (System.out/System.err)");
        editorPane.setSelectedComponent(cons);
        info.add(editorPane, JSplitPane.RIGHT);
    }

    /**
     *
     */
    public RupsController(Dimension dimension, File f) {
        this(dimension);
        loadFile(f);
    }

    /**
     * Getter for the menubar.
     */
    public RupsMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Getter for the master component.
     */
    public Component getMasterComponent() {
        return masterComponent;
    }

    // Observable

    /**
     * @see java.util.Observable#notifyObservers(java.lang.Object)
     */
    @Override
    public void notifyObservers(Object obj) {
        if (obj instanceof FileChooserAction) {
            File file = ( (FileChooserAction) obj ).getFile();

            /* save check */
            if (( (FileChooserAction) obj ).isNewFile()) {
                saveFile(file);
            } else {
                loadFile(file);
            }
            return;
        }
        if (obj instanceof FileCloseAction) {
            pdfFile = null;
            setChanged();
            super.notifyObservers(RupsMenuBar.CLOSE);
            return;
        }
    }

    /**
     * @param file the file to load
     */
    public void loadFile(File file) {
        try {
            byte[] contents = readFileToByteArray(file);

            pdfFile = new PdfFile(contents);
            pdfFile.setDirectory(file.getParentFile());
            pdfFile.setFilename(file.getName());

            setChanged();
            super.notifyObservers(RupsMenuBar.OPEN);
            readerController.startObjectLoader(pdfFile);
            readerController.addNonObserverTabs(pdfFile);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(masterComponent, ioe.getMessage(), "Dialog", JOptionPane.ERROR_MESSAGE);
        } catch (DocumentException de) {
            JOptionPane.showMessageDialog(masterComponent, de.getMessage(), "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Reads a File to a byte[]
     *
     * @param file java.io.File
     * @return the file as a byte array
     * @throws IOException
     */
    private byte[] readFileToByteArray(File file) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = null;
        InputStream inputStream = null;
        try {
            byte[] buffer = new byte[4096];
            byteArrayOutputStream = new ByteArrayOutputStream();
            inputStream = new FileInputStream(file);
            int read = 0;
            while (( read = inputStream.read(buffer) ) != - 1) {
                byteArrayOutputStream.write(buffer, 0, read);
            }
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                // log to console
                e.printStackTrace();
            }

            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // log to console
                e.printStackTrace();
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Saves the pdf to the disk
     *
     * @param file java.io.File file to save
     */
    public void saveFile(File file) {
        try {
            if (file.exists()) {
                int choice = JOptionPane.showConfirmDialog(null, "File already exists, would you like to overwrite file?", "Warning", JOptionPane.YES_NO_CANCEL_OPTION);
                if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }

            if (! file.getName().endsWith(".pdf")) {
                file = new File(file.getPath() + ".pdf");
            }
            pdfFile.getPdfReader().removeUnusedObjects();
            PdfStamper stamper = new PdfStamper(pdfFile.getPdfReader(), new FileOutputStream(file));
            stamper.close();
            JOptionPane.showMessageDialog(masterComponent, "File saved.", "Dialog", JOptionPane.INFORMATION_MESSAGE);

            loadFile(file);
        } catch (DocumentException de) {
            JOptionPane.showMessageDialog(masterComponent, de.getMessage(), "Dialog", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(masterComponent, ioe.getMessage(), "Dialog", JOptionPane.ERROR_MESSAGE);
        }
    }

    // tree selection

    /**
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent evt) {
        Object selectednode = readerController.getPdfTree().getLastSelectedPathComponent();
        if (selectednode instanceof PdfTrailerTreeNode) {
            menuBar.update(this, RupsMenuBar.FILE_MENU);
            return;
        }
        if (selectednode instanceof PdfObjectTreeNode) {
            readerController.update(this, selectednode);
        }
    }

    // page navigation

    /**
     * @see com.itextpdf.rups.view.PageSelectionListener#gotoPage(int)
     */
    public int gotoPage(int pageNumber) {
        readerController.gotoPage(pageNumber);
        return pageNumber;
    }

    /**
     * Getter for the pdfFile
     *
     * @return pdfFile
     */
    public PdfFile getPdfFile() {
        return pdfFile;
    }
}
