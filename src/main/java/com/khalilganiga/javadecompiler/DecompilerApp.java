package com.khalilganiga.javadecompiler;

import javax.swing.*;

/**
 * 
 *
 * @author Khalil.Ganiga
 */
public class DecompilerApp {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DecompilerGUI::new);
    }
}
