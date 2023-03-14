uniform vec3 uColor;

varying vec3 vNormal;
varying vec3 vLightDir;

void main() {
    float exposure = max(dot(vNormal, vLightDir), 0.0) * 0.7 + 0.3;

    gl_FragColor = vec4(uColor * exposure, 1.0);
}
