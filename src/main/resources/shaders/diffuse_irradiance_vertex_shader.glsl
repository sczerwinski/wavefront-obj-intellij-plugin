#version 100

attribute vec2 aPos;
attribute vec2 aTexCoord;

varying vec2 vPos;
varying vec2 vTexCoord;

void main() {
    vPos = aPos;
    vTexCoord = aTexCoord;
    gl_Position = vec4(aPos, 0.0, 1.0);
}
