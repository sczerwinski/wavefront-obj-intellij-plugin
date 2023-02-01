#version 100

#ifdef GL_FRAGMENT_PRECISION_HIGH
#define FLOAT_PRECISION highp
#else
#define FLOAT_PRECISION mediump
#endif

precision FLOAT_PRECISION float;

uniform int uSamples;

varying vec2 vPos;
varying vec2 vTexCoord;

const float pi = 3.14159265358979323846;
const float half_pi = pi / 2.0;
const float tau = 2.0 * pi;

vec3 roughnessDirection(float phi, float rawTheta, mat3 tbnMat, float coeficient) {
    float cosTheta = sqrt((1.0 - rawTheta) / (1.0 + (coeficient - 1.0) * rawTheta));
    float sinTheta = sqrt(1.0 - cosTheta * cosTheta);

    vec3 direction = vec3(cos(phi) * sinTheta, sin(phi) * sinTheta, cosTheta);

    return normalize(tbnMat * direction);
}

float geometryFactor(vec3 normal, vec3 direction, float roughness) {
    float parameter = roughness * roughness / 2.0;
    float exposure = max(0.0, dot(normal, direction));
    return exposure / (exposure * (1.0 - parameter) + parameter);
}

float geometry(vec3 normal, vec3 cameraDir, vec3 envDir, float roughness) {
    return geometryFactor(normal, cameraDir, roughness) * geometryFactor(normal, envDir, roughness);
}

void main() {
    float roughness = vTexCoord.y;
    float coeficient = pow(roughness, 4.0);

    float cosAngle = vTexCoord.x;
    float sinAngle = sqrt(1.0 - cosAngle * cosAngle);
    vec3 cameraDir = vec3(sinAngle, 0.0, cosAngle);

    mat3 tbnMat = mat3(1.0);
    vec3 normal = vec3(0.0, 0.0, 1.0);

    float samples = float(uSamples);
    float thetaStep = half_pi / samples;
    float phiStep = tau / samples;

    float scale = 0.0;
    float bias = 0.0;

    for (float theta = 0.0001; theta < half_pi; theta += thetaStep) {
        for (float phi = 0.0001; phi < tau; phi += phiStep) {
            vec3 direction = roughnessDirection(phi, theta / half_pi, tbnMat, coeficient);
            vec3 envDir = normalize(2.0 * dot(cameraDir, direction) * direction - cameraDir);

            float cosDir = max(0.0, direction.z);
            float cosEnvDir = max(0.0, envDir.z);
            float cosCameraDir = max(0.0, dot(cameraDir, direction));

            if (cosEnvDir > 0.0) {
                float geometry = geometry(normal, cameraDir, envDir, roughness);
                float geometryFactor = (geometry * cosCameraDir) / max(0.0001, (cosDir * cosAngle));
                float cameraDirFactor = pow(1.0 - cosCameraDir, 5.0);

                scale += (1.0 - cameraDirFactor) * geometryFactor;
                bias += cameraDirFactor * geometryFactor;
            }
        }
    }

    gl_FragColor = vec4(scale / samples / samples, bias / samples / samples, 0.0, 1.0);
}
