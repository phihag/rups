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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.tree.DefaultTreeModel;

import com.lowagie.rups.components.XfaTree;
import com.lowagie.rups.components.io.TextAreaOutputStream;
import com.lowagie.rups.components.io.XfaFileOpener;
import com.lowagie.rups.components.io.XfaFileSaver;
import com.lowagie.rups.interfaces.XfaInterface;
import com.lowagie.rups.nodetypes.XdpTreeNode;
import com.lowagie.swing.browse.BrowseResult;
import com.lowagie.swing.browse.FileChooserAction;
import com.lowagie.swing.helpers.Utilities;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Ruxfa extends JFrame implements BrowseResult {

	/** A serial version UID. */
	private static final long serialVersionUID = -8382755405051101725L;

	protected JMenuItem save;
	protected JMenuItem open;

	protected Document xfaDocument;
	
	/** Treeview of the XFA. */
	protected XfaTree treeview = new XfaTree();
	/** XML view of the XFA. */
	protected JTextArea textview = new JTextArea();

	/**
	 * Main method of this application.
	 * @param args	no arguments needed
	 */
	public static void main(String[] args) {
		Ruxfa ruxfa = new Ruxfa();
		ruxfa.setVisible(true);
		ruxfa.open.setAction(new FileChooserAction(ruxfa, "Open XFA file", null, false));
		ruxfa.open.setEnabled(true);
        ruxfa.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	/**
	 * Creates a window that can visualize an XFA resource.
	 */
	public Ruxfa() {
		super();
		initialize();
	}
	
	/**
	 * Initializes the content of the Ruxfa window.
	 */
	private void initialize() {

        // size and location
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(screen.getWidth() * .60), (int)(screen.getHeight() * .60));
        setLocation((int)(screen.getWidth() * .150), (int)(screen.getHeight() * .150));
        setResizable(true);
        
		// title and menu
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setTitle("RUXFA: Understanding XFA");
        setJMenuBar(getMenu());
        
        // tabs
		JTabbedPane tabs = new JTabbedPane();
		this.getContentPane().add(tabs);
		tabs.addTab("XML", null, Utilities.getScrollPane(textview), "Shows the original XFA resource (XML)");
		tabs.addTab("Tree", null, Utilities.getScrollPane(treeview), "Shows the XFA resource in a tree view");
		
	}
    
	/**
	 * Creates a menu bar.
	 * @return the menu bar for the application.
	 */
	private JMenuBar getMenu() {
		JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        open = new JMenuItem("Open");
        open.setEnabled(false);
        file.add(open);
        save = new JMenuItem("Save");
        save.setEnabled(false);
        file.add(save);
        bar.add(file);
		return bar;
	}
	
	public void loadXfa(XfaInterface xfa) {
		if (xfa == null) {
			xfaDocument = null;
			save.setEnabled(false);
			treeview = new XfaTree();
			textview = new JTextArea();
		}
		else {
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				xfa.writeTo(baos, null);
				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
				SAXReader reader = new SAXReader();
				Document xfaDocument = reader.read(bais);
				treeview.setModel(new DefaultTreeModel(new XdpTreeNode(xfaDocument)));
				treeview.repaint();
				save.setEnabled(true);
				save.setAction(new FileChooserAction(new XfaFileSaver(xfa), "Save XFA file", null, true));
				OutputFormat format = new OutputFormat("   ", true);
		        XMLWriter writer = new XMLWriter( new TextAreaOutputStream(textview), format );
		        writer.write( xfaDocument );
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
	}

	public void setFile(File file) {
		loadXfa(new XfaFileOpener(file));
	}
}