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
    float angle = pow(1.0 - length(dot(Normal, vec3(0.0, 0.0, 1.0))), 2.0);
    float diffuse = max(dot(normalize(gl_LightSource[0].position.xyz), Normal.xyz), 0.0);
    float spec = max(dot(reflectVec, viewVec), 0.0);
    spec = pow(spec, gl_FrontMaterial.shininess);
    float intensity = diffuse / pow(distance(ModelPosition, gl_LightSource[0].position), 0.25);

    ModelPosition *= 5.0;
    vec3 tmp = floor(ModelPosition.xyz);
    vec3 DiffColor = pow((ModelPosition - tmp - 0.5) * 2.0, 2.0);
    DiffColor = pow((DiffColor - 0.5) * 2.0, 2.0);
    gl_FragColor = vec4(pow(DiffColor*1.1, 8.0), 1.0) * intensity + spec;
}
