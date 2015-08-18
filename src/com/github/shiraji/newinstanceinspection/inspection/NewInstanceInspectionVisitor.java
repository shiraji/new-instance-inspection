package com.github.shiraji.newinstanceinspection.inspection;

import com.github.shiraji.newinstanceinspection.util.InspectionPsiUtil;
import com.intellij.codeInsight.daemon.impl.quickfix.AddMethodFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.siyeh.ig.BaseInspectionVisitor;
import org.jetbrains.annotations.NotNull;

public class NewInstanceInspectionVisitor extends BaseInspectionVisitor {
    public static final String QUALIFIED_NAME_OF_SUPER_CLASS = "android.app.Fragment";
    public static final String QUALIFIED_NAME_OF_SUPER_CLASS_FOR_SUPPORT_LIBRARY = "android.support.v4.app.Fragment";
    public static final String STATIC_METHOD_NAME = "newInstance";

    private ProblemsHolder mHolder;
    private boolean mIsOnTheFly;

    public NewInstanceInspectionVisitor(ProblemsHolder holder, boolean isOnTheFly) {
        mHolder = holder;
        mIsOnTheFly = isOnTheFly;
    }

    @Override
    public void visitClass(PsiClass aClass) {
        if (InspectionPsiUtil.isAbstactClass(aClass)) {
            return;
        }

        if (!isFragment(aClass)) {
            return;
        }

        PsiMethod[] methods = aClass.getMethods();

        for (PsiMethod method : methods) {
            if (isNewInstanceMethod(aClass, method)) {
                return;
            }
        }

        registerProblem(aClass);
    }

    private boolean isNewInstanceMethod(PsiClass aClass, PsiMethod method) {
        return InspectionPsiUtil.isStaticMethod(method) &&
                InspectionPsiUtil.isPublicMethod(method) &&
                isMethodNameNewInstance(method) &&
                isReturnFragment(aClass, method);
    }

    private boolean isMethodNameNewInstance(PsiMethod method) {
        return STATIC_METHOD_NAME.equals(method.getName());
    }

    private boolean isReturnFragment(PsiClass aClass, PsiMethod method) {
        return method.getReturnTypeElement() != null &&
                aClass.getName().equals(method.getReturnTypeElement().getText());
    }

    private boolean isFragment(PsiClass aClass) {
        PsiClass fragmentClass = InspectionPsiUtil.createPsiClass(QUALIFIED_NAME_OF_SUPER_CLASS, aClass.getProject());
        if (fragmentClass != null && aClass.isInheritor(fragmentClass, true)) {
            return true;
        }

        PsiClass supportLibFragmentClass = InspectionPsiUtil.createPsiClass(QUALIFIED_NAME_OF_SUPER_CLASS_FOR_SUPPORT_LIBRARY, aClass.getProject());
        return supportLibFragmentClass != null && aClass.isInheritor(supportLibFragmentClass, true);
    }

    private void registerProblem(PsiClass aClass) {
        PsiElement nameIdentifier = aClass.getNameIdentifier();
        if (nameIdentifier == null) {
            nameIdentifier = aClass;
        }

        mHolder.registerProblem(nameIdentifier, getAlertMessage(aClass),
                ProblemHighlightType.GENERIC_ERROR_OR_WARNING,
                TextRange.allOf(aClass.getName()), new AddMethodFix(getMethodText(aClass), aClass));
    }

    @NotNull
    private String getAlertMessage(PsiClass aClass) {
        return "Implement public static " + aClass.getName() + " newInstance()";
    }

    @NotNull
    private String getMethodText(PsiClass aClass) {
        String className = aClass.getName();
        return "public static " + className + " newInstance() { " + className + " fragment = new " + className + "();return fragment; }";
    }
}
