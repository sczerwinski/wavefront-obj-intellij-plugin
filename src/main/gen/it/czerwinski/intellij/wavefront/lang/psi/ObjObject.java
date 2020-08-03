// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ObjObject extends ObjGroupingElement {

  @NotNull
  List<ObjFace> getFaceList();

  @NotNull
  List<ObjLine> getLineList();

  @NotNull
  List<ObjMaterialFileReference> getMaterialFileReferenceList();

  @NotNull
  List<ObjMaterialReference> getMaterialReferenceList();

  @NotNull
  List<ObjPoint> getPointList();

  @NotNull
  List<ObjSmoothShading> getSmoothShadingList();

  @NotNull
  List<ObjTextureCoordinates> getTextureCoordinatesList();

  @NotNull
  List<ObjVertex> getVertexList();

  @NotNull
  List<ObjVertexNormal> getVertexNormalList();

}
