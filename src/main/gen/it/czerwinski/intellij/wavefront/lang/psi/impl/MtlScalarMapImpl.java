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

public class MtlScalarMapImpl extends ASTWrapperPsiElement implements MtlScalarMap {

  public MtlScalarMapImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MtlVisitor visitor) {
    visitor.visitScalarMap(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MtlVisitor) accept((MtlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public MtlDisplacementMap getDisplacementMap() {
    return findChildByClass(MtlDisplacementMap.class);
  }

  @Override
  @Nullable
  public MtlDissolveMap getDissolveMap() {
    return findChildByClass(MtlDissolveMap.class);
  }

  @Override
  @Nullable
  public MtlSpecularExponentMap getSpecularExponentMap() {
    return findChildByClass(MtlSpecularExponentMap.class);
  }

  @Override
  @Nullable
  public MtlStencilDecalMap getStencilDecalMap() {
    return findChildByClass(MtlStencilDecalMap.class);
  }

}
