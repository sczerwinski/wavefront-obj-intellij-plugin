#version 100

precision mediump float;

uniform vec3 uAmbColor;
uniform vec3 uDiffColor;
uniform vec3 uSpecColor;
uniform float uSpecExp;

varying vec3 vPosTan;
varying vec3 vCameraPosTan;
varying vec3 vLightPosTan;

void main() {
    vec3 cameraDir = normalize(vCameraPosTan - vPosTan);
    vec3 lightDir = normalize(vLightPosTan - vPosTan);
    vec3 halfVector = normalize(lightDir + cameraDir);

    float exposure = max(lightDir.z, 0.0);
    float specular = pow(max(halfVector.z, 0.0), uSpecExp);

    vec3 ambColor = uAmbColor * 0.4;
    vec3 color = uDiffColor * exposure + ambColor * (1.0 - exposure) + uSpecColor * specular;

    gl_FragColor = vec4(color, 1.0);
}
