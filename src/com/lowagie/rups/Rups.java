/*
 * $Id: PdfDocument.java 2884 2007-08-15 09:28:41Z blowagie $
 * Copyright (c) 2007 Bruno Lowagie
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.lowagie.rups;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.lowagie.rups.components.FormTree;
import com.lowagie.rups.components.OutlineTree;
import com.lowagie.rups.components.PagesTable;
import com.lowagie.rups.components.InfoPanel;
import com.lowagie.rups.components.PdfTree;
import com.lowagie.rups.components.XRefTable;
import com.lowagie.rups.factories.IndirectObjectFactory;
import com.lowagie.rups.factories.PdfWorker;
import com.lowagie.rups.factories.TreeNodeFactory;
import com.lowagie.rups.nodetypes.PdfObjectTreeNode;
import com.lowagie.rups.nodetypes.PdfTrailerTreeNode;
import com.lowagie.swing.files.BrowseResult;
import com.lowagie.swing.files.FileChooserAction;
import com.lowagie.swing.files.OutputStreamResource;
import com.lowagie.swing.files.SaveAction;
import com.lowagie.swing.files.filters.PdfFilter;
import com.lowagie.swing.helpers.Utilities;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPageLabels;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.RandomAccessFileOrArray;

/**
 * An application that allows you to inspect the syntax of a PDF file.
 */
public class Rups extends JFrame implements BrowseResult, OutputStreamResource, TreeSelectionListener {
	
	/**	a serial version id */
	private static final long serialVersionUID = 4501670592786972072L;

	// PDF
	
	/** The reader object for this PDF file. */
	protected PdfReader reader = null;
	
	// menu
	
	/** The action to open a file chooser. */
	protected FileChooserAction fileChooserAction;
	/** The save button in the menu bar will be enabled and disabled. */
	protected JMenuItem save;
	
	// left pane

	/** The table that will show info about the pages. */
	protected PagesTable pages = new PagesTable();
	/** The outlines tree. */
	protected OutlineTree outlines = new OutlineTree();
	/** The form tree. */
	protected FormTree form = new FormTree();
	/** The table that will show info about the PDFs Crossreference table. */
	protected XRefTable xref = new XRefTable();
	
	// right pane
	
	/** The tree that will reveal the internal PDF structure.  */
	protected PdfTree pdf = new PdfTree();
	/** The factory that generates the tree nodes. */
	protected TreeNodeFactory nodes;
	
	// bottom pane
	
	/** The panel that will contain info about a PDF object (card layout). */
	protected InfoPanel info = new InfoPanel();
	
	// main method
	
	/**
	 * Main method of this application.
	 * @param args	no arguments needed
	 */
	public static void main(String[] args) {
		new Rups();
	}
	
	// layout
	
	/**
	 * Creates the JFrame.
	 */
	public Rups() {
        super();
		fileChooserAction = new FileChooserAction(this, "Open", PdfFilter.INSTANCE, false);
		pdf.addTreeSelectionListener(this);
        initialize();
		setVisible(true);
	}
	
    /**
     * Initializes the main components of the Rups application.
     */
    private void initialize() {

        // size and location
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(screen.getWidth() * .75), (int)(screen.getHeight() * .75));
        setLocation((int)(screen.getWidth() * .125), (int)(screen.getHeight() * .125));
        setResizable(true);
        
        // Frame title and menu
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("RUPS: Read/Update PDF Syntax");
        setJMenuBar(getMenu());
        
