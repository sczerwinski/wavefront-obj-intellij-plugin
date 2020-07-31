// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.language.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import it.czerwinski.intellij.wavefront.language.psi.impl.*;

public interface ObjTypes {

  IElementType FACE = new ObjElementType("FACE");
  IElementType GROUP = new ObjElementType("GROUP");
  IElementType OBJECT = new ObjElementType("OBJECT");
  IElementType TEXTURE_COORDINATES = new ObjElementType("TEXTURE_COORDINATES");
  IElementType TEXTURE_COORDINATES_INDEX = new ObjElementType("TEXTURE_COORDINATES_INDEX");
  IElementType VERTEX = new ObjElementType("VERTEX");
  IElementType VERTEX_INDEX = new ObjElementType("VERTEX_INDEX");
  IElementType VERTEX_NORMAL = new ObjElementType("VERTEX_NORMAL");
  IElementType VERTEX_NORMAL_INDEX = new ObjElementType("VERTEX_NORMAL_INDEX");

  IElementType COMMENT = new ObjTokenType("COMMENT");
  IElementType CRLF = new ObjTokenType("CRLF");
  IElementType FACE_KEYWORD = new ObjTokenType("FACE_KEYWORD");
  IElementType FLOAT = new ObjTokenType("FLOAT");
  IElementType GROUP_KEYWORD = new ObjTokenType("GROUP_KEYWORD");
  IElementType INDEX = new ObjTokenType("INDEX");
  IElementType OBJECT_KEYWORD = new ObjTokenType("OBJECT_KEYWORD");
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
      else if (type == GROUP) {
        return new ObjGroupImpl(node);
      }
      else if (type == OBJECT) {
        return new ObjObjectImpl(node);
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
