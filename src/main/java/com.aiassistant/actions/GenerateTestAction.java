package com.aiassistant.actions;

import com.aiassistant.models.TestCase;
import com.aiassistant.services.AITestGenerationService;
import com.aiassistant.services.ExcelParserService;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.util.List;

public class GenerateTestAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        Editor editor = e.getData(CommonDataKeys.EDITOR);

        if (editor == null) {
            return;
        }

        Document document = editor.getDocument();

        /* STEP 0 — CHECK IF TEXT IS SELECTED */

        String selectedText = editor.getSelectionModel().getSelectedText();

        AITestGenerationService aiService = new AITestGenerationService();

        StringBuilder generatedCode = new StringBuilder();

        if (selectedText != null && !selectedText.isEmpty()) {

            /* NEW FEATURE — GENERATE FROM SELECTED TEXT */

            String prompt = """
You are a QA automation engineer.

Generate a Playwright Java JUnit5 test method.

Context:
%s

Rules:
- Generate ONLY one test method
- Do NOT generate full class
- Do NOT include explanations
""".formatted(selectedText);

            String aiGeneratedTest = aiService.generateTestFromPrompt(prompt);

            generatedCode.append(aiGeneratedTest);

        } else {

            /* EXISTING FEATURE — EXCEL TEST GENERATION */

            FileChooserDescriptor descriptor =
                    new FileChooserDescriptor(true, false, false, false, false, false);

            descriptor.setTitle("Select Excel Test Case File");

            VirtualFile file = FileChooser.chooseFile(descriptor, e.getProject(), null);

            if (file == null) {
                return;
            }

            File excelFile = new File(file.getPath());

            ExcelParserService parser = new ExcelParserService();

            List<TestCase> testCases = parser.parseExcel(excelFile);

            for (TestCase tc : testCases) {

                String aiGeneratedTest = aiService.generateTestWithAI(tc);

                generatedCode.append(aiGeneratedTest);
                generatedCode.append("\n\n");
            }
        }

        /* INSERT GENERATED CODE INTO EDITOR */

        WriteCommandAction.runWriteCommandAction(
                e.getProject(),
                () -> document.insertString(
                        editor.getCaretModel().getOffset(),
                        "\n\n" + generatedCode.toString()
                )
        );
    }
}