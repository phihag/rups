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

package com.lowagie.rups.nodetypes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import com.lowagie.rups.interfaces.XfaInterface;
import com.lowagie.text.pdf.PRStream;
import com.lowagie.text.pdf.PdfReader;

/**
 * This is the root tree node for the different parts of the XFA resource; it's a child
 * of the root in the FormTree.
 * This resource can be one XDP stream (in which case this root will only have one child)
 * or different streams with individual packets comprising the XML Data Package.
 */
public class XfaTreeNode extends FormTreeNode implements XfaInterface {

	/** A serial version UID. */
	private static final long serialVersionUID = 2463297568233643790L;

	public static final byte[] BOUNDARY_START = "<!--\nRUPS XFA individual packet: end of [".getBytes();
	public static final byte[] BOUNDARY_MIDDLE = "]; start of [".getBytes();
	public static final byte[] BOUNDARY_END = "]\n-->".getBytes();
	
	/**
	 * Creates the root node of the XFA tree.
	 * This will be a child of the FormTree root node.
	 * @param	xfa	the XFA node in the PdfTree (a child of the AcroForm node in the PDF catalog)
	 */
	public XfaTreeNode(PdfObjectTreeNode xfa) {
		super(xfa);
	}

	/**
	 * Adds a child node to the XFA root.
	 * The child node either corresponds with the complete XDP stream
	 * (if the XFA root only has one child) or with individual packet. 
	 * @param key	the name of the packet
	 * @param value	the corresponding stream node in the PdfTree
	 */
	public void addPacket(String key, PdfObjectTreeNode value) {
		FormTreeNode node = new FormTreeNode(value);
		node.setUserObject(key);
		this.add(node);
	}
	
	/**
	 * Writes (part of) the XFA resource to an OutputStream.
	 * If key is <code>null</code>, the complete resource is written;
	 * if key refers to an individual package, this package only is
	 * written to the OutputStream.
	 * @param os	the OutputStream to which the XML is written.
	 * @param key	the key of an individual package (can be null if the complete XML file is needed)
	 * @throws IOException	usual exception when there's a problem writing to an OutputStream
	 */
	public void writeTo(OutputStream os, String key) throws IOException {
		Enumeration children = this.children();
		FormTreeNode node;
		PRStream stream;
		String tmp = null;
		while (children.hasMoreElements()) {
			node = (FormTreeNode) children.nextElement();
			if (key == null || key.equals(node.getUserObject())) {
				if (tmp != null) {
					os.write(BOUNDARY_START);
					os.write(tmp.getBytes());
					os.write(BOUNDARY_MIDDLE);
					os.write(((String)node.getUserObject()).getBytes());
					os.write(BOUNDARY_END);
				}
				tmp = (String)node.getUserObject();
				stream = (PRStream)node.getCorrespondingPdfObjectNode().getPdfObject();
				os.write(PdfReader.getStreamBytes(stream));
				if (key != null) {
					break;
				}
			}
		}
		os.flush();
		os.close();
	}
}