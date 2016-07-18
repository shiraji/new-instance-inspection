package com.github.shiraji.newinstanceinspection.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;

public class InspectionPsiUtil {
    public static PsiClass createPsiClass(String qualifiedName, Project project) {
        final JavaPsiFacade psiFacade = JavaPsiFacade.getInstance(project);
        final GlobalSearchScope searchScope = GlobalSearchScope.allScope(project);
        return psiFacade.findClass(qualifiedName, searchScope);
    }

    public static boolean isAbstactClass(PsiClass aClass) {
        if (aClass == null || aClass.getModifierList() == null) {
            return false;
        }

        return aClass.getModifierList().hasModifierProperty("abstract");
    }

    public static boolean isStaticMethod(PsiMethod method) {
        return hasMethodModifier(method, "static");
    }

    public static boolean isPublicMethod(PsiMethod method) {
        return hasMethodModifier(method, "public");
    }

    private static boolean hasMethodModifier(PsiMethod method, String aStatic) {
        return method != null &&
                method.getModifierList().hasModifierProperty(aStatic);
    }

}
