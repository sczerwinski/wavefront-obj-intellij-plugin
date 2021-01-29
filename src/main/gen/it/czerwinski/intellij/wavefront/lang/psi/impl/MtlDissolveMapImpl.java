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

public class MtlDissolveMapImpl extends MtlTextureElementImpl implements MtlDissolveMap {

  public MtlDissolveMapImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull MtlVisitor visitor) {
    visitor.visitDissolveMap(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof MtlVisitor) accept((MtlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<MtlBlendUOption> getBlendUOptionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlBlendUOption.class);
  }

  @Override
  @NotNull
  public List<MtlBlendVOption> getBlendVOptionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlBlendVOption.class);
  }

  @Override
  @NotNull
  public List<MtlClampOption> getClampOptionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlClampOption.class);
  }

  @Override
  @NotNull
  public List<MtlOffsetOption> getOffsetOptionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlOffsetOption.class);
  }

  @Override
  @NotNull
  public List<MtlResolutionOption> getResolutionOptionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlResolutionOption.class);
  }

  @Override
  @NotNull
  public List<MtlScalarChannelOption> getScalarChannelOptionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlScalarChannelOption.class);
  }

  @Override
  @NotNull
  public List<MtlScaleOption> getScaleOptionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlScaleOption.class);
  }

  @Override
  @NotNull
  public List<MtlTurbulenceOption> getTurbulenceOptionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlTurbulenceOption.class);
  }

  @Override
  @NotNull
  public List<MtlValueModifierOption> getValueModifierOptionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, MtlValueModifierOption.class);
  }

}
