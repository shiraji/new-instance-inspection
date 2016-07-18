package com.github.shiraji.newinstanceinspection.inspection;

import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.ui.DocumentAdapter;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class NewInstanceInspection extends BaseJavaLocalInspectionTool {

    @SuppressWarnings("WeakerAccess")
    public String methodName = "newInstance";

    @Nls
    @NotNull
    @Override
    public String getGroupDisplayName() {
        return "Android";
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return "Fragment should implement " + methodName + "()";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new NewInstanceInspectionVisitor(holder, methodName);
    }

    @Nullable
    @Override
    public JComponent createOptionsPanel() {
        InspectionOptionPanel panel = new InspectionOptionPanel();
        panel.methodNameTextField.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent e) {
                final Document document = e.getDocument();
                try {
                    final String text = document.getText(0, document.getLength());
                    if (text != null) {
                        methodName = text.trim();
                    }
                } catch (BadLocationException e1) {
                }
            }
        });
        panel.methodNameTextField.setText(methodName);
        return panel.panel;
    }

}
