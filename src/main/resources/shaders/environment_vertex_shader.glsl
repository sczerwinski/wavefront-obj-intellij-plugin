uniform mat4 uMVPMat;

attribute vec3 aPos;

varying vec3 vPos;

void main() {
    vPos = aPos;
    vec4 pos = vec4(aPos, 1.0);
    gl_Position = uMVPMat * pos;
}
