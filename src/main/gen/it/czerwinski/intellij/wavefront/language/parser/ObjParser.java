// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.language.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static it.czerwinski.intellij.wavefront.language.psi.ObjTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class ObjParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return objFile(b, l + 1);
  }

  /* ********************************************************** */
  // FACE_KEYWORD faceVertex faceVertex faceVertex + (faceVertex)*
  public static boolean face(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "face")) return false;
    if (!nextTokenIs(b, FACE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, FACE_KEYWORD);
    r = r && faceVertex(b, l + 1);
    r = r && faceVertex(b, l + 1);
    r = r && face_3(b, l + 1);
    r = r && face_4(b, l + 1);
    exit_section_(b, m, FACE, r);
    return r;
  }

  // faceVertex +
  private static boolean face_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "face_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = faceVertex(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!faceVertex(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "face_3", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // (faceVertex)*
  private static boolean face_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "face_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!face_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "face_4", c)) break;
    }
    return true;
  }

  // (faceVertex)
  private static boolean face_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "face_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = faceVertex(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (vertexIndex VERTEX_INDEX_SEPARATOR textureCoordinatesIndex? VERTEX_INDEX_SEPARATOR vertexNormalIndex)
  //   | (vertexIndex VERTEX_INDEX_SEPARATOR textureCoordinatesIndex)
  //   | (vertexIndex)
  static boolean faceVertex(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "faceVertex")) return false;
    if (!nextTokenIs(b, INDEX)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = faceVertex_0(b, l + 1);
    if (!r) r = faceVertex_1(b, l + 1);
    if (!r) r = faceVertex_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // vertexIndex VERTEX_INDEX_SEPARATOR textureCoordinatesIndex? VERTEX_INDEX_SEPARATOR vertexNormalIndex
  private static boolean faceVertex_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "faceVertex_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = vertexIndex(b, l + 1);
    r = r && consumeToken(b, VERTEX_INDEX_SEPARATOR);
    r = r && faceVertex_0_2(b, l + 1);
    r = r && consumeToken(b, VERTEX_INDEX_SEPARATOR);
    r = r && vertexNormalIndex(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // textureCoordinatesIndex?
  private static boolean faceVertex_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "faceVertex_0_2")) return false;
    textureCoordinatesIndex(b, l + 1);
    return true;
  }

  // vertexIndex VERTEX_INDEX_SEPARATOR textureCoordinatesIndex
  private static boolean faceVertex_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "faceVertex_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = vertexIndex(b, l + 1);
    r = r && consumeToken(b, VERTEX_INDEX_SEPARATOR);
    r = r && textureCoordinatesIndex(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (vertexIndex)
  private static boolean faceVertex_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "faceVertex_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = vertexIndex(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (GROUP_KEYWORD STRING) + item_*
  public static boolean group(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "group")) return false;
    if (!nextTokenIs(b, GROUP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = group_0(b, l + 1);
    r = r && group_1(b, l + 1);
    exit_section_(b, m, GROUP, r);
    return r;
  }

  // (GROUP_KEYWORD STRING) +
  private static boolean group_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "group_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = group_0_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!group_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "group_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // GROUP_KEYWORD STRING
  private static boolean group_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "group_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, GROUP_KEYWORD, STRING);
    exit_section_(b, m, null, r);
    return r;
  }

  // item_*
  private static boolean group_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "group_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "group_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // object|group
  static boolean groupingItem_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "groupingItem_")) return false;
    if (!nextTokenIs(b, "", GROUP_KEYWORD, OBJECT_KEYWORD)) return false;
    boolean r;
    r = object(b, l + 1);
    if (!r) r = group(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // vertex|textureCoordinates|vertexNormal|point|line|face|COMMENT|CRLF
  static boolean item_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_")) return false;
    boolean r;
    r = vertex(b, l + 1);
    if (!r) r = textureCoordinates(b, l + 1);
    if (!r) r = vertexNormal(b, l + 1);
    if (!r) r = point(b, l + 1);
    if (!r) r = line(b, l + 1);
    if (!r) r = face(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, CRLF);
    return r;
  }

  /* ********************************************************** */
  // LINE_KEYWORD vertexIndex vertexIndex + (vertexIndex)*
  public static boolean line(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "line")) return false;
    if (!nextTokenIs(b, LINE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LINE_KEYWORD);
    r = r && vertexIndex(b, l + 1);
    r = r && line_2(b, l + 1);
    r = r && line_3(b, l + 1);
    exit_section_(b, m, LINE, r);
    return r;
  }

  // vertexIndex +
  private static boolean line_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "line_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = vertexIndex(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!vertexIndex(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "line_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // (vertexIndex)*
  private static boolean line_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "line_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!line_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "line_3", c)) break;
    }
    return true;
  }

  // (vertexIndex)
  private static boolean line_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "line_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = vertexIndex(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (groupingItem_|item_)*
  static boolean objFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "objFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!objFile_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "objFile", c)) break;
    }
    return true;
  }

  // groupingItem_|item_
  private static boolean objFile_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "objFile_0")) return false;
    boolean r;
    r = groupingItem_(b, l + 1);
    if (!r) r = item_(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // (OBJECT_KEYWORD STRING) + item_*
  public static boolean object(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object")) return false;
    if (!nextTokenIs(b, OBJECT_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = object_0(b, l + 1);
    r = r && object_1(b, l + 1);
    exit_section_(b, m, OBJECT, r);
    return r;
  }

  // (OBJECT_KEYWORD STRING) +
  private static boolean object_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = object_0_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!object_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "object_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // OBJECT_KEYWORD STRING
  private static boolean object_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, OBJECT_KEYWORD, STRING);
    exit_section_(b, m, null, r);
    return r;
  }

  // item_*
  private static boolean object_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "object_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // POINT_KEYWORD vertexIndex
  public static boolean point(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "point")) return false;
    if (!nextTokenIs(b, POINT_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, POINT_KEYWORD);
    r = r && vertexIndex(b, l + 1);
    exit_section_(b, m, POINT, r);
    return r;
  }

  /* ********************************************************** */
  // TEXTURE_COORDINATES_KEYWORD FLOAT FLOAT? FLOAT?
  public static boolean textureCoordinates(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "textureCoordinates")) return false;
    if (!nextTokenIs(b, TEXTURE_COORDINATES_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, TEXTURE_COORDINATES_KEYWORD, FLOAT);
    r = r && textureCoordinates_2(b, l + 1);
    r = r && textureCoordinates_3(b, l + 1);
    exit_section_(b, m, TEXTURE_COORDINATES, r);
    return r;
  }

  // FLOAT?
  private static boolean textureCoordinates_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "textureCoordinates_2")) return false;
    consumeToken(b, FLOAT);
    return true;
  }

  // FLOAT?
  private static boolean textureCoordinates_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "textureCoordinates_3")) return false;
    consumeToken(b, FLOAT);
    return true;
  }

  /* ********************************************************** */
  // (INDEX)
  public static boolean textureCoordinatesIndex(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "textureCoordinatesIndex")) return false;
    if (!nextTokenIs(b, INDEX)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INDEX);
    exit_section_(b, m, TEXTURE_COORDINATES_INDEX, r);
    return r;
  }

  /* ********************************************************** */
  // VERTEX_KEYWORD FLOAT FLOAT FLOAT FLOAT?
  public static boolean vertex(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "vertex")) return false;
    if (!nextTokenIs(b, VERTEX_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VERTEX_KEYWORD, FLOAT, FLOAT, FLOAT);
    r = r && vertex_4(b, l + 1);
    exit_section_(b, m, VERTEX, r);
    return r;
  }

  // FLOAT?
  private static boolean vertex_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "vertex_4")) return false;
    consumeToken(b, FLOAT);
    return true;
  }

  /* ********************************************************** */
  // (INDEX)
  public static boolean vertexIndex(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "vertexIndex")) return false;
    if (!nextTokenIs(b, INDEX)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INDEX);
    exit_section_(b, m, VERTEX_INDEX, r);
    return r;
  }

  /* ********************************************************** */
  // VERTEX_NORMAL_KEYWORD FLOAT FLOAT FLOAT
  public static boolean vertexNormal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "vertexNormal")) return false;
    if (!nextTokenIs(b, VERTEX_NORMAL_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VERTEX_NORMAL_KEYWORD, FLOAT, FLOAT, FLOAT);
    exit_section_(b, m, VERTEX_NORMAL, r);
    return r;
  }

  /* ********************************************************** */
  // (INDEX)
  public static boolean vertexNormalIndex(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "vertexNormalIndex")) return false;
    if (!nextTokenIs(b, INDEX)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, INDEX);
    exit_section_(b, m, VERTEX_NORMAL_INDEX, r);
    return r;
  }

}
