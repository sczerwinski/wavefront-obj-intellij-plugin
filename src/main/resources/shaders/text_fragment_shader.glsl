uniform vec4 uColor;
uniform sampler2D uTexture;

varying vec2 vTexCoord;

void main() {
    float alpha = texture2D(uTexture, vTexCoord).r;
    gl_FragColor = vec4(uColor.xyz, uColor.a * alpha);
}
