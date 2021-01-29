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

public class MtlMaterialImpl extends ASTWrapperPsiElement implements MtlMaterial {

  public MtlMaterialImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MtlVisitor visitor) {
    visitor.visitMaterial(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MtlVisitor) accept((MtlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<MtlAmbientColor> getAmbientColorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlAmbientColor.class);
  }

  @Override
  @NotNull
  public List<MtlAmbientColorMap> getAmbientColorMapList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlAmbientColorMap.class);
  }

  @Override
  @NotNull
  public List<MtlBumpMap> getBumpMapList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlBumpMap.class);
  }

  @Override
  @NotNull
  public List<MtlDiffuseColor> getDiffuseColorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlDiffuseColor.class);
  }

  @Override
  @NotNull
  public List<MtlDiffuseColorMap> getDiffuseColorMapList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlDiffuseColorMap.class);
  }

  @Override
  @NotNull
  public List<MtlDisplacementMap> getDisplacementMapList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlDisplacementMap.class);
  }

  @Override
  @NotNull
  public List<MtlDissolve> getDissolveList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlDissolve.class);
  }

  @Override
  @NotNull
  public List<MtlDissolveMap> getDissolveMapList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlDissolveMap.class);
  }

  @Override
  @NotNull
  public List<MtlIllumination> getIlluminationList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlIllumination.class);
  }

  @Override
  @NotNull
  public List<MtlOpticalDensity> getOpticalDensityList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlOpticalDensity.class);
  }

  @Override
  @NotNull
  public List<MtlReflectionMap> getReflectionMapList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlReflectionMap.class);
  }

  @Override
  @NotNull
  public List<MtlSharpness> getSharpnessList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlSharpness.class);
  }

  @Override
  @NotNull
  public List<MtlSpecularColor> getSpecularColorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlSpecularColor.class);
  }

  @Override
  @NotNull
  public List<MtlSpecularColorMap> getSpecularColorMapList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlSpecularColorMap.class);
  }

  @Override
  @NotNull
  public List<MtlSpecularExponent> getSpecularExponentList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlSpecularExponent.class);
  }

  @Override
  @NotNull
  public List<MtlSpecularExponentMap> getSpecularExponentMapList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlSpecularExponentMap.class);
  }

  @Override
  @NotNull
  public List<MtlStencilDecalMap> getStencilDecalMapList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlStencilDecalMap.class);
  }

  @Override
  @NotNull
  public List<MtlTransmissionFilter> getTransmissionFilterList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlTransmissionFilter.class);
  }

}
