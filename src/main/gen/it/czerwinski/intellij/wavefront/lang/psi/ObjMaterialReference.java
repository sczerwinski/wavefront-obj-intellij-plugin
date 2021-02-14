// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

public interface ObjMaterialReference extends PsiElement {

  @Nullable
  String getMaterialName();

  @Nullable
  MtlMaterial getMaterial();

  @NotNull
  PsiReference[] getReferences();

}
