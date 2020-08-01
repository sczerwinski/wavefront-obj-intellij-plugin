// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import it.czerwinski.intellij.wavefront.language.psi.impl.*;

public interface ObjTypes {

  IElementType FACE = new ObjElementType("FACE");
  IElementType FACE_VERTEX = new ObjElementType("FACE_VERTEX");
  IElementType GROUP = new ObjElementType("GROUP");
  IElementType LINE = new ObjElementType("LINE");
  IElementType OBJECT = new ObjElementType("OBJECT");
  IElementType POINT = new ObjElementType("POINT");
  IElementType SMOOTH_SHADING = new ObjElementType("SMOOTH_SHADING");
  IElementType TEXTURE_COORDINATES = new ObjElementType("TEXTURE_COORDINATES");
  IElementType TEXTURE_COORDINATES_INDEX = new ObjElementType("TEXTURE_COORDINATES_INDEX");
  IElementType VERTEX = new ObjElementType("VERTEX");
  IElementType VERTEX_INDEX = new ObjElementType("VERTEX_INDEX");
  IElementType VERTEX_NORMAL = new ObjElementType("VERTEX_NORMAL");
  IElementType VERTEX_NORMAL_INDEX = new ObjElementType("VERTEX_NORMAL_INDEX");

  IElementType COMMENT = new ObjTokenType("COMMENT");
  IElementType CRLF = new ObjTokenType("CRLF");
  IElementType FACE_KEYWORD = new ObjTokenType("FACE_KEYWORD");
  IElementType FLAG = new ObjTokenType("FLAG");
  IElementType FLOAT = new ObjTokenType("FLOAT");
  IElementType GROUP_KEYWORD = new ObjTokenType("GROUP_KEYWORD");
  IElementType INDEX = new ObjTokenType("INDEX");
  IElementType LINE_KEYWORD = new ObjTokenType("LINE_KEYWORD");
  IElementType OBJECT_KEYWORD = new ObjTokenType("OBJECT_KEYWORD");
  IElementType POINT_KEYWORD = new ObjTokenType("POINT_KEYWORD");
  IElementType SMOOTH_SHADING_KEYWORD = new ObjTokenType("SMOOTH_SHADING_KEYWORD");
  IElementType STRING = new ObjTokenType("STRING");
  IElementType TEXTURE_COORDINATES_KEYWORD = new ObjTokenType("TEXTURE_COORDINATES_KEYWORD");
  IElementType VERTEX_INDEX_SEPARATOR = new ObjTokenType("VERTEX_INDEX_SEPARATOR");
  IElementType VERTEX_KEYWORD = new ObjTokenType("VERTEX_KEYWORD");
  IElementType VERTEX_NORMAL_KEYWORD = new ObjTokenType("VERTEX_NORMAL_KEYWORD");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == FACE) {
        return new ObjFaceImpl(node);
      }
      else if (type == FACE_VERTEX) {
        return new ObjFaceVertexImpl(node);
      }
      else if (type == GROUP) {
        return new ObjGroupImpl(node);
      }
      else if (type == LINE) {
        return new ObjLineImpl(node);
      }
      else if (type == OBJECT) {
        return new ObjObjectImpl(node);
      }
      else if (type == POINT) {
        return new ObjPointImpl(node);
      }
      else if (type == SMOOTH_SHADING) {
        return new ObjSmoothShadingImpl(node);
      }
      else if (type == TEXTURE_COORDINATES) {
        return new ObjTextureCoordinatesImpl(node);
      }
      else if (type == TEXTURE_COORDINATES_INDEX) {
        return new ObjTextureCoordinatesIndexImpl(node);
      }
      else if (type == VERTEX) {
        return new ObjVertexImpl(node);
      }
      else if (type == VERTEX_INDEX) {
        return new ObjVertexIndexImpl(node);
      }
      else if (type == VERTEX_NORMAL) {
        return new ObjVertexNormalImpl(node);
      }
      else if (type == VERTEX_NORMAL_INDEX) {
        return new ObjVertexNormalIndexImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
