// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static it.czerwinski.intellij.wavefront.lang.psi.ObjTypes.*;
import it.czerwinski.intellij.wavefront.lang.psi.*;

public class ObjGroupImpl extends ObjGroupingElementImpl implements ObjGroup {

  public ObjGroupImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ObjVisitor visitor) {
    visitor.visitGroup(this);
  }

  @Override
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
  public List<ObjLine> getLineList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ObjLine.class);
  }

  @Override
  @NotNull
  public List<ObjMaterialFileReference> getMaterialFileReferenceList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ObjMaterialFileReference.class);
  }

  @Override
  @NotNull
  public List<ObjMaterialReference> getMaterialReferenceList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ObjMaterialReference.class);
  }

  @Override
  @NotNull
  public List<ObjObjectOrGroupIdentifier> getObjectOrGroupIdentifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ObjObjectOrGroupIdentifier.class);
  }

  @Override
  @NotNull
  public List<ObjPoint> getPointList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ObjPoint.class);
  }

  @Override
  @NotNull
  public List<ObjSmoothShading> getSmoothShadingList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, ObjSmoothShading.class);
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
