uniform vec4 uColor;
uniform sampler2D uTexture;

varying vec2 vTexCoord;

void main() {
    gl_FragColor = vec4(texture2D(uTexture, vTexCoord).rgb * uColor.rgb, uColor.a);
}
