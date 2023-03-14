uniform mat4 uProjMat;
uniform mat4 uViewMat;
uniform mat4 uModelMat;
uniform mat3 uNormalMat;

uniform vec3 uCameraPos;

attribute vec3 aPos;
attribute vec3 aNormal;

varying vec3 vNormal;
varying vec3 vLightDir;

void main() {
    vec4 pos = vec4(aPos, 1.0);

    vNormal = normalize(uNormalMat * aNormal);

    vec3 cameraDir = normalize(uCameraPos);
    vec3 cameraUp = (uModelMat * vec4(0.0, 0.0, 1.0, 1.0)).xyz;
    vec3 cameraLeft = normalize(cross(cameraDir, cameraUp));
    cameraUp = cross(cameraLeft, cameraDir);
    vLightDir = normalize(cameraDir * 2.0 + cameraLeft + cameraUp);

    gl_Position = uProjMat * uViewMat * uModelMat * pos;
}
