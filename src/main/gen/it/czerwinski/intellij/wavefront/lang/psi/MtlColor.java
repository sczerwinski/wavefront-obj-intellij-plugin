// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface MtlColor extends PsiElement {

  @Nullable
  MtlAmbientColor getAmbientColor();

  @Nullable
  MtlDiffuseColor getDiffuseColor();

  @Nullable
  MtlSpecularColor getSpecularColor();

  @Nullable
  MtlTransmissionFilter getTransmissionFilter();

}
