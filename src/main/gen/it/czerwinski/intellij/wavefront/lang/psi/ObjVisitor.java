// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class ObjVisitor extends PsiElementVisitor {

  public void visitFace(@NotNull ObjFace o) {
    visitPsiElement(o);
  }

  public void visitFaceVertex(@NotNull ObjFaceVertex o) {
    visitPsiElement(o);
  }

  public void visitGroup(@NotNull ObjGroup o) {
    visitGroupingElement(o);
  }

  public void visitLine(@NotNull ObjLine o) {
    visitPsiElement(o);
  }

  public void visitMaterialFileReference(@NotNull ObjMaterialFileReference o) {
    visitPsiElement(o);
  }

  public void visitMaterialReference(@NotNull ObjMaterialReference o) {
    visitPsiElement(o);
  }

  public void visitObject(@NotNull ObjObject o) {
    visitGroupingElement(o);
  }

  public void visitObjectOrGroupIdentifier(@NotNull ObjObjectOrGroupIdentifier o) {
    visitObjectOrGroupIdentifierElement(o);
  }

  public void visitPoint(@NotNull ObjPoint o) {
    visitPsiElement(o);
  }

  public void visitSmoothShading(@NotNull ObjSmoothShading o) {
    visitFlagElement(o);
  }

  public void visitTextureCoordinates(@NotNull ObjTextureCoordinates o) {
    visitVectorElement(o);
  }

  public void visitTextureCoordinatesIndex(@NotNull ObjTextureCoordinatesIndex o) {
    visitIndexElement(o);
  }

  public void visitVertex(@NotNull ObjVertex o) {
    visitVectorElement(o);
  }

  public void visitVertexIndex(@NotNull ObjVertexIndex o) {
    visitIndexElement(o);
  }

  public void visitVertexNormal(@NotNull ObjVertexNormal o) {
    visitVectorElement(o);
  }

  public void visitVertexNormalIndex(@NotNull ObjVertexNormalIndex o) {
    visitIndexElement(o);
  }

  public void visitFlagElement(@NotNull ObjFlagElement o) {
    visitPsiElement(o);
  }

  public void visitGroupingElement(@NotNull ObjGroupingElement o) {
    visitPsiElement(o);
  }

  public void visitIndexElement(@NotNull ObjIndexElement o) {
    visitPsiElement(o);
  }

  public void visitObjectOrGroupIdentifierElement(@NotNull ObjObjectOrGroupIdentifierElement o) {
    visitPsiElement(o);
  }

  public void visitVectorElement(@NotNull ObjVectorElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
