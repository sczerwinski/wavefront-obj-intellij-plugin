uniform mat4 uProjMat;
uniform mat4 uViewMat;
uniform mat4 uModelMat;
uniform mat3 uNormalMat;

attribute vec3 aPos;
attribute vec2 aTexCoord;
attribute vec3 aNormal;
attribute vec3 aTangent;
attribute vec3 aBitangent;

varying vec3 vPos;
varying vec3 vNormal;
varying vec3 vTangent;
varying vec3 vBitangent;
varying vec2 vTexCoord;

void main() {
    vec4 pos = vec4(aPos, 1.0);

    vPos = vec3(uModelMat * pos);

    vNormal = uNormalMat * aNormal;
    vTangent = uNormalMat * aTangent;
    vBitangent = uNormalMat * aBitangent;

    vTexCoord = aTexCoord;

    gl_Position = uProjMat * uViewMat * uModelMat * pos;
}
