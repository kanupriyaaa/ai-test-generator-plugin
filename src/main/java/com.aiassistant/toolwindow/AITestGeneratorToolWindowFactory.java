package com.aiassistant.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.project.DumbAware;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;

public class AITestGeneratorToolWindowFactory implements ToolWindowFactory, DumbAware {

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {

        AITestGeneratorPanel panel = new AITestGeneratorPanel(project);

        Content content = ContentFactory.getInstance()
                .createContent(panel.getContent(), "", false);

        toolWindow.getContentManager().addContent(content);
    }
}