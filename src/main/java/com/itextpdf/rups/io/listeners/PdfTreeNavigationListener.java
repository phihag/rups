package com.itextpdf.rups.io.listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Class that keeps track of whether the last action was a keyboard or a mouse input.
 * @author Michael Demey
 */
public class PdfTreeNavigationListener implements KeyListener, MouseListener {

    private boolean arrowsUsedAsNavigation = false;

    public boolean isLastActionKeyboardNavigation() {
        return arrowsUsedAsNavigation;
    }

    /* Keyboard inputs */
    public void keyTyped(KeyEvent e) {
        arrowsUsedAsNavigation = true;
    }

    public void keyPressed(KeyEvent e) {
        arrowsUsedAsNavigation = true;
    }

    public void keyReleased(KeyEvent e) {
        arrowsUsedAsNavigation = true;
    }

    /* Mouse inputs */
    public void mouseClicked(MouseEvent e) {
        arrowsUsedAsNavigation = false;
    }

    public void mousePressed(MouseEvent e) {
        arrowsUsedAsNavigation = false;
    }

    public void mouseReleased(MouseEvent e) {
        arrowsUsedAsNavigation = false;
    }

    public void mouseEntered(MouseEvent e) {
        arrowsUsedAsNavigation = false;
    }

    public void mouseExited(MouseEvent e) {
        arrowsUsedAsNavigation = false;
    }
}