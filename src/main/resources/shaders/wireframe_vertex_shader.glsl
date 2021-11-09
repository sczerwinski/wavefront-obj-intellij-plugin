uniform mat4 uMVPMat;

uniform float uPointSize;

attribute vec3 aPos;

void main() {
    vec4 pos = vec4(aPos, 1.0);
    gl_Position = uMVPMat * pos;
    gl_PointSize = uPointSize;
}
