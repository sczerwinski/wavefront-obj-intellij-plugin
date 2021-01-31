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
  // AMBIENT_COLOR_MAP_KEYWORD colorMapOptions_ TEXTURE_FILE
  public static boolean ambientColorMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ambientColorMap")) return false;
    if (!nextTokenIs(b, AMBIENT_COLOR_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AMBIENT_COLOR_MAP_KEYWORD);
    r = r && colorMapOptions_(b, l + 1);
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
  // BUMP_MAP_KEYWORD bumpMapOptions_ TEXTURE_FILE
  public static boolean bumpMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bumpMap")) return false;
    if (!nextTokenIs(b, BUMP_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BUMP_MAP_KEYWORD);
    r = r && bumpMapOptions_(b, l + 1);
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
  //     | bumpMultiplierOption
  // )*
  static boolean bumpMapOptions_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bumpMapOptions_")) return false;
    while (true) {
      int c = current_position_(b);
      if (!bumpMapOptions__0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bumpMapOptions_", c)) break;
    }
    return true;
  }

  // blendUOption | blendVOption
  //     | clampOption
  //     | scalarChannelOption
  //     | valueModifierOption
  //     | offsetOption | scaleOption | turbulenceOption
  //     | resolutionOption
  //     | bumpMultiplierOption
  private static boolean bumpMapOptions__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bumpMapOptions__0")) return false;
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
    if (!r) r = bumpMultiplierOption(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // BUMP_MULTIPLIER_OPTION_NAME FLOAT
  public static boolean bumpMultiplierOption(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bumpMultiplierOption")) return false;
    if (!nextTokenIs(b, BUMP_MULTIPLIER_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, BUMP_MULTIPLIER_OPTION_NAME, FLOAT);
    exit_section_(b, m, BUMP_MULTIPLIER_OPTION, r);
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
  // (
  //     blendUOption | blendVOption
  //     | colorCorrectionOption | clampOption
  //     | valueModifierOption
  //     | offsetOption | scaleOption | turbulenceOption
  //     | resolutionOption
  // )*
  static boolean colorMapOptions_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "colorMapOptions_")) return false;
    while (true) {
      int c = current_position_(b);
      if (!colorMapOptions__0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "colorMapOptions_", c)) break;
    }
    return true;
  }

  // blendUOption | blendVOption
  //     | colorCorrectionOption | clampOption
  //     | valueModifierOption
  //     | offsetOption | scaleOption | turbulenceOption
  //     | resolutionOption
  private static boolean colorMapOptions__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "colorMapOptions__0")) return false;
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
  // ambientColorMap | diffuseColorMap | specularColorMap
  static boolean colorMap_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "colorMap_")) return false;
    boolean r;
    r = ambientColorMap(b, l + 1);
    if (!r) r = diffuseColorMap(b, l + 1);
    if (!r) r = specularColorMap(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // ambientColor | diffuseColor | specularColor | transmissionFilter
  static boolean color_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "color_")) return false;
    boolean r;
    r = ambientColor(b, l + 1);
    if (!r) r = diffuseColor(b, l + 1);
    if (!r) r = specularColor(b, l + 1);
    if (!r) r = transmissionFilter(b, l + 1);
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
  // DIFFUSE_COLOR_MAP_KEYWORD colorMapOptions_ TEXTURE_FILE
  public static boolean diffuseColorMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "diffuseColorMap")) return false;
    if (!nextTokenIs(b, DIFFUSE_COLOR_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DIFFUSE_COLOR_MAP_KEYWORD);
    r = r && colorMapOptions_(b, l + 1);
    r = r && consumeToken(b, TEXTURE_FILE);
    exit_section_(b, m, DIFFUSE_COLOR_MAP, r);
    return r;
  }

  /* ********************************************************** */
  // DISPLACEMENT_MAP_KEYWORD scalarMapOptions_ TEXTURE_FILE
  public static boolean displacementMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "displacementMap")) return false;
    if (!nextTokenIs(b, DISPLACEMENT_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DISPLACEMENT_MAP_KEYWORD);
    r = r && scalarMapOptions_(b, l + 1);
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
  // DISSOLVE_MAP_KEYWORD scalarMapOptions_ TEXTURE_FILE
  public static boolean dissolveMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "dissolveMap")) return false;
    if (!nextTokenIs(b, DISSOLVE_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DISSOLVE_MAP_KEYWORD);
    r = r && scalarMapOptions_(b, l + 1);
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
  // color_ | property_ | map_
  //   | COMMENT | CRLF
  static boolean item_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_")) return false;
    boolean r;
    r = color_(b, l + 1);
    if (!r) r = property_(b, l + 1);
    if (!r) r = map_(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    if (!r) r = consumeToken(b, CRLF);
    return r;
  }

  /* ********************************************************** */
  // colorMap_ | scalarMap_ | bumpMap | reflectionMap
  static boolean map_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "map_")) return false;
    boolean r;
    r = colorMap_(b, l + 1);
    if (!r) r = scalarMap_(b, l + 1);
    if (!r) r = bumpMap(b, l + 1);
    if (!r) r = reflectionMap(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // (NEW_MATERIAL_KEYWORD materialIdentifier) + item_*
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

  // (NEW_MATERIAL_KEYWORD materialIdentifier) +
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

  // NEW_MATERIAL_KEYWORD materialIdentifier
  private static boolean material_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "material_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NEW_MATERIAL_KEYWORD);
    r = r && materialIdentifier(b, l + 1);
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
  // MATERIAL_NAME
  public static boolean materialIdentifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "materialIdentifier")) return false;
    if (!nextTokenIs(b, MATERIAL_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MATERIAL_NAME);
    exit_section_(b, m, MATERIAL_IDENTIFIER, r);
    return r;
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
  static boolean property_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "property_")) return false;
    boolean r;
    r = illumination(b, l + 1);
    if (!r) r = dissolve(b, l + 1);
    if (!r) r = specularExponent(b, l + 1);
    if (!r) r = sharpness(b, l + 1);
    if (!r) r = opticalDensity(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // REFLECTION_MAP_KEYWORD reflectionMapOptions_ TEXTURE_FILE
  public static boolean reflectionMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reflectionMap")) return false;
    if (!nextTokenIs(b, REFLECTION_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, REFLECTION_MAP_KEYWORD);
    r = r && reflectionMapOptions_(b, l + 1);
    r = r && consumeToken(b, TEXTURE_FILE);
    exit_section_(b, m, REFLECTION_MAP, r);
    return r;
  }

  /* ********************************************************** */
  // reflectionTypeOption + colorMapOptions_
  static boolean reflectionMapOptions_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reflectionMapOptions_")) return false;
    if (!nextTokenIs(b, REFLECTION_TYPE_OPTION_NAME)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = reflectionMapOptions__0(b, l + 1);
    r = r && colorMapOptions_(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // reflectionTypeOption +
  private static boolean reflectionMapOptions__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reflectionMapOptions__0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = reflectionTypeOption(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!reflectionTypeOption(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "reflectionMapOptions__0", c)) break;
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
  // (
  //     blendUOption | blendVOption
  //     | clampOption
  //     | scalarChannelOption
  //     | valueModifierOption
  //     | offsetOption | scaleOption | turbulenceOption
  //     | resolutionOption
  // )*
  static boolean scalarMapOptions_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalarMapOptions_")) return false;
    while (true) {
      int c = current_position_(b);
      if (!scalarMapOptions__0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scalarMapOptions_", c)) break;
    }
    return true;
  }

  // blendUOption | blendVOption
  //     | clampOption
  //     | scalarChannelOption
  //     | valueModifierOption
  //     | offsetOption | scaleOption | turbulenceOption
  //     | resolutionOption
  private static boolean scalarMapOptions__0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalarMapOptions__0")) return false;
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
  // specularExponentMap | dissolveMap | displacementMap | stencilDecalMap
  static boolean scalarMap_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalarMap_")) return false;
    boolean r;
    r = specularExponentMap(b, l + 1);
    if (!r) r = dissolveMap(b, l + 1);
    if (!r) r = displacementMap(b, l + 1);
    if (!r) r = stencilDecalMap(b, l + 1);
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
  // SPECULAR_COLOR_MAP_KEYWORD colorMapOptions_ TEXTURE_FILE
  public static boolean specularColorMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specularColorMap")) return false;
    if (!nextTokenIs(b, SPECULAR_COLOR_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SPECULAR_COLOR_MAP_KEYWORD);
    r = r && colorMapOptions_(b, l + 1);
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
  // SPECULAR_EXPONENT_MAP_KEYWORD scalarMapOptions_ TEXTURE_FILE
  public static boolean specularExponentMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "specularExponentMap")) return false;
    if (!nextTokenIs(b, SPECULAR_EXPONENT_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SPECULAR_EXPONENT_MAP_KEYWORD);
    r = r && scalarMapOptions_(b, l + 1);
    r = r && consumeToken(b, TEXTURE_FILE);
    exit_section_(b, m, SPECULAR_EXPONENT_MAP, r);
    return r;
  }

  /* ********************************************************** */
  // STENCIL_DECAL_MAP_KEYWORD scalarMapOptions_ TEXTURE_FILE
  public static boolean stencilDecalMap(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stencilDecalMap")) return false;
    if (!nextTokenIs(b, STENCIL_DECAL_MAP_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STENCIL_DECAL_MAP_KEYWORD);
    r = r && scalarMapOptions_(b, l + 1);
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
