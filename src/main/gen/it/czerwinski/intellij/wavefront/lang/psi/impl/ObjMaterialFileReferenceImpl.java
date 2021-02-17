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
import com.intellij.psi.PsiReference;

public class ObjMaterialFileReferenceImpl extends ASTWrapperPsiElement implements ObjMaterialFileReference {

  public ObjMaterialFileReferenceImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull ObjVisitor visitor) {
    visitor.visitMaterialFileReference(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof ObjVisitor) accept((ObjVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public String getFilename() {
    return ObjPsiImplUtil.getFilename(this);
  }

  @Override
  @Nullable
  public MtlFile getMtlFile() {
    return ObjPsiImplUtil.getMtlFile(this);
  }

  @Override
  @NotNull
  public PsiReference[] getReferences() {
    return ObjPsiImplUtil.getReferences(this);
  }

}
