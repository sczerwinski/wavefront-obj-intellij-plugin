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

public class ObjObjectImpl extends ASTWrapperPsiElement implements ObjObject {

  public ObjObjectImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ObjVisitor visitor) {
    visitor.visitObject(this);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ObjVisitor) accept((ObjVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<ObjFace> getFaceList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ObjFace.class);
  }

  @Override
  @NotNull
  public List<ObjTextureCoordinates> getTextureCoordinatesList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ObjTextureCoordinates.class);
  }

  @Override
  @NotNull
  public List<ObjVertex> getVertexList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ObjVertex.class);
  }

  @Override
  @NotNull
  public List<ObjVertexNormal> getVertexNormalList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ObjVertexNormal.class);
  }

}
