// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.language.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static it.czerwinski.intellij.wavefront.language.psi.ObjTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import it.czerwinski.intellij.wavefront.language.psi.*;

public class ObjVertexNormalImpl extends ASTWrapperPsiElement implements ObjVertexNormal {

  public ObjVertexNormalImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ObjVisitor visitor) {
    visitor.visitVertexNormal(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ObjVisitor) accept((ObjVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  public int getIndex() {
    return ObjPsiImplUtil.getIndex(this);
  }

  @Override
  @NotNull
  public List<Float> getCoordinates() {
    return ObjPsiImplUtil.getCoordinates(this);
  }

}