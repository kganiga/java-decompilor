package com.khalilganiga.javadecompiler;

import com.strobel.decompiler.Decompiler;
import com.strobel.decompiler.PlainTextOutput;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * The type Decompilation worker.
 */
public class DecompilationWorker extends SwingWorker<Void, Integer> {
    private final String inputDirectoryPath;
    private final String outputDirectoryPath;
    private final JProgressBar progressBar;
    private int totalClassFiles;

    /**
     * Instantiates a new Decompilation worker.
     *
     * @param inputDirectoryPath  the input directory path
     * @param outputDirectoryPath the output directory path
     * @param progressBar         the progress bar
     */
    public DecompilationWorker(String inputDirectoryPath, String outputDirectoryPath, JProgressBar progressBar) {
        this.inputDirectoryPath = inputDirectoryPath;
        this.outputDirectoryPath = outputDirectoryPath;
        this.progressBar = progressBar;
    }

    @Override
    protected Void doInBackground() {
        File inputDir = new File(inputDirectoryPath);
        File outputDir = new File(outputDirectoryPath);

        if (!inputDir.exists() || !inputDir.isDirectory() || !outputDir.exists()) {
            showInfoDialog("Invalid input or output directory", "Error");
            return null;
        }

        totalClassFiles = countClassFiles(inputDir);
        if (totalClassFiles == 0) {
            showInfoDialog("No class files found in the input directory", "Error");
            return null;
        }
        progressBar.setMaximum(totalClassFiles);
        processDirectory(inputDir, outputDir);

        progressBar.setValue(progressBar.getMaximum()); // Update progress bar to show completion
        showInfoDialog("Decompilation completed", "Success");

        return null;
    }

    private int countClassFiles(File dir) {
        int count = 0;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    count += countClassFiles(file); // Recursive call for subdirectories
                } else if (file.getName().endsWith(".class")) {
                    count++;
                }
            }
        }
        return count;
    }

    private void processDirectory(File inputDir, File outputDir) {
        File[] files = inputDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    File newOutputDir = new File(outputDir, file.getName());
                    newOutputDir.mkdirs();
                    processDirectory(file, newOutputDir); // Recursive call for subdirectories
                } else if (file.getName().endsWith(".class")) {
                    String relativePath = inputDir.toURI().relativize(file.toURI()).getPath();
                    String outputFilePath = new File(outputDir, relativePath.replace(".class", ".java")).getAbsolutePath();
                    // Decompilation logic goes here...
                    decompileClassFile(file.getAbsolutePath(), outputFilePath);
                    // Simulating delay (Replace this with actual decompilation process)
                    try {
                        Thread.sleep(100); // Replace this with actual decompilation process
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    publish(progressBar.getValue() + 1);
                }
            }
        }
    }

    private void decompileClassFile(String absolutePath, String outputFilePath) {
        try {
            PlainTextOutput output = new PlainTextOutput();
            Decompiler.decompile(absolutePath, output);

            try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)) {
                fileOutputStream.write(output.toString().getBytes());
            }
        } catch (IOException e) {
            Utils.showInfoDialog("Error during de-compilation: " + e.getMessage(), "Error");
        }
    }

    @Override
    protected void process(List<Integer> chunks) {
        for (int value : chunks) {
            progressBar.setValue(value);
        }
    }

    private void showInfoDialog(String message, String title) {
        Utils.showInfoDialog(message, title);
    }
}
