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

package com.lowagie.rups.interfaces;

import com.lowagie.rups.nodetypes.PdfObjectTreeNode;
import com.lowagie.rups.nodetypes.PdfPagesTreeNode;

/**
 * Interface implemented by objects that can show a PDF as a JTree.
 */
public interface PdfTreeNodeSelector {
	/**
	 * Select a specific node in the tree.
	 * Typically this method will be called from a different tree,
	 * such as the pages, outlines or form tree.
	 * @param	node	the node that has to be selected
	 */
	public void selectNode(PdfObjectTreeNode node);
	/**
	 * Gets the root of the page tree
	 * @return	the top level PdfPagesTreeNode
	 */
	public PdfPagesTreeNode getPages();

	/**
	 * Gets the root of the outline tree.
	 * @return	the top level Outline TreeNode
	 */
	public PdfObjectTreeNode getOutlines();

	/**
	 * Gets the root of the form tree.
	 * @return	the top level Outline TreeNode
	 */
	public PdfObjectTreeNode getForm();
}
