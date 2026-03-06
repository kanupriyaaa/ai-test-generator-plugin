package com.aiassistant.toolwindow;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.util.List;

import com.aiassistant.models.TestCase;
import com.aiassistant.services.ExcelParserService;
import com.aiassistant.services.AITestGenerationService;

public class AITestGeneratorPanel {

    private JPanel mainPanel;
    private JButton uploadExcelButton;
    private JButton generateButton;
    private JButton insertButton;
    private JTextArea previewArea;
    private Project project;
    private File uploadedExcelFile;

    private JProgressBar progressBar;

    public AITestGeneratorPanel(Project project) {

        this.project = project;

        mainPanel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();

        uploadExcelButton = new JButton("Upload Excel");
        generateButton = new JButton("Generate Tests");
        insertButton = new JButton("Insert Into Editor");

        /*
        -----------------------------------------
        Progress Bar Loader
        -----------------------------------------
        */

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setString("Generating tests with AI...");
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        /*
        -----------------------------------------
        Upload Excel
        -----------------------------------------
        */

        uploadExcelButton.addActionListener(e -> {

            FileChooserDescriptor descriptor =
                    new FileChooserDescriptor(true, false, false, false, false, false);

            descriptor.setTitle("Select Excel Test Case File");

            VirtualFile file = FileChooser.chooseFile(descriptor, project, null);

            if (file == null) {
                return;
            }

            uploadedExcelFile = new File(file.getPath());

            previewArea.setText("Excel file uploaded:\n" + uploadedExcelFile.getName());
        });

        /*
        -----------------------------------------
        Insert Into Editor
        -----------------------------------------
        */

        insertButton.addActionListener(e -> {

            Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();

            if (editor == null) {
                return;
            }

            Document document = editor.getDocument();
            String code = previewArea.getText();

            WriteCommandAction.runWriteCommandAction(project, () ->
                    document.insertString(editor.getCaretModel().getOffset(), code)
            );
        });

        /*
        -----------------------------------------
        Generate Tests
        -----------------------------------------
        */

        generateButton.addActionListener(e -> {

            if (uploadedExcelFile == null) {

                JOptionPane.showMessageDialog(mainPanel,
                        "Please upload an Excel file first.",
                        "No File",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

            progressBar.setVisible(true);
            generateButton.setEnabled(false);
            previewArea.setText("Generating tests...\n\n");

            new Thread(() -> {

                try {

                    ExcelParserService parser = new ExcelParserService();
                    List<TestCase> testCases = parser.parseExcel(uploadedExcelFile);

                    AITestGenerationService aiService = new AITestGenerationService();

                    StringBuilder generatedCode = new StringBuilder();

                    for (TestCase testCase : testCases) {

                        SwingUtilities.invokeLater(() ->
                                previewArea.append("Generating test for: " + testCase.getTestId() + "\n")
                        );

                        String aiResult = aiService.generateTestWithAI(testCase);

                        generatedCode.append(aiResult).append("\n\n");
                    }

                    SwingUtilities.invokeLater(() -> {

                        progressBar.setVisible(false);
                        generateButton.setEnabled(true);

                        previewArea.setText(generatedCode.toString());
                    });

                } catch (Exception ex) {

                    SwingUtilities.invokeLater(() -> {

                        progressBar.setVisible(false);
                        generateButton.setEnabled(true);

                        previewArea.setText("Error generating tests:\n" + ex.getMessage());
                    });
                }

            }).start();
        });

        /*
        -----------------------------------------
        UI Layout
        -----------------------------------------
        */

        topPanel.add(uploadExcelButton);
        topPanel.add(generateButton);
        topPanel.add(progressBar);

        previewArea = new JTextArea();
        previewArea.setEditable(false);
        previewArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(previewArea);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(insertButton, BorderLayout.SOUTH);
    }

    public JPanel getContent() {
        return mainPanel;
    }
}