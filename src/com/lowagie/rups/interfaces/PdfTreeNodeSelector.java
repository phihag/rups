package com.lowagie.rups.interfaces;

import com.lowagie.rups.nodetypes.PdfObjectTreeNode;
import com.lowagie.rups.nodetypes.PdfPagesTreeNode;

public interface PdfTreeNodeSelector {
	public void selectNode(PdfObjectTreeNode node);
	public PdfPagesTreeNode getPages();
	public PdfObjectTreeNode getOutlines();
}
