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

public class MtlPropertyImpl extends ASTWrapperPsiElement implements MtlProperty {

  public MtlPropertyImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MtlVisitor visitor) {
    visitor.visitProperty(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MtlVisitor) accept((MtlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public MtlDissolve getDissolve() {
    return findChildByClass(MtlDissolve.class);
  }

  @Override
  @Nullable
  public MtlIllumination getIllumination() {
    return findChildByClass(MtlIllumination.class);
  }

  @Override
  @Nullable
  public MtlOpticalDensity getOpticalDensity() {
    return findChildByClass(MtlOpticalDensity.class);
  }

  @Override
  @Nullable
  public MtlSharpness getSharpness() {
    return findChildByClass(MtlSharpness.class);
  }

  @Override
  @Nullable
  public MtlSpecularExponent getSpecularExponent() {
    return findChildByClass(MtlSpecularExponent.class);
  }

}
