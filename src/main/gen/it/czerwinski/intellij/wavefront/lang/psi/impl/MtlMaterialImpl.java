// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static it.czerwinski.intellij.wavefront.lang.psi.MtlTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import it.czerwinski.intellij.wavefront.lang.psi.*;

public class MtlMaterialImpl extends ASTWrapperPsiElement implements MtlMaterial {

  public MtlMaterialImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MtlVisitor visitor) {
    visitor.visitMaterial(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MtlVisitor) accept((MtlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<MtlColor> getColorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlColor.class);
  }

  @Override
  @NotNull
  public List<MtlMap> getMapList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlMap.class);
  }

  @Override
  @NotNull
  public List<MtlProperty> getPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlProperty.class);
  }

}
