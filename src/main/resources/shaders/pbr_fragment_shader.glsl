uniform vec3 uDiffColor;
uniform vec3 uEmissionColor;
uniform float uRoughness;
uniform float uMetalness;

uniform sampler2D uDiffTex;
uniform sampler2D uEmissionTex;
uniform sampler2D uRoughnessTex;
uniform sampler2D uMetalnessTex;
uniform sampler2D uNormalTex;
uniform sampler2D uDispTex;
uniform float uDispGain;
uniform float uDispQuality;
uniform sampler2D uReflectionTex;
uniform sampler2D uRadianceTex;
uniform int uCropTex;

varying vec3 vPosTan;
varying vec3 vCameraPosTan;
varying vec3 vLightPosTan;
varying vec3 vNormal;
varying vec3 vTangent;
varying vec2 vTexCoord;

const float pi = 3.14159265358979323846;
const float tau = 2.0 * pi;

#define MAX_ITERATIONS 1024

mat3 tbnMat() {
    vec3 normal = normalize(vNormal);
    vec3 tangent = normalize(vTangent);
    tangent = normalize(tangent - dot(tangent, normal) * normal);
    vec3 bitangent = normalize(cross(normal, tangent));
    return mat3(tangent, bitangent, normal);
}

vec3 normalWorld(vec3 normalTan) {
    return normalize(tbnMat() * normalTan);
}

float displacement(vec2 texCoord) {
    return 1.0 - texture2D(uDispTex, texCoord).r;
}

vec2 displacedTexCoord(vec3 cameraDir) {
    float depthStep = pow(2.0, -uDispQuality) * max(dot(vec3(0.0, 0.0, 1.0), cameraDir), 0.01);
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

float normalDistribution(vec3 normal, vec3 halfVector, float roughness) {
    float coefficient = pow(roughness, 4.0);
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

vec3 specularFactor(vec3 diffColor, float metalness, vec3 cameraDir, vec3 halfVector, float roughness) {
    vec3 baseColor = mix(vec3(0.04), diffColor, metalness);
    float exposure = min(1.0, max(dot(cameraDir, halfVector), 0.0001));
    return baseColor + (max(vec3(1.0 - roughness), baseColor) - baseColor) * pow(1.0 - exposure, 5.0);
}

vec2 envTexCoord(vec3 direction) {
    float horizontal = length(direction.xy);
    vec2 texCoord = vec2(atan(direction.x, direction.y) / tau, atan(direction.z, horizontal) / pi + 0.5);
    return texCoord;
}

void main() {
    vec3 cameraDir = normalize(vCameraPosTan - vPosTan);

    vec2 texCoord = displacedTexCoord(cameraDir);

    if (uCropTex == 1 && (texCoord.x < 0.0 || texCoord.y < 0.0 || texCoord.x > 1.0 || texCoord.y > 1.0)) {
        discard;
    }

    vec3 lightDir = normalize(vLightPosTan - vPosTan);
    vec3 halfVector = normalize(lightDir + cameraDir);

    vec3 normal = normalize(texture2D(uNormalTex, texCoord).rgb * 2.0 - 1.0);

    vec3 diffColor = texture2D(uDiffTex, texCoord).rgb * uDiffColor;
    vec3 emissionColor = texture2D(uEmissionTex, texCoord).rgb * uEmissionColor;
    vec3 radiance = texture2D(uRadianceTex, envTexCoord(normalWorld(normal))).rgb;

    float roughness = texture2D(uRoughnessTex, texCoord).r * uRoughness;
    float metalness = texture2D(uMetalnessTex, texCoord).r * uMetalness;

    float normalDistribution = normalDistribution(normal, halfVector, roughness);
    float geometry = geometry(normal, cameraDir, lightDir, roughness);
    vec3 specularFactor = specularFactor(diffColor, metalness, cameraDir, normal, roughness);

    vec2 reflectTexCoord = envTexCoord(normalWorld(reflect(-cameraDir, normal)));
    float sharpness = pow(roughness, 0.5);
    vec3 reflection = texture2D(uReflectionTex, reflectTexCoord).rgb * (1.0 - sharpness) +
            texture2D(uRadianceTex, reflectTexCoord).rgb * sharpness;
    vec3 specular = specularFactor * reflection;

    vec3 diffuseFactor = (vec3(1.0) - specularFactor) * (1.0 - metalness);

    vec3 ambColor = radiance * diffColor * diffuseFactor;

    float exposure = max(dot(normal, lightDir), 0.0);

    vec3 color = diffuseFactor * diffColor / pi * exposure + ambColor + specular + emissionColor;

    gl_FragColor = vec4(color, 1.0);
}