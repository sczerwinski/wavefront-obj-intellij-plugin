// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static it.czerwinski.intellij.wavefront.lang.psi.MtlTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class MtlParser implements PsiParser, LightPsiParser {

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
    return mtlFile(b, l + 1);
  }

  /* ********************************************************** */
  // AMBIENT_COLOR_KEYWORD FLOAT FLOAT FLOAT
  public static boolean ambientColor(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ambientColor")) return false;
    if (!nextTokenIs(b, AMBIENT_COLOR_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, AMBIENT_COLOR_KEYWORD, FLOAT, FLOAT, FLOAT);
    exit_section_(b, m, AMBIENT_COLOR, r);
    return r;
  }

  /* ********************************************************** */
  // AMBIENT_COLOR_MAP_KEYWORD colorMapOptions TEXTURE_FILE
  public static boolean ambientColorMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ambientColorMap")) return false;
    if (!nextTokenIs(b, AMBIENT_COLOR_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AMBIENT_COLOR_MAP_KEYWORD);
    r = r && colorMapOptions(b, l + 1);
    r = r && consumeToken(b, TEXTURE_FILE);
    exit_section_(b, m, AMBIENT_COLOR_MAP, r);
    return r;
  }

  /* ********************************************************** */
  // BLEND_U_OPTION_NAME FLAG
  public static boolean blendUOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "blendUOption")) return false;
    if (!nextTokenIs(b, BLEND_U_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, BLEND_U_OPTION_NAME, FLAG);
    exit_section_(b, m, BLEND_U_OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // BLEND_V_OPTION_NAME FLAG
  public static boolean blendVOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "blendVOption")) return false;
    if (!nextTokenIs(b, BLEND_V_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, BLEND_V_OPTION_NAME, FLAG);
    exit_section_(b, m, BLEND_V_OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // BUMP_MAP_KEYWORD bumpMapOptions TEXTURE_FILE
  public static boolean bumpMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bumpMap")) return false;
    if (!nextTokenIs(b, BUMP_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BUMP_MAP_KEYWORD);
    r = r && bumpMapOptions(b, l + 1);
    r = r && consumeToken(b, TEXTURE_FILE);
    exit_section_(b, m, BUMP_MAP, r);
    return r;
  }

  /* ********************************************************** */
  // (
  //     blendUOption | blendVOption
  //     | clampOption
  //     | scalarChannelOption
  //     | valueModifierOption
  //     | offsetOption | scaleOption | turbulenceOption
  //     | resolutionOption
  //     | bumpMultiplier
  // )*
  public static boolean bumpMapOptions(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bumpMapOptions")) return false;
    Marker m = enter_section_(b, l, _NONE_, BUMP_MAP_OPTIONS, "<bump map options>");
    while (true) {
      int c = current_position_(b);
      if (!bumpMapOptions_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bumpMapOptions", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // blendUOption | blendVOption
  //     | clampOption
  //     | scalarChannelOption
  //     | valueModifierOption
  //     | offsetOption | scaleOption | turbulenceOption
  //     | resolutionOption
  //     | bumpMultiplier
  private static boolean bumpMapOptions_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bumpMapOptions_0")) return false;
    boolean r;
    r = blendUOption(b, l + 1);
    if (!r) r = blendVOption(b, l + 1);
    if (!r) r = clampOption(b, l + 1);
    if (!r) r = scalarChannelOption(b, l + 1);
    if (!r) r = valueModifierOption(b, l + 1);
    if (!r) r = offsetOption(b, l + 1);
    if (!r) r = scaleOption(b, l + 1);
    if (!r) r = turbulenceOption(b, l + 1);
    if (!r) r = resolutionOption(b, l + 1);
    if (!r) r = bumpMultiplier(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // BUMP_MULTIPLIER_OPTION_NAME FLOAT
  public static boolean bumpMultiplier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bumpMultiplier")) return false;
    if (!nextTokenIs(b, BUMP_MULTIPLIER_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, BUMP_MULTIPLIER_OPTION_NAME, FLOAT);
    exit_section_(b, m, BUMP_MULTIPLIER, r);
    return r;
  }

  /* ********************************************************** */
  // CLAMP_OPTION_NAME FLAG
  public static boolean clampOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "clampOption")) return false;
    if (!nextTokenIs(b, CLAMP_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, CLAMP_OPTION_NAME, FLAG);
    exit_section_(b, m, CLAMP_OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // ambientColor | diffuseColor | specularColor | transmissionFilter
  public static boolean color(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "color")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COLOR, "<color>");
    r = ambientColor(b, l + 1);
    if (!r) r = diffuseColor(b, l + 1);
    if (!r) r = specularColor(b, l + 1);
    if (!r) r = transmissionFilter(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // COLOR_CORRECTION_OPTION_NAME FLAG
  public static boolean colorCorrectionOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "colorCorrectionOption")) return false;
    if (!nextTokenIs(b, COLOR_CORRECTION_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COLOR_CORRECTION_OPTION_NAME, FLAG);
    exit_section_(b, m, COLOR_CORRECTION_OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // ambientColorMap | diffuseColorMap | specularColorMap
  public static boolean colorMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "colorMap")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, COLOR_MAP, "<color map>");
    r = ambientColorMap(b, l + 1);
    if (!r) r = diffuseColorMap(b, l + 1);
    if (!r) r = specularColorMap(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (
  //     blendUOption | blendVOption
  //     | colorCorrectionOption | clampOption
  //     | valueModifierOption
  //     | offsetOption | scaleOption | turbulenceOption
  //     | resolutionOption
  // )*
  public static boolean colorMapOptions(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "colorMapOptions")) return false;
    Marker m = enter_section_(b, l, _NONE_, COLOR_MAP_OPTIONS, "<color map options>");
    while (true) {
      int c = current_position_(b);
      if (!colorMapOptions_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "colorMapOptions", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // blendUOption | blendVOption
  //     | colorCorrectionOption | clampOption
  //     | valueModifierOption
  //     | offsetOption | scaleOption | turbulenceOption
  //     | resolutionOption
  private static boolean colorMapOptions_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "colorMapOptions_0")) return false;
    boolean r;
    r = blendUOption(b, l + 1);
    if (!r) r = blendVOption(b, l + 1);
    if (!r) r = colorCorrectionOption(b, l + 1);
    if (!r) r = clampOption(b, l + 1);
    if (!r) r = valueModifierOption(b, l + 1);
    if (!r) r = offsetOption(b, l + 1);
    if (!r) r = scaleOption(b, l + 1);
    if (!r) r = turbulenceOption(b, l + 1);
    if (!r) r = resolutionOption(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // DIFFUSE_COLOR_KEYWORD FLOAT FLOAT FLOAT
  public static boolean diffuseColor(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "diffuseColor")) return false;
    if (!nextTokenIs(b, DIFFUSE_COLOR_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DIFFUSE_COLOR_KEYWORD, FLOAT, FLOAT, FLOAT);
    exit_section_(b, m, DIFFUSE_COLOR, r);
    return r;
  }

  /* ********************************************************** */
  // DIFFUSE_COLOR_MAP_KEYWORD colorMapOptions TEXTURE_FILE
  public static boolean diffuseColorMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "diffuseColorMap")) return false;
    if (!nextTokenIs(b, DIFFUSE_COLOR_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DIFFUSE_COLOR_MAP_KEYWORD);
    r = r && colorMapOptions(b, l + 1);
    r = r && consumeToken(b, TEXTURE_FILE);
    exit_section_(b, m, DIFFUSE_COLOR_MAP, r);
    return r;
  }

  /* ********************************************************** */
  // DISPLACEMENT_MAP_KEYWORD scalarMapOptions TEXTURE_FILE
  public static boolean displacementMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "displacementMap")) return false;
    if (!nextTokenIs(b, DISPLACEMENT_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DISPLACEMENT_MAP_KEYWORD);
    r = r && scalarMapOptions(b, l + 1);
    r = r && consumeToken(b, TEXTURE_FILE);
    exit_section_(b, m, DISPLACEMENT_MAP, r);
    return r;
  }

  /* ********************************************************** */
  // DISSOLVE_KEYWORD FLOAT
  public static boolean dissolve(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dissolve")) return false;
    if (!nextTokenIs(b, DISSOLVE_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, DISSOLVE_KEYWORD, FLOAT);
    exit_section_(b, m, DISSOLVE, r);
    return r;
  }

  /* ********************************************************** */
  // DISSOLVE_MAP_KEYWORD scalarMapOptions TEXTURE_FILE
  public static boolean dissolveMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dissolveMap")) return false;
    if (!nextTokenIs(b, DISSOLVE_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DISSOLVE_MAP_KEYWORD);
    r = r && scalarMapOptions(b, l + 1);
    r = r && consumeToken(b, TEXTURE_FILE);
    exit_section_(b, m, DISSOLVE_MAP, r);
    return r;
  }

  /* ********************************************************** */
  // ILLUMINATION_KEYWORD ILLUMINATION_VALUE
  public static boolean illumination(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "illumination")) return false;
    if (!nextTokenIs(b, ILLUMINATION_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ILLUMINATION_KEYWORD, ILLUMINATION_VALUE);
    exit_section_(b, m, ILLUMINATION, r);
    return r;
  }

  /* ********************************************************** */
  // color | property | map
  //   | COMMENT | CRLF
  static boolean item_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_")) return false;
    boolean r;
    r = color(b, l + 1);
    if (!r) r = property(b, l + 1);
    if (!r) r = map(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, CRLF);
    return r;
  }

  /* ********************************************************** */
  // colorMap | scalarMap | bumpMap | reflectionMap
  public static boolean map(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MAP, "<map>");
    r = colorMap(b, l + 1);
    if (!r) r = scalarMap(b, l + 1);
    if (!r) r = bumpMap(b, l + 1);
    if (!r) r = reflectionMap(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (NEW_MATERIAL_KEYWORD MATERIAL_NAME) + item_*
  public static boolean material(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "material")) return false;
    if (!nextTokenIs(b, NEW_MATERIAL_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = material_0(b, l + 1);
    r = r && material_1(b, l + 1);
    exit_section_(b, m, MATERIAL, r);
    return r;
  }

  // (NEW_MATERIAL_KEYWORD MATERIAL_NAME) +
  private static boolean material_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "material_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = material_0_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!material_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "material_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // NEW_MATERIAL_KEYWORD MATERIAL_NAME
  private static boolean material_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "material_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, NEW_MATERIAL_KEYWORD, MATERIAL_NAME);
    exit_section_(b, m, null, r);
    return r;
  }

  // item_*
  private static boolean material_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "material_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "material_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // material*
  static boolean mtlFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mtlFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!material(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "mtlFile", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // OFFSET_OPTION_NAME FLOAT FLOAT FLOAT
  public static boolean offsetOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "offsetOption")) return false;
    if (!nextTokenIs(b, OFFSET_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, OFFSET_OPTION_NAME, FLOAT, FLOAT, FLOAT);
    exit_section_(b, m, OFFSET_OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // OPTICAL_DENSITY_KEYWORD FLOAT
  public static boolean opticalDensity(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "opticalDensity")) return false;
    if (!nextTokenIs(b, OPTICAL_DENSITY_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, OPTICAL_DENSITY_KEYWORD, FLOAT);
    exit_section_(b, m, OPTICAL_DENSITY, r);
    return r;
  }

  /* ********************************************************** */
  // illumination | dissolve | specularExponent | sharpness | opticalDensity
  public static boolean property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PROPERTY, "<property>");
    r = illumination(b, l + 1);
    if (!r) r = dissolve(b, l + 1);
    if (!r) r = specularExponent(b, l + 1);
    if (!r) r = sharpness(b, l + 1);
    if (!r) r = opticalDensity(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // REFLECTION_MAP_KEYWORD reflectionMapOptions TEXTURE_FILE
  public static boolean reflectionMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reflectionMap")) return false;
    if (!nextTokenIs(b, REFLECTION_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, REFLECTION_MAP_KEYWORD);
    r = r && reflectionMapOptions(b, l + 1);
    r = r && consumeToken(b, TEXTURE_FILE);
    exit_section_(b, m, REFLECTION_MAP, r);
    return r;
  }

  /* ********************************************************** */
  // reflectionTypeOption + colorMapOptions
  public static boolean reflectionMapOptions(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reflectionMapOptions")) return false;
    if (!nextTokenIs(b, REFLECTION_TYPE_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = reflectionMapOptions_0(b, l + 1);
    r = r && colorMapOptions(b, l + 1);
    exit_section_(b, m, REFLECTION_MAP_OPTIONS, r);
    return r;
  }

  // reflectionTypeOption +
  private static boolean reflectionMapOptions_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reflectionMapOptions_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = reflectionTypeOption(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!reflectionTypeOption(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "reflectionMapOptions_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // REFLECTION_TYPE_OPTION_NAME REFLECTION_TYPE
  public static boolean reflectionTypeOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reflectionTypeOption")) return false;
    if (!nextTokenIs(b, REFLECTION_TYPE_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, REFLECTION_TYPE_OPTION_NAME, REFLECTION_TYPE);
    exit_section_(b, m, REFLECTION_TYPE_OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // RESOLUTION_OPTION_NAME INTEGER
  public static boolean resolutionOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "resolutionOption")) return false;
    if (!nextTokenIs(b, RESOLUTION_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, RESOLUTION_OPTION_NAME, INTEGER);
    exit_section_(b, m, RESOLUTION_OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // SCALAR_CHANNEL_OPTION_NAME SCALAR_CHANNEL
  public static boolean scalarChannelOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalarChannelOption")) return false;
    if (!nextTokenIs(b, SCALAR_CHANNEL_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, SCALAR_CHANNEL_OPTION_NAME, SCALAR_CHANNEL);
    exit_section_(b, m, SCALAR_CHANNEL_OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // specularExponentMap | dissolveMap | displacementMap | stencilDecalMap
  public static boolean scalarMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalarMap")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SCALAR_MAP, "<scalar map>");
    r = specularExponentMap(b, l + 1);
    if (!r) r = dissolveMap(b, l + 1);
    if (!r) r = displacementMap(b, l + 1);
    if (!r) r = stencilDecalMap(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (
  //     blendUOption | blendVOption
  //     | clampOption
  //     | scalarChannelOption
  //     | valueModifierOption
  //     | offsetOption | scaleOption | turbulenceOption
  //     | resolutionOption
  // )*
  public static boolean scalarMapOptions(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalarMapOptions")) return false;
    Marker m = enter_section_(b, l, _NONE_, SCALAR_MAP_OPTIONS, "<scalar map options>");
    while (true) {
      int c = current_position_(b);
      if (!scalarMapOptions_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scalarMapOptions", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // blendUOption | blendVOption
  //     | clampOption
  //     | scalarChannelOption
  //     | valueModifierOption
  //     | offsetOption | scaleOption | turbulenceOption
  //     | resolutionOption
  private static boolean scalarMapOptions_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalarMapOptions_0")) return false;
    boolean r;
    r = blendUOption(b, l + 1);
    if (!r) r = blendVOption(b, l + 1);
    if (!r) r = clampOption(b, l + 1);
    if (!r) r = scalarChannelOption(b, l + 1);
    if (!r) r = valueModifierOption(b, l + 1);
    if (!r) r = offsetOption(b, l + 1);
    if (!r) r = scaleOption(b, l + 1);
    if (!r) r = turbulenceOption(b, l + 1);
    if (!r) r = resolutionOption(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // SCALE_OPTION_NAME FLOAT FLOAT FLOAT
  public static boolean scaleOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scaleOption")) return false;
    if (!nextTokenIs(b, SCALE_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, SCALE_OPTION_NAME, FLOAT, FLOAT, FLOAT);
    exit_section_(b, m, SCALE_OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // SHARPNESS_KEYWORD FLOAT
  public static boolean sharpness(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sharpness")) return false;
    if (!nextTokenIs(b, SHARPNESS_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, SHARPNESS_KEYWORD, FLOAT);
    exit_section_(b, m, SHARPNESS, r);
    return r;
  }

  /* ********************************************************** */
  // SPECULAR_COLOR_KEYWORD FLOAT FLOAT FLOAT
  public static boolean specularColor(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specularColor")) return false;
    if (!nextTokenIs(b, SPECULAR_COLOR_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, SPECULAR_COLOR_KEYWORD, FLOAT, FLOAT, FLOAT);
    exit_section_(b, m, SPECULAR_COLOR, r);
    return r;
  }

  /* ********************************************************** */
  // SPECULAR_COLOR_MAP_KEYWORD colorMapOptions TEXTURE_FILE
  public static boolean specularColorMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specularColorMap")) return false;
    if (!nextTokenIs(b, SPECULAR_COLOR_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SPECULAR_COLOR_MAP_KEYWORD);
    r = r && colorMapOptions(b, l + 1);
    r = r && consumeToken(b, TEXTURE_FILE);
    exit_section_(b, m, SPECULAR_COLOR_MAP, r);
    return r;
  }

  /* ********************************************************** */
  // SPECULAR_EXPONENT_KEYWORD FLOAT
  public static boolean specularExponent(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specularExponent")) return false;
    if (!nextTokenIs(b, SPECULAR_EXPONENT_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, SPECULAR_EXPONENT_KEYWORD, FLOAT);
    exit_section_(b, m, SPECULAR_EXPONENT, r);
    return r;
  }

  /* ********************************************************** */
  // SPECULAR_EXPONENT_MAP_KEYWORD scalarMapOptions TEXTURE_FILE
  public static boolean specularExponentMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specularExponentMap")) return false;
    if (!nextTokenIs(b, SPECULAR_EXPONENT_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SPECULAR_EXPONENT_MAP_KEYWORD);
    r = r && scalarMapOptions(b, l + 1);
    r = r && consumeToken(b, TEXTURE_FILE);
    exit_section_(b, m, SPECULAR_EXPONENT_MAP, r);
    return r;
  }

  /* ********************************************************** */
  // STENCIL_DECAL_MAP_KEYWORD scalarMapOptions TEXTURE_FILE
  public static boolean stencilDecalMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stencilDecalMap")) return false;
    if (!nextTokenIs(b, STENCIL_DECAL_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STENCIL_DECAL_MAP_KEYWORD);
    r = r && scalarMapOptions(b, l + 1);
    r = r && consumeToken(b, TEXTURE_FILE);
    exit_section_(b, m, STENCIL_DECAL_MAP, r);
    return r;
  }

  /* ********************************************************** */
  // TRANSMISSION_FILTER_KEYWORD FLOAT FLOAT FLOAT
  public static boolean transmissionFilter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "transmissionFilter")) return false;
    if (!nextTokenIs(b, TRANSMISSION_FILTER_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, TRANSMISSION_FILTER_KEYWORD, FLOAT, FLOAT, FLOAT);
    exit_section_(b, m, TRANSMISSION_FILTER, r);
    return r;
  }

  /* ********************************************************** */
  // TURBULENCE_OPTION_NAME FLOAT FLOAT FLOAT
  public static boolean turbulenceOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "turbulenceOption")) return false;
    if (!nextTokenIs(b, TURBULENCE_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, TURBULENCE_OPTION_NAME, FLOAT, FLOAT, FLOAT);
    exit_section_(b, m, TURBULENCE_OPTION, r);
    return r;
  }

  /* ********************************************************** */
  // VALUE_MODIFIER_OPTION_NAME FLOAT FLOAT
  public static boolean valueModifierOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "valueModifierOption")) return false;
    if (!nextTokenIs(b, VALUE_MODIFIER_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, VALUE_MODIFIER_OPTION_NAME, FLOAT, FLOAT);
    exit_section_(b, m, VALUE_MODIFIER_OPTION, r);
    return r;
  }

}
