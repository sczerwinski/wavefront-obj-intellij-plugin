// This is a generated file. Not intended for manual editing.
package it.czerwinski.intellij.wavefront.lang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import it.czerwinski.intellij.wavefront.lang.psi.impl.*;

public interface MtlTypes {

  IElementType AMBIENT_COLOR = new MtlElementType("AMBIENT_COLOR");
  IElementType AMBIENT_COLOR_MAP = new MtlElementType("AMBIENT_COLOR_MAP");
  IElementType BLEND_U_OPTION = new MtlElementType("BLEND_U_OPTION");
  IElementType BLEND_V_OPTION = new MtlElementType("BLEND_V_OPTION");
  IElementType BUMP_MAP = new MtlElementType("BUMP_MAP");
  IElementType BUMP_MULTIPLIER = new MtlElementType("BUMP_MULTIPLIER");
  IElementType CLAMP_OPTION = new MtlElementType("CLAMP_OPTION");
  IElementType COLOR_CORRECTION_OPTION = new MtlElementType("COLOR_CORRECTION_OPTION");
  IElementType DIFFUSE_COLOR = new MtlElementType("DIFFUSE_COLOR");
  IElementType DIFFUSE_COLOR_MAP = new MtlElementType("DIFFUSE_COLOR_MAP");
  IElementType DISPLACEMENT_MAP = new MtlElementType("DISPLACEMENT_MAP");
  IElementType DISSOLVE = new MtlElementType("DISSOLVE");
  IElementType DISSOLVE_MAP = new MtlElementType("DISSOLVE_MAP");
  IElementType ILLUMINATION = new MtlElementType("ILLUMINATION");
  IElementType MATERIAL = new MtlElementType("MATERIAL");
  IElementType OFFSET_OPTION = new MtlElementType("OFFSET_OPTION");
  IElementType OPTICAL_DENSITY = new MtlElementType("OPTICAL_DENSITY");
  IElementType REFLECTION_MAP = new MtlElementType("REFLECTION_MAP");
  IElementType REFLECTION_TYPE_OPTION = new MtlElementType("REFLECTION_TYPE_OPTION");
  IElementType RESOLUTION_OPTION = new MtlElementType("RESOLUTION_OPTION");
  IElementType SCALAR_CHANNEL_OPTION = new MtlElementType("SCALAR_CHANNEL_OPTION");
  IElementType SCALE_OPTION = new MtlElementType("SCALE_OPTION");
  IElementType SHARPNESS = new MtlElementType("SHARPNESS");
  IElementType SPECULAR_COLOR = new MtlElementType("SPECULAR_COLOR");
  IElementType SPECULAR_COLOR_MAP = new MtlElementType("SPECULAR_COLOR_MAP");
  IElementType SPECULAR_EXPONENT = new MtlElementType("SPECULAR_EXPONENT");
  IElementType SPECULAR_EXPONENT_MAP = new MtlElementType("SPECULAR_EXPONENT_MAP");
  IElementType STENCIL_DECAL_MAP = new MtlElementType("STENCIL_DECAL_MAP");
  IElementType TRANSMISSION_FILTER = new MtlElementType("TRANSMISSION_FILTER");
  IElementType TURBULENCE_OPTION = new MtlElementType("TURBULENCE_OPTION");
  IElementType VALUE_MODIFIER_OPTION = new MtlElementType("VALUE_MODIFIER_OPTION");

