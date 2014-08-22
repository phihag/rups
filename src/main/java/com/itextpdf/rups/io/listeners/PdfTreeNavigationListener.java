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

    private boolean keyboard = false;

    public boolean isLastActionKeyboardNavigation() {
        return keyboard;
    }

    /* Keyboard inputs */
    public void keyTyped(KeyEvent e) {
        keyboard = true;
    }

    public void keyPressed(KeyEvent e) {
        keyboard = true;
    }

    public void keyReleased(KeyEvent e) {
        keyboard = true;
    }

    /* Mouse inputs */
    public void mouseClicked(MouseEvent e) {
        keyboard = false;
    }

    public void mousePressed(MouseEvent e) {
        keyboard = false;
    }

    public void mouseReleased(MouseEvent e) {
        keyboard = false;
    }

    public void mouseEntered(MouseEvent e) {
        keyboard = false;
    }

    public void mouseExited(MouseEvent e) {
        keyboard = false;
    }
}