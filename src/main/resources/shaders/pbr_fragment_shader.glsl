#define REFLECTION_LEVELS 8
#define MAX_ITERATIONS 1024

uniform vec3 uCameraPos;
uniform vec3 uLightPos;

uniform vec3 uDiffColor;
uniform vec3 uEmissionColor;
uniform float uRoughness;
uniform float uMetalness;

uniform sampler2D uDiffTex;
uniform sampler2D uEmissionTex;
uniform sampler2D uRoughnessTex;
uniform int uRoughnessChan;
uniform sampler2D uMetalnessTex;
uniform int uMetalnessChan;
uniform sampler2D uNormalTex;
uniform sampler2D uDispTex;
uniform float uDispGain;
uniform float uDispQuality;
uniform int uDispChan;
uniform sampler2D uIrradianceTex;
uniform sampler2D uReflectionTex[REFLECTION_LEVELS];
uniform sampler2D uBRDFTex;
uniform int uCropTex;

varying vec3 vPos;
varying vec3 vNormal;
varying vec3 vTangent;
varying vec3 vBitangent;
varying vec2 vTexCoord;

const float pi = 3.14159265358979323846;
const float tau = 2.0 * pi;

mat3 tbnMat() {
    vec3 normal = normalize(vNormal);
    vec3 tangent = normalize(vTangent);
    vec3 bitangent = normalize(vBitangent);
    return mat3(tangent, bitangent, normal);
}

vec3 normal(vec2 texCoord) {
    return normalize(tbnMat() * normalize(texture2D(uNormalTex, texCoord).rgb * 2.0 - 1.0));
}

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
    vec3 normal = normalize(vNormal);
    vec3 tangent = normalize(vTangent);
    vec3 bitangent = normalize(vBitangent);

    float depthStep = pow(2.0, -uDispQuality) / max(dot(normal, cameraDir), 0.01);
    vec2 texCoordDisplacementStep = vec2(dot(tangent, cameraDir), dot(bitangent, cameraDir)) * uDispGain * depthStep;

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

vec2 envTexCoord(vec3 direction) {
    float horizontal = length(direction.xy);
    vec2 texCoord = vec2(atan(direction.x, direction.y) / tau, atan(direction.z, horizontal) / pi + 0.5);
    return texCoord;
}

float normalDistribution(vec3 normal, vec3 halfVector, float roughness) {
    float coefficient = roughness * roughness;
    float exposure = max(dot(normal, halfVector), 0.0);
    float divisor = exposure * exposure * (coefficient - 1.0) + 1.0;
    return coefficient / (pi * divisor * divisor);
}

float geometryFactor(vec3 normal, vec3 direction, float roughness) {
    float parameter = (roughness + 1.0) * (roughness + 1.0) / 8.0;
    float exposure = max(dot(normal, direction), 0.0);
    return exposure / (exposure * (1.0 - parameter) + parameter);
}

float geometry(vec3 normal, vec3 cameraDir, vec3 lightDir, float roughness) {
    return geometryFactor(normal, cameraDir, roughness) * geometryFactor(normal, lightDir, roughness);
}

vec3 baseSpecularColor(vec3 diffColor, float metalness) {
    return mix(vec3(0.04), diffColor, metalness);
}

vec3 specularFactor(vec3 baseSpecularColor, vec3 cameraDir, vec3 vector) {
    float factor = 1.0 - min(1.0, max(dot(cameraDir, vector), 0.0001));
    return baseSpecularColor + (1.0 - baseSpecularColor) * pow(factor, 5.0);
}

vec3 specularFactor(vec3 baseSpecularColor, vec3 cameraDir, vec3 vector, float roughness) {
    float factor = 1.0 - min(1.0, max(dot(cameraDir, vector), 0.0001));
    return baseSpecularColor + (max(vec3(1.0 - roughness), baseSpecularColor) - baseSpecularColor) * pow(factor, 5.0);
}

float specularDivider(vec3 normal, vec3 cameraDir, vec3 lightDir) {
    return 4.0 * min(1.0, max(max(dot(normal, cameraDir), 0.0) * max(dot(normal, lightDir), 0.0), 0.0001));
}

void main() {
    vec3 cameraDir = normalize(uCameraPos - vPos);

    vec2 texCoord = displacedTexCoord(cameraDir);

    if (uCropTex == 1 && (texCoord.x < 0.0 || texCoord.y < 0.0 || texCoord.x > 1.0 || texCoord.y > 1.0)) {
        discard;
    }

    vec4 diffTexColor = texture2D(uDiffTex, texCoord);
    float alpha = diffTexColor.a;

    if (alpha == 0.0) {
        discard;
    }

    vec3 lightDir = normalize(uLightPos - vPos);
    vec3 halfVector = normalize(lightDir + cameraDir);

    vec3 normal = normal(texCoord);

    if (!gl_FrontFacing) {
        normal = -normal;
    }

    vec3 diffColor = diffTexColor.rgb * uDiffColor;
    vec3 emissionColor = texture2D(uEmissionTex, texCoord).rgb * uEmissionColor;
    vec3 envRadianceColor = texture2D(uIrradianceTex, envTexCoord(normal)).rgb;

    float roughness = channel(uRoughnessChan, texture2D(uRoughnessTex, texCoord)) * uRoughness;
    float metalness = channel(uMetalnessChan, texture2D(uMetalnessTex, texCoord)) * uMetalness;

    vec3 baseSpecularColor = baseSpecularColor(diffColor, metalness);

    float normalDistribution = normalDistribution(normal, halfVector, roughness);
    float geometry = geometry(normal, cameraDir, lightDir, roughness);

    vec3 lightSpecularFactor = specularFactor(baseSpecularColor, cameraDir, halfVector);
    vec3 lightDiffuseFactor = (vec3(1.0) - lightSpecularFactor) * (1.0 - metalness);

    vec3 specularFromLight = normalDistribution * geometry * lightSpecularFactor / specularDivider(normal, cameraDir, lightDir);
    vec3 colorFromLight = (lightDiffuseFactor * diffColor / pi + specularFromLight) * max(dot(normal, lightDir), 0.0);

    vec3 specularFactor = specularFactor(baseSpecularColor, cameraDir, normal, roughness);

    vec2 brdfCoord = vec2(max(0.0, dot(cameraDir, normal)), roughness);
    vec2 brdf = texture2D(uBRDFTex, brdfCoord).rg;

    vec2 reflectTexCoord = envTexCoord(reflect(-cameraDir, normal));
    float reflectionLevel = roughness * float(REFLECTION_LEVELS - 1);
    int reflectionIndex = int(max(0.0, min(float(REFLECTION_LEVELS - 2), reflectionLevel)));
    float reflectionMix = max(0.0, min(1.0, reflectionLevel - float(reflectionIndex)));
    vec3 reflection1 = texture2D(uReflectionTex[reflectionIndex], reflectTexCoord).rgb;
    vec3 reflection2 = texture2D(uReflectionTex[reflectionIndex + 1], reflectTexCoord).rgb;
    vec3 reflection = mix(reflection1, reflection2, reflectionMix);

    vec3 specularColor = min(specularFactor * brdf.x + brdf.y, 1.0) * reflection;

    vec3 diffuseFactor = (vec3(1.0) - specularFactor) * (1.0 - metalness);

    vec3 ambColor = diffuseFactor * envRadianceColor * diffColor;

    vec3 color = colorFromLight + ambColor + specularColor + emissionColor;

    gl_FragColor = vec4(color, alpha);
}
