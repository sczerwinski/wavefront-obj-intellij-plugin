// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

public interface ObjMaterialFileReference extends PsiElement {

  @Nullable
  String getFilename();

  @Nullable
  MtlFile getMtlFile();

  @NotNull
  PsiReference[] getReferences();

}
