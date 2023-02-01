uniform vec3 uAmbColor;
uniform vec3 uDiffColor;
uniform vec3 uSpecColor;
uniform vec3 uEmissionColor;
uniform float uSpecExp;

uniform sampler2D uAmbTex;
uniform sampler2D uDiffTex;
uniform sampler2D uSpecTex;
uniform sampler2D uEmissionTex;
uniform sampler2D uSpecExpTex;
uniform float uSpecExpBase;
uniform float uSpecExpGain;
uniform int uSpecExpChan;
uniform sampler2D uNormalTex;
uniform float uBumpMult;
uniform sampler2D uDispTex;
uniform float uDispGain;
uniform float uDispQuality;
uniform int uDispChan;
uniform int uCropTex;

varying vec3 vPosTan;
varying vec3 vCameraPosTan;
varying vec3 vLightPosTan;
varying vec2 vTexCoord;

#define MAX_ITERATIONS 1024

float channel(int channel, vec4 color) {
    if (channel == 0) return color.r;
    if (channel == 1) return color.g;
    if (channel == 2) return color.b;
    return color.r;
}

float displacement(vec2 texCoord) {
    return 1.0 - channel(uDispChan, texture2D(uDispTex, texCoord));
}

vec2 displacedTexCoord(vec3 cameraDir) {
    float depthStep = pow(2.0, -uDispQuality) / max(dot(vec3(0.0, 0.0, 1.0), cameraDir), 0.01);
    vec2 texCoordDisplacementStep = cameraDir.xy * uDispGain * depthStep;

    vec2 newTexCoord = vTexCoord;
    vec2 oldTexCoord = vTexCoord;
    float newError = displacement(vTexCoord);
    float oldError = newError;

    int iteration = 0;
    for (float depth = 0.0; newError > 0.0; depth += depthStep) {
        oldTexCoord = newTexCoord;
        newTexCoord -= texCoordDisplacementStep;
        oldError = newError;
        newError = displacement(newTexCoord) - depth;
        if (iteration++ > MAX_ITERATIONS) break;
    }

    if (newError == oldError) {
        return oldTexCoord;
    }
    return mix(newTexCoord, oldTexCoord, newError / (newError - oldError));
}

void main() {
    vec3 cameraDir = normalize(vCameraPosTan - vPosTan);

    vec2 texCoord = displacedTexCoord(cameraDir);

    if (uCropTex == 1 && (texCoord.x < 0.0 || texCoord.y < 0.0 || texCoord.x > 1.0 || texCoord.y > 1.0)) {
        discard;
    }

    vec4 diffTexColor = texture2D(uDiffTex, texCoord);

    float alpha = diffTexColor.a;

    if (alpha == 0.0) {
        discard;
    }

    vec3 lightDir = normalize(vLightPosTan - vPosTan);
    vec3 halfVector = normalize(lightDir + cameraDir);

    vec3 normal = normalize(texture2D(uNormalTex, texCoord).rgb * 2.0 - 1.0);
    normal = normalize(vec3(normal.x, normal.y, normal.z / uBumpMult));

    if (!gl_FrontFacing) {
        normal = -normal;
    }

    float exposure = max(dot(normal, lightDir), 0.0);

    float exponent = (uSpecExpBase + channel(uSpecExpChan, texture2D(uSpecExpTex, texCoord)) * uSpecExpGain) * uSpecExp;
    float specular = pow(max(dot(normal, halfVector), 0.0), exponent);

    vec3 ambColor = texture2D(uAmbTex, texCoord).rgb * uAmbColor * 0.4;
    vec3 diffColor = diffTexColor.rgb * uDiffColor;
    vec3 specColor = texture2D(uSpecTex, texCoord).rgb * uSpecColor;
    vec3 emissionColor = texture2D(uEmissionTex, texCoord).rgb * uEmissionColor;

    vec3 color = diffColor * exposure + ambColor * (1.0 - exposure) + specColor * specular + emissionColor;

    gl_FragColor = vec4(color, alpha);
}
