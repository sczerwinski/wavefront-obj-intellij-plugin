#version 100

precision mediump float;

uniform mat4 uProjMat;
uniform mat4 uViewMat;
uniform mat4 uModelMat;
uniform mat3 uNormalMat;

uniform vec3 uCameraPos;
uniform vec3 uUpVector;

attribute vec3 aPos;
attribute vec3 aNormal;
attribute vec3 aTangent;

varying vec3 vPosTan;
varying vec3 vLightPosTan;

void main() {
    vec4 pos = vec4(aPos, 1.0);

    vec3 normal = normalize(uNormalMat * aNormal);
    vec3 tangent = normalize(uNormalMat * aTangent);
    tangent = normalize(tangent - dot(tangent, normal) * normal);
    vec3 bitangent = cross(normal, tangent);

    mat3 tbnMat = mat3(
        vec3(tangent.x, bitangent.x, normal.x),
        vec3(tangent.y, bitangent.y, normal.y),
        vec3(tangent.z, bitangent.z, normal.z)
    );

    vPosTan = tbnMat * vec3(uModelMat * pos);

    vec3 cameraDir = normalize(uCameraPos);
    vec3 upVector = normalize(uUpVector - dot(uUpVector, cameraDir) * cameraDir);
    vec3 leftVector = cross(cameraDir, upVector);
    vLightPosTan = tbnMat * ((cameraDir + leftVector + upVector) * 10.0);

    gl_Position = uProjMat * uViewMat * uModelMat * pos;
}
