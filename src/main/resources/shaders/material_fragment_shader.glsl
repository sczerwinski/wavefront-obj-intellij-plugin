#version 100

precision mediump float;

uniform vec3 uAmbColor;
uniform vec3 uDiffColor;
uniform vec3 uSpecColor;
uniform float uSpecExp;

uniform sampler2D uAmbTex;
uniform sampler2D uDiffTex;
uniform sampler2D uSpecTex;
uniform sampler2D uSpecExpTex;
uniform float uSpecExpBase;
uniform float uSpecExpGain;
uniform sampler2D uNormalTex;
uniform float uBumpMult;

varying vec3 vPosTan;
varying vec3 vCameraPosTan;
varying vec3 vLightPosTan;
varying vec2 vTexCoord;

void main() {
    vec3 cameraDir = normalize(vCameraPosTan - vPosTan);
    vec3 lightDir = normalize(vLightPosTan - vPosTan);
    vec3 halfVector = normalize(lightDir + cameraDir);

    vec3 normal = normalize(texture2D(uNormalTex, vTexCoord).rgb * 2.0 - 1.0);
    normal = normalize(vec3(normal.x, normal.y, normal.z / uBumpMult));

    float exposure = max(dot(normal, lightDir), 0.0);

    float exponent = (uSpecExpBase + texture2D(uSpecExpTex, vTexCoord).r * uSpecExpGain) * uSpecExp;
    float specular = pow(max(dot(normal, halfVector), 0.0), exponent);

    vec3 ambColor = texture2D(uAmbTex, vTexCoord).rgb * uAmbColor * 0.4;
    vec3 diffColor = texture2D(uDiffTex, vTexCoord).rgb * uDiffColor;
    vec3 specColor = texture2D(uSpecTex, vTexCoord).rgb * uSpecColor;
    vec3 color = diffColor * exposure + ambColor * (1.0 - exposure) + specColor * specular;

    gl_FragColor = vec4(color, 1.0);
}
