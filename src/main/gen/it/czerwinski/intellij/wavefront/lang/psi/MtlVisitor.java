// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class MtlVisitor extends PsiElementVisitor {

  public void visitAmbientColor(@NotNull MtlAmbientColor o) {
    visitColorElement(o);
  }

  public void visitAmbientColorMap(@NotNull MtlAmbientColorMap o) {
    visitTextureElement(o);
  }

  public void visitBlendUOption(@NotNull MtlBlendUOption o) {
    visitFlagValueElement(o);
  }

  public void visitBlendVOption(@NotNull MtlBlendVOption o) {
    visitFlagValueElement(o);
  }

  public void visitBumpMap(@NotNull MtlBumpMap o) {
    visitTextureElement(o);
  }

  public void visitBumpMultiplierOption(@NotNull MtlBumpMultiplierOption o) {
    visitFloatValueElement(o);
  }

  public void visitClampOption(@NotNull MtlClampOption o) {
    visitFlagValueElement(o);
  }

  public void visitColorCorrectionOption(@NotNull MtlColorCorrectionOption o) {
    visitFlagValueElement(o);
  }

  public void visitDiffuseColor(@NotNull MtlDiffuseColor o) {
    visitColorElement(o);
  }

  public void visitDiffuseColorMap(@NotNull MtlDiffuseColorMap o) {
    visitTextureElement(o);
  }

  public void visitDisplacementMap(@NotNull MtlDisplacementMap o) {
    visitTextureElement(o);
  }

  public void visitDissolve(@NotNull MtlDissolve o) {
    visitFloatValueElement(o);
  }

  public void visitDissolveMap(@NotNull MtlDissolveMap o) {
    visitTextureElement(o);
  }

  public void visitIllumination(@NotNull MtlIllumination o) {
    visitIlluminationValueElement(o);
  }

  public void visitMaterial(@NotNull MtlMaterial o) {
    visitMaterialElement(o);
  }

  public void visitOffsetOption(@NotNull MtlOffsetOption o) {
    visitFloatVectorValueElement(o);
  }

  public void visitOpticalDensity(@NotNull MtlOpticalDensity o) {
    visitFloatValueElement(o);
  }

  public void visitReflectionMap(@NotNull MtlReflectionMap o) {
    visitTextureElement(o);
  }

  public void visitReflectionTypeOption(@NotNull MtlReflectionTypeOption o) {
    visitReflectionTypeValueElement(o);
  }

  public void visitResolutionOption(@NotNull MtlResolutionOption o) {
    visitIntegerValueElement(o);
  }

  public void visitScalarChannelOption(@NotNull MtlScalarChannelOption o) {
    visitScalarChannelValueElement(o);
  }

  public void visitScaleOption(@NotNull MtlScaleOption o) {
    visitFloatVectorValueElement(o);
  }

  public void visitSharpness(@NotNull MtlSharpness o) {
    visitFloatValueElement(o);
  }

  public void visitSpecularColor(@NotNull MtlSpecularColor o) {
    visitColorElement(o);
  }

  public void visitSpecularColorMap(@NotNull MtlSpecularColorMap o) {
    visitTextureElement(o);
  }

  public void visitSpecularExponent(@NotNull MtlSpecularExponent o) {
    visitFloatValueElement(o);
  }

  public void visitSpecularExponentMap(@NotNull MtlSpecularExponentMap o) {
    visitTextureElement(o);
  }

  public void visitStencilDecalMap(@NotNull MtlStencilDecalMap o) {
    visitTextureElement(o);
  }

  public void visitTransmissionFilter(@NotNull MtlTransmissionFilter o) {
    visitColorElement(o);
  }

  public void visitTurbulenceOption(@NotNull MtlTurbulenceOption o) {
    visitFloatVectorValueElement(o);
  }

  public void visitValueModifierOption(@NotNull MtlValueModifierOption o) {
    visitFloatVectorValueElement(o);
  }

  public void visitColorElement(@NotNull MtlColorElement o) {
    visitPsiElement(o);
  }

  public void visitFlagValueElement(@NotNull MtlFlagValueElement o) {
    visitPsiElement(o);
  }

  public void visitFloatValueElement(@NotNull MtlFloatValueElement o) {
    visitPsiElement(o);
  }

  public void visitFloatVectorValueElement(@NotNull MtlFloatVectorValueElement o) {
    visitPsiElement(o);
  }

  public void visitIlluminationValueElement(@NotNull MtlIlluminationValueElement o) {
    visitPsiElement(o);
  }

  public void visitIntegerValueElement(@NotNull MtlIntegerValueElement o) {
    visitPsiElement(o);
  }

  public void visitMaterialElement(@NotNull MtlMaterialElement o) {
    visitPsiElement(o);
  }

  public void visitReflectionTypeValueElement(@NotNull MtlReflectionTypeValueElement o) {
    visitPsiElement(o);
  }

  public void visitScalarChannelValueElement(@NotNull MtlScalarChannelValueElement o) {
    visitPsiElement(o);
  }

  public void visitTextureElement(@NotNull MtlTextureElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
