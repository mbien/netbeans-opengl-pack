varying vec4 ModelPosition;
varying vec3 ecPosition;
varying vec3 Normal;
varying vec4 Color;
varying vec4 Position;
varying vec3 lightVec;
varying vec3 reflectVec;
varying vec3 viewVec;
    
void main()
{
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
    ecPosition = vec3(gl_ModelViewMatrix * gl_Vertex);
    Normal = normalize(gl_NormalMatrix * gl_Normal);
    lightVec = normalize(gl_LightSource[0].position.xyz - ecPosition);
    reflectVec = reflect(-lightVec, Normal);
    viewVec = normalize(-ecPosition);

    Position = gl_Position;
    ModelPosition = gl_Vertex;
    Color = gl_Color;
}
