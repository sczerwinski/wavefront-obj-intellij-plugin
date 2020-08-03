// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static it.czerwinski.intellij.wavefront.lang.psi.ObjTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import it.czerwinski.intellij.wavefront.lang.psi.*;

public class ObjFaceVertexImpl extends ASTWrapperPsiElement implements ObjFaceVertex {

  public ObjFaceVertexImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ObjVisitor visitor) {
    visitor.visitFaceVertex(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ObjVisitor) accept((ObjVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public ObjTextureCoordinatesIndex getTextureCoordinatesIndex() {
    return findChildByClass(ObjTextureCoordinatesIndex.class);
  }

  @Override
  @NotNull
  public ObjVertexIndex getVertexIndex() {
    return findNotNullChildByClass(ObjVertexIndex.class);
  }

  @Override
  @Nullable
  public ObjVertexNormalIndex getVertexNormalIndex() {
    return findChildByClass(ObjVertexNormalIndex.class);
  }

}
