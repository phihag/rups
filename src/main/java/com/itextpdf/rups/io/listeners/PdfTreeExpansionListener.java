package com.itextpdf.rups.io.listeners;

import com.itextpdf.rups.view.itext.PdfTree;
import com.itextpdf.rups.view.itext.treenodes.PdfObjectTreeNode;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Listener that checks if the expanded node has one child. If it has one child it should expand it.
 * @author Michael Demey
 */
public class PdfTreeExpansionListener implements TreeExpansionListener {

    public void treeExpanded(TreeExpansionEvent event) {
        PdfTree  tree = (PdfTree) event.getSource();
        DefaultMutableTreeNode node = ((DefaultMutableTreeNode) event.getPath().getLastPathComponent());
        if ( node.getChildCount() == 1 ) {
            TreeNode child = node.getChildAt(0);
            tree.selectNode((PdfObjectTreeNode) child);
            tree.expandPath(new TreePath(((DefaultTreeModel) tree.getModel() ).getPathToRoot(child)));
        }
    }

    public void treeCollapsed(TreeExpansionEvent event) {
    }
}