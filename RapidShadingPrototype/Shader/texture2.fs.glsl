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
    vec4 noiseVec1 = texture2D(Noise, vec2(noiseVec.z, 0.5129374838974));
    vec4 noiseVec2 = texture2D(Noise, vec2(noiseVec.y + noiseVec1.r, noiseVec.b + noiseVec1.a));
    vec4 noiseVec3 = texture2D(Noise, vec2(noiseVec.x + noiseVec.g, noiseVec2.r + noiseVec1.b));
    noiseVec = texture2D(Noise, vec2(noiseVec3.r + noiseVec2.g + noiseVec3.b + noiseVec2.a, noiseVec2.r  +noiseVec3.g + noiseVec2.b + noiseVec3.a));
    noiseVec = (noiseVec + noiseVec3) / 2.0;

    //noiseVec = noiseVec3;
    vec3 NoiseNormal = normalize(Normal + vec3((noiseVec - 0.5) * noiseStrength));
    vec3 NoiseNormalLight = normalize(Normal + vec3((noiseVec - 0.5) * noiseStrength * 0.5));
    
    //float angle = pow(1.0 - length(dot(NoiseNormal, vec3(0.0, 0.0, 1.0))), 2.0);
    float diffuse = max(dot(normalize(gl_LightSource[0].position.xyz), NoiseNormalLight.xyz), 0.0);
    
    vec3 reflectVec2 = reflect(-lightVec, Normal);
    float spec2 = max(dot(reflectVec2, viewVec), 0.0);
    spec2 = pow(spec2, 48.0);
    
    reflectVec2 = reflect(-lightVec, NoiseNormal);
    float spec = max(dot(reflectVec2, viewVec), 0.0);
    spec = pow(spec, 2.0);
    
    spec = (spec + spec2) / 2.0;
    float intensity = diffuse / pow(distance(ModelPosition, gl_LightSource[0].position), 0.25);
    intensity = pow(intensity, 1.5);

    // Lines
    vec4 ModelPosition2 = ModelPosition * 5.0;
    vec3 tmp = floor(ModelPosition2.xyz);
    vec3 DiffColor = pow((ModelPosition2.xyz - tmp - 0.5) * 2.0, vec3(2.0, 2.0, 2.0));
    DiffColor = pow((DiffColor - 0.5) * 2.0, vec3(2.0, 2.0, 2.0));
    
    //gl_FragColor = vec4(pow(DiffColor*1.2, 8.0), 1.0) * intensity + spec;
    gl_FragColor = diffuseColor * intensity + vec4(0.7, 0.9, 1.0, 1.0) * spec;
}
