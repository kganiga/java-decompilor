package com.khalilganiga.javadecompiler;

import javax.swing.*;

/**
 * The type Utils.
 *
 * @author Khalil.Ganiga
 */
public class Utils {

    /**
     * Show info dialog.
     *
     * @param message the message
     * @param type    the type
     */
    public static void showInfoDialog(String message, String type) {
        int infoMessage = type == "Success" ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(null, message, type, infoMessage);
    }
}
