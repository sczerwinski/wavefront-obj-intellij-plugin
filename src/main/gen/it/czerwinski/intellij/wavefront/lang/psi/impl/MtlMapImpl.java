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

public class MtlMapImpl extends ASTWrapperPsiElement implements MtlMap {

  public MtlMapImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MtlVisitor visitor) {
    visitor.visitMap(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MtlVisitor) accept((MtlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public MtlBumpMap getBumpMap() {
    return findChildByClass(MtlBumpMap.class);
  }

  @Override
  @Nullable
  public MtlColorMap getColorMap() {
    return findChildByClass(MtlColorMap.class);
  }

  @Override
  @Nullable
  public MtlReflectionMap getReflectionMap() {
    return findChildByClass(MtlReflectionMap.class);
  }

  @Override
  @Nullable
  public MtlScalarMap getScalarMap() {
    return findChildByClass(MtlScalarMap.class);
  }

}
