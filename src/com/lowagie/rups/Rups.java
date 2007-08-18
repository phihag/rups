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

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.lowagie.rups.factories.IndirectObjectStore;
import com.lowagie.rups.factories.PdfWorker;
import com.lowagie.rups.factories.TreeNodeFactory;
import com.lowagie.rups.helpers.OutlineTree;
import com.lowagie.rups.helpers.PagesTable;
import com.lowagie.rups.helpers.PdfObjectPanel;
import com.lowagie.rups.helpers.PdfTree;
import com.lowagie.rups.helpers.XRefTable;
import com.lowagie.rups.nodetypes.PdfObjectTreeNode;
import com.lowagie.rups.nodetypes.PdfTrailerTreeNode;
import com.lowagie.swing.browse.BrowseResult;
import com.lowagie.swing.browse.FileChooserAction;
import com.lowagie.swing.browse.filters.PdfFilter;
import com.lowagie.text.pdf.PdfPageLabels;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.RandomAccessFileOrArray;

/**
 * An application that allows you to inspect the syntax of a PDF file.
 */
public class Rups extends JFrame implements BrowseResult, TreeSelectionListener {
	
	/**	a serial version id */
	private static final long serialVersionUID = 4501670592786972072L;

	/** The reader object for this PDF file. */
	protected PdfReader reader = null;
	/** The factory that generates the tree nodes. */
	protected TreeNodeFactory factory;
	/** The table that will show info about the PDFs Crossreference table. */
	protected XRefTable xrefTable = new XRefTable();
	/** The tree that will reveal the internal PDF structure.  */
	protected PdfTree pdfTree = new PdfTree();
	/** The table that will show info about the pages. */
	protected PagesTable pages = new PagesTable();
	/** The outlines tree. */
	protected OutlineTree outlines = new OutlineTree();
	/** The panel that will contain info about a PDF object (card layout). */
	protected PdfObjectPanel objectPanel = new PdfObjectPanel();
	/** The action to open a file chooser. */
	protected FileChooserAction fileChooserAction;
	
	/**
	 * Main method of this application.
	 * @param args	no arguments needed
	 */
	public static void main(String[] args) {
		new Rups();
	}
	
	/**
	 * Creates the JFrame.
	 */
	public Rups() {
        super();
		fileChooserAction = new FileChooserAction(this, "Open", PdfFilter.INSTANCE, false);
		pdfTree.addTreeSelectionListener(this);
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
        setTitle("RUPS: Understanding PDF Syntax");
        setJMenuBar(getMenu());
        
		this.getContentPane().setLayout(new BorderLayout());
		JSplitPane main_splitpane = new JSplitPane();
		main_splitpane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		main_splitpane.setDividerLocation((int)(screen.getHeight() * .5));
		this.getContentPane().add(main_splitpane, java.awt.BorderLayout.CENTER);
		main_splitpane.add(objectPanel, JSplitPane.BOTTOM);
		
		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BorderLayout());
		main_splitpane.add(top_panel, JSplitPane.TOP);
		
		JSplitPane top_splitpane = new JSplitPane();
		top_splitpane.setDividerSize(3);
		top_splitpane.setDividerLocation((int)(screen.getWidth() * .2));
		JScrollPane tree_scrollpane = new JScrollPane();
		tree_scrollpane.setViewportView(pdfTree);
		top_splitpane.add(tree_scrollpane, JSplitPane.BOTTOM);
		
		JTabbedPane info_panel = new JTabbedPane();
		JScrollPane pages_scrollpane = new JScrollPane();
		pages_scrollpane.setViewportView(pages);
		info_panel.addTab("Pages", null, pages_scrollpane, "Pages");
		JScrollPane outlines_scrollpane = new JScrollPane();
		outlines_scrollpane.setViewportView(outlines);
		info_panel.addTab("Outlines", outlines_scrollpane);
		JScrollPane xref_scrollpane = new JScrollPane();
		xref_scrollpane.setViewportView(xrefTable);
		info_panel.addTab("XRef", null, xref_scrollpane, "Cross-reference table");
		top_splitpane.add(info_panel, JSplitPane.TOP);
		top_panel.add(top_splitpane, BorderLayout.CENTER);
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
        bar.add(file);
		return bar;
	}

	/**
	 * @see com.lowagie.swing.browse.BrowseResult#setFile(java.io.File)
	 */
	public void setFile(File file) {
		try {
			objectPanel.clear();
			xrefTable.setObjects(null);
			pages.loadPages(null, null);
			outlines.loadOutlines(null, null);
			reader = new PdfReader(new RandomAccessFileOrArray(file.getAbsolutePath()), null);
			if (!PdfWorker.loadPdf(this, reader)) {
				JOptionPane.showMessageDialog(this, "Currently loading another PDF document.\nYou can only load one PDF at a time.", "Dialog", JOptionPane.WARNING_MESSAGE);
			}
			else {
				pdfTree.resetRoot(file);
			}
		} catch (IOException e) {
			reader = null;
			JOptionPane.showMessageDialog(this, e.getMessage(), "Dialog", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Updates the components of the Rups application
	 * because a new PDF document was loaded into an
	 * indirect object store.
	 * @param	objects	the indirect object store
	 */
	public void update(IndirectObjectStore objects) {
		factory = new TreeNodeFactory(objects);
		xrefTable.setObjects(objects);
		xrefTable.setRenderer(objectPanel);
		pdfTree.resetRoot(factory, reader.getTrailer());
		pages.loadPages(pdfTree, PdfPageLabels.getPageLabels(reader));
		outlines.loadOutlines(factory, pdfTree);
	}
	
	/**
	 * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
	 */
	public void valueChanged(TreeSelectionEvent e) {
		Object selectednode = pdfTree.getLastSelectedPathComponent();
		if (selectednode instanceof PdfTrailerTreeNode) {
			fileChooserAction.actionPerformed(null);
		}
		else if (selectednode instanceof PdfObjectTreeNode) {
			PdfObjectTreeNode node = (PdfObjectTreeNode)selectednode;
			factory.expandNode(node);
			if (node.isRecursive()) {
				pdfTree.selectNode(node.getAncestor());
			}
			else if (node.isIndirect()) {
				xrefTable.selectRowByReference(node.getNumber());
			}
			else {
				objectPanel.render(node.getPdfObject());
			}
		}
	}
}