        // overall lay-out
		this.getContentPane().setLayout(new BorderLayout());
		JSplitPane main_splitpane = new JSplitPane();
		main_splitpane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		main_splitpane.setDividerLocation((int)(screen.getHeight() * .5));
		this.getContentPane().add(main_splitpane, java.awt.BorderLayout.CENTER);
		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BorderLayout());
		main_splitpane.add(top_panel, JSplitPane.TOP);
		JSplitPane top_splitpane = new JSplitPane();
		top_splitpane.setDividerSize(3);
		top_splitpane.setDividerLocation((int)(screen.getWidth() * .2));
		top_panel.add(top_splitpane, BorderLayout.CENTER);

		// left pane
		JTabbedPane tabs = new JTabbedPane();
		top_splitpane.add(tabs, JSplitPane.TOP);
		tabs.addTab("Pages", null, Utilities.getScrollPane(pages), "Pages");
		tabs.addTab("Outlines", null, Utilities.getScrollPane(outlines), "Outlines (Bookmarks)");
		tabs.addTab("Form", null, Utilities.getScrollPane(form), "Interactive Form");
		tabs.addTab("XRef", null, Utilities.getScrollPane(xref), "Cross-reference table");
		
		// right pane
		top_splitpane.add(Utilities.getScrollPane(pdf), JSplitPane.BOTTOM);
		
		// bottom pane
		main_splitpane.add(info, JSplitPane.BOTTOM);
	}
	
    /**
	 * Creates a menu bar.
	 * @return the menu bar for the application.
	 */
	private JMenuBar getMenu() {
		JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem open = new JMenuItem("Open");
        open.setAction(fileChooserAction);
        file.add(open);
        save = new JMenuItem("Save As");
        save.setAction(new SaveAction(this, PdfFilter.INSTANCE));
        save.setEnabled(false);
        file.add(save);
        bar.add(file);
		return bar;
	}

	/**
	 * @see com.lowagie.swing.files.BrowseResult#setFile(java.io.File)
	 */
	public void setFile(File file) {
		// left pane
		pages.loadPages(null, null);
		outlines.loadOutlines(null, null);
		form.loadFields(null, null);
		xref.setObjects(null, null);
		// right pane
		pdf.resetRoot(file);
		// bottom pane
		info.clear();
        save.setEnabled(false);
		// new reader and go for it!
		try {
			reader = new PdfReader(new RandomAccessFileOrArray(file.getAbsolutePath()), null);
			if (PdfWorker.loadPdf(this, reader)) {
				pdf.repaint();
		        save.setEnabled(true);
			}
			else {
				JOptionPane.showMessageDialog(this, "Currently loading another PDF document.\nYou can only load one PDF at a time.", "Dialog", JOptionPane.WARNING_MESSAGE);
			}
		} catch (IOException e) {
			reader = null;
	        save.setEnabled(false);
			JOptionPane.showMessageDialog(this, e.getMessage(), "Dialog", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Writes the PDF file that is being edited in this application to
	 * an OutputStream.
	 * @see com.lowagie.swing.files.OutputStreamResource#writeTo(java.io.OutputStream)
	 */
	public void writeTo(OutputStream os) throws IOException {
		try {
			PdfStamper stamper = new PdfStamper(reader, os);
			stamper.close();
		} catch (DocumentException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Dialog", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Updates the components of the Rups application
	 * because a new PDF document was loaded into an
	 * indirect object factory.
	 * @param	objects	the indirect object factory
	 */
	public void update(IndirectObjectFactory objects) {
		// right pane
		nodes = new TreeNodeFactory(objects);
		pdf.resetRoot(nodes, reader.getTrailer());
		// left pane
		pages.loadPages(pdf, PdfPageLabels.getPageLabels(reader));
		outlines.loadOutlines(nodes, pdf);
		form.loadFields(nodes, pdf);
		xref.setObjects(objects, info);
	}
	
	/**
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		Object selectednode = pdf.getLastSelectedPathComponent();
		if (selectednode instanceof PdfTrailerTreeNode) {
			fileChooserAction.actionPerformed(null);
		}
		else if (selectednode instanceof PdfObjectTreeNode) {
			PdfObjectTreeNode node = (PdfObjectTreeNode)selectednode;
			nodes.expandNode(node);
			if (node.isRecursive()) {
				pdf.selectNode(node.getAncestor());
			}
			else if (node.isIndirect()) {
				xref.selectRowByReference(node.getNumber());
			}
			else {
				info.render(node.getPdfObject());
			}
		}
	}
}