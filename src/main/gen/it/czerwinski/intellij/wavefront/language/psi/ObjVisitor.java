// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.language.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class ObjVisitor extends PsiElementVisitor {

  public void visitFace(@NotNull ObjFace o) {
    visitPsiElement(o);
  }

  public void visitGroup(@NotNull ObjGroup o) {
    visitGroupingElement(o);
  }

  public void visitLine(@NotNull ObjLine o) {
    visitPsiElement(o);
  }

  public void visitObject(@NotNull ObjObject o) {
    visitGroupingElement(o);
  }

  public void visitPoint(@NotNull ObjPoint o) {
    visitPsiElement(o);
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

  public void visitGroupingElement(@NotNull ObjGroupingElement o) {
    visitPsiElement(o);
  }

  public void visitIndexElement(@NotNull ObjIndexElement o) {
    visitPsiElement(o);
  }

  public void visitVectorElement(@NotNull ObjVectorElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
