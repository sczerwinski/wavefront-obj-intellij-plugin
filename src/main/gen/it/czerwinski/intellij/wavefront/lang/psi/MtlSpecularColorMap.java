// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface MtlSpecularColorMap extends PsiElement {

  @NotNull
  List<MtlBlendUOption> getBlendUOptionList();

  @NotNull
  List<MtlBlendVOption> getBlendVOptionList();

  @NotNull
  List<MtlClampOption> getClampOptionList();

  @NotNull
  List<MtlColorCorrectionOption> getColorCorrectionOptionList();

  @NotNull
  List<MtlOffsetOption> getOffsetOptionList();

  @NotNull
  List<MtlResolutionOption> getResolutionOptionList();

  @NotNull
  List<MtlScaleOption> getScaleOptionList();

  @NotNull
  List<MtlTurbulenceOption> getTurbulenceOptionList();

  @NotNull
  List<MtlValueModifierOption> getValueModifierOptionList();

}
