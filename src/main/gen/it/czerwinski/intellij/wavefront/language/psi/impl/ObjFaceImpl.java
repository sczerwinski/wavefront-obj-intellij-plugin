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

public class ObjFaceImpl extends ASTWrapperPsiElement implements ObjFace {

  public ObjFaceImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ObjVisitor visitor) {
    visitor.visitFace(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ObjVisitor) accept((ObjVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ObjFaceVertex> getFaceVertexList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ObjFaceVertex.class);
  }

  @Override
  @Nullable
  public ObjFaceType getType() {
    return ObjPsiImplUtil.getType(this);
  }

}
