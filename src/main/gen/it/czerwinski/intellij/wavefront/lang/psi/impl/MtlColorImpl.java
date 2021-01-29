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

public class MtlColorImpl extends ASTWrapperPsiElement implements MtlColor {

  public MtlColorImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MtlVisitor visitor) {
    visitor.visitColor(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MtlVisitor) accept((MtlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public MtlAmbientColor getAmbientColor() {
    return findChildByClass(MtlAmbientColor.class);
  }

  @Override
  @Nullable
  public MtlDiffuseColor getDiffuseColor() {
    return findChildByClass(MtlDiffuseColor.class);
  }

  @Override
  @Nullable
  public MtlSpecularColor getSpecularColor() {
    return findChildByClass(MtlSpecularColor.class);
  }

  @Override
  @Nullable
  public MtlTransmissionFilter getTransmissionFilter() {
    return findChildByClass(MtlTransmissionFilter.class);
  }

}
