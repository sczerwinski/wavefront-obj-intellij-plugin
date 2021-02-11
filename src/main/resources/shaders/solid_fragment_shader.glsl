#version 100

precision mediump float;

uniform vec3 uColor;

varying vec3 vPosTan;
varying vec3 vLightPosTan;

void main() {
    vec3 lightDir = normalize(vLightPosTan - vPosTan);
    float exposure = max(lightDir.z, 0.0) * 0.6 + 0.4;

    gl_FragColor = vec4(uColor * exposure, 1.0);
}
