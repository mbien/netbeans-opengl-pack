uniform sampler2D Noise;
uniform float noiseStrength;
uniform vec4 diffuseColor;

varying vec4 ModelPosition;
varying vec4 ecPosition;
varying vec3 Normal;
varying vec4 Color;
varying vec4 Position;
varying vec3 lightVec;
varying vec3 reflectVec;
varying vec3 viewVec;

void main()
{
    vec4 noiseVec = ModelPosition * 500.0;
    gl_FragColor = vec4(0.1, 0.1, 1.0, 1.0) + texture2D(Noise, vec2(noiseVec.g + noiseVec.r, noiseVec.b + noiseVec.a));
}
