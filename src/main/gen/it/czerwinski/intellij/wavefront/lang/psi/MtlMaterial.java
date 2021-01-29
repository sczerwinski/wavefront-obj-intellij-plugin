// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface MtlMaterial extends PsiElement {

  @NotNull
  List<MtlColor> getColorList();

  @NotNull
  List<MtlMap> getMapList();

  @NotNull
  List<MtlProperty> getPropertyList();

}
