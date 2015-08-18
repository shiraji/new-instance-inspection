package com.github.shiraji.newinstanceinspection.inspection;

import com.intellij.codeInspection.BaseJavaLocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class NewInstanceInspection extends BaseJavaLocalInspectionTool {
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
        return "Fragment should implement newInstance()";
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new NewInstanceInspectionVisitor(holder, isOnTheFly);
    }
}
