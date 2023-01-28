#version 100

#ifdef GL_FRAGMENT_PRECISION_HIGH
#define FLOAT_PRECISION highp
#else
#define FLOAT_PRECISION mediump
#endif

precision FLOAT_PRECISION float;

uniform sampler2D uEnvTex;
uniform int uSamples;

varying vec2 vPos;
varying vec2 vTexCoord;

const float pi = 3.14159265358979323846;
const float half_pi = pi / 2.0;
const float tau = 2.0 * pi;

vec3 sphericalToVec3(float lon, float lat) {
    return vec3(cos(lon) * sin(lat), sin(lon) * sin(lat), cos(lat));
}

mat3 tbnMat3(float lon, float lat) {
    vec3 normal = sphericalToVec3(lon, lat);
    vec3 tangent = sphericalToVec3(lon + half_pi, half_pi);
    vec3 bitangent = normalize(cross(normal, tangent));
    return mat3(tangent, bitangent, normal);
}

vec2 vec3ToSpherical(vec3 vector) {
    float axisR = length(vector.xy);
    return vec2(atan(vector.y, vector.x), atan(axisR, vector.z));
}

void main() {
    float lon = vTexCoord.x * tau;
    float lat = (1.0 - vTexCoord.y) * pi;

    mat3 tbnMat = tbnMat3(lon, lat);

    float samples = float(uSamples);
    float thetaStep = half_pi / samples;
    float phiStep = tau / samples;

    vec3 color = vec3(0.0);
    for (float theta = 0.0; theta < half_pi; theta += thetaStep) {
        for (float phi = 0.0; phi < tau; phi += phiStep) {
            vec3 envDir = tbnMat * sphericalToVec3(phi, theta);
            vec2 envDirSpherical = vec3ToSpherical(envDir);
            vec2 envTexCoord = vec2(envDirSpherical.x / tau, envDirSpherical.y / pi);
            color += texture2D(uEnvTex, envTexCoord).rgb * cos(theta) * sin(theta);
        }
    }

    gl_FragColor = vec4(color * pi / samples / samples, 1.0);
}
