uniform sampler2D uEnvTexture;

varying vec3 vPos;

const float pi = 3.14159265358979323846;
const float tau = 2.0 * pi;

vec2 envTexCoord(vec3 direction) {
    float horizontal = length(direction.xy);
    vec2 texCoord = vec2(atan(direction.x, direction.y) / tau, atan(direction.z, horizontal) / pi + 0.5);
    return texCoord;
}

void main() {
    vec3 direction = normalize(vPos);
    vec2 texCoord = envTexCoord(direction);
    gl_FragColor = vec4(texture2D(uEnvTexture, texCoord).rgb, 1.0);
}
