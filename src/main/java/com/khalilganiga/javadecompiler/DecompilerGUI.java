package com.khalilganiga.javadecompiler;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;

/**
 * @author Khalil.Ganiga
 */
public class DecompilerGUI extends JFrame {

    private JTextField inputDirectoryField;
    private JTextField outputDirectoryField;
    private JProgressBar progressBar;
    private JButton runButton;

    /**
     * Instantiates a new Decompiler gui.
     */
    public DecompilerGUI() {
        super("Java Decompiler By Khalil");
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        inputDirectoryField = new JTextField(30);
        JButton inputDirectoryButton = new JButton("Choose Input Directory");
        outputDirectoryField = new JTextField(30);
        JButton outputDirectoryButton = new JButton("Choose Output Directory");
        runButton = new JButton("Run");
        runButton.setEnabled(false);
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 128, 128)); // Teal green color

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel decompilerDescription = new JLabel("Decompile Java classes to readable code");
        decompilerDescription.setFont(new Font("Arial", Font.BOLD, 16)); // Font: Arial, Bold, Size: 16
        decompilerDescription.setForeground(Color.BLACK);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(decompilerDescription, constraints);

        constraints.gridwidth = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        panel.add(inputDirectoryButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(outputDirectoryButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(runButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(inputDirectoryField, constraints);

        constraints.gridy = 2;
        panel.add(outputDirectoryField, constraints);

        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(progressBar, constraints);

        int buttonWidth = Math.max(inputDirectoryButton.getPreferredSize().width, outputDirectoryButton.getPreferredSize().width);
        inputDirectoryButton.setPreferredSize(new Dimension(buttonWidth, inputDirectoryButton.getPreferredSize().height));
        outputDirectoryButton.setPreferredSize(new Dimension(buttonWidth, outputDirectoryButton.getPreferredSize().height));

        inputDirectoryField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRunButtonState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRunButtonState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRunButtonState();
            }
        });

        outputDirectoryField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateRunButtonState();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateRunButtonState();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateRunButtonState();
            }
        });

        inputDirectoryButton.addActionListener(e -> chooseDirectory(inputDirectoryField));
        outputDirectoryButton.addActionListener(e -> chooseDirectory(outputDirectoryField));
        runButton.addActionListener(e -> runDecompiler());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 200);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

        add(panel, BorderLayout.CENTER);
    }

    private void updateRunButtonState() {
        boolean inputSet = !inputDirectoryField.getText().isEmpty();
        boolean outputSet = !outputDirectoryField.getText().isEmpty();
        runButton.setEnabled(inputSet && outputSet);
    }

    private void chooseDirectory(JTextField textField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            textField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void runDecompiler() {
        String inputDirectoryPath = inputDirectoryField.getText();
        String outputDirectoryPath = outputDirectoryField.getText();

        if (isValidDirectory(inputDirectoryPath) && isValidDirectory(outputDirectoryPath)) {
            progressBar.setValue(0);
            new DecompilationWorker(inputDirectoryPath, outputDirectoryPath, progressBar).execute();
        } else {
            showInfoDialog("Invalid input or output directory", "Error");
        }
    }

    private boolean isValidDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        return directory.exists() && directory.isDirectory();
    }

    private void showInfoDialog(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}