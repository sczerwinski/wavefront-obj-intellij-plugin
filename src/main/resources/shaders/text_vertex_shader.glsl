#version 100

precision mediump float;

uniform vec3 uPos;
uniform vec2 uScale;

attribute vec3 aPos;
attribute vec2 aTexCoord;

varying vec2 vTexCoord;

void main() {
    vTexCoord = aTexCoord;

    vec3 scaledPos = vec3(aPos.xy * uScale, 0.0);
    gl_Position = vec4(uPos + scaledPos, 1.0);
}
