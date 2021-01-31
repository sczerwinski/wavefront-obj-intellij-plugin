// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static it.czerwinski.intellij.wavefront.lang.psi.MtlTypes.*;
import it.czerwinski.intellij.wavefront.lang.psi.*;

public class MtlSpecularColorImpl extends MtlColorElementImpl implements MtlSpecularColor {

  public MtlSpecularColorImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MtlVisitor visitor) {
    visitor.visitSpecularColor(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MtlVisitor) accept((MtlVisitor)visitor);
    else super.accept(visitor);
  }

}
