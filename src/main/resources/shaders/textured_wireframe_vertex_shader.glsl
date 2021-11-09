uniform mat4 uMVPMat;

uniform float uPointSize;

attribute vec3 aPos;
attribute vec2 aTexCoord;

varying vec2 vTexCoord;

void main() {
    vTexCoord = aTexCoord;

    vec4 pos = vec4(aPos, 1.0);
    gl_Position = uMVPMat * pos;
    gl_PointSize = uPointSize;
}
