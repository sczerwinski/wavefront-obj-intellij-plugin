// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.language.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface ObjGroup extends PsiElement {

  @NotNull
  List<ObjFace> getFaceList();

  @NotNull
  List<ObjTextureCoordinates> getTextureCoordinatesList();

  @NotNull
  List<ObjVertex> getVertexList();

  @NotNull
  List<ObjVertexNormal> getVertexNormalList();

}
