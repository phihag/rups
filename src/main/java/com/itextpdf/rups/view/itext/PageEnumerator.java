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
package com.itextpdf.rups.view.itext;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.itextpdf.rups.model.TreeNodeFactory;
import com.itextpdf.rups.view.itext.treenodes.PdfPageTreeNode;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;

class PageEnumerator implements Enumeration<PdfPageTreeNode> {

	protected List<PdfPageTreeNode> pages = new ArrayList<PdfPageTreeNode>();
	protected TreeNodeFactory factory;
	protected int cursor = 0;
	
	public PageEnumerator(PdfDictionary catalog, TreeNodeFactory factory) {
		this.factory = factory;
		expand(catalog.getAsIndirectObject(PdfName.PAGES), catalog.getAsDict(PdfName.PAGES));
	}
	
	public boolean hasMoreElements() {
		return cursor < pages.size();
	}

	public PdfPageTreeNode nextElement() {
		return pages.get(cursor++);
	}

	public void expand(PdfIndirectReference ref, PdfDictionary dict) {
		if (dict == null)
			return;
		if (dict.isPages()) {
			PdfArray kids = dict.getAsArray(PdfName.KIDS);
			if (kids != null) {
				for (int i = 0; i < kids.size(); i++) {
					expand(kids.getAsIndirectObject(i), kids.getAsDict(i));
				}
			}
		}
		else if (dict.isPage()) {
			pages.add((PdfPageTreeNode)factory.getNode(ref.getNumber()));
		}
	}
}