  IElementType AMBIENT_COLOR_KEYWORD = new MtlTokenType("AMBIENT_COLOR_KEYWORD");
  IElementType AMBIENT_COLOR_MAP_KEYWORD = new MtlTokenType("AMBIENT_COLOR_MAP_KEYWORD");
  IElementType BLEND_U_OPTION_NAME = new MtlTokenType("BLEND_U_OPTION_NAME");
  IElementType BLEND_V_OPTION_NAME = new MtlTokenType("BLEND_V_OPTION_NAME");
  IElementType BUMP_MAP_KEYWORD = new MtlTokenType("BUMP_MAP_KEYWORD");
  IElementType BUMP_MULTIPLIER_OPTION_NAME = new MtlTokenType("BUMP_MULTIPLIER_OPTION_NAME");
  IElementType CLAMP_OPTION_NAME = new MtlTokenType("CLAMP_OPTION_NAME");
  IElementType COLOR_CORRECTION_OPTION_NAME = new MtlTokenType("COLOR_CORRECTION_OPTION_NAME");
  IElementType COMMENT = new MtlTokenType("COMMENT");
  IElementType CRLF = new MtlTokenType("CRLF");
  IElementType DIFFUSE_COLOR_KEYWORD = new MtlTokenType("DIFFUSE_COLOR_KEYWORD");
  IElementType DIFFUSE_COLOR_MAP_KEYWORD = new MtlTokenType("DIFFUSE_COLOR_MAP_KEYWORD");
  IElementType DISPLACEMENT_MAP_KEYWORD = new MtlTokenType("DISPLACEMENT_MAP_KEYWORD");
  IElementType DISSOLVE_KEYWORD = new MtlTokenType("DISSOLVE_KEYWORD");
  IElementType DISSOLVE_MAP_KEYWORD = new MtlTokenType("DISSOLVE_MAP_KEYWORD");
  IElementType FLAG = new MtlTokenType("FLAG");
  IElementType FLOAT = new MtlTokenType("FLOAT");
  IElementType ILLUMINATION_KEYWORD = new MtlTokenType("ILLUMINATION_KEYWORD");
  IElementType ILLUMINATION_VALUE = new MtlTokenType("ILLUMINATION_VALUE");
  IElementType INTEGER = new MtlTokenType("INTEGER");
  IElementType MATERIAL_NAME = new MtlTokenType("MATERIAL_NAME");
  IElementType NEW_MATERIAL_KEYWORD = new MtlTokenType("NEW_MATERIAL_KEYWORD");
  IElementType OFFSET_OPTION_NAME = new MtlTokenType("OFFSET_OPTION_NAME");
  IElementType OPTICAL_DENSITY_KEYWORD = new MtlTokenType("OPTICAL_DENSITY_KEYWORD");
  IElementType REFLECTION_MAP_KEYWORD = new MtlTokenType("REFLECTION_MAP_KEYWORD");
  IElementType REFLECTION_TYPE = new MtlTokenType("REFLECTION_TYPE");
  IElementType REFLECTION_TYPE_OPTION_NAME = new MtlTokenType("REFLECTION_TYPE_OPTION_NAME");
  IElementType RESOLUTION_OPTION_NAME = new MtlTokenType("RESOLUTION_OPTION_NAME");
  IElementType SCALAR_CHANNEL = new MtlTokenType("SCALAR_CHANNEL");
  IElementType SCALAR_CHANNEL_OPTION_NAME = new MtlTokenType("SCALAR_CHANNEL_OPTION_NAME");
  IElementType SCALE_OPTION_NAME = new MtlTokenType("SCALE_OPTION_NAME");
  IElementType SHARPNESS_KEYWORD = new MtlTokenType("SHARPNESS_KEYWORD");
  IElementType SPECULAR_COLOR_KEYWORD = new MtlTokenType("SPECULAR_COLOR_KEYWORD");
  IElementType SPECULAR_COLOR_MAP_KEYWORD = new MtlTokenType("SPECULAR_COLOR_MAP_KEYWORD");
  IElementType SPECULAR_EXPONENT_KEYWORD = new MtlTokenType("SPECULAR_EXPONENT_KEYWORD");
  IElementType SPECULAR_EXPONENT_MAP_KEYWORD = new MtlTokenType("SPECULAR_EXPONENT_MAP_KEYWORD");
  IElementType STENCIL_DECAL_MAP_KEYWORD = new MtlTokenType("STENCIL_DECAL_MAP_KEYWORD");
  IElementType TEXTURE_FILE = new MtlTokenType("TEXTURE_FILE");
  IElementType TRANSMISSION_FILTER_KEYWORD = new MtlTokenType("TRANSMISSION_FILTER_KEYWORD");
  IElementType TURBULENCE_OPTION_NAME = new MtlTokenType("TURBULENCE_OPTION_NAME");
  IElementType VALUE_MODIFIER_OPTION_NAME = new MtlTokenType("VALUE_MODIFIER_OPTION_NAME");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == AMBIENT_COLOR) {
        return new MtlAmbientColorImpl(node);
      }
      else if (type == AMBIENT_COLOR_MAP) {
        return new MtlAmbientColorMapImpl(node);
      }
      else if (type == BLEND_U_OPTION) {
        return new MtlBlendUOptionImpl(node);
      }
      else if (type == BLEND_V_OPTION) {
        return new MtlBlendVOptionImpl(node);
      }
      else if (type == BUMP_MAP) {
        return new MtlBumpMapImpl(node);
      }
      else if (type == BUMP_MULTIPLIER) {
        return new MtlBumpMultiplierImpl(node);
      }
      else if (type == CLAMP_OPTION) {
        return new MtlClampOptionImpl(node);
      }
      else if (type == COLOR_CORRECTION_OPTION) {
        return new MtlColorCorrectionOptionImpl(node);
      }
      else if (type == DIFFUSE_COLOR) {
        return new MtlDiffuseColorImpl(node);
      }
      else if (type == DIFFUSE_COLOR_MAP) {
        return new MtlDiffuseColorMapImpl(node);
      }
      else if (type == DISPLACEMENT_MAP) {
        return new MtlDisplacementMapImpl(node);
      }
      else if (type == DISSOLVE) {
        return new MtlDissolveImpl(node);
      }
      else if (type == DISSOLVE_MAP) {
        return new MtlDissolveMapImpl(node);
      }
      else if (type == ILLUMINATION) {
        return new MtlIlluminationImpl(node);
      }
      else if (type == MATERIAL) {
        return new MtlMaterialImpl(node);
      }
      else if (type == OFFSET_OPTION) {
        return new MtlOffsetOptionImpl(node);
      }
      else if (type == OPTICAL_DENSITY) {
        return new MtlOpticalDensityImpl(node);
      }
      else if (type == REFLECTION_MAP) {
        return new MtlReflectionMapImpl(node);
      }
      else if (type == REFLECTION_TYPE_OPTION) {
        return new MtlReflectionTypeOptionImpl(node);
      }
      else if (type == RESOLUTION_OPTION) {
        return new MtlResolutionOptionImpl(node);
      }
      else if (type == SCALAR_CHANNEL_OPTION) {
        return new MtlScalarChannelOptionImpl(node);
      }
      else if (type == SCALE_OPTION) {
        return new MtlScaleOptionImpl(node);
      }
      else if (type == SHARPNESS) {
        return new MtlSharpnessImpl(node);
      }
      else if (type == SPECULAR_COLOR) {
        return new MtlSpecularColorImpl(node);
      }
      else if (type == SPECULAR_COLOR_MAP) {
        return new MtlSpecularColorMapImpl(node);
      }
      else if (type == SPECULAR_EXPONENT) {
        return new MtlSpecularExponentImpl(node);
      }
      else if (type == SPECULAR_EXPONENT_MAP) {
        return new MtlSpecularExponentMapImpl(node);
      }
      else if (type == STENCIL_DECAL_MAP) {
        return new MtlStencilDecalMapImpl(node);
      }
      else if (type == TRANSMISSION_FILTER) {
        return new MtlTransmissionFilterImpl(node);
      }
      else if (type == TURBULENCE_OPTION) {
        return new MtlTurbulenceOptionImpl(node);
      }
      else if (type == VALUE_MODIFIER_OPTION) {
        return new MtlValueModifierOptionImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
