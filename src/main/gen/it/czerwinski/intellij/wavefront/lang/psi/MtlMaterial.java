// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface MtlMaterial extends MtlMaterialElement {

  @NotNull
  List<MtlAmbientColor> getAmbientColorList();

  @NotNull
  List<MtlAmbientColorMap> getAmbientColorMapList();

  @NotNull
  List<MtlBumpMap> getBumpMapList();

  @NotNull
  List<MtlDiffuseColor> getDiffuseColorList();

  @NotNull
  List<MtlDiffuseColorMap> getDiffuseColorMapList();

  @NotNull
  List<MtlDisplacementMap> getDisplacementMapList();

  @NotNull
  List<MtlDissolve> getDissolveList();

  @NotNull
  List<MtlDissolveMap> getDissolveMapList();

  @NotNull
  List<MtlIllumination> getIlluminationList();

  @NotNull
  List<MtlOpticalDensity> getOpticalDensityList();

  @NotNull
  List<MtlReflectionMap> getReflectionMapList();

  @NotNull
  List<MtlSharpness> getSharpnessList();

  @NotNull
  List<MtlSpecularColor> getSpecularColorList();

  @NotNull
  List<MtlSpecularColorMap> getSpecularColorMapList();

  @NotNull
  List<MtlSpecularExponent> getSpecularExponentList();

  @NotNull
  List<MtlSpecularExponentMap> getSpecularExponentMapList();

  @NotNull
  List<MtlStencilDecalMap> getStencilDecalMapList();

  @NotNull
  List<MtlTransmissionFilter> getTransmissionFilterList();

}
