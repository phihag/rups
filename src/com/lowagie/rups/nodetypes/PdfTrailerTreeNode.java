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

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.lowagie.text.pdf.PdfDictionary;

/**
 * A special treenode that will be used for the trailer dictionary
 * of a PDF file.
 */
public class PdfTrailerTreeNode extends PdfObjectTreeNode {

	/** A serial version id. */
	private static final long serialVersionUID = -3607980103983635182L;

    /**
     * Constructs a simple text tree node.
     */
    public PdfTrailerTreeNode() {
		super(null);
		setUserObject("Open a document");
	}

	/**
	 * Sets the object for this node.
	 * @param trailer	the trailer dictionary of a PDF file.
	 */
	public void setTrailer(PdfDictionary trailer) {
		object = trailer;
	}
	
    /**
     * @see com.lowagie.rups.nodetypes.PdfObjectTreeNode#getIcon()
     */
    public Icon getIcon() {
		return new ImageIcon(PdfTrailerTreeNode.class
				.getResource("icons/pdf.png"));
    }
}